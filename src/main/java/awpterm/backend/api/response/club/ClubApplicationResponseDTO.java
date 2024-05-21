package awpterm.backend.api.response.club;

import awpterm.backend.domain.ClubApplicant;
import awpterm.backend.domain.File;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClubApplicationResponseDTO {
    private String code;
    private String name;
    private File applicationForm;
}
