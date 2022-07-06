/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsocket;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
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
                String m = "";
                Socket clienteSocket;
                clienteSocket = Servidor.accept();
                OutputStream out = new BufferedOutputStream(clienteSocket.getOutputStream());
                out.write(String.valueOf(clienteSocket.hashCode()).getBytes());
                out.flush();
                InputStream in = clienteSocket.getInputStream();
                byte[] clienteCommando = new byte[256];
                int resp = in.read(clienteCommando, 0, 256);
                if (resp != -1) {
                    m = new String(clienteCommando, StandardCharsets.UTF_8).trim();
                    EventConexion evtConexion = new EventConexion(this, new DataConexion(String.valueOf(clienteSocket.getPort()), String.valueOf(clienteSocket.getLocalAddress()), clienteSocket, String.valueOf(clienteSocket.hashCode()), m));
                    DespachadorEventoConexion(evtConexion);
                }
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
