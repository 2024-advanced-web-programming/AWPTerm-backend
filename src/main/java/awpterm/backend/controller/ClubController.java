package awpterm.backend.controller;

import awpterm.backend.api.request.club.ClubApplicationDecisionDTO;
import awpterm.backend.api.request.club.ClubApplicationRequestDTO;
import awpterm.backend.api.request.club.ClubBasicInfoDTO;
import awpterm.backend.api.request.club.ClubRegisterRequestDTO;
import awpterm.backend.api.response.ApiResponse;
import awpterm.backend.domain.Member;
import awpterm.backend.etc.SessionConst;
import awpterm.backend.service.ClubServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/club")
@RequiredArgsConstructor
public class ClubController {
    //TODO 관리 기능은 Member 중에서도 마스터 권한을 지녀야만 접근 가능해야함 인터셉터로 처리할 것
    private final ClubServiceFacade clubServiceFacade;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody ClubRegisterRequestDTO clubRegisterRequestDTO) {
        if (!clubServiceFacade.isValidMemberByCode(clubRegisterRequestDTO.getRequestorCode()))
            return ApiResponse.response(HttpStatus.BAD_REQUEST, "신청자 학번이 잘못 입력되었습니다.");
        if (!clubServiceFacade.isValidMemberByCode(clubRegisterRequestDTO.getSupervisorCode()))
            return ApiResponse.response(HttpStatus.BAD_REQUEST, "지도교수 코드가 잘못 입력되었습니다.");

        return ApiResponse.response(HttpStatus.CREATED, clubServiceFacade.register(clubRegisterRequestDTO));
    }

    @PostMapping("/application")
    public ResponseEntity<?> application(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member loginMember,
                                         @ModelAttribute ClubApplicationRequestDTO clubApplicationRequestDTO) {
        try {
            return ApiResponse.response(HttpStatus.OK, clubServiceFacade.apply(loginMember, clubApplicationRequestDTO));
        } catch (IOException e) {
            return ApiResponse.response(HttpStatus.INTERNAL_SERVER_ERROR, null);
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

    @PostMapping("/application/decision")
    public ResponseEntity<?> applicationDecision(@RequestBody ClubApplicationDecisionDTO clubApplicationDecisionDTO) {
        clubServiceFacade.applicationDecision(clubApplicationDecisionDTO);
        return ApiResponse.response(HttpStatus.OK, null);
    }

    @DeleteMapping("/dismiss")
    public ResponseEntity<?> dismiss(@RequestParam Long clubId, @RequestParam String memberId) {
        clubServiceFacade.clubMemberDelete(clubId, memberId);
        return ApiResponse.response(HttpStatus.OK, null);
    }

    //동아리 승인 및 거절
    @PutMapping("/updateStatus")
    public ResponseEntity<?> updateClubStatus(@RequestParam Long clubId, @RequestParam String status, @RequestParam String rejectReason) {
        if(!clubServiceFacade.updateStatus(clubId, status, rejectReason)) {
            return ApiResponse.response(HttpStatus.INTERNAL_SERVER_ERROR, Boolean.FALSE);
        }
        return ApiResponse.response(HttpStatus.OK, Boolean.TRUE);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllClubs() {
        return ApiResponse.response(HttpStatus.OK, clubServiceFacade.findAll());
    }

    @PutMapping( "/basicInfo") //기본 정보 입력 및 수정 -> 이미 동아리는 등록되어있으므로 등록된 엔티티에 수정하는 방식
    public ResponseEntity<?> clubInfo(@RequestPart("data") ClubBasicInfoDTO clubBasicInfoDTO,
                                      @RequestPart(value = "image", required = false) MultipartFile representativePicture) {
        return ApiResponse.response(HttpStatus.OK, clubServiceFacade.updateBasicInfo(clubBasicInfoDTO, representativePicture));
    }
}
