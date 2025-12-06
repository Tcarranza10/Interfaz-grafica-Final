package batalla.modelo;

/**
 * Arma del héroe: Espada Sagrada
 */
public class EspadaSagrada extends Arma {
    public EspadaSagrada() {
        super("Espada Sagrada", 25);
    }

    @Override
    public void usarEfectoEspecial(Personaje objetivo) {
        System.out.println("¡La Espada Sagrada emite luz curativa!");
        System.out.println("El héroe recupera energía vital!");
    }
}

