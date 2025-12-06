package batalla.modelo;

/**
 * Arma del villano: Hoz Oxidada
 */
public class HozOxidada extends Arma {
    public HozOxidada() {
        super("Hoz Oxidada", 12);
    }

    @Override
    public void usarEfectoEspecial(Personaje objetivo) {
        System.out.println("La Hoz Oxidada infecta las heridas con óxido maligno!");
        objetivo.setVida(objetivo.getVida() - 5);
        System.out.println(objetivo.getNombre() + " sufre 5 puntos de daño por infección!");
    }
}

