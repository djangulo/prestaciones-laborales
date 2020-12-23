package models;

import java.sql.Date;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;

public class Empleado {
	private LongProperty id = new SimpleLongProperty(0);
	private LongProperty idEmpresa = new SimpleLongProperty(0);
	private StringProperty nombre = new SimpleStringProperty("");
	private StringProperty apellidos = new SimpleStringProperty("");
	private StringProperty fechaDeContratacion = new SimpleStringProperty("");
	private DoubleProperty salarioMensual = new SimpleDoubleProperty(0.0);
	private BooleanProperty checked = new SimpleBooleanProperty(false);
	private Empresa empresa = new Empresa();

	public Empleado() {
	}

	public Empleado(long idEmpresa, String nombre, String apellidos, Date fechaContratacion, double salarioMensual) {
		setIdEmpresa(idEmpresa);
		setNombre(nombre);
		setApellidos(apellidos);
		setFechaDeContratacion(fechaContratacion.toString());
		setSalarioMensual(salarioMensual);
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public String toString() {
		return String.format("%s %s (ID: %d)", getNombre(), getApellidos(), getId());
	}

	public String getRncEmpresa() {
		return empresa.getRnc();
	}

	public String getNombreEmpresa() {
		return empresa.getNombre();
	}

	public ObservableBooleanValue isChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked.set(checked);
	}

	public LongProperty idProperty() {
		return id;
	}

	public LongProperty idEmpresaProperty() {
		return idEmpresa;
	}

	public StringProperty nombreProperty() {
		return nombre;
	}

	public StringProperty apellidosProperty() {
		return apellidos;
	}

	public StringProperty fechaDeContratacionProperty() {
		return fechaDeContratacion;
	}

	public DoubleProperty salarioMensualProperty() {
		return salarioMensual;
	}

	public BooleanProperty checkedProperty() {
		return checked;
	}

	public void setId(long id) {
		this.id.set(id);
	}

	public void setIdEmpresa(long idEmpresa) {
		this.idEmpresa.set(idEmpresa);
	}

	public void setNombre(String nombre) {
		this.nombre.set(nombre);
	}

	public void setApellidos(String apellidos) {
		this.apellidos.set(apellidos);
	}

	public void setFechaDeContratacion(String fechaDeContratacion) {
		this.fechaDeContratacion.set(fechaDeContratacion);
	}

	public void setSalarioMensual(double salarioMensual) {
		this.salarioMensual.set(salarioMensual);
	}

	public void setChecked(boolean checked) {
		this.checked.set(checked);
	}

	public long getId() {
		return id.get();
	}

	public long getIdEmpresa() {
		return idEmpresa.get();
	}

	public String getNombre() {
		return nombre.get();
	}

	public String getApellidos() {
		return apellidos.get();
	}

	public String getFechaDeContratacion() {
		return fechaDeContratacion.get();
	}

	public double getSalarioMensual() {
		return salarioMensual.get();
	}

	public boolean getChecked() {
		return checked.get();
	}

}
