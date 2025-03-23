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

    public void agregarCarta(String carta) {
        mano.add(carta);
    }

    public void mostrarCartas() {
        System.out.println("Tus cartas:");
        for (int i = 0; i < mano.size(); i++) {
            System.out.println((i + 1) + ". " + mano.get(i));
        }
    }

    public int elegirCarta(Scanner scanner, String cartaActual, Mazo mazo) {
        while (true) {
            System.out.println("");
            System.out.print("Elige una carta para jugar o ingresa 0 para robar: ");
            int eleccion = scanner.nextInt();

            if (eleccion == 0) {
                while (true) {
                    if (!mazo.estaVacio()) {
                        String nuevaCarta = mazo.robarCarta();
                        mano.add(nuevaCarta);
                        System.out.println("Has robado: " + nuevaCarta);

                        if (puedeJugar(nuevaCarta, cartaActual)) {
                            System.out.println("Puedes jugar esta carta si lo deseas.");
                            break;
                        }

                        System.out.print("¿Quieres seguir robando? (s/n): ");
                        String respuesta = scanner.next();
                        if (respuesta.equalsIgnoreCase("n")) {
                            break;
                        }
                    } else {
                        System.out.println("No hay más cartas en el mazo.");
                        break;
                    }
                }
            } else if (eleccion > 0 && eleccion <= mano.size()) {
                String seleccionada = mano.get(eleccion - 1);
                if (puedeJugar(seleccionada, cartaActual)) {
                    return eleccion - 1;
                } else {
                    System.out.println("No puedes jugar esa carta. Debes jugar una carta válida.");
                }
            } else {
                System.out.println("Movimiento no válido.");
            }
        }
    }

    public String jugarCarta(int indice, String cartaActual, Mazo mazo, Scanner scanner) {
        String carta = mano.remove(indice);
        System.out.println("Has jugado: " + carta);

        if (mano.isEmpty()) {
            return "-1";
        }

        if (carta.contains("Cancelar Turno")) {
            System.out.println("El turno del oponente ha sido cancelado.");
        } else if (carta.equals("Cambio de Color") || carta.startsWith("+4") || carta.startsWith("+6") || carta.startsWith("+10")) {
            System.out.print("Elige un nuevo color (Rojo, Azul, Verde, Amarillo): ");
            String nuevoColor = scanner.next();
            carta = nuevoColor + " X";
        }

        return carta;
    }

    private boolean puedeJugar(String carta, String cartaActual) {
        if (carta.equals("Cambio de Color") || carta.equals("Uno No Mercy") || carta.startsWith("+")) {
            return true;
        }
        String[] partesCarta = carta.split(" ");
        String[] partesCartaActual = cartaActual.split(" ");
        return partesCarta[0].equals(partesCartaActual[0]) || partesCarta[1].equals(partesCartaActual[1]);
    }
}
