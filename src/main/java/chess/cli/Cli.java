package chess.cli;

import chess.model.CoreGame;

import java.util.Scanner;

/**
 * Starting point of the command line interface
 */
public class Cli {

    static Scanner scan;
    static CoreGame coreGame;


    /**
     * The entry point of the CLI application.
     *
     * @param args The command line arguments passed to the application
     */
    public static void main(String[] args) {
        scan = new Scanner(System.in);
        init();
        enterGame();
    }

    public static void init(){
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
        int input = scan.nextInt();
        if(input <= 1 && input <= 3) coreGame = new CoreGame(input);
        else init();
    }

    public static void enterGame(){
        clearWindow();

    }

    public static void clearWindow(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
