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

/**
 *
 * @author Henrry Roca Joffre
 */
public class ClienteSocket {

    public static void main(String[] args) {
        final String HOST = "localhost";
        final int PUERTO = 3000;
        DataInputStream in;
        DataOutputStream out;

        try {
            Socket sc = new Socket(HOST, PUERTO); 
            in = new DataInputStream(sc.getInputStream());
            out = new DataOutputStream(sc.getOutputStream());

            out.writeUTF("hola soy un cliente :D ");
            String msgCliente = in.readUTF();
            System.out.println(msgCliente);
        } catch (IOException ex) {
            System.err.println("Error de parte del cliente" + ex);
        }
    }

}
