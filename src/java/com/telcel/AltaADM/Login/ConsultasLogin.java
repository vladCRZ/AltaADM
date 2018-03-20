/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telcel.AltaADM.Login;

import com.infomedia.utils.PropertyLoader;
import com.telcel.AltaADM.Utilerias.servicesRemedy;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author INFOTELCEL
 */
public class ConsultasLogin {

    public final Logger log = Logger.getLogger(ControlLogin.class);
    Mensajes msg = new Mensajes();
    servicesRemedy sRemedy = new servicesRemedy();
    Properties prop;

    public String mensajeConsulta;

    public ConsultasLogin() {
        prop = PropertyLoader.load("altaadm.properties");
    }

    public String getMensajeConsulta() {
        return mensajeConsulta;
    }

    public void setMensajeConsulta(String mensajeConsulta) {
        this.mensajeConsulta = mensajeConsulta;
    }

    /**
     * Consulta para validar el usuario y numero de empleado
     *
     * @author HARL
     * @since 25/02/2015
     * @param usuEmpleado
     * @param numEmpleado
     * @return datosOk
     *
     */
    public boolean validaNominaUsuario(String usuEmpleado, String numEmpleado) {
        boolean datosOk = false;
        try {
            String columna = "";
            columna += prop.getProperty("CLIENTE_ARS");
            columna += prop.getProperty("FORM_NOMINA");
            columna += prop.getProperty("DAT_NOMINA1");
            columna += "&cCondiciones='536870914'='" + usuEmpleado + "' ";
            columna += "'536870913'='" + numEmpleado + "'";

            if (sRemedy.consultaGeneral(columna) && sRemedy.resultadoConsulta.startsWith("GERENTE") || sRemedy.resultadoConsulta.startsWith("JEFE") && !sRemedy.resultadoConsulta.startsWith("JEFE DE OYM") && !sRemedy.resultadoConsulta.startsWith("GERENTE DE OYM")) {
                mensajeConsulta = "Las credenciales y el puesto son validas";
                datosOk = true;
            } else {
                mensajeConsulta = "Las credenciales o el puesto son invalidas";
                datosOk = false;
            }

        } catch (Exception exc) {
            mensajeConsulta = "Mensaje de Administrador: Error interno Class:ConsultasLogin()";
            log.error(exc);
        }
        return datosOk;
    }
    
    public String fncConsultaNominaCorreo(String psNumEmpleado){
        String columna = "", vsRes = "";
            //columna += prop.getProperty("CLIENTE_ARS");
            columna += prop.getProperty("FORM_NOMINA");
            columna += prop.getProperty("DAT_NOMINA3");
            columna += "&cCondiciones='536870913'='" + psNumEmpleado + "'";
            log.info("CONSULTA "+ columna);
            vsRes = sRemedy.validaADM(prop.getProperty("CLIENTE_ARS"), columna);
        return vsRes;
    }

    public boolean validaRemedyUsuario(String usuEmpleado, String numEmpleado) {
        boolean datosOk;
        boolean datosOkF = false;// = false;
        try {
            String columna = "";

            columna += prop.getProperty("CLIENTE_ARS");
            columna += prop.getProperty("FORM_PEOPLE");
            columna += prop.getProperty("DAT_PEOPLE");
            columna += "&cCondiciones='7'='2' ";
            columna += "'1000000054'='" + numEmpleado + "'";

            datosOk = sRemedy.consultaGeneral(columna);
            
            if (datosOk) {
                datosOkF = false;
                mensajeConsulta = "El usuario esta dentro del REMEDY, esta solicitud no aplica";
            }else{
                datosOkF = true;
            }
            
            log.info(datosOk);
        } catch (Exception exc) {
            mensajeConsulta = "Mensaje de Administrador: Error interno Class:ConsultasLogin()";
            log.error(exc);
        }
        return datosOkF;
    }
}
