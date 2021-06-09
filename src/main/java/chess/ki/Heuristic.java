package chess.ki;


import chess.model.Board;
import chess.model.Move;

/**
 * This class contains the information about the processes when the computer does a chess move.
 * It will determine the best move with alpha beta pruning and return the move
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-01
 */

public class Heuristic {

    //Points
    private static final float pawnMaterial = 100;
    private static final float rookMaterial = 500;
    private static final float bishopMaterial = 320;
    private static final float knightMaterial = 325;
    private static final float queenMaterial = 975;
    //private static final float kingMaterial = 32000;

    private static final float castlingPoints = 80;
    private static final float checkMatePoints = 100000;
    private static final float checkPoints = 30;

    private static final float repeatPoints = 200;

    /**
     *
     * @param move the current move
     * @param lastMove the last move
     * @return zero when current and last move are the same
     */
   public static float checkRepeat(Move move, Move lastMove){
        if(lastMove != null && move.getActualFigure() == lastMove.getActualFigure()) return -repeatPoints;
        return 0;
    }

    /**
     *
     * @param move the current move
     * @param playerMax maximizing player
     * @return figure score
     */
    public static float checkFigureScore(Move move, boolean playerMax){
        float figureScore = 0;
        switch(move.getActualFigure().getFigureID()){
            case 1: figureScore = pawnMaterial; break;
            case 2: figureScore = rookMaterial; break;
            case 3: figureScore = bishopMaterial; break;
            case 4: figureScore = knightMaterial; break;
            case 5: figureScore = queenMaterial/3; break;
        }
        if(move.getActualFigure().isBlackTeam() != playerMax){
            figureScore = figureScore * (-1);
        }
        return figureScore;
    }

    /**
     *
     * @param material material for each figure
     * @param playerMax maximizing player
     * @param playerMin minimizing player
     * @return the determined value for the material score
     */
    public static float checkMaterial(int[][] material, boolean playerMax, boolean playerMin){
        float score = 0;
        score += queenMaterial*material[playerMax ? 1 : 0][4]-queenMaterial*material[playerMin ? 1 : 0][4];
        score += rookMaterial*material[playerMax ? 1 : 0][1]-rookMaterial*material[playerMin ? 1 : 0][1];
        score += bishopMaterial*material[playerMax ? 1 : 0][2]-bishopMaterial*material[playerMin ? 1 : 0][2];
        score += knightMaterial*material[playerMax ? 1 : 0][3]-knightMaterial*material[playerMin ? 1 : 0][3];
        score += pawnMaterial*material[playerMax ? 1 : 0][0]-pawnMaterial*material[playerMin ? 1 : 0][0];

        return score;
    }

    /**
     *
     * @param board the current board
     * @param playerMax maximizing player
     * @return castling points
     */
    public static float checkCastling(Board board, boolean playerMax){
        return board.isCastlingFlag(playerMax) ? castlingPoints : 0;
    }

    /**
     *
     * @param board the current board
     * @param playerMax maximizing player
     * @param playerMin minimizing player
     * @return chess mate points
     */
    public static float checkChessMate(Board board, boolean playerMax, boolean playerMin){
        return (board.isCheckMateFlag(playerMax) ? -checkMatePoints : 0) + (board.isCastlingFlag(playerMin) ? checkMatePoints : 0);
    }

    /**
     *
     * @param board the current board
     * @param playerMax maximizing player
     * @param playerMin minimizing player
     * @return check points
     */
    public static float checkChess(Board board, boolean playerMax, boolean playerMin){
        return (board.isCheckFlag(playerMax) ? -checkPoints : 0) + (board.isCheckFlag(playerMin) ? checkPoints : 0);
    }
}
