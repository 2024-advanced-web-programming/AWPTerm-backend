package awpterm.backend.service;

import awpterm.backend.api.request.club.ClubRegisterRequestDTO;
import awpterm.backend.domain.Club;
import awpterm.backend.domain.ClubMaster;
import awpterm.backend.domain.Member;
import awpterm.backend.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClubServiceFacade {
    private final ClubService clubService;
    private final MemberService memberService;

    public boolean isValidMember(String memberId) {
        return memberService.isValidMember(memberId);
    }

    public List<Club> register(ClubRegisterRequestDTO clubRegisterRequestDTO) {
        Club club = clubRegisterRequestDTO.toEntity();
        Member president = memberService.findById(clubRegisterRequestDTO.getRequestorId());
        Member supervisor = memberService.findById(clubRegisterRequestDTO.getSupervisorId());
        ClubMaster clubMaster = ClubMaster.builder().club(club).master(president).build();

        club.setPresident(president);
        club.setSupervisor(supervisor);
        club.getMasters().add(clubMaster);
        club.setStatus(Status.검토);

        return clubService.register(club);
    }
}