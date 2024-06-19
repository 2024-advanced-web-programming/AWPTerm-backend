package awpterm.backend.docs;

import awpterm.backend.api.request.admin.AdminLoginRequestDTO;
import awpterm.backend.api.request.club.ClubRegisterRequestDTO;
import awpterm.backend.controller.AdminController;
import awpterm.backend.domain.Admin;
import awpterm.backend.domain.Club;
import awpterm.backend.domain.Member;
import awpterm.backend.enums.*;
import awpterm.backend.etc.SessionConst;
import awpterm.backend.service.AdminService;
import awpterm.backend.service.AdminServiceFacade;
import awpterm.backend.service.ClubServiceFacade;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestBody;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseBody;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AdminControllerDocsTest extends RestDocsTest {
    private final AdminServiceFacade adminServiceFacade = mock(AdminServiceFacade.class);

    @Override
    protected Object initController() {
        return new AdminController(adminServiceFacade);
    }

    @Test
    void 관리자_로그인() throws Exception {
        AdminLoginRequestDTO requestDTO = AdminLoginRequestDTO.builder()
                .id("testId")
                .pw("testPw")
                .build();

        //로그인 성공 여부 - 성공
        given(adminServiceFacade.isValidLoginRequest(requestDTO)).willReturn(true);
        mockMvc.perform(post("/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("admin-login-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestBody(),
                        responseBody()));
    }

    @Test
    void 동아리_신청목록_확인() throws Exception {

        Admin admin = Admin.builder()
                .id("testId")
                .password("testPw")
                .name("admin")
                .build();

        Member supervisor1 = Member.builder()
                .name("supervisor1")
                .birthDate("20000523")
                .code("SE0001")
                .phoneNumber("010-1111-1111")
                .email("testEmail")
                .gender(Gender.남자)
                .major(Major.컴퓨터소프트웨어공학과)
                .position(Position.교수)
                .build();

        Member supervisor2 = Member.builder()
                .name("supervisor2")
                .birthDate("20000523")
                .code("SE0002")
                .phoneNumber("010-3333-3333")
                .email("testEmail")
                .gender(Gender.여자)
                .major(Major.컴퓨터소프트웨어공학과)
                .position(Position.교수)
                .build();

        Member president1 = Member.builder()
                .name("testRequestor")
                .birthDate("20000523")
                .code("20190001")
                .phoneNumber("010-1111-2222")
                .email("testEmail")
                .gender(Gender.남자)
                .major(Major.컴퓨터소프트웨어공학과)
                .position(Position.학생)
                .build();

        Member president2 = Member.builder()
                .name("testRequestor1")
                .birthDate("20000523")
                .code("20190002")
                .phoneNumber("010-1122-3344")
                .email("testEmail")
                .gender(Gender.여자)
                .major(Major.컴퓨터소프트웨어공학과)
                .position(Position.학생)
                .build();

        List<Club> clubs = new ArrayList<>();
        Club club = Club.builder()
                .id(1L)
                .clubType(ClubType.중앙)
                .name("testClub")
                .supervisor(supervisor1)
                .president(president1)
                .status(Status.검토)
                .build();

        Club club2 = Club.builder()
                .id(2L)
                .clubType(ClubType.중앙)
                .name("testClub2")
                .supervisor(supervisor2)
                .president(president2)
                .status(Status.검토)
                .build();

        clubs.add(club);
        clubs.add(club2);

        given(adminServiceFacade.isAdmin(admin.getId())).willReturn(true);
        given(adminServiceFacade.findByStatus(Status.검토)).willReturn(clubs);
        mockMvc.perform(get("/admin/checkList/{adminId}", admin.getId())
                        .sessionAttr(SessionConst.LOGIN_ADMIN, admin)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("checkList-inquiry-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestBody(),
                        responseBody()));
    }

}