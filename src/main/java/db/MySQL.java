package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZoneId;
import java.util.ArrayList;

import models.Empleado;
import models.Empresa;
import db.EmpleadoStorer;
import db.EmpresaStorer;

public class MySQL implements EmpresaStorer, EmpleadoStorer {
    private Connection conn;

    public MySQL(String connURL, String username, String password) {
        try {
            conn = DriverManager.getConnection(connURL, username, password);
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public ArrayList<Empleado> ListarEmpleados(int lim, int off) throws SQLException {
        String query = "SELECT " + "id, nombre, apellidos, fecha_de_contratacion,"
                + "salario_mensual, id_empresa, rnc_empresa, nombre_empresa FROM empleados_full";

        if (lim == 0) {
            query += " LIMIT 10";
        } else {
            query += String.format(" LIMIT %d", lim);
        }

        if (off != 0) {
            query += String.format(" OFFSET %d;", off);
        } else {
            query += ";";
        }

        ArrayList<Empleado> res = new ArrayList<Empleado>();
        try {

            PreparedStatement stmt = conn.prepareStatement(query);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Empleado e = new Empleado();
                e.setId(rs.getLong("id"));
                e.setNombre(rs.getString("nombre"));
                e.setApellidos(rs.getString("apellidos"));
                e.setFechaDeContratacion(rs.getDate("fecha_de_contratacion").toString());
                e.setSalarioMensual(rs.getDouble("salario_mensual"));
                e.setIdEmpresa(rs.getLong("id_empresa"));
                e.getEmpresa().setId(rs.getLong("id_empresa"));
                e.getEmpresa().setRnc(rs.getString("rnc_empresa"));
                e.getEmpresa().setNombre(rs.getString("nombre_empresa"));

                res.add(e);
            }
        } catch (SQLException ex) {
            throw ex;
        }

        return res;
    }

    public ArrayList<Empleado> ListarEmpleadosParaEmpresa(long idEmpresa, int lim, int off) throws SQLException {
        String query = "SELECT " + "id, nombre, apellidos, fecha_de_contratacion,"
                + "salario_mensual, id_empresa, rnc_empresa, nombre_empresa FROM empleados_full"
                + " WHERE id_empresa = ?";

        if (lim == 0) {
            query += " LIMIT 10";
        } else {
            query += String.format(" LIMIT %d", lim);
        }

        if (off != 0) {
            query += String.format(" OFFSET %d;", off);
        } else {
            query += ";";
        }

        ArrayList<Empleado> res = new ArrayList<Empleado>();
        try {

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setLong(1, idEmpresa);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Empleado e = new Empleado();
                e.setId(rs.getLong("id"));
                e.setNombre(rs.getString("nombre"));
                e.setApellidos(rs.getString("apellidos"));
                e.setFechaDeContratacion(rs.getDate("fecha_de_contratacion").toString());
                e.setSalarioMensual(rs.getDouble("salario_mensual"));
                e.setIdEmpresa(rs.getLong("id_empresa"));
                e.getEmpresa().setId(rs.getLong("id_empresa"));
                e.getEmpresa().setRnc(rs.getString("rnc_empresa"));
                e.getEmpresa().setNombre(rs.getString("nombre_empresa"));

                res.add(e);
            }
        } catch (SQLException ex) {
            throw ex;
        }

        return res;
    }

    public Empleado GetEmpleado(long id) throws SQLException {
        String queryStr = "SELECT " + "  id, nombre, apellidos, fecha_de_contratacion,"
                + "  salario_mensual, id_empresa, nombre_empresa, rnc_empresa FROM empleados_full" + "WHERE id = ?;";

        Empleado e = new Empleado();
        try {
            PreparedStatement query = conn.prepareStatement(queryStr);
            query.setLong(1, id);

            ResultSet rs = query.executeQuery();
            while (rs.next()) {
                e.setId(rs.getLong("id"));
                e.setNombre(rs.getString("nombre"));
                e.setApellidos(rs.getString("apellidos"));
                e.setFechaDeContratacion(rs.getDate("fecha_de_contratacion").toString());
                e.setSalarioMensual(rs.getDouble("salario_mensual"));
                e.setIdEmpresa(rs.getLong("id_empresa"));
                e.getEmpresa().setId(rs.getLong("id_empresa"));
                e.getEmpresa().setRnc(rs.getString("rnc_empresa"));
                e.getEmpresa().setNombre(rs.getString("nombre_empresa"));
            }

        } catch (SQLException ex) {
            throw ex;
        }

        return e;
    }

    public Empleado CrearEmpleado(Empleado e) throws SQLException {
        String query = "INSERT INTO empleados "
                + "(nombre, apellidos, fecha_de_contratacion, salario_mensual, id_empresa)"
                + " VALUES (?, ?, ?, ?, ?);";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, e.getNombre());
            stmt.setString(2, e.getApellidos());
            stmt.setDate(3, java.sql.Date.valueOf(e.getFechaDeContratacion()));
            stmt.setDouble(4, e.getSalarioMensual());
            stmt.setLong(5, e.getIdEmpresa());
            stmt.executeUpdate();

            Statement lastIdStmt = conn.createStatement();
            ResultSet rs = lastIdStmt.executeQuery("SELECT LAST_INSERT_ID();");
            while (rs.next()) {
                e.setId(rs.getLong("LAST_INSERT_ID()"));
            }
        } catch (SQLException ex) {
            throw ex;
        }

        return e;
    }

    public Empleado ActualizarEmpleado(long id, Empleado e) throws SQLException {
        String query = "UPDATE empleados SET nombre = ?, apellidos = ?,"
                + "fecha_de_contratacion = ?, salario_mensual = ?,id_empresa = ? WHERE id = ?;";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, e.getNombre());
            stmt.setString(2, e.getApellidos());
            stmt.setDate(3, java.sql.Date.valueOf(e.getFechaDeContratacion()));
            stmt.setDouble(4, e.getSalarioMensual());
            stmt.setLong(5, e.getIdEmpresa());
            stmt.setLong(6, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw ex;
        }
        return e;
    }

    /**
     * Borra el empleado con `id` de la tabla `empleados`.
     */
    public void BorrarEmpleado(long id) throws SQLException {
        String queryStr = "DELETE FROM empleados WHERE id = ?;";
        try {
            PreparedStatement stmt = conn.prepareStatement(queryStr);
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw ex;
        }
    }

    /**
     * Lista las empresas, con offset off y limite lim.
     */
    public ArrayList<Empresa> ListarEmpresas(int lim, int off) throws SQLException {
        String query = "SELECT id, nombre, rnc FROM empresas";

        if (lim == 0) {
            query += " LIMIT 10";
        } else {
            query += String.format(" LIMIT %d", lim);
        }

        if (off != 0) {
            query += String.format(" OFFSET %d;", off);
        } else {
            query += ";";
        }

        ArrayList<Empresa> res = new ArrayList<Empresa>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Empresa e = new Empresa();
                e.setId(rs.getLong("id"));
                e.setNombre(rs.getString("nombre"));
                e.setRnc(rs.getString("rnc"));

                res.add(e);
            }
        } catch (SQLException ex) {
            throw ex;
        }

        return res;
    }

    /**
     * Retorna una empresa de la base de datos.
     */
    public Empresa GetEmpresaPorNombreORNC(String rncOrName) throws SQLException {
        String queryStr = "SELECT id, nombre, rnc FROM empresas WHERE rnc = ? OR nombre = ? LIMIT 1;";
        Empresa e = new Empresa();
        try {
            PreparedStatement query = conn.prepareStatement(queryStr);
            query.setString(1, rncOrName);
            query.setString(2, rncOrName);

            ResultSet rs = query.executeQuery();

            while (rs.next()) {
                e.setId(rs.getLong("id"));
                e.setNombre(rs.getString("nombre"));
                e.setRnc(rs.getString("rnc"));
            }
        } catch (SQLException ex) {
            throw ex;
        }

        return e;
    }

    /**
     * Crea una empresa y retorna la empresa con el ID asignado por la base de
     * datos.
     */
    public Empresa CrearEmpresa(Empresa e) throws SQLException {
        String query = "INSERT INTO empresas" + "(nombre, rnc) VALUES (?, ?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, e.getNombre());
            stmt.setString(2, e.getRnc());
            stmt.executeUpdate();

            Statement lastIdStmt = conn.createStatement();
            ResultSet rs = lastIdStmt.executeQuery("SELECT LAST_INSERT_ID();");
            while (rs.next()) {
                e.setId(rs.getLong("LAST_INSERT_ID()"));
            }

        } catch (SQLException ex) {
            throw ex;
        }

        return e;
    }

    /**
     * Borra la empresa con `rnc` de la tabla `empresas`.
     */
    public void BorrarEmpresa(long id) throws SQLException {
        String queryStr = "DELETE FROM empresas WHERE id = ?;";
        try {
            PreparedStatement stmt = conn.prepareStatement(queryStr);
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw ex;
        }
    }

    public Empresa ActualizarEmpresa(long id, Empresa empresa) throws SQLException {
        String query = "UPDATE empresas SET nombre = ?, rnc = ? WHERE id = ?;";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, empresa.getNombre());
            stmt.setString(2, empresa.getRnc());
            stmt.setLong(3, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            throw ex;
        }
        return empresa;
    }

    /**
     * cierra la conexion, evitando memory-leaks.
     */
    protected void finalize() throws SQLException {
        try {
            conn.close();
        } catch (SQLException ex) {
            throw ex;
        }
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

}
