/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telcel.AltaADM.Alta;

import com.infomedia.utils.PropertyLoader;
import com.telcel.AltaADM.Login.Mensajes;
import com.telcel.AltaADM.Utilerias.SesionUsuario;
import com.telcel.AltaADM.Utilerias.servicesRemedy;

import java.io.FileOutputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import org.primefaces.model.UploadedFile;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;

/**
 *
 * @author INFOTELCEL
 */
public class ControlPerfil {

    ///Variable de LOGGER
    public final Logger LOG = Logger.getLogger(ControlPerfil.class);

    ///Variables de clases
    PerfilUsuarioForm perfil = new PerfilUsuarioForm();
    ConsultasPerfil perfilConsultas = new ConsultasPerfil();
    SesionUsuario sesion = new SesionUsuario();
    Mensajes msg = new Mensajes();
    List<String> listaOpciones;
    List<String> listaRegiones;
    List<String> listaPerfil;

    ///Variables locales
    public String correoValido;
    public String rutaArchivo = "";
    public String nombreArchivo = "";
    private UploadedFile archADM;
    public boolean datosOkNC;
    public boolean datosOk;
    
    public boolean archivo;
    
    Properties prop;

    public DefaultStreamedContent imagen;
    servicesRemedy sRemedy = new servicesRemedy();

    public ControlPerfil() {

        prop = PropertyLoader.load("altaadm.properties");
        archivo = true;
    }

    public DefaultStreamedContent getImagen() {
        return imagen;
    }

    public void setImagen(DefaultStreamedContent imagen) {
        this.imagen = imagen;
    }

    public PerfilUsuarioForm getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilUsuarioForm perfil) {
        this.perfil = perfil;
    }

    /**
     * @return the correoValido
     */
    public String getCorreoValido() {
        return correoValido;
    }

    /**
     * @param correoValido the correoValido to set
     */
    public void setCorreoValido(String correoValido) {
        this.correoValido = correoValido;
    }

    /**
     * @return the listaOpciones
     */
    public List<String> getListaOpciones() {
        return listaOpciones;
    }

    /**
     * @param listaOpciones the listaOpciones to set
     */
    public void setListaOpciones(List<String> listaOpciones) {
        this.listaOpciones = listaOpciones;
    }

    public List<String> getListaRegiones() {
        return listaRegiones;
    }

    public void setListaRegiones(List<String> listaRegiones) {
        this.listaRegiones = listaRegiones;
    }

    /**
     * @return the datosOk
     */
    public boolean isDatosOk() {
        return datosOk;
    }

    /**
     * @param datosOk the datosOk to set
     */
    public void setDatosOk(boolean datosOk) {
        this.datosOk = datosOk;
    }

    public UploadedFile getArchADM() {
        return archADM;
    }

    public void setArchADM(UploadedFile archADM) {
        this.archADM = archADM;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public boolean isArchivo() {
        return archivo;
    }

    public void setArchivo(boolean archivo) {
        this.archivo = archivo;
    }

    /**
     * Funcion para validar que el empleado tenga un correo.
     *
     * @return datosOk
     */
    public boolean validaEmpleado() {
        if (perfilConsultas.consultaEmpleado(perfil.getNumeroEmpleado())) {
            if (validaCorreo()) {
                perfil.setCorreoEmpleado(perfilConsultas.getCorreoEmpleado());
                datosOk = true;
            }
        } else {
            msg.mensajeError(perfilConsultas.getMensajeConsultasPerfil());
            perfil.setCorreoEmpleado("");
            datosOk = false;
        }
        return datosOk;
    }

    /**
     * Funcion para validar si el usuario cuenta con cuenta en REMEDY y correo
     * electrónico
     *
     * @author HARL
     * @since 25/02/2015
     * @return datosOk
     */
    public boolean validaCorreo() {
        if (perfilConsultas.consultaCorreo(perfil.getNumeroEmpleado())) {
            //perfil.setCorreoEmpleado(perfilConsultas.getCorreoEmpleado());
            datosOk = true;
        } else {
            msg.mensajeError(perfilConsultas.getMensajeConsultasPerfil());
            perfil.setCorreoEmpleado("");
            datosOk = false;
        }
        return datosOk;
    }

    /**
     * Funcion para validar si el correo que proporciona el usuario es legible
     *
     * @author HARL
     * @since 25/02/2015
     * @return datosOKNC
     */
    public boolean validaCorreoNuevo() {
        datosOkNC = false;
        if (perfil.getCorreoEmpleado().contains("@mail.telcel.com") || perfil.getCorreoEmpleado().contains("@telcel.com")) {
            datosOkNC = true;
        } else {
            msg.mensajeError("Debe de proporcionar una cuenta de correo corporativo para la creación del ADM");
        }
        return datosOkNC;
    }

    /**
     * Funcion para copiar el archivo a una ruta especifica
     *
     * @author HARL
     * @since 12/03/2015
     * @param event
     * @return ruta final del archivo
     */
    public String CargaArchivoADM(FileUploadEvent event) {
        switch (perfil.getSistemaEmpleado()) {
            case "REMEDY":
                nombreArchivo = "";
                rutaArchivo="";
                break;
            case "ACCESO REMOTO VPN":
                UploadedFile archivoADM = event.getFile();
                rutaArchivo = prop.getProperty("PATH_ARCHIVOS") + "ARCHIVO-ADM-PAGINA_" + archivoADM.getFileName();
                try {
                    try (FileOutputStream fileOut = new FileOutputStream(rutaArchivo)) {
                        fileOut.write(archivoADM.getContents());
                        fileOut.flush();
                    }
                    LOG.info("Carga del archivo con exito");
                    LOG.info(rutaArchivo);
                    nombreArchivo = "ARCHIVO-ADM-PAGINA_" + archivoADM.getFileName();
                    imagen = new DefaultStreamedContent(event.getFile().getInputstream());
                    msg.mensajeInformacion("Se cargo el archivo: " + archivoADM.getFileName());
                } catch (IOException EFIO) {
                    LOG.error(EFIO);
                    msg.mensajeError("Ocurrio un problema al cargar el archivo");
                    nombreArchivo = "";
                    rutaArchivo = "";
                }
                break;
            default:
                nombreArchivo = "";
                rutaArchivo = "";
                break;
        }        
        return rutaArchivo;
    }

    /**
     * Funcion para validar perfiles de Acceso Remoto VPN
     *
     * @author GVelasco
     * @since 27/03/2019
     */
    
    
    public List<String> fncConsultarCampanias() {
        List<String> voCompañias = new ArrayList();
        
        try {
            String columna = "";

            columna += prop.getProperty("CLIENTE_ARS");
            columna += prop.getProperty("FORM_ADMSPR");
            columna += prop.getProperty("DAT_SPR");
            columna += prop.getProperty("COND_SPR");
            System.out.println(columna);
            voCompañias = sRemedy.consultaGeneralList(columna);
        } catch (Exception exc) {
            LOG.error(exc);
        }

        return voCompañias;
    }
    
    
    /**
     * Funcion para validar si el usuario tiene acceso al REMEDY
     *
     * @author HARL
     * @since 26/02/2015
     * @return datosOkSis
     */
    
    
    
    public boolean validaSistema() {
        LOG.debug(perfil.getSistemaEmpleado());
        listaRegiones = new ArrayList<>();
        boolean datosOkSis = false;
        switch (perfil.getSistemaEmpleado()) {
            case "REMEDY":
                if (!perfilConsultas.consultaUsuario(perfil.getNumeroEmpleado()) && !perfilConsultas.valdiaADM(perfil.getNumeroEmpleado(), "REMEDY")) {// && perfilConsultas.validaADM2(perfil.getNumeroEmpleado(), "REMEDY")) {
                    listaRegiones.add("CORP");
                    perfil.setRegionEmpleado("CORP");
                    perfil.setNodoEmpleado("PRODUCCION");
                    validaRegionPerfil();
                    datosOkSis = true;
                } else if (perfilConsultas.valdiaADM(perfil.getNumeroEmpleado(), "REMEDY")) {
                    msg.mensajeError("El usuario tiene un ADM de tipo REMEDY en ejecución");
                    LimpiaVariables();
                } else {
                    msg.mensajeError("El usuario esta en REMEDY esta solicitud no aplica");
                    LimpiaVariables();
                }
                archivo = false;
                break;
            case "ACCESO REMOTO VPN":
                if (!perfilConsultas.valdiaADM(perfil.getNumeroEmpleado(), "ACCESO REMOTO VPN")) {
                    listaRegiones.add("N/A");
                    listaRegiones.add("REGION 1");
                    listaRegiones.add("REGION 2");
                    listaRegiones.add("REGION 3");
                    listaRegiones.add("REGION 4");
                    listaRegiones.add("REGION 5");
                    listaRegiones.add("REGION 6");
                    listaRegiones.add("REGION 7");
                    listaRegiones.add("REGION 8");
                    listaRegiones.add("REGION 9");
                    perfil.setNodoEmpleado("N/A");
                    datosOkSis = true;
                } else if (perfilConsultas.valdiaADM(perfil.getNumeroEmpleado(), "ACCESO REMOTO VPN")) {
                    msg.mensajeError("El usuario tiene un ADM de tipo ACCESO REMOTO VPN en ejecucion");
                    LimpiaVariables();
                }
                archivo = true;
                break;
            default:
                datosOk = false;
                msg.mensajeError(perfilConsultas.getMensajeConsultasPerfil());
                LimpiaVariables();
                break;
        }
        return datosOkSis;
    }

    /**
     * función para agregar nuevos perfiles, dependiendo de la combinación del sistema y de la región,
     * el sistema valida la región y hace la actualización en la lista de perfiles del formulario AltaADM
     * @return
     */
    public void validaRegionPerfil() {
        //boolean datosOkvR = false;
        listaOpciones = new ArrayList<>();
        listaOpciones = fncConsultarCampanias();
        
        for (int i = 1; i < listaOpciones.size() ; i++ ){
            System.out.println("lllll");
           // System.out.println(listaOpciones.get(i));
        }
        
        
/*      switch (perfil.getRegionEmpleado()) {
            case "REGION 1":
                listaOpciones.add("DISTRIBUIDORES");
                listaOpciones.add("SOPORTE ALCATEL MKT");
                listaOpciones.add("SOPORTE CAJEROS RECEPTORES");
                listaOpciones.add("SOPORTE INTELLINX");
                listaOpciones.add("SOPORTE ROAMING");
                listaOpciones.add("SOPORTE_MENTORLMS ");
                listaOpciones.add("RADIOFREC_PARAMETROS");
                listaOpciones.add("SOPORTE_SISTEMAS_R9");
                listaOpciones.add("SAP_V6");
                listaOpciones.add("UNETE_TELCEL");
                listaOpciones.add("DASH_MICROONDAS_DIM");
                listaOpciones.add("IMPLANTACION_MASTERCLAW");
                listaOpciones.add("PONTIS");
                listaOpciones.add("GTID");
                listaOpciones.add("PONTIS_GTID");
                listaOpciones.add("VPN SOPORTE_SIST_R5");
                listaOpciones.add("ACUNETIX");
                listaOpciones.add("SOPORTE_SIST_R5");
                listaOpciones.add("SVA_NOKIA_RIO");
                listaOpciones.add("SOPORTE_MAVENIR");
                listaOpciones.add("SOPORTE_SISEA");
                listaOpciones.add("SOPORTE_TRAP");
                listaOpciones.add("SVA_HEWLETT-PACKARD");
                listaOpciones.add("IDINGMANTTO");
                listaOpciones.add("IDINGMBIBA");
                listaOpciones.add("IDINGAPROVD");
                listaOpciones.add("IDINGVIRTD");
                listaOpciones.add("IDINGMD");
                listaOpciones.add("ASD");
                listaOpciones.add("ISD");
                listaOpciones.add("ESD");
                listaOpciones.add("SOPWINDOWS");
                listaOpciones.add("ASEING");
                listaOpciones.add("VFH_AU_BUDA");
                listaOpciones.add("JEFE_FINANZAS_OID");
                listaOpciones.add("SOPORTE_IAP");
                listaOpciones.add("VDD_ICEO");
                listaOpciones.add("VY4_GPOA");
                listaOpciones.add("VFD_NBO");
                listaOpciones.add("VFK_ARQDATOS");
                listaOpciones.add("VP3_NEKOTEC");
                listaOpciones.add("VFK_NAGARRO");
                listaOpciones.add("VF9_SAP.TI");
                listaOpciones.add("VY1_USERPC");
                break;
            case "REGION 2":
                listaOpciones.add("DISTRIBUIDORES");
                listaOpciones.add("SOPORTE ALCATEL MKT");
                listaOpciones.add("SOPORTE CAJEROS RECEPTORES");
                listaOpciones.add("SOPORTE INTELLINX");
                listaOpciones.add("SOPORTE ROAMING");
                listaOpciones.add("SOPORTE_MENTORLMS ");
                listaOpciones.add("RADIOFREC_PARAMETROS");
                listaOpciones.add("SOPORTE_SISTEMAS_R9");
                listaOpciones.add("SAP_V6");
                listaOpciones.add("UNETE_TELCEL");
                listaOpciones.add("DASH_MICROONDAS_DIM");
                listaOpciones.add("PAPERLESS");
                listaOpciones.add("IMPLANTACION_MASTERCLAW");
                listaOpciones.add("PONTIS");
                listaOpciones.add("GTID");
                listaOpciones.add("PONTIS_GTID");
                listaOpciones.add("VPN SOPORTE_SIST_R5");
                listaOpciones.add("ACUNETIX");
                listaOpciones.add("SOPORTE_SIST_R5");
                listaOpciones.add("SVA_NOKIA_RIO");
                listaOpciones.add("SOPORTE_MAVENIR");
                listaOpciones.add("SOPORTE_SISEA");
                listaOpciones.add("SOPORTE_TRAP");
                listaOpciones.add("SVA_HEWLETT-PACKARD");
                listaOpciones.add("IDINGMANTTO");
                listaOpciones.add("IDINGMBIBA");
                listaOpciones.add("IDINGAPROVD");
                listaOpciones.add("IDINGVIRTD");
                listaOpciones.add("IDINGMD");
                listaOpciones.add("ASD");
                listaOpciones.add("ISD");
                listaOpciones.add("ESD");
                listaOpciones.add("SOPWINDOWS");
                listaOpciones.add("ASEING");
                listaOpciones.add("VFH_AU_BUDA");
                listaOpciones.add("JEFE_FINANZAS_OID");
                listaOpciones.add("SOPORTE_IAP");
                listaOpciones.add("VDD_ICEO");
                listaOpciones.add("VY4_GPOA");
                listaOpciones.add("VFD_NBO");
                listaOpciones.add("VFK_ARQDATOS");
                listaOpciones.add("VP3_NEKOTEC");
                listaOpciones.add("VFK_NAGARRO");
                listaOpciones.add("VF9_SAP.TI");
                listaOpciones.add("VY1_USERPC");
                break;
            case "REGION 3":
                listaOpciones.add("DISTRIBUIDORES");
                listaOpciones.add("SOPORTE ALCATEL MKT");
                listaOpciones.add("SOPORTE CAJEROS RECEPTORES");
                listaOpciones.add("SOPORTE INTELLINX");
                listaOpciones.add("SOPORTE ROAMING");
                listaOpciones.add("SOPORTE_MENTORLMS ");
                listaOpciones.add("RADIOFREC_PARAMETROS");
                listaOpciones.add("SOPORTE_SISTEMAS_R9");
                listaOpciones.add("SAP_V6");
                listaOpciones.add("UNETE_TELCEL");
                listaOpciones.add("DASH_MICROONDAS_DIM");
                listaOpciones.add("PAPERLESS");
                listaOpciones.add("IMPLANTACION_MASTERCLAW");
                listaOpciones.add("PONTIS");
                listaOpciones.add("GTID");
                listaOpciones.add("PONTIS_GTID");
                listaOpciones.add("VPN SOPORTE_SIST_R5");
                listaOpciones.add("ACUNETIX");
                listaOpciones.add("SOPORTE_SIST_R5");
                listaOpciones.add("SVA_NOKIA_RIO");
                listaOpciones.add("SOPORTE_MAVENIR");
                listaOpciones.add("SOPORTE_SISEA");
                listaOpciones.add("SOPORTE_TRAP");
                listaOpciones.add("SVA_HEWLETT-PACKARD");
                listaOpciones.add("IDINGMANTTO");
                listaOpciones.add("IDINGMBIBA");
                listaOpciones.add("IDINGAPROVD");
                listaOpciones.add("IDINGVIRTD");
                listaOpciones.add("IDINGMD");
                listaOpciones.add("ASD");
                listaOpciones.add("ISD");
                listaOpciones.add("ESD");
                listaOpciones.add("SOPWINDOWS");
                listaOpciones.add("ASEING");
                listaOpciones.add("VFH_AU_BUDA");
                listaOpciones.add("JEFE_FINANZAS_OID");
                listaOpciones.add("SOPORTE_IAP");
                listaOpciones.add("VDD_ICEO");
                listaOpciones.add("VY4_GPOA");
                listaOpciones.add("VFD_NBO");
                listaOpciones.add("VFK_ARQDATOS");
                listaOpciones.add("VP3_NEKOTEC");
                listaOpciones.add("VFK_NAGARRO");
                listaOpciones.add("VF9_SAP.TI");
                listaOpciones.add("VY1_USERPC");
                break;
            case "REGION 4":
                listaOpciones.add("DISTRIBUIDORES");
                listaOpciones.add("SOPORTE ALCATEL MKT");
                listaOpciones.add("SOPORTE CAJEROS RECEPTORES");
                listaOpciones.add("SOPORTE INTELLINX");
                listaOpciones.add("SOPORTE ROAMING");
                listaOpciones.add("SOPORTE_MENTORLMS ");
                listaOpciones.add("RADIOFREC_PARAMETROS");
                listaOpciones.add("SOPORTE_SISTEMAS_R9");
                listaOpciones.add("SAP_V6");
                listaOpciones.add("UNETE_TELCEL");
                listaOpciones.add("DASH_MICROONDAS_DIM");
                listaOpciones.add("PAPERLESS");
                listaOpciones.add("IMPLANTACION_MASTERCLAW");
                listaOpciones.add("PONTIS");
                listaOpciones.add("GTID");
                listaOpciones.add("PONTIS_GTID");
                listaOpciones.add("VPN SOPORTE_SIST_R5");
                listaOpciones.add("ACUNETIX");
                listaOpciones.add("SOPORTE_SIST_R5");
                listaOpciones.add("SVA_NOKIA_RIO");
                listaOpciones.add("SOPORTE_MAVENIR");
                listaOpciones.add("SOPORTE_SISEA");
                listaOpciones.add("SOPORTE_TRAP");
                listaOpciones.add("SVA_HEWLETT-PACKARD");
                listaOpciones.add("IDINGMANTTO");
                listaOpciones.add("IDINGMBIBA");
                listaOpciones.add("IDINGAPROVD");
                listaOpciones.add("IDINGVIRTD");
                listaOpciones.add("IDINGMD");
                listaOpciones.add("ASD");
                listaOpciones.add("ISD");
                listaOpciones.add("ESD");
                listaOpciones.add("SOPWINDOWS");
                listaOpciones.add("ASEING");
                listaOpciones.add("VFH_AU_BUDA");
                listaOpciones.add("JEFE_FINANZAS_OID");
                listaOpciones.add("SOPORTE_IAP");
                listaOpciones.add("VDD_ICEO");
                listaOpciones.add("VY4_GPOA");
                listaOpciones.add("VFD_NBO");
                listaOpciones.add("VFK_ARQDATOS");
                listaOpciones.add("VP3_NEKOTEC");
                listaOpciones.add("VFK_NAGARRO");
                listaOpciones.add("VF9_SAP.TI");
                listaOpciones.add("VY1_USERPC");
                break;
            case "REGION 5":
                listaOpciones.add("DISTRIBUIDORES");
                listaOpciones.add("SOPORTE ALCATEL MKT");
                listaOpciones.add("SOPORTE CAJEROS RECEPTORES");
                listaOpciones.add("SOPORTE INTELLINX");
                listaOpciones.add("SOPORTE ROAMING");
                listaOpciones.add("SOPORTE_MENTORLMS ");
                listaOpciones.add("RADIOFREC_PARAMETROS");
                listaOpciones.add("SOPORTE_SISTEMAS_R9");
                listaOpciones.add("SAP_V6");
                listaOpciones.add("UNETE_TELCEL");
                listaOpciones.add("DASH_MICROONDAS_DIM");
                listaOpciones.add("PAPERLESS");
                listaOpciones.add("IMPLANTACION_MASTERCLAW");
                listaOpciones.add("PONTIS");
                listaOpciones.add("GTID");
                listaOpciones.add("PONTIS_GTID");
                listaOpciones.add("VPN SOPORTE_SIST_R5");
                listaOpciones.add("ACUNETIX");
                listaOpciones.add("SOPORTE_SIST_R5");
                listaOpciones.add("SVA_NOKIA_RIO");
                listaOpciones.add("SOPORTE_MAVENIR");
                listaOpciones.add("SOPORTE_SISEA");
                listaOpciones.add("SOPORTE_TRAP");
                listaOpciones.add("SVA_HEWLETT-PACKARD");
                listaOpciones.add("IDINGMANTTO");
                listaOpciones.add("IDINGMBIBA");
                listaOpciones.add("IDINGAPROVD");
                listaOpciones.add("IDINGVIRTD");
                listaOpciones.add("IDINGMD");
                listaOpciones.add("ASD");
                listaOpciones.add("ISD");
                listaOpciones.add("ESD");
                listaOpciones.add("SOPWINDOWS");
                listaOpciones.add("ASEING");
                listaOpciones.add("VFH_AU_BUDA");
                listaOpciones.add("JEFE_FINANZAS_OID");
                listaOpciones.add("SOPORTE_IAP");
                listaOpciones.add("VDD_ICEO");
                listaOpciones.add("VY4_GPOA");
                listaOpciones.add("VFD_NBO");
                listaOpciones.add("VFK_ARQDATOS");
                listaOpciones.add("VP3_NEKOTEC");
                listaOpciones.add("VFK_NAGARRO");
                listaOpciones.add("VF9_SAP.TI");
                listaOpciones.add("VY1_USERPC");
                break;
            case "REGION 6":
                listaOpciones.add("DISTRIBUIDORES");
                listaOpciones.add("SOPORTE ALCATEL MKT");
                listaOpciones.add("SOPORTE CAJEROS RECEPTORES");
                listaOpciones.add("SOPORTE INTELLINX");
                listaOpciones.add("SOPORTE ROAMING");
                listaOpciones.add("SOPORTE_MENTORLMS ");
                listaOpciones.add("RADIOFREC_PARAMETROS");
                listaOpciones.add("SOPORTE_SISTEMAS_R9");
                listaOpciones.add("SAP_V6");
                listaOpciones.add("UNETE_TELCEL");
                listaOpciones.add("DASH_MICROONDAS_DIM");
                listaOpciones.add("PAPERLESS");
                listaOpciones.add("IMPLANTACION_MASTERCLAW");
                listaOpciones.add("PONTIS");
                listaOpciones.add("GTID");
                listaOpciones.add("PONTIS_GTID");
                listaOpciones.add("VPN SOPORTE_SIST_R5");
                listaOpciones.add("ACUNETIX");
                listaOpciones.add("SOPORTE_SIST_R5");
                listaOpciones.add("SVA_NOKIA_RIO");
                listaOpciones.add("SOPORTE_MAVENIR");
                listaOpciones.add("SOPORTE_SISEA");
                listaOpciones.add("SOPORTE_TRAP");
                listaOpciones.add("SVA_HEWLETT-PACKARD");
                listaOpciones.add("IDINGMANTTO");
                listaOpciones.add("IDINGMBIBA");
                listaOpciones.add("IDINGAPROVD");
                listaOpciones.add("IDINGVIRTD");
                listaOpciones.add("IDINGMD");
                listaOpciones.add("ASD");
                listaOpciones.add("ISD");
                listaOpciones.add("ESD");
                listaOpciones.add("SOPWINDOWS");
                listaOpciones.add("ASEING");
                listaOpciones.add("VFH_AU_BUDA");
                listaOpciones.add("JEFE_FINANZAS_OID");
                listaOpciones.add("SOPORTE_IAP");
                listaOpciones.add("VDD_ICEO");
                listaOpciones.add("VY4_GPOA");
                listaOpciones.add("VFD_NBO");
                listaOpciones.add("VFK_ARQDATOS");
                listaOpciones.add("VP3_NEKOTEC");
                listaOpciones.add("VFK_NAGARRO");
                listaOpciones.add("VF9_SAP.TI");
                listaOpciones.add("VY1_USERPC");
                break;
            case "REGION 7":
                listaOpciones.add("DISTRIBUIDORES");
                listaOpciones.add("SOPORTE ALCATEL MKT");
                listaOpciones.add("SOPORTE CAJEROS RECEPTORES");
                listaOpciones.add("SOPORTE INTELLINX");
                listaOpciones.add("SOPORTE ROAMING");
                listaOpciones.add("SOPORTE_MENTORLMS ");
                listaOpciones.add("RADIOFREC_PARAMETROS");
                listaOpciones.add("SOPORTE_SISTEMAS_R9");
                listaOpciones.add("SAP_V6");
                listaOpciones.add("UNETE_TELCEL");
                listaOpciones.add("DASH_MICROONDAS_DIM");
                listaOpciones.add("PAPERLESS");
                listaOpciones.add("IMPLANTACION_MASTERCLAW");
                listaOpciones.add("PONTIS");
                listaOpciones.add("GTID");
                listaOpciones.add("PONTIS_GTID");
                listaOpciones.add("VPN SOPORTE_SIST_R5");
                listaOpciones.add("ACUNETIX");
                listaOpciones.add("SOPORTE_SIST_R5");
                listaOpciones.add("SVA_NOKIA_RIO");
                listaOpciones.add("SOPORTE_MAVENIR");
                listaOpciones.add("SOPORTE_SISEA");
                listaOpciones.add("SOPORTE_TRAP");
                listaOpciones.add("SVA_HEWLETT-PACKARD");
                listaOpciones.add("IDINGMANTTO");
                listaOpciones.add("IDINGMBIBA");
                listaOpciones.add("IDINGAPROVD");
                listaOpciones.add("IDINGVIRTD");
                listaOpciones.add("IDINGMD");
                listaOpciones.add("ASD");
                listaOpciones.add("ISD");
                listaOpciones.add("ESD");
                listaOpciones.add("SOPWINDOWS");
                listaOpciones.add("ASEING");
                listaOpciones.add("VFH_AU_BUDA");
                listaOpciones.add("JEFE_FINANZAS_OID");
                listaOpciones.add("SOPORTE_IAP");
                listaOpciones.add("VDD_ICEO");
                listaOpciones.add("VY4_GPOA");
                listaOpciones.add("VFD_NBO");
                listaOpciones.add("VFK_ARQDATOS");
                listaOpciones.add("VP3_NEKOTEC");
                listaOpciones.add("VFK_NAGARRO");
                listaOpciones.add("VF9_SAP.TI");
                listaOpciones.add("VY1_USERPC");
                break;
            case "REGION 8":
                listaOpciones.add("DISTRIBUIDORES");
                listaOpciones.add("SOPORTE ALCATEL MKT");
                listaOpciones.add("SOPORTE CAJEROS RECEPTORES");
                listaOpciones.add("SOPORTE INTELLINX");
                listaOpciones.add("SOPORTE_MENTORLMS ");
                listaOpciones.add("RADIOFREC_PARAMETROS");
                listaOpciones.add("SOPORTE_SISTEMAS_R9");
                listaOpciones.add("SAP_V6");
                listaOpciones.add("UNETE_TELCEL");
                listaOpciones.add("DASH_MICROONDAS_DIM");
                listaOpciones.add("PAPERLESS");
                listaOpciones.add("IMPLANTACION_MASTERCLAW");
                listaOpciones.add("PONTIS");
                listaOpciones.add("GTID");
                listaOpciones.add("PONTIS_GTID");
                listaOpciones.add("VPN SOPORTE_SIST_R5");
                listaOpciones.add("ACUNETIX");
                listaOpciones.add("SOPORTE_SIST_R5");
                listaOpciones.add("SVA_NOKIA_RIO");
                listaOpciones.add("SOPORTE_MAVENIR");
                listaOpciones.add("SOPORTE_SISEA");
                listaOpciones.add("SOPORTE_TRAP");
                listaOpciones.add("SVA_HEWLETT-PACKARD");
                listaOpciones.add("IDINGMANTTO");
                listaOpciones.add("IDINGMBIBA");
                listaOpciones.add("IDINGAPROVD");
                listaOpciones.add("IDINGVIRTD");
                listaOpciones.add("IDINGMD");
                listaOpciones.add("ASD");
                listaOpciones.add("ISD");
                listaOpciones.add("ESD");
                listaOpciones.add("SOPWINDOWS");
                listaOpciones.add("ASEING");
                listaOpciones.add("VFH_AU_BUDA");
                listaOpciones.add("JEFE_FINANZAS_OID");
                listaOpciones.add("SOPORTE_IAP");
                listaOpciones.add("VDD_ICEO");
                listaOpciones.add("VY4_GPOA");
                listaOpciones.add("VFD_NBO");
                listaOpciones.add("VFK_ARQDATOS");
                listaOpciones.add("VP3_NEKOTEC");
                listaOpciones.add("VFK_NAGARRO");
                listaOpciones.add("VF9_SAP.TI");
                listaOpciones.add("VY1_USERPC");
                break;
            case "REGION 9":
                listaOpciones.add("DISTRIBUIDORES");
                listaOpciones.add("SOPORTE ALCATEL MKT");
                listaOpciones.add("SOPORTE CAJEROS RECEPTORES");
                listaOpciones.add("SOPORTE INTELLINX");
                listaOpciones.add("SOPORTE ROAMING");
                listaOpciones.add("SOPORTE_MENTORLMS ");
                listaOpciones.add("RADIOFREC_PARAMETROS");
                listaOpciones.add("SOPORTE_SISTEMAS_R9");
                listaOpciones.add("SAP_V6");
                listaOpciones.add("UNETE_TELCEL");
                listaOpciones.add("DASH_MICROONDAS_DIM");
                listaOpciones.add("PAPERLESS");
                listaOpciones.add("IMPLANTACION_MASTERCLAW");
                listaOpciones.add("PONTIS");
                listaOpciones.add("GTID");
                listaOpciones.add("PONTIS_GTID");
                listaOpciones.add("VPN SOPORTE_SIST_R5");
                listaOpciones.add("ACUNETIX");
                listaOpciones.add("SOPORTE_SIST_R5");
                listaOpciones.add("SVA_NOKIA_RIO");
                listaOpciones.add("SOPORTE_MAVENIR");
                listaOpciones.add("SOPORTE_SISEA");
                listaOpciones.add("SOPORTE_TRAP");
                listaOpciones.add("SVA_HEWLETT-PACKARD");
                listaOpciones.add("IDINGMANTTO");
                listaOpciones.add("IDINGMBIBA");
                listaOpciones.add("IDINGAPROVD");
                listaOpciones.add("IDINGVIRTD");
                listaOpciones.add("IDINGMD");
                listaOpciones.add("ASD");
                listaOpciones.add("ISD");
                listaOpciones.add("ESD");
                listaOpciones.add("SOPWINDOWS");
                listaOpciones.add("ASEING");
                listaOpciones.add("VFH_AU_BUDA");
                listaOpciones.add("JEFE_FINANZAS_OID");
                listaOpciones.add("SOPORTE_IAP");
                listaOpciones.add("VDD_ICEO");
                listaOpciones.add("VY4_GPOA");
                listaOpciones.add("VFD_NBO");
                listaOpciones.add("VFK_ARQDATOS");
                listaOpciones.add("VP3_NEKOTEC");
                listaOpciones.add("VFK_NAGARRO");
                listaOpciones.add("VF9_SAP.TI");
                listaOpciones.add("VY1_USERPC");
                break;
            case "N/A":
                listaOpciones.add("N/A");
                break;
            case "CORP":*/
                /*
                listaOpciones.add("JEFE_ING");
                listaOpciones.add("JEFE_INFORMATICA");
                listaOpciones.add("JEFE_IMP_PROY");
                listaOpciones.add("JEFE_FINANZAS");
                 */
                /*listaOpciones.add("JEFE_COMERCIAL");
                listaOpciones.add("JEFE_DEUR");
                listaOpciones.add("JEFE_DISTRIBUIDORES");
                listaOpciones.add("JEFE_FINANZAS");
                listaOpciones.add("JEFE_IMPLANTACION");
                listaOpciones.add("JEFE_INFORMATICA");
                listaOpciones.add("JEFE_INGENIERIA");
                listaOpciones.add("JEFE_MERCADOTECNIA");
                listaOpciones.add("JEFE_MINISWITCH");
                listaOpciones.add("JEFE_OPERACIONES");
                listaOpciones.add("JEFE_OYM_RECEPCION");
                listaOpciones.add("JEFE_PROYECTOS");
                listaOpciones.add("JEFE_RH");
                listaOpciones.add("JEFE_SERVICIO_CLIENTES");
                listaOpciones.add("JEFE_SERVICIOS_INTER");
                listaOpciones.add("JEFE_SVA");
                listaOpciones.add("JEFE_FINANZAS_OID");
                break;
        }*/
       // return datosOkvR;
    }

    /**
     * Funcion para generar la información que se enviara al formulario del ADM
     * en REMEDY.
     *
     * @author Autor : HARL
     * @param event
     * @since 26/02/2015
     *
     *
     */
    public void generaRegistro(ActionEvent event) {
        String usuarioUniversal = "";
        LOG.info("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
        if (!perfil.getNumeroEmpleado().isEmpty() && !perfil.getCorreoEmpleado().isEmpty() && !perfil.getPerfilEmpleado().isEmpty() && !perfil.getJustificacionEmpleado().isEmpty() && !perfil.getRegionEmpleado().isEmpty()) {
            switch (perfil.getSistemaEmpleado()) {
                case "REMEDY":
                    usuarioUniversal = sesion.inciaSesion();
                    LOG.info(usuarioUniversal);
                    perfilConsultas.generaADM(perfil.getNumeroEmpleado(), perfil.getSistemaEmpleado(), perfil.getPerfilEmpleado(), perfil.getJustificacionEmpleado(), perfil.getNodoEmpleado(), perfil.getCorreoEmpleado(), perfil.getRegionEmpleado(), nombreArchivo, usuarioUniversal);
                    break;
                case "ACCESO REMOTO VPN":
                    usuarioUniversal = sesion.inciaSesion();
                    LOG.info(usuarioUniversal);
                    if (!nombreArchivo.equals("")){
                        perfilConsultas.generaADM(perfil.getNumeroEmpleado(), perfil.getSistemaEmpleado(), perfil.getPerfilEmpleado(), perfil.getJustificacionEmpleado(), perfil.getNodoEmpleado(), perfil.getCorreoEmpleado(), perfil.getRegionEmpleado(), nombreArchivo, usuarioUniversal);
                    }
                    else{
                        msg.mensajeError("Debe de adjuntar el archivo correspondiente para el ADM");
                    }
                    break;
            }
        } else if (perfil.getNumeroEmpleado().isEmpty()) {
            msg.mensajeError("El número de empleado es obligatorio");
        } else if (perfil.getCorreoEmpleado().isEmpty()) {
            msg.mensajeError("El correo de empleado es obligatorio");
        } else if (perfil.getPerfilEmpleado().isEmpty()) {
            msg.mensajeError("El perfil de empleado es obligatorio");
        } else if (perfil.getJustificacionEmpleado().isEmpty()) {
            msg.mensajeError("Debe de proporcionar una justificacion");
        } 
        LOG.info("-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
        LimpiaVariables();
    }

    /**
     * Funcion para limpiar las variables del entitie del formulario
     *
     * @author : HARL
     * @since 26/02/2015
     *
     */
    public void LimpiaVariables() {
        perfil.setNumeroEmpleado("");
        perfil.setPerfilEmpleado("");
        perfil.setCorreoEmpleado("");
        perfil.setSistemaEmpleado("noAplica");
        perfil.setJustificacionEmpleado("");
        perfil.setNodoEmpleado("");
        perfil.setRegionEmpleado("");
        setNombreArchivo("");
    }

    public void limpiaVariablesFrm(ActionEvent event) {
        perfil.setNumeroEmpleado("");
        perfil.setPerfilEmpleado("");
        perfil.setCorreoEmpleado("");
        perfil.setSistemaEmpleado("noAplica");
        perfil.setJustificacionEmpleado("");
        perfil.setNodoEmpleado("");
        setNombreArchivo("");
        perfil.setRegionEmpleado("");
    }
}
