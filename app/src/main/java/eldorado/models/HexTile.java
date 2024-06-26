package eldorado.models;

import java.awt.*;

public abstract class HexTile {
  public final int[] xPoints;
  public final int[] yPoints;
  public final int x;
  public final int y;
  public final Polygon hexPoly;

  public HexTile(Integer[] _xPoints, Integer[] _yPoints, int sideLength) {
    Integer _xOffset = null;
    Integer _yOffset = null;
    xPoints = new int[6];
    yPoints = new int[6];

    // Find the offset of the hexagon and fill in the missing points
    // if they are not provided in the constructor arguments (null).
    for (int i = 0; i < 6; i++) {
      if (_xPoints[i] != null || _yPoints[i] != null) {
        xPoints[i] = _xPoints[i];
        yPoints[i] = _yPoints[i];
        if (_xOffset == null) {
          _xOffset = (int)Math.round(xPoints[i] -
                                     Math.sin(i * Math.PI / 3) * sideLength);
          _yOffset = (int)Math.round(yPoints[i] -
                                     Math.cos(i * Math.PI / 3) * sideLength);
        }
      }
    }
    x = _xOffset != null ? _xOffset : 0;
    y = _yOffset != null ? _yOffset : 0;

    for (int i = 0; i < 6; i++) {
      if (_xPoints[i] == null || _yPoints[i] == null) {
        xPoints[i] = x + (int)(Math.sin(i * Math.PI / 3) * sideLength);
        yPoints[i] = y + (int)(Math.cos(i * Math.PI / 3) * sideLength);
      }
    }
    hexPoly = new Polygon(xPoints, yPoints, 6);
  }
}
