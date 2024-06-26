/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package eldorado;

import eldorado.gamemanager.ElDoradoManager;
import eldorado.gamemanager.MarketManager;
import eldorado.gameui.MainGameFrame;
import eldorado.utils.HexMapBuilder;
import eldorado.utils.MapGenerator;
import eldorado.utils.json.MapReader;

import javax.swing.*;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.*;

public class AppTest {

    private ElDoradoManager elDoradoManager;
    private MarketManager shopManager;

    @Before
    public void setUp() {
        HexMapBuilder hexMapBuilder = new HexMapBuilder();
        MapGenerator mapGenerator = new MapGenerator();
        MapReader mapReader = new MapReader();
        ElDoradoManager.initializeInstance(hexMapBuilder, mapGenerator, mapReader, false, false);

        elDoradoManager = (ElDoradoManager) ElDoradoManager.getInstance();
        shopManager = MarketManager.getInstance();
        shopManager.shopManagement().createShop();
        shopManager.stockManagement().createStock();

        // Clear players list before each test
        elDoradoManager.reset(false);
    }

    @Test
    public void testPlayGameSimulation() {
        SwingUtilities.invokeLater(() -> {
            // Call the method to be tested
            App.main(new String[] {});

            // Get the main frame that should have been created
            Frame[] frames = Frame.getFrames();
            MainGameFrame mainFrame = null;
            for (Frame frame : frames) {
                if (frame instanceof MainGameFrame) {
                    mainFrame = (MainGameFrame) frame;
                    break;
                }
            }

            // Verify that the main frame is created and visible
            assertNotNull(mainFrame);
            assertTrue(mainFrame.isVisible());

            // Verify that the frame's title is correct
            assertEquals("El Dorado", mainFrame.getTitle());

            // Verify that the content pane has been set up with the expected components
            Container contentPane = mainFrame.getContentPane();
            assertNotNull(contentPane);
            assertEquals(BorderLayout.class, contentPane.getLayout().getClass());

            // Check if the map area is initialized
            Component centerComponent = ((BorderLayout) contentPane.getLayout())
                    .getLayoutComponent(BorderLayout.CENTER);
            assertNotNull(centerComponent);
            assertTrue(centerComponent instanceof JPanel);

            // Check if the player indicator is initialized
            Component northComponent = ((BorderLayout) contentPane.getLayout()).getLayoutComponent(BorderLayout.NORTH);
            assertNotNull(northComponent);
            assertTrue(northComponent instanceof JPanel);
            JLabel playerTurnLabel = (JLabel) ((JPanel) northComponent).getComponent(0);
            assertEquals("Player 1", playerTurnLabel.getText());

            // Check if the bottom UI is initialized
            Component southComponent = ((BorderLayout) contentPane.getLayout()).getLayoutComponent(BorderLayout.SOUTH);
            assertNotNull(southComponent);
            assertTrue(southComponent instanceof JPanel);

            // Check if the market UI is initialized
            Component westComponent = ((BorderLayout) contentPane.getLayout()).getLayoutComponent(BorderLayout.WEST);
            assertNotNull(westComponent);
            assertTrue(westComponent instanceof JPanel);

            Component eastComponent = ((BorderLayout) contentPane.getLayout()).getLayoutComponent(BorderLayout.EAST);
            assertNotNull(eastComponent);
            assertTrue(eastComponent instanceof JPanel);

            // Dispose the frame after the test
            mainFrame.dispose();
        });
    }
}