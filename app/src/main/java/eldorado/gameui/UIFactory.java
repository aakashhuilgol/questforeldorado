package eldorado.gameui;

import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import eldorado.models.Card;
import eldorado.models.Token;
import eldorado.utils.Utils;

public class UIFactory {
    public static JButton createButton(Card card, int index, ActionListener selectionListener,
            MouseListener contextMenuListener, Token token) {
        JButton cardButton = new JButton();
        ImageIcon icon = new ImageIcon();
        if (card != null) {
            icon = Utils.getImageIcon(card.getType().toString().toLowerCase() + ".png");
        } else {
            icon = Utils.getImageIcon(token.getTokenType().toString().toLowerCase() + ".png");
        }
        Image image = icon.getImage();
        Image newimg = image.getScaledInstance(100, 140, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(newimg);

        cardButton.setIcon(icon);
        cardButton.setActionCommand(String.valueOf(index));
        cardButton.setBorder(BorderFactory.createEmptyBorder());
        cardButton.addActionListener(selectionListener);
        cardButton.addMouseListener(contextMenuListener);

        return cardButton;
    }
}
