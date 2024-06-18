package awpterm.backend.api.response.club;

import awpterm.backend.domain.ClubApplicant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClubApplicationResponseDTO {
    private Long clubId;
    private String clubName;
    private String clubStatus;
    private String applicantId;
    private String applicantCode;
    private String applicantName;
    private String applicantMajor;
    private Long applicationFormId;
    private String applicationFormName;

    public static ClubApplicationResponseDTO valueOf(ClubApplicant clubApplicant) {
        return ClubApplicationResponseDTO.builder()
                .clubId(clubApplicant.getClub().getId())
                .clubName(clubApplicant.getClub().getName())
                .clubStatus(clubApplicant.getClub().getStatus().toString())
                .applicantId(clubApplicant.getApplicant().getId())
                .applicantCode(clubApplicant.getApplicant().getCode())
                .applicantName(clubApplicant.getApplicant().getName())
                .applicantMajor(clubApplicant.getApplicant().getMajor().toString())
                .applicationFormId(clubApplicant.getApplicationForm().getId())
                .applicationFormName(clubApplicant.getApplicationForm().getUploadFileName())
                .build();
    }
}
