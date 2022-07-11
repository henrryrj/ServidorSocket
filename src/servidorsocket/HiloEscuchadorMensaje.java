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
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    OutputStream out;
    private DataConexion datos;

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
            Timer tiempo = new Timer();
            tiempo.schedule(hiloVerificador(tiempo), 0, 1000);
            while (bandera) {
                System.out.println("Escuchando mensaje del cliente...");
                byte[] clienteCommando = new byte[256];
                in.read(clienteCommando);
                String m = new String(clienteCommando, StandardCharsets.UTF_8).trim();
                DespachadorEventoMensaje(new EventMensaje(this, m, this.datos));
            }
        } catch (IOException e) {
            System.err.println("HILO_ESCUCHADOR: " + e.getMessage());
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

    public TimerTask hiloVerificador(Timer t) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    out = clienteSocket.getOutputStream();
                    out.write("isRechable".getBytes());
                    out.flush();
                } catch (IOException ex) {
                    try {
                        if (ex.getMessage().equals("Connection reset by peer: socket write error")
                                || ex.getMessage().equals("Connection reset")) {
                            System.out.println("cliente desconectado ( " + datos.getIdCliente() + " ). Dejando de escuchar mensajes...");
                            EventConexion evtConexion = new EventConexion(this, new DataConexion(String.valueOf(clienteSocket.getPort()), String.valueOf(clienteSocket.getLocalAddress()), clienteSocket, String.valueOf(clienteSocket.hashCode()), datos.getMsg()));
                            CerrarEventoConexion(evtConexion);
                            t.cancel();
                            bandera = !bandera;
                            in.close();
                            clienteSocket.close();
                        }
                    } catch (IOException ex1) {
                        Logger.getLogger(HiloEscuchadorMensaje.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
            }
        };
    }
}
