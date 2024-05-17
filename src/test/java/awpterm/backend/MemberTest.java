package awpterm.backend;

import awpterm.backend.api.request.member.MemberLoginRequestDTO;
import awpterm.backend.api.request.member.MemberRegisterRequestDTO;
import awpterm.backend.domain.Member;
import awpterm.backend.enums.Gender;
import awpterm.backend.enums.Major;
import awpterm.backend.enums.Position;
import awpterm.backend.repository.MemberRepository;
import awpterm.backend.service.MemberService;
import awpterm.backend.util.SHA256;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;

    @Test
    void registerTest() {
        String id = "testId";
        String password = "testPassword";

        Member member = Member.builder()
                .id(id)
                .password(password)
                .name("testName")
                .birthDate("2000-01-01")
                .code("20190001")
                .phoneNumber("010-0000-0000")
                .email("testEmail@naver.com")
                .gender(Gender.남자)
                .major(Major.컴퓨터소프트웨어공학과)
                .position(Position.학생)
                .build();

        memberService.register(MemberRegisterRequestDTO.of(member));
        member.setPassword(SHA256.encrypt(password));
        Member findMember = memberRepository.findById("testId").orElse(null);

        assertThat(member)
                .usingRecursiveComparison()
                .ignoringFields("createdDate")
                .isEqualTo(findMember);
    }

    @Test
    void loginTest() {
        String id = "testId";
        String password = "testPassword";

        Member member = Member.builder()
                .id(id)
                .password(password)
                .name("testName")
                .birthDate("2000-01-01")
                .code("20190001")
                .phoneNumber("010-0000-0000")
                .email("testEmail@naver.com")
                .gender(Gender.남자)
                .major(Major.컴퓨터소프트웨어공학과)
                .position(Position.학생)
                .build();

        memberService.register(MemberRegisterRequestDTO.of(member));

        Member right = Member.builder()
                .id(id)
                .password(password)
                .build();

        Member idWrong = Member.builder()
                .id("test")
                .password(password)
                .build();

        Member passwordWrong = Member.builder()
                .id(id)
                .password("test")
                .build();

        Member bothWrong = Member.builder()
                .id("test")
                .password("test")
                .build();

        assertThat(memberService.isValidLoginRequest(MemberLoginRequestDTO.of(right)))
                .isEqualTo(true);
        assertThat(memberService.isValidLoginRequest(MemberLoginRequestDTO.of(idWrong)))
                .isEqualTo(false);
        assertThat(memberService.isValidLoginRequest(MemberLoginRequestDTO.of(passwordWrong)))
                .isEqualTo(false);
        assertThat(memberService.isValidLoginRequest(MemberLoginRequestDTO.of(bothWrong)))
                .isEqualTo(false);
    }
}