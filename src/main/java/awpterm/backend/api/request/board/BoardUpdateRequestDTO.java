package awpterm.backend.api.request.board;

import awpterm.backend.domain.Board;
import awpterm.backend.domain.Club;
import awpterm.backend.enums.BoardType;
import awpterm.backend.enums.ClubType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardUpdateRequestDTO implements BoardRequestDTO{
    Long boardId;
    String title;
    String content;
    Long clubId;
    BoardType boardType;
    String videoURL;

    public Board toEntity() {
        return Board.builder()
                .id(boardId)
                .title(title)
                .content(content)
                .id(clubId)
                .boardType(boardType)
                .build();
    }
}
