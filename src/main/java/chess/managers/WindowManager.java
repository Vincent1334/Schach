package chess.managers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * This class loads FXML code from file
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-25
 */
public class WindowManager {

    private static  FXMLLoader fxmlLoader;

    /**
     * Load FXML code from File
     * @param key file name
     * @return parent object
     */
    public static Parent createWindow(String key){
        try{
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            fxmlLoader = new FXMLLoader(classLoader.getResource("chess/gui/" +key));
            fxmlLoader.setResources(LanguageManager.getResourceBundle());
            return fxmlLoader.load();
        }catch (Exception x){
            x.printStackTrace();
            return null;
        }
    }

    /**
     * return controller object
     * @return controller
     */
    public static Object getController(){
        return fxmlLoader.getController();
    }
}
