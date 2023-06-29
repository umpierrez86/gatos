package obligatorio;

import java.io.FileOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author Luciano Umpierrez 
 */
public class Menus {

    private static final Font TITULO = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static final Font SUBTITULO = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
    private static final Font CUERPO = new Font(Font.FontFamily.HELVETICA, 12);
    private static final Scanner leer = new Scanner(System.in);
    private static final int INGRESAR_JUGADORES = 1;
    private static final int INGRESAR_GATO = 1;
    private static final int JUGAR = 2;
    private static final int TERMINAR_MIN = 88; //x en codigo ASCII miniscula
    private static final int TERMINAR_MAY = 120; //x en codigo ASCII mayuscula
    private static final String SIMPLE = "Simple";
    private static final int REGLA_ADICIONAL_UNO = 2;
    private static final int REGLA_ADICIONAL_DOS = 3;
    private static final int TERMINAR = 3;
    private static final int TERMINAR_FULL = 4;
    private static final String ROJO = "\u001B[31m";
    private static final String AZUL = "\u001B[34m";
    private static final int VERSION_SIMPLE = 1;
    private static final int VERSION_FULL = 2;

    //Menu 
    public void menuParaingresasrJugadores(Juego unJuego, String cualJuego,
            String[] tablero) {
        int eleccion = 0;
        while (eleccion != TERMINAR_MIN && eleccion != TERMINAR_MAY) {
            eleccion = ingresarOJugar2(unJuego);

            switch (eleccion) {
                case INGRESAR_JUGADORES:
                    int cuantos = this.cuantosIngresan(unJuego);
                    this.DatosDelJugador(unJuego, cuantos);
                    break;
                case JUGAR:
                    QueVersionJuego(unJuego, cualJuego);
                    //reseteo juego
                    resetJuego(cualJuego, unJuego);
                    //reseteo tablero
                    unJuego.setTablero(tablero);
                    break;
                default:
                    break;
            }

        }

    }

    //Esta funcion fue creada para resetear el juego
    public void resetJuego(String cualJuego, Juego unJuego) {
        if (cualJuego.equals("Simple")) {
            unJuego.resetSimple();
        } else {
            unJuego.resetFull();
        }
    }

    /* Metodo que genera pdf, esta forma de generar pdf la pudimos generar gracias a
    que ChatGPT nos dio la idea de hacerlo con apache o itext, elegimos itext para generar el pdf
    de eso buscamos como se usa itext y nos encontramos con esta pagina https://codigoxules.org/java-itext-pdf-creando-pdf-java-itext/
    en esta pudimos ver como generar titulo, subtitulo y que queremos dentro del pdf */
    public static void generarPDF(ArrayList<Jugador> jugadores) throws IOException, DocumentException {
        Document documento = new Document();
        PdfWriter.getInstance(documento, new FileOutputStream("juego.pdf"));
        documento.open();

        Titulo(documento, "Lista de Jugadores");

        Subtitulo(documento, "Ordenados alfabéticamente por nombre");

        Cuerpo(documento, generarContenido(jugadores));

        documento.close();
    }

    //Metodo para el titulo
    private static void Titulo(Document document, String texto) throws DocumentException {
        Paragraph titulo = new Paragraph(texto, TITULO);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(10f);
        document.add(titulo);
    }

    private static void Subtitulo(Document document, String texto) throws DocumentException {
        Paragraph subtitulo = new Paragraph(texto, SUBTITULO);
        subtitulo.setAlignment(Element.ALIGN_CENTER);
        subtitulo.setSpacingAfter(10f);
        document.add(subtitulo);
    }

    private static void Cuerpo(Document documento, String texto) throws DocumentException {
        Paragraph cuerpo = new Paragraph(texto, CUERPO);
        cuerpo.setSpacingAfter(10f);
        documento.add(cuerpo);
    }

    public static String generarContenido(ArrayList<Jugador> jugadores) {
        StringBuilder contenido = new StringBuilder();

        for (Jugador jugador : jugadores) {
            contenido.append("Nombre: ").append(jugador.getNombre()).append(", edad: ").append(jugador.getEdad()).append(", alias: ").append(jugador.getAlias()).append(" jugo ").append(jugador.getCuantasVecesJugo());
            contenido.append("\n");
        }

        return contenido.toString();
    }

    public void MenuBienvenida() {
        System.out.println("BIENVENIDO!");
        int elegido = 0;
        Juego unJuego = null;
        int vecesJugadas = 1;
        while (elegido != TERMINAR_MIN && elegido != TERMINAR_MAY) {
            System.out.println("""
                           A que quieres jugar?
                           1 - Versión simple
                           2 - Versión full
                           3 - x para salir""");
            System.out.println("Ingresa el número del juego deseado: ");
            int[] valores = {1, 2};
            elegido = eleccionRestringida(valores);
            if (elegido != TERMINAR_MIN && elegido != TERMINAR_MAY) {

                String[] tablero = this.queTablero();
                if (vecesJugadas == 1) {
                    unJuego = new Juego(tablero);

                    switch (elegido) {
                        case VERSION_SIMPLE:
                            this.menuParaingresasrJugadores(unJuego, "Simple",
                                    tablero);
                            break;
                        case VERSION_FULL:
                            this.menuParaingresasrJugadores(unJuego, "Full",
                                    tablero);
                            break;
                        default:
                            break;
                    }

                    vecesJugadas++;
                } else {

                    unJuego.setTablero(tablero);

                    switch (elegido) {
                        case VERSION_SIMPLE:
                            this.menuParaingresasrJugadores(unJuego, "Simple",
                                    tablero);
                            break;
                        case VERSION_FULL:
                            this.menuParaingresasrJugadores(unJuego, "Full",
                                    tablero);
                            break;
                        default:
                            break;
                    }
                }

            }

        }
        try {
            unJuego.ordenarJugadoresPorNombre(unJuego.getJugadores());
            generarPDF(unJuego.getJugadores());
            System.out.println("El archivo PDF se ha generado correctamente.");
        } catch (DocumentException | IOException e) {
            System.out.println("Ha ocurrido un error al generar el archivo PDF.");
        }
        System.out.println("**** GRACIAS POR JUGAR ****");
        System.out.println("**** HASTA LA PROXIMA ****");
    }

    //menu de si ingreso jugadores o juego
    public int ingresarOJugar2(Juego unJuego) {
        int elegido = 0;
        if (unJuego.cuantosJugadoresHay() < 2) {
            System.out.println("""
                 Ingrese que desea
                 1 - Ingresar Jugador/es
                 2 - Pulse X para cerrar""");
            int[] valores = {1};
            elegido = eleccionRestringida(valores);
        } else {
            System.out.println("""
                 Ingrese que desea
                 1 - Ingresar Jugador/es
                 2 - Jugar!
                 3 - Pulse X para cerrar""");
            int[] valores = {1, 2};
            elegido = eleccionRestringida(valores);
        }

        return elegido;

    }

    //Cantidad de jugadores que voy a ingresar
    public int cuantosIngresan(Juego unJuego) {
        boolean esValido = false;
        int cuantos = 0;
        while (!esValido) {
            try {
                System.out.println("Cuántos jugadores desea ingresar?");
                cuantos = leer.nextInt();
                if (cuantos <= 0) {
                    throw new Exception("Error. Ingrese una cantidad"
                            + "válida de jugadores. Minimo dos para jugar.");
                }
                if (unJuego.cuantosJugadoresHay() < 2 && cuantos < 2) {
                    throw new Exception("Debe ingresar minomo dos "
                            + "jugadores");
                }
                esValido = true;
            } catch (InputMismatchException e) {
                System.out.println("Ingrese en formato numerico");
                leer.nextLine();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        leer.nextLine();
        return cuantos;
    }

    //Obtener nombre, edad y alias de cada jugador
    public void DatosDelJugador(Juego unJuego, int cuantos) {
        ArrayList<Jugador> listaJugadores = new ArrayList<Jugador>();
        for (int i = 0; i < cuantos; i++) {
            System.out.print("Nombre del jugador " + (i + 1) + ": ");
            String nombre = leer.nextLine();
            System.out.print("Edad del jugador " + (i + 1) + ": ");
            boolean esInt = false;
            int edad = 0;
            while (!esInt) {
                try {
                    if (!leer.hasNextInt()) {
                        leer.nextLine();
                        throw new Exception("Ingrese la edead en formato"
                                + " numerico");
                    } else {
                        edad = leer.nextInt();
                        esInt = true;
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    //edad = l
                }
            }
            //int edad = leer.nextInt();
            leer.nextLine();
            //unJuego.VerAlias();
            System.out.print("Alias del jugador " + (i + 1) + ": ");
            String alias = leer.nextLine();
            boolean repetido = false;

            while (!repetido) {
                try {
                    if (!unJuego.aliasRepetido(alias)) {
                        repetido = true;
                    } else {
                        throw new Exception("El alias ya esta en uso, por favor ingrese otro.");
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage() + "\n" + "Alias del jugador:");
                    alias = leer.nextLine();
                }
            }

            Jugador jugador = new Jugador(nombre, alias, edad);
            unJuego.agregarJugador(listaJugadores, jugador);

            if (i < cuantos - 1) {
                System.out.println("");
                System.out.println("Ingrese próximo jugador");
                System.out.println("");
            }

            System.out.println("");
        }
    }

    //Eligo la version del juego
    public void QueVersionJuego(Juego unJuego, String version) {
        Jugador[] quienesJuegan = this.Alias(unJuego);
        String ganador = "";
        if (version.equals(SIMPLE)) {
            jugarVersionSimple(unJuego, quienesJuegan);
        } else {
            jugarVersionFull(unJuego, quienesJuegan);
        }
    }

    //Funcion para asignarle un color a un jugador segun su alias
    public Jugador[] Alias(Juego unJuego) {
        System.out.println("Elija los alias según su número");
        System.out.println("Ingrese primero el que usara color rojo y luego "
                + "el de color azul \n");
        Jugador[] jugadores = new Jugador[2];
        boolean esValido = false;
        int cuantos = 0;
        String rojo = "\u001B[31m";
        String azul = "\u001B[34m";
        String color = "rojo";
        int opcion;
        unJuego.VerAlias();
        while (!esValido) {
            System.out.print("Ingresar número para jugador " + color + ": ");
            try {
                if (!leer.hasNextInt()) {
                    leer.nextLine();
                    throw new Exception("Ingrese las opciones en formato numerico");
                } else {
                    opcion = leer.nextInt();
                }
                if (opcion >= 1 && opcion <= unJuego.cuantosJugadoresHay()) {
                    String alias = unJuego.retornarAlias(opcion - 1);

                    if (unJuego.queJugadorEs(alias) == null) {
                        System.out.println("Jugador no registrado");
                        System.out.println("Desea ingresarlo o elegir uno nuevamente");
                        System.out.println("""
                                       1 - Ingresarlo
                                       2 - Elegir alias nuevamente""");
                        int[] valores = {1, 2};
                        int eleccion = this.eleccionRestringida(valores);
                        if (eleccion == 1) {
                            this.DatosDelJugador(unJuego, 1);
                        }

                        System.out.println("Vuelva a ingresar alias");

                    } else {
                        jugadores[cuantos] = unJuego.queJugadorEs(alias);
                        unJuego.aumentarVecesJugadas(alias);
                        if (cuantos == 0) {
                            jugadores[cuantos].setColor(rojo);
                        } else {
                            jugadores[cuantos].setColor(azul);
                        }
                        cuantos++;
                        color = "azul";
                    }

                    if (cuantos == 2) {
                        esValido = true;
                    }
                }
            } catch (Exception e) {
                System.out.println("Ingreselo en formato numerico");
            }
        }

        return jugadores;
    }

    //Funcion para comenzar a jugar version simple
    public void jugarVersionSimple(Juego unJuego, Jugador[] jugadores) {
        int turno = 0;
        String rojo = "\u001B[31m";
        String color = jugadores[turno].getColor();
        boolean faltanGatos = false;
        boolean[] noHayMasGatos = new boolean[2];
        noHayMasGatos[0] = false;
        noHayMasGatos[1] = false;
        boolean ganoRojo = unJuego.hayGanador(ROJO);
        boolean ganoAzul = unJuego.hayGanador(AZUL);
        boolean quiereSalir = false;
        unJuego.imprimirTablero();
        System.out.println("");
        while (!ganoRojo && !ganoAzul && !faltanGatos && !quiereSalir) {
            int[] dondeInsertar = this.queGatoInsertar(unJuego,
                    jugadores[turno].getAlias());
            if (dondeInsertar[0] != TERMINAR_MIN && !noHayMasGatos[turno]) {

                unJuego.insertarGato(color, dondeInsertar);
                if (unJuego.cuantosGatosQuedan(turno) == 0) {
                    noHayMasGatos[turno] = true;
                }

                if (turno == 0) {
                    turno = 1;
                } else {
                    turno = 0;
                }

                ganoRojo = unJuego.hayGanador(ROJO);
                ganoAzul = unJuego.hayGanador(AZUL);

                if (!ganoAzul && !ganoRojo) {
                    color = jugadores[turno].getColor();
                }

                if (noHayMasGatos[0] && !ganoRojo) {
                    faltanGatos = true;
                    ganoAzul = true;
                    System.out.println("Rojo, has perdidio. Te has quedado"
                            + " sin gatos");
                }

                if (noHayMasGatos[1] && !ganoAzul) {
                    faltanGatos = true;
                    ganoRojo = true;
                    System.out.println("Azul, has perdidio. Te has quedado"
                            + " sin gatos");
                }

                unJuego.imprimirTablero();
                unJuego.imprimirCantidadEnCajas();
                System.out.println("Gatos rojos en tablero: "
                        + (8 - unJuego.cuantosGatosQuedan(0)));
                System.out.println("Gatos azules en tablero: "
                        + (8 - unJuego.cuantosGatosQuedan(1)));

            } else {
                quiereSalir = true;
                if (turno == 0) {
                    ganoAzul = true;
                } else {
                    ganoRojo = true;
                }
            }
        }

        String quienGano = "";
        if (ganoRojo) {
            quienGano = jugadores[0].toString();
        } else if (ganoAzul) {
            quienGano = jugadores[1].toString();
        } else {
            quienGano = "nadie, hubo empate. Más suerte la próxima.";
        }

        System.out.println("El ganador de la partida fue "
                + quienGano);
    }

    //Jugar a versión full
    public void jugarVersionFull(Juego unJuego, Jugador[] jugadores) {
        int turno = 0;
        String rojo = "\u001B[31m";
        String color = jugadores[turno].getColor();
        boolean ganoRojo = unJuego.hayGanador(ROJO);
        boolean ganoAzul = unJuego.hayGanador(AZUL);
        boolean quiereSalir = false;
        unJuego.imprimirTablero();
        while (!ganoRojo && !ganoAzul && !quiereSalir) {
            int eleccion = this.opciones(unJuego, color);
            int[] dondeInsertar;
            switch (eleccion) {
                case INGRESAR_GATO:
                    dondeInsertar = this.queGatoInsertarFull(unJuego,
                            jugadores[turno].getAlias(), color);
                    String tamano = this.queTamano(dondeInsertar[2]);
                    unJuego.insertarGatoFull(color, dondeInsertar,
                            tamano);
                    break;
                case REGLA_ADICIONAL_UNO:
                    this.reglaAdicionalUno(unJuego, color);
                    break;
                case REGLA_ADICIONAL_DOS:
                    this.reglaAdicionalDos(unJuego, color);
                    break;
                case TERMINAR_MIN, TERMINAR_MAY:
                    quiereSalir = true;
                    break;
            }
            if (!(eleccion == TERMINAR_MIN) && !(eleccion == TERMINAR_MAY)) {

                CajaFull[] lasCajas = unJuego.getCajaFull();
                CajaFull caja = lasCajas[turno];
                int gatitosEnCaja = caja.getCantidadGatosChicos();
                ganoRojo = unJuego.ganadorFull(ROJO);
                ganoAzul = unJuego.ganadorFull(AZUL);

                if (gatitosEnCaja >= 1) {

                    if (turno == 0) {
                        turno = 1;
                    } else {
                        turno = 0;
                    }

                    boolean ganaGatosRojos = unJuego.ganaGrandes(ROJO);
                    boolean ganaGatosAzules = unJuego.ganaGrandes(AZUL);

                    if (ganaGatosRojos) {
                        System.out.println("Rojo, ganaste tres gatos grandes");
                    }

                    if (ganaGatosAzules) {
                        System.out.println("Azul, ganaste tres gatos grandes");
                    }

                    if (!ganoRojo && !ganoAzul) {
                        color = jugadores[turno].getColor();
                    }

                    unJuego.imprimirTablero();
                    unJuego.imprimirCantidadEnCajasFull();
                    unJuego.imprimirGatosEnTablero();
                } else {
                    if (unJuego.ganaGrandes(ROJO)) {
                        System.out.println("Rojo, ganaste tres gatos grandes");
                    } else if (unJuego.ganaGrandes(AZUL)) {
                        System.out.println("Azul, ganaste tres gatos grandes");
                    } else {
                        if (turno == 0) {
                            ganoAzul = true;
                        } else {
                            ganoRojo = true;
                        }
                    }
                }

            } else {
                generarContenido(unJuego.getJugadores());
                quiereSalir = true;
                if (turno == 0) {
                    ganoAzul = true;
                } else {
                    ganoRojo = true;
                }
            }
        }
        String quienGano = "";
        if (ganoRojo) {
            quienGano = jugadores[0].toString();
        } else if (ganoAzul) {
            quienGano = jugadores[1].toString();
        } else {
            quienGano = "nadie, hubo empate. Más suerte la próxima.";
        }

        System.out.println("El ganador de la partida fue "
                + quienGano);

    }

    public void quienGano(int actual, boolean ganoRojo, boolean ganoAzul) {
        if (actual == 0) {
            ganoAzul = true;
        } else {
            ganoRojo = true;
        }
    }

    //Obtener datos sobre que tablero desea el usuario elegir
    public String[] queTablero() {
        int opcion = 0;
        boolean opcionValida = false;
        char caracter = ' ';
        String color = "\u001B[30m";

        while (!opcionValida) {
            try {
                System.out.println("Elige un tablero:");
                System.out.println("1. Verde con simbolo #");
                System.out.println("2. Morado con simbolo O");
                System.out.println("3. Amarillo con simbolo  ");
                System.out.println("4. Cyan con simbolo x");
                System.out.println("5. Morado con fondo cyan con simbolo @");
                System.out.print("Elija una opcion: ");

                opcion = leer.nextInt();

                if (opcion < 1 || opcion > 5) {
                    throw new Exception("Opción inválida");
                }

                opcionValida = true;
            } catch (InputMismatchException e) {
                leer.nextLine(); // Limpiar el buffer del scanner
                System.out.println("Ingrese valores numericos");
            } catch (Exception e) {
                leer.nextLine(); // Limpiar el buffer del scanner
                System.out.println("Error: " + e.getMessage() + ". Por favor, "
                        + "elija una opcion válida.");
            }
        }

        // Realizar acción correspondiente a la opción elegida
        switch (opcion) {
            case 1 -> {
                System.out.println("Ha elegido la opción 1.");
                caracter = '#';
                // color verde
                color = "\u001B[32m";
            }
            case 2 -> {
                System.out.println("Ha elegido la opción 2.");
                caracter = 'O';
                // color morado
                color = "\u001B[35m";
            }
            case 3 -> {
                System.out.println("Ha elegido la opción 3.");
                caracter = '?';
                // color amarillo
                color = "\u001B[33m";
            }
            case 4 -> {
                System.out.println("Ha elegido la opción 4.");
                // color cyan
                caracter = 'x';
                color = "\u001B[36m";
            }
            case 5 -> {
                System.out.println("Ha elegido la opción 5.");
                caracter = '@';
                // color morado con fondo cyan
                color = "\u001B[35m\u001B[46m";
            }
            default -> {
            }
        }

        leer.nextLine();

        String caracterEnString = "" + caracter;
        String[] valoresParaTablero = {color, caracterEnString};
        System.out.println("");

        return valoresParaTablero;
    }

    //Insertar gato en version simple
    public int[] queGatoInsertar(Juego unJuego, String alias) {
        boolean esValida = false;
        String pos = "";
        String[] posicion;
        int dondeIngresar[] = new int[2];
        int fila = 0;
        int columna = 0;
        String rojo = "\u001B[31m";
        Scanner in = new Scanner(System.in);
        System.out.println(alias + ", eliga una posición para ingresar un gato");
        System.out.println("Ingresela con el formato FilaColumna, ejemplo: A1");
        System.out.println("Si desea salir ingrese X");
        while (!esValida) {
            try {
                pos = in.nextLine();
                if (pos.equalsIgnoreCase("x")) {
                    dondeIngresar[0] = TERMINAR_MIN;
                    esValida = true;
                } else {
                    posicion = pos.split("");
                    columna = Integer.parseInt(posicion[1]);
                    fila = obtenerFila(posicion[0]);
                    if (fila > 6 || fila < 1 || columna > 6 || columna < 1
                            || !unJuego.getTablero().esVacio(fila - 1, columna - 1)) {
                        throw new Exception("Opción invalida");
                    }
                }
                esValida = true;
            } catch (NumberFormatException e) {
                System.out.println("Ingrese una posición válida o X para salir");
            } catch (Exception e) {
                //in.nextLine(); // Limpiar el buffer del scanner
                System.out.println("Error: " + e.getMessage() + ". Por favor, "
                        + "elija una opción válida.");
            }

        }

        if (!pos.equals("x")) {
            dondeIngresar[0] = fila;
            dondeIngresar[1] = columna;
        }

        return dondeIngresar;
    }

    //Ingresar gato en version full
    public int[] queGatoInsertarFull(Juego unJuego, String alias, String color) {
        boolean esValida = false;
        String pos = "";
        String[] posicion;
        int dondeIngresar[] = new int[3];
        int fila = 0;
        int columna = 0;
        String rojo = "\u001B[31m";
        Scanner in = new Scanner(System.in);
        System.out.println(alias + ", eliga una posición para ingresar un gato");
        System.out.println("Ingresela con el formato FilaColumnaGato, "
                + "ejemplo: A1g para gato chico o A1G para gato grande");

        //System.out.println("Si desea salir ingrese X");
        while (!esValida) {
            try {
                pos = in.nextLine();
                posicion = pos.split("");
                columna = Integer.parseInt(posicion[1]);
                String tamano = posicion[2];
                fila = this.obtenerFila(posicion[0]);

                if (fila > 6 || fila < 1 || columna > 6 || columna < 1
                        || !unJuego.getTablero().esVacio(fila - 1, columna - 1)) {
                    throw new Exception("Opcion invalida");
                }
                if (tamano.equals("G")
                        && unJuego.cantGatosGrandes(color) < 1) {

                    throw new Exception("No tiene gatos grandes."
                            + "Ingrese gatos pequeños");
                } else if (tamano.equals("G")
                        && unJuego.cantGatosGrandes(color) > 0) {
                    dondeIngresar[2] = 1;
                }
                if (tamano.equals("g")
                        && unJuego.cantGatosChicos(color) < 1) {

                    throw new Exception("No tiene gatos grandes."
                            + "Ingrese gatos chicos");
                } else if (tamano.equals("g")
                        && unJuego.cantGatosChicos(color) > 0) {
                    dondeIngresar[2] = 0;
                }
                esValida = true;
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Formato invalido. Ingreselo otra vez");
                //in.nextLine();
            } catch (NumberFormatException e) {
                System.out.println("Ingrese una posición válida o X para salir");
            } catch (Exception e) {
                //in.nextLine(); // Limpiar el buffer del scanner
                System.out.println("Error: " + e.getMessage() + ". Por favor, "
                        + "elija una opcion válida.");
            }

        }

        dondeIngresar[0] = fila;
        dondeIngresar[1] = columna;
        return dondeIngresar;
    }

    /*Diferentes opciones desplegadas al usuario segun factores como cantidad de
    gatos en caja y en tablero. */
    public int opciones(Juego unJuego, String unColor) {
        System.out.println("Que desea hacer?");
        int color = 0;
        int elegido = 0;
        boolean puedeUsarRA1 = false;
        boolean puedeUsarRA2 = false;

        if (!unColor.equals(ROJO)) {
            color = 1;
        }

        int gatosChicosEnTablero = unJuego.getGatosEnTableroFull()[color][0];
        int gatosGrandesEnTablero = unJuego.getGatosEnTableroFull()[color][1];
        int sumaGatos = gatosChicosEnTablero + gatosGrandesEnTablero;
        CajaFull[] lasCajas = unJuego.getCajaFull();
        CajaFull caja = lasCajas[color];
        int gatitosEnCaja = caja.getCantidadGatosChicos();
        int gatosEnCaja = caja.getCantidadGatosGrandes();
        boolean tieneGatosParaInsertar = true;
        boolean aunTieneGatitosEnCaja = true;

        if ((gatitosEnCaja + gatosEnCaja) < 1) {
            tieneGatosParaInsertar = false;
        }

        if (gatitosEnCaja < 1) {
            aunTieneGatitosEnCaja = false;
        }

        if (gatosChicosEnTablero > 0 && sumaGatos >= 8) {
            puedeUsarRA1 = true;
        }

        if (gatosGrandesEnTablero > 0 && sumaGatos >= 8) {
            puedeUsarRA2 = true;
        }

        if ((puedeUsarRA1 && puedeUsarRA2 && tieneGatosParaInsertar)
                && aunTieneGatitosEnCaja) {
            System.out.println("""
                           1 - Insertar gato
                           2 - Regla adicional 1
                           3 - Regla adicional 2
                           4 - Salir pulsando x""");
            int[] valores = {1, 2, 3};
            elegido = eleccionRestringida(valores);
        } else if ((puedeUsarRA1 && puedeUsarRA2 && !tieneGatosParaInsertar)
                && aunTieneGatitosEnCaja) {
            System.out.println("""
                           2 - Regla adicional 1
                           3 - Regla adicional 2
                           4 - Salir pulsando x""");
            int[] valores = {2, 3};
            elegido = eleccionRestringida(valores);
        } else if ((puedeUsarRA1 && tieneGatosParaInsertar && !puedeUsarRA2)
                && aunTieneGatitosEnCaja) {
            System.out.println("""
                           1 - Insertar gato
                           2 - Regla adicional 1
                           4 - Salir pulsando x""");
            int[] valores = {1, 2};
            elegido = eleccionRestringida(valores);
        } else if ((puedeUsarRA2 && tieneGatosParaInsertar && !puedeUsarRA1)
                && aunTieneGatitosEnCaja) {
            System.out.println("""
                           1 - Insertar gato
                           3 - Regla adicional 2
                           4 - Salir pulsando x""");
            int[] valores = {1, 3};
            elegido = eleccionRestringida(valores);
        } else if ((tieneGatosParaInsertar && !puedeUsarRA1 && !puedeUsarRA2)
                && aunTieneGatitosEnCaja) {
            System.out.println("""
                           1 - Insertar gato
                           4 - Salir pulsando x""");
            int[] valores = {1};
            elegido = eleccionRestringida(valores);
        } else if ((!tieneGatosParaInsertar && puedeUsarRA1 && !puedeUsarRA2)
                && aunTieneGatitosEnCaja) {
            System.out.println("""
                           2 - Regla adicional 1
                           4 - Salir pulsando x""");
            int[] valores = {2};
            elegido = eleccionRestringida(valores);
        } else if ((!tieneGatosParaInsertar && !puedeUsarRA1 && puedeUsarRA2)
                && aunTieneGatitosEnCaja) {
            System.out.println("""
                           3 - Regla adicional 2
                           4 - Salir pulsando x""");
            int[] valores = {3};
            elegido = eleccionRestringida(valores);
        } else {
            System.out.println("Perdiste " + unJuego.retornarAlias(color)
                    + " te quedaste sin gatitos");
            elegido = 88;
        }

        return elegido;
    }

    //Función que permite obtener la elección del usuario
    public int eleccionRestringida(int[] posiblesValores) {
        boolean esValido = false;
        int queJuego = 0;
        String seTermino;
        //Scanner leer = new Scanner(System.in);
        //leer.nextLine();
        while (!esValido) {
            try {
                seTermino = leer.nextLine();
                if (!seTermino.equalsIgnoreCase("x")) {
                    queJuego = Integer.parseInt(seTermino);
                    boolean opcionCorrecta = false;
                    for (int i = 0; i < posiblesValores.length; i++) {
                        if (posiblesValores[i] == queJuego) {
                            opcionCorrecta = true;
                            esValido = true;
                        }
                    }
                    if (!opcionCorrecta) {
                        throw new Exception("Ingrese valores correctos");
                    }
                } else {
                    if (seTermino.equals("X")) {
                        queJuego = 120;
                    } else {
                        queJuego = 88;
                    }
                    esValido = true;
                }
            } catch (InputMismatchException e) {
                System.out.println("Por favor ingrese solo numeros o X para "
                        + "salir");

            } catch (Exception e) {
                //System.out.println(e.getMessage());
                System.out.println("Ingrese valores correctos");
            }
        }

        //leer.nextLine();
        //leer.close();
        return queJuego;
    }

    //Obtener tamaño de gatoFull
    public String queTamano(int unTamano) {
        String tamano = "";
        if (unTamano == 1) {
            tamano = "Grande";
        } else {
            tamano = "Chico";
        }

        return tamano;
    }

    //Metodo para pedir datos necesario para el uso de reglas.
    public int[] pedirReglaAdicional(Juego unJuego, int reglaAUsar, String color) {
        int[] reglaPosicion = new int[3];
        int[] posiciones;
        int regla = 0;
        String queRegla = "";
        String[] datosDeRegla = queRegla.split("");
        int fila = 0;
        int columna = 0;
        Scanner in = new Scanner(System.in);
        boolean esValida = false;
        while (!esValida) {
            try {
                System.out.println("Ingrese la regla adicional con el siguiente"
                        + " formato. RAXFilaColumna. En lugar de x ponga 1 o 2 "
                        + "dependiendo que regla quiera y en FilaColumna la fila"
                        + " y columna del gato");
                queRegla = in.nextLine();
                datosDeRegla = queRegla.split("");
                columna = Integer.parseInt(datosDeRegla[4]) - 1;
                regla = Integer.parseInt(datosDeRegla[2]);
                fila = this.obtenerFila(datosDeRegla[3]) - 1;

                if (regla != reglaAUsar) {
                    throw new Exception("Esa no fue la regla"
                            + " seleccionada");
                }

                if (!datosDeRegla[0].equalsIgnoreCase("R")
                        || !datosDeRegla[1].equalsIgnoreCase("A")) {
                    throw new Exception("Formato de regla invalido.");
                }

                Tablero elTablero = unJuego.getTablero();
                GatoFull gato = ((GatoFull) elTablero.getTablero()[fila][columna]);

                if (!elTablero.esVacio(fila, columna)) {
                    String colorGato = gato.getColor();
                    if (!colorGato.equals(color)) {
                        throw new Exception("Opcion invalida");
                    }
                }

                String tamano = gato.getTamano();

                if (fila > 5 || fila < 0 || columna > 5 || columna < 0) {
                    throw new Exception("Opción invalida");
                }

                if (reglaAUsar == 1) {
                    if (tamano.equals("Grande")) {
                        throw new Exception("Opción invalida");
                    }
                } else {
                    if (tamano.equals("Chico")) {
                        throw new Exception("Opción invalida");
                    }
                }

                esValida = true;
            } catch (NumberFormatException e) {
                System.out.println("Ingrese regla en formato de numero");
            } catch (Exception e) {
                //in.nextLine(); // Limpiar el buffer del scanner
                System.out.println("Error: " + e.getMessage() + ". Por favor, "
                        + "elija una opción válida o ingresela en el formato"
                        + "correcto.");
            }

        }

        reglaPosicion[0] = regla;
        reglaPosicion[1] = fila;
        reglaPosicion[2] = columna;

        return reglaPosicion;

    }

    public void reglasAdicionales(Juego unJuego, String color) {
        int[] regla = pedirReglaAdicional(unJuego, 1, color);
        int fila = regla[1];
        int columna = regla[2];
        int cual = 0;
        if (!color.equals(ROJO)) {
            cual = 1;
        }
        CajaFull caja = (CajaFull) unJuego.getCaja()[cual];
        int cantgatosCTablero = unJuego.getGatosEnTableroFull()[cual][0];
        int cantgatosGTablero = unJuego.getGatosEnTableroFull()[cual][1];
        if (regla[0] == REGLA_ADICIONAL_UNO && cantgatosCTablero > 1) {
            unJuego.reglaAdicionalUno(fila, columna, color);
        } else if (cantgatosGTablero > 1) {
            unJuego.reglaAdicionalDos(fila, columna, color);
        } else {
            System.out.println("Necesita 8 gatos en la colcha para usar"
                    + "las reglas");
        }
    }

    public void reglaAdicionalUno(Juego unJuego, String color) {
        int[] regla = pedirReglaAdicional(unJuego, 1, color);
        int fila = regla[1];
        int columna = regla[2];
        unJuego.reglaAdicionalUno(fila, columna, color);
    }

    public void reglaAdicionalDos(Juego unJuego, String color) {
        int[] regla = pedirReglaAdicional(unJuego, 2, color);
        int fila = regla[1];
        int columna = regla[2];
        unJuego.reglaAdicionalDos(fila, columna, color);
    }

    public int obtenerFila(String fila) {
        int filaNumerica = 0;
        switch (fila) {
            case "A", "a":
                filaNumerica = 1;
                break;
            case "B", "b":
                filaNumerica = 2;
                break;
            case "C", "c":
                filaNumerica = 3;
                break;
            case "D", "d":
                filaNumerica = 4;
                break;
            case "E", "e":
                filaNumerica = 5;
                break;
            case "F", "f":
                filaNumerica = 6;
                break;
            default:
                filaNumerica = 7;
                break;
        }

        return filaNumerica;
    }

}
