package interfaces;
import eldorado.models.HexMap;
import eldorado.utils.HexMapBuilder;
import eldorado.utils.json.MapConfiguration;

public interface IHexMapBuilder {
    HexMapBuilder setHexMap(HexMap hexMap);
    HexMapBuilder setSideLength(int sideLength);
    HexMapBuilder setConfigMap(MapConfiguration configMap);
    HexMap build();
    void buildInitialHex();
    void buildFirstRow();
    void buildRemainingRows();
    void buildHexInFirstColumn(int j);
    void buildHexInRemainingColumns(int j);
}
