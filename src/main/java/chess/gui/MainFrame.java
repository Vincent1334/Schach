package chess.gui;

import chess.controller.CoreGame;
import chess.model.Move;
import chess.model.Position;
import chess.util.Observer;
import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainFrame implements Initializable {

    private Position mousePosition;

    @FXML
    private Canvas chessBoard;
    @FXML
    private Canvas markMove;
    @FXML
    private Canvas figureCanvas;

    private CoreGame coreGame;

    ArrayList<Move> possibleMoves = new ArrayList<Move>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        coreGame = new CoreGame();

    }

    @FXML
    private void getSelectedField(MouseEvent event) {
        mousePosition = new Position(((int)event.getX()/64), ((int)event.getY()/64));
        chessBoard.getGraphicsContext2D().setStroke(Color.BLACK);
        chessBoard.getGraphicsContext2D().clearRect(0, 0, chessBoard.getWidth(), chessBoard.getHeight());
        chessBoard.getGraphicsContext2D().strokeRect(mousePosition.getPosX()*64, mousePosition.getPosY()*64, 64, 64);
        drawFigures();



    }

    private void drawFigures(){
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                figureCanvas.getGraphicsContext2D().drawImage(getImage(coreGame.getCurrentBoard().getFigure(x, y).getFigureID(), coreGame.getCurrentBoard().getFigure(x, y).isBlackTeam()), x*64+17, y*64+13, 30, 50);
            }
        }

    }

    private void drawPossibleMoves(){
        markMove.getGraphicsContext2D().clearRect(0, 0, markMove.getWidth(), markMove.getHeight());
        markMove.getGraphicsContext2D().setFill(Color.LIGHTBLUE);
        for(int i = 0; i < possibleMoves.size(); i++){
            markMove.getGraphicsContext2D().fillOval(possibleMoves.get(i).getTargetPosition().getPosX()*64+23, possibleMoves.get(i).getTargetPosition().getPosY()*64+23, 20, 20);
        }
    }

    private Image getImage(int symbol, boolean isBlackTeam) {
        switch (symbol) {
            case 1:
                return isBlackTeam ? ImageHandler.getInstance().getImage("PawnBlack") : ImageHandler.getInstance().getImage("PawnWhite");
            // Rook
            case 2:
                return isBlackTeam ? ImageHandler.getInstance().getImage("RookBlack") : ImageHandler.getInstance().getImage("RookWhite");
            // Knight
            case 3:
                return isBlackTeam ? ImageHandler.getInstance().getImage("KnightBlack") : ImageHandler.getInstance().getImage("KnightWhite");
            // Bishop
            case 4:
                return isBlackTeam ? ImageHandler.getInstance().getImage("BishopBlack") : ImageHandler.getInstance().getImage("BishopWhite");
            // Queen
            case 5:
                return isBlackTeam ? ImageHandler.getInstance().getImage("QueenBlack") : ImageHandler.getInstance().getImage("QueenWhite");
            case 6:
                return isBlackTeam ? ImageHandler.getInstance().getImage("KingBlack") : ImageHandler.getInstance().getImage("KingWhite");
        }
        return null;
    }
}





