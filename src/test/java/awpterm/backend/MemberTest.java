package awpterm.backend;

import awpterm.backend.domain.Member;
import awpterm.backend.enums.Gender;
import awpterm.backend.enums.Major;
import awpterm.backend.enums.Position;
import awpterm.backend.repository.MemberRepository;
import awpterm.backend.service.MemberService;
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
                .password(password.hashCode())
                .name("testName")
                .birthDate("2000-01-01")
                .code("20190001")
                .phoneNumber("010-0000-0000")
                .email("testEmail@naver.com")
                .gender(Gender.남자)
                .major(Major.컴퓨터소프트웨어공학과)
                .position(Position.학생)
                .build();

        memberService.register(member);
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
                .password(password.hashCode())
                .name("testName")
                .birthDate("2000-01-01")
                .code("20190001")
                .phoneNumber("010-0000-0000")
                .email("testEmail@naver.com")
                .gender(Gender.남자)
                .major(Major.컴퓨터소프트웨어공학과)
                .position(Position.학생)
                .build();

        memberService.register(member);

        Member right = Member.builder()
                .id(id)
                .password(password.hashCode())
                .build();

        Member idWrong = Member.builder()
                .id("test")
                .password(password.hashCode())
                .build();

        Member passwordWrong = Member.builder()
                .id(id)
                .password("test".hashCode())
                .build();

        Member bothWrong = Member.builder()
                .id("test")
                .password("test".hashCode())
                .build();

        assertThat(memberService.login(right))
                .isEqualTo(true);
        assertThat(memberService.login(idWrong))
                .isEqualTo(false);
        assertThat(memberService.login(passwordWrong))
                .isEqualTo(false);
        assertThat(memberService.login(bothWrong))
                .isEqualTo(false);
    }
}