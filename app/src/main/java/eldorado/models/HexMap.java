package eldorado.models;

import java.awt.Point;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;

public class HexMap extends GenericHexMap<GameTile> {
  final public int size;

  public HexMap(int size_x, int size_y, int size) {
    super(size_x, size_y, GameTile.class);
    this.size = size;
  }

  public int[] pixelToHexCoordinates(int x, int y) {
    float q = (float)(2.0 / 3.0 * y) / size;
    float r = (float)(Math.sqrt(3.0) / 3.0 * x - 1.0 / 3.0 * y) / size;
    return this.axialRound(q, r);
  }

  public List<Point> getCommonVertices(GameTile hex1, GameTile hex2) {
    List<Point> commonVertices = new ArrayList<>();

    // Iterate through this hexagon's vertices
    for (int i = 0; i < 6; i++) {
      Point vertex1 = new Point(hex1.xPoints[i], hex1.yPoints[i]);

      // Iterate through the other hexagon's vertices
      for (int j = 0; j < 6; j++) {
        Point vertex2 = new Point(hex2.xPoints[j], hex2.yPoints[j]);

        // Check if the vertices are the same
        if (vertex1.equals(vertex2)) {
          commonVertices.add(vertex1);
          break; // No need to check further for this vertex
        }
      }
    }

    return commonVertices;
  }
}
