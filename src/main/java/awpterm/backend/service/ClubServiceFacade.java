package awpterm.backend.service;

import awpterm.backend.api.request.club.ClubApplicationDecisionDTO;
import awpterm.backend.api.request.club.ClubApplicationRequestDTO;
import awpterm.backend.api.request.club.ClubBasicInfoDTO;
import awpterm.backend.api.request.club.ClubRegisterRequestDTO;
import awpterm.backend.api.response.club.ClubApplicationResponseDTO;
import awpterm.backend.api.response.club.ClubResponseDTO;
import awpterm.backend.domain.*;
import awpterm.backend.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
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

    public ClubResponseDTO register(ClubRegisterRequestDTO clubRegisterRequestDTO) throws MalformedURLException {
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

    public Club findById(Long id) {
        return clubService.findById(id);
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
        Club club = findById(clubId);
        return club.getApplicants().stream().map(ClubApplicationResponseDTO::valueOf).toList();
    }

    public List<ClubApplicationResponseDTO> getApplicationList(Member member) {
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

    public Club updateBasicInfo(ClubBasicInfoDTO clubBasicInfoDTO, MultipartFile representativePicture) {
        return clubService.updateBasicInfo(clubBasicInfoDTO, representativePicture);
    }
}