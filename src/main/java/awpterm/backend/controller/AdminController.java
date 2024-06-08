package awpterm.backend.controller;

import awpterm.backend.api.request.admin.AdminLoginRequestDTO;
import awpterm.backend.api.response.ApiResponse;
import awpterm.backend.domain.Club;
import awpterm.backend.enums.Status;
import awpterm.backend.etc.SessionConst;
import awpterm.backend.service.AdminService;
import awpterm.backend.service.ClubService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    // TODO 아래와 같이 2개 이상의 종속성이 발생하면 파사드 패턴으로 묶어줄 것
    private final AdminService adminService;
    private final ClubService clubService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminLoginRequestDTO adminLoginRequestDTO, HttpServletRequest request) {
        if (!adminService.isValidLoginRequest(adminLoginRequestDTO))
            return ApiResponse.response(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호가 일치하지 않습니다.");

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_ADMIN, adminService.findById(adminLoginRequestDTO.getId()));
        return ApiResponse.response(HttpStatus.OK, Boolean.TRUE);
    }

    @GetMapping("/checkList")
    public ResponseEntity<?> checkStatus() {
        List<Club> clubList = clubService.findByStatus(Status.검토);
        return ApiResponse.response(HttpStatus.OK, clubList);
    }

    //TODO 아래 내용은 클럽 API로 옮기는 것이 적절할 듯
    @PutMapping("/updateStatus")
    public ResponseEntity<?> updateClubStatus(@RequestParam Long id, @RequestParam String status) {
        if(!clubService.updateStatus(id, status)) {
            return ApiResponse.response(HttpStatus.INTERNAL_SERVER_ERROR, Boolean.FALSE);
        }
        return ApiResponse.response(HttpStatus.OK, Boolean.TRUE);
    }
}
