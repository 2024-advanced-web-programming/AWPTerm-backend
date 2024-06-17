package awpterm.backend.service;

import awpterm.backend.api.request.board.*;
import awpterm.backend.api.response.board.BoardResponseDTO;
import awpterm.backend.domain.Board;
import awpterm.backend.domain.FileProperty;
import awpterm.backend.domain.Member;
import awpterm.backend.enums.BoardType;
import awpterm.backend.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardResponseDTO saveAllTypeBoard(Member loginMember, BoardAddAllTypeRequestDTO boardAddAllTypeRequestDTO) {
        Board board = boardAddAllTypeRequestDTO.toEntity();
        board.setWriter(loginMember);
        return BoardResponseDTO.valueOf(boardRepository.save(board));
    }

    public BoardResponseDTO savePhotoBoard(Member loginMember, BoardAddPhotoRequestDTO boardAddPhotoRequestDTO) {
        Board board = boardAddPhotoRequestDTO.toEntity();
        board.setWriter(loginMember);
        return BoardResponseDTO.valueOf(boardRepository.save(board));
    }

    public BoardResponseDTO saveVideoBoard(Member loginMember, BoardAddVideoRequestDTO boardAddVideoRequestDTO) {
        Board board = boardAddVideoRequestDTO.toEntity();
        board.setWriter(loginMember);
        return BoardResponseDTO.valueOf(boardRepository.save(board));
    }

    public BoardResponseDTO saveRecruitmentBoard(Member loginMember, BoardAddRecruitmentRequestDTO boardAddRecruitmentRequestDTO) {
        Board board = boardAddRecruitmentRequestDTO.toEntity();
        board.setWriter(loginMember);
        return BoardResponseDTO.valueOf(boardRepository.save(board));
    }

    public BoardResponseDTO saveNoticeBoard(Member loginMember, BoardAddNoticeRequestDTO boardAddNoticeRequestDTO) {
        Board board = boardAddNoticeRequestDTO.toEntity();
        board.setWriter(loginMember);
        return BoardResponseDTO.valueOf(boardRepository.save(board));
    }

    public List<BoardResponseDTO> findAllByBoardType(BoardType boardType) {
        return boardRepository.findAllByBoardType(boardType);
    }

    public Optional<Board> findByBoardId(Long boardId) {
        return boardRepository.findById(boardId);
    }

    public boolean updateByDTO(BoardUpdateRequestDTO requestDTO) {
        Board board = findByBoardId(requestDTO.getBoardId()).orElse(null);
        if(board == null) {
            return false;
        }

        board.setTitle(requestDTO.getTitle());
        board.setBoardType(requestDTO.getBoardType());
        board.setContent(requestDTO.getContent());
        board.setClub(requestDTO.getClub());
        if(requestDTO.getBoardType() == BoardType.활동_영상) {
            board.setVideoURL(requestDTO.getVideoURL());
        }
        return true;
    }
}
