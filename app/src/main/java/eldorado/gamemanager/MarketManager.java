package eldorado.gamemanager;

import eldorado.gamemanager.shop.ShopInterface;
import eldorado.gamemanager.shop.ShopManagement;
import eldorado.gamemanager.shop.StockInterface;
import eldorado.gamemanager.shop.StockManagement;

public class MarketManager {
    private static MarketManager instance;

    private ShopInterface shop;
    private StockInterface stock;
    
    private MarketManager() {
        stock = new StockManagement();
        shop = new ShopManagement(stock);
    }

    public static MarketManager getInstance() {
        if (instance == null) {
            instance = new MarketManager();
        }
        return instance;
    }

    public ShopInterface shopManagement() {
        return shop;
    }

    public StockInterface stockManagement() {
        return stock;
    }
}
