/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telcel.AltaADM.Utilerias;

import com.infomedia.utils.PropertyLoader;
import com.telcel.AltaADM.Login.Mensajes;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author INFOTELCEL
 */
public class servicesRemedy {

    public final Logger LOG = Logger.getLogger(servicesRemedy.class);
    public String LINE;
    public String urlParametros;
    public String folioIN;
    public String resultadoConsulta;

    List<String> ListaResultado = new ArrayList<>();
    Mensajes msjADM = new Mensajes();
    Properties prop = PropertyLoader.load("altaadm.properties");

    public String getResultadoConsulta() {
        return resultadoConsulta;
    }

    public void setResultadoConsulta(String resultadoConsulta) {
        this.resultadoConsulta = resultadoConsulta;
    }

    public List<String> getListaResultado() {
        return ListaResultado;
    }

    public void setListaResultado(List<String> ListaResultado) {
        this.ListaResultado = ListaResultado;
    }

    public String getFolioIN() {
        return folioIN;
    }

    public void setFolioIN(String folioIN) {
        this.folioIN = folioIN;
    }

    /**
     * Funcion para la generación del ADM, recibe los datos de las columnas y
     * del sistema para hacer la consulta del API del REMEDYCONTROL
     *
     * @author HARL
     * @param parametrosInsercion
     * @return folioIN
     * @since 25/02/2015
     */
    public String InsertaRegistro(String parametrosInsercion) {
        try {
            URL urlRemedy = new URL(prop.getProperty("URL_INSERT"));
            URLConnection conn = urlRemedy.openConnection();
            conn.setDoOutput(true);
            BufferedReader reader;
            LOG.debug(urlRemedy + parametrosInsercion);
            try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream())) {
                writer.write(parametrosInsercion);
                writer.flush();
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((LINE = reader.readLine()) != null) {
                    LOG.debug(LINE);
                    if (LINE.startsWith("<NUEVO>")) {                    
                        folioIN = LINE.substring(7, 22);
                        msjADM.mensajeInformacion(" SE GENERO EL ADM CON NÚMERO DE FOLIO:" + folioIN);

                    } else if (LINE.startsWith("<ERROR>")) {
                        folioIN = LINE.substring(7, 30);
                        msjADM.mensajeErrorFatal("OCURRIO UN ERROR INTERNO DE APLICACIÓN, FAVOR DE REPORTARLO CON EL ADMINISTRADOR DEL SISTEMA");
                    }
                }
            }
            reader.close();
        } catch (Exception exc) {
            LOG.error(exc);
        }
        return folioIN;
    }

    /**
     * Funcion para consultas de datos dependiendo del formulario que reciba, en
     * este caso sistema
     *
     * @param consultaWS
     * @return datosOk
     * @since 25/02/2015
     */
    public boolean consultaGeneral(String consultaWS) {
        boolean datosOk = false;
        try {
            resultadoConsulta = "";
            URL urlRemedy = new URL(prop.getProperty("URL_SELECT"));
            URLConnection conn = urlRemedy.openConnection();
            conn.setDoOutput(true);
            BufferedReader reader;
            LOG.debug(urlRemedy + consultaWS);
            try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream())) {
                writer.write(consultaWS);
                writer.flush();
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((LINE = reader.readLine()) != null) {
                    if (LINE.startsWith("<value>")) {
                        int numero = LINE.indexOf("</val");
                        resultadoConsulta = LINE.substring(7, numero);
                        datosOk = true;
                    } 
                }
                LOG.debug(datosOk);
            }
            reader.close();
        } catch (Exception exc) {
            LOG.error(exc);
        }
        return datosOk;
    }

    /**
     * Funcion para validar si el usuario tiene cuenta de correo electronico
     * dentro del formulario de NominaTelcel
     *
     * @author HARL
     * @param sistema
     * @param columna
     * @return datosOk
     * @since 25/02/2015
     */
    public boolean consultaCorreo(String sistema, String columna) {
        boolean datosOk = false;
        try {
            urlParametros = sistema + columna;
            URL urlRemedy = new URL(prop.getProperty("URL_SELECT"));
            URLConnection conn = urlRemedy.openConnection();
            conn.setDoOutput(true);
            BufferedReader reader;
            try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream())) {
                writer.write(urlParametros);
                writer.flush();
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((LINE = reader.readLine()) != null) {
                    if (!LINE.startsWith("<value>000") && LINE.startsWith("<value>")) {
                        int numero = LINE.indexOf("</val");
                        resultadoConsulta = LINE.substring(7, numero);
                        datosOk = true;
                    }

                }
            }
            reader.close();
        } catch (Exception exc) {
            LOG.error(exc);
        }
        return datosOk;
    }

    /**
     * Funcion para la consulta del usuario dentro del formulario de Nomina
     *
     * @author HARL
     * @param sistema
     * @param columna
     * @return datosOk
     * @since 25/02/2015
     */
    public boolean consultaUsuario(String sistema, String columna) {
        boolean datosOk = false;
        try {
            urlParametros = sistema + columna;
            URL urlRemedy = new URL(prop.getProperty("URL_SELECT"));
            URLConnection conn = urlRemedy.openConnection();
            conn.setDoOutput(true);
            BufferedReader reader;
            try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream())) {
                writer.write(urlParametros);
                writer.flush();
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((LINE = reader.readLine()) != null) {
                    if (!LINE.startsWith("<value>00") && LINE.startsWith("<value>")) {
                        int numero = LINE.indexOf("</val");
                        resultadoConsulta = LINE.substring(7, numero);
                        datosOk = true;
                    }
                }
            }
            reader.close();
        } catch (Exception exc) {
            LOG.error(exc);
        }
        return datosOk;
    }

    /**
     * Funcion que obtiene los valores finales que seran enviados al formulario
     * para la creacion del ADM
     *
     * @author HARL
     * @param columna
     * @return resultadoConsulta
     * @since 25/02/2015
     */
    public List consultaGeneralList(String columna) {
        try {
            URL urlRemedy = new URL(prop.getProperty("URL_SELECT"));
            URLConnection conn = urlRemedy.openConnection();
            conn.setDoOutput(true);
            BufferedReader reader;
            LOG.info(urlRemedy + columna);
            try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream())) {
                writer.write(columna);
                writer.flush();
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                ListaResultado.removeAll(getListaResultado());
                while ((LINE = reader.readLine()) != null) {
                    if (!LINE.startsWith("<value>00") && LINE.startsWith("<value>")) {
                        int numero = LINE.indexOf("</val");
                        if (numero <= 0) {
                            resultadoConsulta = LINE.substring(7, 10);
                            ListaResultado.add(resultadoConsulta);
                        } else {
                            resultadoConsulta = LINE.substring(7, numero);
                            ListaResultado.add(resultadoConsulta);
                        }
                    }
                }
            }
            reader.close();
        } catch (Exception exc) {
            LOG.error(exc);
        }
        return ListaResultado;
    }

    /**
     * Funcion para validar que el usuario no tenga un ADM en proceso o aun este
     * en estado de pendiente
     *
     * @author HARL
     * @since 17/07/2015
     * @param sistema
     * @param columna
     * @return estatus
     */
    public String validaADM(String sistema, String columna) {
        String estatus = "NA";
        try {
            urlParametros = sistema + columna;
            URL urlRemedy = new URL(prop.getProperty("URL_SELECT"));
            URLConnection conn = urlRemedy.openConnection();
            conn.setDoOutput(true);
            BufferedReader reader;
            try (OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream())) {
                writer.write(urlParametros);
                writer.flush();
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((LINE = reader.readLine()) != null) {
                    if (LINE.startsWith("<value>")) {
                        int numero = LINE.indexOf("</val");
                        estatus = LINE.substring(7, numero);
                        LOG.info(resultadoConsulta);
                    }
                }
            }
            reader.close();
        } catch (Exception exc) {
            LOG.error(exc);
        }
        return estatus;
    }
}
