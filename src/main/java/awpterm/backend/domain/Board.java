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
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    String title;
    @ManyToOne
    private Member writer;
    private String content;
    @OneToMany
    private List<FileProperty> activityPhotos = new ArrayList<FileProperty>();
    private String videoURL;
    @ManyToOne
    private Club club;
    @Enumerated(EnumType.STRING)
    private BoardType boardType;
}
