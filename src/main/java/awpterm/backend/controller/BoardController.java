package awpterm.backend.controller;

import awpterm.backend.Config;
import awpterm.backend.api.request.board.*;
import awpterm.backend.api.response.ApiResponse;
import awpterm.backend.domain.Board;
import awpterm.backend.domain.Member;
import awpterm.backend.enums.BoardType;
import awpterm.backend.etc.SessionConst;
import awpterm.backend.service.BoardService;
import awpterm.backend.service.BoardServiceFacade;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardServiceFacade boardServiceFacade;

    @PostMapping("add/allType") //전체 공지 등록
    public ResponseEntity<?> addAllTypeBoards(@SessionAttribute(value = SessionConst.LOGIN_MEMBER) Member loginMember,
                                              @RequestBody BoardAddAllTypeRequestDTO requestDTO) {
        return ApiResponse.response(HttpStatus.CREATED, boardServiceFacade.saveAllTypeBoard(loginMember, requestDTO));
    }

    @PostMapping("/add/activity/photo") //활동 사진 등록
    public ResponseEntity<?> addPhotoBoard(@SessionAttribute(value = SessionConst.LOGIN_MEMBER) Member loginMember,
                                           @RequestBody BoardAddPhotoRequestDTO requestDTO) {
        return ApiResponse.response(HttpStatus.CREATED, boardServiceFacade.savePhotoBoard(loginMember, requestDTO));
    }

    @PostMapping("/add/activity/video") // 활동 영상 등록
    public ResponseEntity<?> addVideoBoard(@SessionAttribute(value = SessionConst.LOGIN_MEMBER) Member loginMember,
                                           @RequestBody BoardAddVideoRequestDTO requestDTO) {
        return ApiResponse.response(HttpStatus.CREATED, boardServiceFacade.saveVideoBoard(loginMember, requestDTO));
    }

    @PostMapping("/add/recruitment") // 부원 모집 등록
    public ResponseEntity<?> addRecruitmentBoard(@SessionAttribute(value = SessionConst.LOGIN_MEMBER) Member loginMember,
                                                 @RequestBody BoardAddRecruitmentRequestDTO requestDTO) {
        return ApiResponse.response(HttpStatus.CREATED, boardServiceFacade.saveRecruitmentBoard(loginMember, requestDTO));
    }

    @PostMapping("/add/notice") //동아리 공지 등록
    public ResponseEntity<?> addNoticeBoard(@SessionAttribute(value = SessionConst.LOGIN_MEMBER) Member loginMember,
                                            @RequestBody BoardAddNoticeRequestDTO requestDTO) {
        return ApiResponse.response(HttpStatus.CREATED, boardServiceFacade.saveNoticeBoard(loginMember, requestDTO));
    }

    @PostMapping("/fileUpload") //이미지들 DB에 저장 및 정보 반환
    public ResponseEntity<?> fileUpload(@RequestPart MultipartFile image) {
        return ApiResponse.response(HttpStatus.OK, Config.FILE_SERVER_URL+"/"+ boardServiceFacade.storeFile(image).getStoredFileName());
    }

    @GetMapping("/all/{boardType}") //타입에 맞는 모든 게시판에 대해서 보냄
    public ResponseEntity<?> getAllBoards(@PathVariable BoardType boardType) {
        if(BoardType.동아리_공지 == boardType) {
            return ApiResponse.response(HttpStatus.BAD_REQUEST, null);
        }
        return ApiResponse.response(HttpStatus.OK, boardServiceFacade.findAllByBoardType(boardType));
    }
    @GetMapping("/{boardId}") //게시판 하나 선택해서 정보 보기
    public ResponseEntity<?> getBoardById(@PathVariable Long boardId) {
        return ApiResponse.response(HttpStatus.OK, boardServiceFacade.findByBoardId(boardId));
    }
    @GetMapping("/inquiry/noticeType") //로그인 유저가 가입되어있는 동아리에 대해서만 조회
    public ResponseEntity<?> inquiryNoticeTypeBoard(@SessionAttribute(required = false) Member loginMember) {
        if(loginMember == null) {
            return ApiResponse.response(HttpStatus.OK, "[]");
        }

        return ApiResponse.response(HttpStatus.OK, boardServiceFacade.findAllByNoticeType(loginMember, BoardType.동아리_공지));
    }

    @GetMapping("/inquiry/all") // 동아리 + 전체 공지
    public ResponseEntity<?> inquiryNoticeTypeAndAllTypeBoard(HttpSession session) {
        Member loginMember = (Member) session.getAttribute("loginMember");

        if(loginMember == null) {
            return getAllBoards(BoardType.전체_공지);
        }

        return ApiResponse.response(HttpStatus.OK, boardServiceFacade.getNoticeTypeAndAllTypeBoard(loginMember));
    }
}
