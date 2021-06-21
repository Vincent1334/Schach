package chess.cli;

import chess.controller.CoreGame;
import chess.ai.Computer;
import chess.model.Move;
import chess.model.Parser;
import chess.model.Position;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * Starting point of the command line interface
 */
public class Cli {

    private static Scanner scan;
    private static CoreGame coreGame;
    private static int gameMode = 0;
    private static Computer computer;

    private static Locale locale = new Locale("en", "US");
    private static ResourceBundle messages = ResourceBundle.getBundle("/languages/MessagesBundle", locale);

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
     * @param args for simple mode
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
                System.out.println(messages.getString("gamemode_title"));
                System.out.println(messages.getString("gamemode01"));
                System.out.println(messages.getString("gamemode02"));
                System.out.println(messages.getString("gamemode03"));
                System.out.print(messages.getString("input_label"));

                String input = scan.nextLine();
                if (input.length() == 1 && input.charAt(0) >= 49 && input.charAt(0) <= 51) {
                    gameMode = input.charAt(0)-48;
                    coreGame = new CoreGame();
                    break;
                }
            } while (true);
            //Enter simpleMode
        } else {
            coreGame = new CoreGame();
        }

        //create Computer
        if(gameMode == 2){
            computer = new Computer(true);
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
                System.out.println(messages.getString("beaten_figures_label"));
                for (int i = 0; i < coreGame.getCurrentBoard().getBeatenFigures().size(); i++) {
                    System.out.println(coreGame.getCurrentBoard().getBeatenFigures().get(i).getSymbol());
                }
                continue;
            }

            // Check syntax and make move
            if (Parser.validSyntax(input)) {
                if(!coreGame.chessMove(Parser.parse(input))){
                    continue;
                }
            }else { 
                System.out.println(messages.getString("invalid_move_label"));
                continue; 
            }

            // Check computer move
            if(gameMode == 2) {
                //draw human input
                drawBoard();
                computer.makeMove(coreGame.getCurrentBoard());
                //waiting for computer
                try {
                    computer.getThread().join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //perform computer move
                coreGame.chessMove(computer.getMove());
            }
        } while (!coreGame.isGameOver());
    }

    /**
     * Screen for credits
     */
    public static void endGame() {
        System.out.println(messages.getString("gameover_label"));
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

}
