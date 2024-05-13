package awpterm.backend.service;

import awpterm.backend.api.request.MemberLoginRequestDTO;
import awpterm.backend.api.request.MemberRegisterRequestDTO;
import awpterm.backend.domain.Member;
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

    public List<Member> register(MemberRegisterRequestDTO memberRegisterRequestDTO) {
        Member member = memberRegisterRequestDTO.toEntity();
        memberRepository.save(member);
        return memberRepository.findAll();
    }

    public boolean login(MemberLoginRequestDTO memberLoginRequestDTO) {
        Member member = memberLoginRequestDTO.toEntity();
        Member findMember = memberRepository.findById(member.getId()).orElse(null);
        return findMember != null && findMember.isSame(member);
    }
    public Member findById(String id) {
        return memberRepository.findById(id).orElse(null);
    }
}
