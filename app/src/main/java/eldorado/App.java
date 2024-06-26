package eldorado;

import javax.swing.*;

import eldorado.gamemanager.CaveManager;
import eldorado.gamemanager.ElDoradoManager;
import eldorado.gamemanager.MarketManager;
import eldorado.gameui.MainGameFrame;
import eldorado.utils.json.MapReader;
import eldorado.utils.MapGenerator;
import eldorado.utils.HexMapBuilder;

public class App {
    private static boolean isOriginalMap = false;
    private static boolean isWithCaves = false;
    private static int numberOfPlayers = 2;

    public static void main(String[] args) {

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("ormap")) {
                System.out.println("Original map");
                isOriginalMap = true;
            }
            if (args[i].equals("caves")) {
                System.out.println("Playing with caves");
                isWithCaves = true;
            }
            if (args[i].equals("np1")) {
                System.out.println("One player");
                numberOfPlayers = 1;
            } else if (args[i].equals("np2")) {
                System.out.println("Two players");
                numberOfPlayers = 2;
            } else if (args[i].equals("np3")) {
                System.out.println("Three players");
                numberOfPlayers = 3;
            } else if (args[i].equals("np4")) {
                System.out.println("Four players");
                numberOfPlayers = 4;
            }
        }

        playGameSimulation();
    }

    private static void playGameSimulation() {
        CaveManager caveManager = CaveManager.getInstance();
        caveManager.createCave();
        HexMapBuilder hexMapBuilder = new HexMapBuilder();
        MapGenerator mapGenerator = new MapGenerator();
        MapReader mapReader = new MapReader();
        ElDoradoManager.initializeInstance(hexMapBuilder, mapGenerator, mapReader, isOriginalMap, isWithCaves);
        ElDoradoManager gameController = ElDoradoManager.getInstance();
        MarketManager market = MarketManager.getInstance();
        market.shopManagement().createShop();
        market.stockManagement().createStock();
        gameController.initializeGame(numberOfPlayers);

        SwingUtilities.invokeLater(() -> {
            MainGameFrame frame = new MainGameFrame(gameController, market);

            frame.setVisible(true);
        });
    }

}
