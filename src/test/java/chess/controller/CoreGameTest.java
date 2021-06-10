package chess.controller;

import chess.controller.CoreGame;
import chess.figures.King;
import chess.figures.None;
import chess.figures.Pawn;
import chess.figures.Rook;
import chess.model.Move;
import chess.model.Position;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains tests to check methods in coreGame
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-07
 *
 */
public class CoreGameTest {
    //CoreGame testGame = new CoreGame(1);
    CoreGame testGame = new CoreGame();
    /**
     * Tests whether the chess move method is correct
     */
    @Test
    public void testDefaultMove() {

        Move move = new Move(new Position(0, 1), new Position(0, 2));
        assertTrue(testGame.chessMove(move), "default move is not accepted even if it is correct");
    }

    /**
     * Tests whether the en passant move is executed correctly
     */
    @Test
    public void testEnPassantMove(){
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                testGame.getCurrentBoard().setFigure(x,y,new None());
            }
        }

        testGame.getCurrentBoard().setFigure(3,4,new Pawn(false));
        testGame.getCurrentBoard().setFigure(0,0,new King(true)); // otherwise CheckChessMate() would not find any possible move to avoid checkmate and would return "checkMate"

        Pawn blackEnPassantPawn = new Pawn(true);
        blackEnPassantPawn.setEnPassant(true);
        testGame.getCurrentBoard().setFigure(4,4,blackEnPassantPawn);

        Move move = new Move(new Position(3,4),new Position(4,5));
        assertTrue(testGame.chessMove(move), "enPassant move is not accepted even if it is correct");
    }
    /**
     * Tests whether the castling move on the right is executed correctly
     */
    @Test
    public void testCastlingMoveRight(){
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                testGame.getCurrentBoard().setFigure(x, y, new None());
            }
        }
        testGame.setActivePlayer(false);

        testGame.getCurrentBoard().setFigure(new Position(4,0),new King(false));
        testGame.getCurrentBoard().setFigure(new Position(7,0),new Rook(false));
        testGame.getCurrentBoard().setFigure(4,7,new King(true)); // otherwise CheckChessMate() would not find any possible move to avoid checkmate and would return "checkMate"

        Move move = new Move(new Position(4,0),new Position(6,0));
        assertTrue(testGame.chessMove(move), "CastlingRight move is not accepted even if it is correct");
    }

    /**
     * Tests whether the castling move on the left is executed correctly
     */
    @Test
    public void testCastlingMoveLeft(){
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                testGame.getCurrentBoard().setFigure(x, y, new None());
            }
        }
        testGame.setActivePlayer(false);
        testGame.getCurrentBoard().setFigure(new Position(4,0),new King(false));
        testGame.getCurrentBoard().setFigure(new Position(0,0),new Rook(false));
        testGame.getCurrentBoard().setFigure(4,7,new King(true)); // otherwise CheckChessMate() would not find any possible move to avoid checkmate and would return "checkMate"

        Move move = new Move(new Position(4,0),new Position(2,0));
        assertTrue(testGame.chessMove(move), "CastlingLeft move is not accepted even if it is correct");
    }

    /**
     * Tests whether the pawn conversion move is executed correctly
     */
    @Test
    public void testPawnConversionMove(){
        for (int x=0; x<8; x++) {
            for (int y=0; y<8; y++) {
                testGame.getCurrentBoard().setFigure(x, y, new None());
            }
        }
        testGame.getCurrentBoard().setFigure(new Position(3,6),new Pawn(false));
        testGame.getCurrentBoard().setFigure(0,0,new King(true)); // otherwise CheckChessMate() would not find any possible move to avoid checkmate and would return "checkMate"
        Move move1 = new Move(new Position(3,6),new Position(3,7));
        assertTrue(testGame.chessMove(move1), "Default PawnConversion move is not accepted even if it is correct");

        testGame.getCurrentBoard().setFigure(new Position(3,7),new None());
        testGame.getCurrentBoard().setFigure(new Position(3,6),new Pawn(false));
        testGame.setActivePlayer(false);
        Move move2 = new Move(new Position(3,6),new Position(3,7),3);
        assertTrue(testGame.chessMove(move2), "PawnConversion move to Knight is not accepted even if it is correct");

        testGame.getCurrentBoard().setFigure(new Position(3,7),new None());
        testGame.getCurrentBoard().setFigure(new Position(3,6),new Pawn(false));
        testGame.setActivePlayer(false);
        Move move3 = new Move(new Position(3,6),new Position(3,7),4);
        assertTrue(testGame.chessMove(move3), "PawnConversion move to Bishop is not accepted even if it is correct");

        testGame.getCurrentBoard().setFigure(new Position(3,7),new None());
        testGame.getCurrentBoard().setFigure(new Position(3,6),new Pawn(false));
        testGame.setActivePlayer(false);
        Move move4 = new Move(new Position(3,6),new Position(3,7),2);
        assertTrue(testGame.chessMove(move4), "PawnConversion move to Rook is not accepted even if it is correct");

        testGame.getCurrentBoard().setFigure(new Position(3,7),new None());
        testGame.getCurrentBoard().setFigure(new Position(3,6),new Pawn(false));
        testGame.setActivePlayer(false);
        Move move5 = new Move(new Position(3,6),new Position(3,7),5);
        assertTrue(testGame.chessMove(move5), "PawnConversion move to Queen is not accepted even if it is correct");
    }

}
