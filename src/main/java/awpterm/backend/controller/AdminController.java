package awpterm.backend.controller;

import awpterm.backend.api.request.admin.AdminLoginRequestDTO;
import awpterm.backend.api.response.ApiResponse;
import awpterm.backend.domain.Club;
import awpterm.backend.enums.Status;
import awpterm.backend.etc.SessionConst;
import awpterm.backend.service.AdminService;
import awpterm.backend.service.AdminServiceFacade;
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
    private final AdminServiceFacade adminServiceFacade;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AdminLoginRequestDTO adminLoginRequestDTO, HttpServletRequest request) {
        if (!adminServiceFacade.isValidLoginRequest(adminLoginRequestDTO))
            return ApiResponse.response(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호가 일치하지 않습니다.");

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_ADMIN, adminServiceFacade.findById(adminLoginRequestDTO.getId()));
        return ApiResponse.response(HttpStatus.OK, Boolean.TRUE);
    }
    //TODO PathVariable -> Session으로 변경 [MemberController 참고]
    @GetMapping("/checkList/{adminId}")
    public ResponseEntity<?> checkStatus(@PathVariable String adminId) {
        if(adminServiceFacade.isAdmin(adminId)) { //관리자 아이디 체크
            List<Club> clubList = adminServiceFacade.findByStatus(Status.검토);
            return ApiResponse.response(HttpStatus.OK, clubList);
        }else {
            return ApiResponse.response(HttpStatus.BAD_REQUEST, "관리자 아이디가 아닙니다.");
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
        }

        return ApiResponse.response(HttpStatus.OK, null);
    }
}
