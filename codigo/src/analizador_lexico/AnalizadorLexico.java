package analizador_lexico;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.io.FileReader;
import java.io.IOException;

/**
 * Clase para modelar el Analizador Léxico.
 */
public class AnalizadorLexico {

    /**
     * Lector del fichero.
     */
    private FileReader lectorFichero;

    /**
     * Lista de palabras reservadas.
     */
    private List<String> palabrasReservadas;

    /**
     * Pila para la Tabla de Símbolos.
     */
    private Stack<TablaDeSimbolos> pilaTablaSimbolos;

    /**
     * lineaFichero --> Línea que se va a ir leyendo del fichero.
     * posId --> Posición correspondiente.
     */
    private int lineaFichero, posId;

    /**
     * El carácter que se lee.
     */
    private char caracterLeido;

    /**
     * El alfabeto del lenguaje (según nuestras opciones de la práctica).
     */
    private String alfabeto;

    /**
     * Constructor de la clase AnalizadorLexico.
     * 
     * @param nombreFichero el nombre de fichero de código.
     */
    public AnalizadorLexico(File nombreFichero) {
        this.pilaTablaSimbolos = new Stack<TablaDeSimbolos>();
        this.pilaTablaSimbolos.push(new TablaDeSimbolos(0, "Contenido de la Tabla de Simbolos Global"));
        this.lineaFichero = 1;
        this.alfabeto = "\r\0 \t\n￿abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789/'=&*;,+(){}:_";
        this.palabrasReservadas = Arrays.asList(
                "int",
                "string",
                "boolean",
                "var",
                "switch",
                "case",
                "function",
                "return",
                "input",
                "break",
                "output",
                "if",
                "void");

        /**
         * Probamos a abrir el fichero...
         */
        try {
            this.lectorFichero = new FileReader(nombreFichero);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Devuelve el lector del fichero.
     * 
     * @return el lector del fichero.
     */
    public FileReader getLectorFichero() {
        return lectorFichero;
    }

    /**
     * Establece el lector del fichero.
     * 
     * @param lectorFichero el nuevo lector del fichero.
     */
    public void setLectorFichero(FileReader lectorFichero) {
        this.lectorFichero = lectorFichero;
    }

    /**
     * Devuelve la lista de palabras reservadas.
     * 
     * @return la lista de palabras reservadas.
     */
    public List<String> getPalabrasReservadas() {
        return palabrasReservadas;
    }

    /**
     * Establece la lista de palabras reservadas.
     * 
     * @param palabrasReservadas la nueva lista de palabras reservadas.
     */
    public void setPalabrasReservadas(List<String> palabrasReservadas) {
        this.palabrasReservadas = palabrasReservadas;
    }

    /**
     * Devuelve la pila para la Tabla de Símbolos.
     * 
     * @return la pila para la Tabla de Símbolos.
     */
    public Stack<TablaDeSimbolos> getPilaTablaSimbolos() {
        return pilaTablaSimbolos;
    }

    /**
     * Establece la pila para la Tabla de Símbolos.
     * 
     * @param pilaTablaSimbolos la nueva pila para la Tabla de Símbolos.
     */
    public void setPilaTablaSimbolos(Stack<TablaDeSimbolos> pilaTablaSimbolos) {
        this.pilaTablaSimbolos = pilaTablaSimbolos;
    }

    /**
     * Devuelve el carácter que se lee.
     * 
     * @return el carácter que se lee.
     */
    public char getCaracterLeido() {
        return caracterLeido;
    }

    /**
     * Establece el carácter que se lee.
     * 
     * @param caracterLeido el nuevo carácter que se lee.
     */
    public void setCaracterLeido(char caracterLeido) {
        this.caracterLeido = caracterLeido;
    }

    /**
     * Devuelve la línea que se va a ir leyendo del fichero.
     * 
     * @return la línea que se va a ir leyendo del fichero.
     */
    public int getLineaFichero() {
        return lineaFichero;
    }

    /**
     * Establece la línea que se va a ir leyendo del fichero.
     * 
     * @param lineaFichero la nueva línea que se va a ir leyendo del fichero.
     */
    public void setLineaFichero(int lineaFichero) {
        this.lineaFichero = lineaFichero;
    }

    /**
     * Devuelve la posición correspondiente.
     * 
     * @return la posición correspondiente.
     */
    public int getPosId() {
        return posId;
    }

    /**
     * Establece la posición correspondiente.
     * 
     * @param posId la nueva posición correspondiente.
     */
    public void setPosId(int posId) {
        this.posId = posId;
    }

    /**
     * Devuelve el alfabeto del lenguaje (según nuestras opciones de la práctica).
     * 
     * @return el alfabeto del lenguaje (según nuestras opciones de la práctica).
     */
    public String getAlfabeto() {
        return alfabeto;
    }

    /**
     * Establece el alfabeto del lenguaje (según nuestras opciones de la práctica).
     * 
     * @param alfabeto el nuevo alfabeto del lenguaje (según nuestras opciones de la
     *                 práctica).
     */
    public void setAlfabeto(String alfabeto) {
        this.alfabeto = alfabeto;
    }

    /**
     * Leemos el archivo de Tokens.
     * 
     * @throws IOException lanza excepción si hubiera algún problema al leer.
     */
    public void leerArchivoTokens() throws IOException {
        try {
            this.caracterLeido = (char) lectorFichero.read();
            if (caracterLeido == '\n')
                lineaFichero++;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Comprobamos si {@code palabraReservada} está en la lista predefinida como
     * atributo.
     * 
     * @param palabraReservada la palabra a buscar.
     * @return true si se encuentra en {@code palabrasReservadas}, falso e.o.c.
     */
    private boolean buscarReservada(String palabraReservada) {
        return palabrasReservadas.contains(palabraReservada);
    }

    /**
     * Devuelve la posición de la entrada con {@code lexema} en función de en qué
     * Tabla de Símbolos se encuentre.
     * 
     * @param lexema el lexema a buscar.
     * @return la posición de la entrada con {@code lexema} en función de en qué
     *         Tabla de Símbolos se encuentre.
     */
    private int getPosIDTablaSimbolos(String lexema) {
        int r = -1;
        TablaDeSimbolos t = pilaTablaSimbolos.get(0);
        int aux = t.getPosicionPorLexema(lexema);
        if (aux != -1) {
            r = aux;
        }
        return r;

    }

    /**
     * Procesamos el lexema según corresponda y devolvemos un nuevo Token.
     * 
     * @param lexema el lexema a procesar.
     * @param token  el token.
     * 
     * @return el Token que se tiene que generar.
     */
    private Token procesarLexema(String lexema, Token token) {
        EntradaTS t = new EntradaTS(lexema);
        // Si no se encuentra una entrada con ese lexema en la tabla, se introduce...
        if (getPosIDTablaSimbolos(lexema) == -1) {
            pilaTablaSimbolos.get(0).añadirEntradaTablaDeSimbolos(t, posId++);
        }
        token = new Token("ID", getPosIDTablaSimbolos(lexema));
        return token;
    }

    /**
     * Leemos y generamos (en base a lo que leamos) el Token según corresponda.
     * 
     * @return un nuevo Token si se tiene que generar, o null e.o.c (que
     *         generalmente será por algún error, ya que normalmente SIEMPRE
     *         devuelve un Token).
     * @throws IOException Lanza excepción si hay algún problema con la escritura.
     */
    public Token leerToken() throws IOException {
        Token token = null;
        int estado = 0;
        int valor = 0;
        String lexema = "";
        int lineaAux = 0;
        boolean genToken = false;
        // Mientras no tengamos que generar Token...
        while (!genToken) {
            switch (estado) {
                case 0:
                    if (caracterLeido == '\r' || caracterLeido == '\0' || caracterLeido == ' ' || caracterLeido == '\t'
                            || caracterLeido == '\n') {
                        leerArchivoTokens();
                    } else if (caracterLeido == '￿') {
                        token = new Token("EOF", null);
                        genToken = true;
                    } else if (Character.isLetter(caracterLeido)) {
                        estado = 7;
                        lexema = lexema + (char) caracterLeido;
                        leerArchivoTokens();
                    } else if (Character.isDigit(caracterLeido)) {
                        estado = 3;
                        lexema = lexema + (char) caracterLeido;
                        leerArchivoTokens();
                    } else if (caracterLeido == '/') {
                        lineaAux = lineaFichero;
                        estado = 1;
                        leerArchivoTokens();
                    } else if (caracterLeido == '\'') {
                        lexema = lexema + (char) caracterLeido;
                        estado = 5;
                        leerArchivoTokens();
                    } else if (caracterLeido == '=') {
                        estado = 9;
                        leerArchivoTokens();
                    } else if (caracterLeido == '&') {
                        estado = 12;
                        leerArchivoTokens();
                    } else if (caracterLeido == '*') {
                        lineaAux = lineaFichero;
                        estado = 13;
                        leerArchivoTokens();
                    } else if (caracterLeido == ';' || caracterLeido == ',' || caracterLeido == '('
                            || caracterLeido == ')'
                            || caracterLeido == '{' || caracterLeido == '}' || caracterLeido == ':'
                            || caracterLeido == '+') {
                        estado = 15;
                    } else {
                        System.out.println(
                                "ERROR en línea " + lineaFichero + ": Carácter '" + Character.toString(caracterLeido)
                                        + "' no soportado o desconocido por el lenguaje.");
                        return null;
                    }
                    break;
                case 1:
                    estado = 2;
                    leerArchivoTokens();
                    break;
                case 2:
                    if (caracterLeido == '*') {
                        leerArchivoTokens();
                        if (caracterLeido == '/') {
                            estado = 0;
                            leerArchivoTokens();
                        }
                    } else if (caracterLeido == '￿') {
                        System.out.println("ERROR en línea " + lineaAux + ": Comentario abierto y no cerrado.");
                        return null;
                    } else {
                        leerArchivoTokens();
                    }
                    break;
                case 3:
                    if (Character.isLetterOrDigit(caracterLeido)) {
                        lexema = lexema + (char) caracterLeido;
                        leerArchivoTokens();
                    } else {
                        estado = 4;
                    }
                    break;
                case 4:
                    if (!lexema.matches("\\d+")) {
                        System.out.println(
                                "ERROR en línea " + lineaFichero + ": Variable '" + lexema + "' mal escrita.");
                        return null;
                    }
                    valor = Integer.parseInt(lexema);
                    if (valor > 32767) {
                        System.out.println("ERROR en línea " + lineaFichero + ": " + Integer.toString(valor)
                                + " ∈/ [0,32767] (no pertenece al rango).");
                        return null;
                    }
                    token = new Token("ENTERO", valor);
                    genToken = true;
                    break;
                case 5:
                    if (caracterLeido == '\'') {
                        lexema = lexema + (char) caracterLeido;
                        estado = 6;
                    } else if (caracterLeido == '￿') {
                        System.out.println("ERROR en línea " + lineaFichero + ": Cadena abierta y no cerrada.");
                        return null;
                    } else {
                        lexema = lexema + (char) caracterLeido;
                    }
                    leerArchivoTokens();
                    break;
                case 6:
                    if (lexema.length() >= 66) {
                        System.out.println("ERROR en línea " + lineaFichero + ": Cadena " + lexema
                                + " demasiado larga. Debe tener como máximo 64 caractéres entre las comillas simples.");
                        return null;
                    }
                    lexema = lexema.replace("'", "\"");
                    token = new Token("CADENA", lexema);
                    genToken = true;
                    break;
                case 7:
                    if ((Character.isDigit(caracterLeido) || Character.isLetter(caracterLeido) || caracterLeido == '_')
                            || alfabeto.indexOf(caracterLeido) == -1) {
                        lexema = lexema + (char) caracterLeido;
                        leerArchivoTokens();
                    } else {
                        estado = 8;
                    }
                    break;
                case 8:
                    if (buscarReservada(lexema)) {
                        token = new Token(lexema.toUpperCase(), null);
                    } else {
                        if (!lexema.matches("[" + alfabeto + "]*")) {
                            System.out.println(
                                    "ERROR en línea " + lineaFichero + ": Variable '" + lexema + "' mal escrita.");
                            return null;
                        } else {
                            token = procesarLexema(lexema, token);
                        }
                    }
                    genToken = true;
                    break;
                case 9:
                    if (caracterLeido == '=') {
                        estado = 10;
                        leerArchivoTokens();
                    } else {
                        estado = 11;
                    }
                    break;
                case 10:
                    token = new Token("IGUIGU", null);
                    genToken = true;
                    break;
                case 11:
                    token = new Token("IGUAL", null);
                    genToken = true;
                    break;
                case 12:
                    if (caracterLeido == '&') {
                        estado = 14;
                        leerArchivoTokens();
                    }
                    break;
                case 13:
                    if (caracterLeido == '=') {
                        estado = 16;
                        leerArchivoTokens();
                    } else if (caracterLeido == '/') {
                        System.out.println("ERROR en línea " + lineaAux + ": Comentario abierto y no cerrado.");
                        return null;
                    } else {
                        leerArchivoTokens();
                    }
                    break;
                case 14:
                    token = new Token("AND", null);
                    genToken = true;
                    break;
                case 15:
                    if (caracterLeido == ';') {
                        token = new Token("PUNTYCOM", null);
                    } else if (caracterLeido == ',') {
                        token = new Token("COMA", null);
                    } else if (caracterLeido == '(') {
                        token = new Token("PARENTABRE", null);
                    } else if (caracterLeido == ')') {
                        token = new Token("PARENTCIERRA", null);
                    } else if (caracterLeido == '{') {
                        token = new Token("LLAVEABRE", null);
                    } else if (caracterLeido == '}') {
                        token = new Token("LLAVECIERRA", null);
                    } else if (caracterLeido == ':') {
                        token = new Token("DOSPUNTOS", null);
                    } else if (caracterLeido == '+') {
                        token = new Token("SUMA", null);
                    }
                    genToken = true;
                    leerArchivoTokens();
                    break;
                case 16:
                    token = new Token("ASIGMULT", null);
                    genToken = true;
                    break;
            }
        }

        return token;
    }

}