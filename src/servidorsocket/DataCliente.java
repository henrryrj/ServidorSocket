/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsocket;

import java.net.Socket;

/**
 *
 * @author stephani
 */
public class DataCliente {
    private Socket socketClient;
    private HiloEscuchadorMensaje hiloCliente;

    public DataCliente(Socket socketClient, HiloEscuchadorMensaje hiloCliente) {
        this.socketClient = socketClient;
        this.hiloCliente = hiloCliente;
    }
    
    public Socket getSocketClient() {
        return socketClient;
    }

    public void setSocketClient(Socket socketClient) {
        this.socketClient = socketClient;
    }

    public HiloEscuchadorMensaje getHiloCliente() {
        return hiloCliente;
    }

    public void setHiloCliente(HiloEscuchadorMensaje hiloCliente) {
        this.hiloCliente = hiloCliente;
    }

    
}
