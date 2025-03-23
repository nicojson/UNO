// Carta.java
public class Carta {
    private String color;
    private String valor;

    public Carta(String color, String valor) {
        this.color = color;
        this.valor = valor;
    }

    public String getColor() {
        return color;
    }

    public String getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return color + " " + valor;
    }
}
