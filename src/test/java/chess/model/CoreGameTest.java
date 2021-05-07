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
    public void testDefaultMove() {
        Move move = new Move(new Position(0,1),new Position(0,2));
        assertTrue(testGame.chessMove(move),"default move is not accepted even if it is correct");

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

    @Test
    public void testEnPassantMove(){
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                testGame.getBoard().setFigure(x,y,new None());
            }
        }
        testGame.getBoard().setFigure(3,4,new Pawn(0));
        testGame.getBoard().setFigure(0,0,new King(1)); // otherwise CheckChessMate() would not find any possible move to avoid checkmate and would return "checkMate"

        Pawn blackEnPassantPawn = new Pawn(1);
        blackEnPassantPawn.setEnPassant(true);
        testGame.getBoard().setFigure(4,4,blackEnPassantPawn);

        Move move = new Move(new Position(3,4),new Position(4,5));
        assertTrue(testGame.chessMove(move), "enPassant move is not accepted even if it is correct");
    }

}
