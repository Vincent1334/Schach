package chess.managers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * This class loads FXML code from file
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-25
 */
public class WindowManager {

    private static final FXMLLoader[] FXML_LOADER = new FXMLLoader[4];
    private static final Stage[] STAGES = new Stage[4];

    /**
     * Load FXML code from File
     *
     * @param key      file name
     * @param titleKey parent object
     */
    public static void initialWindow(String key, String titleKey) {
        try {
            //load FXML file
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            FXML_LOADER[keyToStage(key)] = new FXMLLoader(classLoader.getResource("chess/gui/" + key + ".fxml"));
            FXML_LOADER[keyToStage(key)].setResources(LanguageManager.getResourceBundle());

            STAGES[keyToStage(key)] = new Stage();
            STAGES[keyToStage(key)].setTitle(LanguageManager.getText(titleKey));
            STAGES[keyToStage(key)].setResizable(false);
            STAGES[keyToStage(key)].centerOnScreen();
            STAGES[keyToStage(key)].setScene(new Scene(FXML_LOADER[keyToStage(key)].load()));

            //Special Settings
            if ("PromotionStage".equals(key)) {
                STAGES[keyToStage(key)].initStyle(StageStyle.UNDECORATED);
                STAGES[keyToStage(key)].setAlwaysOnTop(true);
            }

        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
     * Convert key to a stage
     *
     * @param key keyword
     * @return Stage
     */
    private static int keyToStage(String key) {
        switch (key) {
            case "MenuStage":
                return 0;
            case "GameStage":
                return 1;
            case "PromotionStage":
                return 2;
            case "NetworkStage":
                return 3;
            default:
                return 4;
        }
    }

    /**
     * Set stage to visible
     *
     * @param key keyword
     */
    public static void showStage(String key) {
        STAGES[keyToStage(key)].show();
    }

    /**
     * Close a Stage
     *
     * @param key keyword
     */
    public static void closeStage(String key) {
        if (STAGES[keyToStage(key)] != null) {
            STAGES[keyToStage(key)].hide();
        }
    }

    //--------------getter / setter---------------------------------------------------------------------------------------------------------------

    /**
     * Returns controller object
     *
     * @param key keyword
     * @return controller
     */
    public static Object getController(String key) {
        return FXML_LOADER[keyToStage(key)].getController();
    }

    /**
     * Returns a stage
     *
     * @param key keyword
     * @return Stage
     */
    public static Stage getStage(String key) {
        return STAGES[keyToStage(key)];
    }
}
