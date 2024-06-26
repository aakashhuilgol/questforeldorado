package eldorado.utils;

public enum CardTypes {
    SAILOR(1, 0, 0.5, TerrainTypes.SEA),
    EXPLORER(1, 0, 0.5, TerrainTypes.JUNGLE),
    TRAVELER(1, 0, 1.0, TerrainTypes.VILLAGE),
    CAPTAIN(3, 2, 0.5, TerrainTypes.SEA),
    CARTOGRAPHER(3, 2, 0.5, TerrainTypes.JUNGLE),
    COMPASS(0, 2, 0.5, null),
    SCIENTIST(0, 4, 0.5, null),
    TRAVELLOG(0, 3, 0.5, null),
    NATIVE(1, 5, 0.5, TerrainTypes.NATIVE),
    PIONEER(5, 5, 0.5, TerrainTypes.JUNGLE),
    GIANTMACHETE(6, 3, 0.5, TerrainTypes.JUNGLE),
    ADVENTURER(2, 4, 0.5, null),
    PROPPLANE(4, 4, 0.5, null),
    JOURNALIST(3, 3, 3, TerrainTypes.VILLAGE),
    MILLIONARE(4, 5, 4.0, TerrainTypes.VILLAGE),
    SCOUT(2, 1, 0.5, TerrainTypes.JUNGLE),
    TRAILBLAZER(3, 3, 0.5, TerrainTypes.JUNGLE),
    JACKOFALLTRADES(1, 2, 0.5, null),
    PHOTOGRAPHER(2, 2, 2, TerrainTypes.VILLAGE),
    TREASURECHEST(4, 3, 4, TerrainTypes.VILLAGE),
    TRANSMITTER(0, 4, 10, null);

    public final int movementValue;
    public final int cost;
    public final double value;
    public final TerrainTypes terrainType;

    CardTypes(int movementValue, int cost, double value, TerrainTypes terrainType) {
        this.movementValue = movementValue;
        this.cost = cost;
        this.value = value;
        this.terrainType = terrainType;
    }
}
