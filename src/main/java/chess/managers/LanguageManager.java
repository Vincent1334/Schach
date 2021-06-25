package chess.managers;

import java.util.*;

/**
 * This class contains the Language Manager for the program
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-25
 */
public class LanguageManager {

    private static int index = 0;
    private static List<Locale> locale = new ArrayList<Locale>(Arrays.asList(new Locale("en", "US"),
                                                                             new Locale("de", "DE"),
                                                                             new Locale("fr", "FR")));

    private static ResourceBundle messages = ResourceBundle.getBundle("/languages/MessagesBundle", locale.get(index));

    /**
     * Change to the next Language file in the list
     */
    public static void nextLocale(){
        index ++;
        if(index == locale.size()){
            index = 0;
        }
        messages = ResourceBundle.getBundle("/languages/MessagesBundle", locale.get(index));
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
}