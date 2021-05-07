package chess.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * This class contains the information about the chess board which contains the chess pieces/figures
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-05-07
 */
public class Board {

    private Figure[][] internalBoard;
    private List<Figure> beatenFigures = new ArrayList<>();

    /**
     * Creates a new board with the standard figure setup
     */
    public Board() {
        internalBoard = new Figure[8][8];

        for (int team = 0; team <= 1; team++) {
            //create Pawns
            for (int i = 0; i < 8; i++) {
                internalBoard[i][1 + team * 5] = new Pawn(team);
            }
            //create King
            internalBoard[4][team * 7] = new King(team);

            //create Queen
            internalBoard[3][team * 7] = new Queen(team);

            //create Rooks
            for (int i = 0; i <= 1; i++) {
                internalBoard[i * 7][team * 7] = new Rook(team);
            }
            //create Bishops
            for (int i = 0; i <= 1; i++) {
                internalBoard[2 + i * 3][team * 7] = new Bishop(team);
            }
            //create Knights
            for (int i = 0; i <= 1; i++) {
                internalBoard[1 + i * 5][team * 7] = new Knight(team);
            }
        }
        //create None
        for (int y = 2; y < 6; y++) {
            for (int x = 0; x < 8; x++) {
                internalBoard[x][y] = new None();
            }
        }
    }

    /**
     * Copy Constructor
     * copies the current board
     *
     * @param sourceClass the board-object you want to copy
     */
    public Board(Board sourceClass) {
        internalBoard = new Figure[8][8];
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                switch (sourceClass.getFigure(x, y).getFigureID()) {
                    case 0:
                        internalBoard[x][y] = new None((None) sourceClass.getFigure(x, y));
                        break;
                    case 1:
                        internalBoard[x][y] = new Pawn((Pawn) sourceClass.getFigure(x, y));
                        break;
                    case 2:
                        internalBoard[x][y] = new Rook((Rook) sourceClass.getFigure(x, y));
                        break;
                    case 3:
                        internalBoard[x][y] = new Knight((Knight) sourceClass.getFigure(x, y));
                        break;
                    case 4:
                        internalBoard[x][y] = new Bishop((Bishop) sourceClass.getFigure(x, y));
                        break;
                    case 5:
                        internalBoard[x][y] = new Queen((Queen) sourceClass.getFigure(x, y));
                        break;
                    case 6:
                        internalBoard[x][y] = new King((King) sourceClass.getFigure(x, y));
                        break;
                }
            }
        }
        // beatenFigures wird allerdings nicht verwendet
        beatenFigures = new ArrayList<>();
        beatenFigures.addAll(sourceClass.getBeatenFigures());
    }

    /**
     * Get the figure at a specified position on the board
     *
     * @param x x-coordinate of the position
     * @param y y-coordinate of the position
     * @return the figure at this position of the board
     */
    public Figure getFigure(int x, int y) {
        return internalBoard[x][y];
    }

    /**
     * Get the figure at a specified position on the board
     *
     * @param position the position on the board where you want to get the figure from
     * @return the figure at this position of the board
     */
    public Figure getFigure(Position position) {
        return internalBoard[position.getPosX()][position.getPosY()];
    }

    /**
     * Set a figure to a specified position on the board
     *
     * @param x      x-coordinate of the position
     * @param y      y-coordinate of the position
     * @param figure the figure you want to set to the position on the board
     */
    public void setFigure(int x, int y, Figure figure) {
        internalBoard[x][y] = figure;
    }

    /**
     * Set a figure to a specified position on the board
     *
     * @param position the position on the board where you want to set the figure to
     * @param figure   the figure you want to set to the position on the board
     */
    public void setFigure(Position position, Figure figure) {
        int posX = position.getPosX();
        int posY = position.getPosY();
        internalBoard[posX][posY] = figure;
    }

    /**
     * Get the list of beatenFigures of the board
     *
     * @return the list of beatenFigures of the board
     */
    public List<Figure> getBeatenFigures() {
        return this.beatenFigures;
    }

    /*
    <-------Board-analysis--------------------------------------------------------------------------------------------->
     */

    /**
     * Get the position of the target king
     *
     * @param board current chessboard
     * @param team  team-ID of the king you want to get
     * @return position of target king, {0,0} if there is no king found
     */
    public static Position getKing(Board board, int team) {
        //Searching target King position
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (board.getFigure(x, y) instanceof King && board.getFigure(x, y).getTeam() == team) {
                    //Return target king position
                    return new Position(x, y);
                }
            }
        }
        //Return nothing
        return new Position(0, 0);
    }

    /**
     * Checks if the figure is threatened by the other team
     *
     * @param tmpBoard  current chessboard
     * @param team      team of the figure you want to check
     * @param targetPos position of the figure you want to check
     * @return whether the figure is threatened by the other team
     */
    public static boolean isThreatened(Board tmpBoard, Position targetPos, int team) {
        //Check if any enemy figure can do a valid move to target Position
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (tmpBoard.getFigure(x, y) != null &&
                        tmpBoard.getFigure(x, y).getTeam() != team &&
                        tmpBoard.getFigure(x, y).validMove(new Position(x, y), targetPos, tmpBoard)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Check chessMate
     *
     * @param board current chessboard
     * @param team  the team of the target king
     * @return whether the king of "team"-color is in checkmate
     */
    public static boolean checkChessMate(Board board, int team) {
        boolean possibleSolution = false;

        // test all possible moves and check whether your king is still in check
        for (int y = 0; y < 8; y++) {                                           // for all your own figures on the board
            for (int x = 0; x < 8; x++) {
                if (board.getFigure(new Position(x, y)).getTeam() == team) {
                    for (int newX = 0; newX < 8; newX++) {                      // test all possible moves to every possible targetField
                        for (int newY = 0; newY < 8; newY++) {
                            Board tmpBoard = new Board(board);                  // on a copy of the board
                            possibleSolution = possibleSolution || possibleSolution(new Position(x, y),new Position(newX, newY),tmpBoard,team);
                        }
                    }
                }
            }
        }
        if (!possibleSolution) {
            System.out.println("You are checkmate");
        }
        return !possibleSolution;
    }

    private static boolean possibleSolution(Position actualPos, Position targetPos, Board tmpBoard, int team) {
        if (Rules.checkEnPassant(actualPos, targetPos, tmpBoard)) {             // check EnPassant and eventually perform it on the temporary board
            Rules.performEnPassantMove(actualPos, targetPos, tmpBoard);
        }
        if (Rules.checkCastling(actualPos, targetPos, tmpBoard) == 1) {         // check CastlingLeft and eventually perform it on the temporary board
            Rules.performCastlingMoveLeft(actualPos, targetPos, tmpBoard);
        }
        if (Rules.checkCastling(actualPos, targetPos, tmpBoard) == 2) {         // check CastlingRight and eventually perform it on the temporary board
            Rules.performCastlingMoveRight(actualPos, targetPos, tmpBoard);
        }
        if (Rules.checkValidDefaultMove(actualPos, targetPos, tmpBoard)) {      // checkValidDefaultMove and eventually perform it on the temporary board
            Rules.performDefaultMove(actualPos, targetPos, tmpBoard);
        }
        return !kingInCheck(tmpBoard, team);
    }

    /**
     * Checks whether the king is in check
     *
     * @param board current chessboard
     * @param team  The team ID of the target King
     * @return Whether the king is in check or not
     */
    public static boolean kingInCheck(Board board, int team) {
        // System.out.println("You are in check");      --> hier auskommentiert, da sonst auch (falsche) Ausgabe bei checkChessMate
        return isThreatened(board, Board.getKing(board, team), team);
    }

    /*@Override
    public boolean equals(Object other) {
        if (other instanceof Board) {
            return ((((Board) other).getBoard()) == board) && (((Board) other).getBeatenFigures() == beatenFigures);
        }
        return false;
    }*/

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Board board1 = (Board) other;
        return Arrays.deepEquals(internalBoard, board1.internalBoard) && Objects.equals(beatenFigures, board1.beatenFigures);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(beatenFigures);
        result = 31 * result + Arrays.deepHashCode(internalBoard);
        return result;
    }
}
