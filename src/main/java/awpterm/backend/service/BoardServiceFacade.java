package awpterm.backend.service;

import awpterm.backend.api.request.board.BoardAddNoticeRequestDTO;
import awpterm.backend.api.request.board.BoardAddPhotoRequestDTO;
import awpterm.backend.api.request.board.BoardAddRecruitmentRequestDTO;
import awpterm.backend.api.request.board.BoardAddVideoRequestDTO;
import awpterm.backend.api.response.board.BoardResponseDTO;
import awpterm.backend.api.response.file.FilePropertyResponseDTO;
import awpterm.backend.domain.Member;
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

    public FilePropertyResponseDTO storeFile(MultipartFile image) {
        return filePropertyService.storeFile(image);
    }
}
