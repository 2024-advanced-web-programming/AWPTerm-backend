package awpterm.backend.api.request.board;

import awpterm.backend.domain.Board;
import awpterm.backend.domain.Club;
import awpterm.backend.domain.Member;
import awpterm.backend.enums.BoardType;
import lombok.Data;

@Data
public class BoardAddNoticeRequestDTO {
    String title;
    Long clubId;
    String content;

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .content(content)
                .id(clubId)
                .boardType(BoardType.동아리_공지)
                .build();
    }
}
