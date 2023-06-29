/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package obligatorio;

/**
 *
 * @author Luciano Umpierrez
 */
public class GatoFull extends Gato {

    private String tamano;

    //getters y setters
    
    public String getTamano() {
        return this.tamano;
    }

    public void setTamano(String unTamano) {
        this.tamano = unTamano;
    }

    public GatoFull(String unTamano, String unColor) {
        //this.setColor(unColor);
        super(unColor);
        this.setTamano(unTamano);

    }

    @Override
    public String toString() {
        String gato = "";
        if (this.getTamano().equals("Grande")) {
            gato = this.getColor() + "G";
        } else {
            gato = this.getColor() + "g";
        }

        return gato;
    }

}
