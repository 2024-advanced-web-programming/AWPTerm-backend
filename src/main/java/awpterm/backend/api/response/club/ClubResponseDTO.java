package awpterm.backend.api.response.club;

import awpterm.backend.api.response.member.MemberResponseDTO;
import awpterm.backend.domain.Club;
import awpterm.backend.domain.ClubMember;
import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;

@Data
@Builder
public class ClubResponseDTO {
    private Long id;
    private MemberResponseDTO president;
    private UrlResource representativePicture;
    private String introduction;
    private String clubType;
    private String name;
    private String status;

    public static ClubResponseDTO valueOf(Club club) throws MalformedURLException {
        return ClubResponseDTO.builder()
                .id(club.getId())
                .president(MemberResponseDTO.valueOf(club.getPresident()))
                .representativePicture(club.getClubDetail().getRepresentativePicture().getResource())
                .introduction(club.getClubDetail().getIntroduction())
                .clubType(club.getClubType().toString())
                .name(club.getName())
                .status(club.getStatus().toString())
                .build();
    }

    public static ClubResponseDTO valueOf(ClubMember clubMember) throws MalformedURLException {
        return ClubResponseDTO.builder()
                .id(clubMember.getClub().getId())
                .president(MemberResponseDTO.valueOf(clubMember.getClub().getPresident()))
                .representativePicture(clubMember.getClub().getClubDetail().getRepresentativePicture().getResource())
                .introduction(clubMember.getClub().getClubDetail().getIntroduction())
                .clubType(clubMember.getClub().getClubType().toString())
                .name(clubMember.getClub().getName())
                .status(clubMember.getClub().getStatus().toString())
                .build();
    }
}
