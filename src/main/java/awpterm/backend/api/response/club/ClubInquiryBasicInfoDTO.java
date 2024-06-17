package awpterm.backend.api.response.club;

import awpterm.backend.domain.Club;
import awpterm.backend.domain.ClubMember;
import awpterm.backend.domain.Member;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ClubInquiryBasicInfoDTO {
    private Long id;
    private String name;
    private String introduce;
    private LocalDateTime regularMeetingTime;
    @Builder.Default
    private String representativePictureURL = "";
    private Long registerFileId;
    private Member vicePresident; // 부대표
    private Member secretary; // 총무

    @Builder.Default
    private List<ClubMember> members = new ArrayList<>();

    public static ClubInquiryBasicInfoDTO of(Club club) {

        if (club.getClubDetail().getRepresentativePicture() == null) {
            return ClubInquiryBasicInfoDTO.builder()
                    .id(club.getId())
                    .name(club.getName())
                    .introduce(club.getClubDetail().getIntroduction())
                    .regularMeetingTime(club.getClubDetail().getRegularMeetingTime())
                    .registerFileId(club.getClubDetail().getRegisterFile().getId())
                    .vicePresident(club.getVicePresident())
                    .secretary(club.getSecretary())
                    .members(club.getMembers())
                    .build();
        }

        return ClubInquiryBasicInfoDTO.builder()
                .id(club.getId())
                .name(club.getName())
                .introduce(club.getClubDetail().getIntroduction())
                .regularMeetingTime(club.getClubDetail().getRegularMeetingTime())
                .representativePictureURL(club.getClubDetail().getRepresentativePicture().getFileUrl())
                .registerFileId(club.getClubDetail().getRegisterFile().getId())
                .vicePresident(club.getVicePresident())
                .secretary(club.getSecretary())
                .members(club.getMembers())
                .build();

    }
}
