package awpterm.backend.api.request.club;


import awpterm.backend.domain.Club;
import awpterm.backend.domain.ClubMember;
import awpterm.backend.domain.Member;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ClubUpdateBasicInfoDTO { //요청과 응답에 공통적인 DTO로 사용 예정
    private Long id;
    private String name;
    private String introduce;
    private String regularMeetingTime;

    @Nullable
    private Member vicePresident; // 부대표
    @Nullable
    private Member secretary; // 총무

    @Builder.Default
    private List<ClubMember> members = new ArrayList<>();

    public static ClubUpdateBasicInfoDTO of(Club club) {

        return ClubUpdateBasicInfoDTO.builder()
                .id(club.getId())
                .name(club.getName())
                .introduce(club.getClubDetail().getIntroduction())
                .regularMeetingTime(club.getClubDetail().getRegularMeetingTime().toString())
                .vicePresident(club.getVicePresident())
                .secretary(club.getSecretary())
                .members(club.getMembers())
                .build();
    }
}
