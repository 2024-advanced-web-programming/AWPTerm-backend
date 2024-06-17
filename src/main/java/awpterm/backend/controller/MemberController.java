package awpterm.backend.controller;

import awpterm.backend.api.kakao.KakaoAPI;
import awpterm.backend.api.request.member.MemberLoginRequestDTO;
import awpterm.backend.api.request.member.MemberRegisterRequestDTO;
import awpterm.backend.api.response.ApiResponse;
import awpterm.backend.api.response.member.MemberResponseDTO;
import awpterm.backend.domain.Member;
import awpterm.backend.enums.Position;
import awpterm.backend.etc.SessionConst;
import awpterm.backend.service.MemberServiceFacade;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.util.HashMap;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberServiceFacade memberServiceFacade;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberLoginRequestDTO memberLoginRequestDTO, HttpServletRequest request) {
        if (!memberServiceFacade.isValidLoginRequest(memberLoginRequestDTO))
            return ApiResponse.response(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호가 일치하지 않습니다.");

        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, memberServiceFacade.findById(memberLoginRequestDTO.getId()));
        return ApiResponse.response(HttpStatus.OK, Boolean.TRUE);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
        }

        return ApiResponse.response(HttpStatus.OK, null);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody MemberRegisterRequestDTO memberRegisterRequestDTO) {
        return ApiResponse.response(HttpStatus.CREATED, memberServiceFacade.register(memberRegisterRequestDTO));
    }


    @PostMapping("/kakao/login")
    public ResponseEntity<?> kakaoLogin(@RequestBody MemberLoginRequestDTO memberLoginRequestDTO) {
        if (!memberServiceFacade.isValidMemberById(memberLoginRequestDTO.getId()))
            return ApiResponse.response(HttpStatus.UNPROCESSABLE_ENTITY, "카카오 회원이 현재 존재하지 않습니다.");
        if (!memberServiceFacade.isValidLoginRequest(memberLoginRequestDTO))
            return ApiResponse.response(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호가 일치하지 않습니다.");

        return ApiResponse.response(HttpStatus.OK, Boolean.TRUE);
    }

    @GetMapping("/kakao/token")
    public ResponseEntity<?> kakaoToken(@RequestParam String code) {
        String token = KakaoAPI.requestToken(code);
        HashMap<String, Object> userInfo = KakaoAPI.getUserInfo(token);

        if (token == null)
            return ApiResponse.response(HttpStatus.BAD_REQUEST, "Token is NULL");

        String data = "{\"id\":\"" + userInfo.get("id") + "\",\"email\":\"" + userInfo.get("email") + "\"}";
        return ApiResponse.response(HttpStatus.OK, data);
    }

    @GetMapping("/professors")
    public ResponseEntity<?> professors() {
        return ApiResponse.response(HttpStatus.OK, memberServiceFacade.findByPosition(Position.교수));
    }

    @GetMapping("/clubs")
    public ResponseEntity<?> findClubByMember(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member loginMember) {
        return ApiResponse.response(HttpStatus.OK, memberServiceFacade.findClubByMember(loginMember));
    }

    @GetMapping("/applied/clubs")
    public ResponseEntity<?> appliedClubs(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member loginMember) {
        return ApiResponse.response(HttpStatus.OK, memberServiceFacade.findClubApplicantByMember(loginMember));
    }

    @GetMapping("/registered/clubs")
    public ResponseEntity<?> registeredClubs(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member loginMember) {
        return ApiResponse.response(HttpStatus.OK, memberServiceFacade.findClubByCreatedBy(loginMember));
    }

    @GetMapping("/president/clubs")
    public ResponseEntity<?> presidentClubs(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member loginMember) {
        try {
            return ApiResponse.response(HttpStatus.OK, memberServiceFacade.findClubByPresident(loginMember));
        } catch (MalformedURLException e) {
            return ApiResponse.response(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getMyInfo(@SessionAttribute(name = SessionConst.LOGIN_MEMBER) Member loginMember) {
        return ApiResponse.response(HttpStatus.OK, MemberResponseDTO.valueOf(loginMember));
    }
}
