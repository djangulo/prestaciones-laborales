package models;

import java.sql.Date;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;

public class Empleado {
	public long ID;
	public long IDEmpresa;
	public String Nombre;
	public String Apellidos;
	public Date FechaDeContratacion;
	public double SalarioMensual;
	public Empresa empresa;
	private BooleanProperty checked = new SimpleBooleanProperty(false);

	public Empleado(){
		empresa = new Empresa();
	}
	
	public Empleado(long idEmpresa, String nombre, String apellidos, Date fechaContratacion, double salario) {
		IDEmpresa = idEmpresa;
		Nombre = nombre;
		Apellidos = apellidos;
		FechaDeContratacion = fechaContratacion;
		SalarioMensual = salario;
		empresa = new Empresa();
	}
	public String toString() {
        return String.format("%s %s (ID: %d)", Nombre, Apellidos, ID);
	}
	public String getRNCEmpresa() {
		return empresa.RNC;
	}
	public String getNombreEmpresa() {
		return empresa.Nombre;
	}

	public ObservableBooleanValue isChecked() {
		return checked;
	}
	
	public void setChecked(Boolean checked) {
		this.checked.set(checked);
	}
}
