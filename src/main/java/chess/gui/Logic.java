package chess.gui;

import chess.GameMode;
import chess.controller.*;
import chess.ai.Computer;
import chess.network.NetworkPlayer;
import chess.figures.Pawn;
import chess.model.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * transposes the logic of a move
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-09
 */
public class Logic implements Runnable {

    private static CoreGame coreGame;
    private Computer computer;
    private Rectangle startField;
    private Controller controller;
    private GameMode gameMode;
    private NetworkPlayer network;
    private boolean isBlack;

    /**
     * initializes gameMode, coreGame, computer, beatenFigureList and conversion
     *
     * @param gameMode         against a local friend (0) or a network game (1) or against the computer (2)
     * @param playerColorBlack the color you want to play
     * @param controller the controller
     */
    public Logic(GameMode gameMode,boolean playerColorBlack,Controller controller, NetworkPlayer networkPlayer) {
        this.gameMode = gameMode;
        coreGame = new CoreGame();
        this.controller = controller;

        if (gameMode == GameMode.COMPUTER) {
            computer = new Computer(!playerColorBlack, this);
            controller.getRotate().setDisable(true);
            if (playerColorBlack) {
                computerMove();
            }
        }
        if (gameMode == GameMode.NETWORK) {
            controller.setCalculating(true, Gui.messages.getString("network_waiting_label"));
            this.network = networkPlayer;
            this.network.initNetworkPlayer(this);
            controller.getRotate().setDisable(true);
        }
    }

    /**
     * performs a move if the target position is clicked after the start position was clicked and if the move is allowed
     *
     * @param clickedField the clicked field
     * @param blacksTurn is black on turn?
     */
    public void handleFieldClick(Rectangle clickedField, boolean blacksTurn) {
        if (startField == null && controller.getFigure(clickedField) != null && controller.isImageBlack(controller.getFigure(clickedField)) == blacksTurn) {
            startField = clickedField;
            controller.setMark(startField, true, coreGame.getCurrentBoard());
        } else if (startField != null && controller.getFigure(startField) != null) {
            move(startField, clickedField);
        }
    }

    /**
     * Returns the move from the startField to the targetField
     *
     * @param startField  the start field of the move
     * @param targetField the target field of the move
     * @return move from the startField to the targetField
     */
    private void move(Rectangle startField, Rectangle targetField) {
        Position startPosition = new Position(GridPane.getColumnIndex(startField) - 1, 8 - GridPane.getRowIndex(startField));
        Position targetPosition = new Position(GridPane.getColumnIndex(targetField) - 1, 8 - GridPane.getRowIndex(targetField));

        if(coreGame.getCurrentBoard().getFigure(startPosition) instanceof Pawn && (targetPosition.getPosY() == 0 || targetPosition.getPosY() == 7)&& Rules.possibleTargetFields(startPosition,coreGame.getCurrentBoard()).contains(targetPosition)) {
            controller.getBoard().setMouseTransparent(true);
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Promotion.fxml"));
                fxmlLoader.setResources(ResourceBundle.getBundle("/languages/MessagesBundle", Gui.messages.getLocale()));
                Parent root = fxmlLoader.load();
                Promotion promotion = fxmlLoader.getController();
                promotion.init(startPosition,targetPosition,this);
                Stage stage = new Stage();
                stage.setTitle("Promotion");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else  performMove(new Move(startPosition, targetPosition));
    }

    /**
     * performs the move if possible and updates scene
     *
     * @param move the move which should be performed
     */
    protected void performMove(Move move) {
        controller.getBoard().setMouseTransparent(false);
        controller.setMark(startField, false, coreGame.getCurrentBoard());
        if (coreGame.chessMove(move)) {
            controller.updateScene(move, coreGame);
            startField = null;
            if (gameMode == GameMode.COMPUTER) {
                computerMove();
            }
            if (gameMode == GameMode.NETWORK) {
                network.sendMove(move);
                controller.setCalculating(true, Gui.messages.getString("network_player_waitning_label"));
            }
        } else if (controller.getTouchMove().isSelected() && !controller.getPossibleFields(startField, coreGame.getCurrentBoard()).isEmpty()) {
            controller.setMark(startField, true, coreGame.getCurrentBoard());
        } else {
            startField = null;
        }
    }

    /**
     * starts a computer move
     */
    private void computerMove() {
        computer.makeMove(coreGame.getCurrentBoard());
        controller.setCalculating(true, Gui.messages.getString("calculating_label"));
    }


    /**
     * return core game
     * @return CoreGame
     */
    public CoreGame getCoreGame(){
        return coreGame;
    }

    /**
     * Returns the start field
     * @return Rectangle startField
     */
    public Rectangle getStartField(){
        return startField;
    }

    /**
     * Turns the task into thread if the computer thread is terminated
     */
    public void computerOrNetworkIsFinish() {
        Platform.runLater(this);
    }

    /**
     * Turns the task into thread if the networkPlayer thread is terminated
     */
    public void killNetworkPlayer(){
        if(network != null) network.killNetwork();
    }

    /**
     * executes the computer move
     */
    @Override
    public void run() {
        if(gameMode == GameMode.COMPUTER){
            controller.setCalculating(false, Gui.messages.getString("calculating_label"));
            Move computerMove = computer.getMove();
            coreGame.chessMove(computerMove);
            controller.updateScene(computerMove, coreGame);
        }
        if(gameMode == GameMode.NETWORK){
            if(network.isReadyToPlay()){
                Move networkMove = network.getMove();
                coreGame.chessMove(networkMove);
                controller.updateScene(networkMove, coreGame);
            }else{
                isBlack = network.isBlack();
                network.setReadyToPlay(true);
                if(!isBlack) controller.setCalculating(false, "");
                else controller.turnBoard(false);
            }
        }
    }
}
