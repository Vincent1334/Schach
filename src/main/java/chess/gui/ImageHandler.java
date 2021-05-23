package chess.gui;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;

public class ImageHandler {
    private static ImageHandler instance;

    private Map<String, Image> imgs = new HashMap<String, Image>();

    public static ImageHandler getInstance() {
        if (instance == null) {
            instance = new ImageHandler();
        }
        return instance;
    }

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

    public Image getImage(final String key) {
        if (imgs.get(key) == null) {
            loadImage(key, "jpg", "png", "bmp", "gif");
        }
        return imgs.get(key);
    }

}
