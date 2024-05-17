package awpterm.backend;

import awpterm.backend.api.request.member.MemberLoginRequestDTO;
import awpterm.backend.api.request.member.MemberRegisterRequestDTO;
import awpterm.backend.api.response.member.MemberResponseDTO;
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


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberControllerTest {
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

    @Test
    void professorsTest() {
        MemberRegisterRequestDTO professor1 = MemberRegisterRequestDTO.builder()
                .id("id1")
                .password("password1")
                .name("교수1")
                .birthDate("2000-11-11")
                .code("SE0001")
                .phoneNumber("010-1111-1111")
                .email("SE1@kumoh.ac.kr")
                .gender(Gender.남자.toString())
                .major(Major.컴퓨터소프트웨어공학과.toString())
                .position(Position.교수.toString())
                .build();
        MemberRegisterRequestDTO professor2 = MemberRegisterRequestDTO.builder()
                .id("id2")
                .password("password2")
                .name("교수2")
                .birthDate("2000-12-22")
                .code("CE0001")
                .phoneNumber("010-2222-2222")
                .email("CE1@kumoh.ac.kr")
                .gender(Gender.여자.toString())
                .major(Major.컴퓨터공학과.toString())
                .position(Position.교수.toString())
                .build();
        MemberRegisterRequestDTO professor3 = MemberRegisterRequestDTO.builder()
                .id("id3")
                .password("password3")
                .name("교수3")
                .birthDate("2000-12-23")
                .code("AI0001")
                .phoneNumber("010-3333-3333")
                .email("AI1@kumoh.ac.kr")
                .gender(Gender.여자.toString())
                .major(Major.인공지능공학과.toString())
                .position(Position.교수.toString())
                .build();

        memberService.register(professor1);
        memberService.register(professor2);
        memberService.register(professor3);

        MemberResponseDTO response1 = MemberResponseDTO.builder()
                .name("교수1")
                .birthDate("2000-11-11")
                .code("SE0001")
                .phoneNumber("010-1111-1111")
                .email("SE1@kumoh.ac.kr")
                .gender(Gender.남자.toString())
                .major(Major.컴퓨터소프트웨어공학과.toString())
                .position(Position.교수.toString())
                .build();
        MemberResponseDTO response2 = MemberResponseDTO.builder()
                .name("교수2")
                .birthDate("2000-12-22")
                .code("CE0001")
                .phoneNumber("010-2222-2222")
                .email("CE1@kumoh.ac.kr")
                .gender(Gender.여자.toString())
                .major(Major.컴퓨터공학과.toString())
                .position(Position.교수.toString())
                .build();
        MemberResponseDTO response3 = MemberResponseDTO.builder()
                .name("교수3")
                .birthDate("2000-12-23")
                .code("AI0001")
                .phoneNumber("010-3333-3333")
                .email("AI1@kumoh.ac.kr")
                .gender(Gender.여자.toString())
                .major(Major.인공지능공학과.toString())
                .position(Position.교수.toString())
                .build();

        List<MemberResponseDTO> result = memberService.findByPosition(Position.교수);

        assertThat(result).containsExactly(response1, response2, response3);
    }
}