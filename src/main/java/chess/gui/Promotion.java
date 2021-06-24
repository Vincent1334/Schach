package chess.gui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class Promotion {
    private int promotionID = 0;
    private static ResourceBundle messages = ResourceBundle.getBundle("/languages/MessagesBundle", Gui.locale);
    @FXML
    Pane promotion;


    @FXML
    private void getPromotionFigure(MouseEvent event) {
        String item = ((Button) event.getSource()).getText();
        if (item.equals(messages.getString("queen_label"))) {
            promotionID = 5;
        }
        if (item.equals(messages.getString("bishop_label"))) {
            promotionID = 4;
        }
        if (item.equals(messages.getString("rook_label"))) {
            promotionID = 2;
        }
        if (item.equals(messages.getString("knight_label"))) {
            promotionID = 3;
        }
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    public int getPromotionID() {
        return promotionID;
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
