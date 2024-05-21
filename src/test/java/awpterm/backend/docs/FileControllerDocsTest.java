package awpterm.backend.docs;

import awpterm.backend.api.request.file.FileUploadRequestDTO;
import awpterm.backend.api.response.file.FileResponseDTO;
import awpterm.backend.controller.FileController;
import awpterm.backend.domain.File;
import awpterm.backend.domain.Member;
import awpterm.backend.enums.FileType;
import awpterm.backend.service.FileService;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FileControllerDocsTest extends RestDocsTest {
    private final FileService fileService = mock(FileService.class);


    @Override
    protected Object initController() {
        return new FileController(fileService);
    }

    @Test
    void 리스트_테스트() throws Exception {
        File file1 = File.builder()
                .type(FileType.PICTURE)
                .name("테크모")
                .content(File.readFile("src/test/resources/static/테크모.jpg"))
                .build();

        File file2 = File.builder()
                .type(FileType.APPLICATION_FORM)
                .name("가입신청서")
                .content(File.readFile("src/test/resources/static/가입신청서_테스트.docx"))
                .build();

        List<FileResponseDTO> result = new ArrayList<>();
        result.add(FileResponseDTO.of(file1));
        result.add(FileResponseDTO.of(file2));

        given(fileService.getList())
                .willReturn(result);

        mockMvc.perform(get("/file/list")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("file-list",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestBody(),
                        responseBody()));
    }

    @Test
    void 리스트_테스트_타입() throws Exception {
        File file1 = File.builder()
                .type(FileType.PICTURE)
                .name("테크모")
                .content(File.readFile("src/test/resources/static/테크모.jpg"))
                .build();
        List<FileResponseDTO> result = new ArrayList<>();
        result.add(FileResponseDTO.of(file1));

        given(fileService.getList(FileType.PICTURE.toString()))
                .willReturn(result);

        mockMvc.perform(get("/file/list/PICTURE")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("file-list-type",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestBody(),
                        responseBody()));
    }

    @Test
    void 업로드() throws Exception {
        FileUploadRequestDTO dto = FileUploadRequestDTO.builder()
                .type(FileType.PICTURE.toString())
                .name("KIT")
                .content(File.readFile("src/test/resources/static/KIT.jpg"))
                .build();
        FileResponseDTO result = FileResponseDTO.of(File.builder()
                .type(FileType.valueOf(dto.getType()))
                .name(dto.getName())
                .content(dto.getContent())
                .uploader(Member.builder().build())
                .build());
        result.setId(3L);

        given(fileService.upload(any(Member.class), dto))
                .willReturn(result);

        mockMvc.perform(post("/file/upload")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("file-upload",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestBody(),
                        responseBody()));
    }

    @Test
    void 다운로드() throws Exception {
        FileResponseDTO dto = FileResponseDTO.builder()
                .id(1L)
                .name("테크모")
                .content(File.readFile("src/test/resources/static/테크모.jpg"))
                .build();
        given(fileService.download(1L))
                .willReturn(dto);

        mockMvc.perform(get("/file/download/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("file-download",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestBody(),
                        responseBody()));
    }
}
