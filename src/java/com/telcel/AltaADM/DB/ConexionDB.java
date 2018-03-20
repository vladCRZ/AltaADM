/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telcel.AltaADM.DB;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.log4j.Logger;

/**
 *
 * @author INFOTELCEL
 */
public class ConexionDB {

    Logger LOG = Logger.getLogger(ConexionDB.class);
    Context context;
    DataSource dataSource;
    Connection conecta = null;

    public Connection getConecta() {
        return conecta;
    }

    public void setConecta(Connection conecta) {
        this.conecta = conecta;
    }

    public Connection ConexionDB() {
        conecta =null;
        try {
            context = new InitialContext();
            dataSource = (DataSource) context.lookup("java:/comp/env/ADM_Pool");
            conecta = dataSource.getConnection();
        } catch (SQLException | NamingException exc) {
            LOG.error(exc);
            conecta = null;
        }
        return conecta;
    }

    public void cierraConexion() throws SQLException {
        conecta.close();
    }    
}
