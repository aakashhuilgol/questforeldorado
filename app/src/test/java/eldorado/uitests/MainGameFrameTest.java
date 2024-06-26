package eldorado.uitests;
import javax.swing.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.*;

import org.junit.Before;
import org.junit.Test;

import eldorado.gamemanager.CaveManager;
import eldorado.gamemanager.ElDoradoManager;
import eldorado.gamemanager.MarketManager;
import eldorado.gameui.HandUI;
import eldorado.gameui.MainGameFrame;
import eldorado.utils.HexMapBuilder;
import eldorado.utils.MapGenerator;
import eldorado.utils.PileTypes;
import eldorado.utils.json.MapReader;

public class MainGameFrameTest {
//    static{
//        System.setProperty("java.awt.headless", "true");
//    }
//    private MainGameFrame mainGameFrame;
//
//    public void setUp() {
//         CaveManager caveManager = CaveManager.getInstance();
//         caveManager.createCave();
//         HexMapBuilder hexMapBuilder = new HexMapBuilder();
//         MapGenerator mapGenerator = new MapGenerator();
//         MapReader mapReader = new MapReader();
//         ElDoradoManager.initializeInstance(hexMapBuilder, mapGenerator, mapReader,true);
//         ElDoradoManager gameController = ElDoradoManager.getInstance();
//         MarketManager market = MarketManager.getInstance();
//         market.shopManagement().createShop();
//         market.stockManagement().createStock();
//         gameController.initializeGame(2);
//
//         mainGameFrame = new MainGameFrame(gameController, null);
//    }
//
//    @Test
//    public void whenMainGameSetUpSuccessful_thenHeadlessIsTrue() {
//        assertTrue(GraphicsEnvironment.isHeadless());
//    }
//     @Test
//     public void testMainGameFrameInitialization() {
//        setUp();
//         assertNotNull(mainGameFrame);
//         assertEquals("El Dorado", mainGameFrame.getTitle());
//         assertEquals(JFrame.EXIT_ON_CLOSE, mainGameFrame.getDefaultCloseOperation());
//     }
//
//     @Test
//     public void testMapAreaInitialization() {
//        setUp();
//        mainGameFrame.mapArea();
//         Component[] components = mainGameFrame.getContentPane().getComponents();
//         boolean foundMap = false;
//         for (Component component : components) {
//             if (component instanceof JPanel) {
//                 Component[] panelComponents = ((JPanel) component).getComponents();
//                 for (Component panelComponent : panelComponents) {
//                     if (panelComponent instanceof JInternalFrame) {
//                         foundMap = true;
//                         break;
//                     }
//                 }
//             }
//         }
//         assertTrue("Map area should be initialized", foundMap);
//     }
//
//     @Test
//     public void testPlayerIndicatorUI() {
//        setUp();
//        mainGameFrame.playerIndicatorUI();
//         Component[] components = mainGameFrame.getContentPane().getComponents();
//         boolean foundPlayerIndicator = false;
//         for (Component component : components) {
//             if (component instanceof JPanel) {
//                 Component[] panelComponents = ((JPanel) component).getComponents();
//                 for (Component panelComponent : panelComponents) {
//                     if (panelComponent instanceof JLabel) {
//                         foundPlayerIndicator = true;
//                         assertEquals("Player 1", ((JLabel) panelComponent).getText());
//                         break;
//                     }
//                 }
//             }
//         }
//         assertTrue("Player indicator UI should be initialized", foundPlayerIndicator);
//     }
//
//     @Test
//     public void testBottomUI() {
//        setUp();
//        mainGameFrame.BottomUI();
//         Component[] components = mainGameFrame.getContentPane().getComponents();
//         boolean foundHandUI = false;
//         boolean foundEndTurnButton = false;
//         for (Component component : components) {
//             if (component instanceof JPanel) {
//                 Component[] panelComponents = ((JPanel) component).getComponents();
//                 for (Component panelComponent : panelComponents) {
//                     if (panelComponent instanceof HandUI) {
//                         foundHandUI = true;
//                     } else if (panelComponent instanceof JPanel) {
//                         Component[] buttonPanelComponents = ((JPanel) panelComponent).getComponents();
//                         for (Component buttonPanelComponent : buttonPanelComponents) {
//                             if (buttonPanelComponent instanceof JButton) {
//                                 foundEndTurnButton = true;
//                                 assertEquals("End Turn", ((JButton) buttonPanelComponent).getText());
//                             }
//                         }
//                     }
//                 }
//             }
//         }
//         assertTrue("HandUI should be initialized", foundHandUI);
//         assertTrue("End Turn button should be initialized", foundEndTurnButton);
//     }
//
//     @Test
//     public void testMarketUI() {
//        setUp();
//        mainGameFrame.MarketUI(BorderLayout.WEST, new PileTypes[]{PileTypes.SHOP, PileTypes.STOCK});
//         mainGameFrame.MarketUI(BorderLayout.EAST, new PileTypes[]{PileTypes.DISCARDED, PileTypes.REMOVED});
//
//         Component[] components = mainGameFrame.getContentPane().getComponents();
//         boolean foundWestPanel = false;
//         boolean foundEastPanel = false;
//
//         for (Component component : components) {
//             if (component instanceof JPanel) {
//                 if (((BorderLayout) mainGameFrame.getContentPane().getLayout()).getLayoutComponent(BorderLayout.WEST) == component) {
//                     foundWestPanel = true;
//                     assertEquals(2, ((JPanel) component).getComponentCount());
//                 } else if (((BorderLayout) mainGameFrame.getContentPane().getLayout()).getLayoutComponent(BorderLayout.EAST) == component) {
//                     foundEastPanel = true;
//                     assertEquals(2, ((JPanel) component).getComponentCount());
//                 }
//             }
//         }
//
//         assertTrue("West MarketUI should be initialized", foundWestPanel);
//         assertTrue("East MarketUI should be initialized", foundEastPanel);
//     }
//
//     @Test
//     public void testRepaintUI() {
//        setUp();
//        mainGameFrame.repaintUI();
//
//         // Check if components are re-initialized
//         Component[] components = mainGameFrame.getContentPane().getComponents();
//         assertEquals(5, components.length);
//     }
}
