package batalla.modelo;

/**
 * Arma del héroe: Espada Celestial
 */
public class EspadaCelestial extends Arma {
    public EspadaCelestial() {
        super("Espada Celestial", 35);
    }

    @Override
    public void usarEfectoEspecial(Personaje objetivo) {
        System.out.println("¡¡¡LA ESPADA CELESTIAL DESATA PODER DIVINO!!!");
        System.out.println("Efectos: Daño devastador + curación + bendición defensiva!");
    }
}

