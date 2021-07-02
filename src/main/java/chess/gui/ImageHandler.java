package chess.gui;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;

/**
 * handles the images for the gui
 *
 * @author Lydia Engelhardt, Sophia Kuhlmann, Vincent Schiller, Friederike Weilbeer
 * 2021-06-09
 */
public class ImageHandler {

    private static ImageHandler instance;
    private Map<String, Image> imgs = new HashMap<>();

    /**
     * returns the ImageHandler
     *
     * @return the instance
     */
    public static ImageHandler getInstance() {
        if (instance == null) {
            instance = new ImageHandler();
        }
        return instance;
    }

    /**
     * loads an image
     *
     * @param name       the name of the image
     * @param extensions the datatype of the Image
     */
    private void loadImage(String name, String... extensions) {
        URL url = null;
        for (String ext : extensions) {
            url = ImageHandler.class.getResource(name + "." + ext);
            if (url != null) {
                break;
            }
        }
        Image image = new Image(url.toExternalForm(), true);
        imgs.put(name, image);
    }

    /**
     * returns a image
     *
     * @param key the key of the image
     * @return the image
     */
    public Image getImage(final String key) {
        if (imgs.get(key) == null) {
            loadImage(key, "jpg", "png", "bmp", "gif");
        }
        return imgs.get(key);
    }

    /**
     * returns the Image of a figure by its char-value
     *
     * @param symbol of the figure you want the image from
     * @return the image of the figure
     */
    public static Image getImageBySymbol(char symbol) {
        String color = "Black";
        if (Character.isUpperCase(symbol)) {
            color = "White";
        }
        switch (Character.toUpperCase(symbol)) {
            // Rook
            case 'R':
                return getInstance().getImage("Rook" + color);
            // Knight
            case 'N':
                return getInstance().getImage("Knight" + color);
            // Bishop
            case 'B':
                return getInstance().getImage("Bishop" + color);
            // Queen
            case 'Q':
                return getInstance().getImage("Queen" + color);
            // King
            case 'K':
                return getInstance().getImage("King" + color);
            // Pawn
            case 'P':
                return getInstance().getImage("Pawn" + color);
            // None
            default:
                return null;
        }
    }
}
