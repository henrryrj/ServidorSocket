/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Monitor;

import Conexion.Temperatura;
import Mail.Mail;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import servidorsocket.*;
import servidorsocket.EventMensaje;

/**
 *
 * @author Henrry Roca Joffre
 */
public class MonitorTemperatura implements ISocketListener{
    
    private Temperatura tem;
    
    public MonitorTemperatura(Temperatura tem){
        this.tem = tem;
    }
    
    public void onClienteConectado(EventConexion e) {
        // que se va implementar???
    }

    @Override
    public void onClienteDesconectado(EventConexion e) {
        // Aqui ya no hay drama XD
    }

    @Override
    public void onMensajeCliente(EventMensaje e) {
        String mensaje = e.getMensage();
        tem.setTemperatura(Float.parseFloat(mensaje));
        tem.setIdCliente(Integer.parseInt(e.getDato().getIdCliente()));
        //tem = new Temperatura(0,Float.parseFloat(mensaje), Integer.parseInt(e.getDato().getIdCliente()));
        tem.agregar(tem);
        try {
            Mail m=new Mail("config/configuracion.prop");
            m.enviarEmail("Alerta este hdp se exedio"+e.getDato().getIdCliente(), "Termperatura: "+mensaje, "vaquillorj@gmail.com");
            
        } catch (IOException | MessagingException ex) {
            Logger.getLogger(MonitorTemperatura.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
