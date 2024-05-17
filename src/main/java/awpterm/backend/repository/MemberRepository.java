package awpterm.backend.repository;

import awpterm.backend.domain.Member;
import awpterm.backend.enums.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, String> {
    public List<Member> findByPosition(Position position);
}
