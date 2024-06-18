package awpterm.backend.api.request.club;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClubUpdateStatusRequestDTO {
    private Long clubId;
    private String status;
    private String rejectedReason;
}
