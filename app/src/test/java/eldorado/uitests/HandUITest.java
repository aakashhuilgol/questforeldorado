package eldorado.uitests;

import javax.swing.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.awt.Color;

import eldorado.gamemanager.CaveManager;
import eldorado.gamemanager.ElDoradoManager;
import eldorado.gamemanager.MarketManager;
import eldorado.gameui.HandUI;
import eldorado.utils.HexMapBuilder;
import eldorado.utils.MapGenerator;
import eldorado.utils.json.MapReader;

public class HandUITest {
    private HandUI handUI;

    @Before
    public void setUp() {
         CaveManager caveManager = CaveManager.getInstance();
         caveManager.createCave();
         HexMapBuilder hexMapBuilder = new HexMapBuilder();
         MapGenerator mapGenerator = new MapGenerator();
         MapReader mapReader = new MapReader();
         ElDoradoManager.initializeInstance(hexMapBuilder, mapGenerator, mapReader, true, true);
         ElDoradoManager gameController = ElDoradoManager.getInstance();
         MarketManager market = MarketManager.getInstance();
         market.shopManagement().createShop();
         market.stockManagement().createStock();
         gameController.initializeGame(2);

         // Create a new instance of HandUI
         handUI = new HandUI();
         // Clear players list before each test
         gameController.reset(true);
    }

     @Test
     public void testSetHand() {
         // Set the hand in HandUI
         handUI.setHand();

         // Check if the hand and tokenHand are correctly set
         assertEquals(4, handUI.getComponents().length);
         assertTrue(handUI.getComponents()[0] instanceof JButton);
         assertTrue(handUI.getComponents()[1] instanceof JButton);
     }

     @Test
     public void testToggleCardSelection() {
         // Set the hand in HandUI
         handUI.setHand();

         // Get the first button (corresponding to the first card)
         JButton cardButton = (JButton) handUI.getComponents()[0];

         // Simulate selecting the card
         handUI.toggleCardSelection(0, cardButton);

         // Check if the card is selected
         assertTrue(handUI.getSelectedCards().contains(0));
         assertEquals(BorderFactory.createLineBorder(Color.BLUE, 3).getBorderInsets(cardButton),
                 cardButton.getBorder().getBorderInsets(cardButton));

         // Simulate deselecting the card
         handUI.toggleCardSelection(0, cardButton);

         // Check if the card is deselected
         assertFalse(handUI.getSelectedCards().contains(0));
         assertEquals(BorderFactory.createEmptyBorder().getBorderInsets(cardButton),
                 cardButton.getBorder().getBorderInsets(cardButton));
     }

     @Test
     public void testClearSelections() {
         // Set the hand in HandUI
         handUI.setHand();

         // Get the first button (corresponding to the first card)
         JButton cardButton = (JButton) handUI.getComponents()[0];

         // Simulate selecting the card
         handUI.toggleCardSelection(0, cardButton);

         // Clear selections
         handUI.clearSelections();

         // Check if all cards are deselected
         assertTrue(handUI.getSelectedCards().contains(0));
         assertEquals(BorderFactory.createEmptyBorder().getBorderInsets(cardButton),
                 cardButton.getBorder().getBorderInsets(cardButton));
     }

     @Test
     public void testOnShopClosed() {
         // Set the hand in HandUI
         handUI.setHand();

         // Simulate closing the shop
         handUI.onShopClosed();

         // Check if the hand is reset and selections are cleared
         assertEquals(4, handUI.getComponents().length);
         assertTrue(handUI.getComponents()[0] instanceof JButton);
         assertTrue(handUI.getComponents()[1] instanceof JButton);
         assertTrue(handUI.getSelectedCards().isEmpty());
     }

     @Test
     public void testRightClickContextMenuForCard() {
         // Set the hand in HandUI
         handUI.setHand();

         // Get the first button (corresponding to the first card)
         JButton cardButton = (JButton) handUI.getComponents()[0];

         // Simulate a right-click event to open context menu
         JPopupMenu contextMenu = handUI.setupContextMenu(cardButton, 0);
         assertNotNull(contextMenu);
         assertEquals(4, contextMenu.getComponentCount());

         // Check if the context menu has the correct items
         assertEquals("Play", ((JMenuItem) contextMenu.getComponent(0)).getText());
         assertEquals("Discard", ((JMenuItem) contextMenu.getComponent(2)).getText());
         assertEquals("Buy", ((JMenuItem) contextMenu.getComponent(1)).getText());
         assertEquals("Remove", ((JMenuItem) contextMenu.getComponent(3)).getText());
     }

     @Test
     public void testRightClickContextMenuForToken() {
         // Set the hand in HandUI
         handUI.setHand();

         // Get the third button (corresponding to the first token)
         JButton tokenButton = (JButton) handUI.getComponents()[2];

         // Simulate a right-click event to open context menu
         JPopupMenu contextMenu = handUI.setupTokenContextMenu(tokenButton, 0);
         assertNotNull(contextMenu);
         assertEquals(1, contextMenu.getComponentCount());

         // Check if the context menu has the correct item
         assertEquals("Play", ((JMenuItem) contextMenu.getComponent(0)).getText());
     }
}