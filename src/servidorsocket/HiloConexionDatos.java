/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsocket;

import Conexion.Cliente;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.event.EventListenerList;

/**
 *
 * @author stephani
 */
public class HiloConexionDatos extends Thread {
    Cliente cl;
    ServerSocket Servidor;
    private boolean Continue = true;
    private EventListenerList listSocketListener = null;

    public HiloConexionDatos(ServerSocket Servidor) {
        this.Servidor = Servidor;
        listSocketListener = new EventListenerList();
    }

    void addEscuchadorConexion(ISocketListener l) {
        listSocketListener.add(ISocketListener.class, l);
    }

    void removeEscuchadorConexion(ISocketListener l) {
        listSocketListener.remove(ISocketListener.class, l);
    }

    public void shutdown() {
    }

    @Override
    public void run() {
        System.out.println("Server Conectado !!");
        while (Continue) {
            try {
                Socket clienteSocket = Servidor.accept();
                DataInputStream in = new DataInputStream(clienteSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clienteSocket.getOutputStream());
                out.writeUTF("Hola Soy el Socket y te he asignado el siguiente id:\nid = " + clienteSocket.hashCode() + "");
                cl = new Cliente();
                cl.setId(clienteSocket.hashCode());
                cl.agregar(cl);
                EventConexion evtConexion = new EventConexion(this, new DataConexion(clienteSocket.getPort() + "", clienteSocket.getLocalAddress() + "", clienteSocket, clienteSocket.hashCode() + ""));
                DespachadorEventoConexion(evtConexion);
            } catch (IOException e) {
            } finally {
                try {
                    System.out.println("");
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public void interrupt() {
        this.Continue = false;
    }

    protected void DespachadorEventoConexion(EventConexion e) {
        ISocketListener[] ls = listSocketListener.getListeners(ISocketListener.class);
        for (ISocketListener l : ls) {
            l.onClienteConectado(e);
        }
    }

    protected void CerrarEventoConexion(EventConexion e) {
        ISocketListener[] ls = listSocketListener.getListeners(ISocketListener.class);
        for (ISocketListener l : ls) {
            l.onClienteDesconectado(e);
        }
    }
}
