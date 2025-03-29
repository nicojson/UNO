// src/model/AiJugador.java
package model;

import java.util.List;

public class AiJugador extends Jugador {

    public AiJugador(String nombre) {
        super(nombre);
    }

    public String seleccionarCarta(String cartaActual) {
        List<String> mano = getMano();
        String mejorCarta = null;
        int mejorPrioridad = Integer.MIN_VALUE;

        for (String carta : mano) {
            int prioridad = calcularPrioridad(carta, cartaActual);
            if (prioridad > mejorPrioridad) {
                mejorPrioridad = prioridad;
                mejorCarta = carta;
            }
        }
        return mejorCarta;
    }

    private int calcularPrioridad(String carta, String cartaActual) {
        int prioridad = 0;

        // Prioritize matching color or number
        if (carta.startsWith(cartaActual.split(" ")[0]) || carta.endsWith(cartaActual.split(" ")[1])) {
            prioridad += 10;
        }

        // Prioritize special cards
        if (carta.contains("Cancelar Turno") || carta.contains("+2") || carta.contains("+4") || carta.contains("Cambio de Color")) {
            prioridad += 20;
        }

        // Adjust priority based on AI's hand size and card type
        if (carta.contains("Cambio de Color")) {
            prioridad += 5; // Slightly lower priority for wild cards
        }

        return prioridad;
    }
}
