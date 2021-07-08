package chess.managers;

import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LanguageManagerTest {

    @Test
    public void testLanguage(){
        LanguageManager.setLanguage("de");

        assertEquals("Starte ein lokales Spiel gegen einen Freund", LanguageManager.getText("gamemode01"), "Language DE is not correct!");
        assertEquals("Starte ein lokales Spiel gegen den Computer", LanguageManager.getText("gamemode02"), "Language DE is not correct!");
        assertEquals("Starte ein Netzwerkspiel gegen einen Freund", LanguageManager.getText("gamemode03"), "Language DE is not correct!");

        LanguageManager.setLanguage("en");

        assertEquals("Start a local game against a friend", LanguageManager.getText("gamemode01"), "Language EN is not correct!");
        assertEquals("Start a local game against the computer", LanguageManager.getText("gamemode02"), "Language EN is not correct!");
        assertEquals("Start a network game against a friend", LanguageManager.getText("gamemode03"), "Language EN is not correct!");

        LanguageManager.setLanguage("fr");

        assertEquals("Démarrer une partie locale contre un ami", LanguageManager.getText("gamemode01"), "Language FR is not correct!");
        assertEquals("Démarrer une partie locale contre l'ordinateur", LanguageManager.getText("gamemode02"), "Language FR is not correct!");
        assertEquals("Démarrer une partie en réseau contre un ami", LanguageManager.getText("gamemode03"), "Language FR is not correct!");
    }

    @Test
    public void testRessourceBundle(){
        LanguageManager.setLanguage("en");
        ResourceBundle enMessage = ResourceBundle.getBundle("/languages/MessagesBundle", new Locale("en", "US"));
        assertEquals(enMessage, LanguageManager.getResourceBundle(), "Ressourcebundle EN is not correct!");

        LanguageManager.setLanguage("de");
        ResourceBundle deMessage = ResourceBundle.getBundle("/languages/MessagesBundle", new Locale("de", "DE"));
        assertEquals(deMessage, LanguageManager.getResourceBundle(), "Ressourcebundle DE is not correct!");

        LanguageManager.setLanguage("fr");
        ResourceBundle frMessage = ResourceBundle.getBundle("/languages/MessagesBundle", new Locale("fr", "FR"));
        assertEquals(frMessage, LanguageManager.getResourceBundle(), "Ressourcebundle FR is not correct!");
    }
}
