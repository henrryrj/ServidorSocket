/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Henrry Roca Joffre
 */
public class MySocket implements SocketListener{
    private ServerSocket servidor;

    public MySocket(int puerto) {
        try {
            this.servidor = new ServerSocket(puerto);
            Socket sc;
            System.out.println("Servidor iniciado");
            
            while (true) { 
                sc = servidor.accept();
                DataInputStream in = new DataInputStream(sc.getInputStream());
                DataOutputStream out = new DataOutputStream(sc.getOutputStream());
                out.writeUTF("¿Cual es tu nombre?: ");
                String nombreCliente = in.readUTF();                
                HiloEscuchador escuchador = new HiloEscuchador(in,out, nombreCliente);
                escuchador.start();   
                System.out.println("Creada la conexion con: " + nombreCliente);
            }
            
            
        } catch (IOException ex) {
            System.err.println("Algo paso con mySocket" + ex);
        }
    }


    @Override
    public void clienteOn() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


   
}
