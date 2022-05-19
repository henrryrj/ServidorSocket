package Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Erick Vidal
 */
public class Temperatura {

    int id;
    double temperatura;
    int idCliente;

    PreparedStatement ps;
    ResultSet rs;
    Singleton s;

    public Temperatura() {
        this.s = Singleton.getInstancia();
    }

    public Temperatura(int id, double temperatura, int idCliente) {
        this.id = id;
        this.temperatura = temperatura;
        this.idCliente = idCliente;
    }

    public int getId() {
        return id;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public void agregar(Temperatura temperatura) {
        String sql = "insert into temperatura(temperatura,idCliente) values(?,?)";
        try {
            ps = s.con.prepareStatement(sql);
            ps.setDouble(1, temperatura.getTemperatura());
            ps.setInt(2, temperatura.getIdCliente());
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    public void toSring(){
        System.out.println("Temperatura: {");
        System.out.println("id: " + this.id);    
        System.out.println("temperatura: " + this.temperatura);
        System.out.println("idCliente: " + this.idCliente);
        System.out.println("}");
        System.out.println("");

    }
}
