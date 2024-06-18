package awpterm.backend.api.request.board;

import awpterm.backend.domain.Board;
import awpterm.backend.domain.Club;
import awpterm.backend.domain.Member;
import awpterm.backend.enums.BoardType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardAddVideoRequestDTO {
    String title;
    Long clubId;
    String videoURL;

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .id(clubId)
                .videoURL(videoURL)
                .boardType(BoardType.활동_영상)
                .build();
    }
}
