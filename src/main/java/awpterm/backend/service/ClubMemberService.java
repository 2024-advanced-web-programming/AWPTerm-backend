package awpterm.backend.service;

import awpterm.backend.domain.Club;
import awpterm.backend.domain.ClubMember;
import awpterm.backend.domain.Member;
import awpterm.backend.repository.ClubMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClubMemberService {
    private final ClubMemberRepository clubMemberRepository;

    public ClubMember findByClubAndMember(Club club, Member member) {
        return clubMemberRepository.findByClubAndMember(club, member);
    }

    public void delete(ClubMember clubMember) {
        clubMemberRepository.delete(clubMember);
    }

    public void save(ClubMember clubMember) {
        clubMemberRepository.save(clubMember);
    }
}
