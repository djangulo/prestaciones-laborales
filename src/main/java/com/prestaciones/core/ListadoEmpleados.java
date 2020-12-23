package com.prestaciones.core;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.event.EventHandler;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import javafx.util.Callback;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.beans.value.ObservableValue;
import db.MySQL;
import models.Empleado;

public class ListadoEmpleados {
    MySQL db;

    private int seleccionadosCount = 0;

    ArrayList<Empleado> empleados = new ArrayList<Empleado>();
    HashMap<Long, Boolean> idsSeleccionados = new HashMap<Long, Boolean>();
    final Label label = new Label("Listado de empleados");
    private TableView<Empleado> table = new TableView<Empleado>();
    private Hoister hoister = new Hoister();

    TableColumn<Empleado, String> nombreEmpresaCol = new TableColumn<Empleado, String>("Empresa");
    TableColumn<Empleado, String> rncEmpresaCol = new TableColumn<Empleado, String>("RNC");
    TableColumn<Empleado, String> nombreCol = new TableColumn<Empleado, String>("Nombre");
    TableColumn<Empleado, String> apellidosCol = new TableColumn<Empleado, String>("Apellidos");
    TableColumn<Empleado, Double> salarioCol = new TableColumn<Empleado, Double>("Salario");
    TableColumn<Empleado, String> fechaDeContratacionCol = new TableColumn<Empleado, String>("Contratado");

    TableColumn<Empleado, Boolean> selectCol;

    final VBox vbox = new VBox(5);
    Button editarBtn = new Button("Editar");
    Button eliminarBtn = new Button("Eliminar");
    HBox hbBtn = new HBox(10);

    public ListadoEmpleados(MySQL db) {
        this.db = db;
        actualizar();
        for (Empleado e : empleados) {
            idsSeleccionados.put(e.getId(), false);
        }
        table.setEditable(true);
        label.setFont(new Font("Arial", 20));
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);

        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        table.setMinHeight(600);

        editarBtn.setDisable(true);
        eliminarBtn.setDisable(true);

        hbBtn.getChildren().addAll(editarBtn, eliminarBtn);

        Callback<CellDataFeatures<Empleado, Boolean>, ObservableValue<Boolean>> cb = new Callback<CellDataFeatures<Empleado, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(CellDataFeatures<Empleado, Boolean> param) {
                setearBotones();
                return param.getValue().checkedProperty();
            }
        };
        selectCol = new TableColumn<Empleado, Boolean>("Seleccionar");

        selectCol.setCellValueFactory(cb);
        selectCol.setCellFactory(CheckBoxTableCell.forTableColumn(selectCol));
        selectCol.setEditable(true);

        // selectCol.setCellValueFactory(param -> param.getValue().isChecked());
        // selectCol.setCellFactory(CheckBoxTableCell.forTableColumn(selectCol));

        nombreCol.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombre"));
        nombreCol.setMinWidth(100);
        apellidosCol.setCellValueFactory(new PropertyValueFactory<Empleado, String>("apellidos"));
        apellidosCol.setMinWidth(150);
        salarioCol.setCellValueFactory(new PropertyValueFactory<Empleado, Double>("salarioMensual"));
        salarioCol.setMinWidth(100);

        fechaDeContratacionCol.setCellValueFactory(new PropertyValueFactory<Empleado, String>("fechaDeContratacion"));
        fechaDeContratacionCol.setMinWidth(100);

        nombreEmpresaCol.setCellValueFactory(new PropertyValueFactory<Empleado, String>("nombreEmpresa"));
        nombreEmpresaCol.setMinWidth(100);
        rncEmpresaCol.setCellValueFactory(new PropertyValueFactory<Empleado, String>("RncEmpresa"));
        rncEmpresaCol.setMinWidth(100);

        eliminarBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                borrarSeleccionados();
            }
        });
        editarBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                handleEdit();
            }
        });
        table.getColumns().addAll(selectCol, nombreCol, apellidosCol, salarioCol, fechaDeContratacionCol,
                nombreEmpresaCol, rncEmpresaCol);
    }

    public void actualizar() {
        try {
            empleados = db.ListarEmpleados(1000, 0);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        table.setItems(FXCollections.observableArrayList(empleados));
    }

    public VBox getTable() {

        vbox.getChildren().addAll(label, table, hbBtn);
        return vbox;

    }

    private void borrarSeleccionados() {
        for (Empleado e : empleados) {
            if (e.getChecked()) {
                try {
                    db.BorrarEmpleado(e.getId());
                } catch (SQLException ex) {
                    System.out.println(ex);
                }
            }
        }
        actualizar();
    }

    private void handleEdit() {
        for (Empleado e : empleados) {
            if (e.getChecked()) {
                hoister.enviarEmpleado(e);
                hoister.getEmpleadoForm().actualizarTitulo();
                hoister.enviarEmpresa(e.getEmpresa());
                hoister.getEmpresaForm().actualizarTitulo();
                break;
            }
        }
    }

    private void setearBotones() {
        int count = 0;
        for (Empleado e : empleados) {
            if (e.getChecked()) {
                count++;
            }
        }
        setSeleccionadosCount(count);
        if (getSeleccionadosCount() == 1) {
            editarBtn.setDisable(false);
        } else if (getSeleccionadosCount() == 0 || getSeleccionadosCount() > 1) {
            editarBtn.setDisable(true);
        }
        if (getSeleccionadosCount() >= 1) {
            eliminarBtn.setDisable(false);
        } else if (getSeleccionadosCount() == 0) {
            eliminarBtn.setDisable(true);
        }
    }

    private void setSeleccionadosCount(int c) {
        this.seleccionadosCount = c;
    }

    private int getSeleccionadosCount() {
        return seleccionadosCount;
    }

    public void setHoister(Hoister hoister) {
        this.hoister = hoister;
    }
}
