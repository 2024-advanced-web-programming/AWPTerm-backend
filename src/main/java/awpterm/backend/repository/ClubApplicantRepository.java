package awpterm.backend.repository;

import awpterm.backend.domain.Club;
import awpterm.backend.domain.ClubApplicant;
import awpterm.backend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClubApplicantRepository extends JpaRepository<ClubApplicant, Long> {
    ClubApplicant findByClubAndApplicant(Club club, Member Applicant);
    @Query("SELECT ca FROM ClubApplicant ca JOIN FETCH ca.club JOIN FETCH ca.applicant JOIN FETCH ca.applicationForm")
    List<ClubApplicant> findByApplicant(Member Applicant);
}
