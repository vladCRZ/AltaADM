/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telcel.AltaADM.Utilerias;

import com.csvreader.CsvReader;
import com.telcel.AltaADM.DB.ConsultasDB;
import org.apache.log4j.Logger;

/**
 *
 * @author iHector
 */
public class CargaDatos {

    public static Logger LOG = Logger.getLogger(CargaDatos.class);

    public CargaDatos() {
    }

    public static void main(String[] args) {

        ConsultasDB cDB = new ConsultasDB();
        try {
            CsvReader csvReader = new CsvReader("C:/Users/ACsatillo/Desktop/HORAS_HARL.csv");
            csvReader.readHeaders();
            while (csvReader.readRecord()) {
                int indice = 0;
                indice = cDB.Maximo("ADM_REGISTRO", "ARD_CLAVE");
                String Query = "";
                Query += "INSERT INTO ADM_REGISTRO VALUES(";
                Query += " " + csvReader.get("NUM") + ",";
                Query += "'" + csvReader.get("ARD_ARA_CLAVE")+ "',";
                Query += "TO_TIMESTAMP('" + csvReader.get("ARD_FECHA")+ "','DD/MM/YYYY'),";
                Query += "TO_TIMESTAMP('" + csvReader.get("ARD_INICO")+ "','DD/MM/YYYY HH24:Mi:SS'),";
                Query += "TO_TIMESTAMP('" + csvReader.get("ARD_FIN")+ "','DD/MM/YYYY HH24:Mi:SS'),";
                Query += "'" + csvReader.get("ARD_DESCRIPCION")+ "',";
                Query += "'" + csvReader.get("ARD_EMPLEADO")+ "')";
                LOG.info(Query);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

}
