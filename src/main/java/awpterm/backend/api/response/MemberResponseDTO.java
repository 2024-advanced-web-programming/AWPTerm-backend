package awpterm.backend.api.response;

import awpterm.backend.domain.Member;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberResponseDTO {
    private String name;
    private String birthDate;
    private String code;
    private String phoneNumber;
    private String email;
    private String gender;
    private String major;
    private String position;

    public static MemberResponseDTO of(Member member) {
        return MemberResponseDTO.builder()
                .name(member.getName())
                .birthDate(member.getBirthDate())
                .code(member.getCode())
                .phoneNumber(member.getPhoneNumber())
                .email(member.getEmail())
                .gender(member.getGender().toString())
                .major(member.getMajor().toString())
                .position(member.getPosition().toString())
                .build();
    }
}
