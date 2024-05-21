package awpterm.backend.controller;

import awpterm.backend.api.request.file.FileUploadRequestDTO;
import awpterm.backend.api.response.file.FileResponseDTO;
import awpterm.backend.domain.File;
import awpterm.backend.enums.FileType;
import awpterm.backend.repository.FileRepository;
import awpterm.backend.service.FileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FileControllerTest {
    @Autowired
    private FileService fileService;
    @Autowired
    private FileRepository fileRepository;

    @BeforeEach
    void 테스트_데이터_삽입() {
        File file1 = File.builder()
                .type(FileType.사진)
                .name("테크모")
                .content(File.readFile("src/test/resources/static/테크모.jpg"))
                .build();

        File file2 = File.builder()
                .type(FileType.가입신청서)
                .name("가입신청서")
                .content(File.readFile("src/test/resources/static/가입신청서_테스트.docx"))
                .build();

        fileRepository.save(file1);
        fileRepository.save(file2);
    }

    @Test
    void 리스트_테스트() {
        List<FileResponseDTO> files = fileService.getList();
        assertThat(files.size()).isEqualTo(2);
    }

    @Test
    void 리스트_테스트_타입() {
        List<FileResponseDTO> files = fileService.getList("사진");
        assertThat(files.size()).isEqualTo(1);
    }

    @Test
    void 업로드() {
        FileUploadRequestDTO dto = FileUploadRequestDTO.builder()
                .type("사진")
                .name("KIT")
                .content(File.readFile("src/test/resources/static/KIT.jpg"))
                .build();

        FileResponseDTO result = fileService.upload(dto);
        File findFile = fileRepository.findByName("KIT");
        assertThat(result.getContent()).isEqualTo(findFile.getContent());
    }

    @Test
    void 다운로드() {
        List<FileResponseDTO> dtos = fileService.getList();
        FileResponseDTO target = null;

        for (FileResponseDTO f : dtos) {
            if (f.getName().equals("가입신청서")) {
                target = f;
            }
        }

        FileResponseDTO downloadFile = fileService.download(target.getId());
        assertThat(downloadFile).isEqualTo(target);
    }
}