/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexion;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Erick Vidal
 */
public class Dispositivo {

    String id;
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

    public Dispositivo(String id, double tem, double hum, String fechaReg, boolean estado) {
        this.id = id;
        this.Tem = tem;
        this.Hum = hum;
        this.estado = estado;
        this.ultimoRegistro = fechaReg;
    }

    public String getId() {
        return this.id;
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

    public void setId(String id) {
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
        String sql = "INSERT INTO dispositivo(id,temp,hum,estado,ultimoRegistro) VALUES(?,0.0,0.0,?,-1)";
        try {
            ps = s.con.prepareStatement(sql);
            ps.setString(1, cliente.getId());
            ps.setBoolean(2, cliente.getEstado());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void updateRegistro(Dispositivo disp) {
        //String sql = "insert into dispositivo(id) values(?)";
        String sql = "UPDATE dispositivo SET temp ='" + disp.getTem() + "', hum ='" + disp.getHum() + "', ultimoRegistro ='" + disp.getUltimoRegistro() + "' WHERE id ='" + disp.id + "'" ;
        try {
            ps = s.con.prepareStatement(sql);
            ps.executeUpdate(sql);
            System.out.println("dispositivo Actualizado!");
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public void setEstadoDisp(Dispositivo cliente) {
        String sql = "UPDATE dispositivo SET estado=" +cliente.estado + " WHERE id='" + cliente.id + "'" ;
        try {
            ps = s.con.prepareStatement(sql);
            ps.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println(e);
        }
    }

    public boolean existe(String id) {
        String sql = "select id from dispositivo where id='" + id + "'" ;
        try {
            ps = s.con.prepareStatement(sql);
            rs = ps.executeQuery(sql);
            return rs.next();
        } catch (SQLException | NumberFormatException e) {
            System.err.println(e);
        }
        return false;
    }

    public void pushNotificacionOnClientConnected(String id) {
        try {
            URL urlConectado;
            urlConectado = new URL("https://api-node-mysql-sd.herokuapp.com/dispConectado/" + id);
            HttpURLConnection connect = (HttpURLConnection) urlConectado.openConnection();
            connect.setRequestMethod("GET");
            connect.connect();
            int resStatus = connect.getResponseCode();
            System.out.println(resStatus);
            if (resStatus == 200) {
                System.out.println("NotificacionEnviada!: " + connect.getResponseMessage());
            }
        } catch (IOException ex) {
            Logger.getLogger(Dispositivo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void pushNotificationOnClienteDesconnected(String id) {
        try {
            URL urlConectado;
            urlConectado = new URL("https://api-node-mysql-sd.herokuapp.com/dispDesconectado/" + id);
            HttpURLConnection connect = (HttpURLConnection) urlConectado.openConnection();
            connect.setRequestMethod("GET");
            connect.connect();
            int resStatus = connect.getResponseCode();
            if (resStatus == 200) {
                System.out.println("NotificacionEnviada!: " + connect.getResponseMessage() + "\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(Dispositivo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
