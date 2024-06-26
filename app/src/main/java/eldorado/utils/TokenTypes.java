package eldorado.utils;

public enum TokenTypes {
  JUNGLE1(TerrainTypes.JUNGLE, 1),
  JUNGLE2(TerrainTypes.JUNGLE, 2),
  JUNGLE3(TerrainTypes.JUNGLE, 3),
  VILLAGE1(TerrainTypes.VILLAGE, 1),
  VILLAGE2(TerrainTypes.VILLAGE, 2),
  SEA1(TerrainTypes.SEA, 1),
  SEA2(TerrainTypes.SEA, 2),
  DRAWCARD(null, 0),
  REMOVECARD(null, 0),
  REPLACECARDS(null, 0),
  SAVEITEM(null, 0),
  IGNOREOCCUPIED(null, 0),
  NATIVETOKEN(null, 0),
  CHANGECARDTYPE(null, 0);

  public final TerrainTypes terrainType;
  public final int movementValue;

  TokenTypes(TerrainTypes terrainType, int movementValue) {
    this.terrainType = terrainType;
    this.movementValue = movementValue;
  }
}
