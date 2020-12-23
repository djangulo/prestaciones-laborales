package models;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Empresa
 */
public class Empresa {
    private LongProperty id = new SimpleLongProperty(0);
    private StringProperty nombre = new SimpleStringProperty("");
    private StringProperty rnc = new SimpleStringProperty("");

    public Empresa() {
    }

    public Empresa(String nombre, String rnc) {
        setRnc(rnc);
        setNombre(nombre);
    }

    public String toString() {
        return String.format("%s (RNC: %s)", getNombre(), getRnc());
    }

    public long getId() {
        return id.get();
    }

    public String getNombre() {
        return nombre.get();
    }

    public String getRnc() {
        return rnc.get();
    }

    public final void setId(long id) {
        this.id.set(id);
    }

    public final void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public final void setRnc(String rnc) {
        this.rnc.set(rnc);
    }

    public LongProperty idProperty() {
        return id;
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public StringProperty rncProperty() {
        return rnc;
    }

}