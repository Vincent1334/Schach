package chess.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class contains tests to check the pawns movements
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-07
 *
 */
public class PawnTest {

    CoreGame game = new CoreGame(0);
    Board board = new Board();
    Figure pawnWhite = new Pawn(0);
    Figure pawnBlack = new Pawn(1);

    /**
     * Tests some random valid and invalid moves for a white pawn
     *
     */
    @Test
    public void testValidMoveWhite(){
        pawnWhite.setAlreadyMoved(true);
        pawnBlack.setAlreadyMoved(true);

        assertTrue(pawnWhite.validMove(new Position(0,1),new Position(0,2),board),"!white pawn can't move one step forward!");

        assertFalse(pawnWhite.validMove(new Position(0,3),new Position(0,2),board),"!white pawn can move one step backwards!");

        assertFalse(pawnWhite.validMove(new Position(0,1),new Position(0,1),board),"!white pawn can move to the actual place!");

        assertFalse(pawnWhite.validMove(new Position(0,1),new Position(0,3),board),"!white pawn can always move two steps forward!");

        assertFalse(pawnWhite.validMove(new Position(0,1),new Position(1,2),board),"!white pawn can move diagonal even if there isn't a opposing figure!");
    }

    /**
     * Tests some random valid and invalid moves for a black pawn
     *
     */
    @Test
    public void testValidMoveBlack(){
        pawnWhite.setAlreadyMoved(true);
        pawnBlack.setAlreadyMoved(true);

        assertTrue(pawnBlack.validMove(new Position(0,6),new Position(0,5),board),"!black pawn can't move one step forward!");

        assertFalse(pawnBlack.validMove(new Position(0,5),new Position(0,6),board),"!black pawn can move one step backwards!");

        assertFalse(pawnBlack.validMove(new Position(0,6),new Position(0,6),board),"!black pawn can move to the actual place!");

        assertFalse(pawnBlack.validMove(new Position(0,6),new Position(0,4),board),"!black pawn can always move two steps forward!");

        assertFalse(pawnBlack.validMove(new Position(0,6),new Position(1,5),board),"!black pawn can move diagonal even if there isn't a opposing figure!");

    }

    /**
     * Tests some random valid and invalid attacks for a pawn
     * Tests some random valid and invalid attacks for a pawn
     */
    @Test
    public void testAttack(){
        for(int i=0;i<8;i++){
            board.setFigure(i,i,new None());
        }
        board.setFigure(0,4,pawnWhite);
        board.setFigure(1,5,pawnBlack);
        assertTrue(pawnWhite.validMove(new Position(0,4),new Position(1,5),board),"!white pawn can't attack a figure!");
        assertTrue(pawnBlack.validMove(new Position(1,5),new Position(0,4),board),"!black pawn can't attack a figure!");


    }

    /**
     * Tests whether a pawn can move either one or two steps forward in his first move
     */
    @Test
    public void testFirstMove(){
        pawnWhite.setAlreadyMoved(false);
        pawnBlack.setAlreadyMoved(false);

        assertTrue(pawnWhite.validMove(new Position(0,1),new Position(0,2),board),"!white pawn can't move one step forward in his first move!");
        assertTrue(pawnBlack.validMove(new Position(0,6),new Position(0,5),board),"!black pawn can't move one step forward in his first move!");

        assertTrue(pawnWhite.validMove(new Position(0,1),new Position(0,3),board),"!white pawn can't move two steps forward in his first move!");
        assertTrue(pawnBlack.validMove(new Position(0,6),new Position(0,4),board),"!black pawn can't move two steps forward in his first move!");
    }

}
