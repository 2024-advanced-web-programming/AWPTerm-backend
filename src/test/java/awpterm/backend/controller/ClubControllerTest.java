package awpterm.backend.controller;

import awpterm.backend.api.response.club.ClubInquiryBasicInfoDTO;
import awpterm.backend.api.request.club.ClubUpdateBasicInfoDTO;
import awpterm.backend.api.request.club.ClubRegisterRequestDTO;
import awpterm.backend.api.response.club.ClubResponseDTO;
import awpterm.backend.domain.*;
import awpterm.backend.enums.*;
import awpterm.backend.service.ClubServiceFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class ClubControllerTest {
    @Autowired
    private ClubServiceFacade clubServiceFacade;

    @Test
    void registerTest() {
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

        ClubResponseDTO club = clubServiceFacade.register(request);
        Club findClub = clubServiceFacade.findById(club.getId());

//        assertThat(club).isEqualTo(findClub);
    }

    @Test
    void 기본_정보_관리() {
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

        ClubResponseDTO club = clubServiceFacade.register(request);

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
                .id(club.getId())
                .name(request.getName())
                .introduce("간단한 소개")
                .regularMeetingTime(LocalDateTime.now().toString())
                .vicePresident(vicePresident)
                .secretary(secretary)
                .members(members)
                .build();

        MockMultipartFile registerFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "This is a dummy file content.".getBytes()
        );

        boolean result = clubServiceFacade.updateBasicInfo(clubUpdateBasicInfoDTO, null, registerFile);

        assertThat(result).isEqualTo(true);
    }

    @Test
    void 동아리_기본_정보_조회() {
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

        ClubResponseDTO club = clubServiceFacade.register(request);

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
                .id(club.getId())
                .name(request.getName())
                .introduce("간단한 소개")
                .regularMeetingTime(LocalDateTime.now().toString())
                .vicePresident(vicePresident)
                .secretary(secretary)
                .members(members)
                .build();

        MockMultipartFile registerFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "This is a dummy file content.".getBytes()
        );
        boolean result = clubServiceFacade.updateBasicInfo(clubUpdateBasicInfoDTO, null, registerFile);
        if(result) {
            ClubInquiryBasicInfoDTO res = clubServiceFacade.getClubInfo(club.getId());
            assertThat(res.getId()).isEqualTo(club.getId());
            assertThat(res.getRegularMeetingTime()).isEqualTo(clubUpdateBasicInfoDTO.getRegularMeetingTime());
        }

    }
}