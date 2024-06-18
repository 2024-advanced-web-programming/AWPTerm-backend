package awpterm.backend.domain;


import awpterm.backend.enums.BoardType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    String title;
    @ManyToOne
    private Member writer;
    //content 내에 이미지 정보가 담겨있기 때문에 굳이 이미지에 대해서 테이블 연관 관계 필요 x
    private String content;
    private String videoURL;
    @ManyToOne
    private Club club;
    @Enumerated(EnumType.STRING)
    private BoardType boardType;
}
