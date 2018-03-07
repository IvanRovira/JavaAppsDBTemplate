/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.japo.java.entities;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author CicloM
 */
public class DataAccessManager {

    // Sentencias SQL
    public static final String DEF_SQL_MOD1 = "SELECT * FROM modulo";
    public static final String DEF_SQL_MOD2 = "DELETE * FROM modulo";
    public static final String DEF_SQL_MOD3 = "INSERT * FROM modulo";
    public static final String DEF_SQL_MOD4 = "UPDATE * FROM modulo";

    public static final String DEF_SQL_ALU = "SELECT * FROM alumno";
    public static final String DEF_SQL_PRO = "SELECT * FROM profesor";

    public static final String DEL_SQL_MOD = "DELETE * FROM modulo";
    public static final String DEL_SQL_ALU = "DELETE * FROM alumno";
    public static final String DEL_SQL_PRO = "DELETE * FROM profesor";

    public static final String CAB_LIST_MOD1 = "#    Id   Acrónimo    Nombre                   Código   Horas   Curso";
    public static final String CAB_LIST_MOD2 = "==   ==== ==========  ======================   ======== ======= ======= ";

    public static final String CAB_LIST_ALU1 = "";
    public static final String CAB_LIST_ALU2 = "";
    public static final String CAB_LIST_PROF = "";
    public static final String CAB_LIST_PROF2 = "";

    // Sentencias SQL Borrado, Inserción, Update
    public static final String DEF_MOD_SQL2 = "DELETE FROM modulo WHERE acronimo='ED'";
    public static final String DEF_MOD_SQL3 = "INSERT INTO modulo(id,acronimo,nombre,codigo,horasCurso,curso) VALUES ('2','ED','Entorno de Desarrollo','MP0465','200','1')";
    public static final String DEF_MOD_SQL4 = "UPDATE modulo SET curso='2' WHERE horasCurso<200";

    private Connection con;
    private Statement stmt;

    public DataAccessManager(Connection con) {
        this.con = con;
    }

    public DataAccessManager(Connection con, Statement stmt) {
        this.con = con;
        this.stmt = stmt;
    }

    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }

    public Statement getStmt() {
        return stmt;
    }

    public void setStmt(Statement stmt) {
        this.stmt = stmt;
    }

    public final void mostrarMetadatos() throws SQLException {
        DatabaseMetaData dmd = getCon().getMetaData();
        System.out.println("Información BASE DE DATOS");
        System.out.println("==========================");
        System.out.printf("Usuario ...: %s%n", dmd.getUserName());
        System.out.printf("Base de datos ...: %s%n", dmd.getDatabaseProductName());
        System.out.printf("Version SGBD ...: %s%n", dmd.getDatabaseProductVersion());
        System.out.printf("Driver JDBC ...: %s%n", dmd.getDriverName());
        System.out.printf("Versión JDBC ...: %2d.%d%n", dmd.getJDBCMajorVersion(), dmd.getJDBCMinorVersion());
    }

    public final void listarModulos() throws SQLException {
        System.out.println("Listado de módulos... ");
        System.out.println("---");

        // rs obtiene el resultado de la consulta. Las sentencias que generan datos (select) se tienen que atacar
        // con executeQuery. Y las que no generan datos, sino que actúa sobre el número de filas afectadas por la secuencia SQL
        // se atacan con el método executeUpdate() (como insert, delete, update...).
        // Cuando el try termine se cierra el rs que hemos abierto con la sentencia def_sql_mod, pero la conexión y el stmt quedan abiertos
        // por lo cual lo cerraremos en el try del app.
        try (ResultSet rs = stmt.executeQuery(DEF_SQL_MOD1)) {
            if (rs.next()) {
                System.out.println(CAB_LIST_MOD1);
                System.out.println(CAB_LIST_MOD2);
                do {
                    // getRow() devuelve el número de fila donde está el puntero en el rs.
                    int fila = rs.getRow();
                    // getInt() cogerá el valor del campo id en este caso, ya que contiene datos de tipo entero se usará este método,
                    // también se podría usar getInt(1) ya que es el número de columna de id, la forma con número se puede hacer con cualquier rs.get
                    int id = rs.getInt("id");
                    // getString cogerá el valor del campo acrónimo en este caso, y como es varchar en la tabla, es el equivalente.
                    String acro = rs.getString("acronimo");
                    String nombre = rs.getString("nombre");
                    String codigo = rs.getString("codigo");
                    int horasCurso = rs.getInt("horasCurso");
                    int curso = rs.getInt("curso");

                    System.out.printf("%-5d %-6d %-8s %-25s %-2s %6d %6d %n", fila, id, acro, nombre, codigo, horasCurso, curso);
                } while (rs.next());
            } else {
                System.out.println("No hay datos que mostrar");
            }
        }

    }

    public final void borrarModulos() throws SQLException {

        System.out.println("");
        System.out.println("---");
        System.out.println("Borrado de módulos ...");
        System.out.println("---");

        int filas = stmt.executeUpdate(DEF_MOD_SQL2);
        System.out.printf("Se han borrado %d modulos%n", filas);
        System.out.println("");

    }

    public final void insertarModulos() throws SQLException {

        System.out.println("");
        System.out.println("---");
        System.out.println("Inserción de datos ...");
        System.out.println("---");
        System.out.println("");

        int inser = stmt.executeUpdate(DEF_MOD_SQL3);
        System.out.printf("Se han insertado %d módulos%n", inser);
        System.out.println("");
    }

    public final void insertarModulosPreparados() throws SQLException {

        // Creamos un string que contenga una secuencia SQL preparada, cuyos parámetros serán IN ("?").
        String sql = "INSERT INTO modulo VALUES ( ?, ? ,?, ?, ?, ? )";
        // Creamos un objeto PreparedStatement, cuyo parámetro será el String, mediante la Connection (con)
        PreparedStatement sentencia = getCon().prepareStatement(sql);

        // Suministramos un valor para cada parámetro IN, gracias a los métodos de PreparedStatement
        // donde el primer parámetro será el número de valor a sustituir, y el segundo parámetro será el valor.
        sentencia.setInt(1, 6);
        sentencia.setString(2, "SIS");
        sentencia.setString(3, "Sistemas");
        sentencia.setString(4, "MP5005");
        sentencia.setInt(5, 150);
        sentencia.setInt(6, 1);

        // Ejecutamos el objeto de PreparedStatement, el cual contiene la secuencia SQL con los valores ya añadidos
        sentencia.executeUpdate();

        System.out.println("");
        System.out.println("---");
        System.out.println("Inserción de datos con SQL Preparada");
        System.out.println("---");

    }

    public final void modificarModulos() throws SQLException {

        System.out.println("");
        System.out.println("---");
        System.out.println("Modificación de datos ...");
        System.out.println("---");

        int mod = stmt.executeUpdate(DEF_MOD_SQL4);
        System.out.printf("Se han modificado %d modulos%n", mod);
        System.out.println("");
    }

}
