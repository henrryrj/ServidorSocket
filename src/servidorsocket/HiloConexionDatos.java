/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsocket;


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
    ServerSocket Servidor;
    private boolean Continue = true;
    private EventListenerList listSocketListener = null;

    public HiloConexionDatos(ServerSocket Servidor) {
        this.Servidor = Servidor;
        listSocketListener = new EventListenerList();
    }

    void addEscuchadorConexion(ISocketListener l, ISocketListener m) {
        listSocketListener.add(ISocketListener.class, l);
        listSocketListener.add(ISocketListener.class, m);
    }

    void removeEscuchadorConexion(ISocketListener l, ISocketListener m) {
        listSocketListener.add(ISocketListener.class, l);
        listSocketListener.add(ISocketListener.class, m);
    }

    public void shutdown() {
    }

    @Override
    public void run() {
        System.out.println("Server Conectado!! \n");
        while (Continue) {
            try {
                Socket clienteSocket;
                clienteSocket = Servidor.accept();
                DataOutputStream out = new DataOutputStream(clienteSocket.getOutputStream());
                out.writeUTF(String.valueOf(clienteSocket.hashCode()));
                EventConexion evtConexion = new EventConexion(this, new DataConexion(String.valueOf(clienteSocket.getPort()), String.valueOf(clienteSocket.getLocalAddress()), clienteSocket, String.valueOf(clienteSocket.hashCode())));
                DespachadorEventoConexion(evtConexion);
            } catch (IOException e) {
                System.err.println("My Hilo escuchador se detuvo: " + e.getMessage());
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
