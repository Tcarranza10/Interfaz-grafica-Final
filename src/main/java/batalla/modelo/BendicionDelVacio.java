package batalla.modelo;

/**
 * Clase que gestiona la invocación de armas del vacío para villanos
 */
public class BendicionDelVacio {
    public Arma invocarArma(int porcentajeMaldicion) {
        if (porcentajeMaldicion >= 80) {
            return new HozMortifera();
        } else if (porcentajeMaldicion >= 50) {
            return new HozVenenosa();
        } else if (porcentajeMaldicion >= 30) {
            return new HozOxidada();
        }
        return null;
    }
}

