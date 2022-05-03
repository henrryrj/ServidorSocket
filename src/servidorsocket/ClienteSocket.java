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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.InputStreamReader;
/**
 *
 * @author Henrry Roca Joffre
 */
public class ClienteSocket {
    private String Host;
    private int Puerto;
    private DataOutputStream out;
    private boolean Continua = true;
    private Socket socket = null;
    
    public ClienteSocket(String Host, int Puerto) {
        this.Host= Host;
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
    
    public void connect(){
        try {
            //Se crea el socket para la conexion de clientes
          this.socket = new Socket(Host, Puerto);
          while(Continua){
            System.out.println("Escribe un mensaje o escribe 'salir' para cerrar la conexion: ");
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
            Logger.getLogger(ClienteSocket.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    public static void main(String[] args) throws Exception {
        String  Host    =   "127.0.0.1";
        int     Puerto  =   6000;
                
        ClienteSocket cl = new ClienteSocket(Host, Puerto);
        cl.connect();
        /*try {
            Scanner sn = new Scanner(System.in);
            sn.useDelimiter("\n");
            Socket sc = new Socket("localhost", 3000);

            DataInputStream in = new DataInputStream(sc.getInputStream());
            DataOutputStream out = new DataOutputStream(sc.getOutputStream());
            
            String mensaje = in.readUTF();
            System.out.println(mensaje);
            
            String nombreCliente = sn.next();
            out.writeUTF(nombreCliente);
            HiloDeDatos datosCliente = new HiloDeDatos(in,out);
            datosCliente.start();
            datosCliente.join();
            
        } catch (IOException ex) {
            Logger.getLogger(ClienteSocket.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ClienteSocket.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }

}
