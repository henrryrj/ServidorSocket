/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 *
 * @author Henrry Roca Joffre
 */
public class ClienteSocket {

    private String Host;
    private int Puerto;
    private DataOutputStream out;
    private DataInputStream in;
    private boolean Continua = true;
    private Socket socket = null;

    public ClienteSocket(String Host, int Puerto) {
        this.Host = Host;
        this.Puerto = Puerto;
    }

    public String getHost() {
        return Host;
    }

    public void setHost(String Host) {
        this.Host = Host;
    }

    public int getPuerto() {
        return Puerto;
    }

    public void setPuerto(int Puerto) {
        this.Puerto = Puerto;
    }

    public void escuchandoMensaje() {
        try {
            this.in = new DataInputStream(this.socket.getInputStream());
            String mensaje = this.in.readUTF();
            System.out.println(mensaje);
        } catch (IOException ex) {
            Logger.getLogger(ClienteSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void connect() {
        try {
            //Se crea el socket para la conexion de clientes
            this.socket = new Socket(Host, Puerto);
            this.in = new DataInputStream(this.socket.getInputStream());
            String mensaje = this.in.readUTF();
            System.out.println(mensaje);
            while (Continua) {
                System.out.println("Escriba un mensaje");
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                out = new DataOutputStream(socket.getOutputStream());
                String msg = br.readLine();
                if (msg.compareTo("salir") == 0) {
                    out.close();
                    socket.close();
                    System.out.println("Desconectado");
                    break;
                }
                //Envio un mensaje al cliente
                out.writeUTF(msg);

            }
        } catch (IOException ex) {
            Logger.getLogger(ClienteSocket.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws Exception {
        Properties propiedades = new Properties();
        propiedades.load(new FileReader("datos.properties"));
        ClienteSocket cl = new ClienteSocket(propiedades.getProperty("Host"), Integer.parseInt(propiedades.getProperty("Puerto")));
        cl.connect();

    }

}
