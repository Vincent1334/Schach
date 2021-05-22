package chess.gui;

import chess.controller.CoreGame;
import chess.figures.Figure;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Starting point of the JavaFX GUI
 */
public class Gui extends Application {
    private static CoreGame coreGame;
    private SampleController controller;

    /**
     * This method is called by the Application to start the GUI.
     *
     * @param primaryStage The initial root stage of the application.
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(event -> System.out.println("Hello World!"));

        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }

    /**
     * The entry point of the GUI application.
     *
     * @param args The command line arguments passed to the application
     */
    public static void main(String[] args) {
        launch(args);
    }

    public void enterGame() {

        coreGame = new CoreGame();

        while (!coreGame.isGameOver()) {
            MouseEvent event = null;
            if (controller.handleFieldClick(event) != null) {
                coreGame.chessMove(controller.handleFieldClick(event));
            }
            updateScene();
        }
    }

    private void updateScene() {
        //drawBoard();
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                drawFigure(x, y);
            }
        }
    }

    private void drawFigure(int x, int y) {
        //Image image = getImage(coreGame.getCurrentBoard().getFigure(x,y).getSymbol);
        //scene.grid(x+1,y+1).setImage(image);
    }

    /*
    private Image getImage(char symbol){
    switch(symbol){
        case p:
            return ImageOfWhitePawn;
            ...
        }
    }
    */
}
