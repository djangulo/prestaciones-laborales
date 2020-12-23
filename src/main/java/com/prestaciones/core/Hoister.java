package com.prestaciones.core;

import models.Empleado;
import models.Empresa;

public class Hoister {
    private EmpleadoForm empleadoForm;
    private EmpresaForm empresaForm;

    public Hoister() {
    }

    public Hoister(EmpleadoForm empleadoForm, EmpresaForm empresaForm) {
        this.empleadoForm = empleadoForm;
        this.empresaForm = empresaForm;
    }

    public void enviarEmpleado(Empleado e) {
        empleadoForm.setEmpleado(e);
    }

    public void enviarEmpresa(Empresa e) {
        empresaForm.setEmpresa(e);
    }

    public EmpleadoForm getEmpleadoForm() {
        return empleadoForm;
    }

    public EmpresaForm getEmpresaForm() {
        return empresaForm;
    }
}