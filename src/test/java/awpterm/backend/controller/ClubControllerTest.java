package awpterm.backend.controller;

import awpterm.backend.api.request.club.ClubRegisterRequestDTO;
import awpterm.backend.domain.Club;
import awpterm.backend.enums.ClubType;
import awpterm.backend.enums.Major;
import awpterm.backend.service.ClubServiceFacade;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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

        Club club = clubServiceFacade.register(request);
        Club findClub = clubServiceFacade.findById(club.getId());

        assertThat(club).isEqualTo(findClub);
    }
}