/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.gersonmatta.system;

/**
 *
 * @author Gerson Matta
 * 
 */

import java.util.*;

public class SopaDeLetrasApp {

    /**
     * @param args the command line arguments
     */
    
    // Scanner global y almacenamiento de historial
    static Scanner sc = new Scanner(System.in);
    static List<GameRecord> gameHistory = new ArrayList<>();
    
    public static void main(String[] args) {
        // TODO code application logic here
        new SopaDeLetrasApp().start();
    }
    
    // Menú principal
    public void start() {
        int opcion;
        do {
            System.out.println("\n--- Menú Principal ---");
            System.out.println("1. Nueva Partida");
            System.out.println("2. Historial de Partidas");
            System.out.println("3. Mostrar Puntuaciones Más Altas");
            System.out.println("4. Mostrar Información de Estudiante");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();
            sc.nextLine(); // Consumir salto de línea
            switch (opcion) {
                case 1:
                    nuevaPartida();
                    break;
                case 2:
                    mostrarHistorial();
                    break;
                case 3:
                    mostrarTopScores();
                    break;
                case 4:
                    mostrarInfoEstudiante();
                    break;
                case 5:
                    System.out.println("Saliendo de la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 5);
    }

    // Nueva Partida
    public void nuevaPartida() {
        System.out.print("Ingrese el nombre del jugador: ");
        String jugador = sc.nextLine();
        // Seleccionar sección (A a G)
        SectionConfig config = seleccionarSeccion();
        // Banco de palabras (inicialmente vacío)
        List<String> bancoPalabras = new ArrayList<>();
        int opcion;
        do {
            System.out.println("\n--- Menú Nueva Partida ---");
            System.out.println("1. Menú Palabras");
            System.out.println("2. Jugar");
            System.out.println("3. Terminar Partida y volver al menú principal");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();
            sc.nextLine();
            switch (opcion) {
                case 1:
                    bancoPalabras = menuPalabras(bancoPalabras, config);
                    break;
                case 2:
                    if (bancoPalabras.isEmpty()) {
                        System.out.println("Error: No se han ingresado palabras.");
                    } else {
                        playGame(jugador, bancoPalabras, config);
                        // Al finalizar el juego, se retorna al menú principal.
                        return;
                    }
                    break;
                case 3:
                    System.out.println("Terminando partida...");
                    return;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (true);
    }

    // Submenú de gestión de palabras
    public List<String> menuPalabras(List<String> banco, SectionConfig config) {
        int opcion;
        do {
            System.out.println("\n--- Menú Palabras ---");
            System.out.println("1. Insertar palabras");
            System.out.println("2. Modificar palabra");
            System.out.println("3. Eliminar palabra");
            System.out.println("4. Mostrar palabras");
            System.out.println("5. Salir (volver a menú Nueva Partida)");
            System.out.print("Seleccione una opción: ");
            opcion = sc.nextInt();
            sc.nextLine();
            switch (opcion) {
                case 1:
                    System.out.print("Ingrese el número de palabras a insertar: ");
                    int num = sc.nextInt();
                    sc.nextLine();
                    for (int i = 0; i < num; i++) {
                        boolean valido = false;
                        String palabra;
                        do {
                            System.out.print("Ingrese palabra " + (i + 1) + ": ");
                            palabra = sc.nextLine().toUpperCase();
                            if (palabra.length() < config.minLength || palabra.length() > config.maxLength) {
                                System.out.println("Error: La palabra debe tener entre " + config.minLength + " y " + config.maxLength + " caracteres.");
                            } else {
                                valido = true;
                            }
                        } while (!valido);
                        banco.add(palabra);
                    }
                    break;
                case 2:
                    System.out.print("Ingrese la palabra a modificar: ");
                    String antigua = sc.nextLine().toUpperCase();
                    if (banco.contains(antigua)) {
                        boolean valido = false;
                        String nueva;
                        do {
                            System.out.print("Ingrese la nueva palabra: ");
                            nueva = sc.nextLine().toUpperCase();
                            if (nueva.length() < config.minLength || nueva.length() > config.maxLength) {
                                System.out.println("Error: La palabra debe tener entre " + config.minLength + " y " + config.maxLength + " caracteres.");
                            } else {
                                valido = true;
                            }
                        } while (!valido);
                        int index = banco.indexOf(antigua);
                        banco.set(index, nueva);
                        System.out.println("Palabra modificada exitosamente.");
                    } else {
                        System.out.println("Error: La palabra a modificar no se encuentra.");
                    }
                    break;
                case 3:
                    System.out.print("Ingrese la palabra a eliminar: ");
                    String eliminar = sc.nextLine().toUpperCase();
                    if (banco.contains(eliminar)) {
                        banco.remove(eliminar);
                        System.out.println("Palabra eliminada.");
                    } else {
                        System.out.println("Error: Palabra no encontrada.");
                    }
                    break;
                case 4:
                    System.out.println("Palabras en el banco:");
                    for (String p : banco) {
                        System.out.println(p);
                    }
                    break;
                case 5:
                    return banco;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (true);
    }

    // Método que ejecuta la partida (juego)
    public void playGame(String jugador, List<String> banco, SectionConfig config) {
        int boardSize = config.boardSize;
        char[][] board = new char[boardSize][boardSize];
        // Inicializar tablero con espacios
        for (int i = 0; i < boardSize; i++) {
            Arrays.fill(board[i], ' ');
        }

        // Lista para almacenar la posición de cada palabra
        List<WordPlacement> placements = new ArrayList<>();
        Random rand = new Random();
        // Colocar cada palabra en el tablero
        for (String palabra : banco) {
            WordPlacement wp = placeWord(board, palabra, boardSize, rand);
            if (wp != null) {
                placements.add(wp);
            } else {
                System.out.println("No se pudo colocar la palabra: " + palabra);
            }
        }
        // Rellenar celdas vacías con letras aleatorias
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = (char) ('A' + rand.nextInt(26));
                }
            }
        }

        // Iniciar partida
        int score = 25;
        int errores = 0;
        int palabrasEncontradas = 0;
        int totalPalabras = placements.size();

        while (palabrasEncontradas < totalPalabras && errores < 4) {
            printBoard(board);
            System.out.println("Palabras encontradas: " + palabrasEncontradas + " / " + totalPalabras);
            System.out.println("Errores: " + errores + " (máximo 4)");
            System.out.println("Puntaje: " + score);
            System.out.print("Ingrese una palabra: ");
            String intento = sc.nextLine().toUpperCase();
            boolean encontrada = false;
            for (WordPlacement wp : placements) {
                if (!wp.found && wp.word.equalsIgnoreCase(intento)) {
                    wp.found = true;
                    encontrada = true;
                    // Reemplazar letras de la palabra por '#'
                    int r = wp.row;
                    int c = wp.col;
                    for (int k = 0; k < wp.word.length(); k++) {
                        board[r][c] = '#';
                        if (wp.horizontal) {
                            c++;
                        } else {
                            r++;
                        }
                    }
                    score += wp.word.length();
                    palabrasEncontradas++;
                    System.out.println("¡Palabra encontrada! +" + wp.word.length() + " puntos.");
                    break;
                }
            }
            if (!encontrada) {
                errores++;
                score -= 5;
                System.out.println("Palabra no encontrada. -5 puntos.");
            }
        }
        System.out.println("Fin de la partida. Puntaje final: " + score);
        // Guardar registro de la partida
        gameHistory.add(new GameRecord(jugador, score, errores, palabrasEncontradas));
    }

    // Método para colocar una palabra en el tablero de forma aleatoria (horizontal o vertical)
    public WordPlacement placeWord(char[][] board, String word, int boardSize, Random rand) {
        int attempts = 100;
        word = word.toUpperCase();
        while (attempts-- > 0) {
            boolean horizontal = rand.nextBoolean();
            int maxRow = boardSize;
            int maxCol = boardSize;
            if (horizontal) {
                maxCol = boardSize - word.length();
            } else {
                maxRow = boardSize - word.length();
            }
            int row = rand.nextInt(maxRow);
            int col = rand.nextInt(maxCol);
            // Verificar que la palabra pueda colocarse (sin conflicto)
            boolean canPlace = true;
            int r = row, c = col;
            for (int i = 0; i < word.length(); i++) {
                if (board[r][c] != ' ' && board[r][c] != word.charAt(i)) {
                    canPlace = false;
                    break;
                }
                if (horizontal) {
                    c++;
                } else {
                    r++;
                }
            }
            if (canPlace) {
                // Colocar la palabra en el tablero
                r = row;
                c = col;
                for (int i = 0; i < word.length(); i++) {
                    board[r][c] = word.charAt(i);
                    if (horizontal) {
                        c++;
                    } else {
                        r++;
                    }
                }
                return new WordPlacement(word, row, col, horizontal);
            }
        }
        return null; // Si no se pudo colocar la palabra
    }

    // Método para imprimir el tablero en consola
    public void printBoard(char[][] board) {
        System.out.println("\n--- Tablero ---");
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Mostrar historial de partidas
    public void mostrarHistorial() {
        System.out.println("\n--- Historial de Partidas ---");
        if (gameHistory.isEmpty()) {
            System.out.println("No hay partidas registradas.");
        } else {
            for (GameRecord record : gameHistory) {
                System.out.println("Jugador: " + record.jugador + " | Puntaje: " + record.score +
                        " | Errores: " + record.errores + " | Palabras encontradas: " + record.palabrasEncontradas);
            }
        }
    }

    // Mostrar top 3 puntuaciones
    public void mostrarTopScores() {
        System.out.println("\n--- Top 3 Puntuaciones ---");
        if (gameHistory.isEmpty()) {
            System.out.println("No hay partidas registradas.");
            return;
        }
        List<GameRecord> sorted = new ArrayList<>(gameHistory);
        sorted.sort((a, b) -> b.score - a.score);
        int limit = Math.min(3, sorted.size());
        for (int i = 0; i < limit; i++) {
            GameRecord record = sorted.get(i);
            System.out.println((i + 1) + ". " + record.jugador + " - " + record.score + " puntos");
        }
    }

    // Mostrar información del estudiante
    public void mostrarInfoEstudiante() {
        System.out.println("\n--- Información del Estudiante ---");
        System.out.println("Nombre: Juan Pérez");
        System.out.println("Carnet: 2025-1234");
        System.out.println("Sección: A");
    }

    // Seleccionar sección y retornar configuración (según Tabla 1)
    public SectionConfig seleccionarSeccion() {
        System.out.println("\nSeleccione la sección (A, B, C, D, E, F, G): ");
        char seccion = sc.nextLine().toUpperCase().charAt(0);
        switch (seccion) {
            case 'A':
                return new SectionConfig(17, 4, 10);
            case 'B':
                return new SectionConfig(15, 3, 8);
            case 'C':
                return new SectionConfig(20, 5, 12);
            case 'D':
                return new SectionConfig(25, 6, 15);
            case 'E':
                return new SectionConfig(14, 5, 10);
            case 'F':
                return new SectionConfig(18, 4, 12);
            case 'G':
                return new SectionConfig(16, 6, 10);
            default:
                System.out.println("Sección no válida. Se usará la sección A por defecto.");
                return new SectionConfig(17, 4, 10);
        }
    }

    // Clase para la configuración según la sección
    class SectionConfig {
        int boardSize;
        int minLength;
        int maxLength;

        public SectionConfig(int boardSize, int minLength, int maxLength) {
            this.boardSize = boardSize;
            this.minLength = minLength;
            this.maxLength = maxLength;
        }
    }

    // Clase para almacenar la posición y orientación de una palabra en el tablero
    class WordPlacement {
        String word;
        int row;
        int col;
        boolean horizontal;
        boolean found;

        public WordPlacement(String word, int row, int col, boolean horizontal) {
            this.word = word;
            this.row = row;
            this.col = col;
            this.horizontal = horizontal;
            this.found = false;
        }
    }

    // Clase para almacenar el registro de una partida
    class GameRecord {
        String jugador;
        int score;
        int errores;
        int palabrasEncontradas;

        public GameRecord(String jugador, int score, int errores, int palabrasEncontradas) {
            this.jugador = jugador;
            this.score = score;
            this.errores = errores;
            this.palabrasEncontradas = palabrasEncontradas;
        }
    }
    
}
