package servidorsocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Henrry Roca Joffre
 */
public class Lanzador {
    /*
    todo lo que esta aqui debera separarse en una clase aparte especialmente llamada
    ServidorSocket
    */
    public static void main(String[] args) {
        // servidor sockete (librerias importadas)
        ServerSocket servidor = null;
        Socket sc = null;
        DataInputStream in; // recibe informacion
        DataOutputStream out; // envia informacion
        final int PUERTO = 3000;
        try {

            servidor = new ServerSocket(PUERTO);
            System.out.println("Servidor iniciado");
            
            //escuchando a los clientes
            while(true){
                sc = servidor.accept(); // sockete esperando
                System.out.println("Cliente On!");
                in = new DataInputStream(sc.getInputStream());
                out = new DataOutputStream(sc.getOutputStream());
                String msg = in.readUTF(); // se queda a la espera del cliente :D
                System.out.println(msg);
                out.writeUTF("hola soy el ServerSocket!");
                sc.close(); // cerrando el cliente
                System.out.println("Cliente Off!");
            }
        } catch (IOException ex) {
            System.err.println("Error encontrado: " + ex);
        }
    }

}
