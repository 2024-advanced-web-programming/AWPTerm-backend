package awpterm.backend.service;

import awpterm.backend.api.request.board.*;
import awpterm.backend.api.response.board.BoardResponseDTO;
import awpterm.backend.domain.Board;
import awpterm.backend.domain.Club;
import awpterm.backend.domain.Member;
import awpterm.backend.enums.BoardType;
import awpterm.backend.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardResponseDTO saveAllTypeBoard(Member loginMember, BoardAddAllTypeRequestDTO boardAddAllTypeRequestDTO, Club club) {
        return BoardResponseDTO.valueOf(boardRepository.save(createBoard(loginMember, boardAddAllTypeRequestDTO, club)));
    }

    public BoardResponseDTO savePhotoBoard(Member loginMember, BoardAddPhotoRequestDTO boardAddPhotoRequestDTO, Club club) {
        return BoardResponseDTO.valueOf(boardRepository.save(createBoard(loginMember, boardAddPhotoRequestDTO, club)));
    }

    public BoardResponseDTO saveVideoBoard(Member loginMember, BoardAddVideoRequestDTO boardAddVideoRequestDTO, Club club) {
        return BoardResponseDTO.valueOf(boardRepository.save(createBoard(loginMember, boardAddVideoRequestDTO, club)));
    }

    public BoardResponseDTO saveRecruitmentBoard(Member loginMember, BoardAddRecruitmentRequestDTO boardAddRecruitmentRequestDTO, Club club) {
        return BoardResponseDTO.valueOf(boardRepository.save(boardRepository.save(createBoard(loginMember, boardAddRecruitmentRequestDTO, club))));
    }

    public BoardResponseDTO saveNoticeBoard(Member loginMember, BoardAddNoticeRequestDTO boardAddNoticeRequestDTO, Club club) {
        return BoardResponseDTO.valueOf(boardRepository.save(boardRepository.save(createBoard(loginMember, boardAddNoticeRequestDTO, club))));
    }

    public List<BoardResponseDTO> findAllByBoardType(BoardType boardType) {
        return boardRepository.findAllByBoardTypeOrderByTimestampDesc(boardType);
    }

    public BoardResponseDTO findByBoardId(Long boardId) {
        return BoardResponseDTO.valueOf(Objects.requireNonNull(boardRepository.findById(boardId).orElse(null)));
    }
    private Board createBoard(Member loginMember, BoardRequestDTO boardDTO, Club club) {
        Board board = boardDTO.toEntity();
        board.setClub(club);
        board.setWriter(loginMember);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        board.setTimestamp(LocalDateTime.now().format(formatter));

        return board;
    }
}
