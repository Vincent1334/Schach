package chess.ki;

import java.util.Arrays;

/**
 * This class contains information about the strategic positions of the figures king, knight, bishop and pawn
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-02
 */
public class PieceSquareTable {

    /**
     *piece square table for a white pawn
     * contains information about clever positions for a pawn
     */
    public final static short[][] pawnTable = new short[][]
            {
                {0,  0,  0,  0,  0,  0,  0,  0},
                {50, 50, 50, 50, 50, 50, 50, 50},
                {10, 10, 20, 30, 30, 20, 10, 10},
                {5,  5, 10, 27, 27, 10,  5,  5},
                {0,  0,  0, 25, 25,  0,  0,  0},
                {5, -5,-10,  0,  0,-10, -5,  5},
                {5, 10, 10,-25,-25, 10, 10,  5},
                {0,  0,  0,  0,  0,  0,  0,  0}
            };


    /**
     *piece square table for a white knight
     * contains information about clever positions for a knight
     */
    public final static short[][] knightTable = new short[][]
            {
                {-50,-40,-30,-30,-30,-30,-40,-50},
                {-40,-20,  0,  0,  0,  0,-20,-40},
                {-30,  0, 10, 15, 15, 10,  0,-30},
                {-30,  5, 15, 20, 20, 15,  5,-30},
                {-30,  0, 15, 20, 20, 15,  0,-30},
                {-30,  5, 10, 15, 15, 10,  5,-30},
                {-40,-20,  0,  5,  5,  0,-20,-40},
                {-50,-40,-20,-30,-30,-20,-40,-50},
            };


    /**
     *piece square table for a white bishop
     * contains information about clever positions for a bishop
     */
    public final static short[][] bishopTable = new short[][]
            {
                {-20,-10,-10,-10,-10,-10,-10,-20},
                {-10,  0,  0,  0,  0,  0,  0,-10},
                {-10,  0,  5, 10, 10,  5,  0,-10},
                {-10,  5,  5, 10, 10,  5,  5,-10},
                {-10,  0, 10, 10, 10, 10,  0,-10},
                {-10, 10, 10, 10, 10, 10, 10,-10},
                {-10,  5,  0,  0,  0,  0,  5,-10},
                {-20,-10,-40,-10,-10,-40,-10,-20},
            };


    /**
     *piece square table for a white king
     * contains information about clever positions for a king
     */
    public final static short[][] kingTable = new short[][]
            {
                {-30, -40, -40, -50, -50, -40, -40, -30},
                {-30, -40, -40, -50, -50, -40, -40, -30},
                {-30, -40, -40, -50, -50, -40, -40, -30},
                {-30, -40, -40, -50, -50, -40, -40, -30},
                {-20, -30, -30, -40, -40, -30, -30, -20},
                {-10, -20, -20, -20, -20, -20, -20, -10},
                { 20,  20,   0,   0,   0,   0,  20,  20},
                { 20,  30,  10,   0,   0,  10,  30,  20}
            };


    /**
     *piece square table for a white king in the endgame
     * contains information about clever positions for a king in the end game
     */
    /*public final static short[][] kingTableEndGame = new short[][]
            {
                {-50,-40,-30,-20,-20,-30,-40,-50},
                {-30,-20,-10,  0,  0,-10,-20,-30},
                {-30,-10, 20, 30, 30, 20,-10,-30},
                {-30,-10, 30, 40, 40, 30,-10,-30},
                {-30,-10, 30, 40, 40, 30,-10,-30},
                {-30,-10, 20, 30, 30, 20,-10,-30},
                {-30,-30,  0,  0,  0,  0,-30,-30},
                {-50,-30,-30,-30,-30,-30,-30,-50}
            };*/

    /**
     *piece square table for a white pawn
     * contains information about clever positions for a pawn
     */
    public final static short[][] nullTable = new short[][]
            {
                    {0,  0,  0,  0,  0,  0,  0,  0},
                    {0,  0,  0,  0,  0,  0,  0,  0},
                    {0,  0,  0,  0,  0,  0,  0,  0},
                    {0,  0,  0,  0,  0,  0,  0,  0},
                    {0,  0,  0,  0,  0,  0,  0,  0},
                    {0,  0,  0,  0,  0,  0,  0,  0},
                    {0,  0,  0,  0,  0,  0,  0,  0},
                    {0,  0,  0,  0,  0,  0,  0,  0}
            };


    /**
     * returns the correct table for each figure ID
     * @param figureID the figure ID
     * @return the table for the figure ID
     */
    public static short[][] getTable(int figureID){
        switch(figureID){
            case 1: return Arrays.copyOf(pawnTable, pawnTable.length);
            case 3: return Arrays.copyOf(knightTable, knightTable.length);
            case 4: return Arrays.copyOf(bishopTable, bishopTable.length);
            case 6: return Arrays.copyOf(kingTable, kingTable.length);
            default: return Arrays.copyOf(nullTable, nullTable.length);
        }
    }
}