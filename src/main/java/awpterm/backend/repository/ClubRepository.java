package awpterm.backend.repository;

import awpterm.backend.domain.Club;
import awpterm.backend.domain.Member;
import awpterm.backend.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long> {
    List<Club> findByStatus(Status status);
    List<Club> findByCreatedBy(Member createdBy);
    @Query("SELECT c FROM Club c WHERE c.president = :president AND c.status = '승인'")
    List<Club> findByPresident(Member president);
}
