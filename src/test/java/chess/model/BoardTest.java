package chess.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BoardTest {

    @Test
    public void testFigureSetUp(){
        Board boardA = new Board();
        Figure[][] setup = new Figure[8][8];

        //Build default setup white
        for(int i = 0; i < 8; i++) setup[i][1] = new Pawn(0);
        setup[0][0] = new Rook(0);
        setup[7][0] = new Rook(0);
        setup[1][0] = new Knight(0);
        setup[6][0] = new Knight(0);
        setup[2][0] = new Bishop(0);
        setup[5][0] = new Bishop(0);
        setup[3][0] = new Queen(0);
        setup[4][0] = new King(0);

        //Build default setup black
        for(int i = 0; i < 8; i++) setup[i][6] = new Pawn(1);
        setup[0][7] = new Rook(1);
        setup[7][7] = new Rook(1);
        setup[1][7] = new Knight(1);
        setup[6][7] = new Knight(1);
        setup[2][7] = new Bishop(1);
        setup[5][7] = new Bishop(1);
        setup[3][7] = new Queen(1);
        setup[4][7] = new King(1);

        //Build default setup none
        for(int y = 2; y < 6; y++){
            for(int x = 0; x < 8; x++){
                setup[x][y] = new None();
            }
        }

        //check Board is full build
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                assertNotNull(boardA.getFigure(x, y));
            }
        }

        //check that all the figures are in the right place
        //assertEquals(setup, boardA.getBoard());
    }
}
