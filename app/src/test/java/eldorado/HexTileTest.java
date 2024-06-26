package eldorado;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Arrays;

import eldorado.models.HexTile;

public class HexTileTest {

  @Test
  public void testHexTileConstructorWithPoints() {
    Integer[] xPoints = {0, 1, 2, 3, 4, 5};
    int[] xPoints_int = Arrays.stream(xPoints).mapToInt(Integer::intValue).toArray(); 
    Integer[] yPoints = {0, 1, 2, 3, 4, 5};
    int[] yPoints_int = Arrays.stream(yPoints).mapToInt(Integer::intValue).toArray();
    int sideLength = 10;

    HexTile hexTile = new HexTileImpl(xPoints, yPoints, sideLength);

    assertArrayEquals(xPoints_int, hexTile.xPoints);
    assertArrayEquals(yPoints_int, hexTile.yPoints);
    assertEquals(0, hexTile.x);
    assertEquals(-10, hexTile.y);
  }

  @Test
  public void testHexTileConstructorWithNullPoints() {
    Integer[] xPoints = {null, null, null, null, null, 5};
    Integer[] yPoints = {null, null, null, null, null, 5};
    int sideLength = 10;

    HexTile hexTile = new HexTileImpl(xPoints, yPoints, sideLength);

    int[] expectedXPoints = {14, 22, 22, 14, 6, 5};
    int[] expectedYPoints = {10, 5, -4, -10, -5, 5};
    assertArrayEquals(expectedXPoints, hexTile.xPoints);
    assertArrayEquals(expectedYPoints, hexTile.yPoints);
    assertEquals(14, hexTile.x);
    assertEquals(0, hexTile.y);
  }

  @Test
  public void testHexTileConstructorWithOnlyNullPoints() {
    Integer[] xPoints = {null, null, null, null, null, null};
    Integer[] yPoints = {null, null, null, null, null, null};
    int sideLength = 10;

    HexTile hexTile = new HexTileImpl(xPoints, yPoints, sideLength);

    for (int point : hexTile.xPoints) {
      System.err.println("xpoints: "+point);
    }
    for (int point : hexTile.yPoints) {
      System.err.println("ypoints: "+point);
    }

    int[] expectedXPoints = {0, 8, 8, 0, -8, -8};
    int[] expectedYPoints = {10, 5, -4, -10, -5, 5};
    assertArrayEquals(expectedXPoints, hexTile.xPoints);
    assertArrayEquals(expectedYPoints, hexTile.yPoints);
    assertEquals(0, hexTile.x);
    assertEquals(0, hexTile.y);
  }

  private static class HexTileImpl extends HexTile {
    public HexTileImpl(Integer[] xPoints, Integer[] yPoints, int sideLength) {
      super(xPoints, yPoints, sideLength);
    }
  }
}