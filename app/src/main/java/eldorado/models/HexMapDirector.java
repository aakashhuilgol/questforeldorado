package eldorado.models;

import eldorado.utils.HexMapBuilder;
import eldorado.utils.json.MapConfiguration;

public class HexMapDirector {
    private HexMapBuilder builder;

    public HexMapDirector(HexMapBuilder builder) {
        this.builder = builder;
    }

    public HexMap construct(HexMap hexMap, int sideLength, MapConfiguration gameMap) {
        return builder.setHexMap(hexMap)
                      .setSideLength(sideLength)
                      .setConfigMap(gameMap)
                      .build();
    }
}
