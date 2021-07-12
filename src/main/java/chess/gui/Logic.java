package chess.gui;

import chess.ai.Computer;
import chess.controller.CoreGame;
import chess.enums.GameMode;
import chess.enums.NetworkFlags;
import chess.figures.Pawn;
import chess.managers.LanguageManager;
import chess.managers.WindowManager;
import chess.model.Move;
import chess.model.Position;
import chess.model.Rules;
import chess.network.NetworkPlayer;
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
    private final Controller CONTROLLER;
    private final GameMode GAME_MODE;
    private NetworkPlayer network;
    private boolean playerBlack;
    private final String WAITING_NETWORK = "network_player_waiting_label";


    /**
     * initializes gameMode, coreGame, computer, beatenFigureList and conversion
     *
     * @param GAME_MODE        against a local friend (NORMAL) or a network game (NETWORK) or against the computer (COMPUTER)
     * @param playerColorBlack the color you want to play (true if you want to play black)
     * @param networkPlayer    the networkPlayer
     */
    public Logic(GameMode GAME_MODE, boolean playerColorBlack, NetworkPlayer networkPlayer) {
        this.GAME_MODE = GAME_MODE;
        coreGame = new CoreGame();
        this.CONTROLLER = (Controller) WindowManager.getController("GameStage");

        if (GAME_MODE == GameMode.COMPUTER) {
            computer = new Computer(!playerColorBlack, this);
            CONTROLLER.getRotate().setDisable(true);
            if (playerColorBlack) {
                computerMove();
            }
        }
        if (GAME_MODE == GameMode.NETWORK) {
            setNotification(true, LanguageManager.getText("network_waiting_label"));
            this.network = networkPlayer;
            this.network.initNetworkPlayer(this);
            CONTROLLER.getRotate().setDisable(true);
        }
    }

    /**
     * performs a move if the target position is clicked after the start position was clicked and if the move is allowed
     *
     * @param clickedField the clicked field
     * @param blacksTurn   is black on turn?
     */
    public void handleFieldClick(Rectangle clickedField, boolean blacksTurn) {
        if (startField == null && CONTROLLER.getFigure(clickedField) != null && CONTROLLER.isImageBlack(CONTROLLER.getFigure(clickedField)) == blacksTurn) {
            startField = clickedField;
            CONTROLLER.setMark(startField, true, coreGame.getCurrentBoard());
        } else if (startField != null && CONTROLLER.getFigure(startField) != null) {
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
                (targetPosition.getPOS_Y() == 0 || targetPosition.getPOS_Y() == 7) &&
                Rules.possibleTargetFields(startPosition, coreGame.getCurrentBoard()).contains(targetPosition)) {

            CONTROLLER.getBoard().setMouseTransparent(true);

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
        CONTROLLER.getBoard().setMouseTransparent(false);
        CONTROLLER.setMark(startField, false, coreGame.getCurrentBoard());
        if (coreGame.chessMove(move)) {
            if (!CONTROLLER.getUndoRedo().getUNDO_REDO_MOVES_AS_TEXT().isEmpty()) {
                CONTROLLER.getUndoRedo().resetUndoRedo(CONTROLLER.getHistory(), this);
            }
            CONTROLLER.getScene().updateHistory(move);
            CONTROLLER.getScene().updateScene();
            startField = null;
            if (GAME_MODE == GameMode.COMPUTER) {
                computerMove();
            }
            if (GAME_MODE == GameMode.NETWORK) {
                network.sendMove(move);
                setNotification(true, LanguageManager.getText(WAITING_NETWORK));
            }
        } else if (CONTROLLER.getTouchMove().isSelected() && !CONTROLLER.getPossibleFields(startField, coreGame.getCurrentBoard()).isEmpty()) {
            CONTROLLER.setMark(startField, true, coreGame.getCurrentBoard());
        } else {
            startField = null;
        }
    }

    /**
     * starts a computer move
     */
    public void computerMove() {
        computer.makeMove(coreGame.getCurrentBoard());
        setNotification(true, LanguageManager.getText("calculating_label"));
    }

    /**
     * Turns the task into thread if the computer or network thread is terminated
     */
    public void computerOrNetworkIsFinish() {
        Platform.runLater(this);
    }

    /**
     * Turns the task into thread if the networkPlayer thread is terminated
     */
    public void killNetworkPlayer() {
        if (network != null) {
            network.killNetwork();
        }
    }

    /**
     * executes the computer or network move
     */
    @Override
    public void run() {
        if (GAME_MODE == GameMode.COMPUTER) {
            setNotification(false, LanguageManager.getText("calculating_label"));
            Move computerMove = computer.getMove();
            System.out.println("Computer: " + computerMove.toString());
            coreGame.chessMove(computerMove);
            CONTROLLER.getScene().updateHistory(computerMove);
            CONTROLLER.getScene().updateScene();
        }
        if (GAME_MODE == GameMode.NETWORK) {
            if (network.getFlag() == NetworkFlags.Connecting) {
                setNotification(true, LanguageManager.getText("network_waiting_label"));
            } else if (network.getFlag() == NetworkFlags.SetupTeams) {
                networkSetupTeams();
            } else if (network.getFlag() == NetworkFlags.Move) {
                networkPerformMove();
            } else if (network.getFlag() == NetworkFlags.UndoRedo) {
                networkUndoRedo();
            } else if (network.getFlag() == NetworkFlags.Exit) {
                exit();
            }
        }
    }

    /**
     * setups the teams in a networkgame
     */
    private void networkSetupTeams() {
        playerBlack = network.isNetworkPlayerBlack();
        if (network.isNetworkPlayerBlack()) {
            setNotification(true, LanguageManager.getText(WAITING_NETWORK));
            CONTROLLER.getBoard().setRotate(180);
            turnFigures(180);
        } else {
            setNotification(false, "");
        }
        network.setFlag(NetworkFlags.InGame);
    }

    /**
     * performs a network move
     */
    private void networkPerformMove() {
        Move networkMove = (Move) network.getNetworkOutput();
        coreGame.chessMove(networkMove);
        setNotification(false, "");
        CONTROLLER.getUndoRedo().resetUndoRedo(CONTROLLER.getHistory(), this);
        CONTROLLER.getScene().updateHistory(networkMove);
        CONTROLLER.getScene().updateScene();
        network.setFlag(NetworkFlags.InGame);
    }

    /**
     * performs undo redo
     */
    private void networkUndoRedo() {
        CONTROLLER.getUndoRedo().undoRedoClicked(CONTROLLER.getHistory(), this, (Integer) network.getNetworkOutput());
        if ((Integer) network.getNetworkOutput() == -1) CONTROLLER.getUndoRedo().undo(CONTROLLER.getHistory(), this);

        if (coreGame.isActivePlayerBlack() && network.isNetworkPlayerBlack()) {
            setNotification(false, "");
        } else {
            setNotification(true, LanguageManager.getText(WAITING_NETWORK));
        }
        CONTROLLER.getScene().updateScene();
        network.setFlag(NetworkFlags.InGame);
    }

    /**
     * disables board and undo redo and sets notification
     */
    private void exit() {
        CONTROLLER.getBoard().setMouseTransparent(true);
        CONTROLLER.getHistory().setMouseTransparent(true);
        CONTROLLER.getButtonRedo().setMouseTransparent(true);
        CONTROLLER.getButtonUndo().setMouseTransparent(true);
        setNotification(true, LanguageManager.getText("network_exit"));
    }

    /**
     * disables undo redo function
     */
    public void disableUndoRedo() {
        CONTROLLER.getHistory().setMouseTransparent(true);
        CONTROLLER.getButtonRedo().setOpacity(0);
        CONTROLLER.getButtonRedo().setDisable(true);
        CONTROLLER.getButtonUndo().setOpacity(0);
        CONTROLLER.getButtonUndo().setDisable(true);
    }

    /**
     * turns the chessboard so that the figures of the actualPlayer are always on the bottom
     *
     * @param reset reset the boardTurning
     */
    public void turnBoard(boolean reset) {
        if (reset) {
            CONTROLLER.getBoard().setRotate(0);
            turnFigures(0);
        } else {
            if (coreGame.isActivePlayerBlack()) {
                CONTROLLER.getBoard().setRotate(180);
                turnFigures(180);
            } else {
                CONTROLLER.getBoard().setRotate(0);
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
        for (Node node : CONTROLLER.getBoard().getChildren()) {
            node.setRotate(angle);
        }
    }

    // -------------setter---------------------------------------------------------------------------------------------------------------

    /**
     * sets the notification message and ist visibility
     *
     * @param notificationVisible whether the notification is visible
     * @param message             the notification message
     */
    public void setNotification(boolean notificationVisible, String message) {
        if (notificationVisible) {
            CONTROLLER.getLabelCalculating().setText(message);
            CONTROLLER.getLabelCalculating().setVisible(true);
            CONTROLLER.getBoard().setMouseTransparent(true);
        } else {
            CONTROLLER.getLabelCalculating().setVisible(false);
            CONTROLLER.getBoard().setMouseTransparent(false);
        }
    }

    // -------------getter---------------------------------------------------------------------------------------------------------------

    /**
     * returns the coreGame
     *
     * @return coreGame
     */
    public CoreGame getCoreGame() {
        return coreGame;
    }


    /**
     * returns the startField
     *
     * @return startField
     */
    public Rectangle getStartField() {
        return startField;
    }

    /**
     * returns the GAME_MODE
     *
     * @return GAME_MODE
     */
    public GameMode getGameMode() {
        return GAME_MODE;
    }


    /**
     * returns true when we play black. For network use only
     *
     * @return playerBlack
     */
    public boolean isPlayerBlack() {
        return playerBlack;
    }

    /**
     * returns the CONTROLLER
     *
     * @return CONTROLLER
     */
    public Controller getController() {
        return CONTROLLER;
    }


    /**
     * returns the network
     *
     * @return network
     */
    public NetworkPlayer getNetwork() {
        return network;
    }

    public Computer getComputer() {
        return computer;
    }
}
