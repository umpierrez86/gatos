/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package obligatorio;
import java.util.*;
import java.lang.Comparable;

/**
 *
 * @author Luciano Umpierrez
 */
public class Jugador implements Comparable<Jugador> {
    private int cuantasVecesJugo;
    private String nombre;
    private int edad;
    private String alias;
    private String color;

    //getters y setters
    
    public int getCuantasVecesJugo() {
        return cuantasVecesJugo;
    }

    public void setCuantasVecesJugo(int cuantasVecesJugo) {
        this.cuantasVecesJugo = cuantasVecesJugo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String unColor) {
        this.color = unColor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String Unnombre) {
        this.nombre = Unnombre;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int Unaedad) {
        this.edad = Unaedad;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String Unalias) {
        this.alias = Unalias;
    }

    //métodos
    
    public Jugador(String unNombre, String alias, int edad) {
        this.setNombre(unNombre);
        this.setAlias(alias);
        this.setEdad(edad);
        this.setCuantasVecesJugo(0);
    }

    public Jugador(String alias) {
        this.setAlias(alias);
    }
    
    @Override
    public int compareTo(Jugador jugador) {
        String nombre1 = this.getNombre().toLowerCase();
        String nombre2 = jugador.getNombre().toLowerCase();
        return nombre1.compareTo(nombre2);
    }
    
    @Override
    public String toString() {
        String jugador = this.getNombre()
                + " alias: " + this.getAlias();

        return jugador;
    }
    
    @Override
    public boolean equals(Object obj){
        Jugador jug = (Jugador)obj;
        return jug.getAlias().equals(this.getAlias());
    }
    


}
