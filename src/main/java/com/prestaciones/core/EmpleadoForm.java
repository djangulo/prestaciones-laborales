package com.prestaciones.core;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import java.text.NumberFormat;
import java.text.ParseException;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;

import db.MySQL;
import models.Empleado;
import models.Empresa;
import prestaciones.Prestaciones;

public class EmpleadoForm {
    private MySQL db;
    private ActualizadorVistas actualizador; 
    private Empleado empleado = new Empleado();
    ArrayList<Empresa> empresas = new ArrayList<Empresa>();

    Label empresasLabel = new Label("Empresa:");
    ComboBox empresasInput = new ComboBox(FXCollections.observableArrayList(empresas));

    Label nombreLabel = new Label("Nombre:");
    final TextField nombreInput = new TextField();
    Text nombreError = new Text();

    Label apellidosLabel = new Label("Apellidos:");
    final TextField apellidosInput = new TextField();
    Text apellidosError = new Text();

    Label fechaDeContratacionLabel = new Label("Fecha de contratación:");
    final DatePicker fechaDeContratacionInput = new DatePicker(LocalDate.now());
    Text fechaDeContratacionError = new Text();

    Label salarioMensualLabel = new Label("Salario mensual:");
    final TextField salarioMensualInput = new TextField();
    Text salarioMensualError = new Text();

    Button someterBtn = new Button("Someter");
    HBox hbBtn = new HBox(10);
    Button limpiarBtn = new Button("Limpiar");
    Text formErrors = new Text();

    Text titulo = new Text();

    public EmpleadoForm(MySQL db, ActualizadorVistas actualizador) {
        if (getEmpleado() == null) {
            setEmpleado(new Empleado());
        }
        setDB(db);
        setActualizador(actualizador);
        

        try {
            setEmpresas(getDB().ListarEmpresas(100, 0));
        } catch(SQLException ex) {
            formErrors.setText(ex.toString());
        }
        empresasInput.setItems(FXCollections.observableArrayList(getEmpresas()));
        empresasInput.getSelectionModel().selectFirst();
        setIDEmpresa(getEmpresas().get(0).ID);

        empresasInput.setMaxWidth(200);

        actualizarTitulo();
        formErrors.setFill(Color.FIREBRICK);
        nombreError.setFill(Color.FIREBRICK);
        apellidosError.setFill(Color.FIREBRICK);
        fechaDeContratacionError.setFill(Color.FIREBRICK);
        salarioMensualError.setFill(Color.FIREBRICK);

        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);

        empresasInput.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                setIDEmpresa( ( (Empresa)empresasInput.getValue() ).ID );
            }
        });

        nombreInput.focusedProperty().addListener((arg0, oldValue, newValue)    -> {
            if (!newValue) { //when focus lost
                validarNombre();
            }
        });
        apellidosInput.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                validarApellidos();
            }
        });
        salarioMensualInput.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            if (!newValue) { //when focus lost
                validarSalario();
            }
        });

        fechaDeContratacionInput.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                setFechaDeContratacion( Date.valueOf(fechaDeContratacionInput.getValue()) );
            }
        });

        someterBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                validarNombre();
                validarApellidos();
                validarSalario();

                if (
                    nombreError.getText().length() == 0 &&
                    apellidosError.getText().length() == 0 &&
                    salarioMensualError.getText().length() == 0 &&
                    getIDEmpresa() != 0 ) {
                    try {
                        if (getEmpleado().ID == 0) {
                            setEmpleado(getDB().CrearEmpleado(getEmpleado()));
                        } else {
                            setEmpleado(getDB().ActualizarEmpleado(getID(), getEmpleado()));
                        }
                        actualizarTitulo();
                        actualizador.actualizar();
                        formErrors.setText("OK");
                    } catch(SQLException ex) {
                        formErrors.setText(ex.toString());
                    }
                }
            }
        });
        limpiarBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                resetForm();
            }
        });
    }

    public void validarSalario() {
        String msg = "";
        if (salarioMensualInput.getText().length() == 0) {
            msg = "Salario mensual: no puede estar vacío.";
        }
        
        String s = salarioMensualInput.getText().replaceAll("[^\\d,.]", "");
        salarioMensualInput.setText(s);

        if (msg.length() != 0) {
            salarioMensualError.setText(msg);
        } else {
            salarioMensualError.setText("");
            setSalarioMensual(Double.parseDouble(s.replaceAll(",", "")));
        }
        System.out.println(getSalarioMensual());
    }
    public void validarNombre() {
        String msg = "";
        if (nombreInput.getText().length() == 0) {
            msg = "Nombre: no puede estar vacío.";
        }
        if (nombreInput.getText().length() > 64) {
            msg = "Nombre: no puede exceder 64 caracteres.";
        }
        if (msg.length() != 0) {
            nombreError.setText(msg);
        } else {
            nombreError.setText("");
            setNombre(nombreInput.getText());
        }
    }

    public void validarApellidos() {
        String msg = "";
        if (apellidosInput.getText().length() == 0) {
            msg = "Apellidos: no puede estar vacío.";
        }
        if (apellidosInput.getText().length() > 127) {
            msg = "Apellidos: no puede exceder 127 caracteres.";
        }
        if (msg.length() != 0) {
            apellidosError.setText(msg);
        } else {
            apellidosError.setText("");
            setApellidos(apellidosInput.getText());
        }
    }

    public void actualizarTitulo() {
        titulo.setText(
            (getEmpleado().ID == 0 ? "Crear empleado" : String.format("Editar: %s", getEmpleado().toString()))
        );
    }


    public GridPane getForm() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        titulo.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(titulo, 0, 0, 3, 1);

        grid.add(empresasLabel, 0, 1);
        grid.add(empresasInput, 1, 1);

        grid.add(nombreLabel, 0, 2);
        grid.add(nombreInput, 1, 2);
        grid.add(nombreError, 1, 3);

        grid.add(apellidosLabel, 0, 4);
        grid.add(apellidosInput, 1, 4);
        grid.add(apellidosError, 1, 5);

        grid.add(salarioMensualLabel, 0, 6);
        grid.add(salarioMensualInput, 1, 6);
        grid.add(salarioMensualError, 1, 7);

        grid.add(fechaDeContratacionLabel, 0, 8);
        grid.add(fechaDeContratacionInput, 1, 8);

        grid.add(formErrors, 1, 9);

        hbBtn.getChildren().add(limpiarBtn);
        hbBtn.getChildren().add(someterBtn);
        grid.add(hbBtn, 1, 10);

        return grid;
    }

    private void resetForm() {
        nombreInput.setText("");
        nombreError.setText("");
        apellidosInput.setText("");
        apellidosError.setText("");
        salarioMensualInput.setText("");
        salarioMensualError.setText("");
        fechaDeContratacionInput.setValue(LocalDate.now());
        formErrors.setText("");
        actualizarTitulo();
    }

    public MySQL getDB() {
        return db;
    }
    public void setDB(MySQL db) {
        this.db = db;
    }
    public void setActualizador(ActualizadorVistas actualizador) {
        this.actualizador = actualizador;
    }
    

    public long getID() {
        return empleado.ID;
    }
    public void setID(long id) {
        this.empleado.ID = id;
    }
    public String getNombre() {
        return empleado.Nombre;
    }
    public void setNombre(String nombre) {
        this.empleado.Nombre = nombre;
    }
    public double getSalarioMensual() {
        return empleado.SalarioMensual;
    }
    public void setSalarioMensual(double salario) {
        this.empleado.SalarioMensual = salario;
    }
    public Date getFechaDeContratacion() {
        return empleado.FechaDeContratacion;
    }
    public void setFechaDeContratacion(Date fecha) {
        this.empleado.FechaDeContratacion = fecha;
    }
    public String getApellidos() {
        return empleado.Apellidos;
    }
    public void setApellidos(String apellidos) {
        this.empleado.Apellidos = apellidos;
    }
    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }
    public Empleado getEmpleado() {
        return empleado;
    }
    private ArrayList<Empresa> getEmpresas() {
        return empresas;
    }
    private void setEmpresas(ArrayList<Empresa> empresas){
        this.empresas = empresas;
    }
    public long getIDEmpresa() {
        return empleado.IDEmpresa;
    }
    public void setIDEmpresa(long idEmpresa) {
        this.empleado.IDEmpresa = idEmpresa;
    }
}