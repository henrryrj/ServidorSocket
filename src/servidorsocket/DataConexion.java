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
    private String idCliente;
    private String msg;

    public DataConexion(String IP, String Host, Socket SocketCliente, String id, String msg) {
        this.IP = IP;
        this.Host = Host;
        this.SocketCliente = SocketCliente;
        this.idCliente = id;
        this.msg = msg;
    }

    public String getHost() {
        return Host;
    }

    public void setHost(String Host) {
        this.Host = Host;
    }

    public String getIdCliente() {
        return this.idCliente;
    }

    public void setIdCliente(String id) {
        this.idCliente = id;
    }

    public Socket getSocketCliente() {
        return SocketCliente;
    }

    public void setSocketCliente(Socket SocketCliente) {
        this.SocketCliente = SocketCliente;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
