package eldorado.utils;

import java.net.URL;

import javax.swing.ImageIcon;

public class Utils {
    public static ImageIcon getImageIcon(String path) {
        URL imageURL = Utils.class.getResource("/" + path);
        ImageIcon icon;
        try {
            icon = new ImageIcon(imageURL);
        } catch (Exception e) {
            // Set a default icon
            icon = new ImageIcon(Utils.class.getResource("/sailor.png"));
        }
        return icon;
    }
}
