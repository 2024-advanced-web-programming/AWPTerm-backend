package awpterm.backend.api.response.club;

import awpterm.backend.domain.FileProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClubApplicationResponseDTO {
    private String code;
    private String name;
    private FileProperty applicationForm;
}
