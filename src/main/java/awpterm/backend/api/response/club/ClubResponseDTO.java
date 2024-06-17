package awpterm.backend.api.response.club;

import awpterm.backend.api.response.member.MemberResponseDTO;
import awpterm.backend.domain.Club;
import awpterm.backend.domain.ClubDetail;
import awpterm.backend.domain.ClubMember;
import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ClubResponseDTO {
    private Long id;
    private String presidentName;
    private UrlResource representativePicture;
    private String introduction;
    private String clubType;
    private String name;
    private String supervisorName;
    private String status;
    private String rejectedReason;
    private LocalDateTime regularMeetingTime;
    private List<ClubMemberResponseDTO> members;

    public static ClubResponseDTO valueOf(Club club) {
        UrlResource representativePictureResource = null;
        String introduction = null;
        LocalDateTime regularMeetingTime = null;

        if (club.getClubDetail() != null) {
            if (club.getClubDetail().getRepresentativePicture() != null) {
                try {
                    representativePictureResource = club.getClubDetail().getRepresentativePicture().getResource();
                } catch (MalformedURLException e) {}
            }
            introduction = club.getClubDetail().getIntroduction();
            regularMeetingTime = club.getClubDetail().getRegularMeetingTime();
        }

        return ClubResponseDTO.builder()
                .id(club.getId())
                .presidentName(club.getPresident().getName())
                .representativePicture(representativePictureResource)
                .introduction(introduction)
                .clubType(club.getClubType().toString())
                .name(club.getName())
                .supervisorName(club.getSupervisor().getName())
                .status(club.getStatus().toString())
                .rejectedReason(club.getRejectReason())
                .regularMeetingTime(regularMeetingTime)
                .members(ClubMemberResponseDTO.valueOf(club, club.getMembers()))
                .build();
    }

    public static ClubResponseDTO valueOf(ClubMember clubMember) {
        UrlResource representativePictureResource = null;
        String introduction = null;
        LocalDateTime regularMeetingTime = null;
        Club club = clubMember.getClub();

        if (club.getClubDetail() != null) {
            ClubDetail clubDetail = club.getClubDetail();
            if (clubDetail.getRepresentativePicture() != null) {
                try {
                    representativePictureResource = clubMember.getClub().getClubDetail().getRepresentativePicture().getResource();
                } catch (MalformedURLException e) {}
            }
            introduction = clubDetail.getIntroduction();
            regularMeetingTime = club.getClubDetail().getRegularMeetingTime();
        }

        return ClubResponseDTO.builder()
                .id(club.getId())
                .presidentName(club.getPresident().getName())
                .representativePicture(representativePictureResource)
                .introduction(introduction)
                .clubType(club.getClubType().toString())
                .name(club.getName())
                .supervisorName(club.getSupervisor().getName())
                .status(club.getStatus().toString())
                .rejectedReason(club.getRejectReason())
                .regularMeetingTime(regularMeetingTime)
                .members(ClubMemberResponseDTO.valueOf(club, club.getMembers()))
                .build();
    }
}
