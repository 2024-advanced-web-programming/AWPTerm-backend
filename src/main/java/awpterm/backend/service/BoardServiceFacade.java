package awpterm.backend.service;

import awpterm.backend.api.request.board.*;
import awpterm.backend.api.response.board.BoardResponseDTO;
import awpterm.backend.api.response.club.ClubMemberResponseDTO;
import awpterm.backend.domain.*;
import awpterm.backend.enums.BoardType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.fasterxml.jackson.databind.type.LogicalType.Collection;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardServiceFacade {
    private final BoardService boardService;
    private final FilePropertyService filePropertyService;
    private final ClubMemberService clubMemberService;
    private final ClubService clubService;

    public BoardResponseDTO saveAllTypeBoard(Member loginMember, BoardAddAllTypeRequestDTO requestDTO) {
        return boardService.saveAllTypeBoard(loginMember, requestDTO, clubService.findById(requestDTO.getClubId()));
    }

    public BoardResponseDTO savePhotoBoard(Member loginMember, BoardAddPhotoRequestDTO requestDTO) {
        return boardService.savePhotoBoard(loginMember, requestDTO, clubService.findById(requestDTO.getClubId()));
    }

    public BoardResponseDTO saveVideoBoard(Member loginMember, BoardAddVideoRequestDTO requestDTO) {
        return boardService.saveVideoBoard(loginMember, requestDTO, clubService.findById(requestDTO.getClubId()));
    }

    public BoardResponseDTO saveRecruitmentBoard(Member loginMember, BoardAddRecruitmentRequestDTO requestDTO) {
        return boardService.saveRecruitmentBoard(loginMember, requestDTO,clubService.findById(requestDTO.getClubId()));
    }

    public BoardResponseDTO saveNoticeBoard(Member loginMember, BoardAddNoticeRequestDTO requestDTO) {
        return boardService.saveNoticeBoard(loginMember, requestDTO, clubService.findById(requestDTO.getClubId()));
    }

    public FileProperty storeFile(MultipartFile image) {
        return filePropertyService.storeFile(image);
    }

    public List<BoardResponseDTO> findAllByBoardType(BoardType boardType) {
        return boardService.findAllByBoardType(boardType);
    }

    public BoardResponseDTO findByBoardId(Long boardId) {
        return boardService.findByBoardId(boardId);
    }

    public List<BoardResponseDTO> findAllByNoticeType(Member loginMember, BoardType boardType) {
        List<BoardResponseDTO> findByTypeResults = boardService.findAllByBoardType(boardType);
        List<ClubMember> clubMembers = clubMemberService.findByMember(loginMember);
        List<BoardResponseDTO> results = new ArrayList<>();
        for (BoardResponseDTO boardResponseDTO : findByTypeResults) {
            for (ClubMember clubMember : clubMembers) {
                if (Objects.equals(boardResponseDTO.getClubId(), clubMember.getClub().getId())) {
                    results.add(boardResponseDTO);
                }
            }
        }
        return results;
    }

    public List<BoardResponseDTO> getNoticeTypeAndAllTypeBoard(Member loginMember) {
        List<BoardResponseDTO> allTypeBoards = boardService.findAllByBoardType(BoardType.전체_공지);
        List<BoardResponseDTO> noticeTypeBoards = findAllByNoticeType(loginMember, BoardType.동아리_공지);

        allTypeBoards.addAll(noticeTypeBoards);

        // Define the date format corresponding to the timestamps
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        allTypeBoards.sort((b1, b2) -> {
            try {
                Date date1 = dateFormat.parse(b1.getTimestamp());
                Date date2 = dateFormat.parse(b2.getTimestamp());
                // For descending order, compare date2 with date1
                return date2.compareTo(date1);
            } catch (ParseException e) {
                throw new IllegalArgumentException(e);
            }
        });

        return allTypeBoards;
    }
}
