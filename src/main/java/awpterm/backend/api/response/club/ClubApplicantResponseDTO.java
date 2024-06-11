package awpterm.backend.api.response.club;

import awpterm.backend.domain.ClubApplicant;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClubApplicantResponseDTO {
    private Long id;
    private Long clubId;
    private String applicantId;
    private Long applicationFormId;

    public static ClubApplicantResponseDTO valueOf(ClubApplicant clubApplicant) {
        return ClubApplicantResponseDTO.builder()
                .id(clubApplicant.getId())
                .clubId(clubApplicant.getClub().getId())
                .applicantId(clubApplicant.getApplicant().getId())
                .applicationFormId(clubApplicant.getApplicationForm().getId())
                .build();
    }
}
