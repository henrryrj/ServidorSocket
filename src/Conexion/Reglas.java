/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexion;
import Mail.Mail;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.mail.MessagingException;
import java.time.LocalDateTime;
/**
 *
 * @author Erick Vidal
 */
public class Reglas {
    int id;
    double temMax;
    double temMin;
    String mensaje;
    String correo;

    
    public String getMensaje() {
        return mensaje;
    }
    
    Mail m;
    PreparedStatement ps;
    ResultSet rs;
    Singleton s;

    public Reglas() throws IOException {
        this.s=Singleton.getInstancia();
        this.m=new Mail();
    }

    public Reglas(int id, double temMax, double temMin, String correo) {
        this.id = id;
        this.temMax = temMax;
        this.temMin = temMin;
        this.correo = correo;
    }

    public int getId() {
        return id;
    }

    public double getTemMax() {
        return temMax;
    }

    public double getTemMin() {
        return temMin;
    }

    public String getCorreo() {
        return correo;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTemMax(double temMax) {
        this.temMax = temMax;
    }

    public void setTemMin(double temMin) {
        this.temMin = temMin;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    public void agregar(Reglas regla){
        String sql="insert into reglas(temMin,temMax,mensaje,correo) values(?,?,?,?)";
        try{   
            ps=s.con.prepareStatement(sql);
            ps.setDouble(1, regla.getTemMin());
            ps.setDouble(2, regla.getTemMax());
            ps.setString(3, regla.getMensaje());
            ps.setString(4, regla.getCorreo());
            ps.executeUpdate();
        }catch(Exception e){
            System.err.println(e);
        }
    }
    public String getFecha() {
        String fecha = LocalDateTime.now().toString();
        return fecha.replace("T", " ");
    }
    public void verificarReglas(double tem) throws SQLException, MessagingException{
        String consulta="select temMin,temMax,mensaje,correo from reglas";
        ps=s.con.prepareStatement(consulta);
        rs=ps.executeQuery(consulta);
        while (rs.next()) { 
            double min=Double.valueOf(rs.getString("temMin"));
            double max=Double.valueOf(rs.getString("temMax"));
            String men=rs.getString("mensaje");
            men=men+" El dia y Hora: "+getFecha()+", con una temperatura de: "+tem+" °C";
            String corre=rs.getString("correo");
            if (tem>min && tem<max) {
                m.enviarEmail("Cuidado con su monito de Temperatura", men, corre);
                System.out.println("Correo enviado...");
            }
        }
    }
}