package chess.model;

import chess.cli.Cli;
import org.junit.jupiter.api.Test;

public class GameTest {

    private String[] testInput01 = {"b1-c3","g8-h6","g2-g4","e7-e6","f2-f4","d8-e7","b2-b3","a7-a6","c3-a4","e6-e5","g4-g5","e5-f4","a2-a3","b7-b6","g5-g6","a6-a5","e2-e4","f4-e3","f1-a6","h6-f5","c1-b2","c8-b7","a4-c3","a8-a7","a6-f1","f5-h4","d2-d3","e7-e4","h2-h3","h7-h5","d1-g4","b8-c6","a1-d1","h8-g8","d1-a1","b6-b5"};
    private String[] testInput02 = {"h2-h4","b8-a6","d2-d4","g7-g6","d4-d5","g8-f6","d1-d2","h7-h5","f2-f3","b7-b6","d2-e3","e7-e6","e3-a3","a6-b8","a3-b3","e6-d5","b3-a3","h8-g8","a3-e3","f6-e4","e3-c5","e4-d2","c2-c3","f7-f6","c5-d6","e8-f7","d6-d7","d8-e7","e2-e4","e7-d7","f3-f4","d7-c6","c1-d2","f7-e6","h1-h3","g6-g5","h3-h1","c6-a4","h1-h2","g8-h8","g2-g4","f6-f5","f1-c4","f8-h6","f4-g5","h6-g5","g1-h3","g5-e7","g4-h5","a4-a3","h3-g5","e6-d7","h2-h3","e7-f6","h3-f3","a3-a6","e4-d5","h8-h6","d2-e3","a6-a5","c4-e2","b8-c6","f3-h3","c6-e7","e1-d2","e7-c6","a2-a4","f6-g5","e3-g5","h6-h8","d2-d3","c6-b4","d3-d4","h8-e8","c3-b4","a5-a4","e2-g4","c8-a6","b1-a3","c7-c5","d5-c6","a4-c6","h3-e3","c6-e6","a3-b1","f5-f4","g4-h3","a6-c4","e3-e5","a8-d8"};

    @Test
    public void testGame01(){
        CoreGame game01 = new CoreGame(1);

        for(int i = 0; i < testInput01.length; i++){
            if(!(game01.chessMove(Cli.parse(testInput01[i])))){
                System.out.println("Fail on Move " + i + " " + testInput01[i]);
                break;
            }
        }

        //perform problem move from log.txt
        game01.chessMove(Cli.parse("e1-e2"));

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

    @Test
    public void testGame02(){
        CoreGame game02 = new CoreGame(1);

        for(int i = 0; i < testInput02.length; i++){
            if(!(game02.chessMove(Cli.parse(testInput02[i])))){
                System.out.println("Fail on Move " + i + " " + testInput02[i]);
                break;
            }
        }

        //perform problem move from log.txt
        game02.chessMove(Cli.parse("b1-d2"));

        for (int y = 0; y < 8; y++) {
            System.out.print(8 - y + " ");
            for (int x = 0; x < 8; x++) {
                System.out.print(game02.getCurrentBoard().getFigure(x, 7 - y).getSymbol() + " ");
            }
            System.out.println("");
        }
        System.out.println("  a b c d e f g h");
        System.out.println("");
    }
}



