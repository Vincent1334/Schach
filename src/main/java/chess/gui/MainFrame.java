package chess.gui;

import chess.controller.CoreGame;
import chess.figures.None;
import chess.ki.Computer;
import chess.model.Move;
import chess.model.Position;
import chess.model.Rules;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainFrame implements Initializable {

    @FXML
    private Canvas boardCanvas;
    @FXML
    private Pane mainpanel;

    private Image figures;

    private CoreGame coreGame;
    private Computer computer;

    //Flags
    private boolean rotate = false;
    private boolean singleSelect = false;
    private boolean showPossibleMoves = false;
    private boolean gameStart = false;
    private int gameMode = 0;

    private ArrayList<Position> possibleMoves = new ArrayList<Position>();
    private ArrayList<Position> move = new ArrayList<Position>();
    private Position mousePosition;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        coreGame = new CoreGame();
        computer = new Computer(true);

        figures = ImageHandler.getInstance().getImage("FiguresTile");
    }

    @FXML
    private void playerInputEvent(MouseEvent event) {
        if(gameStart){
            //set first select
            if(coreGame.getCurrentBoard().getFigure(mousePosition).isBlackTeam() == coreGame.getCurrentPlayer() && !(coreGame.getCurrentBoard().getFigure(mousePosition) instanceof None)){
                if(move.size() == 0 || !singleSelect){

                    move.clear();
                    move.add(mousePosition);

                    renderBoard();

                //set second select
                }
            }else if(move.size() == 1){
                if(coreGame.chessMove(new Move(move.get(0), mousePosition))){
                    renderBoard();
                    move.clear();
                }else return;

                renderBoard();

                //Check Computer play
                if(gameMode == 2){
                    coreGame.chessMove(computer.makeMove(coreGame.getCurrentBoard()));
                    renderBoard();
                }
            }
        }
    }

    @FXML
    private void getSelectedField(MouseEvent event) {
        mousePosition = new Position(getRotatePosition((int)(event.getX()/64)), getRotatePosition(((int)event.getY()/64)));
        renderBoard();
    }

    /*
    <---Draw-functions------------------------------------------------------------------------------------------------->
     */

    public void renderBoard(){
        GraphicsContext g = boardCanvas.getGraphicsContext2D();

        //clear Board
        g.clearRect(0, 0 , boardCanvas.getWidth(), boardCanvas.getHeight());

        //draw possible Moves
        if(move.size() != 0 && showPossibleMoves){
            possibleMoves.clear();
            possibleMoves = Rules.possibleTargetFields(new Position(move.get(0).getPosX(), move.get(0).getPosY()), coreGame.getCurrentBoard());
            g.setFill(Color.LIGHTBLUE);
            for(int i = 0; i < possibleMoves.size(); i++){
                g.fillOval(getRotatePosition(possibleMoves.get(i).getPosX())*64+23, getRotatePosition(possibleMoves.get(i).getPosY())*64+23, 20, 20);
            }
        }

        //draw Field select
        g.setStroke(Color.BLACK);
        g.strokeRect(getRotatePosition(mousePosition.getPosX())*64, getRotatePosition(mousePosition.getPosY())*64, 64, 64);

        //draw select Rectangle
        g.setStroke(Color.BLUE);
        g.strokeRect(getRotatePosition(mousePosition.getPosX()*64), getRotatePosition(mousePosition.getPosY()*64), 64, 64);

        //draw Figures
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                g.drawImage(figures, (coreGame.getCurrentBoard().getFigure(getRotatePosition(x), getRotatePosition(y)).getFigureID()-1)*64, coreGame.getCurrentBoard().getFigure(getRotatePosition(x), getRotatePosition(y)).isBlackTeam() ? 64 : 0, 64, 64, x*64, y*64, 64, 64);
            }
        }
    }

    /*
    <---Button-events-------------------------------------------------------------------------------------------------->
     */

    @FXML
    private void toggleShowPossibleMoves(MouseEvent event) {
        showPossibleMoves = !showPossibleMoves;
        renderBoard();
    }

    @FXML
    private void toggleSingleSelect(MouseEvent event) {
        singleSelect = !singleSelect;
    }

    @FXML
    private void rotateBoard(MouseEvent event) {
        rotate = !rotate;
        renderBoard();
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

        possibleMoves.clear();
        move.clear();
    }

    public void setGameMode(int mode){
        this.gameMode = mode;
    }

    public void setGameStart(boolean gameStart){
        this.gameStart = gameStart;
    }
}





