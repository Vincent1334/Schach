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

    private static  FXMLLoader[] fxmlLoader = new FXMLLoader[4];

    private static Stage[] stages = new Stage[4];

    /**
     * Load FXML code from File
     * @param key file name
     * @return parent object
     */
    public static void initialWindow(String key, String titleKey){
        try{
            //load FXML file
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            fxmlLoader[keyToStage(key)] = new FXMLLoader(classLoader.getResource("chess/gui/" + key + ".fxml"));
            fxmlLoader[keyToStage(key)].setResources(LanguageManager.getResourceBundle());

            stages[keyToStage(key)] = new Stage();
            stages[keyToStage(key)].setTitle(LanguageManager.getText(titleKey));
            stages[keyToStage(key)].setResizable(false);
            stages[keyToStage(key)].centerOnScreen();
            stages[keyToStage(key)].setScene(new Scene(fxmlLoader[keyToStage(key)].load()));

            //Special Settings
            switch (key){
                case "PromotionStage":{
                    stages[keyToStage(key)].initStyle(StageStyle.UNDECORATED);
                    stages[keyToStage(key)].setAlwaysOnTop(true);
                    break;
                }
            }

        }catch (Exception x){
            x.printStackTrace();
        }
    }

    /**
     * Convert key to a stage
     * @param key keyword
     * @return Stage
     */
    private static int keyToStage(String key){
        switch (key){
            case "MenuStage": return 0;
            case "GameStage": return 1;
            case "PromotionStage": return 2;
            case "NetworkStage": return 3;
            default: return 4;
        }
    }

    /**
     * Set visible of stage
     * @param key keyword
     */
    public static void showStage(String key){
        stages[keyToStage(key)].show();
    }

    /**
     * Close a Stage
     * @param key keyword
     */
    public static void closeStage(String key){
        if(stages[keyToStage(key)] != null) stages[keyToStage(key)].hide();
    }

    /**
     * Returns controller object
     * @param key keyword
     * @return controller
     */
    public static Object getController(String key){
        return fxmlLoader[keyToStage(key)].getController();
    }

    /**
     * Returns a stage
     * @param key keyword
     * @return Stage
     */
    public static Stage getStage(String key){
        return stages[keyToStage(key)];
    }
}
