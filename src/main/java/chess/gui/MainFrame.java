package chess.gui;

import chess.controller.CoreGame;
import chess.model.Move;
import chess.model.Position;
import chess.util.Observer;
import javafx.beans.NamedArg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

    private Position mousePosition;

    @FXML
    private Canvas chessBoard;
    @FXML
    private Canvas markMove;
    @FXML
    private Canvas figureCanvas;
    @FXML
    private Pane mainpanel;

    private CoreGame coreGame;

    //Flags
    private boolean rotate = false;
    private boolean singleSelect = false;
    private boolean showPossibleMoves = false;

    ArrayList<Move> possibleMoves = new ArrayList<Move>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        coreGame = new CoreGame();
        drawFigures();
    }

    @FXML
    private void getSelectedField(MouseEvent event) {
        mousePosition = new Position(((int)event.getX()/64), ((int)event.getY()/64));
        chessBoard.getGraphicsContext2D().setStroke(Color.BLACK);
        chessBoard.getGraphicsContext2D().clearRect(0, 0, chessBoard.getWidth(), chessBoard.getHeight());
        chessBoard.getGraphicsContext2D().strokeRect(mousePosition.getPosX()*64, mousePosition.getPosY()*64, 64, 64);
    }



    /*
    <---Draw-functions------------------------------------------------------------------------------------------------->
     */

    public void drawFigures(){
        figureCanvas.getGraphicsContext2D().clearRect(0, 0, figureCanvas.getWidth(), figureCanvas.getHeight());
        for(int y = 0; y < 8; y++){
            for(int x = 0; x < 8; x++){
                figureCanvas.getGraphicsContext2D().drawImage(ImageHandler.getInstance().getImage("FiguresTile"), (coreGame.getCurrentBoard().getFigure(x, (rotate ? 7 : 0)+y*(rotate ? -1 : 1)).getFigureID()-1)*64, coreGame.getCurrentBoard().getFigure(x, (rotate ? 7 : 0)+y*(rotate ? -1 : 1)).isBlackTeam() ? 64 : 0, 64, 64, x*64, y*64, 64, 64);
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

    /*
    <---Button-events-------------------------------------------------------------------------------------------------->
     */

    @FXML
    private void toggleShowPossibleMoves(MouseEvent event) {
        showPossibleMoves = !showPossibleMoves;
    }

    @FXML
    private void toggleSingleSelect(MouseEvent event) {
        singleSelect = !singleSelect;
    }

    @FXML
    private void rotateBoard(MouseEvent event) {
        rotate = !rotate;
        drawFigures();
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

    /*
    <---Getter-Setter---------------------------------------------------------------------------------------------------------->
     */

    public Pane getMainpanel(){
        return mainpanel;
    }

    public void resetCoreGame(){
        coreGame = new CoreGame();
    }



}





