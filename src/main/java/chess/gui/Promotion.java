package chess.gui;

import chess.model.Move;
import chess.model.Position;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.Locale;
import java.util.ResourceBundle;

public class Promotion {
    private static ResourceBundle messages = ResourceBundle.getBundle("/languages/MessagesBundle", Gui.locale);
    private Logic logic;
    private Position startPosition;
    private Position targetPosition;
    @FXML
    Pane promotion;

    protected void init(Position startPosition, Position targetPosition,Logic logic){
        this.startPosition = startPosition;
        this.targetPosition = targetPosition;
        this.logic = logic;

    }


    @FXML
    private void getPromotionFigure(MouseEvent event) {
        int id = 0;
        String item = ((Button) event.getSource()).getText();
        if (item.equals(messages.getString("queen_label"))) {
            id = 5;
        }
        if (item.equals(messages.getString("bishop_label"))) {
            id = 4;
        }
        if (item.equals(messages.getString("rook_label"))) {
            id = 2;
        }
        if (item.equals(messages.getString("knight_label"))) {
            id = 3;
        }
        ((Node) (event.getSource())).getScene().getWindow().hide();
        logic.performMove(new Move(startPosition, targetPosition, id));
    }

    @FXML
    private void setLanguage(){
        if (messages.getLocale().getCountry().equals("US")) {
            messages = ResourceBundle.getBundle("/languages/MessagesBundle", new Locale("de", "GE"));
        } else if (messages.getLocale().getCountry().equals("DE")) {
            messages = ResourceBundle.getBundle("/languages/MessagesBundle", new Locale("fr", "FR"));
        } else {
            messages = ResourceBundle.getBundle("/languages/MessagesBundle", new Locale("en", "US"));
        }
        getLabelPromotion().setText(messages.getString("promotion_label"));
        getButtonQueen().setText(messages.getString("queen_label"));
        getButtonBishop().setText(messages.getString("bishop_label"));
        getButtonRook().setText(messages.getString("rook_label"));
        getButtonKnight().setText(messages.getString("knight_label"));
        getLanguage().setText(messages.getString("language"));
    }



    private Label getLabelPromotion(){
        return (Label) promotion.getChildren().get(1);
    }
    private Button getButtonQueen(){
        return (Button) promotion.getChildren().get(2);
    }
    private Button getButtonBishop(){
        return (Button) promotion.getChildren().get(3);
    }
    private Button getButtonRook(){
        return (Button) promotion.getChildren().get(4);
    }
    private Button getButtonKnight(){
        return (Button) promotion.getChildren().get(5);
    }
    private Button getLanguage(){
        return (Button) promotion.getChildren().get(6);
    }
}
