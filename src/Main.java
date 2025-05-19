import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class Main extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel, gamePanel, centerPanel;
    private JLabel cartaActualLabel, jugadorActualLabel;
    private JTextArea logArea;
    private DefaultListModel<String> modeloJugador1, modeloJugador2;
    private JList<String> listaJugador1, listaJugador2;
    private JButton jugarBtn, robarBtn, menuBtn;
    private List<String> mazo, jugador1, jugador2;
    private String cartaActual, colorActual = "";
    private int turno = 1;
    private boolean modoIA = false;

    public Main() {
        setTitle("UNO - Juego de Cartas");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(menuPanel(), "Menu");
        mainPanel.add(initGamePanel(), "Juego");
        mainPanel.add(initIAStub(), "IA");

        add(mainPanel);
        setVisible(true);
    }

    private JPanel menuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(new Color(30, 30, 60));
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel title = new JLabel("Bienvenido a UNO!");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(Color.WHITE);

        JButton localBtn = new JButton("Jugar en Local");
        JButton iaBtn = new JButton("Jugar contra IA");

        styleButton(localBtn);
        styleButton(iaBtn);

        localBtn.addActionListener(e -> {
            modoIA = false;
            iniciarJuegoLocal();
        });
        iaBtn.addActionListener(e -> {
            modoIA = true;
            iniciarJuegoIA();
        });

        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(title, gbc);

        gbc.gridy = 1;
        panel.add(localBtn, gbc);

        gbc.gridy = 2;
        panel.add(iaBtn, gbc);

        return panel;
    }

    private JPanel initIAStub() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Modo IA", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private void iniciarJuegoLocal() {
        mazo = generarMazo();
        Collections.shuffle(mazo);
        jugador1 = new ArrayList<>(mazo.subList(0, 7));
        jugador2 = new ArrayList<>(mazo.subList(7, 14));
        mazo = new ArrayList<>(mazo.subList(14, mazo.size()));

        do {
            cartaActual = mazo.remove(0);
        } while (cartaActual.startsWith("+") || cartaActual.equals("Uno No Mercy") || cartaActual.equals("Cambio de Color"));

        colorActual = cartaActual.split(" ")[0];

        actualizarListas();
        cartaActualLabel.setText("Carta en juego: " + cartaActual);
        logArea.setText("Modo Local iniciado. Turno del Jugador 1.\n");
        turno = 1;
        jugadorActualLabel.setText("Turno del Jugador 1");
        cardLayout.show(mainPanel, "Juego");
    }

    private void iniciarJuegoIA() {
        mazo = generarMazo();
        Collections.shuffle(mazo);
        jugador1 = new ArrayList<>(mazo.subList(0, 7));
        jugador2 = new ArrayList<>(mazo.subList(7, 14));
        mazo = new ArrayList<>(mazo.subList(14, mazo.size()));

        do {
            cartaActual = mazo.remove(0);
        } while (cartaActual.startsWith("+") || cartaActual.equals("Uno No Mercy") || cartaActual.equals("Cambio de Color"));

        colorActual = cartaActual.split(" ")[0];

        actualizarListas();
        cartaActualLabel.setText("Carta en juego: " + cartaActual);
        logArea.setText("Modo IA iniciado. Turno del Jugador 1.\n");
        turno = 1;
        jugadorActualLabel.setText("Turno del Jugador 1");
        cardLayout.show(mainPanel, "Juego");
    }

    private void turnoIA() {
        if (!modoIA || turno != 2) return;

        Timer timer = new Timer(1000, e -> {
            List<String> cartasJugables = obtenerCartasJugables(jugador2, cartaActual, colorActual);

            if (!cartasJugables.isEmpty()) {
                String cartaAJugar = estrategiaIA(cartasJugables, jugador2);
                int index = jugador2.indexOf(cartaAJugar);
                listaJugador2.setSelectedIndex(index);
                jugarCartaIA(cartaAJugar);
            } else {
                robarCartaIA();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private List<String> obtenerCartasJugables(List<String> cartas, String cartaActual, String colorActual) {
        List<String> jugables = new ArrayList<>();
        for (String carta : cartas) {
            if (puedeJugar(carta, cartaActual)) {
                jugables.add(carta);
            }
        }
        return jugables;
    }

    private String estrategiaIA(List<String> cartasJugables, List<String> manoIA) {
        // Prioridad 1: Cartas especiales
        for (String carta : cartasJugables) {
            if (carta.equals("+4") || carta.equals("+6") || carta.equals("+10") ||
                    carta.equals("Uno No Mercy") || carta.equals("Cambio de Color")) {
                return carta;
            }
        }

        // Prioridad 2: Cartas del color más común
        Map<String, Integer> conteoColores = new HashMap<>();
        for (String carta : manoIA) {
            if (carta.contains(" ")) {
                String color = carta.split(" ")[0];
                conteoColores.put(color, conteoColores.getOrDefault(color, 0) + 1);
            }
        }

        String colorMasComun = colorActual;
        int max = 0;
        for (Map.Entry<String, Integer> entry : conteoColores.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                colorMasComun = entry.getKey();
            }
        }

        for (String carta : cartasJugables) {
            if (carta.startsWith(colorMasComun)) {
                return carta;
            }
        }

        return cartasJugables.get(0);
    }

    private void jugarCartaIA(String carta) {
        jugador2.remove(carta);
        modeloJugador2.removeElement(carta);
        cartaActual = carta;
        cartaActualLabel.setText("Carta en juego: " + cartaActual);
        logArea.append("La IA ha jugado: " + cartaActual + "\n");

        if (carta.equals("Uno No Mercy")) {
            String colorElegido = elegirColorIA(jugador2);
            colorActual = colorElegido;
            cartaActualLabel.setText("Carta en juego: " + carta + " - Color elegido: " + colorActual);

            int cartasRobadas = 0;
            boolean encontroColor = false;

            while (!mazo.isEmpty() && !encontroColor) {
                String cartaRobada = mazo.remove(0);
                jugador1.add(cartaRobada);
                modeloJugador1.addElement(cartaRobada);
                cartasRobadas++;

                if (cartaRobada.startsWith(colorElegido)) {
                    encontroColor = true;
                }
            }

            logArea.append("Has robado " + cartasRobadas + " cartas hasta obtener una de color " + colorElegido + ".\n");
        } else if (carta.equals("Cambio de Color") || carta.startsWith("+")) {
            String colorElegido = elegirColorIA(jugador2);
            colorActual = colorElegido;
            cartaActualLabel.setText("Carta en juego: " + carta + " - Color elegido: " + colorActual);

            if (carta.startsWith("+")) {
                int cantidad = Integer.parseInt(carta.substring(1));
                for (int i = 0; i < cantidad && !mazo.isEmpty(); i++) {
                    String cartaRobada = mazo.remove(0);
                    jugador1.add(cartaRobada);
                    modeloJugador1.addElement(cartaRobada);
                }
                logArea.append("Has robado " + cantidad + " cartas.\n");
            }
        } else {
            colorActual = carta.split(" ")[0];
        }

        if (jugador2.isEmpty()) {
            JOptionPane.showMessageDialog(this, "¡La IA ha ganado!", "Juego terminado", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(mainPanel, "Menu");
            return;
        }

        turno = 1;
        jugadorActualLabel.setText("Turno del Jugador 1");
        CardLayout cl = (CardLayout)(centerPanel.getLayout());
        cl.show(centerPanel, "1");
    }

    private String elegirColorIA(List<String> manoIA) {
        Map<String, Integer> conteoColores = new HashMap<>();
        for (String carta : manoIA) {
            if (carta.contains(" ")) {
                String color = carta.split(" ")[0];
                conteoColores.put(color, conteoColores.getOrDefault(color, 0) + 1);
            }
        }

        String colorElegido = "Rojo";
        int max = 0;
        for (Map.Entry<String, Integer> entry : conteoColores.entrySet()) {
            if (entry.getValue() > max) {
                max = entry.getValue();
                colorElegido = entry.getKey();
            }
        }
        return colorElegido;
    }

    private void robarCartaIA() {
        if (!mazo.isEmpty()) {
            String nuevaCarta = mazo.remove(0);
            jugador2.add(nuevaCarta);
            modeloJugador2.addElement(nuevaCarta);
            logArea.append("La IA ha robado una carta.\n");

            if (puedeJugar(nuevaCarta, cartaActual)) {
                Timer timer = new Timer(1000, e -> {
                    int index = jugador2.indexOf(nuevaCarta);
                    listaJugador2.setSelectedIndex(index);
                    jugarCartaIA(nuevaCarta);
                });
                timer.setRepeats(false);
                timer.start();
                return;
            }
        } else {
            logArea.append("La IA no pudo robar carta (mazo vacío).\n");
        }

        turno = 1;
        jugadorActualLabel.setText("Turno del Jugador 1");
        CardLayout cl = (CardLayout)(centerPanel.getLayout());
        cl.show(centerPanel, "1");
    }

    private JPanel initGamePanel() {
        gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBackground(new Color(25, 25, 45));

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setBackground(new Color(35, 35, 70));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        cartaActualLabel = new JLabel("Carta en juego: ", SwingConstants.CENTER);
        cartaActualLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        cartaActualLabel.setForeground(Color.WHITE);

        jugadorActualLabel = new JLabel("", SwingConstants.CENTER);
        jugadorActualLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        jugadorActualLabel.setForeground(new Color(255, 193, 7));

        topPanel.add(cartaActualLabel);
        topPanel.add(jugadorActualLabel);

        gamePanel.add(topPanel, BorderLayout.NORTH);

        modeloJugador1 = new DefaultListModel<>();
        modeloJugador2 = new DefaultListModel<>();
        listaJugador1 = new JList<>(modeloJugador1);
        listaJugador2 = new JList<>(modeloJugador2);

        listaJugador1.setFont(new Font("Consolas", Font.BOLD, 18));
        listaJugador1.setForeground(Color.WHITE);
        listaJugador1.setBackground(new Color(60, 60, 100));
        listaJugador1.setSelectionBackground(new Color(0, 150, 136));

        listaJugador2.setFont(new Font("Consolas", Font.BOLD, 18));
        listaJugador2.setForeground(Color.WHITE);
        listaJugador2.setBackground(new Color(60, 60, 100));
        listaJugador2.setSelectionBackground(new Color(0, 150, 136));

        JScrollPane scrollCartas1 = new JScrollPane(listaJugador1);
        JScrollPane scrollCartas2 = new JScrollPane(listaJugador2);

        scrollCartas1.setBorder(BorderFactory.createTitledBorder("Jugador 1"));
        scrollCartas2.setBorder(BorderFactory.createTitledBorder("IA"));

        centerPanel = new JPanel(new CardLayout());
        centerPanel.add(scrollCartas1, "1");
        centerPanel.add(scrollCartas2, "2");

        gamePanel.add(centerPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(35, 35, 70));

        jugarBtn = new JButton("Jugar Carta");
        robarBtn = new JButton("Robar Carta");
        menuBtn = new JButton("Menú Principal");

        styleButton(jugarBtn);
        styleButton(robarBtn);
        styleButton(menuBtn);

        controlPanel.add(jugarBtn);
        controlPanel.add(robarBtn);
        controlPanel.add(menuBtn);

        gamePanel.add(controlPanel, BorderLayout.SOUTH);

        logArea = new JTextArea(12, 24);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        logArea.setEditable(false);
        logArea.setBackground(new Color(50, 50, 70));
        logArea.setForeground(Color.GREEN);
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder("Registro del Juego"));

        gamePanel.add(logScroll, BorderLayout.EAST);

        jugarBtn.addActionListener(e -> {
            jugarCarta(centerPanel);
            if (modoIA && turno == 2) {
                turnoIA();
            }
        });
        robarBtn.addActionListener(e -> {
            robarCarta(centerPanel);
            if (modoIA && turno == 2) {
                turnoIA();
            }
        });
        menuBtn.addActionListener(e -> volverAlMenu());

        return gamePanel;
    }

    private void jugarCarta(JPanel centerPanel) {
        if (modoIA && turno == 2) return;

        DefaultListModel<String> modelo = (turno == 1) ? modeloJugador1 : modeloJugador2;
        List<String> jugador = (turno == 1) ? jugador1 : jugador2;
        JList<String> lista = (turno == 1) ? listaJugador1 : listaJugador2;

        int index = lista.getSelectedIndex();
        if (index == -1) return;
        String seleccionada = jugador.get(index);

        if (!puedeJugar(seleccionada, cartaActual)) {
            logArea.append("Jugador " + turno + " no puede jugar esa carta.\n");
            return;
        }

        jugador.remove(index);
        modelo.remove(index);
        cartaActual = seleccionada;
        cartaActualLabel.setText("Carta en juego: " + cartaActual);
        logArea.append("Jugador " + turno + " ha jugado: " + cartaActual + "\n");

        if (seleccionada.equals("Uno No Mercy")) {
            String[] opciones = {"Rojo", "Azul", "Verde", "Amarillo"};
            String colorElegido = (String) JOptionPane.showInputDialog(this, "Selecciona un color para 'No Mercy':", "No Mercy", JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
            colorActual = colorElegido;
            cartaActualLabel.setText("Carta en juego: " + seleccionada + " - Color elegido: " + colorActual);

            int cartasRobadas = 0;
            boolean encontroColor = false;

            while (!mazo.isEmpty() && !encontroColor) {
                String cartaRobada = mazo.remove(0);
                jugador2.add(cartaRobada);
                modeloJugador2.addElement(cartaRobada);
                cartasRobadas++;

                if (cartaRobada.startsWith(colorElegido)) {
                    encontroColor = true;
                }
            }

            logArea.append("La IA robó " + cartasRobadas + " cartas hasta obtener una de color " + colorElegido + ".\n");
        } else if (seleccionada.equals("Cambio de Color") || seleccionada.startsWith("+")) {
            String[] opciones = {"Rojo", "Azul", "Verde", "Amarillo"};
            colorActual = (String) JOptionPane.showInputDialog(this, "Selecciona un color:", "Cambio de Color", JOptionPane.PLAIN_MESSAGE, null, opciones, opciones[0]);
            cartaActualLabel.setText("Carta en juego: " + seleccionada + " - Color elegido: " + colorActual);

            if (seleccionada.startsWith("+")) {
                int cantidad = Integer.parseInt(seleccionada.substring(1));
                for (int i = 0; i < cantidad && !mazo.isEmpty(); i++) {
                    String cartaRobada = mazo.remove(0);
                    jugador2.add(cartaRobada);
                    modeloJugador2.addElement(cartaRobada);
                }
                logArea.append("La IA ha robado " + cantidad + " cartas.\n");
            }
        } else {
            colorActual = cartaActual.split(" ")[0];
        }

        if (jugador.isEmpty()) {
            JOptionPane.showMessageDialog(this, "¡Jugador " + turno + " ha ganado!", "Juego terminado", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(mainPanel, "Menu");
            return;
        }

        cambiarTurno(centerPanel);

        if (modoIA && turno == 2) {
            turnoIA();
        }
    }

    private void robarCarta(JPanel centerPanel) {
        if (!mazo.isEmpty()) {
            String nuevaCarta = mazo.remove(0);
            if (turno == 1) {
                jugador1.add(nuevaCarta);
                modeloJugador1.addElement(nuevaCarta);
            } else {
                jugador2.add(nuevaCarta);
                modeloJugador2.addElement(nuevaCarta);
            }
            logArea.append("Jugador " + turno + " robó: " + nuevaCarta + "\n");
        } else {
            logArea.append("No hay más cartas en el mazo.\n");
        }
    }

    private void cambiarTurno(JPanel centerPanel) {
        turno = (turno == 1) ? 2 : 1;
        jugadorActualLabel.setText(turno == 1 ? "Turno del Jugador 1" : "Turno de la IA");
        CardLayout cl = (CardLayout)(centerPanel.getLayout());
        cl.show(centerPanel, String.valueOf(turno));
    }

    private void volverAlMenu() {
        cardLayout.show(mainPanel, "Menu");
    }

    private void actualizarListas() {
        modeloJugador1.clear();
        modeloJugador2.clear();
        for (String carta : jugador1) modeloJugador1.addElement(carta);
        for (String carta : jugador2) modeloJugador2.addElement(carta);
    }

    private void styleButton(JButton button) {
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setBackground(new Color(255, 87, 34));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(220, 40));
    }

    private boolean puedeJugar(String carta, String cartaActual) {
        if (carta.equals("Cambio de Color") || carta.equals("Uno No Mercy") || carta.startsWith("+")) return true;

        String[] partesCarta = carta.split(" ");
        if (partesCarta.length < 2) return false;

        String colorCarta = partesCarta[0];
        String valorCarta = partesCarta[1];

        String[] partesCartaActual = cartaActual.split(" ");
        String colorCartaActual = (partesCartaActual.length >= 2) ? partesCartaActual[0] : "";

        if (colorCarta.equalsIgnoreCase(colorActual)) return true;

        if (partesCartaActual.length >= 2 && valorCarta.equals(partesCartaActual[1])) return true;

        return false;
    }

    private List<String> generarMazo() {
        List<String> mazo = new ArrayList<>();
        String[] colores = {"Rojo", "Azul", "Verde", "Amarillo"};
        String[] numeros = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

        for (String color : colores) {
            for (String numero : numeros) {
                mazo.add(color + " " + numero);
                mazo.add(color + " " + numero);
            }
        }

        for (String color : colores) {
            for (int i = 0; i < 2; i++) {
                mazo.add(color + " +4");
            }
        }

        for (int i = 0; i < 2; i++) mazo.add("+6");
        for (int i = 0; i < 2; i++) mazo.add("+10");
        for (int i = 0; i < 3; i++) mazo.add("Uno No Mercy");
        for (int i = 0; i < 4; i++) mazo.add("Cambio de Color");
        for (int i = 0; i < 2; i++) mazo.add("+4");

        return mazo;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}
