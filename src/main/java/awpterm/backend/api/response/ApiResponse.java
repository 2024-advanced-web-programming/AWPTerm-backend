package awpterm.backend.api.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ApiResponse{
    public static <T> ResponseEntity<T> response(HttpStatus code, T data) {
        return new ResponseEntity<>(data, code);
    }
}