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
public class Cliente {
    int id;
    
    Conexion conectar = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    Singleton s;

    public Cliente() {
        s=new Singleton();
    }

    public Cliente(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    //4
    public void agregar(Cliente cliente){
        String sql="insert into cliente(id) values(?)";
        try{
            ps=s.con.prepareStatement(sql);
            ps.setInt(1, cliente.getId());
            ps.executeUpdate();
        }catch(Exception e){
            System.err.println(e);
        }
    }
}
