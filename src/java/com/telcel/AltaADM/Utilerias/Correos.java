/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telcel.AltaADM.Utilerias;

import com.infomedia.utils.PropertyLoader;
import com.telcel.AltaADM.Login.ControlLogin;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author AMCruz
 */
public class Correos {

    Properties voPropertiesproperties;
    javax.mail.Session veEmailSession;
    servicesRemedy voRemedy = new servicesRemedy();
    Properties prop = PropertyLoader.load("altaadm.properties");
    public final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ControlLogin.class);

    private void init() {
        voPropertiesproperties = new Properties();
        voPropertiesproperties.put("mail.host", prop.getProperty("HOST_MAIL"));
        voPropertiesproperties.put("mail.user", prop.getProperty("USER_MAIL"));
        voPropertiesproperties.put("mail.transport.protocol", "smtp");
        voPropertiesproperties.put("mail.smtp.sendpartial", "true");
        voPropertiesproperties.put("mail.debug", "true");
        veEmailSession = javax.mail.Session.getInstance(voPropertiesproperties,
                new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(prop.getProperty("USER_MAIL"), prop.getProperty("G3l4t#n4"));
            }
        });
        veEmailSession.setDebug(true);
    }

    public void fncEnviarCorreo(String psCorreo, String psPass) throws MessagingException {
        LOG.info("CORREO: " + psCorreo);
        LOG.info("PASS: " + psPass);
        try {

            if (!psCorreo.isEmpty() || psCorreo != null || !psCorreo.equals("NA")) {
                init();
                MimeMessage voMensaje = new MimeMessage(veEmailSession);
                voMensaje.setFrom(new InternetAddress("soporte.tid@mail.telcel.com"));
                voMensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(psCorreo));
                //voMensaje.addRecipient(Message.RecipientType.TO, new InternetAddress("edson.pichardo@telcel.com"));
                voMensaje.setSubject(prop.getProperty("MOTIVO_MAIL"));
                voMensaje.setText(prop.getProperty("MENSAJE_MAIL")
                        + "\nSu Contraseña es: " + psPass);

                Transport voTransport = veEmailSession.getTransport("smtp");
                voTransport.connect();
                voTransport.sendMessage(voMensaje, voMensaje.getAllRecipients());
                voTransport.close();
            }

        } catch (AddressException ex) {
            LOG.error("ERROR AL ENVIAR EL CORREO: "+ex);
        }
    }

}
