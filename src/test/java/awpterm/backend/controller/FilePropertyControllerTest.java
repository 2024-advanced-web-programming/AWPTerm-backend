package awpterm.backend.controller;

import awpterm.backend.api.response.file.FilePropertyResponseDTO;
import awpterm.backend.domain.FileProperty;
import awpterm.backend.domain.Member;
import awpterm.backend.enums.FileType;
import awpterm.backend.repository.FileRepository;
import awpterm.backend.service.FilePropertyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class FilePropertyControllerTest {
    @Autowired
    private FilePropertyService filePropertyService;
    @Autowired
    private FileRepository fileRepository;

    @BeforeEach
    void 테스트_데이터_삽입() {
        FileProperty fileProperty1 = FileProperty.builder()
                .type(FileType.PICTURE)
                .name("테크모")
                .content(FileProperty.readFile("src/test/resources/static/테크모.jpg"))
                .build();

        FileProperty fileProperty2 = FileProperty.builder()
                .type(FileType.APPLICATION_FORM)
                .name("가입신청서")
                .content(FileProperty.readFile("src/test/resources/static/가입신청서_테스트.docx"))
                .build();

        fileRepository.save(fileProperty1);
        fileRepository.save(fileProperty2);
    }

    @Test
    void 리스트_테스트() {
        List<FilePropertyResponseDTO> files = filePropertyService.getList();
        assertThat(files.size()).isEqualTo(2);
    }

    @Test
    void 리스트_테스트_타입() {
        List<FilePropertyResponseDTO> files = filePropertyService.getList("사진");
        assertThat(files.size()).isEqualTo(1);
    }

    @Test
    void 업로드() {
        FileUploadRequestDTO dto = FileUploadRequestDTO.builder()
                .type(FileType.PICTURE.toString())
                .name("KIT")
                .content(FileProperty.readFile("src/test/resources/static/KIT.jpg"))
                .build();

        FilePropertyResponseDTO result = filePropertyService.upload(Member.builder().build(), dto);
        FileProperty findFileProperty = fileRepository.findByName("KIT");
        assertThat(result.getContent()).isEqualTo(findFileProperty.getContent());
    }

    @Test
    void 다운로드() {
        List<FilePropertyResponseDTO> dtos = filePropertyService.getList();
        FilePropertyResponseDTO target = null;

        for (FilePropertyResponseDTO f : dtos) {
            if (f.getName().equals("가입신청서")) {
                target = f;
            }
        }

        FilePropertyResponseDTO downloadFile = filePropertyService.download(target.getId());
        assertThat(downloadFile).isEqualTo(target);
    }
}