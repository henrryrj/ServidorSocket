/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsocket;

import java.util.EventObject;

/**
 *
 * @author stephani
 */
public class EventMensaje extends EventObject{
    String mensaje;
    DataConexion dato;
    public EventMensaje(Object source, String mensaje, DataConexion d) {
        super(source);
        this.mensaje = mensaje; 
        this.dato = d;
    }
    public String getMensage(){return this.mensaje;}  
    public DataConexion getDato(){return this.dato;}

}
