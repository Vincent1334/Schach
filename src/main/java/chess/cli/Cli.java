package chess.cli;

import java.util.Scanner;

/**
 * Starting point of the command line interface
 */
public class Cli {

    static Scanner scan;

    /**
     * The entry point of the CLI application.
     *
     * @param args The command line arguments passed to the application
     */
    public static void main(String[] args) {
        init();
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







    }

    public static void init(){
        scan = new Scanner(System.in);

    }
}
