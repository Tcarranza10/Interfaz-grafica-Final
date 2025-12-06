package batalla.modelo;

/**
 * Arma del héroe: Espada Simple
 */
public class EspadaSimple extends Arma {
    public EspadaSimple() {
        super("Espada Simple", 15);
    }

    @Override
    public void usarEfectoEspecial(Personaje objetivo) {
        System.out.println("La Espada Simple brilla con luz divina!");
    }
}

