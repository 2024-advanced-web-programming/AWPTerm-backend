package awpterm.backend.service;

import awpterm.backend.api.request.club.ClubApplicationDecisionDTO;
import awpterm.backend.api.request.club.ClubApplicationRequestDTO;
import awpterm.backend.api.request.club.ClubRegisterRequestDTO;
import awpterm.backend.api.request.file.FileUploadRequestDTO;
import awpterm.backend.api.response.club.ClubApplicationResponseDTO;
import awpterm.backend.api.response.file.FileResponseDTO;
import awpterm.backend.domain.*;
import awpterm.backend.enums.FileType;
import awpterm.backend.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClubServiceFacade {
    private final ClubService clubService;
    private final ClubMemberService clubMemberService;
    private final ClubApplicantService clubApplicantService;
    private final MemberService memberService;
    private final FileService fileService;

    public boolean isValidMemberById(String memberId) {
        return memberService.isValidMemberById(memberId);
    }

    public boolean isValidMemberByCode(String memberCode) {
        return memberService.isValidMemberByCode(memberCode);
    }

    public Club register(ClubRegisterRequestDTO clubRegisterRequestDTO) {
        Club club = clubRegisterRequestDTO.toEntity();
        Member president = memberService.findByCode(clubRegisterRequestDTO.getRequestorCode());
        Member supervisor = memberService.findByCode(clubRegisterRequestDTO.getSupervisorCode());
        ClubMaster clubMaster = ClubMaster.builder().club(club).master(president).build();

        club.setPresident(president);
        club.setSupervisor(supervisor);
        club.getMasters().add(clubMaster);
        club.setStatus(Status.검토);

        return clubService.register(club);
    }

    public Club findById(Long id) {
        return clubService.findById(id);
    }

    public ClubApplicationResponseDTO apply(Member loginMember, ClubApplicationRequestDTO clubApplicationRequestDTO) {
        Club club = clubService.findById(clubApplicationRequestDTO.getClubId());

        FileUploadRequestDTO uploadRequest = FileUploadRequestDTO.builder()
                .type(FileType.APPLICATION_FORM.toString())
                .name(clubApplicationRequestDTO.getFileName())
                .content(clubApplicationRequestDTO.getFileContent())
                .build();
        FileResponseDTO fileResponse = fileService.upload(loginMember, uploadRequest);
        File applicantForm = File.builder()
                .name(fileResponse.getName())
                .type(FileType.APPLICATION_FORM)
                .content(fileResponse.getContent())
                .uploader(loginMember)
                .build();

        clubApplicantService.save(
                ClubApplicant.builder()
                        .club(club)
                        .applicant(loginMember)
                        .applicationForm(applicantForm)
                        .build()
        );

        return ClubApplicationResponseDTO.builder()
                .code(loginMember.getCode())
                .name(loginMember.getName())
                .applicationForm(applicantForm)
                .build();
    }

    public List<ClubApplicationResponseDTO> getApplicationList(Long clubId) {
        Club club = clubService.findById(clubId);
        List<ClubApplicant> clubApplicants = club.getApplicants();
        List<ClubApplicationResponseDTO> response = new ArrayList<>();

        for (ClubApplicant a : clubApplicants) {
            response.add(ClubApplicationResponseDTO.builder()
                    .code(a.getApplicant().getCode())
                    .name(a.getApplicant().getName())
                    .applicationForm(a.getApplicationForm())
                    .build());
        }

        return response;
    }


    public void clubMemberDelete(Long clubId, Long memberId) {
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
}