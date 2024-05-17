package awpterm.backend.docs;

import awpterm.backend.api.response.MemberResponseDTO;
import awpterm.backend.controller.MemberController;
import awpterm.backend.domain.Member;
import awpterm.backend.enums.Gender;
import awpterm.backend.enums.Major;
import awpterm.backend.enums.Position;
import awpterm.backend.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberControllerTest extends RestDocsTest {
    private final MemberService memberService = mock(MemberService.class);

    @Override
    protected Object initController() {
        return new MemberController(memberService);
    }

    @Test
    void 회원가입_테스트() {

    }

    @Test
    void 로그인_테스트() {

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

        given(memberService.findByPosition(Position.교수))
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
