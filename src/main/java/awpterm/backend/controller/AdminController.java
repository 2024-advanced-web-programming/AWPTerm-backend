package awpterm.backend.controller;

import awpterm.backend.api.request.admin.AdminLoginRequestDTO;
import awpterm.backend.api.request.club.ClubStatusRequestDTO;
import awpterm.backend.api.response.ApiResponse;
import awpterm.backend.domain.Club;
import awpterm.backend.enums.Status;
import awpterm.backend.service.AdminService;
import awpterm.backend.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final ClubService clubService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminLoginRequestDTO adminLoginRequestDTO) {
        if (!adminService.isValidLoginRequest(adminLoginRequestDTO))
            return ApiResponse.response(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호가 일치하지 않습니다.");

        return ApiResponse.response(HttpStatus.OK, Boolean.TRUE);
    }

    @GetMapping("/checkList")
    public ResponseEntity<?> checkStatus(@RequestParam String id) {
        if (!adminService.isAdmin(id)) { //관리자인지 판단
            return ApiResponse.response(HttpStatus.BAD_REQUEST, Boolean.FALSE);
        }
        List<Club> clubList = clubService.findByStatus(Status.검토);
        return ApiResponse.response(HttpStatus.OK, clubList);
    }
    @PutMapping("/updateStatus")
    public ResponseEntity<?> updateClubStatus(@RequestParam Long id, @RequestParam String status) {
        if(!clubService.updateStatus(id, status)) {
            return ApiResponse.response(HttpStatus.INTERNAL_SERVER_ERROR, Boolean.FALSE);
        }
        return ApiResponse.response(HttpStatus.OK, Boolean.TRUE);
    }
}
