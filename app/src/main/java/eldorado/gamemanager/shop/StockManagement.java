package eldorado.gamemanager.shop;

import java.util.ArrayList;
import java.util.List;

import eldorado.models.Card;
import eldorado.utils.CardTypes;

public class StockManagement implements StockInterface {

    private List<Card> stock = new ArrayList<>();

    @Override
    public void createStock() {
        stock.clear();
        addCardToStock(new Card(CardTypes.CAPTAIN, null));
        addCardToStock(new Card(CardTypes.CARTOGRAPHER, null));
        addCardToStock(new Card(CardTypes.COMPASS, null));
        addCardToStock(new Card(CardTypes.SCIENTIST, null)); 
        addCardToStock(new Card(CardTypes.TRAVELLOG, null));
        addCardToStock(new Card(CardTypes.NATIVE, null));
        addCardToStock(new Card(CardTypes.PIONEER, null));
        addCardToStock(new Card(CardTypes.PROPPLANE, null));
        addCardToStock(new Card(CardTypes.GIANTMACHETE, null));
        addCardToStock(new Card(CardTypes.ADVENTURER, null));
        addCardToStock(new Card(CardTypes.JOURNALIST, null));
        addCardToStock(new Card(CardTypes.MILLIONARE, null));
    }

    @Override
    public List<Card> getStock() {
        return new ArrayList<>(stock);
    }

    private void addCardToStock(Card card) {
        for (int i = 0; i < 3; i++) {
            stock.add(card);
        }
    }

    @Override
    public boolean removeCardsFromStock(List<Card> stocks) {
        return stock.removeAll(stocks);
    }
}
