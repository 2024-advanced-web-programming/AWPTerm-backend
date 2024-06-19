package awpterm.backend.repository;

import awpterm.backend.api.response.board.BoardResponseDTO;
import awpterm.backend.domain.Board;
import awpterm.backend.enums.BoardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    List<BoardResponseDTO> findAllByBoardTypeOrderByTimestampDesc(BoardType boardType);
}
