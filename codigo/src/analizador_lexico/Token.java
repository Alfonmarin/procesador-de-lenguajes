package analizador_lexico;

/**
 * Clase para modelar Tokens.
 */
public class Token {

    /**
     * El código que identifica el tipo de Token.
     */
    private String codigoToken;

    /**
     * El atributo del Token (puede ser un número (Integer), una cadena (String) o
     * nulo).
     */
    private Object atributo;

    /**
     * Constructor de la clase Token.
     *
     * @param codigoToken el código que identifica el tipo de Token.
     * @param atributo    el atributo del Token (puede ser un número (Integer), una
     *                    cadena (String) o nulo).
     */
    public Token(String codigoToken, Object atributo) {
        this.codigoToken = codigoToken;
        this.atributo = atributo;
    }

    /**
     * Devuelve el código que identifica el tipo de Token.
     *
     * @return el código que identifica el tipo de Token.
     */
    public String getCodigoToken() {
        return codigoToken;
    }

    /**
     * Establece el código que identifica el tipo de Token.
     *
     * @param codigoToken el nuevo código que identifica el tipo de Token.
     */
    public void setCodigoToken(String codigoToken) {
        this.codigoToken = codigoToken;
    }

    /**
     * Devuelve el atributo del Token (puede ser un número (Integer), una cadena
     * (String) o nulo).
     *
     * @return el atributo del Token (puede ser un número (Integer), una cadena
     *         (String) o nulo).
     */
    public Object getAtributo() {
        return atributo;
    }

    /**
     * Establece el atributo del Token (puede ser un número (Integer), una cadena
     * (String) o nulo).
     *
     * @param atributo el nuevo atributo del Token (puede ser un número (Integer),
     *                 una cadena (String) o nulo).
     */
    public void setAtributo(Object atributo) {
        this.atributo = atributo;
    }

    /**
     * Devuelve una representación en cadena del Token.
     *
     * @return una cadena con el código y el atributo del Token.
     */
    @Override
    public String toString() {
        String r = "<" + codigoToken;
        if (atributo == null) {
            r += ", >";
        } else {
            r += (", " + atributo + ">");
        }
        return r;
    }

    /**
     * Compara este objeto Token con el objeto especificado para determinar si
     * son
     * iguales. Dos objetos Token se consideran iguales si tienen el mismo
     * {@code codigoToken} y el mismo {@code atributo}.
     *
     * @param obj el objeto a comparar con este Token.
     * @return {@code true} si los objetos son iguales; {@code false} e.o.c
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Token)) {
            return false;
        }
        Token other = (Token) obj;
        return codigoToken.equals(other.codigoToken) && atributo.equals(other.atributo);
    }

}