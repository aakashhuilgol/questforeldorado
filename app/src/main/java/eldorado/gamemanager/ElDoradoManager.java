package eldorado.gamemanager;

import eldorado.models.Card;
import eldorado.models.GameTile;
import eldorado.models.HexMap;
import eldorado.models.Token;
import eldorado.utils.CardTypes;
import eldorado.utils.HexMapBuilder;
import eldorado.utils.MapGenerator;
import eldorado.utils.TerrainTypes;
import eldorado.utils.json.BlockadeConfiguration;
import eldorado.utils.json.HexCoordinates;
import eldorado.utils.json.MapConfiguration;
import eldorado.utils.json.MapReader;
import interfaces.IHexMapBuilder;
import interfaces.IMapGenerator;
import interfaces.IMapReader;
import java.util.ArrayList;
import java.util.List;

public class ElDoradoManager extends GameManager {

    private static ElDoradoManager instance;
    private int currentPlayerIndex;
    public HexMap hex_map;

    private final IHexMapBuilder hexMapBuilder;
    private final IMapReader mapReader;
    private final IMapGenerator mapGenerator;
    public BlockadeConfiguration[] blockades;
    private MapConfiguration gameMap;
    private final boolean playingWithCaves;
    private boolean forceNextTurn;

    private ElDoradoManager(IHexMapBuilder hexMapBuilder,
            IMapGenerator mapGenerator, IMapReader mapReader,
            boolean isOriginalMap, boolean isWithCaves) {
        this.playingWithCaves = isWithCaves;
        this.hexMapBuilder = hexMapBuilder;
        this.mapReader = mapReader;
        this.mapGenerator = mapGenerator;
        this.forceNextTurn = false;
        this.currentPlayerIndex = 0;
        this.hex_map = new HexMap(35, 35, 50);
        int size = 50;

        String mapFilename = MapGenerator.generateMap(isOriginalMap);
        this.gameMap = MapReader.read(mapFilename, isOriginalMap);
        ((HexMapBuilder) hexMapBuilder)
                .setHexMap(this.hex_map)
                .setSideLength(size)
                .setConfigMap(gameMap)
                .build();
        blockades = gameMap.blockades;
    }

    public static ElDoradoManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException(
                    "Instance not initialized. Call initializeInstance first.");
        }
        return instance;
    }

    public static void initializeInstance(IHexMapBuilder hexMapBuilder,
            IMapGenerator mapGenerator,
            IMapReader mapReader,
            boolean isOriginalMap,
            boolean isWithCaves) {
        if (instance == null) {
            instance = new ElDoradoManager(hexMapBuilder, mapGenerator, mapReader,
                    isOriginalMap, isWithCaves);
        }
    }

    public void reset(boolean isOriginalMap) {
        instance = null;
        initializeInstance(hexMapBuilder, mapGenerator, mapReader, isOriginalMap, playingWithCaves);
    }

    @Override
    public boolean initializeGame(int numPlayers) {
        var startHexes = this.getStartHexes();
        for (int i = 0; i < numPlayers; i++) {
            PlayerManager player = new PlayerManager();
            player.createInitialDeck();
            player.currentHex = startHexes.get(i);
            player.fillHand();
            players.add(player);
        }
        return true;
    }

    private boolean isOccupied(GameTile hex) {
        for (PlayerManager player : players) {
            if (player.currentHex == hex) {
                return true;
            }
        }
        return false;
    }

    private boolean clickedBlockade(GameTile hex, HexCoordinates[] side1,
            HexCoordinates[] side2) {
        return _clickedBlockade(hex, side1, side2) |
                _clickedBlockade(hex, side2, side1);
    }

    private boolean _clickedBlockade(GameTile hex, HexCoordinates[] side1,
            HexCoordinates[] side2) {
        var player = players.get(currentPlayerIndex);
        for (HexCoordinates hexCoord : side1) {
            GameTile blockadeTile = hex_map.getTile(hexCoord.r, hexCoord.q);
            if (blockadeTile == hex) {
                System.out.println("Clicked on a barricade");
            }
            if (blockadeTile == player.currentHex) {
                for (HexCoordinates hexCoord2 : side2) {
                    GameTile blockadeTile2 = hex_map.getTile(hexCoord2.r, hexCoord2.q);
                    if (blockadeTile2 == hex) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void clickHex(GameTile hex) {

        var player = players.get(currentPlayerIndex);
        var playerhex = player.currentHex;
        if (hex.value == 0 | hex.type == TerrainTypes.EMPTY |
                hex.type == TerrainTypes.MOUNTAIN |
                (hex.type == TerrainTypes.CAVE && !this.playingWithCaves) |
                (isOccupied(hex) && !player.ignoreOccupied)) {
            return;
        }
        if (hex.type == TerrainTypes.BASECAMP) {
            if (player.getHand().size() >= hex.value) {
                player.movement.put(
                        TerrainTypes.BASECAMP,
                        player.movement.getOrDefault(TerrainTypes.BASECAMP, 0) - hex.value);
                player.currentHex = hex;
            }
            return;
        }

        for (BlockadeConfiguration blockade : this.blockades) {
            if (clickedBlockade(hex, blockade.side1, blockade.side2)) {
                if (player.movement.getOrDefault(blockade.type, 0) >= blockade.value) {
                    player.movement.put(blockade.type,
                            player.movement.get(blockade.type) -
                                    blockade.value);
                    this.gameMap.removeBlockade(blockade);
                    System.out.println("Removed Blockade");
                    this.blockades = gameMap.blockades;
                } else if (player.movement.getOrDefault(TerrainTypes.NATIVE, 0) >= 1) {
                    player.movement.put(TerrainTypes.NATIVE,
                            player.movement.get(TerrainTypes.NATIVE) - 1);
                    this.gameMap.removeBlockade(blockade);
                    this.blockades = gameMap.blockades;
                }
                return;
            }
        }

        if (hex_map.distance(playerhex, hex) == 1 &&
                player.movement.getOrDefault(hex.type, 0) >= hex.value) {
            player.currentHex = hex;
            player.movement.put(hex.type, player.movement.get(hex.type) - hex.value);
            player.hasMoved = true;
        } else if (hex_map.distance(playerhex, hex) == 1 &&
                player.movement.getOrDefault(null, 0) >= hex.value) {
            player.currentHex = hex;
            player.movement.put(null, player.movement.get(null) - hex.value);
            player.hasMoved = true;
        } else if (hex_map.distance(playerhex, hex) == 1 &&
                player.movement.getOrDefault(TerrainTypes.NATIVE, 0) == 1) {
        } else if (hex_map.distance(playerhex, hex) == 1 &&
                player.movement.getOrDefault(TerrainTypes.NATIVE, 0) == 1) {
            player.currentHex = hex;
            player.movement.put(TerrainTypes.NATIVE,
                    player.movement.get(TerrainTypes.NATIVE) - 1);
        } else if (hex_map.distance(playerhex, hex) == 1 &&
                hex.type == TerrainTypes.CAVE && hex.getCave() != null &&
                !player.hasGotToken) {
            this.givePlayerToken(hex.getToken());
            player.hasMoved = true;
            player.hasGotToken = true;
            this.forceNextTurn = true;
        }

        return;
    }

    public void checkCaveDistance() {
        var player = players.get(currentPlayerIndex);
        var playerhex = player.currentHex;
        boolean allDistancesGreaterThanOne = true;
        for (int r = 0; r < hex_map.size_r; r++) {
            for (int q = 0; q < hex_map.size_q; q++) {
                GameTile curHex = hex_map.map[q][r];
                if (hex_map.distance(playerhex, curHex) <= 1 &&
                        curHex.type == TerrainTypes.CAVE) {
                    allDistancesGreaterThanOne = false;
                }
            }
        }
        if (allDistancesGreaterThanOne) {
            player.hasGotToken = false;
        } else {
            if (player.hasGotToken) {
                player.hasGotToken = true;
            } else {
                player.hasGotToken = false;
            }
        }
    }

    public PlayerManager getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public int getPlayerNumber() {
        return currentPlayerIndex;
    }

    public void nextTurn() {
        if (!players.isEmpty() && validTurn()) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            PlayerManager player = players.get(currentPlayerIndex);
            player.movement.clear();
            player.hasMoved = false;
            player.fillHand();
            this.forceNextTurn = false;
        }
    }

    @Override
    public boolean playCards(int playerId, int cardIndex) {
        if (!canPlay()) {
            return false;
        }
        PlayerManager player = players.get(playerId);
        player.playCard(cardIndex);
        return true;
    }

    @Override
    public boolean playToken(int playerId, int tokenIndex) {
        if (!canPlay()) {
            return false;
        }
        PlayerManager player = players.get(playerId);
        player.playToken(tokenIndex);
        return true;
    }

    @Override
    public boolean discardHand(int playerId) {
        PlayerManager player = players.get(playerId);
        player.discardHand();
        return true;
    }

    @Override
    public boolean discardCards(int playerId, List<Card> discardedCards) {
        if (!canDiscard()) {
            return false;
        }
        PlayerManager player = players.get(playerId);
        player.discardCard(discardedCards);
        return true;
    }

    @Override
    public boolean removeCards(int playerId, List<Card> removedCards) {
        if (!canRemove()) {
            return false;
        }
        PlayerManager player = players.get(playerId);
        if (player.movement.getOrDefault(TerrainTypes.BASECAMP, 0) <= -removedCards.size()) {
            player.removeCards(removedCards);
            return true;
        }
        return false;
    }

    @Override
    public boolean shuffleDeck(int playerId) {
        PlayerManager player = players.get(playerId);
        player.shuffleDeck();
        return true;
    }

    @Override
    public boolean drawCards(int playerId) {
        PlayerManager player = players.get(playerId);
        player.fillHand();
        return true;
    }

    @Override
    public List<Card> getHand(int playerId) {
        PlayerManager player = players.get(playerId);
        return player.getHand();
    }

    @Override
    public List<Token> getToken(int playerId) {
        PlayerManager player = players.get(playerId);
        return player.getToken();
    }

    @Override
    public List<Card> getDeck(int playerId) {
        PlayerManager player = players.get(playerId);
        return player.getDeck();
    }

    @Override
    public List<Card> getDiscardPile(int playerId) {
        PlayerManager player = players.get(playerId);
        return player.getDiscardPile();
    }

    @Override
    public List<Card> getRemovedPile(int playerId) {
        PlayerManager player = players.get(playerId);
        return player.getRemovedPile();
    }

    @Override
    public boolean purchaseCards(MarketManager market, int playerId,
            int cardIndex, List<Card> paymentCards) {
        PlayerManager player = players.get(playerId);
        if (canBuy(market, cardIndex, paymentCards)) {
            // TODO: Fix this horrible mess, but it works for now
            List<Card> removeCards = new ArrayList<>();
            List<Card> discardCards = new ArrayList<>();
            for (Card paymentcard : paymentCards) {
                if (paymentcard.getType() == CardTypes.TREASURECHEST |
                        paymentcard.getType() == CardTypes.TRANSMITTER) {
                    removeCards.add(paymentcard);
                } else {
                    discardCards.add(paymentcard);
                }
            }
            if (player.saveItem) {
                player.toDiscardedPile(removeCards);
                player.saveItem = false;
            } else {
                player.removeCards(removeCards);
            }
            player.toDiscardedPile(discardCards);
            for (Card card : paymentCards) {
                player.getHand().remove(card);
            }

            this.forceNextTurn = true;
            return player.buyCard(market, cardIndex);
        } else {
            return false;
        }
    }

    private boolean canBuy(MarketManager market, int cardIndex,
            List<Card> paymentCards) {
        PlayerManager player = players.get(currentPlayerIndex);
        if (player.hasMoved | this.forceNextTurn) {
            return false;
        }
        Card cardToBuy = market.shopManagement().getShop().get(cardIndex);
        double totalPayment = 0.0;
        if (getCurrentPlayer().changeType) {
            totalPayment += paymentCards.get(0).getMovementValue();
            totalPayment += paymentCards.subList(1, paymentCards.size())
                    .stream()
                    .mapToDouble(Card::getCost)
                    .sum();
            getCurrentPlayer().changeType = false;
        } else {
            totalPayment = paymentCards.stream().mapToDouble(Card::getValue).sum();
        }
        return Double.compare(totalPayment, cardToBuy.getCost()) >= 0;
    }

    @Override
    public List<Card> getShop(MarketManager market) {
        return market.shopManagement().getShop();
    }

    @Override
    public List<Card> getStock(MarketManager market) {
        return market.stockManagement().getStock();
    }

    @Override
    public boolean refillShop(MarketManager market, int cardIndex) {
        market.shopManagement().refillShop(cardIndex);
        return true;
    }

    public void givePlayerToken(Token token) {
        PlayerManager player = players.get(currentPlayerIndex);
        player.addToken(token);
    }

    private boolean canPlay() {
        PlayerManager player = players.get(currentPlayerIndex);
        if (this.forceNextTurn) {
            return false;
        }
        if (player.movement.getOrDefault(TerrainTypes.BASECAMP, 0) != 0 |
                player.movement.getOrDefault(TerrainTypes.RUBBLE, 0) < 0) {
            System.out.println("Cant move");
            System.out.println(
                    player.movement.getOrDefault(TerrainTypes.BASECAMP, 0));
            System.out.println(player.movement.getOrDefault(TerrainTypes.RUBBLE, 0));
            return false;
        }
        System.out.println("Can move");
        return true;
    }

    private boolean canDiscard() {
        PlayerManager player = players.get(currentPlayerIndex);
        if (this.forceNextTurn) {
            return false;
        }
        if (player.movement.getOrDefault(TerrainTypes.BASECAMP, 0) != 0) {
            return false;
        }
        return true;
    }

    private boolean canRemove() {
        PlayerManager player = players.get(currentPlayerIndex);
        if (this.forceNextTurn) {
            return false;
        }
        if (player.movement.getOrDefault(TerrainTypes.BASECAMP, 0) < 0) {
            return true;
        }
        return false;
    }

    private boolean validTurn() {
        PlayerManager player = players.get(currentPlayerIndex);
        for (int value : player.movement.values()) {
            if (value < 0) {
                System.out.println("Invalid turn");
                return false;
            }
        }
        System.out.println("Valid turn");
        return true;
    }

    public List<GameTile> getStartHexes() {
        List<GameTile> startHexes = new ArrayList<>();
        for (GameTile[] hex_row : hex_map.map) {
            for (GameTile hex : hex_row) {
                if (hex == null) {
                    continue;
                } else if (hex.type == TerrainTypes.START) {
                    startHexes.add(hex);
                }
            }
        }
        return startHexes;
    }
}
