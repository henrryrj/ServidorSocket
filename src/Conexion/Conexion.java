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
        
        String url="jdbc:mysql://lcpbq9az4jklobvq.cbetxkdyhwsb.us-east-1.rds.amazonaws.com:3306/n9n7mcbwue2jqi15";
        String user="qjo6u6wws7494j3b";
        String pass="ayp4dq7o4ie548uf";
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