package awpterm.backend.api.request.member;

import awpterm.backend.domain.Member;
import awpterm.backend.util.SHA256;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberLoginRequestDTO {
    private String id;
    private String password;

    public Member toEntity() {
        return Member.builder()
                .id(id)
                .password(SHA256.encrypt(password))
                .build();
    }

    public static MemberLoginRequestDTO of(Member member) {
        return MemberLoginRequestDTO.builder()
                .id(member.getId())
                .password(member.getPassword())
                .build();
    }
}
