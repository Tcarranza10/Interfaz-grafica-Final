package batalla.modelo;

/**
 * Clase abstracta que representa un arma en el juego
 */
public abstract class Arma {
    protected String nombre;
    protected int danioExtra;

    public Arma(String nombre, int danioExtra) {
        this.nombre = nombre;
        this.danioExtra = danioExtra;
    }

    public String getNombre() { 
        return nombre; 
    }
    
    public int getDanioExtra() { 
        return danioExtra; 
    }

    /**
     * Efecto especial del arma (veneno, cura, buff, etc.)
     */
    public abstract void usarEfectoEspecial(Personaje objetivo);
}

