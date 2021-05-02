package chess.model;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PawnTest {

    public PawnTest(){
    }

    CoreGame game = new CoreGame(0);
    Board board = new Board();
    Figure pawnWhite = new Pawn(0);
    Figure pawnBlack = new Pawn(1);

    @Test
    public void testNormalMove(){
        pawnWhite.setAlreadyMoved(true);
        pawnBlack.setAlreadyMoved(true);

        assertTrue(pawnWhite.validMove(0,1,0,2,board),"!white pawn can't move one step forward!");
        assertTrue(pawnBlack.validMove(0,6,0,5,board),"!black pawn can't move one step forward!");

        assertFalse(pawnWhite.validMove(0,3,0,2,board),"!white pawn can move one step backwards!");
        assertFalse(pawnBlack.validMove(0,5,0,6,board),"!black pawn can move one step backwards!");

        assertFalse(pawnWhite.validMove(0,1,0,1,board),"!white pawn can move to the actual place!");
        assertFalse(pawnBlack.validMove(0,6,0,6,board),"!black pawn can move to the actual place!");

        assertFalse(pawnWhite.validMove(0,1,0,3,board),"!white pawn can always move two steps forward!");
        assertFalse(pawnBlack.validMove(0,6,0,4,board),"!black pawn can always move two steps forward!");

        assertFalse(pawnWhite.validMove(0,1,1,2,board),"!white pawn can move diagonal even if there isn't a opposing figure!");
        assertFalse(pawnBlack.validMove(0,6,1,5,board),"!black pawn can move diagonal even if there isn't a opposing figure!");

    }

    @Test
    public void testAttack(){
        for(int i=0;i<8;i++){
            board.setFigure(i,i,new None());
        }
        board.setFigure(0,4,pawnWhite);
        board.setFigure(1,5,pawnBlack);
        assertTrue(pawnWhite.validMove(0,4,1,5,board),"!white pawn can't attack an opposing figure!");
        assertTrue(pawnBlack.validMove(1,5,0,4,board),"!black pawn can't attack an opposing figure!");

        board.setFigure(0,4,pawnWhite);
        board.setFigure(1,5,pawnWhite);
        assertFalse(pawnWhite.validMove(0,4,1,5,board),"!white pawn can attack a white figure!");

        board.setFigure(0,4,pawnBlack);
        board.setFigure(1,5,pawnBlack);
        assertFalse(pawnBlack.validMove(1,5,0,4,board),"!black pawn can attack a black figure!");


    }

    @Test
    public void testFirstMove(){
        pawnWhite.setAlreadyMoved(false);
        pawnBlack.setAlreadyMoved(false);

        assertTrue(pawnWhite.validMove(0,1,0,2,board),"!white pawn can't move one step forward in his first move!");
        assertTrue(pawnBlack.validMove(0,6,0,5,board),"!black pawn can't move one step forward in his first move!");

        assertTrue(pawnWhite.validMove(0,1,0,3,board),"!white pawn can't move two steps forward in his first move!");
        assertTrue(pawnBlack.validMove(0,6,0,4,board),"!black pawn can't move two steps forward in his first move!");
    }
    @Test
    @Disabled
    public void testEnPassant(){
        for(int i=0;i<8;i++){
            board.setFigure(i,i,new None());
        }
        board.setFigure(0,3,pawnWhite);
        board.setFigure(1,3,pawnBlack);
        if(board.getFigure(0,3) instanceof Pawn){
            ((Pawn)board.getFigure(0,3)).setEnPassant(true);
        }

        //assertTrue(game.checkEnPassant(1,3,0,2,board),"!black pawn can't make an en passant move!");



    }
    @Test
    public void testConversion(){

    }
}
