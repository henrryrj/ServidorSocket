/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidorsocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;

/**
 *
 * @author Henrry Roca Joffre
 */
public class HiloDeDatos extends Thread {

    private DataInputStream in;
    private DataOutputStream out;

    public HiloDeDatos(DataInputStream in, DataOutputStream out) {
        this.in = in;
        this.out = out;

    }
    @Override
    public void run() {

    }
}
