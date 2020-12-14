package db;
import java.sql.SQLException;
import java.util.ArrayList;

import models.Empresa;

/**
 * EmpleadoStorer es el interfaz para trabajar con empleados en un medio persistente.
 */
public interface EmpresaStorer {
    ArrayList<Empresa> ListarEmpresas(int lim, int off) throws SQLException;
	Empresa GetEmpresaPorNombreORNC(String rncOrName)  throws SQLException;
	Empresa ActualizarEmpresa(long id, Empresa empresa) throws SQLException;
	Empresa CrearEmpresa(Empresa e) throws SQLException;
	void BorrarEmpresa(long id) throws SQLException;
}
