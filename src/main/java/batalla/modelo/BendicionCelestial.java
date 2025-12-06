package batalla.modelo;

/**
 * Clase que gestiona la invocación de armas celestiales para héroes
 */
public class BendicionCelestial {
    public Arma invocarArma(int porcentajeBendicion) {
        if (porcentajeBendicion >= 80) {
            return new EspadaCelestial();
        } else if (porcentajeBendicion >= 50) {
            return new EspadaSagrada();
        } else if (porcentajeBendicion >= 30) {
            return new EspadaSimple();
        }
        return null;
    }
}

