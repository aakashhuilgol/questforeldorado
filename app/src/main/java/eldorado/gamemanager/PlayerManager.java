package eldorado.gamemanager;

import eldorado.models.Card;
import eldorado.models.GameTile;
import eldorado.utils.CardTypes;
import eldorado.utils.TerrainTypes;
import eldorado.models.Token;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {
  private List<Card> deck = new ArrayList<>();
  private List<Card> hand = new ArrayList<>();
  private List<Token> token = new ArrayList<>();
  public List<Card> discardPile = new ArrayList<>();
  private List<Card> removedPile = new ArrayList<>();
  public Map<TerrainTypes, Integer> movement = new HashMap<>();
  public GameTile currentHex;
  public boolean hasMoved = false;
  public boolean hasGotToken = false;
  // token flags
  public boolean saveItem = false;
  public boolean changeToNative = false;
  public boolean ignoreOccupied = false;
  public boolean changeType = false;
  public boolean replaceCards = false;

  public void createInitialDeck() {
    deck.add(new Card(CardTypes.SAILOR, this)); // 1 Sailor
    for (int i = 0; i < 3; i++)
      deck.add(new Card(CardTypes.EXPLORER, this)); // 3 Explorers
    for (int i = 0; i < 4; i++)
      deck.add(new Card(CardTypes.TRAVELER, this)); // 4 Travelers
    shuffleDeck();
  }

  public void shuffleDeck() {
    Collections.shuffle(deck);
  }

  public void drawCards(int amount) {
    for (int i = 0; i < amount; i++) {
      if (deck.isEmpty()) {
        deck.addAll(discardPile);
        discardPile.clear();
        shuffleDeck();
      }
      hand.add(deck.remove(0));
    }
  }

  public void fillHand() {
    while (hand.size() < 4) {
      if (deck.isEmpty()) {
        deck.addAll(discardPile);
        discardPile.clear();
        shuffleDeck();
      }
      hand.add(deck.remove(0));
    }
  }

  public void addToken(Token token) {
    this.token.add(token);
  }

  public Card[] selectCards(int amount) {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  public void playCard(int cardIndex) {
    if (cardIndex < hand.size()) {
      // Card playedCard = hand.remove(cardIndex);
      Card playedCard = hand.get(cardIndex);
      playedCard.playCard();
      var terrain = playedCard.getTerrainType();
      if (changeType) {
        terrain = null;
      }
      if (changeToNative) {
        movement.put(TerrainTypes.NATIVE, movement.getOrDefault(terrain, 0) +
            playedCard.getMovementValue());
        changeToNative = false;
      } else {
        movement.put(terrain, movement.getOrDefault(terrain, 0) +
            playedCard.getMovementValue());
      }

      if (hand.remove(playedCard)) {
        discardPile.add(playedCard);
      }
    }
  }

  public void playToken(int tokenIndex) {
    if (tokenIndex < token.size()) {
      // Card playedCard = hand.remove(cardIndex);
      Token playedToken = token.get(tokenIndex);
      playedToken.play();
      var terrain = playedToken.getTerrainType();
      movement.put(terrain, movement.getOrDefault(terrain, 0) +
          playedToken.getMovementValue());
      token.remove(playedToken);
    }
  }

  public void discardCard(List<Card> discardedCards) {
    List<Card> cardsToDiscard = new ArrayList<>(discardedCards);
    for (Card card : cardsToDiscard) {
      for (int i = 0; i < hand.size(); i++) {
        if (hand.get(i).getType() == card.getType()) {
          hand.remove(i);
          discardPile.add(card);
          this.movement.put(TerrainTypes.RUBBLE,
              this.movement.getOrDefault(TerrainTypes.RUBBLE, 0) +
                  1);
          if (replaceCards) {
            drawCards(1);
          }
          break;
        }
      }
    }
  }

  public void removeCards(List<Card> removedCards) {
    List<Card> cardsToRemove = new ArrayList<>(removedCards);
    for (Card card : cardsToRemove) {
      hand.remove(card);
      System.out.println("removed card");
      removedPile.add(card);
      this.movement.put(TerrainTypes.BASECAMP,
          this.movement.getOrDefault(TerrainTypes.BASECAMP, 0) + 1);
    }
  }

  public void discardHand() {
    discardPile.addAll(hand);
    hand.clear();
  }

  public void toDiscardedPile(List<Card> discardedCards) {
    for (Card card : discardedCards) {
      for (int i = 0; i < hand.size(); i++) {
        if (hand.get(i).getType() == card.getType()) {
          hand.remove(i);
          discardPile.add(card);
          break;
        }
      }
    }
  }

  public List<Card> getHand() {
    return hand;
  }

  public List<Token> getToken() {
    return token;
  }

  public List<Card> getDeck() {
    return deck;
  }

  public List<Card> getDiscardPile() {
    return discardPile;
  }

  public List<Card> getRemovedPile() {
    return removedPile;
  }

  public boolean buyCard(MarketManager market, int cardIndex) {
    Card purchasedCard = market.shopManagement().buyCard(cardIndex);
    if (purchasedCard != null) {
      purchasedCard.setPlayer(this);
      discardPile.add(purchasedCard);
      return true;
    }
    return false;
  }
}
