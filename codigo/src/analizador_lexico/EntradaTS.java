package analizador_lexico;

/**
 * Clase para modelar las entradas de la Tabla de Símbolos (solo para el
 * Analizador Léxico).
 */
public class EntradaTS {

    /**
     * El nombre del identificador.
     */
    private String lexema;

    /**
     * Constructor de la clase EntradaTS.
     *
     * @param lexema el nombre del identificador.
     */
    public EntradaTS(String lexema) {
        this.lexema = lexema;
    }

    /**
     * Devuelve el nombre del identificador.
     * 
     * @return el nombre del identificador.
     */
    public String getLexema() {
        return lexema;
    }

    /**
     * Establece el nombre del identificador.
     * 
     * @param lexema el nuevo nombre del identificador.
     */
    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    /**
     * Devuelve en forma de cadena (con un formato establecido) las entradas de la
     * Tabla de Símbolos.
     * 
     * @return la entrada en forma de cadena.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("* LEXEMA : '").append(lexema).append("'\n");
        return sb.toString();
    }

    /**
     * Compara este objeto EntradaTS con el objeto especificado para determinar si
     * son
     * iguales. Dos objetos EntradaTS se consideran iguales si tienen el mismo
     * {@code lexema}.
     *
     * @param obj el objeto a comparar con esta EntradaTS.
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
        EntradaTS other = (EntradaTS) obj;
        return lexema.equals(other.lexema);
    }

}
