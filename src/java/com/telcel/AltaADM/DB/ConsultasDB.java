/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telcel.AltaADM.DB;

import com.telcel.AltaADM.Login.Mensajes;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author INFOTELCEL
 */
public class ConsultasDB {

    private int maximo = 0;
    private boolean datosOk;
    private Statement stDB;
    private ResultSet rtDB;

    Logger log = Logger.getLogger(ConsultasDB.class);
    ConexionDB conDB = new ConexionDB();
    Mensajes msg = new Mensajes();
    Connection conn;

    public int getMaximo() {
        return maximo;
    }

    public void setMaximo(int maximo) {
        this.maximo = maximo;
    }

    public boolean isDatosOk() {
        return datosOk;
    }

    public void setDatosOk(boolean datosOk) {
        this.datosOk = datosOk;
    }

    public Statement getStDB() {
        return stDB;
    }

    public void setStDB(Statement stDB) {
        this.stDB = stDB;
    }

    public ResultSet getRtDB() {
        return rtDB;
    }

    public void setRtDB(ResultSet rtDB) {
        this.rtDB = rtDB;
    }

    /**
     * Función para consultar si existe la informacion en la base de datos
     * @param QueryDB
     * @return datosOk
     */
    public boolean ConsultaGeneral(String QueryDB) {
        datosOk = false;
        System.err.println(QueryDB);
        try {
            conn = conDB.ConexionDB();
            System.err.println(conn);
            stDB = conn.createStatement();
            rtDB = stDB.executeQuery(QueryDB);
            if (rtDB.next()) {
                datosOk = true;
            }
            rtDB.close();
            stDB.close();
            conDB.cierraConexion();
        } catch (SQLException sqlEXC) {
            log.error(sqlEXC);
            datosOk = false;
        }
        return datosOk;
    }

    /**
     * Función de consulta, para obtener el ultimo indice de la tabla de la base
     * de datos, en la que se realizara una insercion de registros
     *
     * @param tabla
     * @param columna
     * @return maximo
     */
    public int Maximo(String tabla, String columna) {

        try {
            maximo = 0;
            conn = conDB.ConexionDB();
            stDB = conn.createStatement();
            rtDB = stDB.executeQuery("SELECT MAX(" + columna + ")+1 FROM " + tabla);
            if (rtDB.next()) {
                maximo = rtDB.getInt("MAX(" + columna + ")+1");
                log.info("NUMERO DE INDICE DE LA TABLA" + maximo);
            }
            rtDB.close();
            stDB.close();
            conDB.cierraConexion();
        } catch (SQLException sqlEXC) {
            log.error(sqlEXC);
            datosOk = false;
        }
        return maximo;
    }

    /**
     * Función para actualizar el registro dentro de una tabla en a base de
     * datos
     *
     * @param Query
     * @return datosOk
     */
    public boolean ActualizaDatos(String Query) {
        try {
            int rsTB;
            conn = conDB.ConexionDB();
            stDB = conn.createStatement();
            rsTB = stDB.executeUpdate(Query);
            if (rsTB != 0) {
                datosOk = true;
            }
            stDB.close();
            conn.close();
        } catch (SQLException EXC) {
            log.error(EXC);
            datosOk = false;
        }
        return datosOk;
    }
    /**
     * funcion de insercion de informacion a una tabla de la base de datos.
     * @param Query
     * @return datosOk
     */
    public boolean InsertaDatos(String Query) {
        try {
            conn = conDB.ConexionDB();
            stDB = conn.createStatement();
            rtDB = stDB.executeQuery(Query);
            if (rtDB.next()) {
                datosOk = true;
            }
            rtDB.close();
            stDB.close();
            conn.close();
        } catch (SQLException exc) {
            datosOk = false;
            msg.mensajeError("OCURRIO UN ERROR INTERNO AL TRATAR DE GUARDAR LA CONTRASEÑA, FAVOR DE REPORTARLO CON EL ADMINISTRADOR DEL SISTEMA");
            msg.mensajeErrorFatal(exc.getMessage());
        }
        return datosOk;
    }
    
       public List<String> consultaListas(String Query,String psColumna) {
        List<String> resultadoConsulta = new ArrayList<>();
        try {
            conn = conDB.ConexionDB();
            stDB = conn.createStatement();
            rtDB = stDB.executeQuery(Query);
            log.debug(rtDB);
            while (rtDB.next()) {
                log.debug(rtDB.getString(psColumna));
                resultadoConsulta.add(rtDB.getString(psColumna));
            }
            rtDB.close();
            stDB.close();
            conn.close();
        } catch (Exception exc) {
            msg.mensajeError("OCURRIO UN ERROR INTERNO AL TRATAR DE GUARDAR LA CONTRASEÑA, FAVOR DE REPORTARLO CON EL ADMINISTRADOR DEL SISTEMA");
            msg.mensajeErrorFatal(exc.getMessage());
        }
        return resultadoConsulta;
    }
}
