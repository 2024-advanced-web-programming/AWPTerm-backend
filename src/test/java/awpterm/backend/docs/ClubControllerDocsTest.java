package awpterm.backend.docs;

import awpterm.backend.api.request.club.ClubApplicationDecisionDTO;
import awpterm.backend.api.request.club.ClubApplicationRequestDTO;
import awpterm.backend.api.request.club.ClubRegisterRequestDTO;
import awpterm.backend.api.request.club.ClubUpdateBasicInfoDTO;
import awpterm.backend.api.response.club.ClubApplicationResponseDTO;
import awpterm.backend.api.response.club.ClubInquiryBasicInfoDTO;
import awpterm.backend.api.response.club.ClubResponseDTO;
import awpterm.backend.controller.ClubController;
import awpterm.backend.domain.*;
import awpterm.backend.enums.*;
import awpterm.backend.etc.SessionConst;
import awpterm.backend.service.ClubServiceFacade;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestBody;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseBody;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

        ClubResponseDTO response = ClubResponseDTO.builder()
                .id(1L)
                .clubType(ClubType.중앙.toString())
                .name("testClub")
                .status(Status.검토.toString())
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

    @Test
    void 동아리_가입_신청_테스트() throws Exception {
        Member requestor = Member.builder()
                .name("testRequestor")
                .birthDate("2000-00-00")
                .code("20190001")
                .phoneNumber("010-1111-1111")
                .email("test@kumoh.ac.kr")
                .gender(Gender.여자)
                .major(Major.컴퓨터소프트웨어공학과)
                .position(Position.학생)
                .build();

        ClubApplicationRequestDTO request = ClubApplicationRequestDTO.builder()
                .clubId(1L)
                .multipartFile(null)
                .build();

        ClubApplicationResponseDTO response = ClubApplicationResponseDTO.builder()
                .code(requestor.getCode())
                .name(requestor.getName())
                .applicationForm(null)
                .build();

        MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, new Member());
        given(clubServiceFacade.apply(requestor, request)).willReturn(response);
        mockMvc.perform(post("/club/application")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .session(session)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("club-application",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestBody(),
                        responseBody()));
    }

    @Test
    void 동아리_리스트_테스트() throws Exception {
        List<ClubApplicationResponseDTO> applicationResponseDTOS = new ArrayList<>();
        applicationResponseDTOS.add(
                ClubApplicationResponseDTO.builder()
                        .code("20190001")
                        .name("testMember1")
                        .applicationForm(null)
                        .build()
        );
        applicationResponseDTOS.add(
                ClubApplicationResponseDTO.builder()
                        .code("20190002")
                        .name("testMember2")
                        .applicationForm(null)
                        .build()
        );

        given(clubServiceFacade.findById(any())).willReturn(new Club());
        given(clubServiceFacade.getApplicationList(1L)).willReturn(applicationResponseDTOS);
        mockMvc.perform(get("/club/application/list/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("club-application-list-right",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestBody(),
                        responseBody()));

        given(clubServiceFacade.findById(any())).willReturn(null);
        mockMvc.perform(get("/club/application/list/1L")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(document("club-application-list-wrong",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestBody(),
                        responseBody()));
    }

    @Test
    void 동아리_신청_결정_테스트() throws Exception {
        ClubApplicationDecisionDTO request = ClubApplicationDecisionDTO.builder()
                .clubId(1L)
                .memberId("testId")
                .isApproval(true)
                .build();

        mockMvc.perform(post("/club/application/decision")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("club-application-decision",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestBody(),
                        responseBody()));
    }

    @Test
    void 동아리_회원탈퇴_테스트() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("clubId", "1");
        params.add("memberId", "testId");

        mockMvc.perform(delete("/club/dismiss")
                        .contentType(MediaType.APPLICATION_JSON)
                        .params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("club-dismiss",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestBody(),
                        responseBody()));
    }

    @Test
    void 동아리_신청_승인() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("clubId", "1");
        params.add("status", Status.승인.toString());
        params.add("rejectReason", "");

        Member supervisor1 = Member.builder()
                .name("testReuqestor")
                .birthDate("20000523")
                .code("20190001")
                .phoneNumber("010-1111-1111")
                .email("testEmail")
                .gender(Gender.남자)
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

        Club club = Club.builder()
                .id(1L)
                .clubType(ClubType.중앙)
                .name("testClub")
                .supervisor(supervisor1)
                .president(president1)
                .status(Status.검토)
                .build();

        given(clubServiceFacade.updateStatus(club.getId(), Status.승인.toString(), "")).willReturn(true);
        mockMvc.perform(put("/club/updateStatus").contentType(MediaType.APPLICATION_JSON)
                        .params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("club-updateStatus-accept-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestBody(),
                        responseBody()));

        given(clubServiceFacade.updateStatus(club.getId(), Status.승인.toString(), null)).willReturn(false);
        mockMvc.perform(put("/club/updateStatus").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(document("club-updateStatus-accept-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestBody(),
                        responseBody()));
    }

    @Test
    void 동아리_신청_거절() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("clubId", "1");
        params.add("status", Status.거절.toString());
        params.add("rejectReason", "부적절한 이유로 거절되었습니다.");

        Member supervisor1 = Member.builder()
                .name("testReuqestor")
                .birthDate("20000523")
                .code("20190001")
                .phoneNumber("010-1111-1111")
                .email("testEmail")
                .gender(Gender.남자)
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

        Club club = Club.builder()
                .id(1L)
                .clubType(ClubType.중앙)
                .name("testClub")
                .supervisor(supervisor1)
                .president(president1)
                .status(Status.검토)
                .build();

        given(clubServiceFacade.updateStatus(club.getId(), Status.거절.toString(), "부적절한 이유로 거절되었습니다.")).willReturn(true);
        mockMvc.perform(put("/club/updateStatus").contentType(MediaType.APPLICATION_JSON)
                        .params(params))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("club-updateStatus-reject-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestBody(),
                        responseBody()));

        given(clubServiceFacade.updateStatus(club.getId(), Status.거절.toString(), "부적절한 이유로 거절되었습니다.")).willReturn(false);
        mockMvc.perform(put("/club/updateStatus").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(document("club-updateStatus-reject-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestBody(),
                        responseBody()));
    }

    @Test
    void 기본_정보_관리() throws Exception { //TODO Docs 작성 마무리
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

        Member vicePresident = Member.builder()
                .name("vicePresident")
                .birthDate("2000-11-11")
                .code("20190002")
                .phoneNumber("010-2222-3333")
                .email("test123@kumoh.ac.kr")
                .gender(Gender.남자)
                .major(Major.컴퓨터소프트웨어공학과)
                .position(Position.학생)
                .build();

        Member secretary = Member.builder()
                .name("secretary")
                .birthDate("2000-11-11")
                .code("20190003")
                .phoneNumber("010-2222-3333")
                .email("test123@kumoh.ac.kr")
                .gender(Gender.남자)
                .major(Major.컴퓨터소프트웨어공학과)
                .position(Position.학생)
                .build();

        List<ClubMember> members = new ArrayList<>();
        members.add(ClubMember.builder()
                .club(request.toEntity())
                .member(president)
                .build());

        members.add(ClubMember.builder()
                .club(request.toEntity())
                .member(vicePresident)
                .build());

        members.add(ClubMember.builder()
                .club(request.toEntity())
                .member(secretary)
                .build());


        ClubUpdateBasicInfoDTO clubUpdateBasicInfoDTO = ClubUpdateBasicInfoDTO.builder()
                .id(1L)
                .name(request.getName())
                .introduce("간단한 소개")
                .regularMeetingTime(LocalDateTime.now().toString())
                .vicePresident(vicePresident)
                .secretary(secretary)
                .members(members)
                .build();

        String img_name = "image";
        String img_contentType = "multipart/form-data";
        String img_path = "test.jpg";


        MockMultipartFile representativePicture = new MockMultipartFile(img_name, img_contentType, img_path, img_path.getBytes(StandardCharsets.UTF_8));

        String file_name = "file";
        String file_contentType = "multipart/form-data";
        String file_path = "test.text";

        MockMultipartFile registerFile = new MockMultipartFile(file_name, file_contentType, file_path, file_path.getBytes(StandardCharsets.UTF_8));

    }

    @Test
    void 기본_정보_조회() throws Exception { //TODO Docs 작성 마무리

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

        Member vicePresident = Member.builder()
                .name("vicePresident")
                .birthDate("2000-11-11")
                .code("20190002")
                .phoneNumber("010-2222-3333")
                .email("test123@kumoh.ac.kr")
                .gender(Gender.남자)
                .major(Major.컴퓨터소프트웨어공학과)
                .position(Position.학생)
                .build();

        Member secretary = Member.builder()
                .name("secretary")
                .birthDate("2000-11-11")
                .code("20190003")
                .phoneNumber("010-2222-3333")
                .email("test123@kumoh.ac.kr")
                .gender(Gender.남자)
                .major(Major.컴퓨터소프트웨어공학과)
                .position(Position.학생)
                .build();

        List<ClubMember> members = new ArrayList<>();
        members.add(ClubMember.builder()
                .club(request.toEntity())
                .member(president)
                .build());

        members.add(ClubMember.builder()
                .club(request.toEntity())
                .member(vicePresident)
                .build());

        members.add(ClubMember.builder()
                .club(request.toEntity())
                .member(secretary)
                .build());

        ClubInquiryBasicInfoDTO res = ClubInquiryBasicInfoDTO.builder()
                .id(1L)
                .name(request.getName())
                .introduce("간단한 소개")
                .regularMeetingTime(LocalDateTime.now())
                .vicePresident(vicePresident)
                .secretary(secretary)
                .members(members)
                .registerFileId(1L)
                .representativePictureURL("Image URL")
                .build();

    }
}
