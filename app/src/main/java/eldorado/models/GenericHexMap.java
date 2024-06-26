package eldorado.models;

import java.lang.reflect.Array;

public class GenericHexMap<T> {
  public T[][] map;
  final public int size_q;
  final public int size_r;

  public GenericHexMap(int size_q, int size_r, Class<T> cls) {
    @SuppressWarnings("unchecked")
    T[][] newMap = (T[][])Array.newInstance(cls, size_q, size_r);
    this.map = newMap;
    this.size_q = size_q;
    this.size_r = size_r;
  }

  private int[] getCoordinates(T tile) throws IllegalArgumentException {

    for (int q = 0; q < this.size_q; q++) {
      for (int r = 0; r < this.size_r; r++) {
        if (this.map[q][r] == tile) {
          return new int[] {q, r, -q - r};
        }
      }
    }
    throw new IllegalArgumentException("Tile not found in the map");
  }

  public T getTile(int q, int r) throws IllegalArgumentException {
    if (q < 0 || q >= this.size_q || r < 0 || r >= this.size_r) {
      throw new IndexOutOfBoundsException("Coordinates out of bounds");
    };
    return this.map[q][r];
  }

  public int distance(T a, T b) {
    int[] a_coords = getCoordinates(a);
    int[] b_coords = getCoordinates(b);
    return (Math.abs(a_coords[0] - b_coords[0]) +
            Math.abs(a_coords[1] - b_coords[1]) +
            Math.abs(a_coords[2] - b_coords[2])) /
        2;
  }

  public T[] getNeighbors(T a) {
    int[] a_coords = getCoordinates(a);
    int q = a_coords[0];
    int r = a_coords[1];

    @SuppressWarnings("unchecked")
    T[] neighbors = (T[])Array.newInstance(a.getClass(), 6);
    if (q > 0) {
      neighbors[0] = this.map[q - 1][r];
    }
    if (q < this.size_q - 1) {
      neighbors[1] = this.map[q + 1][r];
    }
    if (r > 0) {
      neighbors[2] = this.map[q][r - 1];
    }
    if (r < this.size_r - 1) {
      neighbors[3] = this.map[q][r + 1];
    }
    if (q > 0 && r < this.size_r - 1) {
      neighbors[4] = this.map[q - 1][r + 1];
    }
    if (q < this.size_q - 1 && r > 0) {
      neighbors[5] = this.map[q + 1][r - 1];
    }
    return neighbors;
  }

  private int[] cubeRound(float q, float r, float s) {
    int qRounded = (int)Math.round(q);
    int rRounded = (int)Math.round(r);
    int sRounded = (int)Math.round(s);

    float qDiff = Math.abs(qRounded - q);
    float rDiff = Math.abs(rRounded - r);
    float sDiff = Math.abs(sRounded - s);

    if (qDiff > rDiff && qDiff > sDiff) {
      qRounded = -rRounded - sRounded;
    } else if (rDiff > sDiff) {
      rRounded = -qRounded - sRounded;
    } else {
      sRounded = -qRounded - rRounded;
    }

    return new int[] {qRounded, rRounded, sRounded};
  }

  public int[] axialRound(float q, float r) { 
    int[] results = cubeRound(q, r, -q - r);
    return new int[] {results[0], results[1]};
  }
}