package awpterm.backend.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ClubDetail {
    private String introduction;
    private String history;
    private Byte[] representativePicture;
    private LocalDateTime regularMeetingTime;
}
