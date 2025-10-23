package analizador_semantico;

import analizador_lexico.EntradaTS;

/**
 * Clase para modelar las funciones para las entradas de la TS.
 */
public class Funcion {

    /**
     * tipoDev --> El tipo de retorno de la función.
     * tipoParam --> El tipo de los parámetros de la función.
     * etiqueta --> La etiqueta de la función.
     */
    private String tipoDev, tipoParam, etiqueta;

    /**
     * numParam --> El número de los parámetros de la función.
     * modoParam --> EL modo del los parámetros de la función (1 para valor, 2 por
     * referencia).
     */
    private int numParam, modoParam;

    /**
     * Constructor de la clase Funcion.
     */
    public Funcion() {
    }

    /**
     * Devuelve el tipo de retorno de la función.
     * 
     * @return el tipo de retorno de la función.
     */
    public String getTipoDev() {
        return tipoDev;
    }

    /**
     * Establece el tipo de retorno de la función.
     * 
     * @param tipoDev el tipo de retorno de la función a establecer.
     */
    public void setTipoDev(String tipoDev) {
        this.tipoDev = tipoDev;
    }

    /**
     * Devuelve el tipo de los parámetros de la función.
     * 
     * @return el tipo de los parámetros de la función.
     */
    public String getTipoParam() {
        return tipoParam;
    }

    /**
     * Establece el tipo de los parámetros de la función.
     * 
     * @param tipoParam el tipo de los parámetros de la función a establecer.
     */
    public void setTipoParam(String tipoParam) {
        this.tipoParam = tipoParam;
    }

    /**
     * Devuelve la etiqueta de la función.
     * 
     * @return la etiqueta de la función.
     */
    public String getEtiqueta() {
        return etiqueta;
    }

    /**
     * Establece la etiqueta de la función.
     * 
     * @param etiqueta la etiqueta de la función a establecer.
     */
    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    /**
     * Devuelve el número de los parámetros de la función.
     * 
     * @return el número de los parámetros de la función.
     */
    public int getNumParam() {
        return numParam;
    }

    /**
     * Establece el número de los parámetros de la función.
     * 
     * @param numParam el número de los parámetros de la función a establecer.
     */
    public void setNumParam(int numParam) {
        this.numParam = numParam;
    }

    /**
     * Devuelve el modo de los parámetros de la función.
     * 
     * @return el modo de los parámetros de la función (1 para valor, 2 para
     *         referencia).
     */
    public int getModoParam() {
        return modoParam;
    }

    /**
     * Establece el modo de los parámetros de la función.
     * 
     * @param modoParam el modo de los parámetros de la función a establecer (1 para
     *                  valor, 2 para referencia).
     */
    public void setModoParam(int modoParam) {
        this.modoParam = modoParam;
    }

    /**
     * Devuelve en forma de cadena (con un formato establecido) la función de la
     * entrada asociada.
     * 
     * @return la función en forma de cadena.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  + numParam : ").append(numParam).append("\n");
        String[] tipos = tipoParam != null ? tipoParam.split(",") : new String[0];
        for (int i = 0; i < tipos.length; i++) {
            sb.append("  + TipoParam").append(i + 1).append(" : '").append(tipos[i].trim()).append("'\n");
            sb.append("  + ModoParam").append(i + 1).append(" : ").append(modoParam).append("\n");
        }
        sb.append("  + TipoRetorno : '").append(tipoDev).append("'\n");
        sb.append("  + EtiqFuncion : '").append(etiqueta).append("'\n");

        return sb.toString();
    }

    /**
     * Compara este objeto Funcion con el objeto especificado para determinar si
     * son
     * iguales. Dos objetos Funcion se consideran iguales si tienen la misma
     * {@code etiqueta}.
     *
     * @param obj el objeto a comparar con esta EntradaTSAS.
     * @return {@code true} si los objetos son iguales; {@code false} e.o.c.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EntradaTS)) {
            return false;
        }
        Funcion other = (Funcion) obj;
        return etiqueta.equals(other.etiqueta);
    }

}
