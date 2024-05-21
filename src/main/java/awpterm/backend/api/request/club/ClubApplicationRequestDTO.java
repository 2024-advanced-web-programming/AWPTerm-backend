package awpterm.backend.api.request.club;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClubApplicationRequestDTO {
    private Long clubId;
    private String fileName;
    private Byte[] fileContent;
}
