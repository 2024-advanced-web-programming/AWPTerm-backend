package awpterm.backend.api.request.board;

import awpterm.backend.domain.Board;
import awpterm.backend.domain.Club;
import awpterm.backend.domain.FileProperty;
import awpterm.backend.domain.Member;
import awpterm.backend.enums.BoardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class BoardAddPhotoRequestDTO {
    String title;
    Club club;
    String content;

    public Board toEntity() {
        return Board.builder()
                .title(title)
                .club(club)
                .content(content)
                .boardType(BoardType.활동_사진)
                .build();
    }
}
