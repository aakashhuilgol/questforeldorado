package eldorado.gamemanager.shop;

import java.util.List;

import eldorado.models.Card;

public interface StockInterface {
    void createStock();
    List<Card> getStock();
    boolean removeCardsFromStock(List<Card> stocks);
}
