package awpterm.backend.controller;

import awpterm.backend.api.kakao.KakaoAPI;
import awpterm.backend.api.request.member.MemberLoginRequestDTO;
import awpterm.backend.api.request.member.MemberRegisterRequestDTO;
import awpterm.backend.api.response.ApiResponse;
import awpterm.backend.enums.Position;
import awpterm.backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberLoginRequestDTO memberLoginRequestDTO) {
        if (!memberService.isValidLoginRequest(memberLoginRequestDTO))
            return ApiResponse.response(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호가 일치하지 않습니다.");

        return ApiResponse.response(HttpStatus.OK, Boolean.TRUE);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody MemberRegisterRequestDTO memberRegisterRequestDTO) {
        return ApiResponse.response(HttpStatus.CREATED, memberService.register(memberRegisterRequestDTO));
    }


    @PostMapping("/kakao/login")
    public ResponseEntity<?> kakaoLogin(@RequestBody MemberLoginRequestDTO memberLoginRequestDTO) {
        if (!memberService.isValidMember(memberLoginRequestDTO.getId()))
            return ApiResponse.response(HttpStatus.UNPROCESSABLE_ENTITY, "카카오 회원이 현재 존재하지 않습니다.");
        if (!memberService.isValidLoginRequest(memberLoginRequestDTO))
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
        return ApiResponse.response(HttpStatus.OK, memberService.findByPosition(Position.교수));
    }
}
