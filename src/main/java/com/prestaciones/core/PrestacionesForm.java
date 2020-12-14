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
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import db.MySQL;
import models.Empleado;
import models.Empresa;
import prestaciones.Prestaciones;

public class PrestacionesForm {
    private Empresa empresa;
    private Empleado empleado;
    private boolean preaviso;
    private boolean cesantia;
    private boolean vacaciones;
    private boolean salarioNavidad;
    private Date fechaDeSalida = Date.valueOf(LocalDate.now());
    private String message = "";
    private MySQL db;


    ArrayList<Empresa> empresas = new ArrayList<Empresa>();
    ArrayList<Empleado> empleados = new ArrayList<Empleado>();

    Label empresasLabel = new Label("Empresa:");
    ComboBox empresasInput = new ComboBox(FXCollections.observableArrayList(empresas));

    Label empleadosLabel = new Label("Empleados:");
    ComboBox empleadosInput = new ComboBox(FXCollections.observableArrayList(empleados));

    final CheckBox preavisoInput = new CheckBox("Preaviso");
    final CheckBox cesantiaInput = new CheckBox("Cesantía");
    final CheckBox vacacionesInput = new CheckBox("Vacaciones");
    final CheckBox salarioNavidadInput = new CheckBox("Salario navidad");

    Label fechaDeSalidaLabel = new Label("Fecha de salida:");
    DatePicker fechaDeSalidaInput = new DatePicker(LocalDate.now());

    Button calcularBtn = new Button("Calcular");
    HBox hbBtn = new HBox(10);
    Button limpiarBtn = new Button("Limpiar");
    Text messageLabel = new Text();

    public PrestacionesForm(MySQL db) {
        setDB(db);
        preavisoInput.setIndeterminate(false);
        cesantiaInput.setIndeterminate(false);
        vacacionesInput.setIndeterminate(false);
        salarioNavidadInput.setIndeterminate(false);

        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);

        try {
            setEmpresas(getDB().ListarEmpresas(100, 0));
        } catch(SQLException ex) {
            setMessage(ex.toString());
        }
        empresasInput.setItems(FXCollections.observableArrayList(getEmpresas()));
        empresasInput.getSelectionModel().selectFirst();
        setEmpresa(getEmpresas().get(0));

        try {
            setEmpleados(getDB().ListarEmpleadosParaEmpresa(getEmpresa().ID, 100,0));
        } catch(SQLException ex) {
            setMessage(ex.toString());
        }
        empleadosInput.setItems(FXCollections.observableArrayList(getEmpleados()));
        empleadosInput.getSelectionModel().selectFirst();
        setEmpleado(getEmpleados().get(0));

        empresasInput.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                setEmpresa((Empresa)empresasInput.getValue());
                try {
                    setEmpleados(getDB().ListarEmpleadosParaEmpresa(getEmpresa().ID, 100,0));
                    empleadosInput.setItems(FXCollections.observableArrayList(getEmpleados()));
                    empleadosInput.getSelectionModel().selectFirst();
                    setEmpleado(getEmpleados().get(0));
                } catch(SQLException sqlex) {
                    setMessage(sqlex.toString());
                }
            }
        });
        empleadosInput.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                setEmpleado((Empleado)empleadosInput.getValue());
            }
        });
        fechaDeSalidaInput.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                setFechaDeSalida( Date.valueOf(fechaDeSalidaInput.getValue()) );
            }
        });
        preavisoInput.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                setPreaviso( preavisoInput.isSelected() );
            }
        });
        cesantiaInput.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                setCesantia( cesantiaInput.isSelected() );
            }
        });
        vacacionesInput.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                setVacaciones( vacacionesInput.isSelected() );
            }
        });
        salarioNavidadInput.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                setSalarioNavidad( salarioNavidadInput.isSelected() );
            }
        });

        calcularBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Prestaciones p = new Prestaciones(
                    getEmpleado().FechaDeContratacion,
                    getFechaDeSalida(),
                    getEmpleado().SalarioMensual
                );
                setMessage(
                    String.format("%s, %s (%s):\n\n", getEmpleado().Apellidos, getEmpleado().Nombre, getEmpresa().Nombre)
                            +String.format("\t%-21s\t\t%,12.2f\n", "Salario Mensual:", getEmpleado().SalarioMensual)
                            +String.format("\t%-21s\t%14s\n", "Fecha de contratación:", getEmpleado().FechaDeContratacion)
                            +(getPreaviso() ? String.format("\t%-21s\t\t\t%,12.2f\n", "Preaviso:", p.Preaviso) : "")
                            +(getCesantia() ? String.format("\t%-21s\t\t\t%,12.2f\n", "Cesantía:", p.Cesantia) : "")
                            +(getVacaciones() ? String.format("\t%-21s\t\t\t%,12.2f\n", "Vacaciones:", p.Vacaciones) : "")
                            +(getSalarioNavidad() ? String.format("\t%-21s\t\t%,12.2f\n", "Salario navidad:", p.SalarioNavidad) : "")
                            +"----------------------------------------------------------------\n"
                            +String.format(
                                "\t%-21s\t\t\t%,12.2f\n",
                                "Total:",
                                    (getPreaviso() ? p.Preaviso : 0)
                                    +(getCesantia() ? p.Cesantia : 0)
                                    +(getVacaciones() ? p.Vacaciones: 0)
                                    +(getSalarioNavidad() ? p.SalarioNavidad  : 0)
                            )
                );
                displayResults();
            }
        });
        limpiarBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                resetForm();
            }
        });
    }

    private void displayResults() {
        messageLabel.setText(getMessage());
    }

    public GridPane getForm() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text titulo = new Text("Calculador de prestaciones");
        titulo.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(titulo, 0, 0, 2, 1);

        grid.add(empresasLabel, 0, 1);
        grid.add(empresasInput, 1, 1);

        grid.add(empleadosLabel, 0, 2);
        grid.add(empleadosInput, 1, 2);

        grid.add(preavisoInput, 1, 3);
        grid.add(cesantiaInput, 1, 4);
        grid.add(vacacionesInput, 1, 5);
        grid.add(salarioNavidadInput, 1, 6);

        grid.add(fechaDeSalidaLabel, 0, 7);
        grid.add(fechaDeSalidaInput, 1, 7);

        hbBtn.getChildren().add(limpiarBtn);
        hbBtn.getChildren().add(calcularBtn);
        grid.add(hbBtn, 1, 8);

        grid.add(messageLabel, 1, 9);

        return grid;
    }

    private void resetForm() {
        empleadosInput.getSelectionModel().selectFirst();
        preavisoInput.setSelected(false);
        cesantiaInput.setSelected(false);
        vacacionesInput.setSelected(false);
        salarioNavidadInput.setSelected(false);
        fechaDeSalidaInput.setValue(LocalDate.now());
        messageLabel.setText("");
    }

    public MySQL getDB() {
        return db;
    }
    public void setDB(MySQL db) {
        this.db = db;
    }
    
    public Empresa getEmpresa() {
        return empresa;
    }
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    public Empleado getEmpleado() {
        return empleado;
    }
    public void setEmpleado(Empleado e) {
        this.empleado = e;
    }
    public boolean getPreaviso() {
        return preaviso;
    };
    public void setPreaviso(boolean preaviso) {
        this.preaviso = preaviso;
    };
    public boolean getCesantia() {
        return cesantia;
    };
    public void setCesantia(boolean cesantia) {
        this.cesantia = cesantia;
    };
    public boolean getVacaciones() {
        return vacaciones;
    };
    public void setVacaciones(boolean vacaciones) {
        this.vacaciones = vacaciones;
    };
    public boolean getSalarioNavidad() {
        return salarioNavidad;
    };
    public void setSalarioNavidad(boolean salarioNavidad) {
        this.salarioNavidad = salarioNavidad;
    };
    public Date getFechaDeSalida() {
        return    fechaDeSalida;
    };
    public void setFechaDeSalida(Date fechaDeSalida) {
        this.fechaDeSalida = fechaDeSalida;
    };

    public String getMessage() {
        return    message;
    };
    public void setMessage(String message) {
        this.message = message;
    };

    private ArrayList<Empresa> getEmpresas() {
        return empresas;
    }
    private void setEmpresas(ArrayList<Empresa> empresas){
        this.empresas = empresas;
    }
    private ArrayList<Empleado> getEmpleados() {
        return empleados;
    }
    private void setEmpleados(ArrayList<Empleado> empleados){
        this.empleados = empleados;
    }

}