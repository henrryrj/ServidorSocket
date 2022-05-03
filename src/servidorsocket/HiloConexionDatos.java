/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsocket;

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
    private EventListenerList ListaEscuchadores = null;

    public HiloConexionDatos(ServerSocket Servidor) {
        this.Servidor = Servidor;
        ListaEscuchadores = new EventListenerList();
    }
    
    void addEscuchadorConexion(ISocketListener l){
        ListaEscuchadores.add(ISocketListener.class, l);   
    }
    
    void removeEscuchadorConexion(ISocketListener l){
        ListaEscuchadores.remove(ISocketListener.class, l);  
    }
    
    public void shutdown(){}
        
    @Override
    public void run() {
            System.out.println("Server Conectado: ");
            while (Continue) {      
                try {
                    Socket clienteSocket = Servidor.accept();
                    EventConexion evtConexion = new  EventConexion(this, new DataConexion(clienteSocket.getPort()+"",clienteSocket.getLocalAddress()+"",clienteSocket));
                    DespachadorEventoConexion(evtConexion);
                }                                               
                catch (IOException e) {
               } finally
               {   
                   try {
                       System.out.println("");                
                   } catch (Exception e) {
                   }
               }
            }
    }
    
    @Override
    public void interrupt(){
        this.Continue = false;
    }
    
    
    
    protected void DespachadorEventoConexion(EventConexion e){
        ISocketListener[] ls = ListaEscuchadores.getListeners(ISocketListener.class);
        for(ISocketListener l : ls){
            l.onClienteConectado(e);
        }
    }
    protected void CerrarEventoConexion(EventConexion e){
        ISocketListener[] ls = ListaEscuchadores.getListeners(ISocketListener.class);
        for(ISocketListener l : ls){
            l.onClienteDesconectado(e);
        }
    }
}
