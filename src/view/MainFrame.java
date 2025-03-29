// src/view/MainFrame.java
package view;

import controller.GameController;
import model.Jugador;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame implements CardPanel.CardClickListener {
    private final GameController gameController;
    private final JButton localModeButton;
    private final JButton aiModeButton;
    private final JLabel currentCardLabel;
    private final JLabel playerNameLabel;
    private final CardPanel cardPanel;
    private final JButton drawCardButton;
    private final JPanel deckPanel;

    public MainFrame(GameController gameController) {
        this.gameController = gameController;
        setTitle("Juego de Cartas UNO");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        localModeButton = new JButton("Play Local Mode");
        aiModeButton = new JButton("Play Against AI");
        currentCardLabel = new JLabel("Current Card: ");
        playerNameLabel = new JLabel("Player: ");
        cardPanel = new CardPanel();
        cardPanel.setCardClickListener(this);
        drawCardButton = new JButton("Draw Card");
        deckPanel = new JPanel();
        deckPanel.setPreferredSize(new Dimension(100, 150));
        deckPanel.setBackground(Color.DARK_GRAY);
        deckPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        localModeButton.addActionListener(e -> {
            this.gameController.jugarModoLocal();
            actualizarInterfaz();
        });
        aiModeButton.addActionListener(e -> {
            this.gameController.jugarContraIA();
            actualizarInterfaz();
        });
        drawCardButton.addActionListener(e -> {
            Jugador jugadorActual = gameController.getJuego().getJugadores().get(gameController.getJuego().getTurnoActual());
            jugadorActual.robarCarta(gameController.getJuego().getMazo());
            actualizarInterfaz();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(localModeButton);
        buttonPanel.add(aiModeButton);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(currentCardLabel, BorderLayout.NORTH);
        centerPanel.add(deckPanel, BorderLayout.CENTER);
        centerPanel.add(drawCardButton, BorderLayout.SOUTH);

        add(buttonPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(playerNameLabel, BorderLayout.SOUTH);
        add(cardPanel, BorderLayout.EAST);
    }

    private void actualizarInterfaz() {
        currentCardLabel.setText("Current Card: " + gameController.getJuego().getCartaActual());
        Jugador jugadorActual = gameController.getJuego().getJugadores().get(gameController.getJuego().getTurnoActual());
        playerNameLabel.setText("Player: " + jugadorActual.getNombre());
        cardPanel.actualizarMano(jugadorActual.getMano());
    }

    @Override
    public void onCardClick(String carta) {
        Jugador jugadorActual = gameController.getJuego().getJugadores().get(gameController.getJuego().getTurnoActual());
        if (jugadorActual.puedeJugar(carta, gameController.getJuego().getCartaActual())) {
            jugadorActual.jugarCarta(jugadorActual.getMano().indexOf(carta), gameController.getJuego().getCartaActual(), gameController.getJuego().getMazo());
            if (carta.contains("Wild")) {
                String[] colores = {"Rojo", "Verde", "Azul", "Amarillo"};
                String nuevoColor = (String) JOptionPane.showInputDialog(this, "Elige un color:", "Cambio de Color", JOptionPane.QUESTION_MESSAGE, null, colores, colores[0]);
                gameController.getJuego().actualizarColorActual(nuevoColor);
            } else {
                gameController.getJuego().actualizarCartaActual(carta); // Update the current card
            }
            gameController.getJuego().siguienteTurno();
            actualizarInterfaz();
        } else {
            JOptionPane.showMessageDialog(this, "No puedes jugar esta carta.", "Movimiento inválido", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameController gameController = new GameController();
            MainFrame frame = new MainFrame(gameController);
            frame.setVisible(true);
        });
    }
}