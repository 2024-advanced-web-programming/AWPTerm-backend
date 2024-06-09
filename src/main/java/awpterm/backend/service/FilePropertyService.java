package awpterm.backend.service;

import awpterm.backend.api.response.file.FilePropertyResponseDTO;
import awpterm.backend.domain.FileProperty;
import awpterm.backend.repository.FilePropertyRepository;
import awpterm.backend.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FilePropertyService {
    private final FileRepository fileRepository;
    private final FilePropertyRepository filePropertyRepository;

    public FilePropertyResponseDTO storeFile(MultipartFile multipartFile) throws IOException {
        //파일 정보 추출(생성)
        FileProperty fileProperty = FileProperty.valueOf(multipartFile);
        //파일 시스템에 저장
        fileRepository.save(fileProperty, multipartFile);
        //DB에 저장
        filePropertyRepository.save(fileProperty);

        return FilePropertyResponseDTO.of(fileProperty);
    }
    public List<FilePropertyResponseDTO> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        //파일 정보 추출(생성)
        List<FilePropertyResponseDTO> storeFileResults = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if(!multipartFiles.isEmpty()){
                FileProperty fileProperty = FileProperty.valueOf(multipartFile);
                //파일 시스템에 저장
                fileRepository.save(fileProperty, multipartFile);
                //DB에 저장
                filePropertyRepository.save(fileProperty);
                storeFileResults.add(FilePropertyResponseDTO.of(fileProperty));
            }
        }
        return storeFileResults;
    }
    public FilePropertyResponseDTO delete(Long id) {
        FileProperty fileProperty = filePropertyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 파일을 찾을 수 없습니다."));

        fileRepository.delete(fileProperty);
        filePropertyRepository.delete(fileProperty);
        return FilePropertyResponseDTO.of(fileProperty);
    }
}
