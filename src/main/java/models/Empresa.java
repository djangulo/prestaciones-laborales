package models;

/**
 * Empresa
 */
public class Empresa {
    public long ID;
    public String Nombre;
    public String RNC;

    public Empresa() {}

    public Empresa(String nombre, String rnc) {
        RNC = rnc;
        Nombre = nombre;
    }

    public String toString() {
        return String.format("%s (RNC: %s)", Nombre, RNC);
    }
}