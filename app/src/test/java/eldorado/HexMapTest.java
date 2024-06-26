package eldorado;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;

import eldorado.models.HexMap;

public class HexMapTest {
    private HexMap hexMap;

    @Before
    public void setUp() {
        hexMap = new HexMap(10, 10, 5);
    }

    @Test
    public void testPixelToHexCoordinates() {
        int[] expectedCoordinates = { 0, 0 };
        int[] actualCoordinates = hexMap.pixelToHexCoordinates(0, 0);
        assertArrayEquals("Incorrect hex coordinates for pixel (0, 0).", expectedCoordinates, actualCoordinates);

        expectedCoordinates = new int[] { 13, 5};
        actualCoordinates = hexMap.pixelToHexCoordinates(100, 100);
        assertArrayEquals("Incorrect hex coordinates for pixel (10, 10).", expectedCoordinates, actualCoordinates);

        expectedCoordinates = new int[] { 11, 52 };
        actualCoordinates = hexMap.pixelToHexCoordinates(500, 80);
        assertArrayEquals("Incorrect hex coordinates for pixel (-10, 0).", expectedCoordinates, actualCoordinates);

        expectedCoordinates = new int[] { 27, 10};
        actualCoordinates = hexMap.pixelToHexCoordinates(200, 200);
        assertArrayEquals("Incorrect hex coordinates for pixel (0, -10).", expectedCoordinates, actualCoordinates);
    }
}
