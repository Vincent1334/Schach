package chess.managers;

import chess.gui.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class WindowManager {

    private static  FXMLLoader fxmlLoader;

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

    public static Object getController(){
        return fxmlLoader.getController();
    }
}
