package awpterm.backend.api.request.club;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class ClubApplicationRequestDTO {
    private Long clubId;
    private String applicantName;
    private String applicantCode;
    private String applicantMajor;
}
