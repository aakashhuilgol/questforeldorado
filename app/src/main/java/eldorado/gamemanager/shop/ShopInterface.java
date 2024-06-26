package eldorado.gamemanager.shop;

import java.util.List;

import eldorado.models.Card;

public interface ShopInterface {
    void createShop();
    List<Card> getShop();
    Card buyCard(int cardIndex);
    boolean checkLastCard(int cardIndex);
    void refillShop(int cardIndex);
}
