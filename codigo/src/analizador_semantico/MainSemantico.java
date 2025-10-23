package analizador_semantico;

import analizador_lexico.Token;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

        Path basePath = Paths.get(System.getProperty("user.dir")); // raíz del proyecto
        Path carpetaCodigo = basePath.resolve(Paths.get("codigo", "Códigos de prueba", codigoElegido));


        // Mostrar ruta final para depuración
        System.out.println("📂 Buscando archivos en: " + carpetaCodigo.toAbsolutePath());

        // Crear objetos File usando rutas absolutas seguras (convertir Path a File)
        File codigoFuente = carpetaCodigo.resolve("Código.txt").toFile();
        File archivoTabla = carpetaCodigo.resolve("TS.txt").toFile();
        File archivoTokens = carpetaCodigo.resolve("Tokens.txt").toFile();
        File archivoErrores = carpetaCodigo.resolve("Errores.txt").toFile();
        File archivoParse = carpetaCodigo.resolve("Parse.txt").toFile();

        // === 3. Comprobar existencia del archivo de entrada ===
        if (!codigoFuente.exists()) {
            System.err.println("❌ No se encontró el archivo: " + codigoFuente.getAbsolutePath());
            System.err.println("Asegúrate de que la carpeta '" + codigoElegido + "' contiene el archivo 'Código.txt'.");
            return;
        }

        // === 4. Limpiar archivos antiguos ===
        for (File f : new File[]{archivoTokens, archivoTabla, archivoErrores, archivoParse}) {
            if (f.exists()) f.delete();
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
