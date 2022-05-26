/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitor;

import Conexion.Cliente;
import Conexion.Reglas;
import Conexion.Temperatura;
import Mail.Mail;
import java.io.FileInputStream;
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
    Cliente cl;
    Temperatura tem;

    public MonitorTemperatura() {
        this.cl = new Cliente();
        this.tem = new Temperatura();
    }


    @Override
    public void onClienteConectado(EventConexion e) {
        // que se va implementar???
    }

    @Override
    public void onClienteDesconectado(EventConexion e) {
        // Aqui ya no hay drama XD
    }

    @Override
    public void onMensajeCliente(EventMensaje e) {
        LinkedList<String> lista=listaDeDatos(e.getMensage());
        System.out.println(lista.toString());
//        cl.setId(Integer.parseInt(e.getDato().getIdCliente()));
//        if (!cl.existe(Integer.parseInt(e.getDato().getIdCliente()))) {
//            cl.agregar(cl);
//        }else{
//            System.out.println("Gracias por volver a conectarte....");
//        }
//        tem.setTemperatura(Double.parseDouble(e.getMensage()));
//        tem.setIdCliente(cl.getId());
//        tem.toSring();
//        String mensaje = e.getMensage();
//        tem.setTemperatura(Double.parseDouble(mensaje));
//        tem.setIdCliente(Integer.parseInt(e.getDato().getIdCliente()));
//        tem.agregar(tem);
//        System.out.println("Temperatura guardada...");
//        try {
//            Reglas r=new Reglas();
//            r.verificarReglas(Double.parseDouble(mensaje));
//        } catch (IOException | SQLException | MessagingException ex) {
//            Logger.getLogger(MonitorTemperatura.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    public LinkedList<String> listaDeDatos(String parse){
        LinkedList<String> l1=new LinkedList<>();
        String dato="";
        int posBarra=0;
        for (int i = 0; i < 4; i++) {
            int posIncial=parse.indexOf("=");
            if (i==3) {
                dato=parse.substring(posIncial+1,parse.length());
                parse=parse.substring(posIncial+1, parse.length());
                

            }else{
                posBarra=parse.indexOf("|");
                dato=parse.substring(posIncial+1,posBarra);
                parse=parse.substring(posBarra+1, parse.length());
            }
            l1.add(dato);
        }
        return l1;
    }
    public static void main(String[] args) throws Exception {
        Properties propiedades = new Properties();
        propiedades.load(new FileReader("datos.properties"));
        ServidorSocket servidor = new ServidorSocket(Integer.parseInt(propiedades.getProperty("Puerto")));
        servidor.getConexionCliente().start();
        
    }
}
