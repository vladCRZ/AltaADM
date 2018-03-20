/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telcel.AltaADM.DB;

import com.infomedia.utils.PropertyLoader;
import com.telcel.AltaADM.Login.ControlLogin;
import com.telcel.AltaADM.Login.Mensajes;
import com.telcel.AltaADM.Utilerias.SesionUsuario;
import com.telcel.AltaADM.Utilerias.servicesRemedy;
import java.util.Properties;
import javax.faces.event.ActionEvent;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

/**
 *
 * @author INFOTELCEL
 */
public class CambiaPass {

    Logger LOG = Logger.getLogger(CambiaPass.class);
    SesionUsuario sesion = new SesionUsuario();
    ConsultasDB cDB = new ConsultasDB();
    ControlLogin login = new ControlLogin();
    Mensajes msg = new Mensajes();
    servicesRemedy sRemedy = new servicesRemedy();

    RequestContext context;
    Properties prop;

    public String passAct;
    public String pass1;
    public String pass2;
    public boolean datosOk;

    public CambiaPass() {

        prop = PropertyLoader.load("altaadm.properties");

    }

    public String getPass1() {
        return pass1;
    }

    public void setPass1(String pass1) {
        this.pass1 = pass1;
    }

    public String getPass2() {
        return pass2;
    }

    public boolean isDatosOk() {
        return datosOk;
    }

    public void setDatosOk(boolean datosOk) {
        this.datosOk = datosOk;
    }

    public void setPass2(String pass2) {
        this.pass2 = pass2;
    }

    public String getPassAct() {
        return passAct;
    }

    public void setPassAct(String passAct) {
        this.passAct = passAct;
    }

    public void CambiaPass(ActionEvent action) {
        if (validaCaracter(getPass1()) && validaLetraM(getPass1()) && validaLetraMin(getPass1()) && validaLongitud(getPass1()) && validaNumero(getPass1()) && validaPass()) {
            String columna = "";

            columna += prop.getProperty("CLIENTE_ARS");
            columna += prop.getProperty("FORM_NOMINA");
            columna += prop.getProperty("DAT_NOMINA6");
            columna += "&cCondiciones=";
            columna += "'536870913'='" + sesion.inciaSesion().toUpperCase() + "'";

            sRemedy.consultaGeneralList(columna);

            String usuarioNum = sRemedy.getListaResultado().get(0);
            String usuarioUni = sRemedy.getListaResultado().get(1);
            String usuarioSol = sRemedy.getListaResultado().get(4) + " " + sRemedy.getListaResultado().get(2) + " " + sRemedy.getListaResultado().get(3);
            String correoSol = sRemedy.getListaResultado().get(5);
            String puestoSol = sRemedy.getListaResultado().get(6);

            int indice = 0;
            indice = cDB.Maximo("USER_ADM", "ID_USERADM");
            String Query = "";
            Query += "INSERT INTO USER_ADM VALUES(";
            Query += " " + indice + ",";
            Query += "'" + usuarioNum + "',";
            Query += "'" + usuarioUni + "',";
            Query += "'" + usuarioSol + "',";
            Query += "'" + puestoSol + "',";
            Query += "'" + correoSol + "',";
            Query += "'" + pass1 + "')";
            LOG.info(Query);
            if (cDB.InsertaDatos(Query)) {
                msg.mensajeInformacion("Se cambio el password con exito");
                login.redireccionaInicio(sesion.inciaSesion());
            } else {
                msg.mensajeErrorFatal("Ocurrió un error interno al modificar el password, favor de reportalo con el administrador");
            }
        }
    }

    public void ActualizaPass(ActionEvent event) {
        if (validaPassACT(passAct, sesion.inciaSesion()) && validaCaracter(getPass1()) && validaLetraM(getPass1()) && validaLetraMin(getPass1()) && validaLongitud(getPass1()) && validaNumero(getPass1()) && validaPass()) {
            String Query = "";
            Query += "UPDATE USER_ADM SET ";
            Query += "PASSWORDUSER_USERADM='" + pass1 + "' ";
            Query += "WHERE NUMEROEMPLEADO_USERADM='" + sesion.inciaSesion().toUpperCase() + "'";
            if (cDB.ActualizaDatos(Query)) {
                msg.mensajeInformacion("Se actualizo el password con exito.");
            } else {
                msg.mensajeError("No se actulizo el password, favor de volver a intentarlo");
            }
        }
    }

    public boolean validaPassACT(String passACT, String usuario) {
        String Query = "";
        Query += "SELECT ID_USERADM FROM USER_ADM ";
        Query += "WHERE NUMEROEMPLEADO_USERADM='" + usuario + "' ";
        Query += "AND PASSWORDUSER_USERADM='" + passACT + "'";
        datosOk = false;
        if (cDB.ConsultaGeneral(Query)) {
            datosOk = true;
        }
        return datosOk;
    }

    public boolean validaPass() {
        datosOk = getPass1().equals(getPass2());
        if (!datosOk) {
            msg.mensajeError("Las contraseñas no coinciden");
        }
        return datosOk;
    }

    public boolean validaLongitud(String cadena) {
        datosOk = cadena.length() == 8;
        if (!datosOk) {
            msg.mensajeError("La contraseña debe ser de 8 digitos");
        }
        return datosOk;
    }

    public boolean validaLetraM(String cadena) {
        datosOk = cadena.matches(".*[A-Z].*");
        if (!datosOk) {
            msg.mensajeError("La contraseña debe de contener letras mayuscula");
        }
        return datosOk;
    }

    public boolean validaLetraMin(String cadena) {
        datosOk = cadena.matches(".*[a-z].*");
        if (!datosOk) {
            msg.mensajeError("La contraseña debe de contener letras minusculas");
        }
        return datosOk;
    }

    public boolean validaNumero(String cadena) {
        datosOk = cadena.matches(".*\\d.*");
        if (!datosOk) {
            msg.mensajeError("La contraseña debe de contener numeros");
        }
        return datosOk;
    }

    public boolean validaCaracter(String cadena) {
        datosOk = cadena.matches(".*\\w.*");
        if (!datosOk) {
            msg.mensajeError("La contraseña debe de contener caracteres especiales");
        }
        return datosOk;
    }

    public void LimpiaVariables(ActionEvent event) {

        passAct = "";
        pass1 = "";
        pass2 = "";

        String rutaPagina = prop.getProperty("URL_INICIO");

        context = RequestContext.getCurrentInstance();
        context.addCallbackParam("loggedOut", true);
        context.addCallbackParam("rutaPagina", rutaPagina);
        LOG.info("CIERRA SESION");
    }
}
