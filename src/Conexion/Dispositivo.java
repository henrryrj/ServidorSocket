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
    double Tem;
    double Hum;
    boolean estado;
    String ultimoRegistro;

    PreparedStatement ps;
    ResultSet rs;
    Singleton s;

    public Dispositivo() {
        this.s = Singleton.getInstancia();
    }

    public Dispositivo(int id, double tem, double hum, String fechaReg, boolean estado) {
        this.id = id;
        this.Tem = tem;
        this.Hum = hum;
        this.estado = estado;
        this.ultimoRegistro = fechaReg;
    }

    public int getId() {
        return id;
    }

    public double getTem() {
        return Tem;
    }

    public double getHum() {
        return Hum;
    }

    public String getUltimoRegistro() {
        return ultimoRegistro;
    }

    public boolean getEstado() {
        return this.estado;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTem(double Tem) {
        this.Tem = Tem;
    }

    public void setHum(double Hum) {
        this.Hum = Hum;
    }

    public void setUltimoRegistro(String ultimoRegistro) {
        this.ultimoRegistro = ultimoRegistro;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    //4
    public void agregar(Dispositivo cliente) {
        String sql = "insert into dispositivo(id,Tem,Hum,estado,ultimoRegistro) values(?,0.0,0.0,?,-1)";
        try {
            ps = s.con.prepareStatement(sql);
            ps.setInt(1, cliente.getId());
            ps.setInt(2, parseBool(cliente.getEstado()));
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e);
        }
    }
    
    public int parseBool(boolean b){
        if(b){
            return 0;
        }else{
            return 1;
        }
    }

    public void updateRegistro(Dispositivo disp) {
        //String sql = "insert into dispositivo(id) values(?)";
        String sql = "UPDATE dispositivo SET Tem ='" + disp.getTem() + "', Hum ='" + disp.getHum() + "', ultimoRegistro ='" + disp.getUltimoRegistro() + "' WHERE id =" + disp.getId();
        try {
            ps = s.con.prepareStatement(sql);
            ps.executeUpdate(sql);
            System.out.println("dispositivo Actualizado!");
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public void setEstadoDisp(Dispositivo cliente) {
        String sql = "UPDATE dispositivo SET estado='" + parseBool(cliente.estado) + "' WHERE id=" + cliente.getId();
        try {
            ps = s.con.prepareStatement(sql);
            ps.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public boolean existe(int id) {
        String sql = "select id from dispositivo where id=" + id;
        try {
            ps = s.con.prepareStatement(sql);
            rs = ps.executeQuery(sql);
            return rs.next();
        } catch (SQLException | NumberFormatException e) {
            System.err.println(e);
        }
        return false;
    }

}
