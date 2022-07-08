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
//            Timer tiempo = new Timer();
//            tiempo.schedule(hiloVerificador(tiempo), 0, 10000);
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
            HiloEscuchadorMensaje hem = new HiloEscuchadorMensaje(this, e.dato.getSocketCliente(), e.dato);
            monitor = new MonitorTemperatura();
            hem.addEscuchadorMensaje(this, monitor);
            DataCliente dc = new DataCliente(e.dato.getSocketCliente(), hem);
            this.clientesConectados.put(String.valueOf(e.dato.getSocketCliente().hashCode()), dc);
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

//    public TimerTask hiloVerificador(Timer t) {
//        return new TimerTask() {
//            @Override
//            public void run() {
//                try {
//                    if (clientesConectados.isEmpty()) {
//                        System.out.println("sin clientes!!!");
//                        return;
//                    }
//                    for (String i : clientesConectados.keySet()) {
//                        DataCliente clienteActual = clientesConectados.get(i);
//                        OutputStream out = clienteActual.getSocketClient().getOutputStream();
//                        out.write("isRecheable".getBytes());
//                        if(out == null){
//                            System.out.println("sera que entra");
//                        }
//                    }
//                } catch (IOException ex) {
//                    if(ex.getMessage().equals("Connection reset by peer: socket write error")){
//                        System.out.println("desconectamos a los locos XD");
//                       // EventConexion evtConexion = new EventConexion(this, new DataConexion(String.valueOf(.getPort()), String.valueOf(clienteSocket.getLocalAddress()), clienteSocket, String.valueOf(clienteSocket.hashCode()), m));
//                    }
//                    //t.cancel();
//                    System.err.println("ERROR HILO VERIFICADOR: " + ex.getMessage());
//                }
//            }
//        };
//    }
}
