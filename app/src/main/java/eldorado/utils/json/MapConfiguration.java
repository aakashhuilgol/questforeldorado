package eldorado.utils.json;
import eldorado.utils.TerrainTypes;

public class MapConfiguration {
  public HexConfiguration[] hexes;
  public BlockadeConfiguration[] blockades;

  public HexConfiguration getHex(int r, int q) {
    for (int i = 0; i < hexes.length; i++) {
      HexConfiguration curHex = hexes[i];
      if (curHex.q == q && curHex.r == r) {
        return curHex;
      }
    }
    HexConfiguration curHex = new HexConfiguration();
    curHex.q = q;
    curHex.r = r;
    curHex.type = TerrainTypes.EMPTY;
    return curHex;
  }

  public void removeBlockade(BlockadeConfiguration element) {
    BlockadeConfiguration[] newBlockades = new BlockadeConfiguration[blockades.length - 1];
    int newIndex = 0;
    for (BlockadeConfiguration blockade: blockades) {
      if (blockade != element) {
        newBlockades[newIndex++] = blockade;
      }
    }
    blockades = newBlockades;
  }
}
