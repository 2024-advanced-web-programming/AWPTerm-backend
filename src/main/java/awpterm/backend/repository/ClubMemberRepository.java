package awpterm.backend.repository;

import awpterm.backend.domain.Club;
import awpterm.backend.domain.ClubMember;
import awpterm.backend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
    ClubMember findByClubAndMember(Club club, Member member);
    @Query("SELECT cm FROM ClubMember cm JOIN FETCH cm.club WHERE cm.member = :member AND cm.club.status = '승인'")
    List<ClubMember> findByMember(Member member);
}
