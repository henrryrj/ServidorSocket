/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsocket;

import Monitor.MonitorTemperatura;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stephani
 */
public class ServidorSocket implements ISocketListener {

    private ServerSocket Servidor = null;
    private HashMap<String, DataCliente> clientesConectados;
    HiloConexionDatos conexionClientes = null;

    public ServidorSocket(int Puerto) {
        try {
            Servidor = new ServerSocket(Puerto);
            clientesConectados = new HashMap<>();
            conexionClientes = new HiloConexionDatos(Servidor);
            conexionClientes.addEscuchadorConexion(this);
        } catch (IOException e) {
            System.out.println("algo paso con el servidor: " + e);
        }
    }

    public HiloConexionDatos getConexionCliente() {
        return this.conexionClientes;
    }

    @Override
    public void onClienteConectado(EventConexion e) {
        try {
            HiloEscuchadorMensaje hem = new HiloEscuchadorMensaje(this, e.dato.getSocketCliente(), e.dato);
            MonitorTemperatura monitor = new MonitorTemperatura();
            hem.addEscuchadorMensaje(this, monitor);
            hem.start();
            DataCliente dc = new DataCliente(e.dato.getSocketCliente(), hem);
            this.clientesConectados.put(e.dato.getSocketCliente().hashCode() + "", dc);
            System.out.println("Nuevo Cliente Conectado: " + e.dato.getSocketCliente().hashCode() + "");
            e.dato.setIdCliente(String.valueOf(e.dato.getSocketCliente().hashCode()));
            System.out.println("Clientes conectados: " + this.clientesConectados.size());
        } catch (Exception ex) {
            System.out.println(ex);
        }

    }

    @Override
    public void onClienteDesconectado(EventConexion e) {
        String key = e.dato.getSocketCliente().hashCode() + "";
        this.clientesConectados.remove(key);
        System.out.println("Cliente Desconectado: " + key);
        System.out.println("Clientes Conectados : " + this.clientesConectados.size());
    }

    @Override
    public void onMensajeCliente(EventMensaje e) {
        System.out.println(e.mensaje.substring(2, e.mensaje.length()));
    }

    public void enviarMensaje(String id, String mensaje) {
        
        ExecutorService executor = Executors.newCachedThreadPool();
        executor.submit(() -> {
            try {
                DataCliente cliente = clientesConectados.get(id);
                if (cliente != null) {
                    BufferedOutputStream out = new BufferedOutputStream(cliente.getSocketClient().getOutputStream());
                    out.write(mensaje.getBytes());
                }
            } catch (IOException ex) {
                Logger.getLogger(ServidorSocket.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        executor.shutdown();
    }
}
