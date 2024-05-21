package awpterm.backend.api.response.file;

import awpterm.backend.domain.File;
import awpterm.backend.domain.Member;
import awpterm.backend.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileResponseDTO {
    private Long id;
    private String name;
    private Member uploader;
    private Byte[] content;

    public static FileResponseDTO of(File file) {
        return FileResponseDTO.builder()
                .id(file.getId())
                .name(file.getName())
                .uploader(file.getUploader())
                .content(file.getContent())
                .build();
    }
}
