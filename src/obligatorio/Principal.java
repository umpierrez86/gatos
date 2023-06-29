package obligatorio;

import java.util.*;

/**
 *
 * @author Luciano Umpierrez
 */
public class Principal {

    private static Scanner leer = new Scanner(System.in);

    public static void main(String[] args) {
        Menus menu = new Menus();
        menu.MenuBienvenida();
    }
}
