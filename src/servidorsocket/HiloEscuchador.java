/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.List;

/**
 *
 * @author Henrry Roca Joffre
 */
public class HiloEscuchador extends Thread implements SocketListener {
   // private final List<SocketListener> listener;
    private DataInputStream in;   
    private DataOutputStream out;
    private String nombreCliente;

    public HiloEscuchador(DataInputStream in, DataOutputStream out, String nombreCliente) {
        this.in = in;
        this.out = out;
        this.nombreCliente = nombreCliente;
    }
    @Override
    public void run(){
        
    }
    
    @Override
    public void clienteOn() {
       
    }
    

    
}
