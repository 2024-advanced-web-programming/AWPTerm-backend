package awpterm.backend.controller;

import awpterm.backend.api.request.club.*;
import awpterm.backend.api.response.ApiResponse;
import awpterm.backend.domain.Member;
import awpterm.backend.enums.Status;
import awpterm.backend.etc.SessionConst;
import awpterm.backend.service.ClubServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/club")
@RequiredArgsConstructor
public class ClubController {
    private final ClubServiceFacade clubServiceFacade;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody ClubRegisterRequestDTO clubRegisterRequestDTO) {
        if (!clubServiceFacade.isValidMemberByCode(clubRegisterRequestDTO.getRequestorCode()))
            return ApiResponse.response(HttpStatus.BAD_REQUEST, "신청자 학번이 잘못 입력되었습니다.");
        if (!clubServiceFacade.isValidMemberByCode(clubRegisterRequestDTO.getSupervisorCode()))
            return ApiResponse.response(HttpStatus.BAD_REQUEST, "지도교수 코드가 잘못 입력되었습니다.");

        return ApiResponse.response(HttpStatus.CREATED, clubServiceFacade.register(clubRegisterRequestDTO));
    }

    @GetMapping("/register/list")
    public ResponseEntity<?> registerList() {
        return ApiResponse.response(HttpStatus.OK, clubServiceFacade.getRegisterList());
    }

    @PostMapping("/application")
    public ResponseEntity<?> application(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member loginMember,
                                         ClubApplicationRequestDTO clubApplicationRequestDTO,
                                         @RequestPart MultipartFile file) {
        try {
            return ApiResponse.response(HttpStatus.OK, clubServiceFacade.apply(loginMember, clubApplicationRequestDTO, file));
        } catch (Exception e) {
            return ApiResponse.response(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GetMapping("/application/list/{clubId}")
    public ResponseEntity<?> applicationList(@PathVariable Long clubId) {
        if (clubServiceFacade.findById(clubId) == null)
            return ApiResponse.response(HttpStatus.BAD_REQUEST, "동아리가 존재하지 않습니다.");

        return ApiResponse.response(HttpStatus.OK, clubServiceFacade.getApplicationList(clubId));
    }

    @GetMapping("/application/list/me")
    public ResponseEntity<?> applicationList(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member loginMember) {
        return ApiResponse.response(HttpStatus.OK, clubServiceFacade.getApplicationList(loginMember));
    }

    @GetMapping("/list/me")
    public ResponseEntity<?> myClubList(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member loginMember) {
        return ApiResponse.response(HttpStatus.OK, clubServiceFacade.getMyClubList(loginMember));
    }

    @PostMapping("/application/decision")
    public ResponseEntity<?> applicationDecision(@RequestBody ClubApplicationDecisionDTO clubApplicationDecisionDTO) {
        clubServiceFacade.applicationDecision(clubApplicationDecisionDTO);
        return ApiResponse.response(HttpStatus.OK, null);
    }

    @DeleteMapping("/{clubId}/dismiss/{memberId}")
    public ResponseEntity<?> dismiss(@PathVariable Long clubId, @PathVariable String memberId) {
        try {
            clubServiceFacade.clubMemberDelete(clubId, memberId);
            return ApiResponse.response(HttpStatus.OK, null);
        } catch (RuntimeException e) {
            return ApiResponse.response(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    //동아리 승인 및 거절
    @PutMapping("/updateStatus")
    public ResponseEntity<?> updateClubStatus(@RequestBody ClubUpdateStatusRequestDTO requestDTO) {
        if(!clubServiceFacade.updateStatus(requestDTO.getClubId(), requestDTO.getStatus(), requestDTO.getRejectedReason())) {
            return ApiResponse.response(HttpStatus.INTERNAL_SERVER_ERROR, Boolean.FALSE);
        }
        return ApiResponse.response(HttpStatus.OK, Boolean.TRUE);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllClubs() {
        return ApiResponse.response(HttpStatus.OK, clubServiceFacade.findByStatus(Status.승인));
    }

    @GetMapping("/inquiryInfo/{clubId}") //동아리 id에 맞는 기본 정보 조회
    public ResponseEntity<?> getClubInfo(@PathVariable Long clubId) {
        return ApiResponse.response(HttpStatus.OK, clubServiceFacade.getClubInfo(clubId));
    }

    @GetMapping("/{clubId}")
    public ResponseEntity<?> findById(@PathVariable Long clubId) {
        return ApiResponse.response(HttpStatus.OK, clubServiceFacade.findById(clubId));
    }

    @PutMapping( "/{clubId}") //기본 정보 입력 및 수정 -> 이미 동아리는 등록되어있으므로 등록된 엔티티에 수정하는 방식
    public ResponseEntity<?> clubInfo(@PathVariable Long clubId,
                                      ClubUpdateBasicInfoRequestDTO clubUpdateBasicInfoRequestDTO,
                                      @RequestPart(required = false) MultipartFile applicationForm,
                                      @RequestPart(required = false) MultipartFile clubPhoto) {
        clubServiceFacade.updateBasicInfo(clubId, clubUpdateBasicInfoRequestDTO, applicationForm, clubPhoto);
        return ApiResponse.response(HttpStatus.OK, null);
    }
}