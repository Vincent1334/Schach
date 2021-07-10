package chess.gui;

import chess.enums.GameMode;
import chess.controller.*;
import chess.ai.Computer;
import chess.managers.LanguageManager;
import chess.managers.WindowManager;
import chess.network.NetworkPlayer;
import chess.figures.Pawn;
import chess.model.*;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

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
    private boolean playerBlack;

    /**
     * initializes gameMode, coreGame, computer, beatenFigureList and conversion
     *
     * @param gameMode         against a local friend (0) or a network game (1) or against the computer (2)
     * @param playerColorBlack the color you want to play
     * @param networkPlayer    the networkPlayer
     */
    public Logic(GameMode gameMode, boolean playerColorBlack, NetworkPlayer networkPlayer) {
        this.gameMode = gameMode;
        coreGame = new CoreGame();
        this.controller = ((Controller)WindowManager.getController("GameStage"));

        if (gameMode == GameMode.COMPUTER) {
            computer = new Computer(!playerColorBlack, this);
            controller.getRotate().setDisable(true);
            if (playerColorBlack) {
                computerMove();
            }
        }
        if (gameMode == GameMode.NETWORK) {
            controller.setCalculating(true, LanguageManager.getText("network_waiting_label"));
            this.network = networkPlayer;
            this.network.initNetworkPlayer(this);
            controller.getRotate().setDisable(true);
        }
    }

    /**
     * performs a move if the target position is clicked after the start position was clicked and if the move is allowed
     *
     * @param clickedField the clicked field
     * @param blacksTurn   is black on turn?
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
     */
    private void move(Rectangle startField, Rectangle targetField) {
        Position startPosition = new Position(GridPane.getColumnIndex(startField) - 1, 8 - GridPane.getRowIndex(startField));
        Position targetPosition = new Position(GridPane.getColumnIndex(targetField) - 1, 8 - GridPane.getRowIndex(targetField));

        // Pawn conversion popup
        if (coreGame.getCurrentBoard().getFigure(startPosition) instanceof Pawn &&
                (targetPosition.getPosY() == 0 || targetPosition.getPosY() == 7) &&
                Rules.possibleTargetFields(startPosition, coreGame.getCurrentBoard()).contains(targetPosition)) {

            controller.getBoard().setMouseTransparent(true);

            WindowManager.initialWindow("PromotionStage", "promotion_title");
            ((Promotion) WindowManager.getController("PromotionStage")).init(startPosition, targetPosition, this);
            WindowManager.showStage("PromotionStage");
        } else {
            performMove(new Move(startPosition, targetPosition));
        }
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
            if (!controller.getUndoRedo().getUNDO_REDO_MOVES_AS_TEXT().isEmpty()) {
                controller.getUndoRedo().resetUndoRedo(controller.getHistory(), this);
            }
            controller.updateHistory(move);
            controller.updateScene();
            startField = null;
            if (gameMode == GameMode.COMPUTER) {
                computerMove();
            }
            if (gameMode == GameMode.NETWORK) {
                network.sendMove(move);
                controller.setCalculating(true, LanguageManager.getText("network_player_waiting_label"));
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
    public void computerMove() {
        computer.makeMove(coreGame.getCurrentBoard());
        controller.setCalculating(true, LanguageManager.getText("calculating_label"));
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
    public void killNetworkPlayer() {
        if (network != null) {
            network.sendExit();
            network.killNetwork();
        }
    }

    /**
     * executes the computer or network move
     */
    @Override
    public void run() {
        if (gameMode == GameMode.COMPUTER) {
            controller.setCalculating(false, LanguageManager.getText("calculating_label"));
            Move computerMove = computer.getMove();
            System.out.println("Computer: " + computerMove.toString());
            coreGame.chessMove(computerMove);
            controller.updateHistory(computerMove);
            controller.updateScene();
        }
        if (gameMode == GameMode.NETWORK) {
            if (network.isReadyToPlay()) {
                //Normal move
                if(network.getOutput() instanceof Move){
                    Move networkMove = (Move) network.getOutput();
                    coreGame.chessMove(networkMove);
                    controller.setCalculating(false, "");
                    controller.updateHistory(networkMove);
                    controller.updateScene();
                    return;
                }
                //Undo Redo
                if(network.getOutput() instanceof Integer){
                    this.getController().undoRedoSend((Integer) network.getOutput());
                    controller.updateScene();
                    return;
                }
                //Exit
                if(network.getOutput() instanceof String){
                    controller.setCalculating(true, LanguageManager.getText("network_exit"));
                    return;
                }
            } else {
                playerBlack = network.team();
                network.setReadyToPlay(true);
                if (playerBlack) {
                    controller.setCalculating(true, LanguageManager.getText("network_player_waiting_label"));
                    controller.getBoard().setRotate(180);
                    turnFigures(180);
                } else {
                    controller.setCalculating(false, "");
                }
            }
        }
    }

    /**
     * turns the chessboard so that the figures of the actualPlayer are always on the bottom
     *
     * @param reset reset the boardTurning
     */
    public void turnBoard(boolean reset) {
        if (reset) {
            controller.getBoard().setRotate(0);
            turnFigures(0);
        } else {
            if (coreGame.isActivePlayer()) {
                controller.getBoard().setRotate(180);
                turnFigures(180);
            } else {
                controller.getBoard().setRotate(0);
                turnFigures(0);
            }
        }
    }

    /**
     * rotates the figures themselves
     *
     * @param angle the angle around which the figures are rotated
     */
    public void turnFigures(int angle) {
        for (Node node : controller.getBoard().getChildren()) {
            node.setRotate(angle);
        }
    }

    // -------------getter---------------------------------------------------------------------------------------------------------------

    public CoreGame getCoreGame() {
        return coreGame;
    }

    public Rectangle getStartField() {
        return startField;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public boolean isPlayerBlack() {
        return playerBlack;
    }

    public Controller getController(){return controller;}

    public NetworkPlayer getNetwork(){return network;}
}
