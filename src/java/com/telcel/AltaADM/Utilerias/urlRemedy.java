/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telcel.AltaADM.Utilerias;

/**
 *
 * @author INFOTELCEL
 */
public class urlRemedy {

    public final String urlRemedyProduccionBusqueda = "http://100.127.3.13:8080/Remedy/servicios/RMDSelect?";
    public final String urlRemedyQABusqueda = "http://100.127.3.13:8080/Remedy/servicios/RMDSelect?";

//+++++ http://10.119.155.84:8080/Remedy/servicios/RMDSelect?cSistema=ADM&cForma=CTM:People&cColumnas=1 1000000054&cCondiciones='1000000054'='19945' '7'='1'
    public final String urlRemedyProduccionInserta = "http://100.127.3.13:8080/Remedy/servicios/RMDInsert?";
    public final String urlRemedyQAInserta = "http://100.127.3.13:8080/Remedy/servicios/RMDInsert?";

    public final String urlRemedyProduccionActualiza = "http://100.127.3.13:8080/Remedy/servicios/RMDUpdate?";
    public final String urlRemedyQActualiza = "http://100.127.3.13:8080/Remedy/servicios/RMDUpdate?";

    public String getUrlRemedyProduccionBusqueda() {
        return urlRemedyProduccionBusqueda;
    }

    public String getUrlRemedyQABusqueda() {
        return urlRemedyQABusqueda;
    }

    public String getUrlRemedyProduccionInserta() {
        return urlRemedyProduccionInserta;
    }

    public String getUrlRemedyQAInserta() {
        return urlRemedyQAInserta;
    }

    public String getUrlRemedyProduccionActualiza() {
        return urlRemedyProduccionActualiza;
    }

    public String getUrlRemedyQActualiza() {
        return urlRemedyQActualiza;
    }
}
