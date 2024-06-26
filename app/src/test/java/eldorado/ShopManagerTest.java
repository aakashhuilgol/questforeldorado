package eldorado;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import eldorado.gamemanager.MarketManager;
import eldorado.models.Card;
import eldorado.utils.CardTypes;

public class ShopManagerTest {

    private MarketManager shopManager;

    @Before
    public void setUp() {
        shopManager = MarketManager.getInstance();
        shopManager.shopManagement().createShop();
        shopManager.stockManagement().createStock();
    }

    @Test
    public void testCreateShop() {
        List<Card> shop = shopManager.shopManagement().getShop();
        assertEquals("Initial market should have 18 cards.", 18, shop.size());
        assertTrue("Initial market contains these card types.", shop.stream().allMatch(card -> card.getType() == CardTypes.SCOUT ||
                card.getType() == CardTypes.TRAILBLAZER ||
                card.getType() == CardTypes.TRANSMITTER ||
                card.getType() == CardTypes.TREASURECHEST ||
                card.getType() == CardTypes.PHOTOGRAPHER ||
                card.getType() == CardTypes.JACKOFALLTRADES));
    }

    @Test
    public void testCreateStock() {
        List<Card> stock = shopManager.stockManagement().getStock();
        assertEquals("Initial shop should have 36 cards.", 36, stock.size());
        assertTrue("Initial stock contains these card types.", stock.stream().allMatch(card -> card.getType() == CardTypes.CAPTAIN ||
                card.getType() == CardTypes.CARTOGRAPHER ||
                card.getType() == CardTypes.COMPASS ||
                card.getType() == CardTypes.SCIENTIST ||
                card.getType() == CardTypes.TRAVELLOG ||
                card.getType() == CardTypes.NATIVE ||
                card.getType() == CardTypes.PIONEER ||
                card.getType() == CardTypes.PROPPLANE ||
                card.getType() == CardTypes.GIANTMACHETE ||
                card.getType() == CardTypes.ADVENTURER ||
                card.getType() == CardTypes.JOURNALIST ||
                card.getType() == CardTypes.MILLIONARE));
    }

    @Test
    public void testBuyCard() {
        List<Card> initialShop = shopManager.shopManagement().getShop();
        int initialSize = initialShop.size();

        Card boughtCard = shopManager.shopManagement().buyCard(0);

        assertNotNull("Not empty.", boughtCard);
        assertEquals("Reduce 1 after buying.", initialSize - 1, shopManager.shopManagement().getShop().size());
        assertTrue("Card is gone.", shopManager.shopManagement().getShop().contains(boughtCard));
    }

    @Test
    public void testRefill() {
        int stockSizeBefore = shopManager.stockManagement().getStock().size();
        shopManager.shopManagement().refillShop(12);
        int stockSizeAfter = shopManager.stockManagement().getStock().size();
        assertTrue("Refill must reduce stock size.", stockSizeAfter < stockSizeBefore);
    }

    @Test
    public void testGetShop() {
        assertNotNull("Success.", shopManager.shopManagement());
        assertEquals("Get initial shop should contain 18 cards.", 18, shopManager.shopManagement().getShop().size());
    }

    @Test
    public void testGetStock() {
        assertNotNull("Success.", shopManager.stockManagement());
        assertEquals("Get initial stock should contain 36 cards.", 36, shopManager.stockManagement().getStock().size());
    }

    @Test
    public void testCheckLastCard() {
        shopManager.shopManagement().buyCard(0);
        shopManager.shopManagement().buyCard(0);
        boolean isLastCard = shopManager.shopManagement().checkLastCard(0);
        assertTrue("After buying same 2 types, 3rd card must be last card", isLastCard);
        
        shopManager.shopManagement().buyCard(0);
        shopManager.shopManagement().refillShop(0);
        isLastCard = shopManager.shopManagement().checkLastCard(17);
        assertFalse("After refill, added card will not be the last card remaining", isLastCard);
    }
}
