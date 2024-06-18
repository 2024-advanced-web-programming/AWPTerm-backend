package awpterm.backend.docs;

import awpterm.backend.controller.FileController;
import awpterm.backend.service.FilePropertyService;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.UrlResource;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FileControllerDocsTest extends RestDocsTest {
    private final FilePropertyService filePropertyService = mock(FilePropertyService.class);


    @Override
    protected Object initController() {
        return new FileController(filePropertyService);
    }

    @Test
    void 다운로드() throws Exception {
        UrlResource resource = new UrlResource("https://google.com");
        String encodedUploadFileName = UriUtils.encode("testName", StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";
        Pair<String, UrlResource> pair = Pair.of(contentDisposition, resource);

        given(filePropertyService.download(any()))
                .willReturn(pair);

        mockMvc.perform(get("/static/download/1")
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
