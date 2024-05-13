package awpterm.backend.controller;

import awpterm.backend.api.kakao.KakaoAPI;
import awpterm.backend.api.request.MemberLoginRequestDTO;
import awpterm.backend.api.request.MemberRegisterRequestDTO;
import awpterm.backend.api.response.ApiResponse;
import awpterm.backend.domain.Member;
import awpterm.backend.enums.Gender;
import awpterm.backend.enums.Major;
import awpterm.backend.enums.Position;
import awpterm.backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/login")
    public ApiResponse<Boolean> login(@RequestBody MemberLoginRequestDTO memberLoginRequestDTO) {
        Member member = Member.builder()
                .id(memberLoginRequestDTO.getId())
                .password(memberLoginRequestDTO.getPassword().hashCode())
                .build();

        if (!memberService.login(member))
            return ApiResponse.response(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호가 일치하지 않습니다.", Boolean.FALSE);

        return ApiResponse.response(HttpStatus.OK, Boolean.TRUE);
    }

    @PostMapping("/register")
    public ApiResponse<List<Member>> register(@RequestBody MemberRegisterRequestDTO memberRegisterRequestDTO) {
        Member member = Member.builder()
                .id(memberRegisterRequestDTO.getId())
                .password(memberRegisterRequestDTO.getPassword().hashCode())
                .name(memberRegisterRequestDTO.getName())
                .birthDate(memberRegisterRequestDTO.getBirthDate())
                .code(memberRegisterRequestDTO.getCode())
                .phoneNumber(memberRegisterRequestDTO.getPhoneNumber())
                .email(memberRegisterRequestDTO.getEmail())
                .gender(Gender.valueOf(memberRegisterRequestDTO.getGender()))
                .major(Major.valueOf(memberRegisterRequestDTO.getMajor()))
                .position(Position.valueOf(memberRegisterRequestDTO.getPosition()))
                .build();
        return ApiResponse.response(HttpStatus.CREATED, memberService.register(member));
    }


    @PostMapping("/kakao/login")
    public ApiResponse<Boolean> kakaoLogin(@RequestBody MemberRegisterRequestDTO memberLoginRequestDTO) {
        Member findMember = memberService.findById(memberLoginRequestDTO.getId());
        if (findMember == null) {
            return ApiResponse.response(HttpStatus.UNPROCESSABLE_ENTITY, "카카오 회원이 현재 존재하지 않습니다.", Boolean.FALSE);
        }

        Member member = Member.builder()
                .id(memberLoginRequestDTO.getId())
                .password(memberLoginRequestDTO.getPassword().hashCode())
                .build();

        if (!memberService.login(member))
            return ApiResponse.response(HttpStatus.BAD_REQUEST, "아이디 혹은 비밀번호가 일치하지 않습니다.", Boolean.FALSE);

        return ApiResponse.response(HttpStatus.OK, Boolean.TRUE);
    }

    @GetMapping("/kakao/token")
    public ApiResponse<String> kakaoToken(@RequestParam String code) {
        String token = KakaoAPI.requestToken(code);
        HashMap<String, Object> userInfo = KakaoAPI.getUserInfo(token);
        if (token != null) {
            return ApiResponse.response(HttpStatus.BAD_REQUEST, "Token is NULL");
        } else {
            String data = "{\"id\":\"" + userInfo.get("id") + "\",\"email\":" + userInfo.get("email") + "\"}";
            return ApiResponse.response(HttpStatus.OK, data);
        }
    }
}
