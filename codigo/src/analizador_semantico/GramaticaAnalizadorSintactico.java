package analizador_semantico;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase para modelar la gramática del Analizador Sintáctico.
 */
public class GramaticaAnalizadorSintactico {
    /**
     * Tabla de la gramática LL1.
     * Las Keys cumplen este formato: "Fila Columna"
     * Los Values cumplen este formato: "NºRegla ConsecuenteDeLaRegla"
     */
    private Map<String, String> tablaSintactico;

    /**
     * Lista de los simbolos terminales de la gramatica.
     */
    private List<String> listaTerminales;

    /**
     * Constructor de la clase GramaticaAnalizadorSintactico.
     */
    public GramaticaAnalizadorSintactico() {

        // Inicializamos la lista con todos los terminales...
        listaTerminales = Arrays.asList("and", "parentabre", "parentcierra", "asigmult", "suma", "coma", "dospuntos",
                "puntycom",
                "igual", "iguigu", "boolean", "break", "cadena", "case", "entero", "eof", "function", "id", "if",
                "input", "int", "output", "return", "string", "switch",
                "var", "llaveabre", "llavecierra", "void");

        // ... y completamos la tablaSintactico de la gramática metiendo todas las
        // reglas
        // generadas.
        tablaSintactico = new HashMap<String, String>();

        // Regla 1. E -> R 1
        tablaSintactico.put("E id", "1 R 1");
        tablaSintactico.put("E entero", "1 R 1");
        tablaSintactico.put("E parentabre", "1 R 1");
        tablaSintactico.put("E cadena", "1 R 1");

        // Regla 2. 1 -> && R 1
        tablaSintactico.put("1 and", "2 and R 1");

        // Regla 3. 1 -> lambda
        tablaSintactico.put("1 puntycom", "3 lambda");
        tablaSintactico.put("1 parentcierra", "3 lambda");
        tablaSintactico.put("1 coma", "3 lambda");

        // Regla 4. R -> U 2
        tablaSintactico.put("R id", "4 U 2");
        tablaSintactico.put("R parentabre", "4 U 2");
        tablaSintactico.put("R entero", "4 U 2");
        tablaSintactico.put("R cadena", "4 U 2");
        // Regla 5. 2 -> == U 2
        tablaSintactico.put("2 iguigu", "5 iguigu U 2");

        // Regla 6. 2 -> lambda
        tablaSintactico.put("2 puntycom", "6 lambda");
        tablaSintactico.put("2 parentcierra", "6 lambda");
        tablaSintactico.put("2 coma", "6 lambda");
        tablaSintactico.put("2 and", "6 lambda");

        // Regla 7. U -> V 3
        tablaSintactico.put("U id", "7 V 3");
        tablaSintactico.put("U parentabre", "7 V 3");
        tablaSintactico.put("U entero", "7 V 3");
        tablaSintactico.put("U cadena", "7 V 3");

        // Regla 8. 3 -> + V 3
        tablaSintactico.put("3 suma", "8 suma V 3");

        // Regla 9. 3 -> lambda
        tablaSintactico.put("3 puntycom", "9 lambda");
        tablaSintactico.put("3 parentcierra", "9 lambda");
        tablaSintactico.put("3 coma", "9 lambda");
        tablaSintactico.put("3 and", "9 lambda");
        tablaSintactico.put("3 iguigu", "9 lambda");

        // Regla 10. V -> id O
        tablaSintactico.put("V id", "10 id O");

        // Regla 11. V -> ( E )
        tablaSintactico.put("V parentabre", "11 parentabre E parentcierra");

        // Regla 12. V -> entero
        tablaSintactico.put("V entero", "12 entero");

        // Regla 13. V -> cadena
        tablaSintactico.put("V cadena", "13 cadena");

        // Regla 14. O -> lambda
        tablaSintactico.put("O and", "14 lambda");
        tablaSintactico.put("O parentcierra", "14 lambda");
        tablaSintactico.put("O suma", "14 lambda");
        tablaSintactico.put("O coma", "14 lambda");
        tablaSintactico.put("O puntycom", "14 lambda");
        tablaSintactico.put("O iguigu", "14 lambda");

        // Regla 15. O -> ( L )
        tablaSintactico.put("O parentabre", "15 parentabre L parentcierra");

        // Regla 16. S -> id D ;
        tablaSintactico.put("S id", "16 id D puntycom");

        // Regla 17. S -> output E ;
        tablaSintactico.put("S output", "17 output E puntycom");

        // Regla 18. S -> input id ;
        tablaSintactico.put("S input", "18 input id puntycom");

        // Regla 19. S -> return X ;
        tablaSintactico.put("S return", "19 return X puntycom");

        // Regla 20. D -> = E
        tablaSintactico.put("D igual", "20 igual E");

        // Regla 21. D -> *= E
        tablaSintactico.put("D asigmult", "21 asigmult E");

        // Regla 22. D -> ( L )
        tablaSintactico.put("D parentabre", "22 parentabre L parentcierra");

        // Regla 23. L -> E Q
        tablaSintactico.put("L id", "23 E Q");
        tablaSintactico.put("L parentabre", "23 E Q");
        tablaSintactico.put("L entero", "23 E Q");
        tablaSintactico.put("L cadena", "23 E Q");

        // Regla 24. L -> lambda
        tablaSintactico.put("L parentcierra", "24 lambda");

        // Regla 25. Q -> , E Q
        tablaSintactico.put("Q coma", "25 coma E Q");

        // Regla 26. Q -> lambda
        tablaSintactico.put("Q parentcierra", "26 lambda");

        // Regla 27. X -> E
        tablaSintactico.put("X id", "27 E");
        tablaSintactico.put("X parentabre", "27 E");
        tablaSintactico.put("X entero", "27 E");
        tablaSintactico.put("X cadena", "27 E");

        // Regla 28. X -> lambda
        tablaSintactico.put("X puntycom", "28 lambda");

        // Regla 29. B -> if ( E ) S
        tablaSintactico.put("B if", "29 if parentabre E parentcierra S");

        // Regla 30. B -> var T id ;
        tablaSintactico.put("B var", "30 var T id puntycom");

        // Regla 31. B -> S
        tablaSintactico.put("B id", "31 S");
        tablaSintactico.put("B input", "31 S");
        tablaSintactico.put("B output", "31 S");
        tablaSintactico.put("B return", "31 S");

        // Regla 32. B -> switch ( E ) { Y }
        tablaSintactico.put("B switch", "32 switch parentabre E parentcierra llaveabre Y llavecierra");
        // Regla 33. Y -> case entero : C M
        tablaSintactico.put("Y case", "33 case entero dospuntos C M");

        // Regla 34. M -> break ;
        tablaSintactico.put("M break", "34 break puntycom N");
        // Regla 35. M -> N
        tablaSintactico.put("M llavecierra", "35 N");
        tablaSintactico.put("M case", "35 N");

        // Regla 36. N -> Y
        tablaSintactico.put("N case", "36 Y");

        // Regla 37. N -> lambda
        tablaSintactico.put("N llavecierra", "37 lambda");

        // Regla 38. T -> int
        tablaSintactico.put("T int", "38 int");

        // Regla 39. T -> boolean
        tablaSintactico.put("T boolean", "39 boolean");

        // Regla 40. T -> string
        tablaSintactico.put("T string", "40 string");

        // Regla 41. F -> function H id ( A ) { C }
        tablaSintactico.put("F function", "41 function H id parentabre A parentcierra llaveabre C llavecierra");

        // Regla 42. H -> T
        tablaSintactico.put("H int", "42 T");
        tablaSintactico.put("H boolean", "42 T");
        tablaSintactico.put("H string", "42 T");

        // Regla 43. H -> void
        tablaSintactico.put("H void", "43 void");

        // Regla 44. A -> T id K
        tablaSintactico.put("A int", "44 T id K");
        tablaSintactico.put("A boolean", "44 T id K");
        tablaSintactico.put("A string", "44 T id K");

        // Regla 45. A -> void
        tablaSintactico.put("A void", "45 void");

        // Regla 46. K -> , T id K
        tablaSintactico.put("K coma", "46 coma T id K");

        // Regla 47. K -> lambda
        tablaSintactico.put("K parentcierra", "47 lambda");

        // Regla 48. C -> B C
        tablaSintactico.put("C if", "48 B C");
        tablaSintactico.put("C var", "48 B C");
        tablaSintactico.put("C id", "48 B C");
        tablaSintactico.put("C input", "48 B C");
        tablaSintactico.put("C output", "48 B C");
        tablaSintactico.put("C return", "48 B C");
        tablaSintactico.put("C switch", "48 B C");

        // Regla 49. C -> lambda
        tablaSintactico.put("C llavecierra", "49 lambda");
        tablaSintactico.put("C case", "49 lambda");
        tablaSintactico.put("C break", "49 lambda");

        // Regla 50. P -> BP
        tablaSintactico.put("P if", "50 B P");
        tablaSintactico.put("P var", "50 B P");
        tablaSintactico.put("P id", "50 B P");
        tablaSintactico.put("P input", "50 B P");
        tablaSintactico.put("P output", "50 B P");
        tablaSintactico.put("P return", "50 B P");
        tablaSintactico.put("P switch", "50 B P");

        // Regña 51. P -> FP
        tablaSintactico.put("P function", "51 F P");

        // Regla 52. P -> eof
        tablaSintactico.put("P eof", "52 eof");

        // Regla 53. P -> lambda
        tablaSintactico.put("P $", "53 lambda");

    }

    /**
     * Devuelve la tabla de la gramática LL1.
     * 
     * @return la tabla de la gramática LL1.
     */
    public Map<String, String> gettablaSintactico() {
        return tablaSintactico;
    }

    /**
     * Establece la tabla de la gramática LL1.
     * 
     * @param tablaSintacticoSintactico la nueva tabla de la gramática
     *                                  LL1.
     */
    public void settablaSintactico(Map<String, String> tablaSintactico) {
        this.tablaSintactico = tablaSintactico;
    }

    /**
     * Devuelve la lista de los simbolos terminales de la gramatica.
     * 
     * @return la lista de los simbolos terminales de la gramatica.
     */
    public List<String> getListaTerminales() {
        return listaTerminales;
    }

    /**
     * Establece la lista de los simbolos terminales de la gramatica.
     * 
     * @param listaTerminales la nueva lista de los simbolos terminales de la
     *                        gramatica.
     */
    public void setListaTerminales(List<String> listaTerminales) {
        this.listaTerminales = listaTerminales;
    }

}
