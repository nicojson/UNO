// Mazo.java
import java.util.*;

public class Mazo {
    private List<String> cartas;

    public Mazo() {
        cartas = generarMazo();
    }

    public void barajar() {
        Collections.shuffle(cartas);
    }

    public String robarCarta() {
        return cartas.remove(0);
    }

    public boolean estaVacio() {
        return cartas.isEmpty();
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
        for (int i = 0; i < 3; i++) mazo.add("+4");
        for (int i = 0; i < 2; i++) mazo.add("+6");
        for (int i = 0; i < 2; i++) mazo.add("+10");
        for (int i = 0; i < 3; i++) mazo.add("Uno No Mercy");
        for (int i = 0; i < 4; i++) mazo.add("Cambio de Color");
        return mazo;
    }
}
