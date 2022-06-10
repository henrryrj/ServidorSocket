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
        cl.setId(Integer.parseInt(e.getDato().getIdCliente()));
        cl.setEstado(true);
        if (!cl.existe(cl.getId())) {
            cl.agregar(cl);
        } else {
            cl.setEstadoDisp(cl);
            System.out.println("Dispositivo( " + cl.getId() + " ): Bienvenido!");
        }
    }

    @Override
    public void onClienteDesconectado(EventConexion e) {
        cl.setId(Integer.parseInt(e.getDato().getIdCliente()));
        if (cl.existe(cl.getId())) {
            cl.setEstado(false);
            cl.setEstadoDisp(cl);
            System.out.println("Dispositivo( " + cl.getId() + " ): Adios! \n");
        }
    }

    @Override
    public void onMensajeCliente(EventMensaje e) {
        try {
            LinkedList<String> lista = listaDeDatos(e.getMensage());
            if (!lista.isEmpty()) {
                guardarTem(lista);
                Reglas r = new Reglas();
                System.out.println("verificando temperatura...");
                r.verificarReglas(sen.getTemp());
            } else {
                System.out.println("EL DISPOSITIVO( " + e.getDato().getIdCliente() + " ) No esta siguiendo el protocolo");
            }

        } catch (IOException | SQLException | MessagingException ex) {
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
                dato = parse.substring(posIncial + 1, parse.indexOf("\u0000"));
                parse = parse.substring(posIncial + 1, parse.indexOf("\u0000"));

            } else {
                posBarra = parse.indexOf("|");
                dato = parse.substring(posIncial + 1, posBarra);
                parse = parse.substring(posBarra + 1, parse.length());
            }
            l1.add(dato);
        }
        return l1;
    }

    public void guardarTem(LinkedList<String> lista) {
            sen.setIdCliente(Integer.parseInt(lista.get(0)));
            sen.setTemp(Double.parseDouble(lista.get(1)));
            sen.setHum(Double.parseDouble(lista.get(2)));
            sen.setTiempo(lista.get(3));
            sen.toSring();
            cl.setId(Integer.parseInt(lista.get(0)));
            cl.setTem(Double.parseDouble(lista.get(1)));
            cl.setHum(Double.parseDouble(lista.get(2)));
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
