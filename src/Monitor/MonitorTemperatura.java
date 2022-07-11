/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitor;

import Conexion.Dispositivo;
import Conexion.Reglas;
import Conexion.Monitor;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import servidorsocket.*;
import servidorsocket.EventMensaje;

/**
 *
 * @author Henrry Roca Joffre
 */
public class MonitorTemperatura implements ISocketListener {

    Dispositivo cl;
    //Temperatura tem;
    Monitor sen;

    public MonitorTemperatura() {
        this.cl = new Dispositivo();
        this.sen = new Monitor();
        //this.tem = new Temperatura();
    }

    @Override
    public void onClienteConectado(EventConexion e) {
        String idCliente = getIdDeLaTrama(e.getDato().getMsg());
        if (idCliente.equals(e.getDato().getIdCliente())) {
            cl.setId(Integer.parseInt(e.getDato().getIdCliente()));
        } else {
            cl.setId(Integer.parseInt(idCliente));
        }
        cl.setEstado(true);
        if (!cl.existe(cl.getId())) {
            cl.agregar(cl);
        } else {
            cl.setEstadoDisp(cl);
            System.out.println("Dispositivo( " + cl.getId() + " ): Bienvenido!");
        }
        cl.pushNotificacionOnClientConnected(String.valueOf(cl.getId()));
    }
    //hacer la api!!!!

    @Override
    public void onClienteDesconectado(EventConexion e) {
        String idCliente = getIdDeLaTrama(e.getDato().getMsg());
        if (idCliente.equals(e.getDato().getIdCliente())) {
            cl.setId(Integer.parseInt(e.getDato().getIdCliente()));
        } else {
            cl.setId(Integer.parseInt(idCliente));
        }
        if (cl.existe(cl.getId())) {
            cl.setEstado(false);
            cl.setEstadoDisp(cl);
            System.out.println("Dispositivo( " + cl.getId() + " ): Adios!");
        }
        //hacer la api!!!
        cl.pushNotificationOnClienteDesconnected(String.valueOf(cl.getId()));
    }

    @Override
    public void onMensajeCliente(EventMensaje e) {
        try {
            LinkedList<String> lista = listaDeDatos(e.getMensage());
            if (!lista.isEmpty()) {
                guardarTem(lista);
                Reglas r = new Reglas();
                System.out.println("verificando temperatura...");
                //r.verificarReglas(sen.getTemp());
            } else {
                System.out.println("EL DISPOSITIVO( " + e.getDato().getIdCliente() + " ) No esta siguiendo el protocolo");
            }

        } catch (IOException ex) {
            Logger.getLogger(MonitorTemperatura.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public LinkedList<String> listaDeDatos(String parse) {
        LinkedList<String> l1 = new LinkedList<>();
        String dato = "";
        int posBarra = 0;
        for (int i = 0; i < 4; i++) {
            int posIncial = parse.indexOf("=");
            if (posIncial == -1) {
                return new LinkedList<>();
            }
            if (i == 3) {
                dato = parse.substring(posIncial + 1, parse.length());
                parse = parse.substring(posIncial + 1, parse.length());

            } else {
                posBarra = parse.indexOf("|");
                if (posBarra == -1) {
                    return new LinkedList<>();
                }
                dato = parse.substring(posIncial + 1, posBarra);
                parse = parse.substring(posBarra + 1, parse.length());
            }
            l1.add(dato);

        }
        return l1;
    }

    public String getIdDeLaTrama(String trama) {
        int posIgual = trama.indexOf("=");
        int posBarra = trama.indexOf("|");
        if (posIgual != -1 && posBarra != -1) {
            return trama.substring(posIgual + 1, posBarra);
        } else {
            return "-1";
        }
    }

    public void guardarTem(LinkedList<String> lista) {
        String id = lista.get(0);
        String temp = lista.get(1);
        String hum = lista.get(2);
        String tiempo = lista.get(3);
        if (id.equals("")) {
            System.out.println("Debe especificar su Id Asignado!!!");
            return;
        }
        if (temp.equals("")) {
            temp = "0.0";
        }
        if (hum.equals("")) {
            hum = " 0.0";
        }
        if (tiempo.equals("")) {
            tiempo = "0.0";
        }
        sen.setIdCliente(Integer.parseInt(id));
        sen.setTemp(Double.parseDouble(temp));
        sen.setHum(Double.parseDouble(hum));
        sen.setTiempo(tiempo);
        sen.toSring();
        cl.setId(Integer.parseInt(id));
        cl.setTem(Double.parseDouble(temp));
        cl.setHum(Double.parseDouble(hum));
        String fechaReg = sen.agregar(sen);
        if (!fechaReg.equals("-1")) {
            cl.setUltimoRegistro(fechaReg);
            cl.updateRegistro(cl);
        }
    }

    public static void main(String[] args) throws Exception {
        Properties propiedades = new Properties();
        propiedades.load(new FileReader("datos.properties"));
        ServidorSocket servidor = new ServidorSocket(Integer.parseInt(propiedades.getProperty("Puerto")));
        servidor.getConexionCliente().start();

    }
}
