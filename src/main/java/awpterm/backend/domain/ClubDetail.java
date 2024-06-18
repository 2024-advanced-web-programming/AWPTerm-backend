package awpterm.backend.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
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
    @OneToOne
    private FileProperty registerFile; //가입 신청서
    @OneToOne
    private FileProperty representativePicture; // 대표 사진
    private String regularMeetingTime;
}
