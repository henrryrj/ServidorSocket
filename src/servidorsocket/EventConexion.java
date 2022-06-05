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
public class EventConexion extends EventObject {

    DataConexion dato;

    public EventConexion(Object source, DataConexion dato) {
        super(source);
        this.dato = dato;
    }

    public DataConexion getDato() {
        return this.dato;
    }

}
