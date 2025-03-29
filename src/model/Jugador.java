// src/model/Jugador.java
package model;

import javax.swing.*;
import java.util.*;

public class Jugador {
    private String nombre;
    private List<String> mano;

    public Jugador(String nombre) {
        this.nombre = nombre;
        this.mano = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<String> getMano() {
        return mano;
    }

    public void agregarCarta(String carta) {
        mano.add(carta);
    }

    public void mostrarCartas() {
        System.out.println("Tus cartas:");
        for (int i = 0; i < mano.size(); i++) {
            System.out.println((i + 1) + ". " + mano.get(i));
        }
    }

    public int elegirCarta(String cartaActual, Mazo mazo) {
        while (true) {
            String eleccionStr = JOptionPane.showInputDialog(null, "Elige una carta para jugar o ingresa 0 para robar:");
            int eleccion = Integer.parseInt(eleccionStr);

            if (eleccion == 0) {
                while (true) {
                    if (!mazo.estaVacio()) {
                        String nuevaCarta = mazo.robarCarta();
                        mano.add(nuevaCarta);
                        JOptionPane.showMessageDialog(null, "Has robado: " + nuevaCarta);

                        if (puedeJugar(nuevaCarta, cartaActual)) {
                            JOptionPane.showMessageDialog(null, "Puedes jugar esta carta si lo deseas.");
                            break;
                        }

                        int respuesta = JOptionPane.showConfirmDialog(null, "¿Quieres seguir robando?", "Continuar robando", JOptionPane.YES_NO_OPTION);
                        if (respuesta == JOptionPane.NO_OPTION) {
                            break;
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "No hay más cartas en el mazo.");
                        break;
                    }
                }
            } else if (eleccion > 0 && eleccion <= mano.size()) {
                String seleccionada = mano.get(eleccion - 1);
                if (puedeJugar(seleccionada, cartaActual)) {
                    return eleccion - 1;
                } else {
                    JOptionPane.showMessageDialog(null, "No puedes jugar esa carta. Debes jugar una carta válida.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Movimiento no válido.");
            }
        }
    }

    public String jugarCarta(int indice, String cartaActual, Mazo mazo) {
        String carta = mano.remove(indice);
        JOptionPane.showMessageDialog(null, "Has jugado: " + carta);

        if (mano.isEmpty()) {
            return "-1";
        }

        if (carta.contains("Cancelar Turno")) {
            JOptionPane.showMessageDialog(null, "El turno del oponente ha sido cancelado.");
        } else if (carta.equals("Cambio de Color") || carta.startsWith("+4") || carta.startsWith("+6") || carta.startsWith("+10")) {
            String[] colores = {"Rojo", "Azul", "Verde", "Amarillo"};
            String nuevoColor = (String) JOptionPane.showInputDialog(null, "Elige un nuevo color:", "Cambio de Color", JOptionPane.QUESTION_MESSAGE, null, colores, colores[0]);
            carta = nuevoColor + " X";
        }

        return carta;
    }

    public boolean puedeJugar(String carta, String cartaActual) {
        if (carta.equals("Cambio de Color") || carta.equals("Uno No Mercy") || carta.startsWith("+")) {
            return true;
        }
        String[] partesCarta = carta.split(" ");
        String[] partesCartaActual = cartaActual.split(" ");
        return partesCarta[0].equals(partesCartaActual[0]) || partesCarta[1].equals(partesCartaActual[1]);
    }

    public void robarCarta(Mazo mazo) {
        if (!mazo.estaVacio()) {
            String nuevaCarta = mazo.robarCarta();
            mano.add(nuevaCarta);
            JOptionPane.showMessageDialog(null, "Has robado: " + nuevaCarta);
        } else {
            JOptionPane.showMessageDialog(null, "No hay más cartas en el mazo.");
        }
    }
}