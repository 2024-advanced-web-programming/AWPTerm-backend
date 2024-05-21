package awpterm.backend.api.request.club;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClubApplicationDecisionDTO {
    private Long clubId;
    private Long memberId;
    private Boolean isApproval;
}
