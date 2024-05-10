package awpterm.backend.api.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ApiResponse<T> {
    private HttpStatus code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> response(HttpStatus code, T data) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(code.toString())
                .data(data)
                .build();
    }
    public static <T> ApiResponse<T> response(HttpStatus code, String message, T data) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(message)
                .data(data)
                .build();
    }
}