package automatas;

import javax.swing.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Automata {

    private ArrayList<String> simbolos;
    private ArrayList<String> estados;
    private ArrayList<Integer> aceptacion;
    private ArrayList<Integer> validaNuevos;
    private ArrayList<ArrayList<String>> transicionesNuevas;
    private ArrayList<ArrayList<String>> transiciones;
    private ArrayList<String> estadosNuevos;
    
    private int columnas;

    public Automata(ArrayList<String> estados, ArrayList<String> simbolos, ArrayList<Integer> aceptacion, ArrayList<ArrayList<String>> transiciones) {

        columnas = transiciones.get(0).size();

        this.estados = estados;
        this.aceptacion = aceptacion;
        this.transiciones = transiciones;
        this.simbolos = simbolos;

        transicionesNuevas = new ArrayList();
        transicionesNuevas.add(this.transiciones.get(0));
        estadosNuevos = new ArrayList<String>();
        estadosNuevos.add(this.estados.get(0));
        validaNuevos = new ArrayList<Integer>();
        validaNuevos.add(this.aceptacion.get(0));
    }

    public boolean esVacio() {
        if (estados.size() == 0) {
            return true;
        }
        return false;
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
    
    public String mostrarAutomata() {
        String datos = "";
        for (int i = 0; i < estados.size(); i++) {
            datos = datos + "Estado: --------------- " + estados.get(i);
            datos = datos + "\nTransiciones: --- " + transiciones.get(i);
            datos = datos + "\nValida: ---------------- " + aceptacion.get(i) + "\n\n";
        }
        return datos;
    }

    public void removerExtranos() {
        int m = 1;
        int l = 0;
        int k = 0;
        while (k < transicionesNuevas.size()) {
            for (int j = 0; j < columnas; j++) {
                if (!estadosNuevos.contains(transicionesNuevas.get(k).get(j))) {
                    estadosNuevos.add(transicionesNuevas.get(k).get(j));
                }
            }
            k++;
            while (m < estadosNuevos.size()) {
                l = estados.indexOf(estadosNuevos.get(m));
                transicionesNuevas.add(transiciones.get(l));
                validaNuevos.add(aceptacion.get(l));
                m++;
            }
        }
    }

    public boolean estadoError(String estado) {
        int n = estadosNuevos.indexOf(estado);
        for (int i = 0; i < simbolos.size(); i++) {
            if (!transicionesNuevas.get(n).get(i).equals(estado)) {
                return false;
            }
        }
        if (validaNuevos.get(n) == 1) {
            return false;
        } else {
            return true;
        }
    }

    public void pasarAFDerterministico() {
        int m = 1;
        int l;
        int k = 0;
        int c = columnas;
        ArrayList<String> subEstado;
        ArrayList<Integer> indices;
        StringTokenizer tokens;
        while (k < c) {
            for (int j = 0; j < columnas; j++) {
                if (!estadosNuevos.contains(transicionesNuevas.get(k).get(j))) {
                    estadosNuevos.add(transicionesNuevas.get(k).get(j));
                }
            }
            k++;
            while (m < estadosNuevos.size()) {
                subEstado = new ArrayList<String>();
                indices = new ArrayList<Integer>();
                if (estadosNuevos.get(m).contains(",")) {
                    tokens = new StringTokenizer(estadosNuevos.get(m), ",");
                    while (tokens.hasMoreTokens()) {
                        indices.add(estados.indexOf(tokens.nextToken()));
                    }
                    String com;
                    for (int i = 0; i < columnas; i++) {
                        com = "";
                        for (int j = 0; j < indices.size(); j++) {
                            if (!com.contains(transiciones.get(indices.get(j)).get(i))) {
                                com = com.concat("," + transiciones.get(indices.get(j)).get(i));
                            }
                        }
                        com = com.replaceFirst(",", "");
                        com = ordenar(com);
                        subEstado.add(com);
                    }
                    transicionesNuevas.add(subEstado);
                    if (valorUno(indices)) {
                        validaNuevos.add(1);
                    } else {
                        validaNuevos.add(0);
                    }
                } else {
                    l = estados.indexOf(estadosNuevos.get(m));
                    transicionesNuevas.add(transiciones.get(l));
                    validaNuevos.add(aceptacion.get(l));
                }
                m++;
            }
            c = transicionesNuevas.size();
        }
        eliminarComas(estadosNuevos, transicionesNuevas);
        eliminarEstadoDeError();
    }

    public void eliminarEstadoDeError() {
        String error = null;
        for (int i = 0; i < estadosNuevos.size(); i++) {
            if (estadoError(estadosNuevos.get(i))) {
                error = estadosNuevos.get(i);
                break;
            }
        }
        if (error != null) {
            for (int i = 0; i < estadosNuevos.size(); i++) {
                if (estadosNuevos.get(i).contains(error) && !estadosNuevos.get(i).equals(error)) {
                    estadosNuevos.set(i, estadosNuevos.get(i).replace(error, ""));
                }
                for (int j = 0; j < transicionesNuevas.get(0).size(); j++) {
                    if (transicionesNuevas.get(i).get(j).contains(error) && !transicionesNuevas.get(i).get(j).equals(error)) {
                        transicionesNuevas.get(i).set(j, transicionesNuevas.get(i).get(j).replace(error, ""));
                    }
                }
            }
            eliminarRepetidos();
        }
    }

    public void eliminarRepetidos() {
        for (int i = 0; i < estadosNuevos.size(); i++) {
            for (int j = 0; j < estadosNuevos.size(); j++) {
                if (estadosNuevos.get(i).equals(estadosNuevos.get(j)) && i != j) {
                    estadosNuevos.remove(j);
                    transicionesNuevas.remove(j);
                    validaNuevos.remove(j);
                    j--;
                }
            }
        }
    }

    public void eliminarComas(ArrayList<String> estados, ArrayList<ArrayList<String>> transiciones) {
        for (int i = 0; i < estados.size(); i++) {
            estados.set(i, estados.get(i).replace(",", ""));
            for (int j = 0; j < transiciones.get(0).size(); j++) {
                transiciones.get(i).set(j, transiciones.get(i).get(j).replace(",", ""));
            }
        }
    }

    public void simplificarEquivalentes() {
        ArrayList<ArrayList<String>> particiones = new ArrayList<ArrayList<String>>();
        ArrayList<String> fila0 = new ArrayList<String>(), fila1 = new ArrayList<String>();
        ArrayList<String> temporal1;
        ArrayList<String> temporal2;
        for (int i = 0; i < transicionesNuevas.size(); i++) {
            if (validaNuevos.get(i) == 0) {
                fila0.add(estadosNuevos.get(i));
            } else {
                fila1.add(estadosNuevos.get(i));
            }
        }
        if (validaNuevos.get(0) == 1) {
            particiones.add(fila1);
            particiones.add(fila0);
        } else {
            particiones.add(fila0);
            particiones.add(fila1);
        }
        int p;
        int inicial = particiones.size();
        boolean control = true;
        while (control == true) {
            for (int i = 0; i < particiones.size(); i++) {
                if (particiones.get(i).size() > 1) {
                    for (int k = 0; k < transicionesNuevas.get(0).size(); k++) {
                        temporal1 = new ArrayList<String>();
                        for (int j = 0; j < particiones.get(i).size(); j++) {
                            p = estadosNuevos.indexOf(particiones.get(i).get(j));
                            temporal1.add(transicionesNuevas.get(p).get(k));
                        }
                        temporal2 = new ArrayList<String>();
                        for (int t = 0; t < particiones.size(); t++) {
                            if (particiones.get(t).contains(temporal1.get(0))) {
                                for (int n = 1; n < temporal1.size(); n++) {
                                    if (!particiones.get(t).contains(temporal1.get(n))) {
                                        temporal2.add(particiones.get(i).get(n));
                                        particiones.get(i).remove(n);
                                        temporal1.remove(n);
                                        n--;
                                    }
                                }
                                break;
                            }
                        }
                        if (temporal2.size() > 0) {
                            particiones.add(temporal2);
                        }
                    }
                }
            }
            if (particiones.size() > inicial) {
                control = true;
                inicial = particiones.size();
            } else {
                control = false;
            }
        }
        ArrayList<Integer> unos = new ArrayList<Integer>();
        transiciones = new ArrayList<ArrayList<String>>();
        estados = new ArrayList<String>();
        aceptacion = new ArrayList<Integer>();
        String nuevo;
        for (int i = 0; i < particiones.size(); i++) {
            if (particiones.get(i).size() > 1) {
                nuevo = "";
                for (int j = 0; j < particiones.get(i).size(); j++) {
                    nuevo = nuevo.concat("," + particiones.get(i).get(j));
                    unos.add(validaNuevos.get(estadosNuevos.indexOf(particiones.get(i).get(j))));
                }
                nuevo = nuevo.replaceFirst(",", "");
                estados.add(nuevo);
                if (unos.contains(1)) {
                    aceptacion.add(1);
                } else {
                    aceptacion.add(0);
                }
            } else {
                aceptacion.add(validaNuevos.get(estadosNuevos.indexOf(particiones.get(i).get(0))));
                estados.add(particiones.get(i).get(0));
            }
        }
        for (int i = 0; i < particiones.size(); i++) {
            if (particiones.get(i).size() < 2) {
                particiones.remove(i);
                i--;
            }
        }
        control = false;
        for (int i = 0; i < estados.size(); i++) {
            fila1 = new ArrayList<String>();
            if (!estados.get(i).contains(",")) {
                p = estadosNuevos.indexOf(estados.get(i));
            } else {
                int c = estados.get(i).indexOf(",");
                p = estadosNuevos.indexOf(estados.get(i).substring(0, c));
            }
            nuevo = "";
            for (int k = 0; k < transicionesNuevas.get(0).size(); k++) {
                for (int l = 0; l < particiones.size(); l++) {
                    if (particiones.get(l).contains(transicionesNuevas.get(p).get(k))) {
                        control = true;
                        nuevo = "";
                        for (int j = 0; j < particiones.get(l).size(); j++) {
                            nuevo = nuevo.concat("," + particiones.get(l).get(j));
                        }
                        nuevo = nuevo.replaceFirst(",", "");
                        break;
                    }
                }
                if (control) {
                    fila1.add(nuevo);
                    control = false;
                } else {
                    fila1.add(transicionesNuevas.get(p).get(k));
                }
            }
            transiciones.add(fila1);
        }
        eliminarComas(estados, transiciones);
        estadosNuevos = estados;
        validaNuevos = aceptacion;
        transicionesNuevas = transiciones;
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
        } else {
            valida = 0;
            recorrido = recorrido + " ----- Cadena rechazada";
        }
        retorno[0] = valida;
        retorno[1] = recorrido;
        return retorno;
    }

    public String ordenar(String estado) {
        ArrayList<String> ordenado = new ArrayList<String>();
        StringTokenizer tok = new StringTokenizer(estado, ",");
        String temp = "";
        while (tok.hasMoreTokens()) {
            ordenado.add(tok.nextToken());
        }
        for (int j = 0; j < ordenado.size() - 1; j++) {
            for (int i = 0; i < ordenado.size() - 1; i++) {
                if (ordenado.get(i).compareTo(ordenado.get(i + 1)) > 0) {
                    temp = ordenado.get(i);
                    ordenado.set(i, ordenado.get(i + 1));
                    ordenado.set(i + 1, temp);
                }
            }
        }
        temp = "";
        for (int i = 0; i < ordenado.size(); i++) {
            temp = temp.concat("," + ordenado.get(i));
        }
        temp = temp.replaceFirst(",", "");

        return temp;
    }

    public boolean valorUno(ArrayList<Integer> indices) {
        for (int i = 0; i < indices.size(); i++) {
            indices.set(i, aceptacion.get(indices.get(i)));
        }
        if (indices.contains(1)) {
            return true;
        }
        return false;
    }

    public String tipoAutomata() {
        for (int i = 0; i < transiciones.size(); i++) {
            for (int j = 0; j < transiciones.get(0).size(); j++) {
                if (transiciones.get(i).get(j).contains(",")) {
                    return "AFND";
                }
            }
        }
        return "AFD";
    }

    public ArrayList<ArrayList<String>> getTransicionesNuevas() {
        return transicionesNuevas;
    }

    public ArrayList<String> getEstadosNuevos() {
        return estadosNuevos;
    }

    public ArrayList<Integer> getValidaNuevos() {
        return validaNuevos;
    }
}
