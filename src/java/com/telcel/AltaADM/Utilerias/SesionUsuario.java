/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telcel.AltaADM.Utilerias;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author INFOTELCEL
 */
public class SesionUsuario {

    String usuario;
    public final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(SesionUsuario.class);
    public HttpServletRequest httpServletRequest;
    public FacesContext facesContext;

    public SesionUsuario() {
    }

    public String inciaSesion( ) {
        facesContext = FacesContext.getCurrentInstance();
        httpServletRequest = (HttpServletRequest) facesContext.getExternalContext().getRequest();
        if (httpServletRequest.getSession().getAttribute("UsuarioLogueado") != null) {
            usuario = httpServletRequest.getSession().getAttribute("UsuarioLogueado").toString();
            LOG.info("usuario logueado: " + usuario);
        } else {
            LOG.info(".:.:.:.:.: Usuario no logueado .:.:.:.:.:.");
            usuario = null;
        }
        return usuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
