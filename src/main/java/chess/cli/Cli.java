package chess.cli;

import chess.model.CoreGame;
import chess.model.Move;
import chess.model.Position;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Starting point of the command line interface
 */
public class Cli {

    static Scanner scan;
    static PrintWriter printWriter;
    static CoreGame coreGame;

    /**
     * The entry point of the CLI application.
     *
     * @param args The command line arguments passed to the application
     */
    public static void main(String[] args) {
        scan = new Scanner(System.in);
        printWriter = new PrintWriter(System.out, true);
        init(args);
        enterGame();
        endGame();
    }

    /**
     * User interface to initial the Gamemode
     */
    public static void init(String[] args) {
        if (!Arrays.asList(args).contains("--simple")) {
            do {
                System.out.println(" ██████╗██╗  ██╗███████╗███████╗███████╗");
                System.out.println("██╔════╝██║  ██║██╔════╝██╔════╝██╔════╝");
                System.out.println("██║     ███████║█████╗  ███████╗███████╗");
                System.out.println("██║     ██╔══██║██╔══╝  ╚════██║╚════██║");
                System.out.println("╚██████╗██║  ██║███████╗███████║███████║");
                System.out.println(" ╚═════╝╚═╝  ╚═╝╚══════╝╚══════╝╚══════╝");
                System.out.println("");
                System.out.println("Please enter the game mode");
                System.out.println("1. Start a local game against a friend");
                System.out.println("2. Start a local game against the computer");
                System.out.println("3. Start a network game");
                System.out.print("Your entry: ");

                String input = scan.next();
                if (input.length() == 1 && input.charAt(0) >= 49 && input.charAt(0) <= 51) {
                    coreGame = new CoreGame(input.charAt(0) - 48);
                    break;
                }
            } while (true);
            //Enter simpleMode
        } else {
            coreGame = new CoreGame(1);
        }
    }

    /**
     * Gameloop
     * Clear Windows, drawBoard and wait for user input
     * After game the loop ends
     */
    public static void enterGame() {
        do {
            drawBoard();
            String input = scan.next();

            //check Commands
            if (input.equals("beaten")) {
                System.out.println("Beaten figures:");
                for (int i = 0; i < coreGame.getBeatenFigures().size(); i++) {
                    printWriter.println(coreGame.getBeatenFigures().get(i).getSymbol());
                }
                continue;
            }

            // Check syntax and make move
            if (validSyntax(input)) {
                coreGame.chessMove(parse(input));
            }
            // end game if game is over because one team is in checkmate
            if (coreGame.isGameOver()) {
                break;
            }

            /*//Check (in)valid syntax and make move, end game if syntax is not correct or chessMove
            if (!validSyntax(input)) {
                continue;
            } else if (coreGame.chessMove(parse((input)))) {
                break;
            }*/

        } while (true);
    }

    /**
     * Screen for credits
     */
    public static void endGame() {
        //TODO: endGame
        System.out.println("Game ends");
    }

    /**
     * Draw the board into the commandline
     */
    public static void drawBoard() {
        for (int y = 0; y < 8; y++) {
            printWriter.print(8 - y + " ");
            for (int x = 0; x < 8; x++) {
                printWriter.print(coreGame.getCurrentBoard().getFigure(x, 7 - y).getSymbol() + " ");
            }
            printWriter.println("");
        }
        printWriter.println("  a b c d e f g h");
        printWriter.println("");
    }

    /**
     * <--System------------------------------------------------------------------------------------------------------->
     */

    /**
     * Converts user input into coordinates. e.g. a3 == x: 0 y: 2
     *
     * @param input User input
     * @return Move coordinates
     */
    /*public static Map<String, Integer> parse(String input) {
        Map<String, Integer> pos = new HashMap<String, Integer>();
        //"a3-b4" or "a3-b4Q"
        if ((input.length() == 5 || input.length() == 6) && input.charAt(2) == 45) {
            //split "a3-b4Q" to "a3" and "b4Q"
            String[] result = input.split("-");
            if (result.length == 2) {
                String[] typ = {"pos", "new"};
                for (int i = 0; i < 2; i++) {
                    String[] xyPosition = result[i].split("");
                    //convert letters to numbers with ASCII code
                    if (xyPosition[0].charAt(0) >= 97 && xyPosition[0].charAt(0) <= 104) {
                        pos.put(typ[i] + "X", (int) xyPosition[0].charAt(0) - 97);
                    }
                    //convert numbers to numbers
                    if (xyPosition[1].charAt(0) >= 49 && xyPosition[1].charAt(0) <= 56) {
                        pos.put(typ[i] + "Y", Integer.parseInt(xyPosition[1]) - 1);
                    }
                }
            }
            //Add Queen as default
            pos.put("convertPawnTo", 5);

            //Check correct pawn conversion
            if(input.length() == 6){
                //split "a7-a8N" to "a7" and "a8" and "P" (Pawn)
                if (input.matches("^[a-h][27]-[a-h][18][P]$")) {
                    pos.put("convertPawnTo", 1);
                }else if (input.matches("^[a-h][27]-[a-h][18][R]$")) {
                    pos.put("convertPawnTo", 2);
                }else if (input.matches("^[a-h][27]-[a-h][18][N]$")) {
                    pos.put("convertPawnTo", 3);
                }else if (input.matches("^[a-h][27]-[a-h][18][B]$")) {
                    pos.put("convertPawnTo", 4);
                }else pos.clear();
            }
        } else {
            //if pos is less than 5 then invalid entry
            System.out.println("!Invalid move");

        }
        //Clear invalid entries
        if(pos.size() != 5) pos.clear();
        return pos;
    }*/
    public static boolean validSyntax(String input) {
        // e.g. "b2-e5Q"
        boolean correct = checkLength(input) && checkHyphen(input) && checkLetters(input) && checkNumbers(input);
        if (input.length() == 6) {
            correct = correct && checkConversionLetter(input);
        }
        if (correct) return true;
        System.out.println("!Invalid move");
        return false;
    }

    private static boolean checkLength(String input) {
        return input.length() == 5 || input.length() == 6;
    }

    private static boolean checkHyphen(String input) {
        return input.charAt(2) == 45;
    }

    private static boolean checkLetters(String input) {
        return input.charAt(0) >= 97 && input.charAt(0) <= 104 && input.charAt(3) >= 97 && input.charAt(3) <= 104;
    }

    private static boolean checkNumbers(String input) {
        return input.charAt(1) >= 49 && input.charAt(1) <= 56 && input.charAt(4) >= 49 && input.charAt(4) <= 56;
    }

    private static boolean checkConversionLetter(String input) {
        return input.charAt(5) == 66 || input.charAt(5) == 78 || input.charAt(5) == 81 || input.charAt(5) == 82;
    }

    /**
     * parse String input into Move objekt
     *
     * @param input String input
     * @return Move object
     */
    public static Move parse(String input) {
        // e.g. "b2-e5Q"
        int posX = (int) input.charAt(0) - 97;
        int posY = (int) input.charAt(1) - 49;
        int newX = (int) input.charAt(3) - 97;
        int newY = (int) input.charAt(4) - 49;
        if (input.length() == 6) {
            int pawnConversionTo;
            switch (input.charAt(5)) {
                // Rook
                case 82:
                    pawnConversionTo = 2;
                    break;
                // Knight
                case 78:
                    pawnConversionTo = 3;
                    break;
                // Bishop
                case 66:
                    pawnConversionTo = 4;
                    break;
                // Queen
                default:
                    pawnConversionTo = 5;
            }
            return new Move(new Position(posX, posY), new Position(newX, newY), pawnConversionTo);
        }
        return new Move(new Position(posX, posY), new Position(newX, newY));
    }
}
