package awpterm.backend.api.request.club;

import awpterm.backend.domain.Club;
import awpterm.backend.enums.ClubType;
import awpterm.backend.enums.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClubStatusRequestDTO {
    private String clubType;
    private String name;
    private String status;

    public Club toEntity() {
        return Club.builder()
                .clubType(ClubType.valueOf(clubType))
                .name(name)
                .status(Status.valueOf(status))
                .build();
    }

    public static ClubStatusRequestDTO of(Club club) {
        return ClubStatusRequestDTO.builder()
                .clubType(club.getClubType().toString())
                .name(club.getName())
                .status(club.getStatus().toString())
                .build();
    }
}
