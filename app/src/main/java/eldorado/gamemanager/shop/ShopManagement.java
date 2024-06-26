package eldorado.gamemanager.shop;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import eldorado.models.Card;
import eldorado.utils.CardTypes;

public class ShopManagement implements ShopInterface {
    private List<Card> shop = new ArrayList<>();
    private StockInterface stock;

    public ShopManagement(StockInterface stock) {
        this.stock = stock;
    }

    @Override
    public void createShop() {
        shop.clear();
        addCardToShop(new Card(CardTypes.SCOUT, null));
        addCardToShop(new Card(CardTypes.TRAILBLAZER, null));
        addCardToShop(new Card(CardTypes.TRANSMITTER, null));
        addCardToShop(new Card(CardTypes.TREASURECHEST, null));
        addCardToShop(new Card(CardTypes.PHOTOGRAPHER, null));
        addCardToShop(new Card(CardTypes.JACKOFALLTRADES, null));
    }

    @Override
    public List<Card> getShop() {
        return new ArrayList<>(shop);
    }

    @Override
    public Card buyCard(int cardIndex) {
        if (cardIndex >= 0 && cardIndex < shop.size()) {
            if (checkLastCard(cardIndex)) {
                Random rand = new Random();
                int randomElement = rand.nextInt(stock.getStock().size());
                refillShop(randomElement);
            }
            return shop.remove(cardIndex);
        }
        return null;
    }

    @Override
    public boolean checkLastCard(int cardIndex) {
        CardTypes typeToCheck = shop.get(cardIndex).getType();
        long count = shop.stream()
                .filter(card -> card.getType().equals(typeToCheck))
                .count();
        return count == 1;
    }

    @Override
    public void refillShop(int cardIndex) {
        List<Card> toAdd = stock.getStock().stream()
                .filter(card -> card.getType() == stock.getStock().get(cardIndex).getType())
                .collect(Collectors.toList());
        shop.addAll(toAdd);
        stock.removeCardsFromStock(toAdd);
    }

    private void addCardToShop(Card card) {
        for (int i = 0; i < 3; i++) {
            shop.add(card);
        }
    }

}
