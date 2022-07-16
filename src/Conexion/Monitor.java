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
    String idDispositivo;
    double temp;
    double hum;
    String tiempo;
    String time;

    PreparedStatement ps;
    ResultSet rs;
    Singleton s;

    public Monitor() {
        this.s = Singleton.getInstancia();
    }

    public Monitor(int id, String idDispositivo, double temp, double hum, String Tiempo, PreparedStatement ps, ResultSet rs, Singleton s) {
        this.id = id;
        this.idDispositivo = idDispositivo;
        this.temp = temp;
        this.hum = hum;
        this.tiempo = Tiempo;
    }

    public int getId() {
        return id;
    }

    public String getIdDispositivo() {
        return this.idDispositivo;
    }

    public double getTemp() {
        return this.temp;
    }

    public double getHum() {
        return this.hum;
    }

    public String getTiempo() {
        return this.tiempo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdDispositivo(String idDisp) {
        this.idDispositivo = idDisp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public void setHum(double hum) {
        this.hum = hum;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public String agregar(Monitor sensor) {
        String sql = "insert into monitor(idDispositivo,temp,hum,tiempo,time) values(?,?,?,?,?)";
        try {
            ps = s.con.prepareStatement(sql);
            ps.setString(1, sensor.getIdDispositivo());
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
        System.out.println("idDispositivo: " + this.idDispositivo);
        System.out.println("Temp: " + this.temp);
        System.out.println("Hum: " + this.hum);
        System.out.println("Tiempo: " + this.tiempo);
        System.out.println("}");
    }

}
