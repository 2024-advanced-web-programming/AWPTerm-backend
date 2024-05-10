package awpterm.backend.controller;

import awpterm.backend.Config;
import awpterm.backend.api.kakao.KakaoAPI;
import awpterm.backend.api.request.MemberLoginRequestDTO;
import awpterm.backend.api.request.MemberRegisterRequestDTO;
import awpterm.backend.api.response.ApiResponse;
import awpterm.backend.domain.Member;
import awpterm.backend.enums.Gender;
import awpterm.backend.enums.Major;
import awpterm.backend.enums.Position;
import awpterm.backend.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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

    @GetMapping("/kakao/auth")
    public ApiResponse<String> kakaoAuth() {
        return ApiResponse.response(HttpStatus.OK, KakaoAPI.requestAuthorize().toString());
    }

    @GetMapping("/kakao/callback")
    public void kakaoCallBack(String code, HttpServletResponse res) throws IOException {
        res.sendRedirect(Config.SERVER_URL + "/member/kakao/login?id="+code+"&password="+KakaoAPI.requestToken(code));
    }

    @GetMapping("/kakao/login")
    public ApiResponse<String> kakaoLogin(@RequestParam String clientId, @RequestParam String token) {
        Member member = Member.builder()
                .id(clientId)
                .password(token.hashCode())
                .build();

        if (!memberService.login(member)) {
            memberService.register(member); //아이디와 패스워드만 가진 녀석 DB에 저장
            return ApiResponse.response(HttpStatus.CREATED, clientId);
        }

        return ApiResponse.response(HttpStatus.OK, clientId);
    }

    @PostMapping("/kakao/register")
    public ApiResponse<List<Member>> kakaoRegister(@RequestBody MemberRegisterRequestDTO memberRegisterRequestDTO) { //자동으로 회원가입이 되게끔
        Member findMember = memberService.findById(memberRegisterRequestDTO.getId());
        Member member = Member.builder()
                .id(findMember.getId())
                .password(findMember.getPassword())
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
}
