/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsocket;

import java.util.EventListener;

/**
 *
 * @author stephani
 */
public interface ISocketListener extends EventListener {
    public void onClienteConectado(EventConexion e);
    public void onClienteDesconectado(EventConexion e);
    public void onMensajeCliente(EventMensaje e);
    
}
