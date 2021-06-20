package chess.ai;

import chess.controller.CoreGame;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains information to test the AI
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-01
 */
public class ComputerTest {

    /**
     * Simulate a short KI game
     */
    @Test
    public void testKI(){
        try {
            CoreGame testGame = new CoreGame();
            Computer testPlayerWhite = new Computer(false);
            Computer testPlayerBlack = new Computer(true);

            testPlayerWhite.makeMove(testGame.getCurrentBoard());
            testPlayerWhite.getThread().join();

            assertTrue(testGame.chessMove(testPlayerWhite.getMove()));

            testPlayerBlack.makeMove(testGame.getCurrentBoard());
            testPlayerBlack.getThread().join();

            assertTrue(testGame.chessMove(testPlayerBlack.getMove()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}