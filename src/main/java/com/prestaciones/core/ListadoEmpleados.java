package com.prestaciones.core;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.geometry.Pos;
import javafx.util.Callback;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.collections.FXCollections;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import db.MySQL;
import models.Empleado; 

public class ListadoEmpleados {
    MySQL db;
 
    ArrayList<Empleado> empleados = new ArrayList<Empleado>();
    HashMap<Long, Boolean> idsSeleccionados = new HashMap<Long, Boolean>();
    final Label label = new Label("Listado de empleados");
    private TableView table = new TableView();
    TableColumn<Empleado, String> nombreEmpresaCol = new TableColumn<Empleado, String>("Empresa");
    TableColumn<Empleado, String> rncEmpresaCol = new TableColumn<Empleado, String>("RNC");
    TableColumn<Empleado, String> nombreCol = new TableColumn<Empleado, String>("Nombre");
    TableColumn<Empleado, String> apellidosCol = new TableColumn<Empleado, String>("Apellidos");
    TableColumn<Empleado, String> salarioCol = new TableColumn<Empleado, String>("Salario");
    TableColumn<Empleado, String> fechaDeContratacionCol = new TableColumn<Empleado, String>("Contratado");


    TableColumn<Empleado, Boolean> selectCol = new TableColumn<Empleado, Boolean>( "Seleccionar" );

    final VBox vbox = new VBox(5);
    Button editarBtn = new Button("Editar");
    Button eliminarBtn = new Button("Eliminar");
    HBox hbBtn = new HBox(10);
    
    public ListadoEmpleados(MySQL db) {
        this.db = db;
        actualizar();
        label.setFont(new Font("Arial", 20));
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);

        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        table.setMinHeight(600);

        hbBtn.getChildren().addAll(editarBtn, eliminarBtn);

        selectCol.setCellValueFactory(param -> param.getValue().isChecked());
        selectCol.setCellFactory(CheckBoxTableCell.forTableColumn(selectCol));


        nombreCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().Nombre));
        nombreCol.setMinWidth(100);
        apellidosCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().Apellidos));
        apellidosCol.setMinWidth(150);
        salarioCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(Double.toString(cellData.getValue().SalarioMensual)));
        salarioCol.setMinWidth(100);
        fechaDeContratacionCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().FechaDeContratacion.toString()));
        fechaDeContratacionCol.setMinWidth(100);
        nombreEmpresaCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getNombreEmpresa()));
        nombreEmpresaCol.setMinWidth(100);
        rncEmpresaCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getRNCEmpresa()));
        rncEmpresaCol.setMinWidth(100);
        table.getColumns().addAll(selectCol, nombreCol, apellidosCol, salarioCol, fechaDeContratacionCol, nombreEmpresaCol, rncEmpresaCol);
    }


    public void actualizar() {
        try {
            empleados = db.ListarEmpleados(1000, 0);
        } catch(SQLException ex) {
            System.out.println(ex);
        }
        for(Empleado e : empleados) {
            deSeleccionarID(e.ID);
        }
        table.setItems(FXCollections.observableArrayList(empleados));
    }

    public VBox getTable() {

        vbox.getChildren().addAll(label, table, hbBtn);
        return vbox;

    }

    public void seleccionarID(long id) {
        System.out.printf("SELECCIONANDO %d\n", id);
        idsSeleccionados.put(id, true);
    }
    public void deSeleccionarID(long id) {
        System.out.printf("DE-SELECCIONANDO %d\n", id);
        idsSeleccionados.remove(id);
    }

    public boolean estaSeleccionado(long id) {
        return idsSeleccionados.getOrDefault(id, false);
    }

    private void borrarSeleccionados() {
        for (Long key: idsSeleccionados.keySet()) {
            try {
                db.BorrarEmpleado(key);
            } catch (SQLException ex) {
                System.out.println(ex);
            }
        }
    }
}
