package awpterm.backend.api.response.club;

import awpterm.backend.domain.Club;
import awpterm.backend.domain.ClubMember;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClubResponseDTO {
    private Long id;
    private String clubType;
    private String name;
    private String status;

    public static ClubResponseDTO valueOf(Club club) {
        return ClubResponseDTO.builder()
                .id(club.getId())
                .clubType(club.getClubType().toString())
                .name(club.getName())
                .status(club.getStatus().toString())
                .build();
    }

    public static ClubResponseDTO valueOf(ClubMember clubMember) {
        return ClubResponseDTO.builder()
                .id(clubMember.getClub().getId())
                .clubType(clubMember.getClub().getClubType().toString())
                .name(clubMember.getClub().getName())
                .status(clubMember.getClub().getStatus().toString())
                .build();
    }
}
