package eldorado.utils;

import eldorado.models.HexMap;
import eldorado.gamemanager.CaveManager;
import eldorado.models.GameTile;
import eldorado.utils.json.MapConfiguration;
import eldorado.utils.json.HexConfiguration;
import interfaces.IHexMapBuilder;

public class HexMapBuilder implements IHexMapBuilder {
  // This piece of code is a bit complex, but it's just a way to build a hexagonal
  // map
  // The code builds a hexagonal map with the specified side length. It starts
  // with the
  // initial hexagon at the top left corner and then builds the rest of the map
  // row by
  // row. Each hexagon is built based on the hexagon above and to the left of it.

  private HexMap hexMap;
  private int sideLength;
  private MapConfiguration configMap;

  public HexMapBuilder setHexMap(HexMap hexMap) {
    this.hexMap = hexMap;
    return this;
  }

  public HexMapBuilder setSideLength(int sideLength) {
    this.sideLength = sideLength;
    return this;
  }

  public HexMapBuilder setConfigMap(MapConfiguration configMap) {
    this.configMap = configMap;
    return this;
  }

  public HexMap build() {
    buildInitialHex();
    buildFirstRow();
    buildRemainingRows();
    return hexMap;
  }

  private void buildInitialHex() {
    HexConfiguration hex = configMap.getHex(0, 0);
    hexMap.map[0][0] = new GameTile(
        new Integer[] { sideLength * 2, null, null, null, null, null },
        new Integer[] { sideLength * 2, null, null, null, null, null },
        sideLength, hex.type, hex.value).setToken(CaveManager.getInstance().getFourRandomTokens());
  }

  private void buildFirstRow() {
    for (int i = 1; i < hexMap.size_r; i++) {
      HexConfiguration hex = configMap.getHex(0, i);
      hexMap.map[0][i] = new GameTile(
          new Integer[] { null, null, null, null, null, hexMap.map[0][i - 1].xPoints[1] },
          new Integer[] { null, null, null, null, null, hexMap.map[0][i - 1].yPoints[1] },
          sideLength, hex.type, hex.value).setToken(CaveManager.getInstance().getFourRandomTokens());
    }
  }

  private void buildRemainingRows() {
    for (int j = 1; j < hexMap.size_q; j++) {
      buildHexInFirstColumn(j);
      buildHexInRemainingColumns(j);
    }
  }

  private void buildHexInFirstColumn(int j) {
    HexConfiguration hex = configMap.getHex(j, 0);
    hexMap.map[j][0] = new GameTile(
        new Integer[] { null, null, hexMap.map[j - 1][1].xPoints[0], hexMap.map[j - 1][0].xPoints[1],
            hexMap.map[j - 1][0].xPoints[0], null },
        new Integer[] { null, null, hexMap.map[j - 1][1].yPoints[0], hexMap.map[j - 1][0].yPoints[1],
            hexMap.map[j - 1][0].yPoints[0], null },
        sideLength, hex.type, hex.value).setToken(CaveManager.getInstance().getFourRandomTokens());
  }

  private void buildHexInRemainingColumns(int j) {
    for (int i = 1; i < hexMap.size_q; i++) {
      HexConfiguration hex = configMap.getHex(j, i);
      if (i == hexMap.size_q - 1) {
        hexMap.map[j][i] = new GameTile(
            new Integer[] { null, null, null, hexMap.map[j - 1][i].xPoints[1], hexMap.map[j - 1][i].xPoints[0],
                hexMap.map[j][i - 1].xPoints[1] },
            new Integer[] { null, null, null, hexMap.map[j - 1][i].yPoints[1], hexMap.map[j - 1][i].yPoints[0],
                hexMap.map[j][i - 1].yPoints[1] },
            sideLength, hex.type, hex.value).setToken(CaveManager.getInstance().getFourRandomTokens());
      } else {
        hexMap.map[j][i] = new GameTile(
            new Integer[] { null, null, hexMap.map[j - 1][i + 1].xPoints[0], hexMap.map[j - 1][i].xPoints[1],
                hexMap.map[j - 1][i].xPoints[0], hexMap.map[j][i - 1].xPoints[1] },
            new Integer[] { null, null, hexMap.map[j - 1][i + 1].yPoints[0], hexMap.map[j - 1][i].yPoints[1],
                hexMap.map[j - 1][i].yPoints[0], hexMap.map[j][i - 1].yPoints[1] },
            sideLength, hex.type, hex.value).setToken(CaveManager.getInstance().getFourRandomTokens());
      }
    }
  }
}
