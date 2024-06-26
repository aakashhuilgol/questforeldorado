package eldorado;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.swing.ImageIcon;

import org.junit.Test;

import eldorado.utils.Utils;

public class UtilsTest {
    @Test
    public void testGetImageIcon_ValidPath() {
        String validPath = "captain.png";
        
        ImageIcon icon = Utils.getImageIcon(validPath);
        
        assertNotNull("The icon should not be null for a valid image path", icon);
    }
    
    @Test
    public void testGetImageIcon_InvalidPath() {
        String invalidPath = "nonExistentImage.png";
        
        ImageIcon icon = Utils.getImageIcon(invalidPath);
        assertNotNull("The icon should not be null for a valid image path", icon);
        // Check if the default icon is loaded
        ImageIcon defaultIcon = new ImageIcon(Utils.class.getResource("/sailor.png"));
        assertEquals("The icon should be the default icon for an invalid image path", defaultIcon.getImage(), icon.getImage());
    }
}
