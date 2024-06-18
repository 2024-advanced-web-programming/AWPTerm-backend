package awpterm.backend.etc;

import awpterm.backend.domain.Member;
import awpterm.backend.enums.Gender;
import awpterm.backend.enums.Major;
import awpterm.backend.enums.Position;

public class MakeMockMember {

    public static Member makeMockStudent(String name) {
        return Member.builder()
                .name(name)
                .birthDate("20000000")
                .code("20190000")
                .phoneNumber("010-1111-2222")
                .email("test@kumoh.ac.kr")
                .gender(Gender.남자)
                .major(Major.컴퓨터소프트웨어공학과)
                .position(Position.학생)
                .build();
    }
    public static Member makeMockProfessor(String name) {
        return Member.builder()
                .name(name)
                .birthDate("19800101")
                .code("CS00000")
                .phoneNumber("010-1234-5678")
                .email("professor@kumoh.ac.kr")
                .gender(Gender.여자)
                .major(Major.컴퓨터소프트웨어공학과)
                .position(Position.교수)
                .build();
    }
}
