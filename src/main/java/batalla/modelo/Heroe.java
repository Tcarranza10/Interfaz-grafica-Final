package batalla.modelo;

/**
 * Clase que representa un héroe en el juego
 */
public class Heroe extends Personaje {
    public Heroe(String nombre, String apodo, int vida, int fuerza, int defensa, int bendiciones) {
        super(nombre, apodo, "Heroe", vida, fuerza, defensa, bendiciones);
    }
   
    @Override
    public void invocarArma() {
        if (this.arma == null) {
            BendicionCelestial bendicion = new BendicionCelestial();
            this.arma = bendicion.invocarArma(this.bendiciones);
            
            if (this.arma != null) {
                System.out.println(this.nombre + " invoca: " + this.arma.getNombre() + "!");
                incrementarArmaInvocada();
            } else {
                System.out.println(this.nombre + " no tiene suficientes bendiciones para invocar un arma.");
            }
        } else {
            System.out.println(this.nombre + " ya tiene un arma: " + this.arma.getNombre());
        }
    }
    
    @Override
    public void decidirAccion(Personaje enemigo) {
        if (this.arma == null && this.bendiciones >= 30) {
            invocarArma();
        } else {
            atacar(enemigo);
        }
    }
    
    /**
     * Ataque supremo del héroe: "Castigo Bendito"
     */
    public boolean usarAtaqueSupremo(Personaje enemigo) {
        try {
            if (enemigo == null) {
                System.out.println("Error: No se puede usar ataque supremo contra un enemigo inexistente.");
                return false;
            }
            
            if (this.bendiciones >= 100) {
                int danioSupremo = (this.vida > 0) ? this.vida / 2 : 0;
                
                try {
                    enemigo.setVida(enemigo.getVida() - danioSupremo);
                } catch (Exception e) {
                    System.out.println("Error al aplicar daño: " + e.getMessage());
                    return false;
                }
                
                System.out.println("CASTIGO BENDITO!!!");
                System.out.println(this.nombre + " canaliza toda su energía divina!");
                System.out.println("El poder celestial desciende sobre " + enemigo.getNombre() + "!");
                System.out.println("Daño infligido: " + danioSupremo + " puntos!");
                System.out.println(enemigo.getNombre() + " ahora tiene " + enemigo.getVida() + " puntos de vida.");
                
                this.bendiciones = 0;
                incrementarAtaqueSupremo();
                System.out.println(this.nombre + " ha agotado todas sus bendiciones divinas!");
                
                return true;
            } else {
                System.out.println(this.nombre + " no tiene suficientes bendiciones para usar el ataque supremo.");
                System.out.println("Bendiciones necesarias: 100% | Bendiciones actuales: " + this.bendiciones + "%");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error durante el ataque supremo: " + e.getMessage());
            return false;
        }
    }
}

