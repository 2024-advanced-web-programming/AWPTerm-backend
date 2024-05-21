package awpterm.backend.repository;

import awpterm.backend.domain.File;
import awpterm.backend.domain.User;
import awpterm.backend.enums.FileType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findByType(FileType type);
    File findByName(String name);
}
