package analizador_lexico;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Clase para modelar la Tabla de Símbolos (solo para Analizador Léxico).
 */
public class TablaDeSimbolos {

    /**
     * La Tabla de Símbolos.
     * Integer --> Posición de la entrada en la Tabla de Símbolos.
     * EntradaTS --> La entrada en la Tabla de Símbolos.
     */
    private Map<EntradaTS, Integer> tabla;

    /**
     * El número asignado a la tabla.
     */
    private int numTabla;

    /**
     * El mensaje de inicio asignado a la tabla (puede ser el simple nombre de la
     * tabla
     * o alguna especificación de ella).
     */
    private String mensaje;

    /**
     * Constructor de la clase TablaDeSímbolos.
     * 
     * @param numTabla el número asignado a la tabla.
     * @param mensaje  el mensaje de inicio asignado a la tabla (puede ser el simple
     *                 nombre de la
     *                 tabla
     *                 o alguna especificación de ella).
     */
    public TablaDeSimbolos(int numTabla, String mensaje) {
        this.tabla = new HashMap<EntradaTS, Integer>();
        this.numTabla = numTabla;
        this.mensaje = mensaje;
    }

    /**
     * Devuelve la Tabla de Símbolos.
     * 
     * @return la Tabla de Símbolos.
     */
    public Map<EntradaTS, Integer> getTabla() {
        return tabla;
    }

    /**
     * Establece la Tabla de Símbolos.
     * 
     * @param tabla la nueva Tabla de Símbolos.
     */
    public void setTabla(Map<EntradaTS, Integer> tabla) {
        this.tabla = tabla;
    }

    /**
     * Devuelve el número asignado a la tabla.
     * 
     * @return el número asignado a la tabla.
     */
    public int getNumTabla() {
        return numTabla;
    }

    /**
     * Establece el número asignado a la tabla.
     * 
     * @param numTabla el nuevo número asignado a la tabla.
     */
    public void setNumTabla(int numTabla) {
        this.numTabla = numTabla;
    }

    /**
     * Devuelve el mensaje de inicio asignado a la tabla (puede ser el simple nombre
     * de la tabla
     * o alguna especificación de ella).
     * 
     * @return el mensaje de inicio asignado a la tabla (puede ser el simple nombre
     *         de la tabla
     *         o alguna especificación de ella).
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Establece el mensaje de inicio asignado a la tabla (puede ser el simple
     * nombre de la tabla
     * o alguna especificación de ella).
     * 
     * @param mensaje el nuevo mensaje de inicio asignado a la tabla (puede ser el
     *                simple nombre de la tabla
     *                o alguna especificación de ella).
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    /**
     * Añadimos una nueva entrada a la Tabla de Símbolos.
     * 
     * @param entradaTS la entrada para la Tabla de Símbolos.
     * @param posicion  la posicion de la entrada para la Tabla de Símbolos.
     */
    public void añadirEntradaTablaDeSimbolos(EntradaTS entradaTS, int posicion) {
        this.tabla.put(entradaTS, posicion);
    }

    /**
     * Buscamos en la Tabla de Símbolos si existe una entrada con lexema
     * {@code lexema}.
     * 
     * @param lexema el lexema a buscar.
     * @return La entrada con el lexema indicado, y null e.o.c.
     */
    public EntradaTS getEntradaTablaDeSimbolos(String lexema) {
        EntradaTS r = null, aux = new EntradaTS(lexema), eTS = null;
        for (Entry<EntradaTS, Integer> t : tabla.entrySet()) {
            eTS = t.getKey();
            if (eTS.equals(aux)) {
                r = eTS;
                break;
            }
        }
        return r;
    }

    /**
     * Destruimos la Tabla de Símbolos.
     */
    public void destruyeTablaDeSimbolos() {
        this.tabla.clear();
        this.tabla = null;
    }

    /**
     * Buscamos en la Tabla de Símbolos si existe una entrada con lexema
     * {@code lexema} y devolvemos su posición.
     * 
     * @param lexema el lexema a buscar.
     * @return la posición de la entrada con el lexema indicado, y -1 e.o.c
     */
    public int getPosicionPorLexema(String lexema) {
        EntradaTS aux = new EntradaTS(lexema);
        for (EntradaTS e : tabla.keySet()) {
            if (e.equals(aux)) {
                return tabla.get(e);
            }
        }
        return -1;
    }

    /**
     * Devuelve en forma de cadena (con un formato establecido) el nombre y número
     * de la Tabla de Símbolos y sus entradas.
     * 
     * @return la tabla en forma de cadena.
     */
    @Override
    public String toString() {
        StringBuilder r = new StringBuilder("---------------------------------------------------\n");
        r.append(mensaje).append(" #").append(numTabla).append(" : \n");
        r.append("---------------------------------------------------\n");
        for (EntradaTS e : tabla.keySet()) {
            r.append(e).append("\n--------- ----------\n");
        }
        return r.toString();
    }

}