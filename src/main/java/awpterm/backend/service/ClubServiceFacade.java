package awpterm.backend.service;

import awpterm.backend.api.request.club.*;
import awpterm.backend.api.response.club.ClubApplicationResponseDTO;
import awpterm.backend.api.response.club.ClubInquiryBasicInfoDTO;
import awpterm.backend.api.response.club.ClubResponseDTO;
import awpterm.backend.api.response.club.RegisterListResponseDTO;
import awpterm.backend.domain.*;
import awpterm.backend.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClubServiceFacade {
    private final ClubService clubService;
    private final ClubMemberService clubMemberService;
    private final ClubMasterService clubMasterService;
    private final ClubApplicantService clubApplicantService;
    private final MemberService memberService;
    private final FilePropertyService filePropertyService;

    public boolean isValidMemberById(String memberId) {
        return memberService.isValidMemberById(memberId);
    }

    public boolean isValidMemberByCode(String memberCode) {
        return memberService.isValidMemberByCode(memberCode);
    }

    public ClubResponseDTO register(ClubRegisterRequestDTO clubRegisterRequestDTO) {
        Club club = clubRegisterRequestDTO.toEntity();
        Member president = memberService.findByCode(clubRegisterRequestDTO.getRequestorCode());
        Member supervisor = memberService.findByCode(clubRegisterRequestDTO.getSupervisorCode());
        ClubMaster clubMaster = ClubMaster.builder().club(club).master(president).build();
        ClubMember clubMember = ClubMember.builder().club(club).member(president).build();

        club.setPresident(president);
        club.setSupervisor(supervisor);
        club.getMasters().add(clubMaster);
        club.setStatus(Status.검토);
        club.setCreatedBy(president);

        clubMasterService.save(clubMaster);
        clubMemberService.save(clubMember);
        return clubService.register(club);
    }

    public ClubResponseDTO findById(Long id) {
        return ClubResponseDTO.valueOf(clubService.findById(id));
    }

    public ClubApplicationResponseDTO apply(Member loginMember, ClubApplicationRequestDTO clubApplicationRequestDTO) throws IOException {
        Club club = clubService.findById(clubApplicationRequestDTO.getClubId());
        filePropertyService.storeFile(clubApplicationRequestDTO.getMultipartFile());

        FileProperty file = FileProperty.valueOf(clubApplicationRequestDTO.getMultipartFile());
        ClubApplicant clubApplicant = ClubApplicant.builder()
                                        .club(club)
                                        .applicant(loginMember)
                                        .applicationForm(file)
                                        .build();
        clubApplicantService.save(clubApplicant);

        return ClubApplicationResponseDTO.valueOf(clubApplicant);
    }

    public List<ClubApplicationResponseDTO> getApplicationList(Long clubId) {
        Club club = clubService.findById(clubId);
        return club.getApplicants().stream().map(ClubApplicationResponseDTO::valueOf).toList();
    }

    public List<ClubApplicationResponseDTO> getApplicationList(Member member) {
        //Proxy 정보 초기화
        member = memberService.findById(member.getId());
        return member.getApplicants().stream().map(ClubApplicationResponseDTO::valueOf).toList();
    }


    public void clubMemberDelete(Long clubId, String memberId) {
        Club club = clubService.findById(clubId);
        Member member = memberService.findById(String.valueOf(memberId));
        ClubMember clubMember = clubMemberService.findByClubAndMember(club, member);
        clubMemberService.delete(clubMember);
    }

    public void applicationDecision(ClubApplicationDecisionDTO clubApplicationDecisionDTO) {
        Club club = clubService.findById(clubApplicationDecisionDTO.getClubId());
        Member applicant = memberService.findById(String.valueOf(clubApplicationDecisionDTO.getMemberId()));
        // 승인 거절 모두 ClubApplicant에서는 삭제
        clubApplicantService.delete(clubApplicantService.findByClubAndApplicant(club, applicant));

        // 승인이면 ClubMember에 추가
        if (clubApplicationDecisionDTO.getIsApproval()) {
            clubMemberService.save(ClubMember.builder()
                    .club(club)
                    .member(applicant)
                    .build());
        }
    }
    public boolean updateStatus(Long id, String status, String rejectReason) {
        return clubService.updateStatus(id, status, rejectReason);
    }

    public List<ClubResponseDTO> findAll() {
        return clubService.findAll();
    }

    public List<ClubResponseDTO> findByStatus(Status status) {
        return clubService.findByStatus(status).stream().map(ClubResponseDTO::valueOf).toList();
    }

    public void updateBasicInfo(Long clubId,
                                ClubUpdateBasicInfoRequestDTO clubUpdateBasicInfoRequestDTO,
                                MultipartFile applicationForm,
                                MultipartFile clubPhoto) {
        Club club = clubService.findById(clubId);
        FileProperty registerFile = filePropertyService.storeFile(applicationForm);
        FileProperty representativePicture = filePropertyService.storeFile(clubPhoto);

        ClubDetail clubDetail = ClubDetail.builder()
                                .introduction(clubUpdateBasicInfoRequestDTO.getIntroduction())
                                .registerFile(registerFile)
                                .representativePicture(representativePicture)
                                .regularMeetingTime(clubUpdateBasicInfoRequestDTO.getRegularMeetingTime()).build();



        Member president = memberService.findById(clubUpdateBasicInfoRequestDTO.getPresidentId());
        Member vicePresident = memberService.findById(clubUpdateBasicInfoRequestDTO.getVicePresidentId());
        Member secretary = memberService.findById(clubUpdateBasicInfoRequestDTO.getSecretaryId());

        club.setClubDetail(clubDetail);
        club.setPresident(president);
        club.setVicePresident(vicePresident);
        club.setSecretary(secretary);

        clubService.updateBasicInfo(club);
    }
    public ClubInquiryBasicInfoDTO getClubInfo(Long clubId) {
        Club club = clubService.findById(clubId);
        return ClubInquiryBasicInfoDTO.of(club);
    }

    public List<RegisterListResponseDTO> getRegisterList() {
        return clubService.findByStatus(Status.검토).stream().map(RegisterListResponseDTO::valueOf).toList();
    }
}