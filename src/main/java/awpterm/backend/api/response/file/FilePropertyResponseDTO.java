package awpterm.backend.api.response.file;

import awpterm.backend.domain.FileProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FilePropertyResponseDTO {
    private Long id;
    private String originalFileName;
    private String storedFileName;
    private String fileUrl;
    private Long fileSize;
    private String contentType;

    public static FilePropertyResponseDTO valueOf(FileProperty fileProperty){
        if(fileProperty == null)
            return null;

        return FilePropertyResponseDTO.builder()
                .id(fileProperty.getId())
                .originalFileName(fileProperty.getUploadFileName())
                .storedFileName(fileProperty.getStoredFileName())
                .fileUrl(fileProperty.getFileUrl())
                .fileSize(fileProperty.getFileSize())
                .contentType(fileProperty.getContentType())
                .build();
    }
}
