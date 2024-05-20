package awpterm.backend.repository;

import awpterm.backend.domain.Club;
import awpterm.backend.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long> {
    List<Club> findByStatus(Status status);
}
