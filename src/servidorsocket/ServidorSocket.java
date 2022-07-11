/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsocket;

import Monitor.MonitorTemperatura;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stephani
 */
public final class ServidorSocket implements ISocketListener {

    private ServerSocket Servidor = null;
    private HashMap<String, DataCliente> clientesConectados;
    HiloConexionDatos conexionClientes = null;
    MonitorTemperatura monitor = null;

    public ServidorSocket(int Puerto) {
        try {
            Servidor = new ServerSocket(Puerto);
            clientesConectados = new HashMap<>();
            conexionClientes = new HiloConexionDatos(Servidor);
            monitor = new MonitorTemperatura();
            conexionClientes.addEscuchadorConexion(this, monitor);
        } catch (IOException e) {
            System.err.println("algo paso con el servidor: " + e.getMessage());
        }
    }

    public HiloConexionDatos getConexionCliente() {
        return this.conexionClientes;
    }

    @Override
    public void onClienteConectado(EventConexion e) {
        try {
            String id = getIdDeLaTrama(e.dato.getMsg());
            HiloEscuchadorMensaje hem = new HiloEscuchadorMensaje(this, e.dato.getSocketCliente(), e.dato);
            monitor = new MonitorTemperatura();
            hem.addEscuchadorMensaje(this, monitor);
            DataCliente dc = new DataCliente(e.dato.getSocketCliente(), hem);
            this.clientesConectados.put(String.valueOf(e.dato.getSocketCliente().hashCode()), dc);
            if (!id.equals("-1")) {
                if (!id.equals(String.valueOf(e.dato.getSocketCliente().hashCode()))) {
                    e.dato.setIdCliente(id);
                    System.out.println("Nuevo Cliente Conectado: " + e.dato.getIdCliente());
                    System.out.println("Clientes conectados: " + this.clientesConectados.size() + "\n");
                } else {
                    e.dato.setIdCliente(String.valueOf(e.dato.getSocketCliente().hashCode()));
                    System.out.println("Nuevo Cliente Conectado: " + e.dato.getIdCliente());
                    System.out.println("Clientes conectados: " + this.clientesConectados.size() + "\n");
                }
            }
            hem.start();
        } catch (Exception ex) {
            System.err.println("ERROR EN EL EVENTO onClienteConectado DEL SERVER_SOCKET: " + ex.getMessage());
        }

    }

    @Override
    public void onClienteDesconectado(EventConexion e) {
        String key = String.valueOf(e.dato.getSocketCliente().hashCode());
        this.clientesConectados.remove(key);
        String id = getIdDeLaTrama(e.dato.getMsg());
        if (!id.equals("-1")) {
            if (!id.equals(key)) {
                System.out.println("Cliente Desconectado: " + id);
                System.out.println("Clientes Conectados : " + this.clientesConectados.size() + "\n");
            } else {
                System.out.println("Cliente Desconectado: " + key);
                System.out.println("Clientes Conectados : " + this.clientesConectados.size() + "\n");
            }
        }
    }

    @Override
    public void onMensajeCliente(EventMensaje e) {
        System.out.println(e.mensaje + "\n");
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

    public String getIdDeLaTrama(String trama) {
        int posIgual = trama.indexOf("=");
        int posBarra = trama.indexOf("|");
        if (posIgual != -1 && posBarra != -1) {
            return trama.substring(posIgual + 1, posBarra);
        } else {
            return "-1";
        }
    }
}
