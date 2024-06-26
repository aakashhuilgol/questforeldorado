package eldorado.models;

import eldorado.gamemanager.PlayerManager;
import eldorado.utils.CardTypes;
import eldorado.utils.TerrainTypes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Card {
  private CardTypes type;
  private int movementValue;
  private double value;
  private TerrainTypes terrainType;
  private double cost;
  private HashMap<CardTypes, Runnable> cardActions;
  private PlayerManager player;

  public Card(CardTypes type, PlayerManager player) {
    this.type = type;
    this.movementValue = type.movementValue;
    this.value = type.value;
    this.terrainType = type.terrainType;
    this.cost = type.cost;
    this.cardActions = new HashMap<>();
    this.player = player;

    // Initialize with empty actions
    for (CardTypes cardType : CardTypes.values()) {
      cardActions.put(cardType, () -> {});
    }

    cardActions.put(CardTypes.CARTOGRAPHER, () -> {
      // Take 2 cards from your draw pile and play them this turn.
      this.player.drawCards(2);
    });

    cardActions.put(CardTypes.COMPASS, () -> {
      // YOu may draw 3 cards, this card is removed from the game after use.
      this.player.drawCards(3);
      checkSaveToken();
    });

    cardActions.put(CardTypes.SCIENTIST, () -> {
      // Implementation now is drawing a card and then be forced to remove 1 of
      // them If you had 0 next to this one you are then forced to remove then
      // one you drew
      this.player.movement.put(
          TerrainTypes.BASECAMP,
          player.movement.getOrDefault(TerrainTypes.BASECAMP, 0) - 1);
      this.player.drawCards(1);
    });

    cardActions.put(CardTypes.TRAVELLOG, () -> {
      // You may draw 2 cards and remove 2 cards from you hand, this card is
      // removed from the game after use.
      this.player.drawCards(2);
      this.player.movement.put(
          TerrainTypes.BASECAMP,
          player.movement.getOrDefault(TerrainTypes.BASECAMP, 0) - 2);
    });

    cardActions.put(CardTypes.GIANTMACHETE, () -> {
      // this card is removed from the game after use.
      checkSaveToken();
    });

    cardActions.put(CardTypes.PROPPLANE, () -> {
      // this card is removed from the game after use.
      checkSaveToken();
    });
  }

  // Getters
  public CardTypes getType() { return type; }

  public int getMovementValue() { return movementValue; }

  public double getValue() { return value; }

  public TerrainTypes getTerrainType() { return terrainType; }

  public double getCost() { return cost; }

  public void playCard() { cardActions.get(type).run(); }

  public void setPlayer(PlayerManager player) { this.player = player; }

  // For logging
  @Override
  public String toString() {
    return "Card{"
        + "type='" + type + '\'' + ", movementValue='" + movementValue + '\'' +
        ", value=" + value + '}';
  }

  private void checkSaveToken() {
    if (this.player.saveItem) {
      this.player.discardPile.add(this);
      this.player.saveItem = false;
    } else {
      List<Card> cardsToRemove = new ArrayList<>();
      cardsToRemove.add(this);
      this.player.removeCards(cardsToRemove);
    }
  }
}
