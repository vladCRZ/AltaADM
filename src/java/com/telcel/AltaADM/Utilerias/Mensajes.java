/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telcel.AltaADM.Login;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author INFOTELCEL
 */
public class Mensajes {

    FacesMessage msg;

    //metodo para los mensajes de Error, en caso de que se haya producido algun error

    public void mensajeError(String mensaje) {
        msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", mensaje);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    //metodo para los mensajes de Informacion

    public void mensajeInformacion(String mensaje) {
        msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Información", mensaje);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    //metodo para los mensajes de Precaucion, para señalar la confirmación de algo.

    public void mensajePrecaucio(String mensaje) {
        msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atención", mensaje);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void mensajeErrorFatal(String mensaje){
        msg = new FacesMessage(FacesMessage.SEVERITY_FATAL,"Error interno", mensaje);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }    
}
