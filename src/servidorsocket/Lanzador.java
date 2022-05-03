/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsocket;

/**
 *
 * @author Henrry Roca Joffre
 */
public class Lanzador {

    public static void main(String[] argv) throws Exception {
        int Puerto = 6000;
        ServidorSocket socket = new ServidorSocket(Puerto);
        socket.conexionClientes.start();

    }

}
