package eldorado.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import eldorado.App;
import interfaces.IMapGenerator;

public class MapGenerator implements IMapGenerator {
    public static String generateMap(boolean isOriginalMap) {

        InputStream inputStream;
        if (isOriginalMap) {
            inputStream = App.class.getResourceAsStream("/maps/map1.json");
        } else {
            inputStream = App.class.getResourceAsStream("/maps/map.json");
        }

        if (inputStream == null) {
            System.out.println("Resource not found: map");
            return "";
        }

        File outputFile = null;
        try {
            outputFile = new File("map.json");
            try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                System.out.println("Map generated at: " + outputFile.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return outputFile.getAbsolutePath();
    }
}
