package batalla.modelo;

/**
 * Arma del villano: Hoz Mortífera
 */
public class HozMortifera extends Arma {
    public HozMortifera() {
        super("Hoz Mortífera", 30);
    }

    @Override
    public void usarEfectoEspecial(Personaje objetivo) {
        System.out.println("LA HOZ MORTÍFERA DESATA MUERTE Y CAOS!!!");
        objetivo.setVida(objetivo.getVida() - 15);
        System.out.println(objetivo.getNombre() + " sufre 15 puntos de daño por veneno letal!");
        System.out.println("El villano absorbe energía vital del enemigo!");
    }
}

