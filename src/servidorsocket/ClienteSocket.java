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
import java.text.DecimalFormat;
import java.util.Properties;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Henrry Roca Joffre
 */
public class ClienteSocket {

    private String Host;
    private int Puerto;
    private DataOutputStream out;
    private DataInputStream in;
    private final boolean Continua = true;
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
            String id = mensaje.substring(mensaje.indexOf(":") + 1, mensaje.length());
            System.out.println("Simulando Dispositivo...");
//            while (Continua) {
            Timer tiempo = new Timer();
            tiempo.schedule(enviarDatos(Integer.parseInt(id)), 0, 1000);
//                System.out.println("Escriba un mensaje");
//                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//                out = new DataOutputStream(socket.getOutputStream());
//                String msg = br.readLine();
//                System.out.println(msg);
//                if (msg.compareTo("salir") == 0) {
//                    out.close();
//                    socket.close();
//                    System.out.println("Desconectado");
//                    break;
//                }
//                //Envio un mensaje al cliente
//                out.writeUTF(msg);
//
//           }
        } catch (IOException ex) {
            Logger.getLogger(ClienteSocket.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public TimerTask enviarDatos(int id) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    out = new DataOutputStream(socket.getOutputStream());
                    String dato;
                    DecimalFormat formato1 = new DecimalFormat("#.00");
                    double tem = (double) (Math.random() * (50 -11) +11);
                    double hum = (double) (Math.random() * 80);
                    long tiempo = (long) (Math.random() * 1000000);
                    tem = Double.parseDouble(formato1.format(tem).replace(",", "."));
                    hum = Double.parseDouble(formato1.format(hum).replace(",", "."));
                    dato = "Id=" + id + "|Temp=" + tem + "|Hum="+hum+"|Tiempo="+tiempo;
                    System.out.println(dato);
                    out.writeUTF(dato);
                } catch (IOException ex) {
                    System.out.println("QUE ESTA PASANDO AQUI" + ex);
                }
            }
        };
    }

    public static void main(String[] args) throws Exception {
        Properties propiedades = new Properties();
        propiedades.load(new FileReader("datos.properties"));
        ClienteSocket cl = new ClienteSocket(propiedades.getProperty("Host"), Integer.parseInt(propiedades.getProperty("Puerto")));
        cl.connect();

    }

}
