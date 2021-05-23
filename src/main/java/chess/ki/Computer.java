package chess.ki;

import chess.figures.None;
import chess.model.Board;

public class Computer {

    private boolean isBlack;

    public Computer(boolean isBlack) {
        this.isBlack = isBlack;
    }

    public String makeMove(Board board) {
        return "";
    }

    private int heuristic(Board board) {
        int score = 0;
        //check material
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (board.getFigure(x, y).isBlackTeam() == isBlack && !(board.getFigure(x, y) instanceof None)) {
                    switch (board.getFigure(x, y).getFigureID()) {
                        case 1:
                            score = score + 1;
                            break;
                        case 2:
                            score = score + 5;
                            break;
                        case 3:
                        case 4:
                            score = score + 3;
                            break;
                        case 5:
                            score = score + 9;
                            break;
                        case 6:
                            score = score + 900;
                            break;
                    }
                }
            }
        }
        return 0;
    }
}