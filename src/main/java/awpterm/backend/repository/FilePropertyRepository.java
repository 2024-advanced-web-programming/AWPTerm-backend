package awpterm.backend.repository;

import awpterm.backend.domain.FileProperty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilePropertyRepository extends JpaRepository<FileProperty, Long> {

}
