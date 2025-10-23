package analizador_semantico;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Clase para modelar la Tabla de Símbolos.
 */
public class TablaDeSimbolosAS {

    /**
     * La Tabla de Símbolos.
     * Integer --> Posición de la entrada en la Tabla de Símbolos.
     * EntradaTSAS --> La entrada en la Tabla de Símbolos.
     */
    private Map<EntradaTSAS, Integer> tabla;

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
     * Constructor de la clase TablaDeSímbolosAS.
     * 
     * @param numTabla el número asignado a la tabla.
     * @param mensaje  el mensaje de inicio asignado a la tabla (puede ser el simple
     *                 nombre de la
     *                 tabla
     *                 o alguna especificación de ella).
     */
    public TablaDeSimbolosAS(int numTabla, String mensaje) {
        this.tabla = new HashMap<EntradaTSAS, Integer>();
        this.numTabla = numTabla;
        this.mensaje = mensaje;
    }

    /**
     * Devuelve la Tabla de Símbolos.
     * 
     * @return la Tabla de Símbolos.
     */
    public Map<EntradaTSAS, Integer> getTabla() {
        return tabla;
    }

    /**
     * Establece la Tabla de Símbolos.
     * 
     * @param tabla la nueva Tabla de Símbolos.
     */
    public void setTabla(Map<EntradaTSAS, Integer> tabla) {
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
    public void añadirEntradaTablaDeSimbolos(EntradaTSAS entradaTS, int posicion) {
        this.tabla.put(entradaTS, posicion);
    }

    /**
     * Buscamos en la Tabla de Símbolos si existe una entrada con lexema
     * {@code lexema}.
     * 
     * @param lexema el lexema a buscar.
     * @return la entrada con el lexema y tipo indicado (y sus demás atributos si
     *         los tuviera), y null e.o.c.
     */
    public EntradaTSAS getEntradaTablaDeSimbolos(String lexema) {
        EntradaTSAS r = null, aux = new EntradaTSAS(lexema), eTS = null;
        for (Entry<EntradaTSAS, Integer> t : tabla.entrySet()) {
            eTS = t.getKey();
            if (eTS.equals(aux)) {
                r = eTS;
                aux = null;
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
        EntradaTSAS aux = new EntradaTSAS(lexema);
        for (EntradaTSAS e : tabla.keySet()) {
            if (e.equals(aux)) {
                return tabla.get(e);
            }
        }
        return -1;
    }

    /**
     * Devuelve en forma de cadena (con un formato establecido) el nombre y número
     * de la Tabla de Símbolos y sus entradas (de menor a mayor desplazamiento y si
     * son funciones, de
     * menor a mayor etiqueta).
     * 
     * @return La tabla en forma de cadena.
     */
    @Override
    public String toString() {
        StringBuilder r = new StringBuilder("---------------------------------------------------\n");
        r.append(mensaje).append(" #").append(numTabla).append(" : \n");
        r.append("---------------------------------------------------\n");
        List<EntradaTSAS> variables = new ArrayList<>();
        List<EntradaTSAS> funciones = new ArrayList<>();

        for (EntradaTSAS e : tabla.keySet()) {
            if ("funcion".equals(e.getTipo())) {
                funciones.add(e);
            } else {
                variables.add(e);
            }
        }

        variables.sort(Comparator.comparingInt(EntradaTSAS::getDesplazamiento));

        for (EntradaTSAS e : variables) {
            r.append(e).append("\n--------- ----------\n");
        }

        funciones.sort(Comparator.comparing(e -> {
            String etiqueta = e.getF().getEtiqueta();
            String lexema = e.getLexema();
            String numeroStr = etiqueta.replace("Et" + lexema, "");
            return Integer.parseInt(numeroStr);
        }));

        for (EntradaTSAS e : funciones) {
            r.append(e).append("\n--------- ----------\n");
        }

        return r.toString();
    }

}
