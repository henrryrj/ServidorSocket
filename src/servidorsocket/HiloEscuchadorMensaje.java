/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsocket;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.event.EventListenerList;

/**
 *
 * @author stephani
 */
public class HiloEscuchadorMensaje extends Thread {

    private final Socket clienteSocket;
    private final boolean Contiene = true;
    private final EventListenerList listSocketListener;
    private DataInputStream in;
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
            in = new DataInputStream(clienteSocket.getInputStream());
            while (Contiene) {
                System.out.println("Escuchando mensaje del cliente...");
                String clienteCommando = in.readUTF();
                DespachadorEventoMensaje(new EventMensaje(this, clienteCommando, this.datos));
            }
        } catch (IOException e) {
            System.err.println(e);
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
