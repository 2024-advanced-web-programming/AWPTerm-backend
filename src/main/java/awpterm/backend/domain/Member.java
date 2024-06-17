package awpterm.backend.domain;

import awpterm.backend.enums.Gender;
import awpterm.backend.enums.Major;
import awpterm.backend.enums.Position;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Member extends User {
    private String name;
    private String birthDate;
    @Column(unique = true)
    private String code;
    private String phoneNumber;
    private String email;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private Major major;
    @Enumerated(EnumType.STRING)
    private Position position;
    @OneToMany(mappedBy = "applicant")
    @Builder.Default
    private List<ClubApplicant> applicants = new ArrayList<>();

    public boolean isSame(Member member) {
        return getId().equals(member.getId()) && getPassword().equals(member.getPassword());
    }
}
