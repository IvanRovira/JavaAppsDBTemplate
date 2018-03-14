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
import org.japo.java.libraries.UtilesEntrada;

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

    public static final String DEF_SQL_ALU1 = "SELECT * FROM alumno";
    public static final String DEF_SQL_ALU2 = "DELETE * FROM alumno";
    public static final String DEF_SQL_ALU3 = "INSERT * FROM alumno";
    public static final String DEF_SQL_ALU4 = "UPDATE * FROM alumno";

    public static final String DEF_SQL_PRO1 = "SELECT * FROM profesor";
    public static final String DEL_SQL_PRO2 = "DELETE * FROM profesor";
    public static final String DEL_SQL_PRO3 = "INSERT * FROM profesor";
    public static final String DEL_SQL_PRO4 = "UPDATE * FROM profesor";

    // Constantes para las cabeceras de las diferentes tablas
    public static final String CAB_LIST_MOD1 = "#    Id   Acrónimo    Nombre                   Código   Horas   Curso";
    public static final String CAB_LIST_MOD2 = "==   ==== ==========  ======================   ======== ======= ======= ";
    public static final String CAB_LIST_ALU = "#     Exp         Nombre    Apellidos   Nif        Nac        Teléfono  Email               Domicilio         Localidad     Provincia     CP     User     Pass      Foto";
    public static final String CAB_LIST_ALU1 = "==   ==========  ========  =========== =========  ========== ========= ==================  ================  ============  ============  =====  =======  =======   =======";
    public static final String CAB_LIST_PRO1 = "#    Id  Nombre         Apellidos      Departamento       Especialidad    Tipo";
    public static final String CAB_LIST_PRO2 = "==   ==  =============  =============  =================  ==============  =========";

    // Constantes información del borrado de módulos interactivo
    public static final String CAB_REG_MOD1 = "Proceso de borrado - Registro %02d";
    public static final String CAB_REG_MOD2 = "==================================";

    // Sentencias SQL Borrado, Inserción, Update
    public static final String DEF_MOD_SQL2 = "DELETE FROM modulo WHERE acronimo='ED'";
    public static final String DEF_MOD_SQL3 = "INSERT INTO modulo(id,acronimo,nombre,codigo,horasCurso,curso) VALUES ('2','ED','Entorno de Desarrollo','MP0465','200','1')";
    public static final String DEF_MOD_SQL4 = "UPDATE modulo SET curso='2' WHERE horasCurso<200";
    public static final String DEF_MOD_SQL5 = "SELECT * FROM modulo WHERE id=%s";

    public static final String DEF_ALU_SQL2 = "DELETE FROM alumno WHERE exp=''";
    public static final String DEF_ALU_SQL3 = "INSERT INTO alumno(exp,nombre,apellido,nif,nac,telefono,email,domicilio,localidad,provincia,cp,user,pass,foto) VALUES ('EXP0000001','Ivan','Rovira','12345678H','17/12/1993','632223456','Calle Pantomima','Altura','Valencia','12410','Ivan','12345','101010')";
    public static final String DEF_ALU_SQL4 = "UPDATE alumno SET exp='' WHERE ";

    public static final String DEF_PRO_SQL2 = "DELETE FROM profesor WHERE id=''";
    public static final String DEF_PRO_SQL3 = "INSERT INTO profesor(id,nombre,apellidos,departamento,especialidad,tipo) VALUES ('1','MamaCheco','PapaPaco','Informática','Programación','Proaso')";
    public static final String DEF_PRO_SQL4 = "UPDATE profesor SET tipo='PROASO' WHERE especialidad=Programación";

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
                    // Print para alumnos
                    //System.out.printf("%-4d %-15s %-5s %-11s %-10s %s %s %-19s %-23s %-7s %-13s %-7s %-8s %-9s %s %n", fila, exp, nombre, apellidos, nif, nac, telefono, email, domicilio, localidad, provincia, cp, user, pass, foto);
                    // Print para profesores
                    // System.out.printf("%-4d %-12s %-5s %-14s %-18s %-15s %s %n", fila, id, nombre, apellidos, departamento, especialidad, tipo);
                } while (rs.next());
            } else {
                System.out.println("No hay datos que mostrar");
            }
        }

    }

    public final void listarModulos(int lineasPagina) throws SQLException {

        // Si el número de líneas es menor o igual a 0 muestra toda la información en una línea
        if (lineasPagina <= 0) {
            listarModulos();
            // Si el número de líneas es mayor que 0 hace el proceso
        } else {
            System.out.println("Listado de módulos ...");
            System.out.println("---");
            // Almacena el resultado de la SQL en el rs
            try (ResultSet rs = stmt.executeQuery(DEF_SQL_MOD1)) {
                // Si hay más elementos en el rs sigue el proceso
                if (rs.next()) {
                    // Semáforo
                    boolean nuevaLineaOK;
                    // Inicializamos la línea y el número de página actual a 1
                    int lineaAct = 1;
                    int paginaAct = 1;
                    // Bucle Externo para mostrar el número de página y el encabezado de tabla con cada página
                    // si hay más de 3 líneas (la información es mayor de lo que cabe en la página actual) nos pregunta si queremos mostrar el resto de información
                    // en otra página, lo hará dependiendo de la respuesta (sSnN), si la respuesta es positiva muestra la siguiente página, sino no la mostrará,
                    // por lo cual sale del bucle
                    do {
                        System.out.println("");
                        System.out.printf("Página...: %02d%n", paginaAct);
                        System.out.println("==============");
                        System.out.println("");
                        System.out.println(CAB_LIST_MOD1);
                        System.out.println(CAB_LIST_MOD2);
                        // Bucle Interno que almacena en variables el contenido de la tabla módulo
                        // y lo muestra por filas, usa un actualizador para llegar al límite de 
                        // líneas por página (que establecimos nosotros en 3) y usa el semáforo 
                        // dependiendo de si hay o no más datos en el rs para salir del bucle.
                        do {
                            int fila = rs.getRow();
                            int id = rs.getInt("id");
                            String acro = rs.getString("acronimo");
                            String nombre = rs.getString("nombre");
                            String codigo = rs.getString("codigo");
                            int horasCurso = rs.getInt("horasCurso");
                            int curso = rs.getInt("curso");
                            System.out.printf("%-5d %-6d %-8s %-25s %-2s %6d %6d %n", fila, id, acro, nombre, codigo, horasCurso, curso);

                            lineaAct++;
                            nuevaLineaOK = rs.next();
                        } while (lineaAct <= lineasPagina && nuevaLineaOK);
                        if (nuevaLineaOK) {
                            System.out.println("---");
                            char respuesta = UtilesEntrada.leerOpcion("sSnN", "Siguiente página (S/N)...:", "ERROR: Entrada incorrecta");
                            if (respuesta == 's' || respuesta == 'S') {
                                paginaAct++;
                                lineaAct = 1;
                                System.out.println("---");
                            } else {
                                nuevaLineaOK = false;
                            }
                        }
                    } while (nuevaLineaOK);

                } else {
                    System.out.println("No hay módulos que mostrar");
                }
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

    public final void borrarModulosInteractivo() throws SQLException {

        System.out.println("");
        System.out.println("---");
        System.out.println("Borrado de módulos ...");
        System.out.println("---");
        System.out.println("");
        // Almacenamos los registros resultantes de la SQL en el rs
        try (ResultSet rs = stmt.executeQuery(DEF_SQL_MOD1)) {
            // Variable para saber el número de registros que hemos borrado
            int regBorrados = 0;
            // Bucle que siga mostrando los registros mientras haya elementos en el rs
            while (rs.next()) {
                // Cogemos cada uno de los campos(columnas 1,2,3...) de la tabla modulos para hacer una registro(fila) con esos datos
                // correspondientes a un modulo completo
                System.out.printf(CAB_REG_MOD1 + "%n", rs.getRow());
                System.out.println(CAB_REG_MOD2);
                System.out.printf("Id ............: %d%n", rs.getInt(1));
                System.out.printf("Acronimo ......: %s%n", rs.getString(2));
                System.out.printf("Nombre ........: %s%n", rs.getString(3));
                System.out.printf("Codigo ........: %s%n", rs.getString(4));
                System.out.printf("Horas Curso ...: %d%n", rs.getInt(5));
                System.out.printf("Curso .........: %d%n", rs.getInt(6));

                System.out.println("");
                // Creamos una variable char que almacenará la respuesta de si queremos borrar o no el registro, gracias al método de leerOpcion
                char respuesta = UtilesEntrada.leerOpcion("SsNn", "Borrar Módulo (S/N) ...: ", "ERROR: Entrada Incorrecta");
                System.out.println("");

                // Si la respuesta es sí, borrará el registro(fila) entero y sumará 1 a regBorrados
                if (respuesta == 'S' || respuesta == 's') {
                    rs.deleteRow();
                    regBorrados++;
                    System.out.println("---");
                    System.out.println("Modulo actual borrado");
                }
                System.out.println("---");
            }
            System.out.printf("Se han borrado %d registros %n", regBorrados);
        }
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

    public final void insertarModulosInteractivos() throws SQLException {
        System.out.println("");
        System.out.println("---");
        System.out.println("Inserción de módulos ...");
        System.out.println("---");
        System.out.println("");

        // Almacenamos el resultado de la SQL en el rs
        try (ResultSet rs = stmt.executeQuery(DEF_SQL_MOD1)) {
            // Nos desplazamos a la fila especial InsertRow
            rs.moveToInsertRow();
            // Insertamos los datos, que de momento se almacenarán en InsertRow
            rs.updateInt(1, UtilesEntrada.leerEntero("Id...: ", "ERROR: Entrada incorrecta"));
            rs.updateString(2, UtilesEntrada.leerTexto("Acrónimo...: "));
            rs.updateString(3, UtilesEntrada.leerTexto("Nombre...: "));
            rs.updateString(4, UtilesEntrada.leerTexto("Código...: "));
            rs.updateInt(5, UtilesEntrada.leerEntero("Horas...: ", "ERROR: Entrada incorrecta"));
            rs.updateInt(6, UtilesEntrada.leerEntero("Curso...: ", "ERROR: Entrada incorrecta"));
            System.out.println("---");

            // Creamos una variable para almacenar la decisión del usuario sobre la inserción de módulos
            char respuesta = UtilesEntrada.leerOpcion("SsNn", "Insertar Modulo (S/N)...: ", "ERROR: Entrada incorrecta");
            // Si la respuesta es sí, moverá los datos de InsertRow a la tabla de la base de datos correspondiente
            if (respuesta == 'S' || respuesta == 's') {
                rs.insertRow();
                System.out.println("---");
                System.out.println("Inserción de datos COMPLETADA");
            } else {
                System.out.println("---");
                System.out.println("Inserción de datos CANCELADA");
            }
            // Nos devuelve a la fila en la que estábamos antes de movernos a la fila especial InsertRow
            rs.moveToCurrentRow();
        }
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

    // Este método busca registros para cambiarlos, excepto el campo que ha servido para realizar la búsqueda (id por ejemplo).
    public final void modificarModulosInteractivo() throws SQLException {

        System.out.println("");
        System.out.println("---");
        System.out.println("Modificación de datos ...");
        System.out.println("---");

        // Utilizamos la ID de la tabla como "motor de búsqueda" para los registros
        int id = UtilesEntrada.leerEntero("ID búsqueda...: ", "ERROR: Entrada incorrecta");

        // Almacenamos en el resultset el resultado de la SQL, haciendo que coincida con la id para dependiendo 
        // de cual elijamos nos muestre un registro u otro, además convertimos la id a String para poder realizar
        // búsquedas en otras tablas independientemente del tipo que sea la clave primaria
        try (ResultSet rs = stmt.executeQuery(String.format(DEF_MOD_SQL5, id + ""))) {
            // La condición se cumple si hay más registros en el rs, así que si hay pasamos a la primera columna desde el BeforeFirst
            if (rs.next()) {

                System.out.println("");
                System.out.println("Registro actual - Estado INICIAL");
                System.out.println("================================");
                System.out.println("");
                // Cogemos los datos de los campos correspondientes a la id especificada arriba y los mostramos 
                System.out.printf("Acrónimo... %s%n", rs.getString("acronimo"));
                System.out.printf("Nombre...: %s%n", rs.getString("nombre"));
                System.out.printf("Código...: %s%n", rs.getString("codigo"));
                System.out.printf("Horas...: %s%n", rs.getInt("horasCurso"));
                System.out.printf("Curso...: %s%n", rs.getInt("curso"));

                System.out.println("");
                System.out.println("Registro actual - Estado FINAL");
                System.out.println("==============================");
                System.out.println("");
                // Actualizamos cada uno de los campos del registro y se almacenan en BeforeFirst
                rs.updateString(2, UtilesEntrada.leerTexto("Arcónimo...: "));
                rs.updateString(3, UtilesEntrada.leerTexto("Nombre...: "));
                rs.updateString(4, UtilesEntrada.leerTexto("Código...: "));
                rs.updateInt(5, UtilesEntrada.leerEntero("Horas...: ", "ERROR: Lectura incorrecta"));
                rs.updateInt(6, UtilesEntrada.leerEntero("Curso...: ", "ERROR: Lectura incorrecta"));
                System.out.println("---");

                // Creamos una variable para almacenar la decisión del usuario de actualizar o no el registro
                char respuesta = UtilesEntrada.leerOpcion("SsNn", "Actualizar MÓDULO (S/N)", "ERROR: Lectura incorrecta");

                System.out.println("---");
                // Si la respuesta contiene S ó s la actualización se realiza gracias a rs.updateRow() que manda el registro
                // desde el BeforeFirst hasta la tabla de la base de datos
                if (respuesta == 'S' || respuesta == 's') {
                    rs.updateRow();
                    System.out.println("Actualización COMPLETADA");
                } else {
                    System.out.println("Actualización CANCELADA");
                }
            } else {
                System.out.println("ERROR: No hay datos asociados");
            }
        }
    }

}
