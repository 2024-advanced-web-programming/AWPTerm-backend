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
    Club club;

    public static BoardResponseDTO valueOf(Board board) {
        return BoardResponseDTO.builder()
                .id(board.getId())
                .title(board.getTitle())
                .writer(board.getWriter())
                .club(board.getClub())
                .build();
    }
}
