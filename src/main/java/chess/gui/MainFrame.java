package chess.gui;

import chess.controller.CoreGame;
import chess.figures.None;
import chess.ki.Computer;
import chess.model.Move;
import chess.model.Position;
import chess.model.Rules;
import chess.util.Observer;
import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainFrame implements Initializable {



    @FXML
    private Canvas chessBoard;
    @FXML
    private Canvas markMove;
    @FXML
    private Canvas figureCanvas;
    @FXML
    private Canvas mouseMarker;
    @FXML
    private Pane mainpanel;

    private CoreGame coreGame;
    private Computer computer;

    //Flags
    private boolean rotate = false;
    private boolean singleSelect = false;
    private boolean showPossibleMoves = false;
    private boolean gameStart = false;
    private int gameMode = 0;

    ArrayList<Position> possibleMoves = new ArrayList<Position>();
    private Position mousePosition;
    ArrayList<Position> move = new ArrayList<Position>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        coreGame = new CoreGame();
        computer = new Computer(true);
        drawFigures();
    }

    @FXML
    private void playerInputEvent(MouseEvent event) {
        if(gameStart){
            //set first select
            if(move.size() == 0 || !singleSelect && coreGame.getCurrentBoard().getFigure(mousePosition).isBlackTeam() == coreGame.getCurrentPlayer() && !(coreGame.getCurrentBoard().getFigure(mousePosition) instanceof None)){
                move.clear();
                mouseMarker.getGraphicsContext2D().clearRect(0, 0, mouseMarker.getWidth(), mouseMarker.getHeight());
                if(coreGame.getCurrentBoard().getFigure(mousePosition).isBlackTeam() == coreGame.getCurrentPlayer() && !(coreGame.getCurrentBoard().getFigure(mousePosition) instanceof None)){
                    move.add(mousePosition);
                    mouseMarker.getGraphicsContext2D().setStroke(Color.BLUE);
                    mouseMarker.getGraphicsContext2D().strokeRect(getRotatePosition(mousePosition.getPosX()*64), getRotatePosition(mousePosition.getPosY()*64), 64, 64);

                    //Draw Moves
                    if(showPossibleMoves) drawPossibleMoves();
                }
            }else if(move.size() == 1){
                if(coreGame.chessMove(new Move(move.get(0), mousePosition))){
                    drawFigures();
                    move.clear();
                    mouseMarker.getGraphicsContext2D().clearRect(0, 0, mouseMarker.getWidth(), mouseMarker.getHeight());


                }else return;

                drawFigures();

                //Check Computer play
                if(gameMode == 2){
                    coreGame.chessMove(computer.makeMove(coreGame.getCurrentBoard()));
                    drawFigures();
                }
            }
        }
    }

    @FXML
    private void getSelectedField(MouseEvent event) {
        mousePosition = new Position(getRotatePosition((int)(event.getX()/64)), getRotatePosition(((int)event.getY()/64)));
        chessBoard.getGraphicsContext2D().setStroke(Color.BLACK);
        chessBoard.getGraphicsContext2D().clearRect(0, 0, chessBoard.getWidth(), chessBoard.getHeight());
        chessBoard.getGraphicsContext2D().strokeRect(getRotatePosition(mousePosition.getPosX())*64, getRotatePosition(mousePosition.getPosY())*64, 64, 64);
    }



    /*
    <---Draw-functions------------------------------------------------------------------------------------------------->
     */

    public void drawFigures(){
        markMove.getGraphicsContext2D().clearRect(0, 0, markMove.getWidth(), markMove.getHeight());
        figureCanvas.getGraphicsContext2D().clearRect(0, 0, figureCanvas.getWidth(), figureCanvas.getHeight());
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                figureCanvas.getGraphicsContext2D().drawImage(ImageHandler.getInstance().getImage("FiguresTile"), (coreGame.getCurrentBoard().getFigure(getRotatePosition(x), getRotatePosition(y)).getFigureID()-1)*64, coreGame.getCurrentBoard().getFigure(getRotatePosition(x), getRotatePosition(y)).isBlackTeam() ? 64 : 0, 64, 64, x*64, y*64, 64, 64);
            }
        }
    }

    private void drawPossibleMoves(){
        //mark possible moves
        if(move.size() != 0){
            possibleMoves = Rules.possibleTargetFields(new Position(getRotatePosition(move.get(0).getPosX()), getRotatePosition(move.get(0).getPosY())), coreGame.getCurrentBoard());

            markMove.getGraphicsContext2D().clearRect(0, 0, markMove.getWidth(), markMove.getHeight());
            markMove.getGraphicsContext2D().setFill(Color.LIGHTBLUE);
            for(int i = 0; i < possibleMoves.size(); i++){
                markMove.getGraphicsContext2D().fillOval(possibleMoves.get(i).getPosX()*64+23, possibleMoves.get(i).getPosY()*64+23, 20, 20);
            }
        }
    }

    /*
    <---Button-events-------------------------------------------------------------------------------------------------->
     */

    @FXML
    private void toggleShowPossibleMoves(MouseEvent event) {
        showPossibleMoves = !showPossibleMoves;
        if(showPossibleMoves){
            drawPossibleMoves();
        }else{
            markMove.getGraphicsContext2D().clearRect(0, 0, markMove.getWidth(), markMove.getHeight());
        }
    }

    @FXML
    private void toggleSingleSelect(MouseEvent event) {
        singleSelect = !singleSelect;
    }

    @FXML
    private void rotateBoard(MouseEvent event) {
        rotate = !rotate;
        drawFigures();
        if(showPossibleMoves) drawPossibleMoves();
    }

    @FXML
    private void startNewGame(MouseEvent event){
        Parent root;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MenuFrame.fxml"));
            root = (Parent)fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Create new game");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            mainpanel.setDisable(true);

            MenuFrame menuController = fxmlLoader.getController();
            menuController.setControllerInterface(this);
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    <---Tools---------------------------------------------------------------------------------------------------------->
     */

    public Image getFigureImage(int figureID, boolean isBlack){
        ImageView figure = new ImageView(ImageHandler.getInstance().getImage("FiguresTile"));
        figure.setPreserveRatio(true);
        figure.setSmooth(true);
        figure.setViewport(new Rectangle2D(0, 0, 20, 20));
        return figure.getImage();
    }

    public int getRotatePosition(int pos){
        return (rotate ? 0 : 7)+pos*(rotate ? 1 : -1);
    }

    public Position getRotatePosition(Position pos){
        return new Position((rotate ? 0 : 7)+pos.getPosX()*(rotate ? 1 : -1), (rotate ? 0 : 7)+pos.getPosY()*(rotate ? 1 : -1)) ;
    }

    /*
    <---Getter-Setter---------------------------------------------------------------------------------------------------------->
     */

    public Pane getMainpanel(){
        return mainpanel;
    }

    public void resetCoreGame(){
        coreGame = new CoreGame();
        computer = new Computer(true);
    }

    public void setGameMode(int mode){
        this.gameMode = mode;
    }

    public void setGameStart(boolean gameStart){
        this.gameStart = gameStart;
    }



}





