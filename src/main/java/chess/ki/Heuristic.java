package chess.ki;


import chess.figures.Figure;
import chess.figures.Pawn;
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
    private static final float PAWN_MATERIAL = 100;
    private static final float ROOK_MATERIAL = 500;
    private static final float BISHOP_MATERIAL = 320;
    private static final float KNIGHT_MATERIAL = 325;
    private static final float QUEEN_MATERIAL = 975;

    private static final float CASTLING_POINTS = 100000;
    private static final float CHECK_MATE_POINTS = 100000;
    private static final float CHECK_POINTS = 30;

    private static final float REPEAT_POINTS = 550;

    private static final float PAWN_CHAIN_POINTS = 120;

    /**
     *
     * @param move the current move
     * @param lastMove the last move
     * @return zero when current and last move are the same
     */
   public static float checkRepeat(Move move, Move lastMove){
        if(lastMove != null && move.getActualFigure() == lastMove.getActualFigure()) return -REPEAT_POINTS;
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
            case 1: figureScore = PAWN_MATERIAL; break;
            case 2: figureScore = ROOK_MATERIAL; break;
            case 3: figureScore = BISHOP_MATERIAL; break;
            case 4: figureScore = KNIGHT_MATERIAL; break;
            case 5: figureScore = QUEEN_MATERIAL /3; break;
        }
        if(move.getActualFigure().isBlackTeam() != playerMax){
            figureScore = figureScore * (-1);
        }
        return figureScore;
    }

    public static float checkPawnChain(Board board, Move move, boolean playerMax){
        float score = 0;
        if(move.getActualFigure() instanceof Pawn){
            for(int i = 0; i < 4; i++){
                Figure tmpFigure;
                try{
                    switch(i){
                        case 0: tmpFigure = board.getFigure(move.getTargetPosition().getPosX()-1, move.getTargetPosition().getPosY()-1); break;
                        case 1: tmpFigure = board.getFigure(move.getTargetPosition().getPosX()-1, move.getTargetPosition().getPosY()+1); break;
                        case 2: tmpFigure = board.getFigure(move.getTargetPosition().getPosX()+1, move.getTargetPosition().getPosY()-1); break;
                        case 3: tmpFigure = board.getFigure(move.getTargetPosition().getPosX()+1, move.getTargetPosition().getPosY()+1); break;
                        default: continue;
                    }
                }catch(Exception x){
                    continue;
                }
                if(tmpFigure instanceof Pawn && tmpFigure.isBlackTeam() == move.getActualFigure().isBlackTeam()){
                    score += PAWN_CHAIN_POINTS;
                }
            }
        }
        if(move.getActualFigure().isBlackTeam() != playerMax){
            score = score *-1;
        }
        return score;
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
        int playerMaxID = playerMax ? 1 : 0;
        int playerMinID = playerMin ? 1 : 0;
        score += QUEEN_MATERIAL *material[playerMaxID][4]- QUEEN_MATERIAL *material[playerMinID][4];
        score += ROOK_MATERIAL *material[playerMaxID][1]- ROOK_MATERIAL *material[playerMinID][1];
        score += BISHOP_MATERIAL *material[playerMaxID][2]- BISHOP_MATERIAL *material[playerMinID][2];
        score += KNIGHT_MATERIAL *material[playerMaxID][3]- KNIGHT_MATERIAL *material[playerMinID][3];
        score += PAWN_MATERIAL *material[playerMaxID][0]- PAWN_MATERIAL *material[playerMinID][0];

        return score;
    }

    /**
     *
     * @param board the current board
     * @param playerMax maximizing player
     * @return castling points
     */
    public static float checkCastling(Board board, boolean playerMax){
        return board.isCastlingFlag(playerMax) ? CASTLING_POINTS : 0;
    }

    /**
     *
     * @param board the current board
     * @param playerMax maximizing player
     * @param playerMin minimizing player
     * @return chess mate points
     */
    public static float checkChessMate(Board board, boolean playerMax, boolean playerMin){
        return (board.isCheckMateFlag(playerMax) ? -CHECK_MATE_POINTS : 0) + (board.isCastlingFlag(playerMin) ? CHECK_MATE_POINTS : 0);
    }

    /**
     *
     * @param board the current board
     * @param playerMax maximizing player
     * @param playerMin minimizing player
     * @return check points
     */
    public static float checkChess(Board board, boolean playerMax, boolean playerMin){
        return (board.isCheckFlag(playerMax) ? -CHECK_POINTS : 0) + (board.isCheckFlag(playerMin) ? CHECK_POINTS : 0);
    }
}
