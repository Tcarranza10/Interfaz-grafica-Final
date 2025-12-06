package batalla.modelo;

/**
 * Arma del villano: Hoz Venenosa
 */
public class HozVenenosa extends Arma {
    public HozVenenosa() {
        super("Hoz Venenosa", 20);
    }

    @Override
    public void usarEfectoEspecial(Personaje objetivo) {
        System.out.println("¡La Hoz Venenosa inyecta toxinas letales!");
        objetivo.setVida(objetivo.getVida() - 8);
        System.out.println(objetivo.getNombre() + " está ENVENENADO! Pierde 8 puntos de vida!");
    }
}

