package eldorado.uitests;

import eldorado.gamemanager.CaveManager;
import eldorado.gamemanager.ElDoradoManager;
import eldorado.gamemanager.MarketManager;
import eldorado.gameui.HandUI;
import eldorado.gameui.PileUI;
import eldorado.models.Card;
import eldorado.utils.CardTypes;
import eldorado.utils.HexMapBuilder;
import eldorado.utils.MapGenerator;
import eldorado.utils.PileTypes;
import eldorado.utils.json.MapReader;

import javax.swing.*;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PileUITest {
    static{
        System.setProperty("java.awt.headless", "true");
    }

    private PileUI pileUI;

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

         pileUI = new PileUI(market.shopManagement().getShop(), new HandUI(), false, PileTypes.SHOP);
    }

//    @Test
//    public void whenSetUpSuccessful_thenHeadlessIsTrue() {
//        assertTrue(GraphicsEnvironment.isHeadless());
//    }
     @Test
     public void testInitialization() {
        setUp();
         assertNotNull(pileUI);
         assertEquals(18, pileUI.shopCards.size());
     }

     @Test
     public void testShowPopup() {
        setUp();
         SwingUtilities.invokeLater(() -> {
             pileUI.showPopup();

             Window[] windows = Window.getWindows();
             JDialog dialog = null;
             for (Window window : windows) {
                 if (window instanceof JDialog) {
                     dialog = (JDialog) window;
                     break;
                 }
             }

             assertNotNull(dialog);
             assertTrue(dialog.isVisible());
             assertEquals("Shop", dialog.getTitle());

             JScrollPane scrollPane = (JScrollPane) dialog.getContentPane().getComponent(0);
             JPanel mainPanel = (JPanel) scrollPane.getViewport().getComponent(0);

             assertTrue(mainPanel.getComponentCount() > 0);
             dialog.dispose();
         });
     }

     @Test
     public void testUpdateMainPanel() {
        setUp();
         JPanel mainPanel = new JPanel();

         pileUI.updateMainPanel(mainPanel, null);

         assertEquals(6, mainPanel.getComponentCount());

         JPanel groupPanel = (JPanel) mainPanel.getComponent(0);
         assertEquals(2, groupPanel.getComponentCount());

         JPanel cardsPanel = (JPanel) groupPanel.getComponent(1);
         assertEquals(3, cardsPanel.getComponentCount());

         JButton cardButton1 = (JButton) cardsPanel.getComponent(0);
         JButton cardButton2 = (JButton) cardsPanel.getComponent(1);

         assertNotNull(cardButton1.getIcon());
         assertNotNull(cardButton2.getIcon());
     }

     @Test
     public void testGroupCardsByType() {
        setUp();
         List<Card> cards = new ArrayList<>();
         cards.add(new Card(CardTypes.ADVENTURER, null));
         cards.add(new Card(CardTypes.ADVENTURER, null));
         cards.add(new Card(CardTypes.COMPASS, null));

         Map<String, List<Card>> groupedCards = pileUI.groupCardsByType(cards);

         assertEquals(2, groupedCards.size());
         assertEquals(2, groupedCards.get("ADVENTURER").size());
         assertEquals(1, groupedCards.get("COMPASS").size());
     }

     @Test
     public void testInteractionEnabled() {
        setUp();
         pileUI = new PileUI(new ArrayList<>(), new HandUI(), true, PileTypes.SHOP);
         assertTrue(pileUI.interactionEnabled);

         pileUI = new PileUI(new ArrayList<>(), new HandUI(), false, PileTypes.STOCK);
         assertFalse(pileUI.interactionEnabled);

         pileUI = new PileUI(new ArrayList<>(), new HandUI(), true, PileTypes.DISCARDED);
         assertTrue(pileUI.interactionEnabled);
     }
}