package awpterm.backend.service;

import awpterm.backend.api.request.club.*;
import awpterm.backend.api.response.club.*;
import awpterm.backend.domain.*;
import awpterm.backend.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    public ClubApplicationResponseDTO apply(Member loginMember,
                                            ClubApplicationRequestDTO clubApplicationRequestDTO,
                                            MultipartFile file) {
        Club club = clubService.findById(clubApplicationRequestDTO.getClubId());
        List<ClubMember> clubMembers = club.getMembers();
        for (ClubMember cm : clubMembers) {
            if (cm.getMember().getId().equals(loginMember.getId())) {
                throw new RuntimeException("이미 가입된 동아리입니다.");
            }
        }

        FileProperty fileProperty = filePropertyService.storeFile(file);
        ClubApplicant clubApplicant = ClubApplicant.builder()
                                        .club(club)
                                        .applicant(loginMember)
                                        .applicationForm(fileProperty)
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


    public void clubMemberDelete(Long clubId, String memberId) throws RuntimeException {
        Club club = clubService.findById(clubId);
        Member member = memberService.findById(memberId);
        ClubMember clubMember = clubMemberService.findByClubAndMember(club, member);

        Member president = club.getPresident();
        if (president.equals(member)) {
            throw new RuntimeException("회장은 탈퇴시킬 수 없습니다.");
        }

        Member vicePresident = club.getVicePresident();
        if (vicePresident != null && vicePresident.equals(member)) {
            club.setVicePresident(null);
        }

        Member secretary = club.getSecretary();
        if (secretary != null && secretary.equals(member)) {
            club.setSecretary(null);
        }

        clubService.save(club);
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
        ClubDetail clubDetail = club.getClubDetail() != null ? club.getClubDetail() : ClubDetail.builder().build();
        FileProperty registerFile;
        FileProperty representativePicture;

        clubDetail.setIntroduction(clubUpdateBasicInfoRequestDTO.getIntroduction());
        clubDetail.setRegularMeetingTime(clubUpdateBasicInfoRequestDTO.getRegularMeetingTime());

        if (applicationForm != null) {
            registerFile = filePropertyService.storeFile(applicationForm);
            clubDetail.setRegisterFile(registerFile);
        }

        if (clubPhoto != null) {
            representativePicture = filePropertyService.storeFile(clubPhoto);
            clubDetail.setRepresentativePicture(representativePicture);
        }

        Member president = memberService.findById(clubUpdateBasicInfoRequestDTO.getPresidentId());
        Member vicePresident = memberService.findById(clubUpdateBasicInfoRequestDTO.getVicePresidentId());
        Member secretary = memberService.findById(clubUpdateBasicInfoRequestDTO.getSecretaryId());

        club.setName(clubUpdateBasicInfoRequestDTO.getClubName());
        club.setClubDetail(clubDetail);
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

    public List<ClubMemberResponseDTO> getMyClubList(Member member) {
        List<ClubMember> clubMembers = clubMemberService.findByMember(member);
        return ClubMemberResponseDTO.valueOf(member, clubMembers);
    }
}