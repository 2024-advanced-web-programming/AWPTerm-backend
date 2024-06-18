package awpterm.backend.api.request.club;


import awpterm.backend.domain.Club;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class ClubUpdateBasicInfoRequestDTO { //요청과 응답에 공통적인 DTO로 사용 예정
    private String clubName;
    private String introduction;
    private String regularMeetingTime;
    private String presidentId;
    private String vicePresidentId;
    private String secretaryId;

    public static ClubUpdateBasicInfoRequestDTO valueOf(Club club) {
        return ClubUpdateBasicInfoRequestDTO.builder()
                .clubName(club.getName())
                .introduction(club.getClubDetail().getIntroduction())
                .regularMeetingTime(club.getClubDetail().getRegularMeetingTime())
                .presidentId(club.getPresident().getId())
                .vicePresidentId(club.getVicePresident().getId())
                .secretaryId(club.getSecretary().getId())
                .build();
    }
}
