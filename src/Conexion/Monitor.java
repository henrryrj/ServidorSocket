/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 *
 * @author henrr
 */
public class Monitor {

    int id;
    int idCliente;
    double Temp;
    double Hum;
    String Tiempo;
    String time;

    PreparedStatement ps;
    ResultSet rs;
    Singleton s;

    public Monitor() {
        this.s = Singleton.getInstancia();
    }

    public Monitor(int id, int idCliente, double Temp, double Hum, String Tiempo, PreparedStatement ps, ResultSet rs, Singleton s) {
        this.id = id;
        this.idCliente = idCliente;
        this.Temp = Temp;
        this.Hum = Hum;
        this.Tiempo = Tiempo;
    }

    public int getId() {
        return id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public double getTemp() {
        return Temp;
    }

    public double getHum() {
        return Hum;
    }

    public String getTiempo() {
        return Tiempo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public void setTemp(double Temp) {
        this.Temp = Temp;
    }

    public void setHum(double Hum) {
        this.Hum = Hum;
    }

    public void setTiempo(String Tiempo) {
        this.Tiempo = Tiempo;
    }

    public String agregar(Monitor sensor) {
        String sql = "insert into monitor(idCliente,Temp,Hum,Tiempo,time) values(?,?,?,?,?)";
        try {
            ps = s.con.prepareStatement(sql);
            ps.setInt(1, sensor.getIdCliente());
            ps.setDouble(2, sensor.getTemp());
            ps.setDouble(3, sensor.getHum());
            ps.setString(4, sensor.getTiempo());
            String fecha = getFecha();
            ps.setString(5, fecha);
            ps.executeUpdate();
            System.out.println("Temperatura guardada!");
            return fecha;
        } catch (SQLException e) {
            return "-1";
        }
    }

    public String getFecha() {
        String fecha = LocalDateTime.now().toString();
        return fecha.replace("T", " ");
    }

    public void toSring() {
        System.out.println("Monitor: {");
        //System.out.println("id: " + this.id);
        System.out.println("idCliente: " + this.idCliente);
        System.out.println("Temp: " + this.Temp);
        System.out.println("Hum: " + this.Hum);
        System.out.println("Tiempo: " + this.Tiempo);
        //System.out.println("time: " + this.time);
        System.out.println("}");
    }

}
