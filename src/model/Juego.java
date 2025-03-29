// src/model/Juego.java
package model;

import java.util.List;

public class Juego {
    private Mazo mazo;
    private List<Jugador> jugadores;
    private String cartaActual;
    private int turnoActual;
    private int cartasAPescar;
    private boolean saltarTurno;
    private boolean reversa;

    public Juego(Mazo mazo, Jugador... jugadores) {
        this.mazo = mazo;
        this.jugadores = List.of(jugadores);
        this.turnoActual = 0;
        this.cartasAPescar = 0;
        this.saltarTurno = false;
        this.reversa = false;
    }

    public void iniciarJuego() {
        cartaActual = mazo.robarCarta();
    }

    public String getCartaActual() {
        return cartaActual;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    public int getTurnoActual() {
        return turnoActual;
    }

    public void siguienteTurno() {
        if (cartasAPescar > 0) {
            Jugador jugadorActual = jugadores.get(turnoActual);
            for (int i = 0; i < cartasAPescar; i++) {
                jugadorActual.agregarCarta(mazo.robarCarta());
            }
            cartasAPescar = 0;
        } else if (saltarTurno) {
            turnoActual = (turnoActual + (reversa ? -1 : 1) + jugadores.size()) % jugadores.size();
            saltarTurno = false;
        }
        turnoActual = (turnoActual + (reversa ? -1 : 1) + jugadores.size()) % jugadores.size();
    }

    public Mazo getMazo() {
        return mazo;
    }

    public void actualizarCartaActual(String nuevaCarta) {
        this.cartaActual = nuevaCarta;
        if (nuevaCarta.endsWith("+2")) {
            cartasAPescar = 2;
        } else if (nuevaCarta.endsWith("+4")) {
            cartasAPescar = 4;
        } else if (nuevaCarta.endsWith("Skip")) {
            saltarTurno = true;
        } else if (nuevaCarta.endsWith("Reverse")) {
            reversa = !reversa;
        }
    }

    public void actualizarColorActual(String nuevoColor) {
        this.cartaActual = nuevoColor + " " + cartaActual.split(" ")[1];
    }
}