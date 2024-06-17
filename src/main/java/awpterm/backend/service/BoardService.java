package awpterm.backend.service;

import awpterm.backend.api.request.board.BoardAddNoticeRequestDTO;
import awpterm.backend.api.request.board.BoardAddPhotoRequestDTO;
import awpterm.backend.api.request.board.BoardAddRecruitmentRequestDTO;
import awpterm.backend.api.request.board.BoardAddVideoRequestDTO;
import awpterm.backend.api.response.board.BoardResponseDTO;
import awpterm.backend.domain.Board;
import awpterm.backend.domain.FileProperty;
import awpterm.backend.domain.Member;
import awpterm.backend.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;

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
}
