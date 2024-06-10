package awpterm.backend.docs;

import awpterm.backend.api.request.member.MemberLoginRequestDTO;
import awpterm.backend.api.request.member.MemberRegisterRequestDTO;
import awpterm.backend.api.response.member.MemberResponseDTO;
import awpterm.backend.controller.MemberController;
import awpterm.backend.enums.Gender;
import awpterm.backend.enums.Major;
import awpterm.backend.enums.Position;
import awpterm.backend.service.MemberService;
import awpterm.backend.service.MemberServiceFacade;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberControllerDocsTest extends RestDocsTest {
    private final MemberServiceFacade memberServiceFacade = mock(MemberServiceFacade.class);

    @Override
    protected Object initController() {
        return new MemberController(memberServiceFacade);
    }

    @Test
    void 회원가입_테스트() throws Exception {
        String id = "testId";
        String password = "testPassword";
        String name = "testName";
        String brithDate = "2000-01-01";
        String code = "20190001";
        String phoneNumber = "010-0000-0000";
        String email = "testEmail@naver.com";
        String gender = Gender.남자.toString();
        String major = Major.컴퓨터소프트웨어공학과.toString();
        String position = Position.학생.toString();

        MemberRegisterRequestDTO dto = MemberRegisterRequestDTO.builder()
                .id(id)
                .password(password)
                .name(name)
                .birthDate(brithDate)
                .code(code)
                .phoneNumber(phoneNumber)
                .email(email)
                .gender(gender)
                .major(major)
                .position(position)
                .build();

        given(memberServiceFacade.register(any(MemberRegisterRequestDTO.class)))
                .willReturn(MemberResponseDTO.builder()
                        .name(name)
                        .birthDate(brithDate)
                        .code(code)
                        .phoneNumber(phoneNumber)
                        .email(email)
                        .gender(gender)
                        .major(major)
                        .position(position)
                        .build());

        mockMvc.perform(post("/member/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("member-register",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestBody(),
                        responseBody()));

    }

    @Test
    void 로그인_테스트() throws Exception {
        MemberLoginRequestDTO request1 = MemberLoginRequestDTO.builder()
                .id("id")
                .password("password")
                .build();
        MemberLoginRequestDTO request2 = MemberLoginRequestDTO.builder()
                .id("wrongId")
                .password("wrongPassword")
                .build();

        given(memberServiceFacade.isValidLoginRequest(request1)).willReturn(true);
        given(memberServiceFacade.isValidLoginRequest(request2)).willReturn(false);

        mockMvc.perform(post("/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request1)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("member-login-right",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestBody(),
                        responseBody()));

        mockMvc.perform(post("/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request2)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(document("member-login-wrong",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestBody(),
                        responseBody()));
    }

    @Test
    void 지도교수_목록_테스트() throws Exception {
        List<MemberResponseDTO> 교수님들 = new ArrayList<>();
        교수님들.add(MemberResponseDTO.builder()
                .name("교수1")
                .birthDate("2000-11-11")
                .code("SE0001")
                .phoneNumber("010-1111-1111")
                .email("SE1@kumoh.ac.kr")
                .gender(Gender.남자.toString())
                .major(Major.컴퓨터소프트웨어공학과.toString())
                .position(Position.교수.toString())
                .build());
        교수님들.add(MemberResponseDTO.builder()
                .name("교수2")
                .birthDate("2000-12-22")
                .code("CE0001")
                .phoneNumber("010-2222-2222")
                .email("CE1@kumoh.ac.kr")
                .gender(Gender.여자.toString())
                .major(Major.컴퓨터공학과.toString())
                .position(Position.교수.toString())
                .build());
        교수님들.add(MemberResponseDTO.builder()
                .name("교수3")
                .birthDate("2000-12-23")
                .code("AI0001")
                .phoneNumber("010-3333-3333")
                .email("AI1@kumoh.ac.kr")
                .gender(Gender.여자.toString())
                .major(Major.인공지능공학과.toString())
                .position(Position.교수.toString())
                .build());

        given(memberServiceFacade.findByPosition(Position.교수))
                .willReturn(교수님들);

        mockMvc.perform(get("/member/professors"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("professors",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseBody()
                        )
                );
    }
}
