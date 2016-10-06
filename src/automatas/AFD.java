/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package automatas;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 *
 * @author usuario
 */
public class AFD {

    ArrayList<Estado> automata = new ArrayList<Estado>();
    String[][] matriz;

    public boolean buscar(String a, String[] v) {
        boolean encontrado = false;
        for (int i = 0; i < v.length; i++) {
            if (a.equalsIgnoreCase(v[i])) {
                encontrado = true;
                return encontrado;
            }
        }
        return encontrado;
    }

    public String mostrar(ArrayList<Estado> arreglo, String datos) {
        for (int i = 0; i < arreglo.size(); i++) {
            datos = datos + "Estado: --------------- " + arreglo.get(i).getNombre();
            datos = datos + "\nSiguiente con 0: --- " + arreglo.get(i).getSiguiente0();
            datos = datos + "\nSiguiente con 1: --- " + arreglo.get(i).getSiguiente1();
            datos = datos + "\nValida: ---------------- " + arreglo.get(i).getValida() + "\n\n";
        }
        return datos;
    }

    public boolean revisaValida(int[] vector) {
        boolean acepta = true;
        for (int i = 0; i < vector.length; i++) {
            if ((vector[i] != 0) && (vector[i] != 1)) {
                acepta = false;
                JOptionPane.showMessageDialog(null, "Debe ingresar 0 o 1 en la colúmna de validación."
                        + "\nError con: " + vector[i] + " en fila " + i + " columna 3", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
        return acepta;
    }

    public boolean revisaTrancisiones0(String[] vector1, String[] vector2) {
        boolean acepta = false;
        for (int j = 0; j < vector2.length; j++) {
            if (buscar(vector2[j], vector1) == true) {
                acepta = true;
                return acepta;
            } else {
                JOptionPane.showMessageDialog(null, "La transición debe hacerse hacia uno de los estados."
                        + "\nError con: " + vector2[j] + " en fila " + j + " columna 1", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
        return acepta;
    }

    public boolean revisaTrancisiones1(String[] vector1, String[] vector2) {
        boolean acepta = false;
        for (int j = 0; j < vector2.length; j++) {
            if (buscar(vector2[j], vector1) == true) {
                acepta = true;
                return acepta;
            } else {
                JOptionPane.showMessageDialog(null, "La transición debe hacerse hacia uno de los estados."
                        + "\nError con: " + vector2[j] + " en fila " + j + " columna 1", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
        return acepta;
    }

    public Estado avanceEstado(ArrayList<Estado> arreglo, String estado) {
        Estado siguiente;
        int i = 0;
        while (!(arreglo.get(i).getNombre().equalsIgnoreCase(estado))) {
            i++;
        }
        siguiente = arreglo.get(i);
        return siguiente;
    }

    public Object[] validarCadena(ArrayList<Estado> arreglo, String[] vector) {
        Object[] retorno = new Object[2];
        String recorrido = "";
        int valida = 0;
        String siguienteEstado = "";
        Estado estadoActual = arreglo.get(0);
        recorrido = "Estado inicial ==> " + estadoActual.getNombre();
        for (int i = 0; i < vector.length; i++) {
            if (vector[i].equals("0")) {
                siguienteEstado = estadoActual.getSiguiente0();
                recorrido = recorrido + " ----- Entra un 0 ";
            } else {
                siguienteEstado = estadoActual.getSiguiente1();
                recorrido = recorrido + " ----- Entra un 1 ";
            }
            estadoActual = avanceEstado(arreglo, siguienteEstado);
            recorrido = recorrido + "\nCambia al estado " + estadoActual.getNombre();
        }
        int i = estadoActual.getValida();
        if (i == 1) {
            valida = 1;   
             recorrido = recorrido + " ----- Cadena aceptada";
        }else{
            valida = 0;
            recorrido = recorrido + " ----- Cadena rechazada";
        }
        retorno[0] = valida;
        retorno[1] = recorrido;
        return retorno;
    }
    
    public void eliminaExtraños(){
        
    }
    
    public void simplificaEquivalentes(){
        
    }

}
