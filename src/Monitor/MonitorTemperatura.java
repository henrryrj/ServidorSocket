/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitor;

import Conexion.Cliente;
import Conexion.Temperatura;
import Mail.Mail;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
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
        cl.setId(Integer.parseInt(e.getDato().getIdCliente()));
        cl.agregar(cl);
        tem.setTemperatura(Double.parseDouble(e.getMensage()));
        tem.setIdCliente(cl.getId());
        tem.toSring();
        String mensaje = e.getMensage();
        tem.setTemperatura(Double.parseDouble(mensaje));
        tem.setIdCliente(Integer.parseInt(e.getDato().getIdCliente()));
        tem.agregar(tem);
        System.out.println("Temperatura guardada...");
        // enviar mensaje al correo....
        
    }

    public static void main(String[] args) throws Exception {
        Properties propiedades = new Properties();
        propiedades.load(new FileReader("datos.properties"));
        ServidorSocket servidor = new ServidorSocket(Integer.parseInt(propiedades.getProperty("Puerto")));
        servidor.getConexionCliente().start();
        
    }
}
