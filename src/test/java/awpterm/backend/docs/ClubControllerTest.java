package awpterm.backend.docs;

import awpterm.backend.controller.ClubController;
import awpterm.backend.service.ClubServiceFacade;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

public class ClubControllerTest extends RestDocsTest {
    private final ClubServiceFacade clubServiceFacade = mock(ClubServiceFacade.class);


    @Override
    protected Object initController() {
        return new ClubController(clubServiceFacade);
    }

    @Test
    void 동아리_신청_테스트() {

    }
}
