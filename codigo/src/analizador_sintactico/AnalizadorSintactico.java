package analizador_sintactico;

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

    /**
     * Realiza el parse según corresponda (en este caso utilizando un
     * Analizador Sintáctico Descendente por Tablas).
     * 
     * @return El parse de la gramática.
     * @throws IOException Si hubiera algún problema con los ficheros.
     */
    public String parseSintactico() throws IOException {
        String parse = "Descendente";
        Stack<String> pila = new Stack<String>();
        pila.push("$");
        pila.push("P");
        FileReader fr = new FileReader(ficheroTokens);
        try (BufferedReader br = new BufferedReader(fr)) {
            String linea[] = br.readLine().split("<|,| ");
            String a = linea[1].toLowerCase();
            String pos = "";
            String consecuente[];
            while(!pila.peek().equals("eof")){
                pos = "";
                if(pila.peek().equals("lambda")) {
                    pila.pop();
                }
                else if(listaTerminales.contains(pila.peek())){
                    //System.out.println(a + " " + pila.peek());
                    if(pila.peek().equals(a)){
                        pila.pop();
                        linea = br.readLine().split("<|,| ");
                        a = linea[1].toLowerCase();
                    } else {
                        return "Error. Token no valido";
                    }
                } else {
                    pos += pila.peek() + " " + a;
                    //System.out.println(pos);
                    if(tablaSintactico.containsKey(pos)){
                        pila.pop();
                        consecuente = tablaSintactico.get(pos).split(" ");
                        parse += " " + consecuente[0];
                        for(int i = consecuente.length -1; i > 0; i--){
                            pila.push(consecuente[i]); 
                        }
                    } else {
                        return "Error. Casilla vacia";
                    }
                }   
                System.out.println(imprimirPila(pila) + "\n-------------");
            }
            if(!a.equals("eof")) return "Error. No EOF";
            fr.close();
            br.close();
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