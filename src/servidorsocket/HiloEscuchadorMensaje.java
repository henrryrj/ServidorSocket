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
public class HiloEscuchadorMensaje extends Thread{
    private Socket clienteSocket; 
    private boolean Contiene = true;
    private EventListenerList listSocketListener = null;
    private DataInputStream in = null;

    HiloEscuchadorMensaje( ServidorSocket owner, Socket s) {
        clienteSocket = s;
        listSocketListener = new EventListenerList();
    }
    
    void addEscuchadorMensaje(ISocketListener l){
        listSocketListener.add(ISocketListener.class, l);
    }
    
    void  removeEscuchadorMensaje(ISocketListener l){
        listSocketListener.remove(ISocketListener.class, l);
    }

    @Override
    public void run() {
        try {
            in = new DataInputStream(clienteSocket.getInputStream());
            
            while (Contiene) {       
                System.out.println("Escuchando mensaje del cliente...");
                String clienteCommando = in.readUTF();
                DespachadorEventoMensaje(new EventMensaje(this,clienteCommando));
            }
        } catch (IOException e) {
            
        }
        finally{
            try {
                EventConexion evtConexion = new  EventConexion(this, new DataConexion(clienteSocket.getPort()+"",clienteSocket.getLocalAddress()+"",clienteSocket,""));
                CerrarEventoConexion(evtConexion);
                in.close();
                clienteSocket.close();
            } catch (IOException e) { 
            }
        }
    
    }
    
    protected void DespachadorEventoMensaje(EventMensaje e){
        ISocketListener[] ls = listSocketListener.getListeners(ISocketListener.class);
        for (ISocketListener l : ls){
            l.onMensajeCliente(e);
        }
    }
    protected void CerrarEventoConexion(EventConexion e){
        ISocketListener[] ls = listSocketListener.getListeners(ISocketListener.class);
        for (ISocketListener l : ls){
            l.onClienteDesconectado(e);
        }
    }
}
