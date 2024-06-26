package eldorado;

import eldorado.models.Card;
import eldorado.gamemanager.PlayerManager;
import eldorado.utils.CardTypes;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CardTest {
    private PlayerManager player;
    private Card card;

    @Before
    public void setup() {
        player = new PlayerManager();
        card = new Card(CardTypes.CARTOGRAPHER, player);
        player.createInitialDeck();
    }

    @Test
    public void testGetType() {
        assertEquals(CardTypes.CARTOGRAPHER, card.getType());
    }

    @Test
    public void testGetMovementValue() {
        assertEquals(CardTypes.CARTOGRAPHER.movementValue, card.getMovementValue());
    }

    @Test
    public void testGetValue() {
        assertEquals(CardTypes.CARTOGRAPHER.value, card.getValue(), 0.0);
    }

    @Test
    public void testGetTerrainType() {
        assertEquals(CardTypes.CARTOGRAPHER.terrainType, card.getTerrainType());
    }

    @Test
    public void testGetCost() {
        assertEquals(CardTypes.CARTOGRAPHER.cost, card.getCost(), 0.0);
    }

    @Test
    public void testPlayCard() {
        // remove all cards from players hand
        player.getHand().clear();
        card = new Card(CardTypes.CARTOGRAPHER, player);
        player.getHand().add(card);
        // get players hand
        player.getHand().get(0).playCard();
        // check if hand has a size of 3
        assertEquals(3, player.getHand().size());

        player.getHand().clear();
        card = new Card(CardTypes.GIANTMACHETE, player);
        player.getHand().add(card);
        player.getHand().get(0).playCard();
        // check if hand is empty
        assertEquals(0, player.getHand().size());

        player.getHand().clear();
        card = new Card(CardTypes.PROPPLANE, player);
        player.getHand().add(card);
        player.getHand().get(0).playCard();
        // check if hand is empty
        assertEquals(0, player.getHand().size());

        player.getHand().clear();
        card = new Card(CardTypes.COMPASS, player);
        player.getHand().add(card);
        player.getHand().get(0).playCard();
        // check if hand has a size of 3
        assertEquals(3, player.getHand().size());

        // player.getHand().clear();
        // card = new Card(CardTypes.TRAVELLOG, player);
        // player.getHand().add(card);
        // player.getHand().get(0).playCard();
        // // check if hand has a size of 1
        // assertEquals(1, player.getHand().size());

        // player.getHand().clear();
        // card = new Card(CardTypes.SCIENTIST, player);
        // player.getHand().add(card);
        // player.getHand().get(0).playCard();
        // // check if hand has a size of 1
        // assertEquals(1, player.getHand().size());

        // player.getHand().clear();
        // card = new Card(CardTypes.TREASURECHEST, player);
        // player.getHand().add(card);
        // player.getHand().get(0).playCard();
        // // check if hand is empty
        // assertEquals(0, player.getHand().size());

        // These shouldnt do anything special
        for (CardTypes cardType : CardTypes.values()) {
            if (cardType != CardTypes.CARTOGRAPHER && cardType != CardTypes.GIANTMACHETE && cardType != CardTypes.PROPPLANE && cardType != CardTypes.COMPASS && cardType != CardTypes.TREASURECHEST && cardType != CardTypes.TRAVELLOG && cardType != CardTypes.SCIENTIST) {
                player.getHand().clear();
                card = new Card(cardType, player);
                player.getHand().add(card);
                player.getHand().get(0).playCard();
                // check if hand has a size of 1
                assertEquals(1, player.getHand().size());
            }
        }
    }

    @Test
    public void testToString() {
        String expected = "Card{type='CARTOGRAPHER', movementValue='3', value=0.5}";
        assertEquals(expected, card.toString());
    }
}
