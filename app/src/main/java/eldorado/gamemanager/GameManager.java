package eldorado.gamemanager;

import java.util.ArrayList;
import java.util.List;

import eldorado.models.Card;
import eldorado.models.Token;

public abstract class GameManager {
    public List<PlayerManager> players = new ArrayList<>();

    public abstract boolean initializeGame(int numPlayers);

    public abstract PlayerManager getCurrentPlayer();

    public abstract int getPlayerNumber();

    public abstract void nextTurn();

    public abstract boolean shuffleDeck(int playerId);

    public abstract boolean drawCards(int playerId);

    public abstract boolean playCards(int playerId, int cardIndex);

    public abstract boolean playToken(int playerId, int tokenIndex);

    public abstract boolean discardCards(int playerId, List<Card> discardedCards);

    public abstract boolean discardHand(int playerId);

    public abstract boolean removeCards(int playerId, List<Card> removedCards);

    public abstract List<Card> getHand(int playerId);

    public abstract List<Card> getDeck(int playerId);

    public abstract List<Token> getToken(int playerId);

    public abstract List<Card> getDiscardPile(int playerId);

    public abstract List<Card> getRemovedPile(int playerId);

    public abstract boolean purchaseCards(MarketManager market, int playerId, int cardIndex, List<Card> paymentCards);

    public abstract List<Card> getShop(MarketManager market);

    public abstract List<Card> getStock(MarketManager market);

    public abstract boolean refillShop(MarketManager market, int cardIndex);
}
