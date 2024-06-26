package eldorado;

import static org.junit.Assert.*;

import java.util.Collections;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import eldorado.gamemanager.PlayerManager;
import eldorado.models.Card;
import eldorado.models.Token;
import eldorado.utils.TokenTypes;

public class PlayerManagerTest {
    private PlayerManager player;

    @Before
    public void setUp() {
        player = new PlayerManager();
        player.createInitialDeck();
        player.shuffleDeck();
    }

    @Test
    public void testInitialDeckSize() {
        assertEquals("Initial deck should have 8 cards.", 8, player.getDeck().size());
    }

    @Test
    public void testDrawUpToFourCards() {
        player.fillHand();
        assertEquals("Hand should have 4 cards after drawing.", 4, player.getHand().size());
    }

    @Test
    public void draw2Cards() {
        player.fillHand();
        player.discardHand();
        player.fillHand();
        // deck empty
        player.drawCards(2);
        assertEquals("Hand should have 4 cards after drawing.", 6, player.getHand().size());
    }

    @Test
    public void testShuffleDeck() {
        // Collect card types before shuffle
        var expectedTypes = player.getDeck().stream().map(Card::getType).collect(Collectors.toList());
        player.shuffleDeck();
        // Collect card types after shuffle to see if same elements exist
        var shuffledTypes = player.getDeck().stream().map(Card::getType).collect(Collectors.toList());
        assertTrue("Shuffled deck should contain the same cards as before.",
                expectedTypes.size() == shuffledTypes.size() && expectedTypes.containsAll(shuffledTypes)
                        && shuffledTypes.containsAll(expectedTypes));
    }

    @Test
    public void testPlayCard() {
        player.fillHand();
        Card playedCard = player.getHand().get(0);
        player.playCard(0);
        assertFalse("Played card should not be in hand.", player.getHand().contains(playedCard));
        assertTrue("Played card should be in discard pile.", player.getDiscardPile().contains(playedCard));
    }

    @Test
    public void testPlayToken() {
        Token token = new Token(TokenTypes.JUNGLE1, player);
        player.addToken(token);
        Token playedToken = player.getToken().get(0);
        player.playToken(0);
        assertFalse("Played token should not be in hand.", player.getToken().contains(playedToken));
    }

    @Test
    public void testReshuffleWhenDeckEmpty() {
        player.fillHand(); // Draw first four cards
        player.discardHand(); // Discard those four cards
        player.fillHand(); // Draw next four cards, deck now empty
        player.discardHand(); // Discard those four cards
        player.fillHand(); // Should trigger reshuffle
        assertEquals("Hand should have 4 cards after reshuffling.", 4, player.getHand().size());
    }

    @Test
    public void testDiscardHand() {
        player.fillHand();
        player.discardHand();
        assertEquals("Cards in hand must be 0.", 0, player.getHand().size());
    }

    @Test
    public void testDiscardCard() {
        player.fillHand();
        player.discardCard(Collections.singletonList(player.getHand().get(0)));
        assertEquals("Cards in hand must be 3.", 3, player.getHand().size());
    }

    @Test
    public void testToDiscardPile() {
        player.fillHand();
        player.toDiscardedPile(Collections.singletonList(player.getHand().get(0)));
        assertEquals("Cards in hand must be 3.", 3, player.getHand().size());
    }

    @Test
    public void testGetDiscardedPile() {
        player.fillHand();
        player.discardCard(Collections.singletonList(player.getHand().get(0)));
        assertNotNull("Success.", player.getDiscardPile());
        assertEquals("Get discard pile should contain 1 card.", 1, player.getDiscardPile().size());
    }

    @Test
    public void testGetRemovedPile() {
        assertNotNull("Success.", player.getRemovedPile());
        assertEquals("Get initial removed pile should contain 0 card.", 0, player.getRemovedPile().size());
    }

    @Test
    public void testToken() {
        Token token = new Token(TokenTypes.JUNGLE1, player);
        player.addToken(token);
        assertEquals("Player will have 1 token.", 1, player.getToken().size());
    }
}
