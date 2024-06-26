package eldorado.gameui;

import javax.swing.*;

import eldorado.gamemanager.ElDoradoManager;
import eldorado.gamemanager.GameManager;
import eldorado.gamemanager.MarketManager;
import eldorado.gamemanager.listeners.ShopListener;
import eldorado.models.Card;
import eldorado.utils.PileTypes;
import eldorado.utils.Utils;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PileUI extends JPanel {
    public List<Card> shopCards;
    private Map<String, List<Card>> groupedCards;
    private GameManager controller = ElDoradoManager.getInstance();
    private MarketManager shop = MarketManager.getInstance();
    private ShopListener shopListener;
    public boolean interactionEnabled;

    public PileUI(List<Card> shopCards, HandUI handUI, boolean specialCardEffect, PileTypes pileType) {
        this.shopCards = shopCards;
        this.shopListener = handUI;
        switch (pileType) {
            case SHOP:
                groupedCards = groupCardsByType(shop.shopManagement().getShop());
                this.interactionEnabled = true;
                break;
            case STOCK:
                groupedCards = groupCardsByType(shop.stockManagement().getStock());
                this.interactionEnabled = false || specialCardEffect;
                break;
            case DISCARDED:
                groupedCards = groupCardsByType(controller.getDiscardPile(controller.getPlayerNumber()));
                this.interactionEnabled = false || specialCardEffect;
                break;
            default:
                groupedCards = groupCardsByType(controller.getRemovedPile(controller.getPlayerNumber()));
                this.interactionEnabled = false || specialCardEffect;
                break;
        }
    }

    public void showPopup() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Shop");
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        updateMainPanel(mainPanel, dialog);

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        dialog.add(scrollPane, BorderLayout.CENTER);

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }

    public void updateMainPanel(JPanel mainPanel, JDialog dialog) {
        mainPanel.removeAll();
        for (Map.Entry<String, List<Card>> entry : groupedCards.entrySet()) {
            String cardType = entry.getKey();
            List<Card> cards = entry.getValue();

            JPanel groupPanel = new JPanel();
            groupPanel.setLayout(new BorderLayout());
            JLabel titleLabel = new JLabel(cardType + " (" + cards.size() + ")");
            titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
            groupPanel.add(titleLabel, BorderLayout.NORTH);

            JPanel cardsPanel = new JPanel();
            cardsPanel.setLayout(new GridLayout(0, 4, 10, 10));
            for (Card card : cards) {
                JButton cardButton = new JButton();
                ImageIcon icon = Utils.getImageIcon(card.getType().toString().toLowerCase() + ".png");
                Image image = icon.getImage();
                Image newimg = image.getScaledInstance(100, 140, java.awt.Image.SCALE_SMOOTH);
                icon = new ImageIcon(newimg);
                cardButton.setIcon(icon);
                if (interactionEnabled) {
                    cardButton.addActionListener(e -> {
                        boolean success = controller.purchaseCards(shop,
                                ElDoradoManager.getInstance().getPlayerNumber(), shop.shopManagement().getShop().indexOf(card),
                                shopCards);
                        updateMainPanel(mainPanel, dialog);
                        mainPanel.revalidate();
                        mainPanel.repaint();
                        if (shopListener != null && success && dialog !=null) {

                            dialog.dispose();
                            shopListener.onShopClosed();
                        } else if (shopListener!=null && success) {
                            shopListener.onShopClosed();
                        }
                    });
                }
                cardsPanel.add(cardButton);
            }
            groupPanel.add(cardsPanel, BorderLayout.CENTER);
            mainPanel.add(groupPanel);
        }
    }

    public Map<String, List<Card>> groupCardsByType(List<Card> cards) {
        Map<String, List<Card>> groupedCards = new HashMap<>();
        for (Card card : cards) {
            String cardType = card.getType().toString();
            groupedCards.computeIfAbsent(cardType, k -> new java.util.ArrayList<>()).add(card);
        }
        return groupedCards;
    }
}