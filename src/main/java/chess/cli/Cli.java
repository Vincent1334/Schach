package chess.cli;

import chess.model.CoreGame;

import java.io.PrintWriter;
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
        init();
        enterGame();
    }

    /**
     * User interface to initial the Gamemode
     */
    public static void init(){
        do{
            clearWindow();
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
            if(input.length() == 1 && input.charAt(0) >= 49 && input.charAt(0) <= 51){
                coreGame = new CoreGame(input.charAt(0)-48);
                break;
            }
        }while(true);
    }

    /**
     * Gameloop
     * Clear Windows, drawBoard and wait for user input
     * After game the loop ends
     */
    public static void enterGame(){
        do{
            clearWindow();
            drawBoard();
            System.out.print("");
        }while(true);
    }

    /**
     * Draw the board into the commandline
     */
    public static void drawBoard(){
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                System.out.print(8-y);
                printWriter.print(coreGame.getBoard().getFigure(x, y).getSymbol());
            }
            System.out.print("\n");
        }
        System.out.println("  a b c d e f g h");
    }

    /**
     * Clear the commandline Window
     */
    public static void clearWindow(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
