package eldorado.models;

import eldorado.gamemanager.ElDoradoManager;
import eldorado.gameui.MainGameFrame;
import eldorado.utils.TerrainTypes;
import eldorado.utils.json.BlockadeConfiguration;
import eldorado.utils.json.HexCoordinates;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.*;

public class HexMapRenderer
    extends JPanel implements MouseListener, MouseMotionListener {
  HexMap hexMap;
  MainGameFrame mainUI;
  private int screenX = 0;
  private int screenY = 0;
  public int myX = 0;
  public int myY = 0;
  private Font f = new Font("Comic Sans MS", Font.BOLD, 20);

  public HexMapRenderer(HexMap hexMap, MainGameFrame mainUI) {
    this.hexMap = hexMap;
    this.mainUI = mainUI;

    // TODO: Make this sizing adaptive
    setSize(10000, 10000);
    addMouseListener();
    addMouseMotionListener();
  }

  @Override
  public void paintComponent(Graphics g) {
    ElDoradoManager eldoradoManager = ElDoradoManager.getInstance();
    BlockadeConfiguration[] blockades = eldoradoManager.blockades;
    super.paintComponent(g);
    for (int r = 0; r < this.hexMap.size_r; r++) {
      for (int q = 0; q < this.hexMap.size_q; q++) {
        if (this.hexMap.map[q][r].type == TerrainTypes.EMPTY) {
          continue;
        }
        var hex = this.hexMap.map[q][r];
        setColorForHex(g, r, q);
        g.fillPolygon(hex.hexPoly);
        g.setColor(Color.BLACK);
        g.drawPolygon(hex.hexPoly);
        g.setFont(f);
        g.setColor(Color.WHITE);
        String s = String.valueOf(hex.value);
        g.drawString(s, hex.x, hex.y);
      }
    }
    Graphics2D g2d = (Graphics2D) g;
    g2d.setStroke(new BasicStroke(5));

    for (BlockadeConfiguration blockade : blockades) {
      int totalx = 0;
      int totaly = 0;
      int total = 0;
      List<List<Point>> listVertices = new ArrayList<List<Point>>();
      for (HexCoordinates blockadeHex1 : blockade.side1) {
        GameTile blockadeHexTile = hexMap.getTile(blockadeHex1.r, blockadeHex1.q);
        for (HexCoordinates blockadeHex2 : blockade.side2) {
          GameTile blockadeHexTile2 = hexMap.getTile(blockadeHex2.r, blockadeHex2.q);
          List<Point> vertices = hexMap.getCommonVertices(blockadeHexTile, blockadeHexTile2);
          if (vertices.size() == 2) {
            listVertices.add(vertices);
          }
          totalx += blockadeHexTile.x;
          totaly += blockadeHexTile.y;
          total++;
          totalx += blockadeHexTile2.x;
          totaly += blockadeHexTile2.y;
          total++;
        }
      }

      switch (blockade.type) {
        case TerrainTypes.JUNGLE:
          g2d.setColor(Color.GREEN);
          break;
        case TerrainTypes.VILLAGE:
          g2d.setColor(Color.YELLOW);
          break;
        case TerrainTypes.SEA:
          g2d.setColor(Color.BLUE);
          break;
        case TerrainTypes.RUBBLE:
          g2d.setColor(Color.GRAY);
          break;
        default:
          g2d.setColor(Color.WHITE);
          break;
      }

      for (List<Point> vertices : listVertices) {
        g2d.drawLine(vertices.get(0).x, vertices.get(0).y, vertices.get(1).x,
            vertices.get(1).y);
      }
      int x = totalx / total;
      int y = totaly / total;
      g.setColor(Color.BLACK);
      String s = String.valueOf(blockade.value);
      g.drawString(s, x, y);

    }
  }

  private void setColorForHex(Graphics g, int r, int q) {
    ElDoradoManager eldoradoManager = ElDoradoManager.getInstance();
    if (hexMap.map[q][r] == null |
        hexMap.map[q][r].type == TerrainTypes.EMPTY) {
      g.setColor(Color.WHITE);
      return;
    }

    for (int i = 0; i < eldoradoManager.players.size(); i++) {
      if (hexMap.map[q][r] == eldoradoManager.players.get(i).currentHex) {
        if (i == 0) {
          g.setColor(Color.PINK);
        } else if (i == 1) {
          g.setColor(Color.ORANGE);
        } else if (i == 2) {
          g.setColor(Color.MAGENTA);
        } else if (i == 3) {
          g.setColor(Color.CYAN);
        }
        return;
      }
    }

    GameTile curHex = hexMap.map[q][r];
    if (curHex == null) {
      g.setColor(Color.WHITE);
      return;
    }
    switch (curHex.type) {
      case TerrainTypes.JUNGLE:
        g.setColor(Color.GREEN);
        break;
      case TerrainTypes.VILLAGE:
        g.setColor(Color.YELLOW);
        break;
      case TerrainTypes.SEA:
        g.setColor(Color.BLUE);
        break;
      case TerrainTypes.RUBBLE:
        g.setColor(Color.GRAY);
        break;
      case TerrainTypes.BASECAMP:
        g.setColor(Color.RED);
        break;
      case TerrainTypes.MOUNTAIN:
        g.setColor(Color.BLACK);
        break;
      case TerrainTypes.CAVE:
        g.setColor(Color.DARK_GRAY);
        break;
      case TerrainTypes.START:
        g.setColor(Color.LIGHT_GRAY);
        break;
      case TerrainTypes.FINISH:
        g.setColor(Color.CYAN);
        break;
      default:
        g.setColor(Color.WHITE);
    }
  }

  public void mouseClicked(java.awt.event.MouseEvent e) {
    ElDoradoManager elDoradoManager = ElDoradoManager.getInstance();
    int[] coordinates = hexMap.pixelToHexCoordinates(
        e.getX() - hexMap.getTile(0, 0).x, e.getY() - hexMap.getTile(0, 0).y);
    System.out.println("Clicked on hex  " + coordinates[0] + " " +
        coordinates[1]);
    elDoradoManager.clickHex(hexMap.getTile(coordinates[0], coordinates[1]));
    elDoradoManager.checkCaveDistance();
    mainUI.repaintUI();
    return;
  }

  public void mouseEntered(java.awt.event.MouseEvent e) {
  }

  public void mouseExited(java.awt.event.MouseEvent e) {
  }

  public void mousePressed(java.awt.event.MouseEvent e) {
    screenX = e.getXOnScreen();
    screenY = e.getYOnScreen();
    myX = getX();
    myY = getY();
  }

  public void mouseReleased(java.awt.event.MouseEvent e) {
  }

  public void addMouseListener() {
    addMouseListener(this);
  }

  @Override
  public void mouseDragged(java.awt.event.MouseEvent e) {
    int deltaX = e.getXOnScreen() - screenX;
    int deltaY = e.getYOnScreen() - screenY;

    setLocation(myX + deltaX, myY + deltaY);
  }

  public void mouseMoved(java.awt.event.MouseEvent e) {
  }

  public void addMouseMotionListener() {
    addMouseMotionListener(this);
  }

  public void removeMouseMotionListener() {
    removeMouseMotionListener(this);
  }

  public void removeMouseListener() {
    removeMouseListener(this);
  }
}
