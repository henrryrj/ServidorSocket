/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;

/**
 *
 * @author stephani
 */
public class ServidorSocket implements ISocketListener {
    private ServerSocket Servidor = null;
    private HashMap<String, DataCliente> clientesConectados;
    HiloConexionDatos conexionClientes = null;
    
    public ServidorSocket(int Puerto){
        try 
        {
            Servidor = new ServerSocket(Puerto);
            clientesConectados = new HashMap<String, DataCliente>();
            conexionClientes = new HiloConexionDatos(Servidor);
            conexionClientes.addEscuchadorConexion(this);
        } 
        catch (IOException e) 
        {
            
        }
    }
    
    @Override
    public void onClienteConectado(EventConexion e) {
        HiloEscuchadorMensaje hem = new HiloEscuchadorMensaje(this, e.dato.getSocketCliente());
        hem.addEscuchadorMensaje(this);
        hem.start();
        DataCliente dc = new DataCliente(e.dato.getSocketCliente(), hem);
        this.clientesConectados.put(e.dato.getSocketCliente().hashCode()+"",dc);
        System.out.println("Nuevo Cliente Conectado: "+e.dato.getSocketCliente().hashCode()+"");
        System.out.println("Clientes conectados: "+this.clientesConectados.size());
    }

    @Override
    public void onClienteDesconectado(EventConexion e) {
        String key = e.dato.getSocketCliente().hashCode()+"";
        this.clientesConectados.remove(key);
        
        System.out.println("Cliente Desconectado: "+key);
        System.out.println("Clientes Conectados : "+this.clientesConectados.size());
    }

    @Override
    public void onMensajeCliente(EventMensaje e) {
        System.out.println("Nuevo Mensaje: "+e.mensaje);
    }

 public static void main(String[] argv) throws Exception {    
        int Puerto = 6000;
        ServidorSocket socket = new ServidorSocket(Puerto);
        socket.conexionClientes.start();
        
    }
}
