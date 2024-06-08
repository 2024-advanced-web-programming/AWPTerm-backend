package awpterm.backend.service;

import awpterm.backend.api.request.member.MemberLoginRequestDTO;
import awpterm.backend.api.request.member.MemberRegisterRequestDTO;
import awpterm.backend.api.response.member.MemberResponseDTO;
import awpterm.backend.domain.Member;
import awpterm.backend.enums.Position;
import awpterm.backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberResponseDTO register(MemberRegisterRequestDTO memberRegisterRequestDTO) {
        Member member = memberRegisterRequestDTO.toEntity();
        memberRepository.save(member);
        return MemberResponseDTO.of(member);
    }

    public boolean isValidLoginRequest(MemberLoginRequestDTO memberLoginRequestDTO) {
        Member member = memberLoginRequestDTO.toEntity();
        Member findMember = memberRepository.findById(member.getId()).orElse(null);
        return findMember != null && findMember.isSame(member);
    }

    public Member findById(String id) {
        return memberRepository.findById(id).orElse(null);
    }

    public Member findByCode(String code) {
        return memberRepository.findByCode(code);
    }

    public boolean isValidMemberById(String memberId) {
        return memberRepository.findById(memberId).orElse(null) != null;
    }

    public boolean isValidMemberByCode(String memberCode) {
        return memberRepository.findByCode(memberCode) != null;
    }

    public List<MemberResponseDTO> findByPosition(Position position) {
        List<Member> members = memberRepository.findByPosition(position);
        return members.stream().map(MemberResponseDTO::of).toList();
    }
}
