package obligatorio;

import java.net.SocketTimeoutException;
import java.text.Format;
import java.util.*;

import javax.swing.plaf.ColorUIResource;

import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * @author Luciano Umpierrez
 */
public class Tablero {

    Gato[][] tablero;
    private char caracter;
    private String color;
    private static final String ROJO = "\u001B[31m";
    private static final int ARRIBA = 1;
    private static final int ABAJO = 2;
    private static final int DERECHA = 3;
    private static final int IZQUIERDA = 4;
    private static final int DIAGONAL_SUP_DER = 5;
    private static final int DIAGONAL_SUP_IZQ = 6;
    private static final int DIAGONAL_INF_IZQ = 7;
    private static final int DIAGONAL_INF_DER = 8;

    //getters y setters
    
    public Gato[][] getTablero() {
        return tablero;
    }

    public void setTablero() {
        tablero = new Gato[6][6];
        for (int fila = 0; fila < tablero.length; fila++) {
            for (int columna = 0; columna < tablero[fila].length; columna++) {
                tablero[fila][columna] = null;
            }
        }
    }

    public char getCaracter() {
        return caracter;
    }

    public void setCaracter(char caracter) {
        this.caracter = caracter;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    //métodos
    
    public Tablero(char unCaracter, String unColor) {
        this.setCaracter(unCaracter);
        this.setColor(unColor);
        this.setTablero();
    }

    /**
     * 
     * @param fila
     * @param columna
     * @return true si el tablero esta vacío en esa posición, falso de lo
     * contrario
     */
    public boolean esVacio(int fila, int columna) {
        boolean estaVacio = getTablero()[fila][columna] == null;
        return estaVacio;
    }

    /*
    Impresión del tablero
     */
    public void imprimir() {
        char letra = 'A';
        int largo = getTablero().length;
        //caracter que el usuario eligió para decorarlo
        char caracter = getCaracter();
        //color que eligió
        String color = getColor();
        // Imprime los numeros de las columnas
        System.out.print(color + "  ");
        for (int i = 1; i <= largo; i++) {
            System.out.print("    " + i + "   ");
        }
        System.out.println();
        System.out.println(color + "  " + caracter + " - - - " + caracter + " - - - " + caracter + " - - - " + caracter
                + " - - - " + caracter + " - - - " + caracter + " - - - " + caracter);
        for (int fila = 0; fila < largo; fila++) {
            // Imprime los bordes del tablero y las letras de las filas
            System.out.println(color
                    + "  | " + caracter + " " + caracter + " " + caracter + " | " + caracter + " " + caracter + " "
                    + caracter + " | " + caracter + " " + caracter + " " + caracter + " | " + caracter + " "
                    + caracter + " " + caracter + " | " + caracter + " " + caracter + " " + caracter
                    + " | " + caracter + " " + caracter + " " + caracter + " |");
            System.out.print((char) (letra + fila) + " |");
            for (int columna = 0; columna < largo; columna++) {
                String gato = " ";
                if (tablero[fila][columna] != null) {
                    gato = tablero[fila][columna].toString();
                }
                System.out.printf(color + " " + caracter + " " + gato + " " + color + caracter + " |");
            }
            System.out.println();
            System.out.println(color
                    + "  | " + caracter + " " + caracter + " " + caracter + " | " + caracter + " " + caracter + " "
                    + caracter + " | " + caracter + " " + caracter + " " + caracter + " | " + caracter + " "
                    + caracter + " " + caracter + " | " + caracter + " " + caracter + " " + caracter
                    + " | " + caracter + " " + caracter + " " + caracter + " |");
            System.out.println(
                    color + "  " + caracter + " - - - " + caracter + " - - - " + caracter + " - - - " + caracter
                    + " - - - " + caracter + " - - - " + caracter + " - - - " + caracter);
        }
    }

    /*
    Funcion que acepta la posicion ingresada por le usuario ya porcesada en Juego
    e ingresa el gato.
     */
    public int[] insertar(int fila, int columna, String color) {
        getTablero()[fila][columna] = new Gato(color);
        int[] aCaja = this.habraSaltos(fila, columna);
        return aCaja;
    }

    /*
    Si hay un gato adyacente al que se ingreso, se fijara si este adyacente 
    esta junto a otro gato. Si no lo esta hara un salto. Si esta en el borde 
    del tablero se volera a la caja.
     */
    public int[] habraSaltos(int fila, int columna) {
        int[] aLaCaja = new int[2];
        //arriba
        if (getTablero()[fila][columna] != null) {
            Gato[][] elTablero = getTablero();
            String rojo = "\u001B[31m";
            if (fila != 0 && getTablero()[fila - 1][columna] != null) {
                String color = elTablero[fila - 1][columna].getColor();
                if ((fila - 1) != 0 && elTablero[fila - 2][columna] == null) {
                    this.saltitos(fila - 2, columna, color);
                    elTablero[fila - 1][columna] = null;

                } else if (fila - 1 == 0) {
                    //volverACaja
                    elTablero[fila - 1][columna] = null;
                    if (color.equals(rojo)) {
                        aLaCaja[0]++;
                        sonido();
                    } else {
                        aLaCaja[1]++;
                        sonido();
                    }
                }
            }
            //abajo
            if (fila != 5 && elTablero[fila + 1][columna] != null) {
                String color = elTablero[fila + 1][columna].getColor();
                if ((fila + 1) != 5 && elTablero[fila + 2][columna] == null) {
                    //String color = elTablero[fila+1][columna].getColor();
                    this.saltitos(fila + 2, columna, color);
                    elTablero[fila + 1][columna] = null;
                } else if (fila + 1 == 5) {

                    //volverACaja
                    elTablero[fila + 1][columna] = null;
                    //aLaCaja++;
                    sonido();
                    if (color.equals(rojo)) {
                        aLaCaja[0]++;
                    } else {
                        aLaCaja[1]++;
                    }
                }
            }
            //derecha
            if (columna != 5 && elTablero[fila][columna + 1] != null) {
                String color = elTablero[fila][columna + 1].getColor();
                if ((columna + 1) != 5 && elTablero[fila][columna + 2] == null) {
                    this.saltitos(fila, columna + 2, color);
                    elTablero[fila][columna + 1] = null;
                } else if (columna + 1 == 5) {
                    elTablero[fila][columna + 1] = null;
                    sonido();
                    if (color.equals(rojo)) {
                        aLaCaja[0]++;
                    } else {
                        aLaCaja[1]++;
                    }
                }
            }
            //izquierda
            if (columna != 0 && elTablero[fila][columna - 1] != null) {
                String color = elTablero[fila][columna - 1].getColor();
                if ((columna - 1) != 0 && elTablero[fila][columna - 2] == null) {
                    this.saltitos(fila, columna - 2, color);
                    elTablero[fila][columna - 1] = null;
                } else if (columna - 1 == 0) {
                    //volverACaja
                    elTablero[fila][columna - 1] = null;
                    sonido();
                    if (color.equals(rojo)) {
                        aLaCaja[0]++;
                    } else {
                        aLaCaja[1]++;
                    }
                }
            }
            //diagonal superior izquierda
            if (fila != 0 && columna != 0 && elTablero[fila - 1][columna - 1] != null) {
                String color = elTablero[fila - 1][columna - 1].getColor();
                if ((fila - 1) != 0 && (columna - 1) != 0 && 
                        elTablero[fila - 2][columna - 2] == null) {
                    this.saltitos(fila - 2, columna - 2, color);
                    elTablero[fila - 1][columna - 1] = null;
                } else if (fila - 1 == 0 || columna - 1 == 0) {
                    //volverACaja
                    elTablero[fila - 1][columna - 1] = null;
                    sonido();
                    if (color.equals(rojo)) {
                        aLaCaja[0]++;
                    } else {
                        aLaCaja[1]++;
                    }
                }
            }
            //diagonal superior derecha
            if (fila != 0 && columna != 5 && elTablero[fila - 1][columna + 1] != null) {
                String color = elTablero[fila - 1][columna + 1].getColor();
                if ((fila - 1) != 0 && (columna + 1) != 5 && 
                        elTablero[fila - 2][columna + 2] == null) {
                    elTablero[fila - 1][columna + 1] = null;
                    this.saltitos(fila - 2, columna + 2, color);
                } else if (fila - 1 == 0 || columna + 1 == 5) {
                    //volverACaja
                    elTablero[fila - 1][columna + 1] = null;
                    sonido();
                    if (color.equals(rojo)) {
                        aLaCaja[0]++;
                    } else {
                        aLaCaja[1]++;
                    }
                }
            }
            //diagonal inferior derecha
            if (fila != 5 && columna != 5 && elTablero[fila + 1][columna + 1] != null) {
                String color = elTablero[fila + 1][columna + 1].getColor();
                if ((fila + 1) != 5 && (columna + 1) != 5 && 
                        elTablero[fila + 2][columna + 2] == null) {
                    elTablero[fila + 1][columna + 1] = null;
                    this.saltitos(fila + 2, columna + 2, color);
                } else if (fila + 1 == 5 || columna + 1 == 5) {
                    //volverACaja
                    elTablero[fila + 1][columna + 1] = null; 
                    sonido();
                    if (color.equals(rojo)) {
                        aLaCaja[0]++;
                    } else {
                        aLaCaja[1]++;
                    }
                }
            }
            //diagonal inferior izquierda
            if (fila != 5 && columna != 0 && elTablero[fila + 1][columna - 1] != null) {
                String color = elTablero[fila + 1][columna - 1].getColor();
                if ((fila + 1) != 5 && (columna - 1) != 0 && 
                        elTablero[fila + 2][columna - 2] == null) {
                    elTablero[fila + 1][columna - 1] = null;
                    this.saltitos(fila + 2, columna - 2, color);
                } else if (fila + 1 == 5 || columna - 1 == 0) {
                    //volverACaja
                    elTablero[fila + 1][columna - 1] = null;
                    sonido();
                    if (color.equals(rojo)) {
                        aLaCaja[0]++;
                    } else {
                        aLaCaja[1]++;
                    }
                }
            }
        }
        
        return aLaCaja;
    }
    
    //saltó un gato por la tanto se crea uno nuevo en la posición a la que saltó
    public void saltitos(int fila, int columna, String color) {
        getTablero()[fila][columna] = new Gato(color);
        sonido();
    }
    
    //verifica si en la posicón dada hay un gato del color especificado
    public boolean estaOcupadaPor(String color, Gato gato) {
        return gato != null && gato.getColor().equals(color);
    }
    
    /**
     * 
     * @param color
     * @return true si hay tres gatos en linea en alguna fíla, columna o
     * diagonal. En caso contrario, false.
     */
    public boolean hayGanador(String color) {
        Gato[][] gatos = getTablero();
        boolean ganara = false;
        int filas = gatos.length;
        int columnas = gatos[0].length;
        // Verificar filas
        for (int fila = 0; fila < filas && !ganara; fila++) {
            for (int columna = 0; columna < columnas - 2 && !ganara; columna++) {
                Gato gato1 = gatos[fila][columna];
                Gato gato2 = gatos[fila][columna + 1];
                Gato gato3 = gatos[fila][columna + 2];

                if (this.estaOcupadaPor(color, gato1) && this.estaOcupadaPor(color, gato2)
                        && this.estaOcupadaPor(color, gato3)) {
                    ganara = true;
                }

            }
        }

        // Verificar columnas
        for (int columna = 0; columna < columnas && !ganara; columna++) {
            for (int fila = 0; fila < filas - 2 && !ganara; fila++) {
                Gato gato1 = gatos[fila][columna];
                Gato gato2 = gatos[fila + 1][columna];
                Gato gato3 = gatos[fila + 2][columna];
                if (this.estaOcupadaPor(color, gato1) && this.estaOcupadaPor(color, gato2)
                        && this.estaOcupadaPor(color, gato3)) {
                    ganara = true;
                }
            }
        }

        // Verificar diagonales de izquierda a derecha
        for (int fila = 0; fila <= filas - 3 && !ganara; fila++) {
            for (int columna = 0; columna <= columnas - 3 && !ganara; columna++) {
                Gato gato1 = gatos[fila][columna];
                Gato gato2 = gatos[fila + 1][columna + 1];
                Gato gato3 = gatos[fila + 2][columna + 2];

                if (this.estaOcupadaPor(color, gato1) && this.estaOcupadaPor(color, gato2)
                        && this.estaOcupadaPor(color, gato3)) {

                    ganara = true;
                }
            }
        }

        // Verificar diagonales de derecha a izquierda
        for (int fila = 0; fila <= filas - 3 && !ganara; fila++) {
            for (int columna = columnas - 1; columna >= 2 && !ganara; columna--) {
                Gato gato1 = gatos[fila][columna];
                Gato gato2 = gatos[fila + 1][columna - 1];
                Gato gato3 = gatos[fila + 2][columna - 2];

                if (this.estaOcupadaPor(color, gato1) && this.estaOcupadaPor(color, gato2)
                        && this.estaOcupadaPor(color, gato3)) {

                    ganara = true;
                }
            }
        }

        return ganara;
    }
    
    /*función que produce un sonido en consola. Será usada cuando un gato salta
    o vuelve a su caja. Esta funcion pudimos crearla a travez de diferente informacion obtenida por la herramienta chatGPT
    que nos proporciono una forma de crear sonido por consola, en este caso acudimos a crear sonido 
    a travez de frecuencia y una duracion del mismo*/
    public static void sonido() {
        try {
            // Definir las características del sonido
            float sampleRate = 44100;
            int sampleSizeInBits = 16;
            int channels = 1;
            boolean signed = true;
            boolean bigEndian = false;
            double frequency = 90; // Frecuencia del sonido en Hz
            int duration = 300; // Duración del sonido en milisegundos

            // Crear la línea de audio
            AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits, 
                    channels, signed, bigEndian);
            SourceDataLine line = AudioSystem.getSourceDataLine(format);
            line.open(format);
            line.start();

            // Generar el sonido de salto
            int bufferSize = (int) (sampleRate * duration / 1000);
            byte[] buffer = new byte[bufferSize];
            for (int i = 0; i < buffer.length; i++) {
                double angle = 2.0 * Math.PI * frequency * i / sampleRate;
                buffer[i] = (byte) (Math.sin(angle) * 127.0);
            }

            // Escribir el sonido en la línea de audio
            line.write(buffer, 0, buffer.length);

            // Detener la línea de audio
            line.drain();
            line.stop();
            line.close();
        } catch (LineUnavailableException e) {
            // Manejo de excepciones
            e.printStackTrace();
        }
    }

    /**
     * Inserta los gatos en la version full.
     * @param fila
     * @param columna
     * @param color
     * @param tamano
     * @param gatosQueVolvieron
     * @return Devuelve un array con la cantidad de gatos de cada color y 
     * tamaño que vulven a la caja.
     */
    public int[][] insertarFull(int fila, int columna, String color, String tamano,
            int[][] gatosQueVolvieron) {

        getTablero()[fila][columna] = new GatoFull(tamano, color);
        int[][] aCaja;
        aCaja = this.habraSaltosFullAux(fila, columna,
                gatosQueVolvieron, tamano);
        return aCaja;
    }
    

    public int[][] habraSaltosFullAux(int fila, int columna,
            int[][] gatosQueVuelven, String tamano) {
        int[][] aLaCaja = new int[2][2];
        //arriba
        if (getTablero()[fila][columna] != null) {
            Gato[][] elTablero = getTablero();
            GatoFull gatoActual = (GatoFull) getTablero()[fila][columna];
            if (fila != 0) {
                boolean salto = false;
                GatoFull gatoArriba = ((GatoFull) getTablero()[fila - 1][columna]);
                if (tamano.equalsIgnoreCase("grande")) {
                    if (gatoArriba != null) {
                        salto = true;
                    }
                } else {
                    if (gatoArriba != null && !gatoArriba.getTamano().equals("Grande")) {
                        salto = true;
                    }
                }
                
                if(salto){
                    this.habraSaltosFull(fila, columna, gatoArriba, ARRIBA,
                                aLaCaja, elTablero, gatosQueVuelven, gatoActual);
                }
                
            }
            //abajo
            if (fila != 5) {
                boolean salto = false;
                GatoFull gatoAbajo = ((GatoFull) getTablero()[fila + 1][columna]);
                if (tamano.equalsIgnoreCase("grande")) {
                    if (gatoAbajo != null) {
                        salto = true;
                    }
                } else {
                    if (gatoAbajo != null && !gatoAbajo.getTamano().equals("Grande")) {
                        salto = true;
                    }
                }
                
                if(salto){
                    this.habraSaltosFull(fila, columna, gatoAbajo, ABAJO,
                                aLaCaja, elTablero, gatosQueVuelven, gatoActual);
                }
                
            }
            //derecha
            if (columna != 5) {
                boolean salto = false;
                GatoFull gatoDer = ((GatoFull) getTablero()[fila][columna + 1]);
                if (tamano.equalsIgnoreCase("grande")) {
                    if (gatoDer != null) {
                        salto = true;
                    }
                } else {
                    if (gatoDer != null && !gatoDer.getTamano().equals("Grande")) {
                        salto = true;
                    }
                }
                
                if(salto){
                    this.habraSaltosFull(fila, columna, gatoDer, DERECHA,
                                aLaCaja, elTablero, gatosQueVuelven, gatoActual);
                    
                }
                

            }
            //izquierda
            if (columna != 0) {
                boolean salto = false;
                GatoFull gatoIzq = ((GatoFull) getTablero()[fila][columna - 1]);
                if (tamano.equalsIgnoreCase("grande")) {
                    if (gatoIzq != null) {
                        salto = true;
                    }
                } else {
                    if (gatoIzq != null && !gatoIzq.getTamano().equals("Grande")) {
                        salto = true;
                    }
                }
                
                if(salto){
                    this.habraSaltosFull(fila, columna, gatoIzq, IZQUIERDA,
                                aLaCaja, elTablero, gatosQueVuelven, gatoActual);
                }
                
            }
            //diagonal superior izquierda
            if (fila != 0 && columna != 0) {
                boolean salto = false;
                GatoFull gatoDIzq = ((GatoFull) getTablero()[fila - 1][columna - 1]);
                if (tamano.equalsIgnoreCase("grande")) {
                    if (gatoDIzq != null) {
                        salto = true;
                    }
                } else {
                    if (gatoDIzq != null && !gatoDIzq.getTamano().equals("Grande")) {
                        salto = true;
                    }
                }
                
                if(salto){
                    this.habraSaltosFull(fila, columna, gatoDIzq,
                                DIAGONAL_SUP_IZQ, aLaCaja, elTablero,
                                gatosQueVuelven, gatoActual);
                }
                
            }
            //diagonal superior derecha
            if (fila != 0 && columna != 5) {
                boolean salto = false;
                GatoFull gatoDSDer = ((GatoFull) getTablero()[fila - 1][columna + 1]);
                if (tamano.equalsIgnoreCase("grande")) {
                    if (gatoDSDer != null) {
                        salto = true;
                    }
                } else {
                    if (gatoDSDer != null && !gatoDSDer.getTamano().equals("Grande")) {
                        salto = true;
                    }
                }
                
                if(salto){
                    this.habraSaltosFull(fila, columna, gatoDSDer,
                                DIAGONAL_SUP_DER, aLaCaja, elTablero,
                                gatosQueVuelven, gatoActual);
                }
            }
            //diagonal inferior derecha
            if (fila != 5 && columna != 5) {
                boolean salto = false;
                GatoFull gatoDIDer = ((GatoFull) getTablero()[fila + 1][columna + 1]);
                if (tamano.equalsIgnoreCase("Grande")) {
                    if (gatoDIDer != null) {
                        salto = true;
                    }
                } else {
                    if (gatoDIDer != null && !gatoDIDer.getTamano().equals("Grande")) {
                        salto = true;
                    }
                }
                
                if(salto){
                    this.habraSaltosFull(fila, columna, gatoDIDer,
                                DIAGONAL_INF_DER, aLaCaja, elTablero,
                                gatosQueVuelven, gatoActual);
                }
            }
            //diagonal inferior izquierda
            if (fila != 5 && columna != 0) {
                boolean salto = false;
                GatoFull gatoDIIzq = ((GatoFull) getTablero()[fila + 1][columna - 1]);
                if (tamano.equalsIgnoreCase("grande")) {
                    if (gatoDIIzq != null) {
                        salto = true;
                    }
                } else {
                    if (gatoDIIzq != null && !gatoDIIzq.getTamano().equals("Grande")) {
                        salto = true;
                    }
                }
                
                if(salto){
                    this.habraSaltosFull(fila, columna, gatoDIIzq,
                                DIAGONAL_INF_IZQ, aLaCaja, elTablero,
                                gatosQueVuelven, gatoActual);
                }
                
            }
        }
        
        //sonido();

        return aLaCaja;
    }
    
    /**
     * Se encarga de hacer saltar los gatos en caso de ser posible en la dirección
     * indicada. 
     * Se fijara si este adyacente esta junto a otro gato. Si no lo esta hara 
     * un salto. Si esta en el borde del tablero se volera a la caja. 
     * @param fila
     * @param columna
     * @param gato
     * @param sentido
     * @param aLaCaja
     * @param elTablero
     * @param gatosQueVuelven
     * @param gatoActual 
     */
    public void habraSaltosFull(int fila, int columna, GatoFull gato, int sentido,
            int[][] aLaCaja, Gato[][] elTablero, int[][] gatosQueVuelven,
             GatoFull gatoActual) {

        String color = gato.getColor();
        String tamano = gato.getTamano();
        int turno = 0;
        int tipo = 0;
        if (tamano.equals("Grande")) {
            tipo = 1;
        }
        if (!color.equals(ROJO)) {
            turno = 1;
        }

        if (ARRIBA == sentido) {
            if ((fila - 1) != 0 && elTablero[fila - 2][columna] == null) {
                //String color = elTablero[fila-1][columna].getColor();
                this.saltitos(fila - 2, columna, color, tamano);
                elTablero[fila - 1][columna] = null;
                sonido();
            } else if (fila - 1 == 0) {
                //volverACaja
                elTablero[fila - 1][columna] = null;
                aLaCaja = this.cuantoDevolverACaja(aLaCaja, color,
                        tamano);
                gatosQueVuelven[turno][tipo]++;
                sonido();
                //aLaCaja++;
            }
        } else if (ABAJO == sentido) {
            if ((fila + 1) != 5 && elTablero[fila + 2][columna] == null) {
                //String color = elTablero[fila+1][columna].getColor();
                this.saltitos(fila + 2, columna, color, tamano);
                elTablero[fila + 1][columna] = null;
                sonido();
            } else if (fila + 1 == 5) {
                //volverACaja
                elTablero[fila + 1][columna] = null;
                aLaCaja = this.cuantoDevolverACaja(aLaCaja, color,
                        tamano);
                gatosQueVuelven[turno][tipo]++;
                sonido();
                //aLaCaja++;
            }
        } else if (IZQUIERDA == sentido) {
            if ((columna - 1) != 0 && elTablero[fila][columna - 2] == null) {
                this.saltitos(fila, columna - 2, color, tamano);
                elTablero[fila][columna - 1] = null;
                sonido();
            } else if (columna - 1 == 0) {
                //volverACaja
                elTablero[fila][columna - 1] = null;
                aLaCaja = this.cuantoDevolverACaja(aLaCaja, color,
                        tamano);
                gatosQueVuelven[turno][tipo]++;
                sonido();
                //aLaCaja++;
            }
        } else if (DERECHA == sentido) {
            if ((columna + 1) != 5 && elTablero[fila][columna + 2] == null) {
                this.saltitos(fila, columna + 2, color, tamano);
                elTablero[fila][columna + 1] = null;
                sonido();
            } else if (columna + 1 == 5) {
                //volverACaja
                elTablero[fila][columna + 1] = null;
                aLaCaja = this.cuantoDevolverACaja(aLaCaja, color,
                        tamano);
                gatosQueVuelven[turno][tipo]++;
                sonido();
                //aLaCaja++;
            }
        } else if (DIAGONAL_SUP_DER == sentido) {
            if ((fila - 1) != 0 && (columna + 1) != 5
                    && elTablero[fila - 2][columna + 2] == null) {
                //String color = elTablero[fila-1][columna].getColor();
                this.saltitos(fila - 2, columna + 2, color, tamano);
                elTablero[fila - 1][columna + 1] = null;
                sonido();
            } else if (fila - 1 == 0 || columna + 1 == 5) {
                //volverACaja
                elTablero[fila - 1][columna + 1] = null;
                aLaCaja = this.cuantoDevolverACaja(aLaCaja, color,
                        tamano);
                gatosQueVuelven[turno][tipo]++;
                sonido();
                //aLaCaja++;
            }
        } else if (DIAGONAL_SUP_IZQ == sentido) {
            if ((fila - 1) != 0 && (columna - 1) != 0
                    && elTablero[fila - 2][columna - 2] == null) {
                //String color = elTablero[fila-1][columna].getColor();
                this.saltitos(fila - 2, columna - 2, color, tamano);
                elTablero[fila - 1][columna - 1] = null;
                sonido();
            } else if (fila - 1 == 0 || columna - 1 == 0) {
                //volverACaja
                elTablero[fila - 1][columna - 1] = null;
                aLaCaja = this.cuantoDevolverACaja(aLaCaja, color,
                        tamano);
                gatosQueVuelven[turno][tipo]++;
                sonido();
                //aLaCaja++;
            }
        } else if (DIAGONAL_INF_IZQ == sentido) {
            if ((fila + 1) != 5 && (columna - 1) != 0
                    && elTablero[fila + 2][columna - 2] == null) {
                //String color = elTablero[fila-1][columna].getColor();
                this.saltitos(fila + 2, columna - 2, color, tamano);
                elTablero[fila + 1][columna - 1] = null;
                sonido();
            } else if (fila + 1 == 5 || columna - 1 == 0) {
                //volverACaja
                elTablero[fila + 1][columna - 1] = null;
                aLaCaja = this.cuantoDevolverACaja(aLaCaja, color,
                        tamano);
                gatosQueVuelven[turno][tipo]++;
                sonido();
                //aLaCaja++;
            }
        } else {
            if ((fila + 1) != 5 && (columna + 1) != 5
                    && elTablero[fila + 2][columna + 2] == null) {
                //String color = elTablero[fila-1][columna].getColor();
                this.saltitos(fila + 2, columna + 2, color, tamano);
                elTablero[fila + 1][columna + 1] = null;
                sonido();
            } else if (fila + 1 == 5 || columna + 1 == 5) {
                //volverACaja
                elTablero[fila + 1][columna + 1] = null;
                aLaCaja = this.cuantoDevolverACaja(aLaCaja, color,
                        tamano);
                gatosQueVuelven[turno][tipo]++;
                sonido();
                //aLaCaja++;
            }
        }
        //sonido();
    }

    /*función encargada de poner un gato en la nueva posición a la que saltó
    en la versión full.*/
    public void saltitos(int fila, int columna, String color, String tamano) {
        getTablero()[fila][columna] = new GatoFull(tamano, color);
    }

    /*
    Aumenta la posición de la matriz donde indica el gato que será vuelto a la
    caja. La fila 0 es para el color rojo, la 1 para el azul. La columna 
    1 es para los gatos grandes y la 0 para los gatitos.
    */
    public int[][] cuantoDevolverACaja(int[][] cuantoACaja, String color, String tamano) {
        if (color.equals(ROJO)) {
            if (tamano.equals("Grande")) {
                cuantoACaja[0][1]++;
            } else {
                cuantoACaja[0][0]++;
            }
        } else {
            if (tamano.equals("Grande")) {
                cuantoACaja[1][1]++;
            } else {
                cuantoACaja[1][0]++;
            }
        }
        return cuantoACaja;
    }

    /**
     * Verifica si el jugador gana gatos grandes por tener tres gatitos en linea
     * en alguna fila, columna o diagonal.
     * @param color
     * @param gatosQueSeVan
     * @return true si gana gatos, false de lo contrari. 
     */
    public boolean GanaGatosGrandes(String color, int[][] gatosQueSeVan) {
        Gato[][] gatos = getTablero();
        boolean ganara = false;
        int filas = gatos.length;
        int columnas = gatos[0].length;
        int jugador = 0;
        if (!color.equals(ROJO)) {
            jugador = 1;
        }
        // Verificar filas
        for (int fila = 0; fila < filas && !ganara; fila++) {
            for (int columna = 0; columna < columnas - 2 && !ganara; columna++) {
                GatoFull gato1 = (GatoFull) gatos[fila][columna];
                GatoFull gato2 = (GatoFull) gatos[fila][columna + 1];
                GatoFull gato3 = (GatoFull) gatos[fila][columna + 2];
                if (gato1 != null && gato2 != null && gato3 != null) {
                    boolean hayTresEnLinea = this.hayTresGatosEnLinea(gato1,
                            gato2, gato3, color);
                    if (hayTresEnLinea) {
                        ganara = true;
                        String tamano1 = gato1.getTamano();
                        String tamano2 = gato2.getTamano();
                        String tamano3 = gato3.getTamano();
                        this.seVanDeTablero(tamano1, tamano2, tamano3,
                                gatosQueSeVan, jugador);
                        gatos[fila][columna] = null;
                        gatos[fila][columna + 1] = null;
                        gatos[fila][columna + 2] = null;

                    }
                }
            }
        }

        // Verificar columnas
        for (int columna = 0; columna < columnas && !ganara; columna++) {
            for (int fila = 0; fila < filas - 2 && !ganara; fila++) {
                GatoFull gato1 = (GatoFull) gatos[fila][columna];
                GatoFull gato2 = (GatoFull) gatos[fila + 1][columna];
                GatoFull gato3 = (GatoFull) gatos[fila + 2][columna];
                if (gato1 != null && gato2 != null && gato3 != null) {
                    boolean hayTresEnLinea = this.hayTresGatosEnLinea(gato1,
                            gato2, gato3, color);
                    if (hayTresEnLinea) {
                        ganara = true;
                        String tamano1 = gato1.getTamano();
                        String tamano2 = gato2.getTamano();
                        String tamano3 = gato3.getTamano();
                        this.seVanDeTablero(tamano1, tamano2, tamano3,
                                gatosQueSeVan, jugador);
                        gatos[fila][columna] = null;
                        gatos[fila + 1][columna] = null;
                        gatos[fila + 2][columna] = null;

                    }
                }
            }
        }

        // Verificar diagonales de izquierda a derecha
        for (int fila = 0; fila <= filas - 3 && !ganara; fila++) {
            for (int columna = 0; columna <= columnas - 3 && !ganara; columna++) {
                GatoFull gato1 = (GatoFull) gatos[fila][columna];
                GatoFull gato2 = (GatoFull) gatos[fila + 1][columna + 1];
                GatoFull gato3 = (GatoFull) gatos[fila + 2][columna + 2];
                if (gato1 != null && gato2 != null && gato3 != null) {
                    boolean hayTresEnLinea = this.hayTresGatosEnLinea(gato1,
                            gato2, gato3, color);
                    if (hayTresEnLinea) {
                        ganara = true;
                        String tamano1 = gato1.getTamano();
                        String tamano2 = gato2.getTamano();
                        String tamano3 = gato3.getTamano();
                        this.seVanDeTablero(tamano1, tamano2, tamano3,
                                gatosQueSeVan, jugador);
                        gatos[fila][columna] = null;
                        gatos[fila + 1][columna + 1] = null;
                        gatos[fila + 2][columna + 2] = null;

                    }
                }
            }
        }

        // Verificar diagonales de derecha a izquierda
        for (int fila = 0; fila <= filas - 3 && !ganara; fila++) {
            for (int columna = columnas - 1; columna >= 2 && !ganara; columna--) {
                GatoFull gato1 = (GatoFull) gatos[fila][columna];
                GatoFull gato2 = (GatoFull) gatos[fila + 1][columna - 1];
                GatoFull gato3 = (GatoFull) gatos[fila + 2][columna - 2];
                if (gato1 != null && gato2 != null && gato3 != null) {
                    boolean hayTresEnLinea = this.hayTresGatosEnLinea(gato1,
                            gato2, gato3, color);
                    if (hayTresEnLinea) {
                        ganara = true;
                        String tamano1 = gato1.getTamano();
                        String tamano2 = gato2.getTamano();
                        String tamano3 = gato3.getTamano();
                        this.seVanDeTablero(tamano1, tamano2, tamano3,
                                gatosQueSeVan, jugador);
                        gatos[fila][columna] = null;
                        gatos[fila + 1][columna - 1] = null;
                        gatos[fila + 2][columna - 2] = null;

                    }
                }
            }
        }

        return ganara;
    }

    /*
    Aumenta en la posicion que corresponde de la matriz gatosQueSeVan para
    llevar la cuenta la cantidad de gatos de este jugador que volveran a la caja.
    */
    public void seVanDeTablero(String tamano1, String tamano2, String tamano3,
            int[][] gatosQueSeVan, int jugador) {

        if (tamano1.equals("Grande")) {
            gatosQueSeVan[jugador][1]++;
        } else {
            gatosQueSeVan[jugador][0]++;
        }
        if (tamano2.equals("Grande")) {
            gatosQueSeVan[jugador][1]++;
        } else {
            gatosQueSeVan[jugador][0]++;
        }
        if (tamano3.equals("Grande")) {
            gatosQueSeVan[jugador][1]++;
        } else {
            gatosQueSeVan[jugador][0]++;
        }

    }

    /**
     * Verifica si un jugador gano teniendo tres gatos en linea en cualquiera de
     * alguna fila, columna o diagonal.
     * @param color
     * @return true si gano, falso de lo contrario.
     */
    public boolean HayGanadorFull(String color) {
        Gato[][] gatos = getTablero();
        boolean ganara = false;
        int filas = gatos.length;
        int columnas = gatos[0].length;
        // Verificar filas
        for (int fila = 0; fila < filas && !ganara; fila++) {
            for (int columna = 0; columna < columnas - 2 && !ganara; columna++) {
                GatoFull gato1 = (GatoFull) gatos[fila][columna];
                GatoFull gato2 = (GatoFull) gatos[fila][columna + 1];
                GatoFull gato3 = (GatoFull) gatos[fila][columna + 2];
                if (gato1 != null && gato2 != null && gato3 != null) {
                    if (gato1.getTamano().equals("Grande")
                            && gato2.getTamano().equals("Grande") && gato3.getTamano().equals("Grande")) {
                        if (this.estaOcupadaPor(color, gato1) && this.estaOcupadaPor(color, gato2)
                                && this.estaOcupadaPor(color, gato3)) {
                            ganara = true;
                        }
                    }
                }
            }
        }

        // Verificar columnas
        for (int columna = 0; columna < columnas && !ganara; columna++) {
            for (int fila = 0; fila < filas - 2 && !ganara; fila++) {
                GatoFull gato1 = (GatoFull) gatos[fila][columna];
                GatoFull gato2 = (GatoFull) gatos[fila + 1][columna];
                GatoFull gato3 = (GatoFull) gatos[fila + 2][columna];
                if (gato1 != null && gato2 != null && gato3 != null) {
                    if (gato1.getTamano().equals("Grande")
                            && gato2.getTamano().equals("Grande") && gato3.getTamano().equals("Grande")) {
                        if (this.estaOcupadaPor(color, gato1) && this.estaOcupadaPor(color, gato2)
                                && this.estaOcupadaPor(color, gato3)) {
                            ganara = true;
                        }
                    }
                }
            }
        }

        // Verificar diagonales de izquierda a derecha
        for (int fila = 0; fila <= filas - 3 && !ganara; fila++) {
            for (int columna = 0; columna <= columnas - 3 && !ganara; columna++) {
                GatoFull gato1 = (GatoFull) gatos[fila][columna];
                GatoFull gato2 = (GatoFull) gatos[fila + 1][columna + 1];
                GatoFull gato3 = (GatoFull) gatos[fila + 2][columna + 2];
                if (gato1 != null && gato2 != null && gato3 != null) {
                    if (gato1.getTamano().equals("Grande")
                            && gato2.getTamano().equals("Grande") && gato3.getTamano().equals("Grande")) {
                        if (this.estaOcupadaPor(color, gato1) && this.estaOcupadaPor(color, gato2)
                                && this.estaOcupadaPor(color, gato3)) {

                            ganara = true;
                        }
                    }
                }
            }
        }

        // Verificar diagonales de derecha a izquierda
        for (int fila = 0; fila <= filas - 3 && !ganara; fila++) {
            for (int columna = columnas - 1; columna >= 2 && !ganara; columna--) {
                GatoFull gato1 = (GatoFull) gatos[fila][columna];
                GatoFull gato2 = (GatoFull) gatos[fila + 1][columna - 1];
                GatoFull gato3 = (GatoFull) gatos[fila + 2][columna - 2];
                if (gato1 != null && gato2 != null && gato3 != null) {
                    if (gato1.getTamano().equals("Grande")
                            && gato2.getTamano().equals("Grande") && gato3.getTamano().equals("Grande")) {
                        if (this.estaOcupadaPor(color, gato1) && this.estaOcupadaPor(color, gato2)
                                && this.estaOcupadaPor(color, gato3)) {

                            ganara = true;
                        }
                    }
                }
            }
        }

        return ganara;
    }

    /*
    Si hay tres gatos en linea de los caules al menos uno de ellos es chico,
    devuelve true, de lo contrario devuelve false.
    */
    public boolean hayTresGatosEnLinea(GatoFull gato1, GatoFull gato2,
            GatoFull gato3, String color) {

        boolean losHay = false;
        String tamano1 = gato1.getTamano();
        String tamano2 = gato2.getTamano();
        String tamano3 = gato3.getTamano();

        if (tamano1.equals("Chico") || tamano2.equals("Chico")
                || tamano3.equals("chico")) {
            if (this.estaOcupadaPor(color, gato1)
                    && this.estaOcupadaPor(color, gato2)
                    && this.estaOcupadaPor(color, gato3)) {
                losHay = true;
            }
        }

        return losHay;

    }

    public void eliminarGatito(int fila, int columna) {
        Gato[][] elTablero = this.getTablero();
        elTablero[fila][columna] = null;
    }

    public void eliminarGato(int fila, int columna) {
        Gato[][] elTablero = this.getTablero();
        elTablero[fila][columna] = null;
    }

    public String tipoDeGato(int fila, int columna) {
        Gato[][] elTablero = this.getTablero();
        if (((GatoFull) elTablero[fila][columna]).getTamano().equals("Grande")) {
            return "Grande";
        } else {
            return "Chico";
        }
    }

}
