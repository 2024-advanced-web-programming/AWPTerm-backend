package awpterm.backend.service;

import awpterm.backend.api.request.member.MemberLoginRequestDTO;
import awpterm.backend.api.request.member.MemberRegisterRequestDTO;
import awpterm.backend.api.response.club.ClubApplicantResponseDTO;
import awpterm.backend.api.response.club.ClubResponseDTO;
import awpterm.backend.api.response.member.MemberResponseDTO;
import awpterm.backend.domain.Member;
import awpterm.backend.enums.Position;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceFacade {
    private final MemberService memberService;
    private final ClubService clubService;
    private final ClubMemberService clubMemberService;
    private final ClubApplicantService clubApplicantService;

    public List<ClubApplicantResponseDTO> findClubApplicantByMember(Member member) {
        return clubApplicantService.findByMember(member);
    }

    public MemberResponseDTO register(MemberRegisterRequestDTO memberRegisterRequestDTO) {
        return memberService.register(memberRegisterRequestDTO);
    }

    public boolean isValidLoginRequest(MemberLoginRequestDTO memberLoginRequestDTO) {
        return memberService.isValidLoginRequest(memberLoginRequestDTO);
    }

    public Member findById(String id) {
        return memberService.findById(id);
    }

    public Member findByCode(String code) {
        return memberService.findByCode(code);
    }

    public boolean isValidMemberById(String memberId) {
        return memberService.isValidMemberById(memberId);
    }

    public boolean isValidMemberByCode(String memberCode) {
        return memberService.isValidMemberByCode(memberCode);
    }

    public List<MemberResponseDTO> findByPosition(Position position) {
        return memberService.findByPosition(position);
    }

    public List<ClubResponseDTO> findClubByMember(Member loginMember) {
        return clubMemberService.findByMember(loginMember);
    }

    public List<ClubResponseDTO> findClubByCreatedBy(Member member) {
        return clubService.findClubByCreatedBy(member);
    }
}
