// src/Main.java
import controller.GameController;
import view.MainFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameController gameController = new GameController();
            MainFrame frame = new MainFrame(gameController);
            frame.setVisible(true);
        });
    }
}
