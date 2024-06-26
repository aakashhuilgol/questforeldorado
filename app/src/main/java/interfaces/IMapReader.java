package interfaces;
import eldorado.utils.json.MapConfiguration;

public interface IMapReader {
    static MapConfiguration read(String filename){
    return new MapConfiguration();
  };
}
