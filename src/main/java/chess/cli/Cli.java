package chess.cli;

import chess.controller.CoreGame;
import chess.model.Move;
import chess.model.Position;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Starting point of the command line interface
 */
public class Cli {

    private static Scanner scan;
    private static CoreGame coreGame;

    /**
     * The entry point of the CLI application.
     *
     * @param args The command line arguments passed to the application
     */
    public static void main(String[] args) {
        scan = new Scanner(System.in);
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

                String input = scan.nextLine();
                if (input.length() == 1 && input.charAt(0) >= 49 && input.charAt(0) <= 51) {
                    //coreGame = new CoreGame(input.charAt(0) - 48);
                    coreGame = new CoreGame();
                    break;
                }
            } while (true);
            //Enter simpleMode
        } else {
            //coreGame = new CoreGame(1);
            coreGame = new CoreGame();
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
            String input = scan.nextLine();

            //check Commands
            if (input.equals("beaten")) {
                System.out.println("Beaten figures:");
                for (int i = 0; i < coreGame.getCurrentBoard().getBeatenFigures().size(); i++) {
                    System.out.println(coreGame.getCurrentBoard().getBeatenFigures().get(i).getSymbol());
                }
                continue;
            }

            // Check syntax and make move
            if (validSyntax(input)) {
                coreGame.chessMove(parse(input));
            }
        } while (!coreGame.isGameOver());
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
            System.out.print(8 - y + " ");
            for (int x = 0; x < 8; x++) {
                System.out.print(coreGame.getCurrentBoard().getFigure(x, 7 - y).getSymbol() + " ");
            }
            System.out.println("");
        }
        System.out.println("  a b c d e f g h");
        System.out.println("");
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
        return input.length() == 5 || input.length() == 6 && !input.isEmpty();
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
