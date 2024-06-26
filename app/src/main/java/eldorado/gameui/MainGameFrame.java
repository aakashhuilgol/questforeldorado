package eldorado.gameui;

import eldorado.gamemanager.ElDoradoManager;
import eldorado.gamemanager.MarketManager;
import eldorado.models.HexMapRenderer;
import eldorado.utils.PileTypes;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MainGameFrame extends JFrame {
  private JInternalFrame mapLoc;
  private HexMapRenderer gui_map;
  public MainGameFrame(ElDoradoManager controller, MarketManager shop) {
    setTitle("El Dorado");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    setExtendedState(JFrame.MAXIMIZED_BOTH);
    setUndecorated(false);

    getContentPane().setLayout(new BorderLayout());

    mapArea();

    playerIndicatorUI();
    BottomUI();
    MarketUI(BorderLayout.WEST,
             new PileTypes[] {PileTypes.SHOP, PileTypes.STOCK});
    MarketUI(BorderLayout.EAST,
             new PileTypes[] {PileTypes.DISCARDED, PileTypes.REMOVED});
  }

  public void mapArea() {
    JPanel centerPanel = new JPanel(new BorderLayout());
    JInternalFrame mapLoc = new JInternalFrame();
    mapLoc.setVisible(true);
    mapLoc.setClosable(false);
    mapLoc.setLayout(null);
    mapLoc.setResizable(true);
    mapLoc.setTitle("Map");
    mapLoc.setLocation(200, 200);
    mapLoc.setSize(500, 500);
    var old_x = 0;
    var old_y = 0;
    if (gui_map != null) {
      old_x = gui_map.myX;
      old_y = gui_map.myY;
    }
    gui_map = new HexMapRenderer(ElDoradoManager.getInstance().hex_map, this);
    gui_map.myX = old_x;
    gui_map.myY = old_y;
    gui_map.setLocation(old_x, old_y);

    mapLoc.add(gui_map);
    mapLoc.repaint();
    centerPanel.add(mapLoc, BorderLayout.CENTER);
    getContentPane().add(centerPanel, BorderLayout.CENTER);
  }

  public void playerIndicatorUI() {
    JPanel topPanel = new JPanel(new BorderLayout());
    JLabel playerTurnLabel = new JLabel("", SwingConstants.CENTER);
    int playerNumber = ElDoradoManager.getInstance().getPlayerNumber() + 1;
    playerTurnLabel.setFont(new Font("Arial", Font.BOLD, 36));
    playerTurnLabel.setOpaque(true);
    playerTurnLabel.setBackground(Color.BLUE);
    playerTurnLabel.setForeground(Color.WHITE);

    playerTurnLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    playerTurnLabel.setText("Player " + playerNumber);
    topPanel.add(playerTurnLabel, BorderLayout.NORTH);
    getContentPane().add(topPanel, BorderLayout.NORTH);
  }

  public void BottomUI() {
    // End Turn button
    JButton endTurnButton = new JButton("End Turn");
    endTurnButton.setFont(new Font("Arial", Font.PLAIN, 24));
    endTurnButton.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    endTurnButton.setOpaque(true);
    endTurnButton.setBackground(Color.RED);

    endTurnButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        ElDoradoManager.getInstance().nextTurn();
        repaintUI();
      }
    });

    // GridLayout for draw and end button
    JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 10));
    buttonPanel.add(endTurnButton);

    // bottom panel for handUI and the previous grid
    JPanel bottomPanel = new JPanel(new BorderLayout());
    bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    // Grid right side
    bottomPanel.add(buttonPanel, BorderLayout.EAST);

    // handUI panel center
    HandUI handPanel = new HandUI();
    handPanel.setHand();
    bottomPanel.add(handPanel, BorderLayout.CENTER);

    // Add the bottom panel to the main panel
    getContentPane().add(bottomPanel, BorderLayout.SOUTH);
  }

  public void MarketUI(String position, PileTypes[] shopTypes) {
    JPanel pilePanel = new JPanel(new GridBagLayout());
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = 0;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.NONE;

    Dimension cardSize = new Dimension(100, 140);

    for (int i = 0; i < shopTypes.length; i++) {
      PileTypes shopType = shopTypes[i];
      JButton button = new JButton(shopType.name());
      button.setForeground(Color.BLUE);
      button.setPreferredSize(cardSize);
      button.setMinimumSize(cardSize);
      button.setMaximumSize(cardSize);

      button.addActionListener(e -> {
        PileUI shopUI = new PileUI(null, null, false, shopType);
        shopUI.showPopup();
      });

      gbc.gridy = i;
      pilePanel.add(button, gbc);
    }

    pilePanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
    add(pilePanel, position);
  }

  public void repaintUI() {
    getContentPane().removeAll();
    mapArea();
    playerIndicatorUI();
    BottomUI();
    MarketUI(BorderLayout.WEST,
             new PileTypes[] {PileTypes.SHOP, PileTypes.STOCK});
    MarketUI(BorderLayout.EAST,
             new PileTypes[] {PileTypes.DISCARDED, PileTypes.REMOVED});
    getContentPane().revalidate();
    getContentPane().repaint();
  }
}
