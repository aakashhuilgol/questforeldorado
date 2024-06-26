package eldorado.gameui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import eldorado.gamemanager.ElDoradoManager;
import eldorado.gamemanager.GameManager;
import eldorado.gamemanager.listeners.ShopListener;
import eldorado.models.Card;
import eldorado.models.Token;
import eldorado.utils.PileTypes;

public class HandUI extends JPanel implements ShopListener {
    private List<Card> hand;
    private List<Token> tokenHand;
    private Set<Integer> selectedCards;
    private GameManager controller = ElDoradoManager.getInstance();

    public HandUI() {
        this.selectedCards = new HashSet<>();
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
    }

    public void setHand() {
        this.hand = controller.getHand(ElDoradoManager.getInstance().getPlayerNumber());
        this.tokenHand = controller.getToken(ElDoradoManager.getInstance().getPlayerNumber());
        removeAll();
        initializeUI();
        revalidate();
        repaint();
    }

    private void initializeUI() {
        for (int i = 0; i < hand.size(); i++) {
            Card card = hand.get(i);
            JButton cardButton = UIFactory.createButton(
                    card,
                    i,
                    e -> toggleCardSelection(Integer.parseInt(e.getActionCommand()), (JButton) e.getSource()),
                    new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (SwingUtilities.isRightMouseButton(e)) {
                                JButton sourceButton = (JButton) e.getSource();
                                JPopupMenu popupMenu = setupContextMenu(sourceButton,
                                        Integer.parseInt(sourceButton.getActionCommand()));
                                popupMenu.show(e.getComponent(), e.getX(), e.getY());
                            }
                        }
                    }, null);

            if (selectedCards.contains(i)) {
                cardButton.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
            }
            add(cardButton);
        }
        for (int i = 0; i < tokenHand.size(); i++) {
            Token token = tokenHand.get(i);
            // Add token UI above card button
            JButton tokenButton = UIFactory.createButton(null, i, null, new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                        JButton sourceButton = (JButton) e.getSource();
                        JPopupMenu popupMenu = setupTokenContextMenu(sourceButton,
                                Integer.parseInt(sourceButton.getActionCommand()));
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }, token);

            add(tokenButton);
        }
    }

    public JPopupMenu setupTokenContextMenu(JButton tokenButton, int tokenIndex) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem play = new JMenuItem("Play");
        popupMenu.add(play);

        play.addActionListener(e -> {
            controller.playToken(ElDoradoManager.getInstance().getPlayerNumber(), tokenIndex);
            onShopClosed();
        });

        return popupMenu;
    }

    public JPopupMenu setupContextMenu(JButton cardButton, int cardIndex) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem remove = new JMenuItem("Remove");
        JMenuItem discard = new JMenuItem("Discard");
        JMenuItem buy = new JMenuItem("Buy");
        JMenuItem play = new JMenuItem("Play");
        popupMenu.add(play);
        popupMenu.add(buy);
        popupMenu.add(discard);
        popupMenu.add(remove);
        play.addActionListener(e -> {
          List<Card> selectedCardsList = getSelectedCardsList();
          controller.playCards(ElDoradoManager.getInstance().getPlayerNumber(),
                               hand.indexOf(selectedCardsList.getFirst()));
          onShopClosed();
        });
    
        discard.addActionListener(e -> {
          List<Card> selectedCardsList = getSelectedCardsList();
          controller.discardCards(ElDoradoManager.getInstance().getPlayerNumber(),
                                  selectedCardsList);
          onShopClosed();
        });
    
        buy.addActionListener(e -> {
          List<Card> selectedCardsList = getSelectedCardsList();
          PileUI shopsUI =
              new PileUI(selectedCardsList, this, true, PileTypes.SHOP);
    
          shopsUI.showPopup();
        });
    
        remove.addActionListener(e -> {
          List<Card> selectedCardsList = getSelectedCardsList();
          controller.removeCards(ElDoradoManager.getInstance().getPlayerNumber(), selectedCardsList);
          onShopClosed();
        });
        return popupMenu;
      }
    

    private List<Card> getSelectedCardsList() {
        return selectedCards.stream()
                .map(index -> hand.get(index))
                .collect(Collectors.toList());
    }

    public void toggleCardSelection(int index, JButton button) {
        if (selectedCards.contains(index)) {
            selectedCards.remove(index);
            button.setBorder(BorderFactory.createEmptyBorder());
        } else {
            selectedCards.add(index);
            button.setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
        }
    }

    public Set<Integer> getSelectedCards() {
        return selectedCards;
    }

    @Override
    public void onShopClosed() {
        setHand();
        selectedCards.clear();
        clearSelections();
    }

    public void clearSelections() {
        Component[] components = getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                ((JButton) component).setBorder(BorderFactory.createEmptyBorder());
            }
        }
    }
}
