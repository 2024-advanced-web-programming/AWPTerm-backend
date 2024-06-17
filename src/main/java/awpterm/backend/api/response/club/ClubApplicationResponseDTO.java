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
    private String applicantCode;
    private String applicantName;
    private Long applicationFormId;

    public static ClubApplicationResponseDTO valueOf(ClubApplicant clubApplicant) {
        return ClubApplicationResponseDTO.builder()
                .clubId(clubApplicant.getClub().getId())
                .clubName(clubApplicant.getClub().getName())
                .clubStatus(clubApplicant.getClub().getStatus().toString())
                .applicantCode(clubApplicant.getApplicant().getCode())
                .applicantName(clubApplicant.getApplicant().getName())
                .applicationFormId(clubApplicant.getApplicationForm().getId())
                .build();
    }
}
