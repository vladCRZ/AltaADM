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
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.log4j.Logger;

/**
 *
 * @author INFOTELCEL
 */
public class ConsultasPerfil implements Serializable {

    public final Logger LOG = Logger.getLogger(ConsultasPerfil.class);

    Mensajes msg = new Mensajes();
    private String mensajeConsultasPerfil;
    private String correoEmpleado;
    SesionUsuario sesion = new SesionUsuario();

    servicesRemedy sRemedy = new servicesRemedy();

    Properties prop;

    public ConsultasPerfil() {
        prop = PropertyLoader.load("altaadm.properties");
    }

    public String getMensajeConsultasPerfil() {
        return mensajeConsultasPerfil;
    }

    public String getCorreoEmpleado() {
        return correoEmpleado;
    }

    /**
     * Funcion para la consulta del correo electronico del usuario.
     *
     * @author HARL
     * @since 25/02/2015
     * @param numeroEmpleado
     * @return datosOk
     *
     */
    public boolean consultaEmpleado(String numeroEmpleado) {
        boolean datosNE = false;
        try {
            String columna = "";

            columna += prop.getProperty("CLIENTE_ARS");
            columna += prop.getProperty("FORM_NOMINA");
            columna += prop.getProperty("DAT_NOMINA2");
            columna += "&cCondiciones=";
            columna += "'536870913'='" + numeroEmpleado + "'";

            if (sRemedy.consultaGeneral(columna)) {
                datosNE = true;
            } else {
                mensajeConsultasPerfil = "Favor de proporcionar un número de empleado valido";
                datosNE = false;
            }
        } catch (Exception exc) {
            LOG.error(exc);
        }
        return datosNE;
    }

    public boolean consultaCorreo(String numeroEmpleado) {
        boolean datosOk = false;
        try {
            String columna = "";

            columna += prop.getProperty("CLIENTE_ARS");
            columna += prop.getProperty("FORM_NOMINA");
            columna += prop.getProperty("DAT_NOMINA3");
            columna += "&cCondiciones=";
            columna += "'536870913'='" + numeroEmpleado + "'";
            //String sistema = "cSistema=ADM&cForma=NominaTelcel";
            //String columna = "&cColumnas=536870918&cCondiciones='536870913'='" + numeroEmpleado + "'";// '536870913'='"+numEmpleado+"'";
            if (sRemedy.consultaGeneral(columna) && !sRemedy.getResultadoConsulta().contains("000000")) {
                correoEmpleado = sRemedy.getResultadoConsulta();
                datosOk = true;
            } else {
                mensajeConsultasPerfil = "Favor de proporcionar una dirección de correo electrónico";
                datosOk = false;
            }
        } catch (Exception exc) {
            LOG.error(exc);
        }
        return datosOk;
    }

    /**
     * Función para la consulta del usuario dentro del formulario de people
     *
     * @author HARL
     * @since 14/07/2015
     * @param numeroEmpleado
     * @return datosOk
     *
     */
    public boolean consultaUsuario(String numeroEmpleado) {
        LOG.debug("CONSULTA USUARIO DENTRO DE PEOPLE");
        boolean datosOk = false;
        try {
            String columna = "";
            columna += prop.getProperty("CLIENTE_ARS");
            columna += prop.getProperty("FORM_PEOPLE");
            columna += prop.getProperty("DAT_PEOPLE1");
            columna += "&cCondiciones='7'='2' ";
            columna += "'1000000054'='" + numeroEmpleado + "'";
            datosOk = sRemedy.consultaGeneral(columna);
        } catch (Exception exc) {
            LOG.error(exc);
        }
        return datosOk;
    }

    /**
     * @author HARL
     * @param numeroEmpleado
     * @param sistemaEmpleado
     * @return datosOkADM
     */
    public boolean valdiaADM(String numeroEmpleado, String sistemaEmpleado) {
        LOG.debug("CONSULTA ADM DEL USUARIO");
        boolean datosOkADM = false;
        try {
            String columna = "";
            columna += prop.getProperty("CLIENTE_ARS");
            columna += prop.getProperty("FORM_ADMCOM");
            columna += prop.getProperty("DAT_ADMCOM1");
            columna += "&cCondiciones='7'<='1' '536870919'='0' ";
            columna += "'536870913'='" + sistemaEmpleado + "' ";
            columna += "'536870964'='" + numeroEmpleado + "'";
            datosOkADM = sRemedy.consultaGeneral(columna);
        } catch (Exception exc) {
            LOG.error(exc);
        }
        return datosOkADM;
    }

    /*
    funcion que valida la fecha de expiracion del ADM
     */
    public boolean validaADM2(String numeroEmpleado, String sistemaEmpleado) {
        LOG.debug("VALIDA LA FECHA DE VIGENCIA DEL ADM");
        boolean datosOkADM = false;
        try {
            Date FechaF = new Date();
            Date today = new Date();
            SimpleDateFormat format_F = new SimpleDateFormat("dd/MM/yyyy");
            String Fech_Actual = format_F.format(today);
            Date Fecha = format_F.parse(Fech_Actual);
            String columna2 = "";

            columna2 += prop.getProperty("CLIENTE_ARS");
            columna2 += prop.getProperty("FORM_ADMCOM");
            columna2 += prop.getProperty("DAT_ADMCOM2");
            columna2 += "&cCondiciones=";
            columna2 += "'536870913'='" + sistemaEmpleado + "' ";
            columna2 += "'536870964'='" + numeroEmpleado + "'";
            sRemedy.consultaGeneral(columna2);
            LOG.debug(sRemedy.getResultadoConsulta());
            if (!sRemedy.getResultadoConsulta().isEmpty()) {
                FechaF = format_F.parse(sRemedy.getResultadoConsulta());
                if (sRemedy.getResultadoConsulta().isEmpty() || Fecha.compareTo(FechaF) > 0) {
                    datosOkADM = true;
                } else {
                    datosOkADM = false;
                }
            }
        } catch (Exception exc) {
            LOG.error(exc);
        }
        return datosOkADM;
    }

    /**
     * Función que primero obtiene los datos de los campos obligatorios del
     * usuario para la creacion del ADM
     *
     * @author HARL
     * @since 25/02/2015
     * @param numeroEmpleado,
     * @param sistemaEmpleado,
     * @param perfilEmpleado,
     * @param justificacionEmpleado,
     * @param nodoEmpleado,
     * @param correoEmpleado,
     * @param archivADM
     * @return datosOk
     *
     */
        public boolean generaADM(String numeroEmpleado, String sistemaEmpleado, String perfilEmpleado, String justificacionEmpleado, String nodoEmpleado, String correoEmpleado, String regionEmpleado, String archivADM, String usuarioUniversal) {
        boolean datosOk = false;
        try {
            String columna = "";
            String columna2 = "";
            String columna3 = "";
            columna += prop.getProperty("CLIENTE_ARS");
            columna += prop.getProperty("FORM_NOMINA");
            columna += prop.getProperty("DAT_NOMINA4");
            columna += "&cCondiciones=";
            columna += "'536870913'='" + numeroEmpleado + "'";

            //String sistema = "cSistema=ADM&cForma=NominaTelcel";
            //String columna1 = "&cColumnas=536870913%20536870914%20536870915%20536870916%20536870917%20536870919%20536870920%20536870922%20536870923%20536870925&cCondiciones=%27536870913%27=%27" + numeroEmpleado + "%27";// '536870913'='"+numEmpleado+"'";
            sRemedy.consultaGeneralList(columna);
            String solNEmpleado = sRemedy.getListaResultado().get(0);
            String solUUniversal = sRemedy.getListaResultado().get(1).toLowerCase();
            String solnombre = sRemedy.getListaResultado().get(4);
            String solPaterno = sRemedy.getListaResultado().get(2);
            String solMaterno = sRemedy.getListaResultado().get(3);
            String solDirEmpleado = sRemedy.getListaResultado().get(6);
            String solGEmpleado = sRemedy.getListaResultado().get(7);
            String solDepEmpleado = sRemedy.getListaResultado().get(8);
            String solPuesto = sRemedy.getListaResultado().get(9);
            String solTipo = sRemedy.getListaResultado().get(10);

            columna2 += prop.getProperty("CLIENTE_ARS");
            columna2 += prop.getProperty("FORM_NOMINA");
            columna2 += prop.getProperty("DAT_NOMINA5");
            columna2 += "&cCondiciones=";
            columna2 += "'536870913'='" + usuarioUniversal + "'";

            //String columna11 = "&cColumnas=536870913 536870915 536870916 536870917 536870918&cCondiciones='536870913'='" + usuarioUniversal + "'";//nombre--536870925
            sRemedy.consultaGeneralList(columna2);
            String usuarioNum = sRemedy.getListaResultado().get(0);
            String usuarioSol = sRemedy.getListaResultado().get(3) + " " + sRemedy.getListaResultado().get(1) + " " + sRemedy.getListaResultado().get(2);
            String correoSol = sRemedy.getListaResultado().get(4);

            columna3 += prop.getProperty("CLIENTE_ARS");
            columna3 += prop.getProperty("FORM_ADMPTE");
            columna3 += prop.getProperty("INS_ADMPTE");
            columna3 += "'590000007'='" + solUUniversal + "' ";
            columna3 += "'536870943'='" + solNEmpleado + "' ";
            columna3 += "'536870935'='" + regionEmpleado + "' ";
            columna3 += "'536870940'='" + regionEmpleado + "' ";
            columna3 += "'536870916'='" + justificacionEmpleado + "' ";
            columna3 += "'536870944'='" + solnombre + "' ";
            columna3 += "'536870948'='" + solPaterno + "' ";
            columna3 += "'536870949'='" + solMaterno + "' ";
            columna3 += "'536870947'='" + correoEmpleado + "' ";
            columna3 += "'536870942'='" + solPuesto.substring(0, 10) + "' ";
            columna3 += "'536870921'='" + solDirEmpleado + "' ";
            columna3 += "'536870922'='" + solGEmpleado + "' ";
            columna3 += "'536870923'='" + solDepEmpleado + "' ";
            columna3 += "'536870933'='" + sistemaEmpleado + "' ";
            columna3 += "'536870934'='" + nodoEmpleado + "' ";            
            columna3 += "'536870936'='" + perfilEmpleado + "' ";
            columna3 += "'536871022'='" + usuarioSol + "' ";
            columna3 += "'536871023'='" + usuarioNum + "' ";
            columna3 += "'536870931'='" + solTipo + "' ";            
            columna3 += "'536870938'='<FILE>" + archivADM + "</FILE>'";

            if (sRemedy.InsertaRegistro(columna3).startsWith("ADM")) {
                enviaCorreo(correoSol, sistemaEmpleado, sRemedy.folioIN, solnombre, solPaterno, solMaterno);
                datosOk = true;
            }

            //String columnas = "%27536870962%27=%273%27%20%27536870919%27=%27Alta%27%20%27590000007%27=%27" + solUUniversal + "%27%20%27536870943%27=%27" + solNEmpleado + "%27%20%27536870944%27=%27" + solnombre + "%27%20%27536870948%27=%27" + solPaterno + "%27%20%27536870949%27=%27" + solMaterno + "%27%20%27536870947%27=%27" + correoEmpleado + "%27%20%27536870942%27=%27" + solPuesto.substring(0, 10) + "%27%20%27536870921%27=%27" + solDirEmpleado + "%27%20%27536870922%27=%27" + solGEmpleado + "%27%20%27536870923%27=%27" + solDepEmpleado + "%27%20%27536870933%27=%27" + sistemaEmpleado + "%27%20%27536870934%27=%27" + nodoEmpleado + "%27%20%27536870940%27=%27" + regionEmpleado + "%27%20%27536870935%27=%27" + regionEmpleado + "%27%20%27536870936%27=%27" + perfilEmpleado + "%27%20%27536870916%27=%27" + justificacionEmpleado + "%27%20%27536871022%27=%27" + usuarioSol + "%27%20%27536871023%27=%27" + usuarioNum + "%27%20%27536870938%27=%27<FILE>" + archivADM + "</FILE>%27";
            //String urlADM = "cSistema=ADM&cForma=ADM:QTZ:PuenteCreacionADMRol&cColumnas=";
            //sRemedy.InsertaRegistro(urlADM + columnas);
            //datosOk = sRemedy.folioIN.startsWith("ADM");
            //if (datosOk) {
            //  enviaCorreo(correoSol, sistemaEmpleado, sRemedy.folioIN, solnombre, solPaterno, solMaterno);
            //}
        } catch (Exception exc) {
            datosOk = false;
            LOG.error(exc);
        }
        return datosOk;
    }

    /* ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ */
    public void enviaCorreo(String remitente, String sistemaEmpleado, String FolioADM, String solnombre, String solPaterno, String solMaterno) {

        try {
            BodyPart texto = new MimeBodyPart();
            texto.setText("ACCESOS-TM" + "\n" + "Se generó el ADM con folio: " + FolioADM + " para el acceso del usuario " + solnombre + " " + solPaterno + " " + solMaterno + " dentro de la plataforma: " + sistemaEmpleado + "\n");
            BodyPart archivo = new MimeBodyPart();
            //archivo.setDataHandler(new DataHandler(new FileDataSource("/webapps/AltaADM/ArchivosADM/Generar Solicitud ADM_SRM.pdf"))); ///webapps/AltaADM/ArchivosADM/Generar Solicitud ADM_SRM.pdf    C:/Users/INFOTELCEL/Documents/NetBeansProjects/AltaADM/web/ArchivosADM/Generar Solicitud ADM_SRM.pdf  
            //archivo.setFileName("Generar Solicitud ADM_SRM.pdf");

            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(texto);
            //multipart.addBodyPart(archivo);

            /*
             Properties prop = new Properties(); 
             prop.setProperty("mail.smtp.host", "smtp.telcel.com");
             prop.setProperty("mail.smtp.starttls.enable", "true");
             prop.setProperty("mail.smtp.port", "25");
             prop.setProperty("mail.smtp.user", "soporte.tid@mail.telcel.com");
             prop.setProperty("mail.smtp.auth", "true");
             */
            Properties prop = new Properties();
            prop.setProperty("mail.host", "smtp.telcel.com");
            prop.setProperty("mail.user", "soporte.tid");
            prop.setProperty("mail.transport.protocol", "smtp");
            prop.setProperty("mail.smtp.sendpartial", "true");

            Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("soporte.tid", "G3l4t#n4");
                }
            });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("soporte.tid@mail.telcel.com"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(remitente));
            message.addRecipient(Message.RecipientType.CC, new InternetAddress("soporte.tid@mail.telcel.com"));
            message.setSubject("ACCESOS-TM");
            message.setContent(multipart);

            Transport.send(message);
            // Transport trans = session.getTransport("smtp");
            //trans.connect("soporte.tid@mail.telcel.com", "G3l4t#n4");
            //trans.sendMessage(message, message.getAllRecipients());

            //trans.close();
            LOG.info("ENVIA EL CORREO ELECTRONICO: " + remitente);

        } catch (Exception | ExceptionInInitializerError exc) {
            exc.printStackTrace();
            LOG.error(exc);
            msg.mensajeErrorFatal("OCURRIO UN ERROR INTERNO AL ENVIAR EL CORREO, FAVOR DE REPORTARLO CON EL ADMINISTRADOR DEL SISTEMA");
        }
    }
}
