package awpterm.backend.service;

import awpterm.backend.api.request.board.*;
import awpterm.backend.api.response.board.BoardResponseDTO;
import awpterm.backend.domain.Board;
import awpterm.backend.domain.FileProperty;
import awpterm.backend.domain.Member;
import awpterm.backend.enums.BoardType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardServiceFacade {
    private final BoardService boardService;
    private final FilePropertyService filePropertyService;

    public BoardResponseDTO saveAllTypeBoard(Member loginMember, BoardAddAllTypeRequestDTO requestDTO) {
        return boardService.saveAllTypeBoard(loginMember, requestDTO);
    }

    public BoardResponseDTO savePhotoBoard(Member loginMember, BoardAddPhotoRequestDTO requestDTO) {
        return boardService.savePhotoBoard(loginMember, requestDTO);
    }

    public BoardResponseDTO saveVideoBoard(Member loginMember, BoardAddVideoRequestDTO requestDTO) {
        return boardService.saveVideoBoard(loginMember, requestDTO);
    }

    public BoardResponseDTO saveRecruitmentBoard(Member loginMember, BoardAddRecruitmentRequestDTO requestDTO) {
        return boardService.saveRecruitmentBoard(loginMember, requestDTO);
    }

    public BoardResponseDTO saveNoticeBoard(Member loginMember, BoardAddNoticeRequestDTO requestDTO) {
        return boardService.saveNoticeBoard(loginMember, requestDTO);
    }

    public FileProperty storeFile(MultipartFile image) {
        return filePropertyService.storeFile(image);
    }

    public List<BoardResponseDTO> findAllByBoardType(BoardType boardType) {
        return boardService.findAllByBoardType(boardType);
    }

    public Board findByBoardId(Long boardId) {
        return boardService.findByBoardId(boardId).orElse(null);
    }

    public boolean updateByDTO(BoardUpdateRequestDTO requestDTO) {
        return boardService.updateByDTO(requestDTO);
    }
}
