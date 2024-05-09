package awpterm.backend.service;

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

    public List<Member> register(Member member) {
        memberRepository.save(member);
        return memberRepository.findAll();
    }

    public boolean login(Member member) {
        Member findMember = memberRepository.findById(member.getId()).orElse(null);
        return findMember != null && findMember.isSame(member);
    }
}
