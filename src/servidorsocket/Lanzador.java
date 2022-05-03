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

    public static void main(String[] args) {
        MySocket socketcito = new MySocket(3000);
    }
}
