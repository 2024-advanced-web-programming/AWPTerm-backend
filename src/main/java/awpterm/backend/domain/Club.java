package awpterm.backend.domain;

import awpterm.backend.enums.ClubType;
import awpterm.backend.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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
    @Builder.Default
    //마스터 권한을 가진 인원
    private List<ClubMaster> masters = new ArrayList<>();
    @OneToMany(mappedBy = "club")
    @Builder.Default
    //동아리에 소속된 모든 인원(마스터 권한, 대표자 모두 포함)
    private List<ClubMember> members = new ArrayList<>();
    @OneToMany(mappedBy = "club")
    @Builder.Default
    private List<ClubApplicant> applicants = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private Status status;
    private String rejectReason = ""; //거절 사유
    @Embedded
    private ClubDetail clubDetail;
    @OneToOne
    private Member createdBy;
}
