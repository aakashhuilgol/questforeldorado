package interfaces;
import eldorado.models.HexMap;
import eldorado.utils.json.MapConfiguration;

public interface IHexMapBuilder {
    public static void buildHexMap(HexMap hexMap, int size, MapConfiguration gameMap){};
}
