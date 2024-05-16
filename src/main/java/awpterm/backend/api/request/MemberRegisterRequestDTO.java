package awpterm.backend.api.request;


import awpterm.backend.domain.Member;
import awpterm.backend.enums.Gender;
import awpterm.backend.enums.Major;
import awpterm.backend.enums.Position;
import awpterm.backend.util.SHA256;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberRegisterRequestDTO {
    private String id;
    private String password;
    private String name;
    private String birthDate;
    private String code;
    private String phoneNumber;
    private String email;
    private String gender;
    private String major;
    private String position;

    public Member toEntity() {
        return Member.builder()
                .id(id)
                .password(SHA256.encrypt(password))
                .name(name)
                .birthDate(birthDate)
                .code(code)
                .phoneNumber(phoneNumber)
                .email(email)
                .gender(Gender.valueOf(gender))
                .major(Major.valueOf(major))
                .position(Position.valueOf(position))
                .build();
    }

    public static MemberRegisterRequestDTO of(Member member) {
        return MemberRegisterRequestDTO.builder()
                .id(member.getId())
                .password(member.getPassword())
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
