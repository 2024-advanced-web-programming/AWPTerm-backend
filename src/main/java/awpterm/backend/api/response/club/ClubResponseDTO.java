package awpterm.backend.api.response.club;

import awpterm.backend.Config;
import awpterm.backend.domain.Club;
import awpterm.backend.domain.ClubDetail;
import awpterm.backend.domain.ClubMember;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClubResponseDTO {
    private Long id;
    private String presidentName;
    private String representativePicture;
    private String introduction;
    private String clubType;
    private String name;
    private String supervisorName;
    private String status;
    private String rejectedReason;
    private String regularMeetingTime;
    private List<ClubMemberResponseDTO> members;

    public static ClubResponseDTO valueOf(Club club) {
        String representativePictureUrl = null;
        String introduction = null;
        String regularMeetingTime = null;

        if (club.getClubDetail() != null) {
            if (club.getClubDetail().getRepresentativePicture() != null) {
                representativePictureUrl = Config.FILE_SERVER_URL + "/" + club.getClubDetail().getRepresentativePicture().getStoredFileName();
            }
            introduction = club.getClubDetail().getIntroduction();
            regularMeetingTime = club.getClubDetail().getRegularMeetingTime();
        }

        return ClubResponseDTO.builder()
                .id(club.getId())
                .presidentName(club.getPresident().getName())
                .representativePicture(representativePictureUrl)
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
        String representativePictureUrl = null;
        String introduction = null;
        String regularMeetingTime = null;
        Club club = clubMember.getClub();

        if (club.getClubDetail() != null) {
            ClubDetail clubDetail = club.getClubDetail();
            if (clubDetail.getRepresentativePicture() != null) {
                representativePictureUrl = Config.FILE_SERVER_URL + "/" + club.getClubDetail().getRepresentativePicture().getStoredFileName();
            }
            introduction = clubDetail.getIntroduction();
            regularMeetingTime = club.getClubDetail().getRegularMeetingTime();
        }

        return ClubResponseDTO.builder()
                .id(club.getId())
                .presidentName(club.getPresident().getName())
                .representativePicture(representativePictureUrl)
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
