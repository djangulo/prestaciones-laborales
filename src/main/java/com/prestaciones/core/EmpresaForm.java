package com.prestaciones.core;

import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

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

public class EmpresaForm {
    private MySQL db;
    private ActualizadorVistas actualizador; 
    private Empresa empresa = new Empresa();

    Label nombreLabel = new Label("Nombre:");
    final TextField nombreInput = new TextField();
    Text nombreError = new Text();

    Label rncLabel = new Label("RNC");
    final TextField rncInput = new TextField();
    Text rncError = new Text();

    Button someterBtn = new Button("Someter");
    HBox hbBtn = new HBox(10);
    Button limpiarBtn = new Button("Limpiar");
    Text formErrors = new Text();

    Text titulo = new Text();

    public EmpresaForm(MySQL db, ActualizadorVistas actualizador) {
        if (getEmpresa() == null) {
            setEmpresa(new Empresa());
        }
        setDB(db);
        setActualizador(actualizador);
        actualizarTitulo();
        formErrors.setFill(Color.FIREBRICK);
        nombreError.setFill(Color.FIREBRICK);
        rncError.setFill(Color.FIREBRICK);

        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);

        nombreInput.focusedProperty().addListener((arg0, oldValue, newValue)    -> {
            if (!newValue) { //when focus lost
                validarNombre();
            }
        });
        rncInput.focusedProperty().addListener((arg0, oldValue, newValue) -> {
            
            if (!newValue) { //when focus lost
                validarRNC();
                
            }
        });

        someterBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                validarNombre();
                validarRNC();
                if (nombreError.getText().length() == 0 && rncError.getText().length() == 0) {
                    try {
                        if (getEmpresa().ID == 0) {
                            setEmpresa(getDB().CrearEmpresa(getEmpresa()));
                        } else {
                            setEmpresa(getDB().ActualizarEmpresa(getID(), getEmpresa()));
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

    public void validarRNC() {
        String msg = "";
        if (rncInput.getText().length() == 0) {
            msg = "RNC: no puede estar vacío.";
        }
        if (rncInput.getText().length() > 9) {
            String s = rncInput.getText().substring(0, 9);
            rncInput.setText(s);
        }
        if (rncInput.getText().length() != 9) {
            msg = "RNC: debe tener 9 dígitos.";
        }
        if (!rncInput.getText().matches("\\d+")) {
            msg = "RNC: debe de ser numérico.";
        }
        if (msg.length() != 0) {
            rncError.setText(msg);
        } else {
            rncError.setText("");
            setRNC(rncInput.getText());
        }
    }
    public void validarNombre() {
        String msg = "";
        if (nombreInput.getText().length() == 0) {
            msg = "Nombre: no puede estar vacío.";
        }
        if (nombreInput.getText().length() > 255) {
            msg = "Nombre: no puede exceder 255 caracteres.";
        }
        if (msg.length() != 0) {
            nombreError.setText(msg);
        } else {
            nombreError.setText("");
            setNombre(nombreInput.getText());
        }
    }
    public void actualizarTitulo() {
        titulo.setText(
            (getEmpresa().ID == 0 ? "Crear empresa" : String.format("Editar: %s", getEmpresa().toString()))
        );
    }


    public GridPane getForm() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        titulo.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(titulo, 0, 0, 2, 1);

        grid.add(nombreLabel, 0, 1);
        grid.add(nombreInput, 1, 1);
        grid.add(nombreError, 1, 2);

        grid.add(rncLabel, 0, 3);
        grid.add(rncInput, 1, 3);
        grid.add(rncError, 1, 4);

        grid.add(formErrors, 1, 5);

        hbBtn.getChildren().add(limpiarBtn);
        hbBtn.getChildren().add(someterBtn);
        grid.add(hbBtn, 1, 6);

        return grid;
    }

    private void resetForm() {
        nombreInput.setText("");
        nombreError.setText("");
        rncInput.setText("");
        rncError.setText("");
        formErrors.setText("");
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
        return empresa.ID;
    }
    public void setID(long id) {
        this.empresa.ID = id;
    }
    public String getNombre() {
        return empresa.Nombre;
    }
    public void setNombre(String nombre) {
        this.empresa.Nombre = nombre;
    }
    public String getRNC() {
        return empresa.RNC;
    }
    public void setRNC(String rnc) {
        this.empresa.RNC = rnc;
    }
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    public Empresa getEmpresa() {
        return empresa;
    }
}