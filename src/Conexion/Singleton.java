/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Conexion;

import java.sql.Connection;

/**
 *
 * @author Erick Vidal
 */
public class Singleton {
    private static Singleton singletonConexion;
    private Conexion conexion;
    Connection con;
    
    
    public Singleton(){
        this.conexion=new Conexion();
        this.con=conexion.getConnection();
    }
    public static Singleton getInstancia(){
        if (singletonConexion==null) {
            singletonConexion=new Singleton();
        }
        return singletonConexion;
    }
}
