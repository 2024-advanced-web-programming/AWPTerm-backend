package awpterm.backend.controller;

import awpterm.backend.api.request.file.FileUploadRequestDTO;
import awpterm.backend.api.response.ApiResponse;
import awpterm.backend.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping("/list")
    public ResponseEntity<?> list() {
        return ApiResponse.response(HttpStatus.OK, fileService.getList());
    }

    @GetMapping("/list/{type}")
    public ResponseEntity<?> list(@PathVariable String type) {
        return ApiResponse.response(HttpStatus.OK, fileService.getList(type));
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestBody FileUploadRequestDTO fileUploadRequestDTO) {
        return ApiResponse.response(HttpStatus.OK, fileService.upload(fileUploadRequestDTO));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> download(@PathVariable Long id) {
        return ApiResponse.response(HttpStatus.OK, fileService.download(id));
    }
}
