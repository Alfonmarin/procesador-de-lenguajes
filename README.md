# 🧠 Procesador de Lenguajes – Analizador Léxico, Sintáctico y Semántico

## 🧩 Descripción general
Este proyecto consiste en la **implementación completa de un procesador de lenguajes**, desarrollado en **Java**, que integra tres fases principales:
- Un **analizador léxico**, basado en autómatas finitos deterministas (AFD).
- Un **analizador sintáctico**, construido a partir de una gramática **LL(1)**.
- Un **analizador semántico**, con comprobaciones de tipos, validación de asignaciones y gestión de la **tabla de símbolos**.

El sistema es capaz de **leer código fuente de un lenguaje diseñado ad hoc**, reconocer sus tokens, analizar su estructura y validar su semántica, generando automáticamente los archivos de salida correspondientes:
`Tokens.txt`, `Parse.txt`, `TS.txt` y `Errores.txt`.

---

## 🎯 Objetivos principales
- Implementar las tres fases del procesamiento de un lenguaje formal: **léxica, sintáctica y semántica**.  
- **Comprobar la validez estructural y semántica** de un programa fuente.  
- Desarrollar **gramáticas y autómatas deterministas** para el reconocimiento del lenguaje.  
- Construir una **tabla de símbolos** dinámica para gestionar identificadores, tipos y funciones.  
- Aplicar técnicas de **detección y manejo de errores** (léxicos, sintácticos y semánticos).  

---

## 🧱 Estructura del proyecto
| Módulo | Descripción destacada |
|---------|------------------------|
| **Analizador Léxico** | Definición de tokens, gramática y autómata finito determinista (AFD). |
| **Analizador Sintáctico** | Comprobación de gramática **LL(1)**, construcción de la tabla sintáctica y análisis descendente predictivo. |
| **Analizador Semántico** | Esquema de traducción y comprobaciones de tipos, retornos, y parámetros de funciones. |
| **Gestor de Errores** | Registro y clasificación de errores en archivo dedicado (`Errores.txt`). |
| **Tabla de Símbolos** | Estructura dinámica con información de identificadores, tipos, desplazamientos y atributos. |

---

## ⚙️ Tecnologías utilizadas
- **Lenguaje:** Java  
- **Paradigma:** Programación modular  
- **Conceptos clave:** AFD, gramáticas LL(1), parsing descendente, acciones semánticas  
- **Entorno:** Visual Studio Code (compatible con consola Java)  

---

## 🧮 Flujo de funcionamiento
```mermaid
graph TD
A[Fuente.txt] --> B[Analizador Léxico]
B --> C[Analizador Sintáctico]
C --> D[Analizador Semántico]
D --> E[TS.txt / Tokens.txt / Parse.txt / Errores.txt]
