package awpterm.backend.domain;


import awpterm.backend.enums.FileType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.annotations.SecondaryRow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
@SecondaryRow
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class File extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private FileType type;
    @Column(columnDefinition = "MEDIUMBLOB")
    @Lob
    private Byte[] content;

    public static Byte[] readFile(String filePath) {
        Path path = Paths.get(filePath);
        byte[] fileBytes = null;

        try {
            fileBytes = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ArrayUtils.toObject(fileBytes);
    }
}
