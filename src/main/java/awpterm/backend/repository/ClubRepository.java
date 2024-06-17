package awpterm.backend.repository;

import awpterm.backend.domain.Club;
import awpterm.backend.domain.Member;
import awpterm.backend.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long> {
    Club findByName(String name);
    List<Club> findByStatus(Status status);
    List<Club> findByCreatedBy(Member createdBy);
    @Query("SELECT c FROM Club c")
    List<Club> findByPresident(Member president);
    //TODO @Query("SELECT c FROM Club c WHERE c.status = '승인'")로 바꿔야 함 현재는 테스트이므로 전체 SELECT
}
