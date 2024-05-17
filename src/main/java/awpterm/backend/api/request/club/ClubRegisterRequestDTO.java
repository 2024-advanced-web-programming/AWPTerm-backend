package awpterm.backend.api.request.club;

import awpterm.backend.domain.Club;
import awpterm.backend.enums.ClubType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClubRegisterRequestDTO {
    private String clubType;
    private String name;
    private String requestorCode;
    private String requestorName;
    private String requestorMajor;
    private String requestorPhoneNumber;
    private String supervisorCode;
    private String supervisorName;
    private String supervisorMajor;
    private String supervisorPhoneNumber;

    public Club toEntity() {
        return Club.builder()
                .clubType(ClubType.valueOf(clubType))
                .name(name)
                .build();
    }

    public static ClubRegisterRequestDTO of(Club club) {
        return ClubRegisterRequestDTO.builder()
                .clubType(club.getClubType().toString())
                .name(club.getName())
                .requestorName(club.getPresident().getName())
                .requestorMajor(club.getPresident().getMajor().toString())
                .requestorCode(club.getPresident().getCode())
                .requestorPhoneNumber(club.getPresident().getPhoneNumber())
                .supervisorCode(club.getSupervisor().getCode())
                .supervisorName(club.getSupervisor().getName())
                .supervisorMajor(club.getSupervisor().getMajor().toString())
                .supervisorPhoneNumber(club.getSupervisor().getPhoneNumber())
                .build();
    }
}