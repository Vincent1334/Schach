package chess.model;

import org.junit.jupiter.api.Test;
import java.io.PrintWriter;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains tests to check methods in coreGame
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-07
 *
 */
public class CoreGameTest {

    CoreGame testGame = new CoreGame(1);

    /**
     * Tests whether the chess move method is correct
     */
    @Test
    public void testChessMove() {
        Move move = new Move(new Position(0,1),new Position(0,2));
        assertTrue(testGame.chessMove(move),"move is not accepted even if it is correct");

        PrintWriter printWriter = new PrintWriter(System.out, true);
        for (int y = 0; y < 8; y++) {
            printWriter.print(8 - y + " ");
            for (int x = 0; x < 8; x++) {
                printWriter.print(testGame.getBoard().getFigure(x, 7 - y).getSymbol() + " ");
            }
            printWriter.println("");
        }
        printWriter.println("  a b c d e f g h");
        printWriter.println("");

    }

}
