/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telcel.AltaADM.Login;

import java.io.Serializable;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

/**
 *
 * @author INFOTELCEL
 */
@SessionScoped
public class Login implements Serializable {

    private String usuarioEmpleado;
    private String numeroEmpleado;
    private String vsNumEmpleado;

    public Login() {
    }

    public String getUsuarioEmpleado() {
        return usuarioEmpleado;
    }

    public void setUsuarioEmpleado(String usuarioEmpleado) {
        this.usuarioEmpleado = usuarioEmpleado;
    }

    public String getNumeroEmpleado() {
        return numeroEmpleado;
    }

    public void setNumeroEmpleado(String numeroEmpleado) {
        this.numeroEmpleado = numeroEmpleado;
    }

    public String getVsNumEmpleado() {
        return vsNumEmpleado;
    }

    public void setVsNumEmpleado(String vsNumEmpleado) {
        this.vsNumEmpleado = vsNumEmpleado;
    }
    
    
}
