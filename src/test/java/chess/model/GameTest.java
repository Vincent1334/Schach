package chess.model;

import chess.cli.Cli;
import org.junit.jupiter.api.Test;

public class GameTest {

    private String[] testInput01 = {"b1-c3","g8-h6","g2-g4","e7-e6","f2-f4","d8-e7","b2-b3","a7-a6","c3-a4","e6-e5","g4-g5","e5-f4","a2-a3","b7-b6","g5-g6","a6-a5","e2-e4","f4-e3","f1-a6","h6-f5","c1-b2","c8-b7","a4-c3","a8-a7","a6-f1","f5-h4","d2-d3","e7-e4","h2-h3","h7-h5","d1-g4","b8-c6","a1-d1","h8-g8","d1-a1","b6-b5"};

    @Test
    public void testGame01(){
        CoreGame game01 = new CoreGame(1);

        for(int i = 0; i < testInput01.length; i++){
            if(!(game01.chessMove(Cli.parse(testInput01[i])))){
                System.out.println("Fail on Move " + i + " " + testInput01[i]);
                break;
            }
        }

        for (int y = 0; y < 8; y++) {
            System.out.print(8 - y + " ");
            for (int x = 0; x < 8; x++) {
                System.out.print(game01.getCurrentBoard().getFigure(x, 7 - y).getSymbol() + " ");
            }
            System.out.println("");
        }
        System.out.println("  a b c d e f g h");
        System.out.println("");

    }

}



