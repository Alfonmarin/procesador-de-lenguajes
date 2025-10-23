package analizador_semantico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Clase para modelar el Analizador Sintáctico.
 */
public class AnalizadorSintactico {

    /**
     * La gramática del Analizador Sintáctico.
     */
    private GramaticaAnalizadorSintactico grSintactico;
    /**
     * La lista de terminales.
     */
    private List<String> listaTerminales;

    /**
     * La tabla del Analizador Sintáctico.
     */
    private Map<String, String> tablaSintactico;

    /**
     * El fichero de Tokens.
     */
    private File ficheroTokens;

    private GestorErrores gestorErrores;

    /**
     * Constructor de la clase AnalizadorSintactico.
     * 
     * @param nombreFichero el nombre del fichero.
     * @throws IOException si hubiera algún problema con los ficheros.
     */
    public AnalizadorSintactico(File nombreFichero) throws IOException {
        // Inicializamos todo lo correspondiente...
        ficheroTokens = nombreFichero;
        grSintactico = new GramaticaAnalizadorSintactico();
        listaTerminales = grSintactico.getListaTerminales();
        tablaSintactico = grSintactico.gettablaSintactico();
        gestorErrores = new GestorErrores();

    }

    /**
     * Devuelve el analizador sintáctico de la gramática.
     *
     * @return El analizador sintáctico de la gramática.
     */
    public GramaticaAnalizadorSintactico getGrSintactico() {
        return grSintactico;
    }

    /**
     * Establece el analizador sintáctico de la gramática.
     *
     * @param grSintactico El nuevo analizador sintáctico de la gramática.
     */
    public void setGrSintactico(GramaticaAnalizadorSintactico grSintactico) {
        this.grSintactico = grSintactico;
    }

    /**
     * Devuelve la lista de los símbolos terminales de la gramática.
     * 
     * @return La lista de los símbolos terminales de la gramática.
     */
    public List<String> getListaTerminales() {
        return listaTerminales;
    }

    /**
     * Establece la lista de los símbolos terminales de la gramática.
     * 
     * @param listaTerminales La nueva lista de los símbolos terminales de la
     *                        gramática.
     */
    public void setListaTerminales(List<String> listaTerminales) {
        this.listaTerminales = listaTerminales;
    }

    /**
     * Devuelve la tabla de la gramática LL1.
     * 
     * @return La tabla de la gramática LL1.
     */
    public Map<String, String> getTablaSintactico() {
        return tablaSintactico;
    }

    /**
     * Establece la tabla de la gramática LL1.
     * 
     * @param tablaSintactico La nueva tabla de la gramática LL1.
     */
    public void setTablaSintactico(Map<String, String> tablaSintactico) {
        this.tablaSintactico = tablaSintactico;
    }

    /**
     * Devuelve el archivo de tokens.
     * 
     * @return El archivo de tokens.
     */
    public File getFicheroTokens() {
        return ficheroTokens;
    }

    /**
     * Establece el archivo de tokens.
     * 
     * @param ficheroTokens El nuevo archivo de tokens.
     */
    public void setFicheroTokens(File ficheroTokens) {
        this.ficheroTokens = ficheroTokens;
    }

    public GestorErrores getGestorErrores() {
        return gestorErrores;
    }

    /**
     * Realiza el parse según corresponda (en este caso utilizando un
     * Analizador Sintáctico Descendente por Tablas).
     * 
     * @return El parse de la gramática.
     * @throws IOException Si hubiera algún problema con los ficheros.
     */
    public String parseSintactico() throws IOException {
        String parse = "Descendente";
        Stack<String> pila = new Stack<>();
        pila.push("$");
        pila.push("P");
        FileReader fr = new FileReader(ficheroTokens);

        try (BufferedReader br = new BufferedReader(fr)) {
            String lin = br.readLine();
            if (lin != null) {
                String[] linea = lin.split("<|,| ");
                String a = linea[1].toLowerCase(); // Token actual
                String pos = "";
                String[] consecuente;

                while (!pila.peek().equals("eof")) {
                    pos = "";

                    if (pila.peek().equals("lambda")) {
                        pila.pop();
                    } else if (listaTerminales.contains(pila.peek())) {
                        if (pila.peek().equals(a)) {
                            pila.pop();
                            lin = br.readLine();
                            if (lin != null) {
                                linea = lin.split("<|,| ");
                                if (linea.length > 1) {
                                    a = linea[1].toLowerCase();
                                } else {
                                    a = "eof";
                                }
                            }
                        } else {
                            if (pila.peek().equals("input")) {
                                pila.pop(); // Eliminamos "input" de la pila
                                if (!a.equals("id")) {
                                    gestorErrores.escribirErrores(16, 0,
                                            "La instrucción 'input' no puede llevar una expresión cualquiera.");
                                    return parse; // Detenemos el análisis
                                }
                            } else if (pila.peek().equals("puntycom")) {
                                gestorErrores.escribirErrores(15, 0, "Falta de punto y coma al final de la sentencia.");
                                return parse; // Detenemos el análisis
                            } else if (pila.peek().equals("if") || pila.peek().equals("switch")) {
                                pila.pop(); // Eliminar "if" o "switch"
                                if (!a.equals("parentabre")) { // Falta el paréntesis abierto
                                    gestorErrores.escribirErrores(18, 0,
                                            "Paréntesis de la condición de la sentencia incorrectos.");
                                    return parse;
                                } else {
                                    pila.push("parentcierra");
                                }
                            } else if (pila.peek().equals("parentabre")) {
                                pila.pop(); // Eliminamos "("
                                if (a.equals("parentcierra")) {
                                    gestorErrores.escribirErrores(19, 0,
                                            "Falta la condición en la sentencia de control de flujo.");
                                    return parse;
                                }
                            } else if (pila.peek().equals("function")) {
                                pila.pop(); // Eliminar "function" de la pila
                                if (!a.equals("int") && !a.equals("boolean") && !a.equals("string")
                                        && !a.equals("void")) {
                                    gestorErrores.escribirErrores(17, 0,
                                            "La instrucción 'function' debe especificar un tipo de retorno válido.");
                                    return parse; // Detenemos el análisis
                                }
                            } else {
                                gestorErrores.escribirErrores(16, 0, "Token inesperado: " + a);
                                return parse; // Detenemos el análisis
                            }
                        }
                    } else {
                        if (lin == null) {
                            return parse;
                        } else {
                            pos += pila.peek() + " " + a;
                            if (tablaSintactico.containsKey(pos)) {
                                pila.pop();
                                consecuente = tablaSintactico.get(pos).split(" ");
                                parse += " " + consecuente[0];
                                for (int i = consecuente.length - 1; i > 0; i--) {
                                    pila.push(consecuente[i]);
                                }
                            } else {
                                gestorErrores.escribirErrores(17, 0, "Casilla vacía para: " + pos);
                                return parse; // Detenemos el análisis
                            }
                        }
                    }
                    System.out.println(imprimirPila(pila) + "\n-------------");
                }

                if (!a.equals("eof")) {
                    gestorErrores.escribirErrores(18, 0, "No se alcanzó el final del archivo correctamente.");
                    return parse; // Detenemos el análisis
                }
            }
        }

        return parse;
    }

    /**
     * Nos ayudamos para visualizar la Pila.
     * 
     * @param pila La pila.
     * @return La pila en forma de cadena.
     */
    private String imprimirPila(Stack<String> pila) {
        Iterator<String> it = pila.iterator();
        String x = it.next();
        while (it.hasNext()) {
            x = it.next() + "\n" + x;
        }
        return x;
    }

}