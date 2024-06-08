package awpterm.backend.api.request.file;

import awpterm.backend.domain.File;
import awpterm.backend.enums.FileType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileUploadRequestDTO {
    private String type;
    private String name;
    private Byte[] content;
}