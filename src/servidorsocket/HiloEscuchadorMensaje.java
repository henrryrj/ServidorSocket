/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsocket;

import java.io.BufferedInputStream;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import javax.swing.event.EventListenerList;

/**
 *
 * @author stephani
 */
public class HiloEscuchadorMensaje extends Thread {

    private final Socket clienteSocket;
    private final boolean Contiene = true;
    private final EventListenerList listSocketListener;
    private BufferedInputStream in;
    private final DataConexion datos;

    HiloEscuchadorMensaje(ServidorSocket owner, Socket s, DataConexion e) {
        clienteSocket = s;
        listSocketListener = new EventListenerList();
        this.datos = e;
    }

    void addEscuchadorMensaje(ISocketListener l, ISocketListener m) {
        listSocketListener.add(ISocketListener.class, l);
        listSocketListener.add(ISocketListener.class, m);

    }

    void removeEscuchadorMensaje(ISocketListener l,ISocketListener m) {
        listSocketListener.remove(ISocketListener.class, l);
        listSocketListener.remove(ISocketListener.class, m);

    }

    @Override
    public void run() {
        try {
            in = new BufferedInputStream(clienteSocket.getInputStream(),128);
            while (Contiene) {
                System.out.println("Escuchando mensaje del cliente...");
                byte[] clienteCommando=new byte[128];
                in.read(clienteCommando);
                String m = new String(clienteCommando, StandardCharsets.UTF_8);
                DespachadorEventoMensaje(new EventMensaje(this, m, this.datos));
            }
        } catch (IOException e) {
            System.out.println("cliente desconectado. Dejando de escuchar mensajes...");
        } finally {
            try {
                EventConexion evtConexion = new EventConexion(this, new DataConexion(clienteSocket.getPort() + "", clienteSocket.getLocalAddress() + "", clienteSocket, ""));
                CerrarEventoConexion(evtConexion);
                in.close();
                clienteSocket.close();
            } catch (IOException e) {
                System.err.println(e);
            }
        }

    }

    protected void DespachadorEventoMensaje(EventMensaje e) {
        ISocketListener[] ls = listSocketListener.getListeners(ISocketListener.class);
        for (ISocketListener l : ls) {
            l.onMensajeCliente(e);
        }
    }

    protected void CerrarEventoConexion(EventConexion e) {
        ISocketListener[] ls = listSocketListener.getListeners(ISocketListener.class);
        for (ISocketListener l : ls) {
            l.onClienteDesconectado(e);
        }
    }
}
