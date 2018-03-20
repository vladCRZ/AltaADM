/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telcel.AltaADM.Utilerias;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

/**
 *
 * @author INFOTELCEL
 */
public class AuthorizationListener implements PhaseListener {
    @Override
    public void afterPhase(PhaseEvent event) {
        // TODO Auto-generated method stub
        FacesContext facesContext = event.getFacesContext();
        String currentPage = facesContext.getViewRoot().getViewId();
        boolean isLoginPage = (currentPage.lastIndexOf("index.xhtml") >-1 ) ? true : false;
        HttpSession sesion = (HttpSession) facesContext.getExternalContext().getSession(true);
        Object usuario = sesion.getAttribute("Usuario");
        if( ! isLoginPage && usuario == null){
            NavigationHandler navHand = facesContext.getApplication().getNavigationHandler();
            navHand.handleNavigation(facesContext, null, "/index.xhtml");
        }
    }

    @Override
    public void beforePhase(PhaseEvent arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public PhaseId getPhaseId() {
        // TODO Auto-generated method stub
        return PhaseId.RESTORE_VIEW;
    }
}
