/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsocket;
import Conexion.Cliente;
import Conexion.Mensaje;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stephani
 */
public class ServidorSocket implements ISocketListener {

    private ServerSocket Servidor = null;
    private HashMap<String, DataCliente> clientesConectados;
    HiloConexionDatos conexionClientes = null;
    Cliente cl = new Cliente();
    Mensaje msg = new Mensaje();

//    Cliente cli=new Cliente();
//    Mensaje men=new Mensaje();
    
    
    

    public ServidorSocket(int Puerto) {
        try {
            Servidor = new ServerSocket(Puerto);
            clientesConectados = new HashMap<String, DataCliente>();
            conexionClientes = new HiloConexionDatos(Servidor);
            conexionClientes.addEscuchadorConexion(this);
        } catch (IOException e) {

        }
    }

    @Override
    public void onClienteConectado(EventConexion e) {
        HiloEscuchadorMensaje hem = new HiloEscuchadorMensaje(this, e.dato.getSocketCliente(), e.dato);
        hem.addEscuchadorMensaje(this);
        hem.start();
        DataCliente dc = new DataCliente(e.dato.getSocketCliente(), hem);
        this.clientesConectados.put(e.dato.getSocketCliente().hashCode() + "", dc);
        System.out.println("Nuevo Cliente Conectado: " + e.dato.getSocketCliente().hashCode() + "");
        e.dato.setIdCliente(String.valueOf(e.dato.getSocketCliente().hashCode()));
        System.out.println("Clientes conectados: " + this.clientesConectados.size());
    }

    @Override
    public void onClienteDesconectado(EventConexion e) {
        String key = e.dato.getSocketCliente().hashCode() + "";
        this.clientesConectados.remove(key);

        System.out.println("Cliente Desconectado: " + key);
        System.out.println("Clientes Conectados : " + this.clientesConectados.size());
    }

    @Override
    public void onMensajeCliente(EventMensaje e) {
        //enviarMensaje(e.mensaje,e);
        //System.out.println("mensaje enviado...");
        System.out.println("nuevo mensaje:" + e.mensaje);
    }
        public void enviarMensaje(String idMensaje, EventMensaje e) {
        DataOutputStream out = null; 
        try {
            String id = "";
            String mensaje = "";
            int posId = idMensaje.indexOf(":");
            id = idMensaje.substring(0, posId);
            cl.setId(Integer.parseInt(e.dato.getIdCliente()));
            cl.agregar(cl);
            mensaje = idMensaje.substring(posId + 1, idMensaje.length());
            //guardando a la bd
            msg.setIdClienteOrigen(Integer.parseInt(e.dato.getIdCliente()));
            msg.setIdClienteDestino(Integer.parseInt(id));
            msg.setMensaje(mensaje);
            msg.agregar(msg);
            // buscar al cliente
            DataCliente cliente = clientesConectados.get(id);   
            out = new DataOutputStream(cliente.getSocketClient().getOutputStream());
            out.writeUTF("Cli(" + e.dato.getIdCliente() + "): "+mensaje);
        } catch (IOException ex) {
            Logger.getLogger(ServidorSocket.class.getName()).log(Level.SEVERE, null, ex);
//        } finally {
//            try {
//                //out.close();
//            } catch (IOException ex) {
//                Logger.getLogger(ServidorSocket.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
        }
    }

}
