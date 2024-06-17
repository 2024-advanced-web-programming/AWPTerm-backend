package awpterm.backend.controller;

import awpterm.backend.api.request.board.BoardAddNoticeRequestDTO;
import awpterm.backend.api.request.board.BoardAddPhotoRequestDTO;
import awpterm.backend.api.request.board.BoardAddRecruitmentRequestDTO;
import awpterm.backend.api.request.board.BoardAddVideoRequestDTO;
import awpterm.backend.api.response.ApiResponse;
import awpterm.backend.domain.Member;
import awpterm.backend.etc.SessionConst;
import awpterm.backend.service.BoardService;
import awpterm.backend.service.BoardServiceFacade;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardServiceFacade boardServiceFacade;

    @PostMapping("/add/activity/photo")
    public ResponseEntity<?> addPhotoBoard(@SessionAttribute(value = SessionConst.LOGIN_MEMBER) Member loginMember,
                                           @RequestBody BoardAddPhotoRequestDTO requestDTO) {
        return ApiResponse.response(HttpStatus.CREATED, boardServiceFacade.savePhotoBoard(loginMember, requestDTO));
    }

    @PostMapping("/add/activity/video")
    public ResponseEntity<?> addVideoBoard(@SessionAttribute(value = SessionConst.LOGIN_MEMBER) Member loginMember,
                                           @RequestBody BoardAddVideoRequestDTO requestDTO) {
        return ApiResponse.response(HttpStatus.CREATED, boardServiceFacade.saveVideoBoard(loginMember, requestDTO));
    }

    @PostMapping("/add/recruitment")
    public ResponseEntity<?> addRecruitmentBoard(@SessionAttribute(value = SessionConst.LOGIN_MEMBER) Member loginMember,
                                                 @RequestBody BoardAddRecruitmentRequestDTO requestDTO) {
        return ApiResponse.response(HttpStatus.CREATED, boardServiceFacade.saveRecruitmentBoard(loginMember, requestDTO));
    }
    @PostMapping("/add/notice")
    public ResponseEntity<?> addNoticeBoard(@SessionAttribute(value = SessionConst.LOGIN_MEMBER) Member loginMember,
                                            @RequestBody BoardAddNoticeRequestDTO requestDTO) {
        return ApiResponse.response(HttpStatus.CREATED, boardServiceFacade.saveNoticeBoard(loginMember, requestDTO));
    }
    @PostMapping("/fileUpload") //이미지들 DB에 저장 및 정보 반환
    public ResponseEntity<?> fileUpload(@RequestPart MultipartFile image) {
        return ApiResponse.response(HttpStatus.OK, boardServiceFacade.storeFile(image).getFileUrl());
    }
}
