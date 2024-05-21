package awpterm.backend.service;

import awpterm.backend.api.request.file.FileUploadRequestDTO;
import awpterm.backend.api.response.file.FileResponseDTO;
import awpterm.backend.domain.File;
import awpterm.backend.enums.FileType;
import awpterm.backend.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FileService {
    private final FileRepository fileRepository;

    public List<FileResponseDTO> getList() {
        return fileRepository.findAll().stream().map(FileResponseDTO::of).toList();
    }

    public List<FileResponseDTO> getList(String type) {
        return fileRepository.findByType(FileType.valueOf(type)).stream().map(FileResponseDTO::of).toList();
    }

    public FileResponseDTO upload(FileUploadRequestDTO fileUploadRequestDTO) {
        File file = fileUploadRequestDTO.toEntity();
        fileRepository.save(file);

        return FileResponseDTO.of(file);
    }

    public FileResponseDTO download(Long id) {
        return FileResponseDTO.of(fileRepository.findById(id).orElse(null));
    }
}
