package analizador_semantico;

import analizador_lexico.Token;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

public class MainSemantico {

    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);
        String codigoElegido = "";

        // === 1. Elegir código de prueba (C1...C10) ===
        while (true) {
            System.out.print("👉 Introduce el código de prueba a ejecutar (C1 - C10): ");
            codigoElegido = sc.nextLine().trim().toUpperCase();

            if (codigoElegido.matches("C(10|[1-9])")) {
                break;
            } else {
                System.out.println("⚠️  Valor no válido. Debes escribir algo como C1, C2, ..., C10.");
            }
        }

        File codigoFuente = new File(
                "Códigos de prueba\\"+codigoElegido+"\\Código.txt");

        File archivoTabla = new File(
                "Códigos de prueba\\"+codigoElegido+"\\TS.txt");

        File archivoTokens = new File(
                "Códigos de prueba\\"+codigoElegido+"\\Tokens.txt");

        File archivoErrores = new File(
                "Códigos de prueba\\"+codigoElegido+"\\Errores.txt");

        File archivoParse = new File(
                "Códigos de prueba\\"+codigoElegido+"\\Parse.txt");

        if (archivoTokens.exists() && archivoTabla.exists() && archivoErrores.exists() && archivoParse.exists()) {
            archivoTokens.delete();
            archivoTabla.delete();
            archivoErrores.delete();
            archivoParse.delete();
        }

        AnalizadorLexicoAS AL = new AnalizadorLexicoAS(codigoFuente);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoTokens))) {
            Token token;
            do {
                try {
                    token = AL.leerToken();
                    System.out.println(token);
                    if (token == null) {
                        break;
                    }
                    writer.write(token.toString());
                    writer.newLine();
                } catch (IOException e) {
                    System.err.println("Error al leer token: " + e.getMessage());
                    break;
                }
            } while (!token.getCodigoToken().equals("EOF"));
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de tokens: " + e.getMessage());
        }

        Stack<TablaDeSimbolosAS> pilaTablas = AL.getPilaTablaSimbolos();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoTabla))) {
            for (TablaDeSimbolosAS tabla : pilaTablas) {
                writer.write(tabla.toString());
                tabla.getTabla().clear();
            }
            pilaTablas.clear();
        } catch (IOException e) {
            System.err.println("Error al escribir las tablas de símbolos en " + archivoTabla + ": " + e.getMessage());
        }

        for (TablaDeSimbolosAS tabla : pilaTablas) {
            try {
                tabla.destruyeTablaDeSimbolos();
            } catch (Exception e) {
                System.err.println("Error al destruir tabla de símbolos: " + e.getMessage());
            }
        }

        AnalizadorSintactico AS;
        try {
            AS = new AnalizadorSintactico(new File(archivoTokens.getAbsolutePath()));
        } catch (IOException e) {
            System.err.println("Error al inicializar el analizador sintáctico: " + e.getMessage());
            return;
        }

        try (BufferedWriter escrituraParse = new BufferedWriter(new FileWriter(archivoParse))) {
            try {
                String resultadoParse = AS.parseSintactico();
                escrituraParse.write(resultadoParse);
            } catch (IOException e) {
                System.err.println("Error durante el análisis sintáctico: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo de parse: " + e.getMessage());
        }

        String mensajeErrorAL = AL.getGestorErrores().getMensajeError().toString();
        String mensajeErrorAS = AS.getGestorErrores().getMensajeError().toString();

        if (!mensajeErrorAL.isBlank()) {
            try (BufferedWriter writerErrores = new BufferedWriter(new FileWriter(archivoErrores, true))) {
                writerErrores.write(mensajeErrorAL);
            } catch (IOException e) {
                System.err.println("Error al escribir errores del analizador léxico: " + e.getMessage());
            }
        }

        if (!mensajeErrorAS.isBlank()) {
            try (BufferedWriter writerErrores = new BufferedWriter(new FileWriter(archivoErrores, true))) {
                writerErrores.write(mensajeErrorAS);
            } catch (IOException e) {
                System.err.println("Error al escribir errores del analizador sintáctico: " + e.getMessage());
            }
        }
    }
}
