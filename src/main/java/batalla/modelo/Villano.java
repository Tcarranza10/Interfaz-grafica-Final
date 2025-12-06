package batalla.modelo;

/**
 * Clase que representa un villano en el juego
 */
public class Villano extends Personaje {
    private int turnosLeviatan = 0;
    private boolean leviatanInvocado = false;
    
    public Villano(String nombre, String apodo, int vida, int fuerza, int defensa, int bendiciones) {
        super(nombre, apodo, "Villano", vida, fuerza, defensa, bendiciones);
    }
    
    @Override
    public void invocarArma() {
        if (this.arma == null) {
            BendicionDelVacio bendicion = new BendicionDelVacio();
            this.arma = bendicion.invocarArma(this.bendiciones);
            
            if (this.arma != null) {
                System.out.println(this.nombre + " invoca: " + this.arma.getNombre() + "!");
                incrementarArmaInvocada();
            } else {
                System.out.println(this.nombre + " no tiene suficientes maldiciones para invocar un arma.");
            }
        } else {
            System.out.println(this.nombre + " ya tiene un arma: " + this.arma.getNombre());
        }
    }
    
    @Override
    public void decidirAccion(Personaje enemigo) {
        if (leviatanInvocado && turnosLeviatan < 3) {
            continuarInvocacionLeviatan();
            return;
        }
        
        if (leviatanInvocado && turnosLeviatan >= 3) {
            ejecutarLeviatan(enemigo);
            return;
        }
        
        if (this.arma == null && this.bendiciones >= 30) {
            invocarArma();
        } else {
            atacar(enemigo);
        }
    }
    
    /**
     * Ataque supremo del villano: "Leviatán del Vacío"
     */
    public boolean invocarLeviatan() {
        try {
            if (this.bendiciones >= 100 && !leviatanInvocado) {
                leviatanInvocado = true;
                turnosLeviatan = 1;
                
                System.out.println("INVOCACIÓN DEL LEVIATÁN DEL VACÍO!!!");
                System.out.println(this.nombre + " comienza a canalizar las fuerzas del abismo!");
                System.out.println("El Leviatán se está materializando... (Turno 1/3)");
                System.out.println("Las aguas se vuelven turbulentas y la oscuridad se intensifica...");
                
                this.bendiciones = 0;
                incrementarAtaqueSupremo();
                System.out.println(this.nombre + " ha agotado todas sus maldiciones del vacío!");
                
                return true;
            } else if (leviatanInvocado) {
                System.out.println(this.nombre + " ya está invocando al Leviatán del Vacío!");
                return false;
            } else {
                System.out.println(this.nombre + " no tiene suficientes maldiciones para invocar al Leviatán.");
                System.out.println("Maldiciones necesarias: 100% | Maldiciones actuales: " + this.bendiciones + "%");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error durante la invocación del Leviatán: " + e.getMessage());
            return false;
        }
    }
    
    private void continuarInvocacionLeviatan() {
        turnosLeviatan++;
        
        if (turnosLeviatan == 2) {
            System.out.println("El Leviatán se acerca! (Turno 2/3)");
            System.out.println("Las sombras se alargan y el viento aúlla con furia...");
        } else if (turnosLeviatan == 3) {
            System.out.println("EL LEVIATÁN ESTÁ AQUÍ!!! (Turno 3/3)");
            System.out.println("Una criatura gigantesca emerge de las profundidades del vacío!");
            System.out.println("Está listo para atacar en el próximo turno...");
        }
    }
    
    private void ejecutarLeviatan(Personaje enemigo) {
        try {
            if (enemigo == null) {
                System.out.println("Error: El Leviatán no puede atacar a un enemigo inexistente.");
                return;
            }
            
            System.out.println("EL LEVIATÁN DEL VACÍO ATACA!!!");
            System.out.println("La criatura gigantesca desata su furia sobre " + enemigo.getNombre() + "!");
            System.out.println("Olas gigantescas y tentáculos se abalanzan!");
            System.out.println("Daño infligido: " + enemigo.getVida() + " puntos!");
            
            try {
                enemigo.setVida(0);
            } catch (Exception e) {
                System.out.println("Error al aplicar daño del Leviatán: " + e.getMessage());
            }
            
            System.out.println(enemigo.getNombre() + " ha sido derrotado por el poder del Leviatán!");
            System.out.println("El Leviatán regresa a las profundidades del vacío...");
            
            leviatanInvocado = false;
            turnosLeviatan = 0;
        } catch (Exception e) {
            System.out.println("Error durante la ejecución del Leviatán: " + e.getMessage());
            leviatanInvocado = false;
            turnosLeviatan = 0;
        }
    }
    
    public boolean isLeviatanInvocado() { 
        return leviatanInvocado; 
    }
    
    public int getTurnosLeviatan() { 
        return turnosLeviatan; 
    }
}

