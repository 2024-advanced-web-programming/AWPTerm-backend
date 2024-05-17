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
    private String requestorId;
    private String requestorName;
    private String requestorMajor;
    private String requestorCode;
    private String requestorPhoneNumber;
    private String supervisorId;
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
                .requestorId(club.getPresident().getId())
                .requestorName(club.getPresident().getName())
                .requestorMajor(club.getPresident().getMajor().toString())
                .requestorCode(club.getPresident().getCode())
                .requestorPhoneNumber(club.getPresident().getPhoneNumber())
                .supervisorId(club.getSupervisor().getId())
                .supervisorName(club.getSupervisor().getName())
                .supervisorMajor(club.getSupervisor().getMajor().toString())
                .supervisorPhoneNumber(club.getSupervisor().getPhoneNumber())
                .build();
    }
}