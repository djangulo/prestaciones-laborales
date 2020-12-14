package db;
import java.sql.SQLException;
import java.util.ArrayList;

import models.Empleado;

/**
 * EmpleadoStorer es el interfaz para trabajar con empleados en un medio persistente.
 */
public interface EmpleadoStorer {
	ArrayList<Empleado> ListarEmpleados(int lim, int off) throws SQLException;
	ArrayList<Empleado> ListarEmpleadosParaEmpresa(long idEmpresa,int lim, int off) throws SQLException;
	Empleado GetEmpleado(long id)  throws SQLException;
	Empleado CrearEmpleado(Empleado e) throws SQLException;
	Empleado ActualizarEmpleado(long id, Empleado e) throws SQLException;
	void BorrarEmpleado(long id) throws SQLException;
}
