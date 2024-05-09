package awpterm.backend.api.request;


import lombok.Data;

@Data
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
}
