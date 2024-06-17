package awpterm.backend.controller;

import awpterm.backend.api.response.ApiResponse;
import awpterm.backend.domain.FileProperty;
import awpterm.backend.service.FilePropertyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;

@Slf4j
@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final FilePropertyService filePropertyService;

    @GetMapping("/download/{filePropertyId}")
    public ResponseEntity<?> download(@PathVariable Long filePropertyId) {
        Pair<String, UrlResource> pair;
        FileProperty fileProperty = filePropertyService.findById(filePropertyId);

        try {
            pair = filePropertyService.download(fileProperty);
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
            return ApiResponse.response(HttpStatus.INTERNAL_SERVER_ERROR, null);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, pair.getFirst())
                .body(pair.getSecond());
    }
}
