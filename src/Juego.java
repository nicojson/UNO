//// Juego.java
//import java.util.*;
//
//public class Juego {
//    private Scanner scanner;
//    private Mazo mazo;
//    private List<Jugador> jugadores;
//    private String cartaActual;
//    private int turno;
//
//    public Juego() {
//        scanner = new Scanner(System.in);
//        mazo = new Mazo();
//        jugadores = new ArrayList<>();
//        jugadores.add(new Jugador("Jugador 1"));
//        jugadores.add(new Jugador("Jugador 2"));
//        turno = 0;
//    }
//
//    public void iniciar() {
//        System.out.println("Bienvenido a UNO!");
//        System.out.println("Seleccione el modo de juego:");
//        System.out.println("1. Local (Dos jugadores)");
//        System.out.println("2. Contra la IA");
//
//        int opcion;
//        do {
//            System.out.print("Ingrese su opción (1 o 2): ");
//            opcion = scanner.nextInt();
//        } while (opcion != 1 && opcion != 2);
//
//        if (opcion == 1) {
//            jugarModoLocal();
//        } else {
//            jugarContraIA();
//        }
//    }
//
//    private void jugarModoLocal() {
//        mazo.barajar();
//        repartirCartas();
//
//        do {
//            cartaActual = mazo.robarCarta();
//        } while (cartaActual.startsWith("+") || cartaActual.equals("Uno No Mercy") || cartaActual.equals("Cambio de Color"));
//
//        System.out.println("Modo Local iniciado. Cada jugador tiene 7 cartas.");
//
//        while (true) {
//            Jugador jugadorActual = jugadores.get(turno);
//            System.out.println("\nTurno del " + jugadorActual.getNombre());
//            System.out.println("Carta en juego: " + cartaActual);
//
//            jugadorActual.mostrarCartas();
//            int eleccion = jugadorActual.elegirCarta(scanner, cartaActual, mazo);
//
//            if (eleccion == -1) {
//                System.out.println("¡" + jugadorActual.getNombre() + " ha ganado!");
//                break;
//            }
//
//            cartaActual = jugadorActual.jugarCarta(eleccion, cartaActual, mazo, scanner);
//            turno = (turno + 1) % jugadores.size();
//        }
//    }
//
//    private void repartirCartas() {
//        for (Jugador jugador : jugadores) {
//            for (int i = 0; i < 7; i++) {
//                jugador.agregarCarta(mazo.robarCarta());
//            }
//        }
//    }
//
//    private void jugarContraIA() {
//        System.out.println("Modo IA en desarrollo...");
//    }
//}