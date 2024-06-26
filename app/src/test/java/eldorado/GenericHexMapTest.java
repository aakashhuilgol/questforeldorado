package eldorado;

import static org.junit.Assert.*;
import eldorado.models.GenericHexMap;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

public class GenericHexMapTest {

  @Test
  public void testGetTile() {
    GenericHexMap<String> map = new GenericHexMap<>(3, 3, String.class);
    map.map[0][0] = "Foo";
    map.map[0][1] = "Bar";
    map.map[1][1] = "Baz";
    map.map[2][0] = "Bax";
    map.map[2][1] = "Bam";
    map.map[2][2] = "Bum";

    assertEquals("Foo", map.getTile(0, 0));
    assertEquals("Bar", map.getTile(0, 1));
    assertEquals("Baz", map.getTile(1, 1));
    assertEquals("Bax", map.getTile(2, 0));
    assertEquals("Bam", map.getTile(2, 1));
    assertEquals("Bum", map.getTile(2, 2));

     // Test out of bounds
        assertThrows(IndexOutOfBoundsException.class, () -> map.getTile(-1, 0)); // q < 0
        assertThrows(IndexOutOfBoundsException.class, () -> map.getTile(3, 0)); // q >= this.size_q
        assertThrows(IndexOutOfBoundsException.class, () -> map.getTile(0, -1)); // r < 0
        assertThrows(IndexOutOfBoundsException.class, () -> map.getTile(0, 3)); // r >= this.size_r
        assertThrows(IndexOutOfBoundsException.class, () -> map.getTile(-1, -1)); // q < 0 and r < 0
        assertThrows(IndexOutOfBoundsException.class, () -> map.getTile(-1, 3)); // q < 0 and r >= this.size_r
        assertThrows(IndexOutOfBoundsException.class, () -> map.getTile(3, -1)); // q >= this.size_q and r < 0
        assertThrows(IndexOutOfBoundsException.class, () -> map.getTile(3, 3)); // q >= this.size_q and r >= this.size_r
  }

  @Test
  public void testDistance() {
    GenericHexMap<String> map = new GenericHexMap<>(3, 3, String.class);
    map.map[0][0] = "Foo";
    map.map[0][1] = "Bar";
    map.map[1][1] = "Baz";
    map.map[2][0] = "Bax";
    map.map[2][1] = "Bam";

    assertEquals(0, map.distance("Foo", "Foo"));
    assertEquals(0, map.distance("Bar", "Bar"));
    assertEquals(0, map.distance("Baz", "Baz"));
    assertEquals(1, map.distance("Foo", "Bar"));
    assertEquals(1, map.distance("Bar", "Foo"));
    assertEquals(1, map.distance("Bar", "Baz"));
    assertEquals(1, map.distance("Baz", "Bar"));
    assertEquals(2, map.distance("Foo", "Bax"));
    assertEquals(3, map.distance("Foo", "Bam"));

    assertThrows(IllegalArgumentException.class, () -> map.distance("aaaaa", "Bum"));
  }

  @Test
  public void testGetNeighbors() {
    GenericHexMap<String> map = new GenericHexMap<>(5, 5, String.class);
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        map.map[i][j] = i * 5 + j + "";
      }
    }
    String[] neighbors_array = map.getNeighbors(map.getTile(2, 2));
    Set<String> neighbors = new HashSet<>(Arrays.asList(neighbors_array));
    assertEquals(6, neighbors.size());
    assertTrue(neighbors.contains("7"));
    assertTrue(neighbors.contains("8"));
    assertTrue(neighbors.contains("13"));
    assertTrue(neighbors.contains("11"));
    assertTrue(neighbors.contains("16"));
    assertTrue(neighbors.contains("17"));

    GenericHexMap<String> map2 = new GenericHexMap<>(1, 1, String.class);
    map2.map[0][0] = "Foo";
    String[] neighbors_array2 = map2.getNeighbors(map2.getTile(0, 0));
    assertEquals(6, neighbors_array2.length);
    for (int i = 0; i < 6; i++) {
      assertEquals(null, neighbors_array2[i]);
    }
  }

  @Test
  public void testAxialRound() {
    GenericHexMap<String> map = new GenericHexMap<>(3, 3, String.class);

    // Test case 1
    float q1 = 0.6f;
    float r1 = 0.2f;
    int[] expected1 = {1, 0};
    int[] result1 = map.axialRound(q1, r1);
    assertArrayEquals(expected1, result1);

    // Test case 2
    float q2 = 0.2f;
    float r2 = 0.6f;
    int[] expected2 = {0, 1};
    int[] result2 = map.axialRound(q2, r2);
    assertArrayEquals(expected2, result2);

    // Test case 3
    float q3 = 0.25f;
    float r3 = 0.25f;
    int[] expected3 = {0, 0};
    int[] result3 = map.axialRound(q3, r3);
    assertArrayEquals(expected3, result3);

    // Test case 9: qDiff > rDiff and qDiff > sDiff
    float q9 = 2.4f;
    float r9 = 1.2f;
    int[] expected9 = {3, 1};
    int[] result9 = map.axialRound(q9, r9);
    assertArrayEquals(expected9, result9);

    // Test case 10: rDiff > sDiff
    float q10 = 1.2f;
    float r10 = 2.4f;
    int[] expected10 = {1, 3};
    int[] result10 = map.axialRound(q10, r10);
    assertArrayEquals(expected10, result10);

    // Test case 11: sDiff > qDiff and sDiff > rDiff
    float q11 = 1.4f;
    float r11 = 1.4f;
    int[] expected11 = {1, 2};
    int[] result11 = map.axialRound(q11, r11);
    assertArrayEquals(expected11, result11);
  }
}