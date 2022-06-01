/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Erick Vidal
 */
public class Dispositivo {
    int id;
    
    PreparedStatement ps;
    ResultSet rs;
    Singleton s;

    public Dispositivo() {
        this.s= Singleton.getInstancia();
    }

    public Dispositivo(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    //4
    public void agregar(Dispositivo cliente){
        String sql="insert into dispositivo(id) values(?)";
        try{
            ps=s.con.prepareStatement(sql);
            ps.setInt(1, cliente.getId());
            ps.executeUpdate();
        }catch(Exception e){
            System.err.println(e);
        }
    }
    public boolean existe(int id){
        String sql="select id from dispositivo where id="+id;
        try {
            ps=s.con.prepareStatement(sql);
            rs=ps.executeQuery(sql);
            return rs.first();
        } catch (SQLException | NumberFormatException e) {
            System.err.println(e);
        }
        return false;
    }

}
