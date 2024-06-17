package awpterm.backend.repository;

import awpterm.backend.domain.Club;
import awpterm.backend.domain.Member;
import awpterm.backend.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long> {
    Club findByName(String name);
    List<Club> findByStatus(Status status);
    List<Club> findByCreatedBy(Member createdBy);
}
