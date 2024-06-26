package eldorado;

import static org.junit.Assert.*;

import eldorado.gamemanager.PlayerManager;
import eldorado.models.Card;
import eldorado.models.Token;
import eldorado.utils.CardTypes;
import eldorado.utils.TerrainTypes;
import eldorado.utils.TokenTypes;
import org.junit.Before;
import org.junit.Test;

public class TokenTest {

  private Token token;
  private PlayerManager player;

  @Before
  public void setUp() {
    player = new PlayerManager();
    player.createInitialDeck();
  }

  @Test
  public void testGetMovementValue() {
    token = new Token(TokenTypes.JUNGLE3, player);
    assertEquals("Movement value should be initialized correctly.",
        TokenTypes.JUNGLE3.movementValue, token.getMovementValue());
  }

  @Test
  public void testGetTerrainType() {
    token = new Token(TokenTypes.JUNGLE3, player);
    assertEquals("Terrain type should be initialized correctly.",
        TokenTypes.JUNGLE3.terrainType, token.getTerrainType());
  }

  @Test
  public void testGetTokenType() {
    token = new Token(TokenTypes.JUNGLE3, player);
    assertEquals("Token type should be initialized correctly.",
        TokenTypes.JUNGLE3, token.getTokenType());
  }

  @Test
  public void testPlay_DrawCard() {
    player.getDeck().add(new Card(CardTypes.ADVENTURER, player));
    token = new Token(TokenTypes.DRAWCARD, player);
    int initialHandSize = player.getHand().size();
    token.play();
    assertEquals("Playing DRAWCARD token should draw 1 card.",
        initialHandSize + 1, player.getHand().size());
  }

  @Test
  public void testPlay_RemoveCard() {
    Card card = new Card(CardTypes.EXPLORER, player);
    player.getHand().add(card);
    int initialHandSize = player.getHand().size();
    token = new Token(TokenTypes.REMOVECARD, player);
    token.play();
    player.removeCards(player.getHand());
    assertEquals("Playing REMOVECARD token should remove 1 card from hand.", initialHandSize - 1,
        player.getHand().size());
    assertFalse("Removed card should not be in the hand.",
        player.getHand().contains(card));
  }

  @Test
  public void testPlay_ReplaceCards() {
    player.getHand().clear();
    player.getHand().add(new Card(CardTypes.EXPLORER, player));
    player.getHand().add(new Card(CardTypes.EXPLORER, player));
    player.getHand().add(new Card(CardTypes.EXPLORER, player));
    player.getHand().add(new Card(CardTypes.EXPLORER, player));
    player.getDeck().clear();
    player.getDeck().add(new Card(CardTypes.ADVENTURER, player));
    player.getDeck().add(new Card(CardTypes.ADVENTURER, player));
    player.getDeck().add(new Card(CardTypes.ADVENTURER, player));
    player.getDeck().add(new Card(CardTypes.ADVENTURER, player));
    token = new Token(TokenTypes.REPLACECARDS, player);
    token.play();
    player.discardCard(player.getHand());
    assertEquals("Playing REPLACECARDS token should draw the same number of cards they discard.", 4,
        player.getHand().size());
  }

  @Test
  public void testPlay_SaveItem() {
    Card card = new Card(CardTypes.PROPPLANE, player);
    player.getHand().add(card);
    token = new Token(TokenTypes.SAVEITEM, player);
    player.playCard(0);
    assertEquals("Playing PROPLANE token should remove it after play.", 1,
        player.getRemovedPile().size());
    player.getHand().add(card);
    token.play();
    player.playCard(0);
    assertEquals("Playing SAVEITEM token should not remove 1 card after play.", 1,
        player.getRemovedPile().size());
  }

  @Test
  public void testPlay_IgnoreOccupied() {
    token = new Token(TokenTypes.IGNOREOCCUPIED, player);
    token.play();
    assertEquals("Playing IGNOREOCCUPIED token will set flag to true.", true,
        player.ignoreOccupied);
  }

  @Test
  public void testPlay_NativeToken() {
    token = new Token(TokenTypes.NATIVETOKEN, player);
    token.play();
    assertEquals("Playing NATIVE token will set flag to true.", true,
        player.changeToNative);
    Card card = new Card(CardTypes.ADVENTURER, player);
    player.getHand().add(card);
    player.playCard(0);
    assertEquals("Playing NATIVE token will set terrain movement value NATIVE to 2.", 2,
        player.movement.getOrDefault(TerrainTypes.NATIVE, 0) + 0);
  }

  @Test
  public void testPlay_ChangeCardType() {
    token = new Token(TokenTypes.CHANGECARDTYPE, player);
    token.play();
    assertEquals("Playing CHANGETYPE token will set flag to true.", true,
        player.changeType);
    Card card = new Card(CardTypes.GIANTMACHETE, player);
    player.getHand().add(card);
    player.playCard(0);
    assertEquals("Playing CHANGETYPE token will set terrain movement value null to 6.", 6,
        player.movement.getOrDefault(null, 0) + 0);
  }
}
