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
    private String vicePresidentName;
    private String secretaryName;
    private String representativePicture;
    private String introduction;
    private String clubType;
    private String name;
    private String supervisorName;
    private String status;
    private String rejectedReason;
    private String regularMeetingTime;
    private List<ClubMemberResponseDTO> members;
    private Long fileId;
    private String fileName;

    public static ClubResponseDTO valueOf(Club club) {
        String representativePictureUrl = null;
        String introduction = null;
        String regularMeetingTime = null;
        Long fileId = null;
        String fileName = null;

        String vicePresidentName = null;
        String secretaryName = null;

        if(club.getVicePresident() != null) {
            vicePresidentName = club.getVicePresident().getName();
        }
        if(club.getSecretary() != null) {
            secretaryName = club.getSecretary().getName();
        }

        if (club.getClubDetail() != null) {
            ClubDetail clubDetail = club.getClubDetail();
            if (clubDetail.getRepresentativePicture() != null) {
                representativePictureUrl = Config.FILE_SERVER_URL + "/" + clubDetail.getRepresentativePicture().getStoredFileName();
            }

            if (clubDetail.getRegisterFile() != null) {
                fileId = clubDetail.getRegisterFile().getId();
                fileName = clubDetail.getRegisterFile().getUploadFileName();
            }

            introduction = club.getClubDetail().getIntroduction();
            regularMeetingTime = club.getClubDetail().getRegularMeetingTime();
        }

        return ClubResponseDTO.builder()
                .id(club.getId())
                .presidentName(club.getPresident().getName())// 대표자 이름
                .vicePresidentName(vicePresidentName)
                .secretaryName(secretaryName)
                .representativePicture(representativePictureUrl)
                .introduction(introduction)
                .clubType(club.getClubType().toString())
                .name(club.getName()) //동아리 이름
                .supervisorName(club.getSupervisor().getName()) //지도교수 이름
                .status(club.getStatus().toString())
                .rejectedReason(club.getRejectReason())
                .regularMeetingTime(regularMeetingTime)
                .members(ClubMemberResponseDTO.valueOf(club, club.getMembers()))
                .fileId(fileId)
                .fileName(fileName)
                .build();
    }

    public static ClubResponseDTO valueOf(ClubMember clubMember) {
        String representativePictureUrl = null;
        String introduction = null;
        String regularMeetingTime = null;
        Long fileId = null;
        String fileName = null;
        Club club = clubMember.getClub();

        String vicePresidentName = null;
        String secretaryName = null;

        if(club.getVicePresident() != null) {
            vicePresidentName = club.getVicePresident().getName();
        }
        if(club.getSecretary() != null) {
            secretaryName = club.getSecretary().getName();
        }

        if (club.getClubDetail() != null) {
            ClubDetail clubDetail = club.getClubDetail();
            if (clubDetail.getRepresentativePicture() != null) {
                representativePictureUrl = Config.FILE_SERVER_URL + "/" + clubDetail.getRepresentativePicture().getStoredFileName();
            }

            if (clubDetail.getRegisterFile() != null) {
                fileId = clubDetail.getRegisterFile().getId();
                fileName = clubDetail.getRegisterFile().getUploadFileName();
            }

            introduction = club.getClubDetail().getIntroduction();
            regularMeetingTime = club.getClubDetail().getRegularMeetingTime();
        }

        return ClubResponseDTO.builder()
                .id(club.getId())
                .presidentName(club.getPresident().getName())
                .vicePresidentName(vicePresidentName)
                .secretaryName(secretaryName)
                .representativePicture(representativePictureUrl)
                .introduction(introduction)
                .clubType(club.getClubType().toString())
                .name(club.getName())
                .supervisorName(club.getSupervisor().getName())
                .status(club.getStatus().toString())
                .rejectedReason(club.getRejectReason())
                .regularMeetingTime(regularMeetingTime)
                .members(ClubMemberResponseDTO.valueOf(club, club.getMembers()))
                .fileId(fileId)
                .fileName(fileName)
                .build();
    }
}
