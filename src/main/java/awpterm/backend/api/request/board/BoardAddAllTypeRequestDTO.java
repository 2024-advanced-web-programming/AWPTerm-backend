package awpterm.backend.api.request.board;

import awpterm.backend.domain.Board;
import awpterm.backend.domain.Club;
import awpterm.backend.enums.BoardType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardAddAllTypeRequestDTO {
    String title;
    Long clubId;
    String content;

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .id(clubId)
                .boardType(BoardType.전체_공지)
                .build();
    }
}
