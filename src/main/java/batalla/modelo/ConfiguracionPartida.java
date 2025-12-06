package batalla.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que almacena la configuración de una partida
 */
public class ConfiguracionPartida implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int vidaInicialMin = 100;
    private int vidaInicialMax = 160;
    private int fuerzaInicialMin = 15;
    private int fuerzaInicialMax = 25;
    private int defensaInicialMin = 8;
    private int defensaInicialMax = 13;
    private int bendicionInicialMin = 30;
    private int bendicionInicialMax = 100;
    private int cantidadBatallas = 3;
    private boolean ataquesSupremosActivados = true;
    
    private List<Personaje> personajes = new ArrayList<>();
    
    public ConfiguracionPartida() {
    }
    
    // Getters y Setters
    public int getVidaInicialMin() { return vidaInicialMin; }
    public void setVidaInicialMin(int vidaInicialMin) { this.vidaInicialMin = vidaInicialMin; }
    
    public int getVidaInicialMax() { return vidaInicialMax; }
    public void setVidaInicialMax(int vidaInicialMax) { this.vidaInicialMax = vidaInicialMax; }
    
    public int getFuerzaInicialMin() { return fuerzaInicialMin; }
    public void setFuerzaInicialMin(int fuerzaInicialMin) { this.fuerzaInicialMin = fuerzaInicialMin; }
    
    public int getFuerzaInicialMax() { return fuerzaInicialMax; }
    public void setFuerzaInicialMax(int fuerzaInicialMax) { this.fuerzaInicialMax = fuerzaInicialMax; }
    
    public int getDefensaInicialMin() { return defensaInicialMin; }
    public void setDefensaInicialMin(int defensaInicialMin) { this.defensaInicialMin = defensaInicialMin; }
    
    public int getDefensaInicialMax() { return defensaInicialMax; }
    public void setDefensaInicialMax(int defensaInicialMax) { this.defensaInicialMax = defensaInicialMax; }
    
    public int getBendicionInicialMin() { return bendicionInicialMin; }
    public void setBendicionInicialMin(int bendicionInicialMin) { this.bendicionInicialMin = bendicionInicialMin; }
    
    public int getBendicionInicialMax() { return bendicionInicialMax; }
    public void setBendicionInicialMax(int bendicionInicialMax) { this.bendicionInicialMax = bendicionInicialMax; }
    
    public int getCantidadBatallas() { return cantidadBatallas; }
    public void setCantidadBatallas(int cantidadBatallas) { this.cantidadBatallas = cantidadBatallas; }
    
    public boolean isAtaquesSupremosActivados() { return ataquesSupremosActivados; }
    public void setAtaquesSupremosActivados(boolean ataquesSupremosActivados) { 
        this.ataquesSupremosActivados = ataquesSupremosActivados; 
    }
    
    public List<Personaje> getPersonajes() { return personajes; }
    public void setPersonajes(List<Personaje> personajes) { this.personajes = personajes; }
    
    public void agregarPersonaje(Personaje personaje) {
        personajes.add(personaje);
    }
    
    public void eliminarPersonaje(Personaje personaje) {
        personajes.remove(personaje);
    }
    
    public Personaje getHeroe() {
        return personajes.stream()
            .filter(p -> p instanceof Heroe)
            .findFirst()
            .orElse(null);
    }
    
    public Personaje getVillano() {
        return personajes.stream()
            .filter(p -> p instanceof Villano)
            .findFirst()
            .orElse(null);
    }
}

