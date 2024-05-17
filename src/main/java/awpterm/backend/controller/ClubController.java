package awpterm.backend.controller;

import awpterm.backend.api.request.club.ClubRegisterRequestDTO;
import awpterm.backend.api.response.ApiResponse;
import awpterm.backend.service.ClubServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/club")
@RequiredArgsConstructor
public class ClubController {
    private final ClubServiceFacade clubServiceFacade;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody ClubRegisterRequestDTO clubRegisterRequestDTO) {
        if (!clubServiceFacade.isValidMember(clubRegisterRequestDTO.getRequestorId()))
            return ApiResponse.response(HttpStatus.BAD_REQUEST, "신청자 ID가 잘못 입력되었습니다.");
        if (!clubServiceFacade.isValidMember(clubRegisterRequestDTO.getSupervisorId()))
            return ApiResponse.response(HttpStatus.BAD_REQUEST, "지도교수 ID가 잘못 입력되었습니다.");

        return ApiResponse.response(HttpStatus.CREATED, clubServiceFacade.register(clubRegisterRequestDTO));
    }
}
