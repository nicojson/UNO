// src/view/CardPanel.java
package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CardPanel extends JPanel {
    private CardClickListener cardClickListener;

    public CardPanel() {
        setLayout(new GridBagLayout());
    }

    public void setCardClickListener(CardClickListener listener) {
        this.cardClickListener = listener;
    }

    public void actualizarMano(List<String> mano) {
        removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);
        for (String carta : mano) {
            JPanel cardPanel = createCardPanel(carta);
            add(cardPanel, gbc);
            gbc.gridx++;
            if (gbc.gridx == 5) { // Adjust the number of columns as needed
                gbc.gridx = 0;
                gbc.gridy++;
            }
        }
        revalidate();
        repaint();
    }

    private JPanel createCardPanel(String carta) {
        JPanel cardPanel = new JPanel();
        cardPanel.setPreferredSize(new Dimension(100, 150));
        cardPanel.setBackground(getCardColor(carta));
        cardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        cardPanel.add(new JLabel(carta));

        cardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (cardClickListener != null) {
                    cardClickListener.onCardClick(carta);
                }
            }
        });

        return cardPanel;
    }

    private Color getCardColor(String carta) {
        if (carta.contains("Rojo")) {
            return Color.RED;
        } else if (carta.contains("Azul")) {
            return Color.BLUE;
        } else if (carta.contains("Verde")) {
            return Color.GREEN;
        } else if (carta.contains("Amarillo")) {
            return Color.YELLOW;
        } else {
            return Color.GRAY;
        }
    }

    public interface CardClickListener {
        void onCardClick(String carta);
    }
}