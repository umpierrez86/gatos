/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package obligatorio;
/**
 *
 * @author Luciano Umpierrez 
 */
public class CajaFull extends Caja{
    
    private int cantidadGatosChicos;
    private int cantidadGatosGrandes;

    //getters y setters
    
    public int getCantidadGatosChicos() {
        return this.cantidadGatosChicos;
    }

    public void setCantidadGatosChicos(int unaCantidadGatosChicos) {
        this.cantidadGatosChicos = unaCantidadGatosChicos;
    }

    public int getCantidadGatosGrandes() {
        return cantidadGatosGrandes;
    }

    public void setCantidadGatosGrandes(int unaCantidadGatosGrandes) {
        this.cantidadGatosGrandes = unaCantidadGatosGrandes;
    }
    
    public CajaFull(){
        super();
        this.setCantidadGatosChicos(8);
        this.setCantidadGatosGrandes(0);  
        //this.setCantidadGatos(8);
    }
    
    
}
