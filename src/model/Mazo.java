// src/model/Mazo.java
package model;

import java.util.*;

public class Mazo {
    private final List<String> cartas;

    public Mazo() {
        cartas = new ArrayList<>();
        // Initialize the deck with cards
        inicializarMazo();
    }

    private void inicializarMazo() {
        // Add cards to the deck
        String[] colores = {"Rojo", "Verde", "Azul", "Amarillo"};
        String[] valores = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "Cancelar Turno", "+2", "Cambio de Color"};

        for (String color : colores) {
            for (String valor : valores) {
                cartas.add(color + " " + valor);
            }
        }
        // Add special cards
        cartas.add("Uno No Mercy");
        cartas.add("+4");
        cartas.add("+6");
        cartas.add("+10");
    }

    public void barajar() {
        Collections.shuffle(cartas);
    }

    public String robarCarta() {
        if (!cartas.isEmpty()) {
            return cartas.remove(0);
        }
        return null;
    }

    public boolean estaVacio() {
        return cartas.isEmpty();
    }
}
