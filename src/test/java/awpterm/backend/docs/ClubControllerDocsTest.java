package awpterm.backend.docs;

import awpterm.backend.api.request.club.ClubRegisterRequestDTO;
import awpterm.backend.controller.ClubController;
import awpterm.backend.domain.Club;
import awpterm.backend.domain.ClubMaster;
import awpterm.backend.domain.Member;
import awpterm.backend.enums.*;
import awpterm.backend.service.ClubServiceFacade;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestBody;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseBody;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ClubControllerDocsTest extends RestDocsTest {
    private final ClubServiceFacade clubServiceFacade = mock(ClubServiceFacade.class);


    @Override
    protected Object initController() {
        return new ClubController(clubServiceFacade);
    }

    @Test
    void 동아리_신청_테스트() throws Exception {
        ClubRegisterRequestDTO request = ClubRegisterRequestDTO.builder()
                .clubType(ClubType.중앙.toString())
                .name("testClub")
                .requestorCode("20190001")
                .requestorName("testRequestor")
                .requestorMajor(Major.컴퓨터소프트웨어공학과.toString())
                .requestorPhoneNumber("010-1111-1111")
                .supervisorCode("SE0001")
                .supervisorName("testSuperVisor")
                .supervisorMajor(Major.컴퓨터소프트웨어공학과.toString())
                .supervisorPhoneNumber("010-2222-2222")
                .build();

        Member president = Member.builder()
                .name("testRequestor")
                .birthDate("2000-00-00")
                .code("20190001")
                .phoneNumber("010-1111-1111")
                .email("test@kumoh.ac.kr")
                .gender(Gender.여자)
                .major(Major.컴퓨터소프트웨어공학과)
                .position(Position.학생)
                .build();

        Member supervisor = Member.builder()
                .name("testSuperVisor")
                .birthDate("2000-00-00")
                .code("SE0001")
                .phoneNumber("010-2222-2222")
                .email("SE1@kumoh.ac.kr")
                .gender(Gender.여자)
                .major(Major.컴퓨터소프트웨어공학과)
                .position(Position.교수)
                .build();
        List<ClubMaster> masters = new ArrayList<>();
        masters.add(ClubMaster.builder()
                .club(request.toEntity())
                .master(president)
                .build());

        Club response = Club.builder()
                .clubType(ClubType.중앙)
                .name("testClub")
                .supervisor(supervisor)
                .president(president)
                .masters(masters)
                .status(Status.검토)
                .build();

        given(clubServiceFacade.register(request)).willReturn(response);
        mockMvc.perform(post("/club/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(document("club-register-wrong",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestBody(),
                        responseBody()));

        given(clubServiceFacade.isValidMemberByCode(any())).willReturn(true);
        given(clubServiceFacade.register(request)).willReturn(response);
        mockMvc.perform(post("/club/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("club-register-right",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestBody(),
                        responseBody()));
    }
}
