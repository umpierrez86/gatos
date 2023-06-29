/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package obligatorio;
/**
 *
 * @author Luciano Umpierrez
 */
public class Gato {

    private String color;

    //getters y setters
    
    public String getColor() {
        return color;
    }

    public void setColor(String unColor) {
        this.color = unColor;
    }

    public Gato(String unColor) {
        this.setColor(unColor);
    }

    public void saltar(String[] posicion) {

    }

    @Override
    public String toString() {
        return this.getColor() + "g";
    }
}