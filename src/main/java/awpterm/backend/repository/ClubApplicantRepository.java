package awpterm.backend.repository;

import awpterm.backend.domain.Club;
import awpterm.backend.domain.ClubApplicant;
import awpterm.backend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubApplicantRepository extends JpaRepository<ClubApplicant, Long> {
    ClubApplicant findByClubAndApplicant(Club club, Member Applicant);
}
