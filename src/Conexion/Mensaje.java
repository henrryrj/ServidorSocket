/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Erick Vidal
 */
public class Mensaje {
    int id;
    String mensaje;
    int idClienteOrigen;
    int idClienteDestino;
    
    
    
    
    Conexion conectar = new Conexion();
    //Connection con;
    PreparedStatement ps;
    ResultSet rs;
    Singleton s;

    public Mensaje() {
    }

    public Mensaje(int id, String mensaje, int idClienteOrigen, int idClienteDestino) {
        this.id = id;
        this.mensaje = mensaje;
        this.idClienteOrigen = idClienteOrigen;
        this.idClienteDestino = idClienteDestino;
    }

    public int getId() {
        return id;
    }

    public int getIdClienteOrigen() {
        return idClienteOrigen;
    }

    public int getIdClienteDestino() {
        return idClienteDestino;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void setIdClienteOrigen(int idClienteOrigen) {
        this.idClienteOrigen = idClienteOrigen;
    }

    public void setIdClienteDestino(int idClienteDestino) {
        this.idClienteDestino = idClienteDestino;
    }

    public void agregar(Mensaje mensaje){
        String sql="insert into mensaje(mensaje,idClienteOrigen,idClienteDestino) values(?,?,?)";
        try{
            ps=s.con.prepareStatement(sql);
            ps.setString(1, mensaje.getMensaje());
            ps.setInt(2, mensaje.getIdClienteOrigen());
            ps.setInt(3, mensaje.getIdClienteDestino());
            ps.executeUpdate();
        }catch(Exception e){
            System.err.println(e);
        }
    }
    
    
    
    
    
    
}
