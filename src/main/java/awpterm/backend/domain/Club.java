package awpterm.backend.domain;

import awpterm.backend.enums.ClubType;
import awpterm.backend.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@ToString
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Club extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private ClubType clubType;
    private String name;
    @ManyToOne
    private Member supervisor;
    @ManyToOne
    private Member president;
    @ManyToOne
    private Member vicePresident;
    @ManyToOne
    private Member secretary;
    @OneToMany(mappedBy = "club")
    private List<ClubMaster> masters;
    @OneToMany(mappedBy = "club")
    private List<ClubMember> members;
    private Status status;
    @Embedded
    private ClubDetail clubDetail;
}
