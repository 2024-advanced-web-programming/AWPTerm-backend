package awpterm.backend.api.request.club;


import awpterm.backend.domain.Club;
import awpterm.backend.domain.ClubMember;
import awpterm.backend.domain.FileProperty;
import awpterm.backend.domain.Member;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ClubBasicInfoDTO { //요청과 응답에 공통적인 DTO로 사용 예정
    private String name;
    private String introduce;
    private String history;
    private LocalDateTime regularMeetingTime;
    private Member vicePresident; // 부대표
    private Member secretary; // 총무

    @Builder.Default
    private List<ClubMember> members = new ArrayList<>();

    public static ClubBasicInfoDTO of(Club club) {

        return ClubBasicInfoDTO.builder()
                .name(club.getName())
                .introduce(club.getClubDetail().getIntroduction())
                .history(club.getClubDetail().getHistory())
                .regularMeetingTime(club.getClubDetail().getRegularMeetingTime())
                .vicePresident(club.getVicePresident())
                .secretary(club.getSecretary())
                .members(club.getMembers())
                .build();
    }
}
