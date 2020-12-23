package com.prestaciones.core;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import db.MySQL;

/**
 * Calcula las prestaciones de los primeros 10 empleados en la base de datos.
 *
 */
public class App extends Application {
    MySQL db = new MySQL("jdbc:mysql://localhost:3306/prestaciones", "root", "abcd1234");

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Prestaciones laborales");

        ListadoEmpleados listado = new ListadoEmpleados(db);
        ActualizadorVistas actualizador = new ActualizadorVistas(listado);

        EmpleadoForm empleadoForm = new EmpleadoForm(getDB(), actualizador);
        EmpresaForm empresaForm = new EmpresaForm(getDB(), actualizador);
        Hoister hoister = new Hoister(empleadoForm, empresaForm);
        listado.setHoister(hoister);

        VBox listadoVBox = listado.getTable();

        GridPane prestacionesForm = (new PrestacionesForm(getDB())).getForm();
        VBox formulariosVBox = new VBox(25);
        formulariosVBox.getChildren().addAll(empresaForm.getForm(), empleadoForm.getForm());

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

    public static void main(String[] args) {
        launch(args);
    }

}
