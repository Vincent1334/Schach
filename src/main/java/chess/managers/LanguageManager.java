package chess.managers;

import chess.gui.Controller;
import chess.gui.MainMenu;
import chess.gui.NetworkMenu;
import chess.gui.Promotion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.util.*;

/**
 * This class contains the Language Manager for the program
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-25
 */
public class LanguageManager {

    private static int index = 0;
    private static List<Locale> locale = new ArrayList<>(Arrays.asList(new Locale("en", "US"),
                                                                             new Locale("de", "DE"),
                                                                             new Locale("fr", "FR")));

    private static ResourceBundle messages = ResourceBundle.getBundle("/languages/MessagesBundle", locale.get(index));
    private static ResourceBundle oldLanguage;
    private final static String LANGUAGE = "language";
    private final static String NETWORKSTAGE = "NetworkStage";

    /**
     * Change to the next Language file in the list
     */
    public static void nextLocale(){
        index ++;
        if(index == locale.size()){
            index = 0;
        }
        oldLanguage = messages;
        messages = ResourceBundle.getBundle("/languages/MessagesBundle", locale.get(index));

        updateLanguageGameStage();
        updateLanguageMenuStage();
        updateLanguageNetworkStage();
        updateLanguagePromotionStage();
    }

    /**
     * Returns ResourceBundle for FXML loader
     * @return messages
     */
    public static ResourceBundle getResourceBundle(){
        return messages;
    }

    /**
     * Return translated String
     * @param key String ID
     * @return title
     */
    public static String getText(String key){
        return messages.getString(key);
    }

    private static void updateLanguageMenuStage(){
        if(WindowManager.getStage("MenuStage") != null){
            Pane pane = ((MainMenu) WindowManager.getController("MenuStage")).pane;

            ((Label) pane.getChildren().get(3)).setText(getText("game_title"));
            ((RadioButton) pane.getChildren().get(4)).setText(getText("gamemode01"));
            ((RadioButton) pane.getChildren().get(5)).setText(getText("gamemode02"));
            ((Label) pane.getChildren().get(6)).setText(getText("team_label"));
            ((RadioButton) pane.getChildren().get(7)).setText(getText("black_label"));
            ((RadioButton) pane.getChildren().get(8)).setText(getText("white_label"));
            ((RadioButton) pane.getChildren().get(9)).setText(getText("gamemode03"));
            ((Button) pane.getChildren().get(10)).setText(getText("start_button"));
            ((Button) pane.getChildren().get(11)).setText(getText("quit_button"));
            ((Button) pane.getChildren().get(13)).setText(getText(LANGUAGE));

            WindowManager.getStage("MenuStage").setTitle(getText("menu_title"));
        }
    }

    private static void updateLanguageNetworkStage(){
        if(WindowManager.getStage(NETWORKSTAGE) != null){
            Pane menu = ((NetworkMenu) WindowManager.getController(NETWORKSTAGE)).menu;

            ((Button) menu.getChildren().get(12)).setText(LanguageManager.getText(LANGUAGE));
            ((Text) menu.getChildren().get(2)).setText(LanguageManager.getText("networkSettingsTitle"));
            ((RadioButton) menu.getChildren().get(13)).setText(LanguageManager.getText("newGame"));
            ((Text) menu.getChildren().get(7)).setText(LanguageManager.getText("yourColor"));
            ((RadioButton) menu.getChildren().get(14)).setText(LanguageManager.getText("joinGame"));
            ((Text) menu.getChildren().get(11)).setText(LanguageManager.getText("ip"));
            ((Text) menu.getChildren().get(4)).setText(LanguageManager.getText("port"));
            ((Button) menu.getChildren().get(6)).setText(LanguageManager.getText("menu_button"));
            ((Button) menu.getChildren().get(5)).setText(LanguageManager.getText("start_button"));
            ((Text) menu.getChildren().get(15)).setText(LanguageManager.getText("network_error"));

            WindowManager.getStage(NETWORKSTAGE).setTitle(getText("network_title"));
        }
    }

    private static void updateLanguagePromotionStage(){
        if(WindowManager.getStage("PromotionStage") != null){
            Pane promotion = ((Promotion) WindowManager.getController(NETWORKSTAGE)).promotionPane;

            ((Label) promotion.getChildren().get(1)).setText(LanguageManager.getText("promotion_label"));
            ((Button) promotion.getChildren().get(2)).setText(LanguageManager.getText("queen_label"));
            ((Button) promotion.getChildren().get(3)).setText(LanguageManager.getText("bishop_label"));
            ((Button) promotion.getChildren().get(4)).setText(LanguageManager.getText("rook_label"));
            ((Button) promotion.getChildren().get(5)).setText(LanguageManager.getText("knight_label"));
            ((Button) promotion.getChildren().get(6)).setText(LanguageManager.getText(LANGUAGE));

            WindowManager.getStage("PromotionStage").setTitle(getText("promotion_title"));
        }
    }

    private static void updateLanguageGameStage(){
        if(WindowManager.getStage("GameStage") != null){
            Pane menu = ((Controller) WindowManager.getController("GameStage")).menu;

            switchFlagLanguage(oldLanguage, "blackCheck_label", menu);
            switchFlagLanguage(oldLanguage, "whiteCheck_label", menu);
            switchFlagLanguage(oldLanguage, "blackCheckmate_label", menu);
            switchFlagLanguage(oldLanguage, "whiteCheckmate_label", menu);
            switchFlagLanguage(oldLanguage, "stalemate_label", menu);
            ((Button) menu.getChildren().get(10)).setText(LanguageManager.getText("menu_button"));
            ((Button) menu.getChildren().get(13)).setText(LanguageManager.getText(LANGUAGE));
            ((Label) menu.getChildren().get(2)).setText(LanguageManager.getText("history_label"));
            ((CheckBox) menu.getChildren().get(6)).setText(LanguageManager.getText("touch_move_button"));
            ((CheckBox) menu.getChildren().get(7)).setText(LanguageManager.getText("possible_moves_button"));
            ((CheckBox) menu.getChildren().get(8)).setText(LanguageManager.getText("rotate_button"));
            ((CheckBox) menu.getChildren().get(9)).setText(LanguageManager.getText("flag_button"));

            WindowManager.getStage("GameStage").setTitle(getText("game_title"));
        }
    }

    private static void switchFlagLanguage(ResourceBundle oldLanguage, String label, Pane menu) {
        if (((Label) menu.getChildren().get(4)).getText().equals(oldLanguage.getString(label))) {
            ((Label) menu.getChildren().get(4)).setText(LanguageManager.getText(label));
        }
    }
}