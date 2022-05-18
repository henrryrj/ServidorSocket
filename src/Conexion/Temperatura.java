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
    float temperatura;
    int idCliente;

    PreparedStatement ps;
    ResultSet rs;
    Singleton s;

    public Temperatura() {
        this.s = Singleton.getInstancia();
    }

    public Temperatura(int id, float temperatura, int idCliente) {
        this.id = id;
        this.temperatura = temperatura;
        this.idCliente = idCliente;
    }

    public int getId() {
        return id;
    }

    public float getTemperatura() {
        return temperatura;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTemperatura(float temperatura) {
        this.temperatura = temperatura;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public void agregar(Temperatura temperatura) {
        String sql = "insert into temperatura(temperatura,idCliente) values(?,?)";
        try {
            ps = s.con.prepareStatement(sql);
            ps.setFloat(1, temperatura.getTemperatura());
            ps.setInt(2, temperatura.getIdCliente());
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
