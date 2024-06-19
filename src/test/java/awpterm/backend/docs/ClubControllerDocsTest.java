package awpterm.backend.docs;

import awpterm.backend.api.request.club.*;
import awpterm.backend.api.response.club.ClubApplicationResponseDTO;
import awpterm.backend.api.response.club.ClubInquiryBasicInfoDTO;
import awpterm.backend.api.response.club.ClubMemberResponseDTO;
import awpterm.backend.api.response.club.ClubResponseDTO;
import awpterm.backend.controller.ClubController;
import awpterm.backend.domain.*;
import awpterm.backend.enums.*;
import awpterm.backend.etc.SessionConst;
import awpterm.backend.service.ClubServiceFacade;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static awpterm.backend.etc.MakeMockClub.makeMockClub;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestBody;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseBody;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
                .applicantName(requestor.getName())
                .applicantCode(requestor.getCode())
                .applicantMajor(requestor.getMajor().toString())
                .build();

        String content = objectMapper.writeValueAsString(request);
        MockMultipartFile requestDTOFile = new MockMultipartFile("clubApplicationRequestDTO", "JsonData", "application/json", content.getBytes(StandardCharsets.UTF_8));

        mockMvc.perform(multipart("/club/application")
                        .file(requestDTOFile)
                        .sessionAttr(SessionConst.LOGIN_MEMBER, requestor)
                        .accept(MediaType.APPLICATION_JSON))
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

        given(clubServiceFacade.findById(any())).willReturn(ClubResponseDTO.valueOf(makeMockClub()));
        mockMvc.perform(get("/club/application/list/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("club-application-list",
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
        ClubUpdateStatusRequestDTO requestDTO = ClubUpdateStatusRequestDTO.builder()
                .clubId(1L)
                .rejectedReason("")
                .status(Status.승인.toString())
                .build();

        given(clubServiceFacade.updateStatus(requestDTO.getClubId(), requestDTO.getStatus(), requestDTO.getRejectedReason())).willReturn(true);
        mockMvc.perform(put("/club/updateStatus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("club-updateStatus-accept-success",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestBody(),
                        responseBody()));
    }

    @Test
    void 동아리_신청_거절() throws Exception {
        ClubUpdateStatusRequestDTO requestDTO = ClubUpdateStatusRequestDTO.builder()
                .clubId(1L)
                .rejectedReason("거절 사유: 불순한 의도의 동아리 이름입니다.")
                .status(Status.거절.toString())
                .build();

        given(clubServiceFacade.updateStatus(requestDTO.getClubId(), requestDTO.getStatus(), requestDTO.getRejectedReason())).willReturn(true);
        mockMvc.perform(put("/club/updateStatus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("club-updateStatus-accept-fail",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestBody(),
                        responseBody()));
    }

    @Test
    void 모든_동아리_조회() throws Exception {

        List<ClubResponseDTO> responseDTOS = new ArrayList<>();
        responseDTOS.add(
                ClubResponseDTO.builder()
                        .id(1L)
                        .presidentName("대표자1")
                        .representativePicture(null)
                        .introduction(null)
                        .clubType(ClubType.중앙.toString())
                        .name("동아리 1")
                        .supervisorName("교수1")
                        .status(Status.승인.toString())
                        .rejectedReason("")
                        .regularMeetingTime(null)
                        .build()
        );
        responseDTOS.add(
                ClubResponseDTO.builder()
                        .id(2L)
                        .presidentName("대표자2")
                        .representativePicture(null)
                        .introduction(null)
                        .clubType(ClubType.학과.toString())
                        .name("동아리 2")
                        .supervisorName("교수2")
                        .status(Status.승인.toString())
                        .rejectedReason("")
                        .regularMeetingTime(null)
                        .build()
        );
        responseDTOS.add(
                ClubResponseDTO.builder()
                        .id(3L)
                        .presidentName("대표자3")
                        .representativePicture(null)
                        .introduction(null)
                        .clubType(ClubType.중앙.toString())
                        .name("동아리 3")
                        .supervisorName("교수3")
                        .status(Status.승인.toString())
                        .rejectedReason("")
                        .regularMeetingTime(null)
                        .build()
        );

        given(clubServiceFacade.findByStatus(Status.승인)).willReturn(responseDTOS);
        mockMvc.perform(get("/club/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("club-all-inquiry",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @Test
    void 기본_정보_관리() throws Exception {
        Club club = makeMockClub();
        given(clubServiceFacade.getClubInfo(1L)).willReturn(ClubInquiryBasicInfoDTO.of(club));

        mockMvc.perform(get("/club/inquiryInfo/{clubId}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("club-inquiry-info-clubId",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );
    }

    @Test
    void 아이디로_동아리_찾기() throws Exception {
        Club club = makeMockClub();

        given(clubServiceFacade.findById(1L)).willReturn(ClubResponseDTO.valueOf(club));
        mockMvc.perform(get("/club/{clubId}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("club-find-clubId",
                                preprocessRequest(prettyPrint()),
                                preprocessResponse(prettyPrint())
                        )
                );

    }

    @Test
    void 기본_정보_수정() throws Exception {

        // 파일 이름
        String fileName = "applicationForm.txt";

        // 파일 내용
        String fileContent = "Name: John Doe\n"
                + "Email: john.doe@example.com\n"
                + "Phone: 123-456-7890\n"
                + "Address: 123 Main St, Anytown, USA\n";

        // MockMultipartFile 객체 생성
        MockMultipartFile applicationForm = new MockMultipartFile(
                "file",                 // 파라미터 이름
                fileName,               // 원본 파일 이름
                "text/plain",           // 파일 타입
                fileContent.getBytes()  // 파일 내용
        );

        // 이미지 파일 이름
        String fileName2 = "dummyImage.png";

        // 1x1 픽셀 이미지 생성
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        image.setRGB(0, 0, Color.RED.getRGB()); // 픽셀 색상을 빨간색으로 설정

        // 이미지를 바이트 배열로 변환
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);
        byte[] imageBytes = baos.toByteArray();

        // MockMultipartFile 객체 생성
        MockMultipartFile clubPhoto = new MockMultipartFile(
                "file",           // 파라미터 이름
                fileName2,         // 원본 파일 이름
                "image/png",      // 파일 타입
                imageBytes        // 파일 내용
        );

        ClubUpdateBasicInfoRequestDTO request = ClubUpdateBasicInfoRequestDTO.valueOf(makeMockClub());


        //void의 경우 이렇게 처리
        willDoNothing().given(clubServiceFacade).updateBasicInfo(1L, request, applicationForm, clubPhoto);

        // When & Then
        mockMvc.perform(multipart(HttpMethod.PUT,"/club/{clubId}", 1L)
                        .file(applicationForm)
                        .file(clubPhoto)
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
