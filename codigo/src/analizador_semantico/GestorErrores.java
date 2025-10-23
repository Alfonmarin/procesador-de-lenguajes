package analizador_semantico;

/**
 * Clase para modelar el Gestor de Errores del compilador.
 */
public class GestorErrores {

    /**
     * El mensaje con los errores correspondientes.
     */
    StringBuilder mensajeError;

    /**
     * Constructor de la clase GestorErrores.
     */
    public GestorErrores() {
        mensajeError = new StringBuilder();
    }

    /**
     * Devuelve el mensaje con los errores correspondientes.
     * 
     * @return el mensaje con los errores correspondientes.
     */
    public StringBuilder getMensajeError() {
        return mensajeError;
    }

    /**
     * Establece el mensaje con los errores correspondientes.
     * 
     * @param mensajeError el nuevo mensaje con los errores correspondientes.
     */
    public void setMensajeError(StringBuilder mensajeError) {
        this.mensajeError = mensajeError;
    }

    /**
     * Escribimos los mensajes de error según se vayan detectando en el programa.
     * 
     * @param numError          el número al que corresponde el error.
     * @param numLinea          la línea donde se detectó el error.
     * @param lexema_o_caracter el lexema o carácter incorrecto.
     */
    public void escribirErrores(int numError, int numLinea, String lexema_o_caracter) {
        switch (numError) {
            case 0:
                mensajeError.append(
                        "Error Léxico en línea " + numLinea + ": Carácter '" + lexema_o_caracter
                                + "' no soportado o desconocido por el lenguaje.\n");
                break;
            case 1:
                mensajeError.append(
                        "Error Léxico en línea " + numLinea + ": Variable '" + lexema_o_caracter + "' mal escrita.\n");
                break;
            case 2:
                mensajeError.append("Error Léxico en línea " + numLinea + ": Comentario abierto y no cerrado.\n");
                break;
            case 3:
                mensajeError.append("Error Léxico en línea " + numLinea + ": Cadena " + lexema_o_caracter
                        + " demasiado larga. Debe tener como máximo 64 caractéres entre las comillas simples.\n");
                break;
            case 4:
                mensajeError.append("Error Léxico en línea " + numLinea + ": Cadena abierta y no cerrada.\n");
                break;
            case 5:
                mensajeError.append("Error Léxico en línea " + numLinea + ": " + lexema_o_caracter
                        + " ∈/ [0,32767] (no pertenece al rango).\n");
                break;
            case 6:
                mensajeError.append("Error Semántico en línea " + numLinea + ": Función '" + lexema_o_caracter
                        + "' no declarada previamente.\n");
                break;
            case 7:
                mensajeError
                        .append("Error Semántico en línea " + numLinea + ": La entrada con lexema '" + lexema_o_caracter
                                + "' no es de tipo entero y debe serlo, ya que se está evaluando en un switch.\n");
                break;
            case 8:
                mensajeError
                        .append("Error Semántico en línea " + numLinea
                                + ": Tipo incompatible para la asignación de la variable '" + lexema_o_caracter
                                + "'.\n");
                break;
            case 9:
                mensajeError
                        .append("Error Semántico en línea " + numLinea
                                + ": Se está haciendo una suma de dos variables, de las cuales al menos una (o incluso las dos) no son de tipo entero.\n");
                break;
            case 10:
                mensajeError
                        .append("Error Semántico en línea " + numLinea
                                + ": La palabra reservada '" + lexema_o_caracter
                                + "' no debería de haberse leído, ya que no está en su sitio esperado.\n");
                break;
            case 11:
                mensajeError
                        .append("Error Semántico en línea " + numLinea
                                + ": Se está llamando a la función '" + lexema_o_caracter
                                + "' con un número de parámetros incorrecto.\n");
                break;
            case 12:
                mensajeError
                        .append("Error Semántico en línea " + numLinea
                                + ": Se está llamando a la función '" + lexema_o_caracter
                                + "' con un parámetro de tipo incorrecto.\n");
                break;
            case 13:
                mensajeError
                        .append("Error Semántico en línea " + numLinea
                                + ": Se ha declarado previamente el identificador '" + lexema_o_caracter
                                + "' en el mismo ámbito.\n");
                break;
            case 14:
                mensajeError
                        .append("Error Semántico en línea " + numLinea
                                + ": El return de la función '" + lexema_o_caracter
                                + "' no coincide con el que fue declarada.\n");
                break;
            case 15:
                mensajeError.append("Error Sintáctico en línea " + numLinea
                        + ": Falta de punto y coma al final de la sentencia.\n");
                break;

            case 16:
                mensajeError.append("Error Sintáctico en línea " + numLinea
                        + ": La instrucción 'input' no puede llevar una expresión cualquiera. Solo se permite un identificador válido.\n");
                break;

            case 17:
                mensajeError.append("Error Sintáctico en línea " + numLinea
                        + ": La instrucción 'function' debe especificar un tipo de retorno válido (int, boolean, string o void).\n");
                break;

            case 18:
                mensajeError.append("Error Sintáctico en línea " + numLinea
                        + ": Paréntesis de la condición de la sentencia incorrectos\n");
                break;

            case 19:
                mensajeError.append("Error Sintáctico en línea " + numLinea
                        + ": Falta la condición en la sentencia de control de flujo.\n");
                break;

        }
    }
}