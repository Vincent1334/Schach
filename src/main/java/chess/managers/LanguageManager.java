package chess.managers;

import chess.gui.ChessBoard;
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
 * This class contains the Language Manager for the gui and cli
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-25
 */
public class LanguageManager {

    private static int index = 0;
    private static final List<Locale> LOCALE = new ArrayList<>(Arrays.asList(new Locale("en", "US"),
            new Locale("de", "DE"),
            new Locale("fr", "FR")));

    private static ResourceBundle messages = ResourceBundle.getBundle("/languages/MessagesBundle", LOCALE.get(index));
    private static ResourceBundle oldLanguage;
    private final static String NETWORKSTAGE = "NetworkStage";

    /**
     * Change the language file to the given language
     *
     * @param language the language you want
     */
    public static void setLanguage(String language) {

        switch (language) {
            case "en":
                index = 0;
                break;
            case "de":
                index = 1;
                break;
            case "fr":
                index = 2;
                break;
        }

        oldLanguage = messages;
        messages = ResourceBundle.getBundle("/languages/MessagesBundle", LOCALE.get(index));

        updateLanguageGameStage();
        updateLanguageMenuStage();
        updateLanguageNetworkStage();
        updateLanguagePromotionStage();
    }

    /**
     * Returns ResourceBundle for FXML loader
     *
     * @return messages
     */
    public static ResourceBundle getResourceBundle() {
        return messages;
    }

    /**
     * Return translated String
     *
     * @param key String ID
     * @return title
     */
    public static String getText(String key) {
        return messages.getString(key);
    }

    /**
     * Update language in menu stage
     */
    private static void updateLanguageMenuStage() {
        if (WindowManager.getStage("MenuStage") != null) {
            Pane pane = ((MainMenu) WindowManager.getController("MenuStage")).pane;

            ((Label) pane.getChildren().get(3)).setText(getText("chess_label"));
            ((RadioButton) pane.getChildren().get(4)).setText(getText("gamemode01"));
            ((RadioButton) pane.getChildren().get(5)).setText(getText("gamemode02"));
            ((Label) pane.getChildren().get(6)).setText(getText("team_label"));
            ((RadioButton) pane.getChildren().get(7)).setText(getText("black_label"));
            ((RadioButton) pane.getChildren().get(8)).setText(getText("white_label"));
            ((RadioButton) pane.getChildren().get(9)).setText(getText("gamemode03"));
            ((Button) pane.getChildren().get(10)).setText(getText("start_button"));
            ((Button) pane.getChildren().get(11)).setText(getText("quit_button"));
            ((RadioButton) pane.getChildren().get(16)).setText(getText("easy_label"));
            ((RadioButton) pane.getChildren().get(17)).setText(getText("medium_label"));
            ((RadioButton) pane.getChildren().get(18)).setText(getText("hard_label"));
            ((Label) pane.getChildren().get(19)).setText(getText("difficulty_label"));

            WindowManager.getStage("MenuStage").setTitle(getText("menu_title"));
        }
    }

    /**
     * Update language in network stage
     */
    private static void updateLanguageNetworkStage() {
        if (WindowManager.getStage(NETWORKSTAGE) != null) {
            Pane menu = ((NetworkMenu) WindowManager.getController(NETWORKSTAGE)).menu;

            ((Text) menu.getChildren().get(2)).setText(LanguageManager.getText("networkSettingsTitle"));
            ((RadioButton) menu.getChildren().get(12)).setText(LanguageManager.getText("newGame"));
            ((Text) menu.getChildren().get(7)).setText(LanguageManager.getText("yourColor"));
            ((RadioButton) menu.getChildren().get(13)).setText(LanguageManager.getText("joinGame"));
            ((Text) menu.getChildren().get(11)).setText(LanguageManager.getText("ip"));
            ((Text) menu.getChildren().get(4)).setText(LanguageManager.getText("port"));
            ((Button) menu.getChildren().get(6)).setText(LanguageManager.getText("menu_button"));
            ((Button) menu.getChildren().get(5)).setText(LanguageManager.getText("start_button"));
            ((Text) menu.getChildren().get(14)).setText(LanguageManager.getText("network_error"));

            WindowManager.getStage(NETWORKSTAGE).setTitle(getText("network_title"));
        }
    }

    /**
     * Update language in promotion stage
     */
    private static void updateLanguagePromotionStage() {
        if (WindowManager.getStage("PromotionStage") != null) {
            Pane promotion = ((Promotion) WindowManager.getController("PromotionStage")).promotionPane;

            ((Label) promotion.getChildren().get(1)).setText(LanguageManager.getText("promotion_label"));
            ((Button) promotion.getChildren().get(2)).setText(LanguageManager.getText("queen_label"));
            ((Button) promotion.getChildren().get(3)).setText(LanguageManager.getText("bishop_label"));
            ((Button) promotion.getChildren().get(4)).setText(LanguageManager.getText("rook_label"));
            ((Button) promotion.getChildren().get(5)).setText(LanguageManager.getText("knight_label"));

            WindowManager.getStage("PromotionStage").setTitle(getText("promotion_title"));
        }
    }

    /**
     * Update language in game stage
     */
    private static void updateLanguageGameStage() {
        if (WindowManager.getStage("GameStage") != null) {
            Pane menu = ((ChessBoard) WindowManager.getController("GameStage")).graphicElements;

            switchFlagLanguage(oldLanguage, "blackCheck_label", menu);
            switchFlagLanguage(oldLanguage, "whiteCheck_label", menu);
            switchFlagLanguage(oldLanguage, "blackCheckmate_label", menu);
            switchFlagLanguage(oldLanguage, "whiteCheckmate_label", menu);
            switchFlagLanguage(oldLanguage, "stalemate_label", menu);
            ((Button) menu.getChildren().get(10)).setText(LanguageManager.getText("menu_button"));
            ((Label) menu.getChildren().get(2)).setText(LanguageManager.getText("history_label"));
            switchMessengerLanguage(oldLanguage, "calculating_label", menu);
            switchMessengerLanguage(oldLanguage, "network_waiting_label", menu);
            switchMessengerLanguage(oldLanguage, "network_player_waiting_label", menu);
            ((CheckBox) menu.getChildren().get(6)).setText(LanguageManager.getText("touch_move_button"));
            ((CheckBox) menu.getChildren().get(7)).setText(LanguageManager.getText("possible_moves_button"));
            ((CheckBox) menu.getChildren().get(8)).setText(LanguageManager.getText("rotate_button"));
            ((CheckBox) menu.getChildren().get(9)).setText(LanguageManager.getText("flag_button"));

            WindowManager.getStage("GameStage").setTitle(getText("game_title"));
        }
    }

    /**
     * Switches the language of the text on the messenger label
     *
     * @param oldLanguage the old language
     * @param label       the text of the messenger label
     * @param menu        the container which contains the label
     */
    private static void switchMessengerLanguage(ResourceBundle oldLanguage, String label, Pane menu) {
        if (((Label) menu.getChildren().get(5)).getText().equals(oldLanguage.getString(label))) {
            ((Label) menu.getChildren().get(5)).setText(LanguageManager.getText(label));
        }
    }

    /**
     * Switches the language of the text on the flags label
     *
     * @param oldLanguage old language pack
     * @param label       the text of the flag label
     * @param menu        the container which contains the label
     */
    private static void switchFlagLanguage(ResourceBundle oldLanguage, String label, Pane menu) {
        if (((Label) menu.getChildren().get(4)).getText().equals(oldLanguage.getString(label))) {
            ((Label) menu.getChildren().get(4)).setText(LanguageManager.getText(label));
        }
    }
}