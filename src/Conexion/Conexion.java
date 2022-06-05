/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Erick Vidal
 */
public class Conexion {
    Connection con;
    public Connection getConnection(){
        
        String url="jdbc:mysql://lyn7gfxo996yjjco.cbetxkdyhwsb.us-east-1.rds.amazonaws.com:3306/ta8n3f167849xcmb";
        String user="ezavw7z2w51th1gn";
        String pass="oyxj6mhjwrl1qrp1";
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con=DriverManager.getConnection(url,user,pass);
        }catch(ClassNotFoundException | SQLException e){
            System.err.println(e);
        }
        return con;
    }
    //guardar un registro
}