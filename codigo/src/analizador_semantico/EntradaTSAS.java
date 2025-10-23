package analizador_semantico;

/**
 * Clase para modelar las entradas de la Tabla de Símbolos.
 */
public class EntradaTSAS {

    /**
     * lexema --> El nombre del identificador.
     * tipo --> El tipo del lexema.
     */
    private String lexema, tipo;

    /**
     * El desplazamiento del lexema (solo se aplica a variables).
     */
    private int desplazamiento;

    /**
     * La funcion (si se aplica) de la entrada asociada.
     */
    private Funcion f;

    /**
     * Constructor de la clase EntradaTS.
     *
     * @param lexema el nombre del identificador.
     */
    public EntradaTSAS(String lexema) {
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
     * Devuelve el tipo del lexema.
     * 
     * @return el tipo del lexema.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo del lexema.
     * 
     * @param tipo el tipo del lexema a establecer.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Devuelve el desplazamiento del lexema.
     * 
     * @return el desplazamiento del lexema.
     */
    public int getDesplazamiento() {
        return desplazamiento;
    }

    /**
     * Establece el desplazamiento del lexema.
     * 
     * @param desplazamiento el desplazamiento del lexema a establecer.
     */
    public void setDesplazamiento(int desplazamiento) {
        this.desplazamiento = desplazamiento;
    }

    /**
     * Devuelve la función asociada.
     * 
     * @return la función asociada.
     */
    public Funcion getF() {
        return f;
    }

    /**
     * Establece la nueva función asociada.
     * 
     * @param desplazamiento la nueva función asociada.
     */
    public void setF(Funcion f) {
        this.f = f;
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
        sb.append("  ATRIBUTOS:\n");
        sb.append("  + tipo : '").append(tipo).append("'\n");

        if ("funcion".equals(tipo)) {
            sb.append(f.toString());
        } else {
            sb.append("  + despl : ").append(desplazamiento).append("\n");
        }

        return sb.toString();
    }

    /**
     * Compara este objeto EntradaTSAS con el objeto especificado para determinar si
     * son
     * iguales. Dos objetos EntradaTSAS se consideran iguales si tienen el mismo
     * {@code lexema}.
     *
     * @param obj el objeto a comparar con esta EntradaTSAS.
     * @return {@code true} si los objetos son iguales; {@code false} e.o.c.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EntradaTSAS)) {
            return false;
        }
        EntradaTSAS other = (EntradaTSAS) obj;
        return lexema.equals(other.lexema);
    }

}
