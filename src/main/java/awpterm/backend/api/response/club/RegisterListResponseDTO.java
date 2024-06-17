package awpterm.backend.api.response.club;

import awpterm.backend.domain.Club;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterListResponseDTO {
    private Long id;
    private String clubType;
    private String clubName;
    private String applicantName;
    private String applicantMajor;
    private String applicantCode;
    private String applicantPhoneNumber;
    private String supervisorName;
    private String supervisorMajor;
    private String supervisorPhoneNumber;
    private String status;

    public static RegisterListResponseDTO valueOf(Club club) {
        return RegisterListResponseDTO.builder()
                .id(club.getId())
                .clubType(club.getClubType().toString())
                .clubName(club.getName())
                .applicantName(club.getPresident().getName())
                .applicantMajor(club.getPresident().getMajor().toString())
                .applicantCode(club.getPresident().getCode())
                .applicantPhoneNumber(club.getPresident().getPhoneNumber())
                .supervisorName(club.getSupervisor().getName())
                .supervisorMajor(club.getSupervisor().getMajor().toString())
                .supervisorPhoneNumber(club.getSupervisor().getPhoneNumber())
                .status(club.getStatus().toString())
                .build();
    }
}
