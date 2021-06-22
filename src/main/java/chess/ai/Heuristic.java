package chess.ai;


import chess.figures.Figure;
import chess.figures.Knight;
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

    private static final float PAWN_BONUS = 1;
    private static final float ROOK_BONUS = 5;
    private static final float BISHOP_BONUS = 6;
    private static final float KNIGHT_BONUS = 6;
    private static final float QUEEN_BONUS = 4;

    private static final float CASTLING_POINTS = 1000;
    private static final float CHECK_MATE_POINTS = 100000;
    private static final float CHECK_POINTS = 10;

    private static final float REPEAT_POINTS = 100;

    private static final float PAWN_CHAIN_POINTS = 15;
    private static final float ENDGAME_POINTS = 25;

    /**
     *
     * @param move the current move
     * @param lastMove the last move
     * @return zero when current and last move are the same
     */
   public static float checkRepeat(Move move, Move lastMove, boolean playerMax){
        if(lastMove != null && move.getActualFigure() == lastMove.getActualFigure() && move.getActualFigure().isBlackTeam() == playerMax) return -REPEAT_POINTS;
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
            case 1: figureScore = PAWN_BONUS; break;
            case 2: figureScore = ROOK_BONUS; break;
            case 3: figureScore = BISHOP_BONUS; break;
            case 4: figureScore = KNIGHT_BONUS; break;
            case 5: figureScore = QUEEN_BONUS; break;
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
                if(move.getActualFigure().isBlackTeam() != playerMax) score = -score;
            }
        }
        return score;
    }

    /**
     *
     * @param material material for each figure
     * @param playerMax maximizing player
     * @return the determined value for the material score
     */
    public static float checkMaterial(int[][] material, boolean playerMax){
        float score = 0;
        int playerMaxID = playerMax ? 1 : 0;
        int playerMinID = !playerMax ? 1 : 0;
        score += QUEEN_MATERIAL *material[playerMaxID][4]- QUEEN_MATERIAL *material[playerMinID][4];
        score += ROOK_MATERIAL *material[playerMaxID][1]- ROOK_MATERIAL *material[playerMinID][1];
        score += BISHOP_MATERIAL *material[playerMaxID][2]- BISHOP_MATERIAL *material[playerMinID][2];
        score += KNIGHT_MATERIAL *material[playerMaxID][3]- KNIGHT_MATERIAL *material[playerMinID][3];
        score += PAWN_MATERIAL *material[playerMaxID][0]- PAWN_MATERIAL *material[playerMinID][0];

        return score;
    }

    public static float checkFieldScore(int[][] fieldScore, boolean playerMax){
        float score = 0;
        int playerMaxID = playerMax ? 1 : 0;
        int playerMinID = !playerMax ? 1 : 0;
        score += fieldScore[playerMaxID][0]-fieldScore[playerMinID][0];
        score += fieldScore[playerMaxID][1]-fieldScore[playerMinID][1];
        score += fieldScore[playerMaxID][4]-fieldScore[playerMinID][4];
        score += fieldScore[playerMaxID][5]-fieldScore[playerMinID][5];

        return score;
    }

    public static float checkEndGame(Board board, boolean playerMax, boolean isEndGame){
        float score = 0;
        if(isEndGame){
            for(int y = 0; y < 8; y++){
                for(int x = 0; x < 8; x++){
                    if (board.getFigure(x, y) instanceof Knight && board.getFigure(x, y).isBlackTeam() == playerMax){
                        score -= ENDGAME_POINTS;
                    }
                    if (board.getFigure(x, y) instanceof Knight && board.getFigure(x, y).isBlackTeam() != playerMax){
                        score += ENDGAME_POINTS;
                    }
                }
            }
        }
        return score;
    }

    /**
     *
     * @param board the current board
     * @param playerMax maximizing player
     * @return castling points
     */
    public static float checkCastling(Board board, boolean playerMax){
        return (board.isCastlingFlag(playerMax) ? CASTLING_POINTS : 0) - (board.isCastlingFlag(!playerMax) ? CASTLING_POINTS : 0);
    }

    /**
     *
     * @param board the current board
     * @param playerMax maximizing player
     * @return chess mate points
     */
    public static float checkChessMate(Board board, boolean playerMax){
        return (board.isCheckMateFlag(!playerMax) ? CHECK_MATE_POINTS : 0) - (board.isCheckMateFlag(playerMax) ? CHECK_MATE_POINTS : 0);
    }

    /**
     *
     * @param board the current board
     * @param playerMax maximizing player
     * @return check points
     */
    public static float checkChess(Board board, boolean playerMax){
        return (board.isCheckFlag(!playerMax) ? CHECK_POINTS : 0) - (board.isCheckFlag(playerMax) ? CHECK_POINTS : 0);
    }
}
