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
public class DataConexion {
    private String IP;
    private String Host;
    private Socket SocketCliente;
    private String nombreCliente;

    public DataConexion(String IP, String Host, Socket SocketCliente, String nombre) {
        this.IP = IP;
        this.Host = Host;
        this.SocketCliente = SocketCliente;
        this.nombreCliente = nombre;
    }

    public String getHost() {
        return Host;
    }
    public String getNombreCliente(){
        return this.nombreCliente;
    }
    public void setNombe(String nombre){
        this.nombreCliente = nombre;
    }

    public void setHost(String Host) {
        this.Host = Host;
    }
    
    public Socket getSocketCliente(){
        return SocketCliente;
    }
    
    public void setSocketCliente(Socket SocketCliente){
        this.SocketCliente = SocketCliente;
    }
    
    public String getIP(){
        return IP;
    }
    
    public void setIP(String IP){
        this.IP = IP;
    }
    
}
