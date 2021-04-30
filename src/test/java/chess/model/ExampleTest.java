package chess.model;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExampleTest {

    @Test
    @DisplayName("Should demonstrate a simple assertion")
    @Disabled("Just a test test")
    public void foo() {
        Rook example = new Rook(23);
        assertEquals(23, example.getTeam(), "bla");         // run with ctrl+shift+f10
    }
}
