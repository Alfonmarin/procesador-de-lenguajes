package analizador_semantico;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import analizador_lexico.Token;

public class AnalizadorLexicoAS {

    /**
     * Lector del fichero.
     */
    private FileReader lectorFichero;

    /**
     * Lista de palabras reservadas.
     */
    private List<String> palabrasReservadas;

    /**
     * Pila para la(s) Tabla(s) de Símbolos.
     */
    private Stack<TablaDeSimbolosAS> pilaTablaSimbolos;

    /**
     * Pila para el/los desplazamiento(s) correspondiente(s).
     */
    private Stack<Integer> pilaDesplazamientos;

    /**
     * lineaFichero --> Línea que se va a ir leyendo del fichero.
     * posId --> Posición correspondiente.
     * contEtiqueta --> Contador para las etiquetas de las funciones.
     * contPilas --> Contador para las pilas.
     * numTablas --> Número de las Tablas de Símbolos.
     * contNumParamFuncion --> Contador para comprobar lo del número de parámetros
     * de las funciones.
     * expectedContPilas --> Auxiliar del contador de las pilas para comprobar
     * errores.
     */
    private int lineaFichero, posId, contEtiqueta, contPilas, numTablas, contNumParamFuncion, expectedContPilas;

    /**
     * El carácter que se lee.
     */
    private char caracterLeido;

    /**
     * Mapa que representa los desplazamientos a sumar en función del tipo de la
     * variable.
     * Las keys son el tipo de variable (entero, lógico o cadena) y los values el
     * desplazamiento
     * correspondiente (1, 1 o 64).
     */
    private Map<String, Integer> desplazamientosLenguaje;

    /**
     * Array que representa un indicador de posibles palabras reservadas que se han
     * leído.
     */
    private boolean[] marcaReservadas;

    /**
     * Lista con el nombre de las funciones.
     */
    private List<String> listaNombreFunciones;

    /**
     * El Gestor de Errores.
     */
    private GestorErrores gestorErrores;

    /**
     * alfabeto --> El alfabeto del lenguaje (según nuestras opciones de la
     * práctica).
     * lexemaAux --> Una copia del último lexema leído (referente a nombres de
     * variables o funciones).
     * lexemaAuxFuncion --> Una copia de la última función leída (para comprobar si
     * tiene errores semánticos).
     */
    private String alfabeto, lexemaAux, lexemaAuxFuncion;

    /**
     * asignacion --> Comprobación para ver si se está asignando un valor a una
     * variable.
     * suma --> Comprobación para ver si se están sumando números.
     * rompe --> Comprobación para ver si se lee un break en un switch.
     * caso --> Lo mismo pero para un case.
     * numParamFuncion --> Comprobación para ver si se está pasando el número de
     * parámetros correcto
     * a una función.
     */
    private boolean asignacion, suma, rompe, caso, numParamFuncion;

    /**
     * Constructor de la clase AnalizadorLexico.
     * 
     * @param nombreFichero el nombre de fichero de código.
     */
    public AnalizadorLexicoAS(File nombreFichero) {
        this.pilaTablaSimbolos = new Stack<TablaDeSimbolosAS>();
        this.pilaDesplazamientos = new Stack<Integer>();
        this.listaNombreFunciones = new ArrayList<String>();
        this.pilaTablaSimbolos.push(new TablaDeSimbolosAS(numTablas++, "Contenido de la Tabla de Simbolos Global"));
        this.pilaDesplazamientos.push(0);
        this.gestorErrores = new GestorErrores();
        this.desplazamientosLenguaje = new HashMap<String, Integer>();
        this.desplazamientosLenguaje.put("entero", 1);
        this.desplazamientosLenguaje.put("logico", 1);
        this.desplazamientosLenguaje.put("cadena", 64);
        this.lineaFichero = 1;
        this.contPilas = 1;
        this.contEtiqueta = 1;
        this.alfabeto = "\r\0 \t\n￿abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789/'=&*;,+(){}:_";
        this.lexemaAux = new String();
        this.lexemaAuxFuncion = new String();
        this.marcaReservadas = new boolean[13];
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
     * Devuelve la pila para la(s) Tabla(s) de Símbolos.
     * 
     * @return la pila para la(s) Tabla(s) de Símbolos.
     */
    public Stack<TablaDeSimbolosAS> getPilaTablaSimbolos() {
        return pilaTablaSimbolos;
    }

    /**
     * Establece la pila para la(s) Tabla(s) de Símbolos.
     * 
     * @param pilaTablaSimbolos la nueva pila para la(s) Tabla(s) de Símbolos.
     */
    public void setPilaTablaSimbolos(Stack<TablaDeSimbolosAS> pilaTablaSimbolos) {
        this.pilaTablaSimbolos = pilaTablaSimbolos;
    }

    /**
     * Devuelve el caracter que se lee.
     * 
     * @return el caracter que se lee.
     */
    public char getCaracterLeido() {
        return caracterLeido;
    }

    /**
     * Establece el caracter que se lee.
     * 
     * @param caracterLeido el nuevo caracter que se lee.
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
     * Devuelve el Mapa que representa los desplazamientos
     * a sumar en función del tipo de la variable.
     * 
     * @return el Mapa que representa los desplazamientos
     *         a sumar en función del tipo de la variable.
     */
    public Map<String, Integer> getDesplazamientosLenguaje() {
        return desplazamientosLenguaje;
    }

    /**
     * Establece el Mapa que representa los desplazamientos
     * a sumar en función del tipo de la variable.
     * 
     * @param desplazamientosLenguaje el nuevo Mapa que representa los
     *                                desplazamientos
     *                                a sumar en función del tipo de la variable.
     */
    public void setDesplazamientosLenguaje(Map<String, Integer> desplazamientosLenguaje) {
        this.desplazamientosLenguaje = desplazamientosLenguaje;
    }

    /**
     * Devuelve el Array que representa un indicador
     * de posibles palabras reservadas que se han leído.
     * 
     * @return el Array que representa un indicador
     *         de posibles palabras reservadas que se han leído.
     */
    public boolean[] getMarcaReservadas() {
        return marcaReservadas;
    }

    /**
     * Establece el Array que representa un indicador
     * de posibles palabras reservadas que se han leído.
     * 
     * @param marcaReservadas el nuevo Array que representa un
     *                        indicador de posibles palabras reservadas que se han
     *                        leído.
     */
    public void setMarcaReservadas(boolean[] marcaReservadas) {
        this.marcaReservadas = marcaReservadas;
    }

    /**
     * Devuelve el contador para las etiquetas de las funciones.
     * 
     * @return el contador para las etiquetas de las funciones.
     */
    public int getContEtiqueta() {
        return contEtiqueta;
    }

    /**
     * Establece el contador para las etiquetas de las funciones.
     * 
     * @param contEtiqueta el nuevo contador para las etiquetas de las funciones.
     */
    public void setContEtiqueta(int contEtiqueta) {
        this.contEtiqueta = contEtiqueta;
    }

    /**
     * Devuelve la lista con los nombres de las funciones.
     * 
     * @return la lista con los nombres de las funciones.
     */
    public List<String> getListaNombreFunciones() {
        return listaNombreFunciones;
    }

    /**
     * Establece la lista con los nombres de las funciones.
     * 
     * @param listaNombreFunciones la nueva lista con los nombres de las funciones.
     */
    public void setListaNombreFunciones(List<String> listaNombreFunciones) {
        this.listaNombreFunciones = listaNombreFunciones;
    }

    /**
     * Devuelve el número de tablas de símbolos.
     * 
     * @return el número de tablas de símbolos.
     */
    public int getNumTablas() {
        return numTablas;
    }

    /**
     * Establece el número de tablas de símbolos.
     * 
     * @param numTablas el nuevo número de tablas de símbolos.
     */
    public void setNumTablas(int numTablas) {
        this.numTablas = numTablas;
    }

    /**
     * Devuelve el contador para las pilas.
     * 
     * @return el contador para las pilas.
     */
    public int getContPilas() {
        return contPilas;
    }

    /**
     * Establece el contador para las pilas.
     * 
     * @param contPilas el nuevo contador para las pilas.
     */
    public void setContPilas(int contPilas) {
        this.contPilas = contPilas;
    }

    /**
     * Devuelve la pila de desplazamientos correspondiente.
     * 
     * @return la pila de desplazamientos correspondiente.
     */
    public Stack<Integer> getPilaDesplazamientos() {
        return pilaDesplazamientos;
    }

    /**
     * Establece la pila de desplazamientos correspondiente.
     * 
     * @param pilaDesplazamientos la nueva pila de desplazamientos correspondiente.
     */
    public void setPilaDesplazamientos(Stack<Integer> pilaDesplazamientos) {
        this.pilaDesplazamientos = pilaDesplazamientos;
    }

    /**
     * Devuelve el Gestor de Errores.
     * 
     * @return el Gestor de Errores.
     */
    public GestorErrores getGestorErrores() {
        return gestorErrores;
    }

    /**
     * Establece el Gestor de Errores.
     * 
     * @param gestorErrores el nuevo Gestor de Errores.
     */
    public void setGestorErrores(GestorErrores gestorErrores) {
        this.gestorErrores = gestorErrores;
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
     * Devuelve una copia del último lexema leído (referente a nombres de variables
     * o funciones).
     * 
     * @return una copia del último lexema leído (referente a nombres de variables o
     *         funciones).
     */
    public String getLexemaAux() {
        return lexemaAux;
    }

    /**
     * Establece una copia del último lexema leído (referente a nombres de variables
     * o funciones).
     * 
     * @param lexemaAux una nueva copia del último lexema leído (referente a nombres
     *                  de variables o funciones).
     */
    public void setLexemaAux(String lexemaAux) {
        this.lexemaAux = lexemaAux;
    }

    /**
     * Devuelve el estado de asignación (si se está asignando un valor a una
     * variable).
     * 
     * @return true si se está realizando una asignación; false en caso contrario.
     */
    public boolean isAsignacion() {
        return asignacion;
    }

    /**
     * Establece el estado de asignación.
     * 
     * @param asignacion true si se está realizando una asignación; false en caso
     *                   contrario.
     */
    public void setAsignacion(boolean asignacion) {
        this.asignacion = asignacion;
    }

    /**
     * Devuelve el estado de suma (si se están sumando valores).
     * 
     * @return true si se está realizando una suma; false en caso contrario.
     */
    public boolean isSuma() {
        return suma;
    }

    /**
     * Establece el estado de suma.
     * 
     * @param suma true si se está realizando una suma; false en caso contrario.
     */
    public void setSuma(boolean suma) {
        this.suma = suma;
    }

    /**
     * Devuelve el estado de rompe (si se está evaluando un "break" en un switch).
     * 
     * @return true si se está evaluando un "break"; false en caso contrario.
     */
    public boolean isRompe() {
        return rompe;
    }

    /**
     * Establece el estado de rompe.
     * 
     * @param rompe true si se está evaluando un "break"; false en caso contrario.
     */
    public void setRompe(boolean rompe) {
        this.rompe = rompe;
    }

    /**
     * Devuelve el estado de caso (si se está evaluando un "case" en un switch).
     * 
     * @return true si se está evaluando un "case"; false en caso contrario.
     */
    public boolean isCaso() {
        return caso;
    }

    /**
     * Establece el estado de caso.
     * 
     * @param caso true si se está evaluando un "case"; false en caso contrario.
     */
    public void setCaso(boolean caso) {
        this.caso = caso;
    }

    /**
     * Devuelve el contador del número de parámetros evaluados en una función.
     * 
     * @return el contador del número de parámetros evaluados.
     */
    public int getContNumParamFuncion() {
        return contNumParamFuncion;
    }

    /**
     * Establece el contador del número de parámetros evaluados en una función.
     * 
     * @param contNumParamFuncion el nuevo valor del contador.
     */
    public void setContNumParamFuncion(int contNumParamFuncion) {
        this.contNumParamFuncion = contNumParamFuncion;
    }

    /**
     * Devuelve el estado de la comprobación del número de parámetros en una
     * función.
     * 
     * @return true si se está verificando el número de parámetros; false en caso
     *         contrario.
     */
    public boolean isNumParamFuncion() {
        return numParamFuncion;
    }

    /**
     * Establece el estado de la comprobación del número de parámetros en una
     * función.
     * 
     * @param numParamFuncion true si se está verificando el número de parámetros;
     *                        false en caso contrario.
     */
    public void setNumParamFuncion(boolean numParamFuncion) {
        this.numParamFuncion = numParamFuncion;
    }

    /**
     * Devuelve el lexema auxiliar asociado a la última función evaluada.
     * 
     * @return el lexema auxiliar de la función.
     */
    public String getLexemaAuxFuncion() {
        return lexemaAuxFuncion;
    }

    /**
     * Establece el lexema auxiliar asociado a la última función evaluada.
     * 
     * @param lexemaAuxFuncion el nuevo lexema auxiliar de la función.
     */
    public void setLexemaAuxFuncion(String lexemaAuxFuncion) {
        this.lexemaAuxFuncion = lexemaAuxFuncion;
    }

    public int getExpectedContPilas() {
        return expectedContPilas;
    }

    public void setExpectedContPilas(int expectedContPilas) {
        this.expectedContPilas = expectedContPilas;
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
     * En función de la palabra reservada leída, marcamos su índice en el array
     * correspondiente.
     * 
     * @param lexema la palabra reservada que se ha leído.
     */
    private void marcaReservada(String lexema) {
        int i = palabrasReservadas.indexOf(lexema);
        if (i != -1) {
            marcaReservadas[i] = true;
        }
    }

    /**
     * Se reinicia el array.
     */
    private void reiniciaReservadas() {
        Arrays.fill(marcaReservadas, false);
    }

    /**
     * Devuelve la posición de la entrada con {@code lexema} en función de en qué
     * Tabla de Símbolos se encuentre.
     * 
     * @param lexema el lexema a buscar.
     * @return la posición de la entrada con {@code lexema} en función de en qué
     *         Tabla de Símbolos se encuentre.
     */
    private int getPosIDTablasSimbolos(String lexema) {
        int r = -1;
        if (contPilas > 1) {
            TablaDeSimbolosAS t = pilaTablaSimbolos.get(contPilas - 1);
            int aux = t.getPosicionPorLexema(lexema);
            if (aux != -1) {
                r = aux;
                expectedContPilas = contPilas - 1;
            } else {
                for (int i = contPilas - 2; i >= 0; i--) {
                    t = pilaTablaSimbolos.get(i);
                    aux = t.getPosicionPorLexema(lexema);
                    if (aux != -1) {
                        r = aux;
                        expectedContPilas = i;
                        break;
                    }
                }
            }
        } else {
            TablaDeSimbolosAS t = pilaTablaSimbolos.get(contPilas - 1);
            int aux = t.getPosicionPorLexema(lexema);
            if (aux != -1) {
                r = aux;
            }
        }
        return r;
    }

    /**
     * Devuelve la entrada asociada a una posición en la Tabla de Símbolos.
     *
     * @param posicion la posición de la entrada en la Tabla de Símbolos.
     * @return la entrada asociada a la posición especificada, o null si no existe.
     */
    public EntradaTSAS getEntradaPorPosicion(int posicion) {
        for (int i = contPilas - 1; i >= 0; i--) {
            TablaDeSimbolosAS t = pilaTablaSimbolos.get(i);
            for (Entry<EntradaTSAS, Integer> entrada : t.getTabla().entrySet()) {
                if (entrada.getValue() == posicion) {
                    return entrada.getKey();
                }
            }
        }
        return null;
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
        EntradaTSAS t = new EntradaTSAS(lexema);
        int desplazamiento = pilaDesplazamientos.get(contPilas - 1);
        // Se ha leido var...
        if (marcaReservadas[palabrasReservadas.indexOf("var")]) {
            // ...int...
            if (marcaReservadas[palabrasReservadas.indexOf("int")]) {
                t.setTipo("entero");
                t.setDesplazamiento(desplazamiento);
                int p = getPosIDTablasSimbolos(lexema);
                if (p != -1 && (contPilas - 1) == expectedContPilas) {
                    gestorErrores.escribirErrores(13, lineaFichero, lexema);
                    return null;
                }
                pilaTablaSimbolos.get(contPilas - 1).añadirEntradaTablaDeSimbolos(t, (p != -1) ? p : posId++);
                desplazamiento += desplazamientosLenguaje.get("entero");
                pilaDesplazamientos.set(contPilas - 1, desplazamiento);
                // ...string...
            } else if (marcaReservadas[palabrasReservadas.indexOf("string")]) {
                t.setTipo("cadena");
                t.setDesplazamiento(desplazamiento);
                int p = getPosIDTablasSimbolos(lexema);
                if (p != -1 && (contPilas - 1) == expectedContPilas) {
                    gestorErrores.escribirErrores(13, lineaFichero, lexema);
                    return null;
                }
                pilaTablaSimbolos.get(contPilas - 1).añadirEntradaTablaDeSimbolos(t, (p != -1) ? p : posId++);
                desplazamiento += desplazamientosLenguaje.get("cadena");
                pilaDesplazamientos.set(contPilas - 1, desplazamiento);
                // ...boolean...
            } else {
                t.setTipo("logico");
                t.setDesplazamiento(desplazamiento);
                int p = getPosIDTablasSimbolos(lexema);
                if (p != -1 && (contPilas - 1) == expectedContPilas) {
                    gestorErrores.escribirErrores(13, lineaFichero, lexema);
                    return null;
                }
                pilaTablaSimbolos.get(contPilas - 1).añadirEntradaTablaDeSimbolos(t, (p != -1) ? p : posId++);
                desplazamiento += desplazamientosLenguaje.get("logico");
                pilaDesplazamientos.set(contPilas - 1, desplazamiento);
            }
            // Se ha leído function...
        } else if (marcaReservadas[palabrasReservadas.indexOf("function")]) {
            String lexemaaux = lexema;
            Funcion f = new Funcion();
            this.listaNombreFunciones.add(lexema);
            if (!lexemaaux.matches("^[a-zA-Z][a-zA-Z0-9]*$")) {
                StringBuilder nuevoLexema = new StringBuilder();
                for (char c : lexemaaux.toCharArray()) {
                    if (Character.isLetterOrDigit(c)) {
                        nuevoLexema.append(c);
                    }
                }
                lexemaaux = nuevoLexema.toString();
            }
            this.pilaDesplazamientos.push(0);
            this.contPilas += (pilaDesplazamientos.size() - 1);
            // ...int...
            if (marcaReservadas[palabrasReservadas.indexOf("int")]) {
                f.setTipoDev("entero");
                f.setEtiqueta("Et" + lexema + contEtiqueta++);
                // ...string...
            } else if (marcaReservadas[palabrasReservadas.indexOf("string")]) {
                f.setTipoDev("cadena");
                f.setEtiqueta("Et" + lexema + contEtiqueta++);
                // ...void...
            } else if (marcaReservadas[palabrasReservadas.indexOf("void")]) {
                f.setTipoDev("vacio");
                f.setEtiqueta("Et" + lexema + contEtiqueta++);
                // ...boolean...
            } else {
                f.setTipoDev("logico");
                f.setEtiqueta("Et" + lexema + contEtiqueta++);
            }
            t.setF(f);
            t.setTipo("funcion");
            this.pilaTablaSimbolos.push(
                    new TablaDeSimbolosAS(numTablas++,
                            "Contenido de la Tabla de Simbolos de la funcion con numero de etiqueta"));
            pilaTablaSimbolos.get(0).añadirEntradaTablaDeSimbolos(t, posId++);
            token = new Token("ID", pilaTablaSimbolos.get(0).getTabla()
                    .get(pilaTablaSimbolos.get(0).getEntradaTablaDeSimbolos(lexema)));
            return token;
        }
        // Se ha leído un parámetro entero de una función...
        else if (marcaReservadas[palabrasReservadas.indexOf("int")]) {
            t.setTipo("entero");
            t.setDesplazamiento(desplazamiento);
            int p = getPosIDTablasSimbolos(lexema);
            if (p != -1 && (contPilas - 1) == expectedContPilas) {
                gestorErrores.escribirErrores(13, lineaFichero, lexema);
                return null;
            }
            pilaTablaSimbolos.get(contPilas - 1).añadirEntradaTablaDeSimbolos(t, (p != -1) ? p : posId++);
            desplazamiento += desplazamientosLenguaje.get("entero");
            pilaDesplazamientos.set(contPilas - 1, desplazamiento);
            EntradaTSAS aux = pilaTablaSimbolos.get(0)
                    .getEntradaTablaDeSimbolos(listaNombreFunciones.get(listaNombreFunciones.size() - 1));
            Funcion faux = aux.getF();
            int numParamsFaux = faux.getNumParam();
            numParamsFaux++;
            faux.setNumParam(numParamsFaux);
            faux.setModoParam(1);
            String s = faux.getTipoParam();
            if (s != null) {
                s += "entero,";
            } else {
                s = "entero,";
            }
            faux.setTipoParam(s);
            pilaTablaSimbolos.get(0)
                    .getEntradaTablaDeSimbolos(listaNombreFunciones.get(listaNombreFunciones.size() - 1))
                    .setF(faux);
        } // Se ha leído un parámetro cadena de una función...
        else if (marcaReservadas[palabrasReservadas.indexOf("string")]) {
            t.setTipo("cadena");
            t.setDesplazamiento(desplazamiento);
            int p = getPosIDTablasSimbolos(lexema);
            if (p != -1 && (contPilas - 1) == expectedContPilas) {
                gestorErrores.escribirErrores(13, lineaFichero, lexema);
                return null;
            }
            pilaTablaSimbolos.get(contPilas - 1).añadirEntradaTablaDeSimbolos(t, (p != -1) ? p : posId++);
            desplazamiento += desplazamientosLenguaje.get("cadena");
            pilaDesplazamientos.set(contPilas - 1, desplazamiento);
            EntradaTSAS aux = pilaTablaSimbolos.get(0)
                    .getEntradaTablaDeSimbolos(listaNombreFunciones.get(listaNombreFunciones.size() - 1));
            Funcion faux = aux.getF();
            int numParamsFaux = faux.getNumParam();
            numParamsFaux++;
            faux.setNumParam(numParamsFaux);
            faux.setModoParam(1);
            String s = faux.getTipoParam();
            if (s != null) {
                s += "cadena,";
            } else {
                s = "cadena,";
            }
            faux.setTipoParam(s);
            pilaTablaSimbolos.get(0)
                    .getEntradaTablaDeSimbolos(listaNombreFunciones.get(listaNombreFunciones.size() - 1))
                    .setF(faux);
        } // Se ha leído un parámetro booleano de una función...
        else if (marcaReservadas[palabrasReservadas.indexOf("boolean")]) {
            t.setTipo("logico");
            t.setDesplazamiento(desplazamiento);
            int p = getPosIDTablasSimbolos(lexema);
            if (p != -1 && (contPilas - 1) == expectedContPilas) {
                gestorErrores.escribirErrores(13, lineaFichero, lexema);
                return null;
            }
            pilaTablaSimbolos.get(contPilas - 1).añadirEntradaTablaDeSimbolos(t, (p != -1) ? p : posId++);
            desplazamiento += desplazamientosLenguaje.get("logico");
            pilaDesplazamientos.set(contPilas - 1, desplazamiento);
            EntradaTSAS aux = pilaTablaSimbolos.get(0)
                    .getEntradaTablaDeSimbolos(listaNombreFunciones.get(listaNombreFunciones.size() - 1));
            Funcion faux = aux.getF();
            int numParamsFaux = faux.getNumParam();
            numParamsFaux++;
            faux.setNumParam(numParamsFaux);
            faux.setModoParam(1);
            String s = faux.getTipoParam();
            if (s != null) {
                s += "logico,";
            } else {
                s = "logico,";
            }
            faux.setTipoParam(s);
            pilaTablaSimbolos.get(0)
                    .getEntradaTablaDeSimbolos(listaNombreFunciones.get(listaNombreFunciones.size() - 1))
                    .setF(faux);

        }
        // Se ha leído un indentificador no declarado previamente
        else if (getPosIDTablasSimbolos(lexema) == -1) {
            // Se ha leído una función no declarada previamente --> error semántico
            if (caracterLeido == '(') {
                gestorErrores.escribirErrores(6, lineaFichero, lexema);
                return null;
                // Se ha leído una variable no declarada previamente --> se hace entera y global
            } else {
                desplazamiento = pilaDesplazamientos.get(0);
                t.setTipo("entero");
                t.setDesplazamiento(desplazamiento);
                pilaTablaSimbolos.get(0).añadirEntradaTablaDeSimbolos(t, posId++);
                desplazamiento += desplazamientosLenguaje.get("entero");
                pilaDesplazamientos.set(0, desplazamiento);
            }
            // Se está evaluando un switch...
        } else if (marcaReservadas[palabrasReservadas.indexOf("switch")] && caracterLeido == ')'
                && !marcaReservadas[palabrasReservadas.indexOf("if")]) {
            EntradaTSAS aux = getEntradaPorPosicion(getPosIDTablasSimbolos(lexema));
            // ... y además el parámetro del switch no es un número --> error semántico
            String tipoAux = (aux.getF() != null) ? aux.getF().getTipoDev() : aux.getTipo();
            if (!tipoAux.equals("entero")) {
                gestorErrores.escribirErrores(7, lineaFichero, lexema);
                return null;
            }
            // Se está evaluando un return...
        } else if (marcaReservadas[palabrasReservadas.indexOf("return")]) {
            EntradaTSAS aux = getEntradaPorPosicion(getPosIDTablasSimbolos(lexema));
            EntradaTSAS fD = getEntradaPorPosicion(
                    getPosIDTablasSimbolos(listaNombreFunciones.get(listaNombreFunciones.size() - 1)));
            String tipoAux = (aux.getF() != null) ? aux.getF().getTipoDev() : aux.getTipo();
            // ... y algún identificador no es correcto para el return --> error semántico
            if (!tipoAux.equals(fD.getF().getTipoDev())) {
                gestorErrores.escribirErrores(14, lineaFichero, fD.getLexema());
                return null;
            }
        }
        EntradaTSAS func = getEntradaPorPosicion(getPosIDTablasSimbolos(lexema));
        if (func.getF() != null) {
            lexemaAuxFuncion = func.getLexema();
            numParamFuncion = true;
        } else if (numParamFuncion) {
            EntradaTSAS paramFunc = getEntradaPorPosicion(getPosIDTablasSimbolos(lexema));
            contNumParamFuncion++;
            func = getEntradaPorPosicion(getPosIDTablasSimbolos(lexemaAuxFuncion));
            if (func.getF().getNumParam() != 0) {
                String[] tipos = func.getF().getTipoParam().split(",");
                if (contNumParamFuncion <= tipos.length) {
                    if (!tipos[contNumParamFuncion - 1].equals(paramFunc.getTipo())) {
                        gestorErrores.escribirErrores(12, lineaFichero, lexemaAuxFuncion);
                        return null;
                    }
                } else {
                    gestorErrores.escribirErrores(11, lineaFichero, lexemaAuxFuncion);
                    return null;
                }
            }
        }

        token = new Token("ID", getPosIDTablasSimbolos(lexema));
        return token;
    }

    /**
     * Leemos y generamos (en base a lo que leamos) el Token según corresponda.
     * 
     * @return un nuevo Token si se tiene que generar, o null e.o.c (que
     *         generalmente será por algún error, ya que normalmente SIEMPRE
     *         devuelve un Token).
     * @throws IOException lanza excepción si hay algún problema con la escritura.
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
                        gestorErrores.escribirErrores(0, lineaFichero, Character.toString(caracterLeido));
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
                        gestorErrores.escribirErrores(2, lineaAux, new String());
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
                        gestorErrores.escribirErrores(1, lineaFichero, lexema);
                        return null;
                    }
                    valor = Integer.parseInt(lexema);
                    if (valor > 32767) {
                        gestorErrores.escribirErrores(5, lineaFichero, Integer.toString(valor));
                        return null;
                    } else if (asignacion) {
                        EntradaTSAS aux = getEntradaPorPosicion(getPosIDTablasSimbolos(lexemaAux));
                        if (!aux.getTipo().equals("entero")) {
                            gestorErrores.escribirErrores(8, lineaFichero, lexemaAux);
                            return null;
                        }
                    }
                    token = new Token("ENTERO", valor);
                    genToken = true;
                    break;
                case 5:
                    if (caracterLeido == '\'') {
                        lexema = lexema + (char) caracterLeido;
                        estado = 6;
                    } else if (caracterLeido == '￿') {
                        gestorErrores.escribirErrores(4, lineaFichero, new String());
                        return null;
                    } else {
                        lexema = lexema + (char) caracterLeido;
                    }
                    leerArchivoTokens();
                    break;
                case 6:
                    if (lexema.length() >= 66) {
                        gestorErrores.escribirErrores(3, lineaFichero, lexema);
                        return null;
                    }
                    lexema = lexema.replace("'", "\"");
                    if (asignacion) {
                        EntradaTSAS aux = getEntradaPorPosicion(getPosIDTablasSimbolos(lexemaAux));
                        if (!aux.getTipo().equals("cadena")) {
                            gestorErrores.escribirErrores(8, lineaFichero, lexemaAux);
                            return null;
                        }
                    }
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
                        if ((lexema.equals("break") && !rompe) ||
                                lexema.equals("case") && !caso) {
                            gestorErrores.escribirErrores(10, lineaFichero, lexema);
                            return null;
                        }
                        token = new Token(lexema.toUpperCase(), null);
                        marcaReservada(lexema);
                        numParamFuncion = false;
                    } else {
                        if (!lexema.matches("[" + alfabeto + "]*")) {
                            gestorErrores.escribirErrores(1, lineaFichero, lexema);
                            return null;
                        } else {
                            token = procesarLexema(lexema, token);
                            EntradaTSAS aux = getEntradaPorPosicion(getPosIDTablasSimbolos(lexemaAux));
                            EntradaTSAS entrada = getEntradaPorPosicion(getPosIDTablasSimbolos(lexema));
                            if (suma && aux != null && entrada != null) {
                                String tipoAux = (aux.getF() != null) ? aux.getF().getTipoDev() : aux.getTipo();
                                String tipoEntrada = (entrada.getF() != null) ? entrada.getF().getTipoDev()
                                        : entrada.getTipo();
                                if (!tipoAux.equals(tipoEntrada) && tipoAux.equals("entero")
                                        && tipoEntrada.equals("entero")) {
                                    gestorErrores.escribirErrores(8, lineaFichero, lexemaAux);
                                    return null;
                                }
                            } else if (asignacion && aux != null && entrada != null) {
                                String tipoAux = aux.getF() != null ? aux.getF().getTipoDev() : aux.getTipo();
                                String tipoEntrada = entrada.getF() != null ? entrada.getF().getTipoDev()
                                        : entrada.getTipo();
                                if (!tipoAux.equals("logico") && !tipoAux.equals(tipoEntrada)) {
                                    gestorErrores.escribirErrores(8, lineaFichero, lexemaAux);
                                    return null;
                                }
                            }
                            lexemaAux = lexema;
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
                    reiniciaReservadas();
                    genToken = true;
                    break;
                case 11:
                    token = new Token("IGUAL", null);
                    asignacion = true;
                    reiniciaReservadas();
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
                        gestorErrores.escribirErrores(2, lineaAux, new String());
                        return null;
                    } else {
                        leerArchivoTokens();
                    }
                    break;
                case 14:
                    token = new Token("AND", null);
                    reiniciaReservadas();
                    genToken = true;
                    break;
                case 15:
                    if (caracterLeido == ';') {
                        if (!marcaReservadas[palabrasReservadas.indexOf("switch")]) {
                            reiniciaReservadas();
                        } else {
                            reiniciaReservadas();
                            marcaReservadas[palabrasReservadas.indexOf("switch")] = true;
                        }
                        suma = false;
                        token = new Token("PUNTYCOM", null);
                        asignacion = false;
                    } else if (caracterLeido == ',') {
                        reiniciaReservadas();
                        token = new Token("COMA", null);
                    } else if (caracterLeido == '(') {
                        if (!marcaReservadas[palabrasReservadas.indexOf("switch")]) {
                            reiniciaReservadas();
                        } else {
                            rompe = true;
                            caso = true;
                        }
                        token = new Token("PARENTABRE", null);
                    } else if (caracterLeido == ')') {
                        if (numParamFuncion) {
                            EntradaTSAS aux = getEntradaPorPosicion(getPosIDTablasSimbolos(lexemaAuxFuncion));
                            if (contNumParamFuncion != aux.getF().getNumParam()) {
                                gestorErrores.escribirErrores(11, lineaFichero, lexemaAuxFuncion);
                                return null;
                            }
                            numParamFuncion = false;
                            contNumParamFuncion = 0;
                        }
                        token = new Token("PARENTCIERRA", null);
                    } else if (caracterLeido == '{') {
                        if (!marcaReservadas[palabrasReservadas.indexOf("switch")]) {
                            reiniciaReservadas();
                            rompe = false;
                            caso = false;
                        } else {
                            rompe = true;
                            caso = true;
                        }
                        token = new Token("LLAVEABRE", null);
                    } else if (caracterLeido == '}') {
                        if (!marcaReservadas[palabrasReservadas.indexOf("switch")]) {
                            contPilas = 1;
                        } else {
                            marcaReservadas[palabrasReservadas.indexOf("switch")] = false;
                            rompe = false;
                            caso = false;
                        }
                        token = new Token("LLAVECIERRA", null);
                    } else if (caracterLeido == ':') {
                        if (marcaReservadas[palabrasReservadas.indexOf("switch")]) {
                            reiniciaReservadas();
                            marcaReservadas[palabrasReservadas.indexOf("switch")] = true;
                        }
                        token = new Token("DOSPUNTOS", null);
                    } else if (caracterLeido == '+') {
                        suma = true;
                        token = new Token("SUMA", null);
                    }
                    genToken = true;
                    leerArchivoTokens();
                    break;
                case 16:
                    token = new Token("ASIGMULT", null);
                    asignacion = true;
                    reiniciaReservadas();
                    genToken = true;
                    break;
            }
        }

        return token;
    }

}
