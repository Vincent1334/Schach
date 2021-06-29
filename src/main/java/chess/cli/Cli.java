package chess.cli;

import chess.controller.CoreGame;
import chess.ai.Computer;
import chess.managers.LanguageManager;
import chess.model.Board;
import chess.model.Parser;
import javafx.scene.text.Text;

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
    private static int gameMode = 0;
    private static Computer computer;
    private static int pointer;

    private static List<Board> undoRedoMovesAsBoard = new ArrayList<>();

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
            do {
                System.out.println(" ██████╗██╗  ██╗███████╗███████╗███████╗");
                System.out.println("██╔════╝██║  ██║██╔════╝██╔════╝██╔════╝");
                System.out.println("██║     ███████║█████╗  ███████╗███████╗");
                System.out.println("██║     ██╔══██║██╔══╝  ╚════██║╚════██║");
                System.out.println("╚██████╗██║  ██║███████╗███████║███████║");
                System.out.println(" ╚═════╝╚═╝  ╚═╝╚══════╝╚══════╝╚══════╝");
                System.out.println("");
                System.out.println(LanguageManager.getText("gamemode_title"));
                System.out.println(LanguageManager.getText("gamemode01"));
                System.out.println(LanguageManager.getText("gamemode02"));
                System.out.print(LanguageManager.getText("input_label"));

                String input = scan.nextLine();
                if (input.length() == 1 && input.charAt(0) >= 49 && input.charAt(0) <= 50) {
                    gameMode = input.charAt(0) - 48;
                    coreGame = new CoreGame();
                    break;
                } else if (input.equals("language")) {
                    LanguageManager.nextLocale();
                }
            } while (true);
            //Enter simpleMode
        } else {
            coreGame = new CoreGame();
        }

        //create Computer
        if (gameMode == 2) {
            computer = new Computer(true);
        }

        //initialize pointer
        pointer = coreGame.getMoveHistory().size() - 1;
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
                System.out.println(LanguageManager.getText("beaten_figures_label"));
                for (int i = 0; i < coreGame.getCurrentBoard().getBeatenFigures().size(); i++) {
                    System.out.println(coreGame.getCurrentBoard().getBeatenFigures().get(i).getSymbol());
                }
                continue;
            }
            if (input.equals("language")) {
                LanguageManager.nextLocale();
                System.out.println(LanguageManager.getText("language"));
                continue;
            }

            if (input.equals("undo")) {
                undo();
            }

            if (input.equals("redo")) {
                redo();
            }

            // Check syntax and make move
            if (Parser.validSyntax(input)) {
                if (!coreGame.chessMove(Parser.parse(input))) {
                    continue;
                } else {
                    pointer = coreGame.getMoveHistory().size() - 1;
                    if (!undoRedoMovesAsBoard.isEmpty()) {
                        resetUndoRedo();
                    }
                }
            } else {
                System.out.println(LanguageManager.getText("invalid_move_label"));
                continue;
            }

            // Check computer move
            if (gameMode == 2) {
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

    public static void undo() {
        pointer--;
        Board newBoard;
        if (pointer >= 0) {
            newBoard = coreGame.getMoveHistory().get(pointer);
        } else {
            newBoard = new Board();
        }
        undoRedoMovesAsBoard.add(coreGame.getCurrentBoard());
        //Board eins zurück setzen
        coreGame.setCurrentBoard(new Board(newBoard));
        //Spielerwechsel
        coreGame.setActivePlayer(!coreGame.getActivePlayer());
    }

    public static void redo() {
        pointer++;
        Board currentBoard;
        if (pointer >= 0) {
            currentBoard = coreGame.getMoveHistory().get(pointer);
        } else {
            currentBoard = coreGame.getMoveHistory().get(0);
        }
        undoRedoMovesAsBoard.remove(coreGame.getMoveHistory().get(pointer));
        //setze Board eins vor
        coreGame.setCurrentBoard(new Board(currentBoard));
        //Spielerwechsel
        coreGame.setActivePlayer(!coreGame.getActivePlayer());
    }

    public static void resetUndoRedo() {
        // MoveHistory von CoreGame & entsprechend Pointer
        for (Board board : undoRedoMovesAsBoard) {
            coreGame.getMoveHistory().remove(board);
        }
        pointer = coreGame.getMoveHistory().size() - 1;
        undoRedoMovesAsBoard.clear();
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
