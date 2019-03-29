package com.telcel.AltaADM.Login;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public class Mensajes
{
  FacesMessage msg;
  
  public void mensajeError(String mensaje)
  {
    this.msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", mensaje);
    FacesContext.getCurrentInstance().addMessage(null, this.msg);
  }
  
  public void mensajeInformacion(String mensaje)
  {
    this.msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Informaci�n", mensaje);
    FacesContext.getCurrentInstance().addMessage(null, this.msg);
  }
  
  public void mensajePrecaucio(String mensaje)
  {
    this.msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenci�n", mensaje);
    FacesContext.getCurrentInstance().addMessage(null, this.msg);
  }
  
  public void mensajeErrorFatal(String mensaje)
  {
    this.msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error interno", mensaje);
    FacesContext.getCurrentInstance().addMessage(null, this.msg);
  }
}