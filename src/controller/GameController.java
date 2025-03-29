// src/controller/GameController.java
package controller;

import model.Juego;
import model.Jugador;
import model.Mazo;

public class GameController {
    private Juego juego;

    public void jugarModoLocal() {
        Mazo mazo = new Mazo();
        mazo.barajar();

        Jugador jugador1 = new Jugador("Jugador 1");
        Jugador jugador2 = new Jugador("Jugador 2");

        for (int i = 0; i < 7; i++) {
            jugador1.agregarCarta(mazo.robarCarta());
            jugador2.agregarCarta(mazo.robarCarta());
        }

        juego = new Juego(mazo, jugador1, jugador2);
        juego.iniciarJuego();
    }

    public void jugarContraIA() {
        Mazo mazo = new Mazo();
        mazo.barajar();

        Jugador jugadorHumano = new Jugador("Jugador Humano");
        Jugador jugadorIA = new Jugador("Jugador IA");

        for (int i = 0; i < 7; i++) {
            jugadorHumano.agregarCarta(mazo.robarCarta());
            jugadorIA.agregarCarta(mazo.robarCarta());
        }

        juego = new Juego(mazo, jugadorHumano, jugadorIA);
        juego.iniciarJuego();
    }

    public Juego getJuego() {
        return juego;
    }
}