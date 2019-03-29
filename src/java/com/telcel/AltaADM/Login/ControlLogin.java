/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telcel.AltaADM.Login;

import com.infomedia.utils.PropertyLoader;
import com.telcel.AltaADM.DB.ConsultasDB;
import com.telcel.AltaADM.Utilerias.Correos;
import com.telcel.AltaADM.Utilerias.SesionUsuario;
import java.util.List;
import java.util.Properties;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

import org.primefaces.context.RequestContext;

/**
 *
 * @author INFOTELCEL
 */
public class ControlLogin {

    //Variable de LOGGER
    public final Logger LOG = Logger.getLogger(ControlLogin.class);

    //Variables de navegacion
    HttpServletRequest httpServletRequest;
    FacesContext facesContext;
    RequestContext context;

    //Variables de clases
    Mensajes msg = new Mensajes();
    Login login = new Login();
    ConsultasLogin consulta = new ConsultasLogin();
    SesionUsuario sesionUsuario = new SesionUsuario();
    ConsultasDB cDB = new ConsultasDB();
    Properties prop;
    //Variables de logueo
    boolean logueado = false;
    String rutaPagina = "";

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public ControlLogin() {
        prop = PropertyLoader.load("altaadm.properties");
    }

    public ConsultasLogin getConsulta() {
        return consulta;
    }

    public void setConsulta(ConsultasLogin consulta) {
        this.consulta = consulta;
    }

    /**
     * Funcion que controla el acceso del usuario al sistema
     *
     * @param event
     * @since 25/02/2015
     *
     */
    public void fncEnviarCorreo(ActionEvent event) {
        String vsQuery = "";
        Correos voCorreos = new Correos();
        vsQuery = "SELECT ID_USERADM FROM USER_ADM WHERE Numeroempleado_Useradm ='" + login.getVsNumEmpleado() + "'";
        String vsConsulta = "SELECT PASSWORDUSER_USERADM FROM USER_ADM WHERE Numeroempleado_Useradm ='" + login.getVsNumEmpleado() + "'";
        try {
            if (cDB.ConsultaGeneral(vsQuery)) {
                List<String> vsPass = cDB.consultaListas(vsConsulta, "PASSWORDUSER_USERADM");
                String vsCorreo = consulta.fncConsultaNominaCorreo(login.getVsNumEmpleado());
                LOG.info("CORREO: "+ vsCorreo);
                LOG.info("PASS: "+ vsPass.get(0));
                voCorreos.fncEnviarCorreo(vsCorreo, vsPass.get(0));
                msg.mensajeInformacion("CORREO ENVIADO AL USUARIO " + login.getVsNumEmpleado());

            } else {
                msg.mensajeError("USUARIO NO REGISTRADO EN EL SISTEMA");
            }
        } catch (Exception e) {
            
            LOG.error(e);
        }

    }

    public void consultaUsuario(ActionEvent event) {

        String SQuery = "";
        String SQuery1 = "";

        facesContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        context = RequestContext.getCurrentInstance();

        SQuery += "SELECT ID_USERADM FROM USER_ADM WHERE ";
        SQuery += "Numeroempleado_Useradm ='" + login.getNumeroEmpleado() + "' ";
        SQuery += "AND Passworduser_Useradm='" + login.getUsuarioEmpleado() + "' ";
        SQuery += "ORDER BY Id_Useradm";

        SQuery1 = "SELECT ID_USERADM FROM USER_ADM WHERE Numeroempleado_Useradm ='" + login.getNumeroEmpleado() + "'";

        if (cDB.ConsultaGeneral(SQuery1)) {
            if (cDB.ConsultaGeneral(SQuery)) {
                context = RequestContext.getCurrentInstance();
                httpServletRequest.getSession().setAttribute("UsuarioLogueado", login.getNumeroEmpleado());
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Usuario", login.getNumeroEmpleado());
                rutaPagina = prop.getProperty("URL_INGRESO");
                LOG.info("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                LOG.info(".:.:.:.:.INICIA SESION.:.:.:.:.");
                LOG.info("Usuario universal:" + login.getUsuarioEmpleado());
                LOG.info("Numero de empleado:" + login.getNumeroEmpleado());
                LOG.info("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
                logueado = true;
            } else {
                msg.mensajeError("LA CONTRASEÑA ES INCORRECTA");
                logueado = false;
            }
        } else if (consulta.validaRemedyUsuario(login.getUsuarioEmpleado().toUpperCase(), login.getNumeroEmpleado()) && consulta.validaNominaUsuario(login.getUsuarioEmpleado().toUpperCase(), login.getNumeroEmpleado())) {
            context = RequestContext.getCurrentInstance();
            httpServletRequest.getSession().setAttribute("UsuarioLogueado", login.getNumeroEmpleado());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("Usuario", login.getNumeroEmpleado());
            rutaPagina = prop.getProperty("URL_PASS");
            LOG.info("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
            LOG.info(".:.:.:.:.CAMBIA PASSWORD.:.:.:.:.");
            LOG.info("Usuario universal:" + login.getUsuarioEmpleado());
            LOG.info("Numero de empleado:" + login.getNumeroEmpleado());
            LOG.info("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
            logueado = true;
        } else {
            rutaPagina = prop.getProperty("URL_INICIO");
            logueado = false;
            msg.mensajeError(consulta.getMensajeConsulta());
        }
        context.addCallbackParam("logueo", logueado);
        context.addCallbackParam("rutaPagina", rutaPagina);
    }

    /**
     * Función que cierra la sesion del usuario y redirecciona a la pagina de
     * inicio
     *
     * @author HARL
     * @since 25/02/2015
     *
     */
    public void cerrarSesion() {

        rutaPagina = prop.getProperty("URL_INICIO");
        context = RequestContext.getCurrentInstance();
        context.addCallbackParam("loggedOut", true);
        context.addCallbackParam("rutaPagina", rutaPagina);
        LOG.info("CIERRA SESION");

    }

    public void redireccionaInicio(String usuario) {

        context = RequestContext.getCurrentInstance();
        logueado = true;
        rutaPagina = prop.getProperty("URL_INGRESO");
        context.addCallbackParam("logueo", logueado);
        context.addCallbackParam("rutaPagina", rutaPagina);

    }
}
