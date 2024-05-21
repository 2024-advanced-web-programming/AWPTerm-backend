package awpterm.backend.api.response.file;

import awpterm.backend.domain.File;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileResponseDTO {
    private Long id;
    private String name;
    private Byte[] content;

    public static FileResponseDTO of(File file) {
        return FileResponseDTO.builder()
                .id(file.getId())
                .name(file.getName())
                .content(file.getContent())
                .build();
    }
}
