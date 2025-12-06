package batalla.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una partida guardada con todos sus datos
 */
public class PartidaGuardada implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String heroeNombre;
    private String heroeApodo;
    private String villanoNombre;
    private String villanoApodo;
    private int cantidadBatallas;
    private boolean ataquesSupremosActivados;
    private List<String> combatLog = new ArrayList<>();
    private List<EstadisticaBatalla> estadisticas = new ArrayList<>();
    
    public PartidaGuardada() {
    }
    
    // Getters y Setters
    public String getHeroeNombre() { return heroeNombre; }
    public void setHeroeNombre(String heroeNombre) { this.heroeNombre = heroeNombre; }
    
    public String getHeroeApodo() { return heroeApodo; }
    public void setHeroeApodo(String heroeApodo) { this.heroeApodo = heroeApodo; }
    
    public String getVillanoNombre() { return villanoNombre; }
    public void setVillanoNombre(String villanoNombre) { this.villanoNombre = villanoNombre; }
    
    public String getVillanoApodo() { return villanoApodo; }
    public void setVillanoApodo(String villanoApodo) { this.villanoApodo = villanoApodo; }
    
    public int getCantidadBatallas() { return cantidadBatallas; }
    public void setCantidadBatallas(int cantidadBatallas) { this.cantidadBatallas = cantidadBatallas; }
    
    public boolean isAtaquesSupremosActivados() { return ataquesSupremosActivados; }
    public void setAtaquesSupremosActivados(boolean ataquesSupremosActivados) { 
        this.ataquesSupremosActivados = ataquesSupremosActivados; 
    }
    
    public List<String> getCombatLog() { return combatLog; }
    public void setCombatLog(List<String> combatLog) { this.combatLog = combatLog; }
    
    public List<EstadisticaBatalla> getEstadisticas() { return estadisticas; }
    public void setEstadisticas(List<EstadisticaBatalla> estadisticas) { this.estadisticas = estadisticas; }
    
    public void agregarLog(String log) {
        combatLog.add(log);
    }
    
    /**
     * Clase interna para estadísticas de batalla
     */
    public static class EstadisticaBatalla implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private int numeroBatalla;
        private String ganador;
        private int turnos;
        private int mayorDanio;
        private String personajeMayorDanio;
        private int armasInvocadasHeroe;
        private int armasInvocadasVillano;
        private int ataquesSupremosHeroe;
        private int ataquesSupremosVillano;
        
        // Getters y Setters
        public int getNumeroBatalla() { return numeroBatalla; }
        public void setNumeroBatalla(int numeroBatalla) { this.numeroBatalla = numeroBatalla; }
        
        public String getGanador() { return ganador; }
        public void setGanador(String ganador) { this.ganador = ganador; }
        
        public int getTurnos() { return turnos; }
        public void setTurnos(int turnos) { this.turnos = turnos; }
        
        public int getMayorDanio() { return mayorDanio; }
        public void setMayorDanio(int mayorDanio) { this.mayorDanio = mayorDanio; }
        
        public String getPersonajeMayorDanio() { return personajeMayorDanio; }
        public void setPersonajeMayorDanio(String personajeMayorDanio) { this.personajeMayorDanio = personajeMayorDanio; }
        
        public int getArmasInvocadasHeroe() { return armasInvocadasHeroe; }
        public void setArmasInvocadasHeroe(int armasInvocadasHeroe) { this.armasInvocadasHeroe = armasInvocadasHeroe; }
        
        public int getArmasInvocadasVillano() { return armasInvocadasVillano; }
        public void setArmasInvocadasVillano(int armasInvocadasVillano) { this.armasInvocadasVillano = armasInvocadasVillano; }
        
        public int getAtaquesSupremosHeroe() { return ataquesSupremosHeroe; }
        public void setAtaquesSupremosHeroe(int ataquesSupremosHeroe) { this.ataquesSupremosHeroe = ataquesSupremosHeroe; }
        
        public int getAtaquesSupremosVillano() { return ataquesSupremosVillano; }
        public void setAtaquesSupremosVillano(int ataquesSupremosVillano) { this.ataquesSupremosVillano = ataquesSupremosVillano; }
    }
}

