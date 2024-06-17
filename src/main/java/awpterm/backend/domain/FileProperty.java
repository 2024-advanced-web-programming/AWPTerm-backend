package awpterm.backend.domain;


import awpterm.backend.Config;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.UUID;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FileProperty extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String uploadFileName;
    private String storedFileName;
    private String filePath;
    private String fileUrl;
    private Long fileSize;
    private String contentType;

    public static FileProperty valueOf(MultipartFile multipartFile) {
        String uploadedFileName = multipartFile.getOriginalFilename();
        String storedFileName = createStoredFileName(uploadedFileName);

        FileProperty fileProperty = FileProperty.builder()
                .uploadFileName(uploadedFileName)
                .storedFileName(storedFileName)
                .filePath(createFilePath(storedFileName))
                .fileUrl(createFileUrl(storedFileName))
                .fileSize(multipartFile.getSize())
                .contentType(multipartFile.getContentType())
                .build();
        return fileProperty;
    }

    private static String createStoredFileName(String uploadedFileName) {
        String ext = uploadedFileName.substring(uploadedFileName.lastIndexOf('.'));
        return UUID.randomUUID().toString().replace("-","").concat(ext);
    }

    private static String createFilePath(String storedFileName) {
        return String.format("%s/%s", Config.FILE_ROOT_PATH, storedFileName);
    }

    private static String createFileUrl(String storedFileName) {
        return String.format("%s/%s", Config.FILE_ROOT_URL, storedFileName);
    }

    public UrlResource getResource() throws MalformedURLException {
        return new UrlResource("file:" + Config.FILE_ROOT_PATH + storedFileName);
    }

    public String getEncodedUploadFileName(Charset charset) {
        return UriUtils.encode(uploadFileName, charset);
    }
}
