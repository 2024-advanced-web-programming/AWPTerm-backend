package awpterm.backend.api.request.board;

import awpterm.backend.domain.Board;
import awpterm.backend.domain.Club;
import awpterm.backend.domain.Member;
import awpterm.backend.enums.BoardType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardAddRecruitmentRequestDTO {
    String title;
    Long clubId;
    String content;

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .id(clubId)
                .boardType(BoardType.부원_모집)
                .build();
    }
}
