package awpterm.backend.api.request;

import lombok.Data;

@Data
public class MemberLoginRequestDTO {
    private String id;
    private String password;
}
