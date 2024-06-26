package eldorado;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import eldorado.gamemanager.ElDoradoManager;
import eldorado.gamemanager.MarketManager;
import eldorado.gamemanager.PlayerManager;
import eldorado.models.Card;
import eldorado.models.GameTile;
import eldorado.models.HexMap;
import eldorado.models.Token;
import eldorado.utils.CardTypes;
import eldorado.utils.TerrainTypes;
import eldorado.utils.TokenTypes;
import eldorado.utils.json.MapReader;
import eldorado.utils.MapGenerator;
import eldorado.utils.HexMapBuilder;
import eldorado.utils.json.BlockadeConfiguration;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class ElDoradoManagerTest {

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
    public void testSingletonInstance() {
        ElDoradoManager instance1 = (ElDoradoManager) ElDoradoManager.getInstance();
        ElDoradoManager instance2 = (ElDoradoManager) ElDoradoManager.getInstance();
        assertSame(
                "ElDoradoManager should return the same instance for subsequent calls.",
                instance1, instance2);
    }

    @Test
    public void testInitializeGame() {
        assertTrue("Game initialization should return true.",
                elDoradoManager.initializeGame(3));
        assertEquals("There should be 3 players after initialization.", 3,
                elDoradoManager.players.size());
    }

    @Test
    public void testGetCurrentPlayer() {
        elDoradoManager.initializeGame(2);
        assertSame("The current player should be the first player initially.",
                elDoradoManager.players.get(0),
                elDoradoManager.getCurrentPlayer());
        elDoradoManager.nextTurn();
        assertSame("The current player should be the second player after " +
                "nextTurn is called.",
                elDoradoManager.players.get(1),
                elDoradoManager.getCurrentPlayer());
    }

    @Test
    public void testNextTurn() {
        elDoradoManager.initializeGame(2);
        assertEquals("Initial player index should be 0.", 0,
                elDoradoManager.getPlayerNumber());
        elDoradoManager.nextTurn();
        assertEquals("Player index should be 1 after nextTurn is called once.", 1,
                elDoradoManager.getPlayerNumber());
        elDoradoManager.nextTurn();
        assertEquals("Player index should wrap around to 0 after reaching the end.",
                0, elDoradoManager.getPlayerNumber());
    }

    @Test
    public void testPlayCards() {
        PlayerManager player = new PlayerManager();
        player.createInitialDeck();
        elDoradoManager.players.add(player);

        assertTrue("playCards should return true when a card is played.",
                elDoradoManager.playCards(0, 0));
        assertEquals("Player's hand should be empty after playing a card.", 0,
                player.getHand().size());
    }

    @Test
    public void testPlayToken() {
        elDoradoManager.initializeGame(2);
        PlayerManager player = elDoradoManager.getCurrentPlayer();
        player.addToken(new Token(TokenTypes.JUNGLE1, player));
        assertTrue("playCards should return true when a token is played.",
                elDoradoManager.playToken(0, 0));
        assertEquals("Player's hand should be empty after playing a card.", 0,
                player.getToken().size());
    }

    @Test
    public void testDiscardHand() {
        PlayerManager player = new PlayerManager();
        player.createInitialDeck();
        elDoradoManager.players.add(player);

        assertTrue("discardHand should return true.",
                elDoradoManager.discardHand(0));
        assertEquals("Player's hand should be empty after discarding.", 0,
                player.getHand().size());
    }

    @Test
    public void testClickHex2() {
        Integer[] points = new Integer[] { 0, null, null, null, null, null };
        ElDoradoManager elDoradoManager = (ElDoradoManager) ElDoradoManager.getInstance();
        elDoradoManager.initializeGame(2);
        PlayerManager player = elDoradoManager.players.get(0);
        elDoradoManager.hex_map = new HexMap(100, 100, 1);
        GameTile start_hex = new GameTile(points, points, 50, TerrainTypes.START, 0);
        elDoradoManager.hex_map.map[0][0] = start_hex;
        player.currentHex = start_hex;
        GameTile movehex = new GameTile(points, points, 50, TerrainTypes.JUNGLE, 2);
        elDoradoManager.hex_map.map[0][1] = movehex;
        GameTile tooFarAwayHex = new GameTile(points, points, 50, TerrainTypes.JUNGLE, 2);
        elDoradoManager.hex_map.map[0][3] = tooFarAwayHex;
        GameTile nullHex = new GameTile(points, points, 0, TerrainTypes.JUNGLE, 0);
        elDoradoManager.hex_map.map[1][0] = nullHex;

        // Test when the distance between the current hex and the clicked hex is 1
        // and the player has enough movement points
        GameTile previousHex = player.currentHex;
        elDoradoManager.clickHex(movehex);
        assertNotSame("Not enough movement so should not move", player.currentHex,
                movehex);
    }

    @Test
    public void testDiscardCards() {
        PlayerManager player = new PlayerManager();
        player.createInitialDeck();
        elDoradoManager.players.add(player);

        assertTrue("discardCards should return true.",
                elDoradoManager.discardCards(0, player.getHand()));
        assertEquals("Player's hand should be empty after discarding a card.", 0,
                player.getHand().size());
    }

    @Test
    public void testShuffleDeck() {
        PlayerManager player = new PlayerManager();
        player.createInitialDeck();
        elDoradoManager.players.add(player);

        assertTrue("shuffleDeck should return true.",
                elDoradoManager.shuffleDeck(0));
        // Assuming shuffleDeck does not change the size of the deck
        assertEquals("Player's deck size should remain the same after shuffling.",
                8, player.getDeck().size());
    }

    @Test
    public void testDrawCards() {
        PlayerManager player = new PlayerManager();
        player.createInitialDeck();
        elDoradoManager.players.add(player);

        assertTrue("drawCards should return true.", elDoradoManager.drawCards(0));
        assertEquals("Player should have 4 cards in hand after drawing.", 4,
                player.getHand().size());
    }

    @Test
    public void testGetHand() {
        PlayerManager player = new PlayerManager();
        player.createInitialDeck();
        elDoradoManager.players.add(player);
        player.fillHand();

        assertEquals("Player's hand should contain 4 cards.", 4,
                elDoradoManager.getHand(0).size());
    }

    @Test
    public void testGetTOken() {
        elDoradoManager.initializeGame(2);
        PlayerManager player = elDoradoManager.getCurrentPlayer();
        elDoradoManager.givePlayerToken(new Token(TokenTypes.JUNGLE1, player));
        assertEquals("Player's token initially should contain 1 token.", 1,
                elDoradoManager.getToken(0).size());
    }

    @Test
    public void testGetDeck() {
        PlayerManager player = new PlayerManager();
        player.createInitialDeck();
        elDoradoManager.players.add(player);

        assertEquals("Player's deck should contain 8 cards.", 8,
                elDoradoManager.getDeck(0).size());
    }

    @Test
    public void testGetDiscardPile() {
        PlayerManager player = new PlayerManager();
        player.createInitialDeck();
        elDoradoManager.players.add(player);
        player.discardHand();

        assertEquals("Player's discard pile should contain 4 cards.", 4,
                player.discardPile.size(), 4);
    }

    @Test
    public void testGetRemovedPile() {
        assertEquals("Player's removed pile should contain 0 card.", 0,
                elDoradoManager.getRemovedPile(0).size());
    }

    @Test
    public void testPurchaseCards() {
        List<Card> paymentCards = new ArrayList<>();
        paymentCards.add(new Card(CardTypes.EXPLORER, null));
        paymentCards.add(new Card(CardTypes.TRAVELER, null));
        paymentCards.add(new Card(CardTypes.SAILOR, null));
        List<Card> shopCards = shopManager.shopManagement().getShop();
        int higherPriceCard = 1;
        int lowerOrEqualPriceCard = 1;
        for (int i = 0; i < shopCards.size(); i++) {
            if (shopCards.get(i).getType() == CardTypes.TRANSMITTER) {
                higherPriceCard = i;
            }
            if (shopCards.get(i).getType() == CardTypes.JACKOFALLTRADES) {
                lowerOrEqualPriceCard = i;
            }
        }
        boolean success = elDoradoManager.purchaseCards(
                shopManager, 0, lowerOrEqualPriceCard, paymentCards);
        boolean failed = elDoradoManager.purchaseCards(
                shopManager, 0, higherPriceCard, paymentCards);

        assertTrue("The card should be purchased successfully", success);
        assertFalse("The card should not be purchased successfully", failed);
    }

    @Test
    public void testGetShop() {
        assertEquals("Shop should contain 18 cards.", 18,
                elDoradoManager.getShop(shopManager).size());
    }

    @Test
    public void testGetStock() {
        assertEquals("Stock should contain 36 cards.", 36,
                elDoradoManager.getStock(shopManager).size());
    }

    @Test
    public void testRefillShop() {
        shopManager.shopManagement().buyCard(0);

        assertTrue("refillShop should return true.",
                elDoradoManager.refillShop(shopManager, 0));
        assertEquals("Shop should contain 5 cards after refilling.", 20,
                shopManager.shopManagement().getShop().size());
    }

    @Test
    public void testClickHex() {
        Integer[] points = new Integer[] { 0, null, null, null, null, null };
        ElDoradoManager elDoradoManager = (ElDoradoManager) ElDoradoManager.getInstance();
        elDoradoManager.initializeGame(2);
        elDoradoManager.blockades = new BlockadeConfiguration[0];
        PlayerManager player = elDoradoManager.players.get(0);
        elDoradoManager.hex_map = new HexMap(10, 10, 1);
        GameTile start_hex = new GameTile(points, points, 50, TerrainTypes.START, 1);
        elDoradoManager.hex_map.map[0][0] = start_hex;
        player.currentHex = start_hex;
        GameTile movehex = new GameTile(points, points, 50, TerrainTypes.JUNGLE, 2);
        elDoradoManager.hex_map.map[0][1] = movehex;
        GameTile tooFarAwayHex = new GameTile(points, points, 50, TerrainTypes.JUNGLE, 2);
        elDoradoManager.hex_map.map[0][3] = tooFarAwayHex;
        GameTile nullHex = new GameTile(points, points, 0, TerrainTypes.EMPTY, 2);
        elDoradoManager.hex_map.map[1][0] = nullHex;

        // Test when the type of the last tile is empty
        elDoradoManager.clickHex(nullHex);
        assertSame("The current hex should not change when the type of the last " +
                "tile is empty.",
                player.currentHex, start_hex);

        // Test when the distance between the current hex and the clicked hex is 1
        // and the player has enough movement points
        GameTile previousHex = player.currentHex;
        elDoradoManager.clickHex(movehex);
        assertNotSame("Not enough movement so should not move", player.currentHex,
                movehex);

        player.movement.put(movehex.type, 3); // Replace with actual movement points
        elDoradoManager.clickHex(movehex);
        assertSame("The current hex should be updated to the clicked hex.",
                player.currentHex, movehex);
        assertEquals("The movement points for the corresponding hex type should " +
                "be deducted.",
                1, player.movement.get(movehex.type).intValue());

        // Test when the distance between the current hex and the clicked hex is 1
        // and the player has enough movement points for null type
        player.currentHex = start_hex;
        player.movement.put(null,
                4); // Replace with actual movement points for null type
        elDoradoManager.clickHex(movehex);
        assertSame("The current hex should be updated to the clicked hex.",
                player.currentHex, movehex);
        assertEquals("The movement points for null type should be deducted.", 2,
                player.movement.get(null).intValue());

        // Test when the distance between the current hex and the clicked hex is not
        // 1
        player.currentHex = start_hex;
        player.movement.put(tooFarAwayHex.type,
                3); // Replace with actual movement points
        elDoradoManager.clickHex(tooFarAwayHex);
        assertSame("The current hex should not change when the distance between " +
                "the current hex and the clicked hex is not 1.",
                player.currentHex, start_hex);
    }

    @Test
    public void testGetStartHexes() {
        Integer[] points = new Integer[] { 0, null, null, null, null, null };
        ElDoradoManager elDoradoManager = (ElDoradoManager) ElDoradoManager.getInstance();
        elDoradoManager.initializeGame(2);
        elDoradoManager.hex_map = new HexMap(10, 10, 1);
        GameTile hex1 = new GameTile(points, points, 50, TerrainTypes.START, 0);
        elDoradoManager.hex_map.map[0][0] = hex1;
        GameTile hex2 = new GameTile(points, points, 50, TerrainTypes.JUNGLE, 0);
        elDoradoManager.hex_map.map[0][1] = hex2;
        GameTile hex3 = new GameTile(points, points, 50, TerrainTypes.JUNGLE, 0);
        elDoradoManager.hex_map.map[0][3] = hex3;
        GameTile hex4 = new GameTile(points, points, 50, TerrainTypes.START, 0);
        elDoradoManager.hex_map.map[1][0] = hex4;

        List<GameTile> startHexes = elDoradoManager.getStartHexes();
        assertEquals("There should be 2 start hexes.", 2, startHexes.size());
        assertTrue("The start hexes should contain the correct hexes.",
                startHexes.contains(hex1) && startHexes.contains(hex4));
    }
}
