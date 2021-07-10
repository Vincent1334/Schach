package chess.cli;

import chess.enums.GameMode;
import chess.controller.CoreGame;
import chess.ai.Computer;
import chess.managers.LanguageManager;
import chess.model.Board;
import chess.model.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Starting point of the command line interface
 */
public class Cli {

    private static Scanner scan;
    private static CoreGame coreGame;
    private static GameMode gameMode2;
    private static Computer computer;
    private static int pointer;

    private static final List<Board> UNDO_REDO_MOVES_AS_BOARD = new ArrayList<>();

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
     *
     * @param args for simple mode
     */
    public static void init(String[] args) {
        if (!Arrays.asList(args).contains("--simple")) {
            startLoop();
            //Enter simpleMode
        } else {
            coreGame = new CoreGame();
        }
        //create Computer
        if (gameMode2 == GameMode.COMPUTER) {
            computer = new Computer(true);
        }
        //initialize pointer
        pointer = coreGame.getMoveHistory().size() - 1;
    }

    /**
     * the start loop
     * (main menu with options to choose the gamemode and language)
     */
    public static void startLoop(){
        do {
            System.out.println(" ██████╗██╗  ██╗███████╗███████╗███████╗");
            System.out.println("██╔════╝██║  ██║██╔════╝██╔════╝██╔════╝");
            System.out.println("██║     ███████║█████╗  ███████╗███████╗");
            System.out.println("██║     ██╔══██║██╔══╝  ╚════██║╚════██║");
            System.out.println("╚██████╗██║  ██║███████╗███████║███████║");
            System.out.println(" ╚═════╝╚═╝  ╚═╝╚══════╝╚══════╝╚══════╝");
            System.out.println("");
            System.out.println(LanguageManager.getText("gamemode_title"));
            System.out.println("1. " + LanguageManager.getText("gamemode01"));
            System.out.println("2. " + LanguageManager.getText("gamemode02"));
            System.out.println(LanguageManager.getText("chooseLanguage"));
            System.out.print(LanguageManager.getText("input_label"));

            String input = scan.nextLine();
            if (input.length() == 1 && input.charAt(0) >= 49 && input.charAt(0) <= 50) {
                gameMode2 = input.charAt(0) == 49 ? GameMode.NORMAL : GameMode.COMPUTER;
                coreGame = new CoreGame();
                break;
            }else if (input.equals("de")||input.equals("fr")||input.equals("en")){
                LanguageManager.setLanguage(input);
            }
        } while (true);
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

            if (checkCommands(input)) {
                continue;
            }

            // Check syntax and make move
            if (Parser.validSyntax(input)) {
                if (!coreGame.chessMove(Parser.parse(input))) {
                    continue;
                } else {
                    pointer = coreGame.getMoveHistory().size() - 1;
                    if (!UNDO_REDO_MOVES_AS_BOARD.isEmpty()) {
                        resetUndoRedo();
                    }
                }
            } else {
                System.out.println(LanguageManager.getText("invalid_move_label"));
                continue;
            }
            checkComputerMove();
        } while (!coreGame.isGameOver());
    }

    /**
     * Check command input
     *
     * @param input keyword
     * @return True when command performed
     */
    private static boolean checkCommands(String input) {
        //check Commands
        if (input.equals("beaten")) {
            System.out.println(LanguageManager.getText("beaten_figures_label"));
            for (int i = 0; i < coreGame.getCurrentBoard().getBeatenFigures().size(); i++) {
                System.out.println(coreGame.getCurrentBoard().getBeatenFigures().get(i).getSymbol());
            }
            return true;
        }
        if (input.equals("de")||input.equals("fr")||input.equals("en")){
            LanguageManager.setLanguage(input);
        }

        if (input.equals("undo")) {
            undo();
            return true;
        }

        if (input.equals("redo")) {
            redo();
            return true;
        }
        return false;
    }

    /**
     * checks and performs the computer move
     */
    public static void checkComputerMove() {
        //check computer move
        if (gameMode2 == GameMode.COMPUTER) {
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
            pointer = coreGame.getMoveHistory().size() - 1;
        }
    }


    /**
     * undo one move for games against a friend and two for a game against the computer
     */
    public static void undo() {
        if (pointer >= 0) {
            UNDO_REDO_MOVES_AS_BOARD.add(coreGame.getMoveHistory().get(pointer));

            if (gameMode2 == GameMode.COMPUTER) {
                UNDO_REDO_MOVES_AS_BOARD.add(coreGame.getMoveHistory().get(pointer - 1));
                pointer--;
            }
            pointer--;
            Board newBoard;
            if (pointer >= 0) {
                newBoard = coreGame.getMoveHistory().get(pointer);
            } else {
                newBoard = new Board();
            }
            coreGame.setCurrentBoard(new Board(newBoard));

            // Change of players
            coreGame.setActivePlayerBlack(pointer % 2 == 0);
        }
    }

    /**
     * redo one move for games against a friend and two for a game against the computer
     */
    public static void redo() {
        if (UNDO_REDO_MOVES_AS_BOARD.size() > 0) {
            pointer++;

            if (gameMode2 == GameMode.COMPUTER) {
                pointer++;
                UNDO_REDO_MOVES_AS_BOARD.remove(coreGame.getMoveHistory().get(pointer - 1));
            }

            Board currentBoard;
            if (pointer >= 0) {
                currentBoard = coreGame.getMoveHistory().get(pointer);
            } else {
                currentBoard = coreGame.getMoveHistory().get(0);
            }
            UNDO_REDO_MOVES_AS_BOARD.remove(coreGame.getMoveHistory().get(pointer));
            coreGame.setCurrentBoard(new Board(currentBoard));

            coreGame.setActivePlayerBlack(pointer % 2 == 0);
        }
    }

    /**
     * updates the moveHistory, pointer and clears the undoRedoMovesAsBoard
     */
    public static void resetUndoRedo() {
        // MoveHistory of CoreGame and corresponding Pointer
        for (Board board : UNDO_REDO_MOVES_AS_BOARD) {
            coreGame.getMoveHistory().remove(board);
        }
        pointer = coreGame.getMoveHistory().size() - 1;
        UNDO_REDO_MOVES_AS_BOARD.clear();
    }

    /**
     * Screen for credits
     */
    public static void endGame() {
        System.out.println(LanguageManager.getText("gameover_label"));
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
