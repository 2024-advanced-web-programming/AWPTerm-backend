package awpterm.backend.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToOne;
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
    @OneToOne
    private FileProperty representativePicture;
    private LocalDateTime regularMeetingTime;
}
