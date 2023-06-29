package obligatorio;

import java.util.*;
import java.util.Collections;


/**
 *
 * @author Luciano Umpierrez
 */
public class Juego {

    private Caja[] caja;
    private CajaFull[] cajaFull;
    private Tablero tablero;
    private ArrayList<Jugador> jugadores;
    private int[] gatosEnTablero;
    private static Scanner leer = new Scanner(System.in);
    private static final String ROJO = "\u001B[31m";
    private int[][] gatosEnTableroFull;

    //getters y setters
    
    public int[][] getGatosEnTableroFull() {
        return gatosEnTableroFull;
    }

    public void setGatosEnTableroFull(int[][] gatosEnTableroFull) {
        this.gatosEnTableroFull = gatosEnTableroFull;
    }

    public Tablero getTablero() {
        return tablero;
    }
    
    public void setTablero(String[] valores) {
        char caracter = valores[1].charAt(0);
        String color = valores[0];
        this.tablero = new Tablero(caracter, color);
    }

    public CajaFull[] getCajaFull() {
        return this.cajaFull;
    }
    
    private void setCajaFull(CajaFull[] unasCajas) {
        this.cajaFull = unasCajas;
    }
    
    public int[] getGatosEnTablero() {
        return this.gatosEnTablero;
    }
    
    public Caja[] getCaja(){
        return this.caja;
    }
    
    private void setCaja(Caja[] unaCaja) {
        this.caja = unaCaja;
    }

    public ArrayList<Jugador> getJugadores() {
        return this.jugadores;
    }
    
    private void setJugadores(ArrayList<Jugador> unosJugadores) {
        this.jugadores = unosJugadores;
    }
    
    //métodos

    public void SacarGatosDelTablero(int color, int tipo, int cantidad) {
        int prueba = getGatosEnTableroFull()[color][tipo];
        prueba = getGatosEnTableroFull()[color][tipo] - cantidad;
        getGatosEnTableroFull()[color][tipo]
                = getGatosEnTableroFull()[color][tipo] - cantidad;
    }

    public int cantGatosGrandes(String color) {
        int cantidad = 0;
        CajaFull[] cajas = getCajaFull();
        if (color.equals(ROJO)) {
            cantidad = cajas[0].getCantidadGatosGrandes();
        } else {
            cantidad = cajas[1].getCantidadGatosGrandes();
        }
        return cantidad;
    }

    public int cantGatosChicos(String color) {
        int cantidad = 0;
        CajaFull[] cajas = getCajaFull();
        if (color.equals(ROJO)) {
            cantidad = cajas[0].getCantidadGatosChicos();
        } else {
            cantidad = cajas[1].getCantidadGatosChicos();
        }
        return cantidad;

    }

    public void insertarGatoFull(String color, int[] posiciones, String tamano) {
        int fila = posiciones[0];
        int columna = posiciones[1];
        String rojo = "\u001B[31m";
        int[][] gatosQueVolvieron = new int[2][2];
        int[][] aCaja = getTablero().insertarFull(fila - 1, columna - 1, color,
                tamano, gatosQueVolvieron);
        CajaFull cajaRoja = getCajaFull()[0];
        CajaFull cajaAzul = getCajaFull()[1];

        if (color.equals(rojo)) {
            if (tamano.equals("Grande")) {
                this.getGatosEnTableroFull()[0][1]++;
                aCaja[0][1] = aCaja[0][1] - 1;
            } else {
                this.getGatosEnTableroFull()[0][0]++;
                aCaja[0][0] = aCaja[0][0] - 1;
            }
        } else {
            if (tamano.equals("Grande")) {
                this.getGatosEnTableroFull()[1][1]++;
                aCaja[1][1] = aCaja[1][1] - 1;
            } else {
                this.getGatosEnTableroFull()[1][0]++;
                aCaja[1][0] = aCaja[1][0] - 1;
            }
        }
        cajaRoja.setCantidadGatosChicos(cajaRoja.getCantidadGatosChicos()
                + aCaja[0][0]);
        cajaRoja.setCantidadGatosGrandes(cajaRoja.getCantidadGatosGrandes()
                + aCaja[0][1]);
        cajaAzul.setCantidadGatosChicos(cajaAzul.getCantidadGatosChicos()
                + aCaja[1][0]);
        cajaAzul.setCantidadGatosGrandes(cajaAzul.getCantidadGatosGrandes()
                + aCaja[1][1]);
        SacarGatosDelTablero(0, 0, gatosQueVolvieron[0][0]);
        SacarGatosDelTablero(0, 1, gatosQueVolvieron[0][1]);
        SacarGatosDelTablero(1, 0, gatosQueVolvieron[1][0]);
        SacarGatosDelTablero(1, 1, gatosQueVolvieron[1][1]);
    }

    public void imprimirCantidadEnCajasFull() {
        CajaFull cajaRoja = getCajaFull()[0];
        CajaFull cajaAzul = getCajaFull()[1];
        int cantCajaRojaChicos = cajaRoja.getCantidadGatosChicos();
        int cantCajaAzulChicos = cajaAzul.getCantidadGatosChicos();
        int cantCajaRojaGrandes = cajaRoja.getCantidadGatosGrandes();
        int cantCajaAzulGrandes = cajaAzul.getCantidadGatosGrandes();
        System.out.println("En la caja roja quedan " + cantCajaRojaChicos
                + " gatitos y " + cantCajaRojaGrandes + " gatos grandes");
        System.out.println("En la caja azul quedan " + cantCajaAzulChicos
                + " gatitos y " + cantCajaAzulGrandes + " gatos Grandes");
    }

    public void reglaAdicionalUno(int fila, int columna, String color) {
        this.getTablero().eliminarGatito(fila, columna);
        if (color.equals(ROJO)) {
            CajaFull cajaRoja = getCajaFull()[0];
            cajaRoja.setCantidadGatosGrandes(cajaRoja.getCantidadGatosGrandes()
                    + 1);
            SacarGatosDelTablero(0, 0, 1);
        } else {
            CajaFull cajaAzul = getCajaFull()[1];
            cajaAzul.setCantidadGatosGrandes(cajaAzul.getCantidadGatosGrandes()
                    + 1);
            SacarGatosDelTablero(1, 0, 1);
        }
    }

    public void reglaAdicionalDos(int fila, int columna, String color) {

        if (color.equals(ROJO)) {
            CajaFull cajaRoja = getCajaFull()[0];
            cajaRoja.setCantidadGatosGrandes(cajaRoja.getCantidadGatosGrandes()
                    + 1);
            SacarGatosDelTablero(0, 1, 1);
        } else {
            CajaFull cajaAzul = getCajaFull()[1];
            cajaAzul.setCantidadGatosGrandes(cajaAzul.getCantidadGatosGrandes()
                    + 1);
            SacarGatosDelTablero(1, 1, 1);
        }
        this.getTablero().eliminarGato(fila, columna);
    }

    public String verificarQueGatoHay(int fila, int columna) {
        String gatoOGatito = this.getTablero().tipoDeGato(fila, columna);
        return gatoOGatito;
    }

    public boolean ganadorFull(String color) {
        boolean ganador = getTablero().HayGanadorFull(color);
        return ganador;
    }

    public boolean ganaGrandes(String color) {
        int[][] gatosQueSeVan = new int[2][2];
        boolean ganaGatos = getTablero().GanaGatosGrandes(color, gatosQueSeVan);
        int cual = 0;
        if (ganaGatos) {
            if (!color.equals(ROJO)) {
                cual = 1;
            }
            CajaFull caja = getCajaFull()[cual];
            caja.setCantidadGatosGrandes(
                    caja.getCantidadGatosGrandes() + 3);
        }
        SacarGatosDelTablero(0, 0, gatosQueSeVan[0][0]);
        SacarGatosDelTablero(0, 1, gatosQueSeVan[0][1]);
        SacarGatosDelTablero(1, 0, gatosQueSeVan[1][0]);
        SacarGatosDelTablero(1, 1, gatosQueSeVan[1][1]);

        return ganaGatos;
    }

    public void VerAlias() {
        boolean hayAlias = false;
        for (Jugador jugador : jugadores) {
            if (jugador.getAlias() != null) {
                hayAlias = true;
                break;
            }
        }
        if (hayAlias) {
            System.out.print("Alias usado: ");
            int i = 1;
            for (Jugador jugador : jugadores) {
                System.out.print(i+"-"+jugador.getAlias() + "  ");
                i++;
            }
            System.out.println("");
        }

    }
    public void ordenarJugadoresPorNombre(ArrayList<Jugador> jugadores) {
        Collections.sort(jugadores);
    }
    public boolean aliasRepetido(String alias) {
        Jugador aux = new Jugador(alias);
        ArrayList<Jugador> lista = getJugadores();
        boolean repetido = lista.contains(aux);
        return repetido;
    }

    //Funcion que recorre la lista jugadores y agrega a jugador
    public void agregarJugador(ArrayList<Jugador> jugadores, Jugador jugador) {
            getJugadores().add(jugador);
        }
    

    //Funcion que retorna la cantidad de jugadores
    public int cuantosJugadoresHay() {
        int hay = 0;
        if (this.getJugadores() != null) {
            hay = getJugadores().size();
        }
        return hay;

    }

    public Juego(String[] datosTablero) {
        this.setTablero(datosTablero);
        //this.setCaja();

        Caja[] nuevaCaja = new Caja[2];
        for (int i = 0; i < nuevaCaja.length; i++) {
            nuevaCaja[i] = new Caja();
        }
        
        this.setCaja(nuevaCaja);

        int[][] unosGatosEnTableroFull = new int[2][2];
        this.setGatosEnTableroFull(unosGatosEnTableroFull);
        
        
        CajaFull[] nuevaCajaFull = new CajaFull[2];
        for (int i = 0; i < nuevaCajaFull.length; i++) {
            nuevaCajaFull[i] = new CajaFull();
        }

        this.setCajaFull(nuevaCajaFull);
        
        ArrayList<Jugador> unosJugadores = new ArrayList<>();
        this.setJugadores(unosJugadores);

    }
    
    public void insertarGato(String color, int[] posiciones) {
        int fila = posiciones[0];
        int columna = posiciones[1];
        String rojo = "\u001B[31m";
        int[] aCaja = getTablero().insertar(fila - 1, columna - 1, color);
        Caja cajaRoja = getCaja()[0];
        Caja cajaAzul = getCaja()[1];

        if (color.equals(rojo)) {
            aCaja[0] = aCaja[0] - 1;
        } else {
            aCaja[1] = aCaja[1] - 1;
        }
        cajaRoja.setCantidadGatos(cajaRoja.getCantidadGatos() + aCaja[0]);
        cajaAzul.setCantidadGatos(cajaAzul.getCantidadGatos() + aCaja[1]);
        //this.getTablero().sonido();
    }

    //Recorro la lista jugadores y pregunto si el alias del jugador es igual al alias que le pase, 
    //entonces me devuelve el jugador
    public Jugador queJugadorEs(String alias) {
        Jugador esEste = null;
        for (Jugador jugador : getJugadores()) {
            if (jugador.getAlias().equals(alias)) {
                esEste = jugador;
            }
        }
        return esEste;
    }

    //Me devuelve true si hay un ganador con el color pasado
    public boolean hayGanador(String unColor) {
        boolean seGano = getTablero().hayGanador(unColor);
        return seGano;
    }

    //Me devuelve la cantidad de gatos que quedan
    public int cuantosGatosQuedan(int cual) {
        return this.caja[cual].getCantidadGatos();
    }
    // public int cuantosGatosQuedaFull(int cual){
    //   return this.cajaFull[cual].()
    //}
    //Recorro la listaJugadores y los imprimo

    public void imprimirJugadores() {
        ArrayList<Jugador> listaJugadores = this.getJugadores();
        for (Jugador jugador : listaJugadores) {
            jugador.toString();
            System.out.println("");
        }
    }

    //Imprime la cantidad de gatos que tengo en la caja actualmente
    public void imprimirCantidadEnCajas() {
        Caja cajaRoja = this.getCaja()[0];
        Caja cajaAzul = this.getCaja()[1];
        int cantCajaRoja = cajaRoja.getCantidadGatos();
        int cantCajaAzul = cajaAzul.getCantidadGatos();
        System.out.println("En la caja roja quedan " + cantCajaRoja
                + " gatitos");
        System.out.println("En la caja azul quedan " + cantCajaAzul
                + " gatitos");
    }

    public void imprimirTablero() {
        this.getTablero().imprimir();
    }

    public void imprimirGatosEnTablero() {
        int[][] prueba = this.getGatosEnTableroFull();
        System.out.format(
                "En tablero hay de color rojo %d chicos y %d grandes."
                + " De color azul hay %d chicos y %d grandes ",
                prueba[0][0], prueba[0][1], prueba[1][0], prueba[1][1]);
        System.out.println(" ");
    }

    public void resetSimple() {
        Caja[] caja = this.getCaja();
        for (int i = 0; i < caja.length; i++) {
            caja[i] = new Caja();
        }
        //this.setCaja(caja);
    }

    public void aumentarVecesJugadas(String alias) {
        for (Jugador jugador : getJugadores()) {
            if (jugador.getAlias().equals(alias)) {
                jugador.setCuantasVecesJugo(jugador.getCuantasVecesJugo() + 1);
            }
        }
    }
    public String retornarAlias(int pos){
        Jugador jugador = jugadores.get(pos);
        return jugador.getAlias();
    }
    
    public void resetFull() {
        //reset gatosEnTablero
        int[][] gatosEnTablero = this.getGatosEnTableroFull();
        for (int i = 0; i < gatosEnTablero.length; i++) {
            for (int j = 0; j < gatosEnTablero.length; j++) {
                gatosEnTablero[i][j] = 0;
            }
        }

        //resetCajas
        CajaFull[] caja = this.getCajaFull();
        for (int i = 0; i < caja.length; i++) {
            caja[i] = new CajaFull();
        }
    }

}
