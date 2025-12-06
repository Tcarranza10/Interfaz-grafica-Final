package batalla.modelo;

import java.util.Random;

/**
 * Clase abstracta que representa un personaje en el juego
 */
public abstract class Personaje {
    protected int id;
    protected String nombre;
    protected String apodo;
    protected String tipo;
    protected int vida;
    protected int vidaMaxima;
    protected int fuerza;
    protected int defensa;
    protected Arma arma;
    protected int bendiciones;
    protected int bendicionesIniciales; // Guardar bendiciones iniciales
    protected int derrotas = 0;
    protected int victorias = 0;
    protected int ataquesSupremosUsados = 0;
    protected int armasInvocadas = 0;
    protected Random random = new Random();
    
    public Personaje(String nombre, String apodo, String tipo, int vida, int fuerza, int defensa, int bendiciones) {
        this.nombre = nombre;
        this.apodo = apodo;
        this.tipo = tipo;
        this.vida = vida;
        this.vidaMaxima = vida;
        this.fuerza = fuerza;
        this.defensa = defensa;
        this.bendiciones = bendiciones;
        this.bendicionesIniciales = bendiciones; // Guardar valor inicial
        this.arma = null;
    }
    
    public boolean estaVivo() {
        return this.vida > 0;
    }
    
    @Override
    public String toString() {
        String armaInfo = (arma != null) ? " | Arma: " + arma.getNombre() : " | Sin arma";
        return "[ " + nombre + " | Vida: " + vida + " | Fuerza: " + fuerza + 
               " | Defensa: " + defensa + " | Bendiciones: " + bendiciones + "%" + armaInfo + " ]";
    }
    
    public int getId() {
        return id;
    }
    
    public String getNombre() { 
        return nombre; 
    }
    
    public String getApodo() {
        return apodo;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public int getVida() { 
        return vida; 
    }
    
    public int getVidaMaxima() {
        return vidaMaxima;
    }
    
    public int getDerrotas() {
        return derrotas;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVida(int vida) { 
        this.vida = vida; 
    }
    
    public int getFuerza() {
        return fuerza;
    }
    
    public int getDefensa() {
        return defensa;
    }
    
    
    public int getBendiciones() { 
        return bendiciones; 
    }
    
    public void setDerrotas(int derrotas) {
        this.derrotas = derrotas;
    }
    
    public void setBendiciones(int bendiciones) { 
        this.bendiciones = Math.max(0, Math.min(100, bendiciones)); 
    }
    
    public int getBendicionesIniciales() {
        return bendicionesIniciales;
    }
    
    /**
     * Restaura las estadísticas iniciales del personaje
    */
   public void restaurarEstadisticasIniciales() {
       this.vida = this.vidaMaxima;
        this.bendiciones = this.bendicionesIniciales;
        this.arma = null;
    }
    
    public Arma getArma() { 
        return arma; 
    }
    
    public void setArma(Arma arma) { 
        this.arma = arma; 
    }
    
    public int getVictorias() {
        return victorias;
    }
    
    public void incrementarVictoria() {
        victorias++;
    }

    public void incrementarDerrota() {
        derrotas++;
    }
    public int getAtaquesSupremosUsados() {
        return ataquesSupremosUsados;
    }
    
    public void setVictorias(int victorias) {
        this.victorias = victorias;
    }

    public void setAtaquesSupremosUsados(int ataquesSupremosUsados) {
        this.ataquesSupremosUsados = ataquesSupremosUsados;
    }

    public void setArmasInvocadas(int armasInvocadas) {
        this.armasInvocadas = armasInvocadas;
    }

    public void incrementarAtaqueSupremo() {
        ataquesSupremosUsados++;
    }
    
    public int getArmasInvocadas() {
        return armasInvocadas;
    }
    
    public void incrementarArmaInvocada() {
        armasInvocadas++;
    }
    
    public int atacar(Personaje enemigo) {
        try {
            if (enemigo == null) {
                System.out.println("Error: No se puede atacar a un enemigo inexistente.");
                return 0;
            }
            
            boolean critico = random.nextInt(100) < 20;
            boolean parry = random.nextInt(100) < 10;
            
            int dmg = this.fuerza;
            if (critico) {
                dmg *= 2;
                System.out.println(getNombre() + " hizo crítico a " + enemigo.nombre);
            }
            if (parry) {
                System.out.println(enemigo.getNombre() + " hace parry, evita el ataque!");
                return 0;
            }
            
            if (this.arma != null) {
                try {
                    dmg += this.arma.getDanioExtra();
                    System.out.println(this.nombre + " ataca con " + this.arma.getNombre() + "!");
                } catch (Exception e) {
                    System.out.println("Error al usar el arma: " + e.getMessage());
                    System.out.println(this.nombre + " ataca sin arma!");
                }
            }
            
            int danioFinal = Math.max(0, dmg - enemigo.defensa);
            enemigo.vida -= danioFinal;
            
            System.out.println(this.nombre + " causa " + danioFinal + " de daño a " + enemigo.nombre);
            
            this.bendiciones = Math.min(100, this.bendiciones + 10);
            System.out.println(this.nombre + " ha ganado 10% de poder! (Total: " + this.bendiciones + "%)");
            
            if (this.arma != null) {
                try {
                    this.arma.usarEfectoEspecial(enemigo);
                } catch (Exception e) {
                    System.out.println("Error al usar efecto especial del arma: " + e.getMessage());
                }
            }
            
            return danioFinal;
        } catch (Exception e) {
            System.out.println("Error durante el ataque: " + e.getMessage());
            return 0;
        }
    }
    
    public abstract void invocarArma();
    public abstract void decidirAccion(Personaje enemigo);
}

