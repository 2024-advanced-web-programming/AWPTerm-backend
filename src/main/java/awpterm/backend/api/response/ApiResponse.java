package awpterm.backend.api.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ApiResponse<T>{
    private T data;

    public static <T> ResponseEntity<ApiResponse<T>> response(HttpStatus code, T data) {
        return new ResponseEntity<>(
                ApiResponse.<T>builder()
                .data(data)
                .build(), code);
    }
}