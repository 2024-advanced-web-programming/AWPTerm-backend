package awpterm.backend.etc;

import awpterm.backend.domain.*;
import awpterm.backend.enums.*;
import org.springframework.mock.web.MockMultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MakeMockClub {

    public static Club makeMockClub() throws IOException {

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

        Club club = Club.builder()
                .id(1L)
                .clubType(ClubType.중앙)
                .name("test")
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
                .club(club)
                .member(president)
                .build());

        members.add(ClubMember.builder()
                .club(club)
                .member(vicePresident)
                .build());

        members.add(ClubMember.builder()
                .club(club)
                .member(secretary)
                .build());
        club.setPresident(president);
        club.setVicePresident(vicePresident);
        club.setSecretary(secretary);
        club.setMembers(members);
        club.setSupervisor(supervisor);
        club.setStatus(Status.검토);
        club.setClubDetail(ClubDetail.builder()
                .representativePicture(FileProperty.valueOf(clubPhoto))
                .registerFile(FileProperty.valueOf(applicationForm))
                .introduction("introduction")
                .regularMeetingTime("time")
                .build());

        return club;
    }
}
