package awpterm.backend.api.request.admin;
import awpterm.backend.api.request.club.ClubStatusRequestDTO;
import awpterm.backend.domain.Admin;
import awpterm.backend.domain.Club;
import awpterm.backend.enums.ClubType;
import awpterm.backend.enums.Status;
import awpterm.backend.util.SHA256;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminLoginRequestDTO {
    private String id;
    private String pw;

    public Admin toEntity() {
        return Admin.builder()
                .id(id)
                .password(SHA256.encrypt(pw))
                .build();
    }

    public static AdminLoginRequestDTO of(Admin admin) {
        return AdminLoginRequestDTO.builder()
                .id(admin.getId())
                .pw(admin.getPassword())
                .build();
    }
}
