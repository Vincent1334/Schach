package chess.model;

import chess.cli.Cli;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class GameTest {

    private String[] testInput01 = {"b1-c3","g8-h6","g2-g4","e7-e6","f2-f4","d8-e7","b2-b3","a7-a6","c3-a4","e6-e5","g4-g5","e5-f4","a2-a3","b7-b6","g5-g6","a6-a5","e2-e4","f4-e3","f1-a6","h6-f5","c1-b2","c8-b7","a4-c3","a8-a7","a6-f1","f5-h4","d2-d3","e7-e4","h2-h3","h7-h5","d1-g4","b8-c6","a1-d1","h8-g8","d1-a1","b6-b5"};
    private String[] testInput02 = {"h2-h4","b8-a6","d2-d4","g7-g6","d4-d5","g8-f6","d1-d2","h7-h5","f2-f3","b7-b6","d2-e3","e7-e6","e3-a3","a6-b8","a3-b3","e6-d5","b3-a3","h8-g8","a3-e3","f6-e4","e3-c5","e4-d2","c2-c3","f7-f6","c5-d6","e8-f7","d6-d7","d8-e7","e2-e4","e7-d7","f3-f4","d7-c6","c1-d2","f7-e6","h1-h3","g6-g5","h3-h1","c6-a4","h1-h2","g8-h8","g2-g4","f6-f5","f1-c4","f8-h6","f4-g5","h6-g5","g1-h3","g5-e7","g4-h5","a4-a3","h3-g5","e6-d7","h2-h3","e7-f6","h3-f3","a3-a6","e4-d5","h8-h6","d2-e3","a6-a5","c4-e2","b8-c6","f3-h3","c6-e7","e1-d2","e7-c6","a2-a4","f6-g5","e3-g5","h6-h8","d2-d3","c6-b4","d3-d4","h8-e8","c3-b4","a5-a4","e2-g4","c8-a6","b1-a3","c7-c5","d5-c6","a4-c6","h3-e3","c6-e6","a3-b1","f5-f4","g4-h3","a6-c4","e3-e5","a8-d8"};
    private String[] testInput03 = {"f2-f4","h7-h5","h2-h4","b8-c6","b2-b3","a7-a6","c1-b2","g8-h6","c2-c3","h6-f5","b1-a3","c6-e5","g2-g3","e5-d3","e2-d3","c7-c5","d1-h5","h8-h5"};
    private String[] testInput04 = {"e2-e4","d7-d6","f1-c4","c8-e6","b1-a3","e6-f5","f2-f4","f5-d7","e1-f2","c7-c6","c4-a6","g8-f6","c2-c4","c6-c5","d1-f1","f6-d5","a3-b1","d8-c8","e4-d5","h8-g8","g1-f3","d7-b5","b2-b4","c5-b4","h2-h4","f7-f6","h1-h3","b7-b6","f4-f5","h7-h5","f2-e1","c8-f5","e1-f2","g7-g6","d2-d4","b5-a4","f2-e1"};
    private String[] testInput05 = {"f2-f3","b8-c6","e2-e4","g7-g5","h2-h4","a7-a5","f1-d3","c6-e5","b2-b3","f8-g7","c2-c4","g8-h6","h1-h3","e8-g8","f3-f4","d7-d5","h4-h5","d8-d6","h3-h2","g7-h8","d1-c2","c8-g4","e4-d5","d6-c5","c2-d1","h8-g7","g1-h3","c5-d4"};
    private String[] testInput06 = {"f2-f3","b7-b6","h2-h3","g8-h6","h3-h4","h6-f5","e1-f2","c7-c6","h1-h2","c8-a6","d1-e1","a6-d3"};
    private String[] testInput07 = {"a2-a3","e7-e5","d2-d3","d8-f6","g2-g3","f8-a3","c1-e3","f6-g6","e3-g5","g6-e4","g5-c1","g8-h6","f2-f3","b7-b5","d3-d4","h8-g8","b2-a3","e4-d4","c1-b2","e8-f8","f1-h3","d4-c5","a1-a2","f7-f5"};
    private String[] testInput08 = {"d2-d4","f7-f5","g1-f3","a7-a6","c1-g5","g7-g6","d1-d3","g8-f6","h2-h4","f6-g4","d3-e3","a8-a7","b1-d2","c7-c5","d4-c5","e7-e5"};

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

    @Test
    public void testGame03(){
        CoreGame game03 = new CoreGame(1);

        for(int i = 0; i < testInput03.length; i++){
            if(!(game03.chessMove(Cli.parse(testInput03[i])))){
                System.out.println("Fail on Move " + i + " " + testInput03[i]);
                break;
            }
        }

        //perform problem move from log.txt
        game03.chessMove(Cli.parse("e1-c1"));

        for (int y = 0; y < 8; y++) {
            System.out.print(8 - y + " ");
            for (int x = 0; x < 8; x++) {
                System.out.print(game03.getCurrentBoard().getFigure(x, 7 - y).getSymbol() + " ");
            }
            System.out.println("");
        }
        System.out.println("  a b c d e f g h");
        System.out.println("");
    }

    @Test
    public void testGame04(){
        CoreGame game04 = new CoreGame(1);

        for(int i = 0; i < testInput04.length; i++){
            if(!(game04.chessMove(Cli.parse(testInput04[i])))){
                System.out.println("Fail on Move " + i + " " + testInput04[i]);
                break;
            }
        }

        //perform problem move from log.txt
        game04.chessMove(Cli.parse("a7-a6"));

        for (int y = 0; y < 8; y++) {
            System.out.print(8 - y + " ");
            for (int x = 0; x < 8; x++) {
                System.out.print(game04.getCurrentBoard().getFigure(x, 7 - y).getSymbol() + " ");
            }
            System.out.println("");
        }
        System.out.println("  a b c d e f g h");
        System.out.println("");
    }

    @Test
    public void testGame05(){
        CoreGame game05 = new CoreGame(1);

        for(int i = 0; i < testInput05.length; i++){
            if(!(game05.chessMove(Cli.parse(testInput05[i])))){
                System.out.println("Fail on Move " + i + " " + testInput05[i]);
                break;
            }
        }

        //perform problem move from log.txt
        game05.chessMove(Cli.parse("g2-g4"));

        for (int y = 0; y < 8; y++) {
            System.out.print(8 - y + " ");
            for (int x = 0; x < 8; x++) {
                System.out.print(game05.getCurrentBoard().getFigure(x, 7 - y).getSymbol() + " ");
            }
            System.out.println("");
        }
        System.out.println("  a b c d e f g h");
        System.out.println("");
    }

    @Test
    public void testGame06(){
        CoreGame game06 = new CoreGame(1);

        for(int i = 0; i < testInput06.length; i++){
            if(!(game06.chessMove(Cli.parse(testInput06[i])))){
                System.out.println("Fail on Move " + i + " " + testInput06[i]);
                break;
            }
        }

        //perform problem move from log.txt
        game06.chessMove(Cli.parse("d2-d4"));

        for (int y = 0; y < 8; y++) {
            System.out.print(8 - y + " ");
            for (int x = 0; x < 8; x++) {
                System.out.print(game06.getCurrentBoard().getFigure(x, 7 - y).getSymbol() + " ");
            }
            System.out.println("");
        }
        System.out.println("  a b c d e f g h");
        System.out.println("");
    }

    @Test
    public void testGame07(){
        CoreGame game07 = new CoreGame(1);

        for(int i = 0; i < testInput07.length; i++){
            if(!(game07.chessMove(Cli.parse(testInput07[i])))){
                System.out.println("Fail on Move " + i + " " + testInput07[i]);
                break;
            }
        }

        //perform problem move from log.txt
        assertFalse(Cli.validSyntax(""));
        //game07.chessMove(Cli.parse(""));

        for (int y = 0; y < 8; y++) {
            System.out.print(8 - y + " ");
            for (int x = 0; x < 8; x++) {
                System.out.print(game07.getCurrentBoard().getFigure(x, 7 - y).getSymbol() + " ");
            }
            System.out.println("");
        }
        System.out.println("  a b c d e f g h");
        System.out.println("");
    }

    @Test
    public void testGame08(){
        CoreGame game08 = new CoreGame(1);

        for(int i = 0; i < testInput08.length; i++){
            if(!(game08.chessMove(Cli.parse(testInput08[i])))){
                System.out.println("Fail on Move " + i + " " + testInput08[i]);
                break;
            }
        }

        //perform problem move from log.txt
       // assertFalse(Cli.validSyntax(""));
        game08.chessMove(Cli.parse("e1-c3"));

        for (int y = 0; y < 8; y++) {
            System.out.print(8 - y + " ");
            for (int x = 0; x < 8; x++) {
                System.out.print(game08.getCurrentBoard().getFigure(x, 7 - y).getSymbol() + " ");
            }
            System.out.println("");
        }
        System.out.println("  a b c d e f g h");
        System.out.println("");
    }
}



