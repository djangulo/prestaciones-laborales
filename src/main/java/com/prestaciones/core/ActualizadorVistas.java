package com.prestaciones.core;

public class ActualizadorVistas {
    ListadoEmpleados listado;

    public ActualizadorVistas(ListadoEmpleados listado) {
        this.listado = listado;
    }
    public void actualizar() {
        listado.actualizar();
    }
}