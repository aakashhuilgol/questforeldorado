package eldorado.utils.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import eldorado.utils.TerrainTypes;
import eldorado.utils.json.twin.HexTypes;
import eldorado.utils.json.twin.MapConfigTwin;
import interfaces.IMapReader;
import java.io.File;
import java.util.Arrays;

public class MapReader implements IMapReader {
  public static MapConfiguration read(String filename, boolean isOriginalMap) {
    ObjectMapper mapper = new ObjectMapper();

    try {
      if (isOriginalMap) {
        MapConfiguration map =
            mapper.readValue(new File(filename), MapConfiguration.class);
        System.out.println("READING OLD MAP");
        return map;
      } else {
        MapConfigTwin mapTwin =
            mapper.readValue(new File(filename), MapConfigTwin.class);
        System.out.println("READING NEW MAP");

        return twinToOrigMap(mapTwin);
      }

    } catch (Exception e) {
      System.out.println(e);
    }

    return null;
  }

  private static MapConfiguration twinToOrigMap(MapConfigTwin mapTwin) {
    MapConfiguration map = new MapConfiguration();
    map.hexes = new HexConfiguration[mapTwin.hexes.length];
    for (int i = 0; i < mapTwin.hexes.length; i++) {
      HexConfiguration hex = new HexConfiguration();
      hex.q = mapTwin.hexes[i].q;
      hex.r = mapTwin.hexes[i].r;
      hex.value = mapTwin.hexes[i].power;
      hex.type = twinToOrigTypes(mapTwin.hexes[i].type);

      map.hexes[i] = hex;
    }

    for (HexCoordinates startingHex : mapTwin.startingPositions) {
      int q = startingHex.q;
      int r = startingHex.r;
      for (int i = 0; i < map.hexes.length; i++) {
        if (map.hexes[i].q == q && map.hexes[i].r == r) {
          map.hexes[i].type = TerrainTypes.START;
        }
      }
    }
    for (HexCoordinates finishHex : mapTwin.endingPositions) {
      int q = finishHex.q;
      int r = finishHex.r;
      for (int i = 0; i < map.hexes.length; i++) {
        if (map.hexes[i].q == q && map.hexes[i].r == r) {
          map.hexes[i].type = TerrainTypes.FINISH;
        }
      }
    }

    int min_q = 0;
    int min_r = 0;
    for (int i = 0; i < map.hexes.length; i++) {
      min_q = Math.min(min_q, map.hexes[i].q);
      min_r = Math.min(min_r, map.hexes[i].r);
    }
    for (int i = 0; i < map.hexes.length; i++) {
      map.hexes[i].q -= min_q;
      map.hexes[i].r -= min_r;
    }

    map.blockades = new BlockadeConfiguration[mapTwin.blockades.length];
    for (int i = 0; i < mapTwin.blockades.length; i++) {
      BlockadeConfiguration blockade = new BlockadeConfiguration();
      blockade.type = twinToOrigTypes(mapTwin.blockades[i].type);
      blockade.value = mapTwin.blockades[i].power;
      blockade.side1 = Arrays.copyOf(mapTwin.blockades[i].side1,
                                     mapTwin.blockades[i].side1.length);
      for (HexCoordinates hexCoord : blockade.side1) {
        hexCoord.q -= min_q;
        hexCoord.r -= min_r;
      }
      blockade.side2 = Arrays.copyOf(mapTwin.blockades[i].side2,
                                     mapTwin.blockades[i].side2.length);
      for (HexCoordinates hexCoord : blockade.side2) {
        hexCoord.q -= min_q;
        hexCoord.r -= min_r;

      }
      map.blockades[i] = blockade;
    }

    return map;
  }

  private static TerrainTypes twinToOrigTypes(HexTypes type) {
    switch (type) {
    case HexTypes.Blue:
      return TerrainTypes.SEA;
    case HexTypes.Yellow:
      return TerrainTypes.VILLAGE;
    case HexTypes.Green:
      return TerrainTypes.JUNGLE;
    case HexTypes.Grey:
      return TerrainTypes.RUBBLE;
    case HexTypes.Red:
      return TerrainTypes.BASECAMP;
    case HexTypes.Mountain:
      return TerrainTypes.MOUNTAIN;
    case HexTypes.Start:
      return TerrainTypes.START;
    case HexTypes.Finish:
      return TerrainTypes.FINISH;
    case HexTypes.Cave:
      return TerrainTypes.CAVE;
    default:
      return TerrainTypes.EMPTY;
    }
  }
}
