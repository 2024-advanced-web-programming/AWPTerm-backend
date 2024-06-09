package awpterm.backend.docs;

import awpterm.backend.api.response.file.FilePropertyResponseDTO;
import awpterm.backend.domain.FileProperty;
import awpterm.backend.domain.Member;
import awpterm.backend.enums.FileType;
import awpterm.backend.service.FilePropertyService;
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
    private final FilePropertyService filePropertyService = mock(FilePropertyService.class);


    @Override
    protected Object initController() {
        return new FileController(filePropertyService);
    }

    @Test
    void 리스트_테스트() throws Exception {
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

        List<FilePropertyResponseDTO> result = new ArrayList<>();
        result.add(FilePropertyResponseDTO.of(fileProperty1));
        result.add(FilePropertyResponseDTO.of(fileProperty2));

        given(filePropertyService.getList())
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
        FileProperty fileProperty1 = FileProperty.builder()
                .type(FileType.PICTURE)
                .name("테크모")
                .content(FileProperty.readFile("src/test/resources/static/테크모.jpg"))
                .build();
        List<FilePropertyResponseDTO> result = new ArrayList<>();
        result.add(FilePropertyResponseDTO.of(fileProperty1));

        given(filePropertyService.getList(FileType.PICTURE.toString()))
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
                .content(FileProperty.readFile("src/test/resources/static/KIT.jpg"))
                .build();
        FilePropertyResponseDTO result = FilePropertyResponseDTO.of(FileProperty.builder()
                .type(FileType.valueOf(dto.getType()))
                .name(dto.getName())
                .content(dto.getContent())
                .uploader(Member.builder().build())
                .build());
        result.setId(3L);

        given(filePropertyService.upload(any(Member.class), dto))
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
        FilePropertyResponseDTO dto = FilePropertyResponseDTO.builder()
                .id(1L)
                .name("테크모")
                .content(FileProperty.readFile("src/test/resources/static/테크모.jpg"))
                .build();
        given(filePropertyService.download(1L))
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
