package chess.ki;

import chess.figures.None;
import chess.model.Board;
import chess.model.Move;
import chess.model.Position;
import chess.model.Rules;
import java.util.LinkedList;
import java.util.List;

public class Node {

    private Node parent;
    private int alpha, beta, depth;
    private boolean isBlack;
    private Board board;
    private static Board tmpBoard;
    private List<Node> children;

    public Node(Node parentNode, Board board, boolean isBlack, int depth, int alpha, int beta){
        this.parent = parentNode;
        this.depth = depth;
        this.alpha = alpha;
        this.beta = beta;
        this.isBlack = isBlack;
        this.board = board;
        this.children = new LinkedList<Node>();
        explore();
    }

    public void explore(){
        if(depth > 0) {
            for (int y = 0; y < 8; y++) {
                for (int x = 0; x < 8; x++) {
                    if(this.alpha > this.beta){
                        if(this.parent.getAlpha() < this.beta) this.parent.setAlpha(this.beta);
                        return;
                    }
                    if (!(board.getFigure(x, y) instanceof None) && board.getFigure(x, y).isBlackTeam() == isBlack && checkPossibleTarget(board, x, y, isBlack)) {
                        children.add(new Node(this, tmpBoard, !isBlack, depth-1, alpha, beta));
                    }
                }
            }
        }else{
            if(alpha < Computer.heuristic(board, isBlack)){
                 this.alpha = Computer.heuristic(board, isBlack);
                 if(this.parent.getBeta() < this.alpha){
                    this.parent.setBeta(this.alpha);
                 }
            }
        }
    }

    private static boolean checkPossibleTarget(Board board, int x, int y, boolean blackTeam){
            for (int newX = 0; newX < 8; newX++) {                      // test all possible moves to every possible targetField
                for (int newY = 0; newY < 8; newY++) {
                    Board localTmpBoard = new Board(board);                  // on a copy of the board
                    if (possibleSolution(new Position(x, y), new Position(newX, newY), localTmpBoard, blackTeam))
                        tmpBoard = localTmpBoard;
                        return true;
                }
            }
            return false;
        }

        private static boolean possibleSolution(Position actualPos, Position targetPos, Board tmpBoard, boolean blackTeam) {
            if (Rules.checkEnPassant(actualPos, targetPos, tmpBoard)) {             // check EnPassant and eventually perform it on the temporary board
                Rules.performEnPassantMove(actualPos, targetPos, tmpBoard);
            }
            if (Rules.checkDefaultMove(actualPos, targetPos, tmpBoard)) { // checkValidDefaultMove and eventually perform it on the temporary board
                Rules.performDefaultMove(actualPos, targetPos, tmpBoard);
            }
            if (Rules.checkCastling(actualPos, targetPos, tmpBoard)) { // checkValidDefaultMove and eventually perform it on the temporary board
                Rules.performCastlingMove(actualPos, targetPos, tmpBoard);
            }
            if (Rules.checkPawnConversion(actualPos, targetPos, tmpBoard)) { // checkValidDefaultMove and eventually perform it on the temporary board
                Rules.performPawnConversion(actualPos, targetPos, 5, tmpBoard);
            }

            return !Board.kingInCheck(tmpBoard, blackTeam);
        }

    public void setAlpha(int alpha){
        this.alpha = alpha;
    }

    public void setBeta(int beta){
        this.beta = beta;
    }

    public int getAlpha(){
        return alpha;
    }

    public int getBeta(){
        return beta;
    }

    public Move bestMove(){
        return new Move(new Position(0, 0), new Position(0, 0));
    }
}
