package awpterm.backend.controller;

import awpterm.backend.api.request.MemberLoginRequestDTO;
import awpterm.backend.api.request.MemberRegisterRequestDTO;
import awpterm.backend.domain.Member;
import awpterm.backend.enums.Gender;
import awpterm.backend.enums.Major;
import awpterm.backend.enums.Position;
import awpterm.backend.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/login")
    public boolean login(@RequestBody MemberLoginRequestDTO memberLoginRequestDTO) {
        Member member = Member.builder()
                .id(memberLoginRequestDTO.getId())
                .password(memberLoginRequestDTO.getPassword().hashCode())
                .build();

        return memberService.login(member);
    }

    @PostMapping("/register")
    public List<Member> register(@RequestBody MemberRegisterRequestDTO memberRegisterRequestDTO) {
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
        return memberService.register(member);
    }
}
