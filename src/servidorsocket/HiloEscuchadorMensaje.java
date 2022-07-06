/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsocket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import javax.swing.event.EventListenerList;

/**
 *
 * @author stephani
 */
public class HiloEscuchadorMensaje extends Thread {

    private final Socket clienteSocket;
    private boolean bandera = true;
    private final EventListenerList listSocketListener;
    private InputStream in;
    private final DataConexion datos;

    HiloEscuchadorMensaje(ServidorSocket owner, Socket s, DataConexion e) {
        this.clienteSocket = s;
        this.datos = e;
        listSocketListener = new EventListenerList();

    }

    void addEscuchadorMensaje(ISocketListener l, ISocketListener m) {
        listSocketListener.add(ISocketListener.class, l);
        listSocketListener.add(ISocketListener.class, m);

    }

    void removeEscuchadorMensaje(ISocketListener l, ISocketListener m) {
        listSocketListener.remove(ISocketListener.class, l);
        listSocketListener.remove(ISocketListener.class, m);
    }

    @Override
    public void run() {
        try {
            in = clienteSocket.getInputStream();
            while (bandera) {
                System.out.println("Escuchando mensaje del cliente...");
                byte[] clienteCommando = new byte[256];
                int resp = in.read(clienteCommando, 0, 256);
                if (resp != -1) {
                    String m = new String(clienteCommando, StandardCharsets.UTF_8).trim();
                    DespachadorEventoMensaje(new EventMensaje(this, m, this.datos));
                } else {
                    String id = this.datos.getIdCliente();
                    if (!clienteSocket.isConnected() || !clienteSocket.isClosed()) {
                        if (!clienteSocket.getInetAddress().isReachable(30000)) {
                            System.out.println("El cliente ( " + id + " ). no se volvio a conectar...");
                            System.out.println("cliente desconectado ( " + id + " ). Dejando de escuchar mensajes...");
                            EventConexion evtConexion = new EventConexion(this, new DataConexion(String.valueOf(clienteSocket.getPort()), String.valueOf(clienteSocket.getLocalAddress()), clienteSocket, String.valueOf(clienteSocket.hashCode()),this.datos.getMsg()));
                            CerrarEventoConexion(evtConexion);
                            in.close();
                            clienteSocket.close();
                            break;
                        } else {
                            if (clienteSocket.isBound()) {
                                System.out.println("cliente desconectado ( " + id + " ). Dejando de escuchar mensajes...");
                                EventConexion evtConexion = new EventConexion(this, new DataConexion(String.valueOf(clienteSocket.getPort()), String.valueOf(clienteSocket.getLocalAddress()), clienteSocket, String.valueOf(clienteSocket.hashCode()),this.datos.getMsg()));
                                CerrarEventoConexion(evtConexion);
                                in.close();
                                clienteSocket.close();
                                break;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            try {
                if (e.getMessage().equals("Connection reset")) {
                    System.out.println("cliente desconectado ( " + this.datos.getIdCliente() + " ). Dejando de escuchar mensajes...");
                    EventConexion evtConexion = new EventConexion(this, new DataConexion(String.valueOf(clienteSocket.getPort()), String.valueOf(clienteSocket.getLocalAddress()), clienteSocket, String.valueOf(clienteSocket.hashCode()),this.datos.getMsg()));
                    CerrarEventoConexion(evtConexion);
                    in.close();
                    clienteSocket.close();
                }
            } catch (IOException ex) {
                System.err.println("HILO_ESCUCHADOR: " + ex.getMessage());
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
