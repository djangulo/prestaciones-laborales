package com.prestaciones.core;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;

import db.MySQL;
import models.Empleado;
import models.Empresa;
import prestaciones.Prestaciones;

/**
 * Calcula las prestaciones de los primeros 10 empleados en la base de datos.
 *
 */
public class App extends Application {
    MySQL db = new MySQL("jdbc:mysql://localhost:3306/prestaciones", "root", "abcd1234");
    ArrayList<Empresa> empresas = new ArrayList<Empresa>();
    ArrayList<Empleado> empleados = new ArrayList<Empleado>();
    Empresa empresa = new Empresa();

    ComboBox empresasComboBox = new ComboBox(FXCollections.observableArrayList(empresas));
    ComboBox empresasComboBox2 = new ComboBox(FXCollections.observableArrayList(empresas));

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Prestaciones laborales");

        ListadoEmpleados listado = new ListadoEmpleados(db);
        ActualizadorVistas actualizador = new ActualizadorVistas(listado);

        VBox listadoVBox = listado.getTable();

        GridPane prestacionesForm = (new PrestacionesForm(getDB())).getForm();
        VBox formulariosVBox = new VBox(25);
        formulariosVBox.getChildren().addAll(
            (new EmpresaForm(getDB(), actualizador)).getForm(),
            (new EmpleadoForm(getDB(), actualizador)).getForm()
        );

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(listadoVBox, formulariosVBox);
        splitPane.setDividerPositions(0.6f);

        
        TabPane tabPane = new TabPane();
        Tab pres = new Tab("Calculador", prestacionesForm);
        Tab admin = new Tab("Admin", splitPane);
    
        tabPane.getTabs().add(pres);
        tabPane.getTabs().add(admin);

        Scene scene = new Scene(tabPane, 1400, 768);
        primaryStage.setScene(scene);
        
        primaryStage.show();
    }

    private MySQL getDB() {
        return db;
    }

    public static void main( String[] args ) {
        launch(args);
    }

}
