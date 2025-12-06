package batalla.main;

import batalla.vista.PantallaPrincipal;
import batalla.controlador.ControladorPrincipal;

/**
 * Clase principal del programa
 * Punto de entrada de la aplicación
 */
public class Desafio4Carranza {
    public static void main(String[] args) {
        // Iniciar la aplicación en el hilo de eventos de Swing
        javax.swing.SwingUtilities.invokeLater(() -> {
            PantallaPrincipal vista = new PantallaPrincipal();
            ControladorPrincipal controlador = new ControladorPrincipal(vista);
            controlador.iniciar();
        });
    }
}

