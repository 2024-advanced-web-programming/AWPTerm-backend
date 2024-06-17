package awpterm.backend.controller;

import awpterm.backend.api.request.club.ClubRegisterRequestDTO;
import awpterm.backend.api.response.club.ClubResponseDTO;
import awpterm.backend.domain.Admin;
import awpterm.backend.domain.Club;
import awpterm.backend.enums.ClubType;
import awpterm.backend.enums.Major;
import awpterm.backend.enums.Status;
import awpterm.backend.repository.AdminRepository;
import awpterm.backend.repository.ClubRepository;
import awpterm.backend.service.AdminServiceFacade;
import awpterm.backend.service.ClubService;
import awpterm.backend.service.ClubServiceFacade;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AdminControllerTest {
    @Autowired
    private AdminServiceFacade adminServiceFacade;
    @Autowired
    private ClubService clubService;
    @Autowired
    private ClubServiceFacade clubServiceFacade;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private AdminRepository adminRepository;

    @Test
    void 동아리_신청목록_확인() {

        Admin admin = Admin.builder()
                .id("testId")
                .password("testPw")
                .name("admin")
                .build();

        adminRepository.save(admin); //테스트 Admin 저장

        ClubRegisterRequestDTO req1 = ClubRegisterRequestDTO.builder()
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
        ClubRegisterRequestDTO req2 = ClubRegisterRequestDTO.builder()
                .clubType(ClubType.학과.toString())
                .name("testClub2")
                .requestorCode("20190002")
                .requestorName("testRequestor2")
                .requestorMajor(Major.컴퓨터소프트웨어공학과.toString())
                .requestorPhoneNumber("010-2222-2222")
                .supervisorCode("SE0002")
                .supervisorName("testSuperVisor2")
                .supervisorMajor(Major.컴퓨터소프트웨어공학과.toString())
                .supervisorPhoneNumber("010-3333-3333")
                .build();

        ClubResponseDTO club = clubServiceFacade.register(req1); //저장
        ClubResponseDTO club1 = clubServiceFacade.register(req2); // 저장

        if(adminServiceFacade.isAdmin(admin.getId())) {
            List<Club> clubList = clubService.findByStatus(Status.검토);
            //DB에 테스트 동아리라는 값이 들어가있어서 0 idx를 제외하고 검사
            assertThat(club.getId()).isEqualTo(clubList.get(1).getId());
            assertThat(club1.getId()).isEqualTo(clubList.get(2).getId());
        }
    }
    @Test
    void 동아리_신청_승인() {
        ClubRegisterRequestDTO req1 = ClubRegisterRequestDTO.builder()
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

        ClubResponseDTO club = clubServiceFacade.register(req1);

        if(clubService.updateStatus(club.getId(), Status.승인.toString(), null)) {
            Club findClub = clubRepository.findById(club.getId()).orElse(null);
            assertThat(findClub.getStatus()).isEqualTo(Status.승인);
        }
    }
    @Test
    void 동아리_신청_거절() {
        ClubRegisterRequestDTO req1 = ClubRegisterRequestDTO.builder()
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

        ClubResponseDTO club = clubServiceFacade.register(req1);

        if(clubService.updateStatus(club.getId(), Status.거절.toString(), "학번 입력이 잘못되었습니다.")) {
            Club findClub = clubRepository.findById(club.getId()).orElse(null);
            assertThat(findClub.getStatus()).isEqualTo(Status.거절);
        }
    }
}