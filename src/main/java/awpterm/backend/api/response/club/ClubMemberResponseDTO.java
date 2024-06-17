package awpterm.backend.api.response.club;

import awpterm.backend.domain.Club;
import awpterm.backend.domain.ClubMember;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ClubMemberResponseDTO {
    private String id;
    private String name;
    private String role;

    private static boolean memberCheck(Club club, ClubMember cm) {
        return !cm.getMember().equals(club.getPresident()) && !cm.getMember().equals(club.getVicePresident()) && !cm.getMember().equals(club.getSecretary());
    }

    public static List<ClubMemberResponseDTO> valueOf(Club club, List<ClubMember> clubMembers) {
        List<ClubMemberResponseDTO> result = new ArrayList<>();
        ClubMemberResponseDTO president;
        ClubMemberResponseDTO vicePresident;
        ClubMemberResponseDTO secretary;

        if (club.getPresident() != null) {
            president = ClubMemberResponseDTO.builder()
                    .id(club.getPresident().getId())
                    .name(club.getPresident().getName())
                    .role("회장")
                    .build();
            result.add(president);
        }

        if (club.getVicePresident() != null) {
            vicePresident = ClubMemberResponseDTO.builder()
                    .id(club.getVicePresident().getId())
                    .name(club.getVicePresident().getName()).role("부회장")
                    .build();
            result.add(vicePresident);
        }

        if (club.getSecretary() != null) {
            secretary = ClubMemberResponseDTO.builder()
                    .id(club.getSecretary().getId())
                    .name(club.getSecretary().getName())
                    .role("총무")
                    .build();
            result.add(secretary);
        }

        for (ClubMember cm : clubMembers) {
            // 역할이 회원이라면 (회장, 부회장, 총무 X)
            if (memberCheck(club, cm)) {
                result.add(ClubMemberResponseDTO.builder()
                        .id(cm.getMember().getId())
                        .name(cm.getMember().getName())
                        .role("회원")
                        .build());
            }
        }

        return result;
    }
}
