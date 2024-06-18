package awpterm.backend.api.response.board;

import awpterm.backend.domain.Board;
import awpterm.backend.domain.Club;
import awpterm.backend.domain.Member;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BoardResponseDTO {
    Long id;
    String title;
    Member writer;
    Long clubId;
    String clubName;
    String content;
    String videoURL;
    String timestamp;


    public static BoardResponseDTO valueOf(Board board) {
        return BoardResponseDTO.builder()
                .id(board.getId())
                .title(board.getTitle())
                .writer(board.getWriter())
                .clubId(board.getClub().getId())
                .clubName(board.getClub().getName())
                .content(board.getContent())
                .videoURL(board.getVideoURL())
                .timestamp(board.getTimestamp())
                .build();
    }
}
