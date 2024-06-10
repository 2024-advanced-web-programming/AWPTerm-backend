package awpterm.backend.repository;

import awpterm.backend.domain.Club;
import awpterm.backend.domain.ClubApplicant;
import awpterm.backend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubApplicantRepository extends JpaRepository<ClubApplicant, Long> {
    ClubApplicant findByClubAndApplicant(Club club, Member Applicant);
    List<ClubApplicant> findByApplicant(Member Applicant);
}
