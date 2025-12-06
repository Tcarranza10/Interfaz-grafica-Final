package batalla.controlador;

import batalla.vista.PantallaPrincipal;
import batalla.vista.PantallaCreacion;

/**
 * Controlador para la pantalla principal
 * Gestiona la navegación desde la pantalla principal
 */
public class ControladorPrincipal {
    private PantallaPrincipal vista;

    public ControladorPrincipal(PantallaPrincipal vista) {
        this.vista = vista;
        configurarEventos();
    }

    private void configurarEventos() {
        vista.getBtnIniciar().addActionListener(e -> abrirConfiguracion());
        vista.getBtnSalir().addActionListener(e -> System.exit(0));
        vista.getBtnHistorial().addActionListener(e -> abrirHistorial());
        vista.getBtnRanking().addActionListener(e -> abrirRanking());
    }
    
    private void abrirHistorial() {
        batalla.vista.PantallaHistorial pantallaHistorial = new batalla.vista.PantallaHistorial();
        batalla.controlador.ControladorHistorial controladorHistorial = new batalla.controlador.ControladorHistorial(pantallaHistorial);
        controladorHistorial.iniciar();
    }
    
    private void abrirRanking() {
        batalla.vista.PantallaRanking pantallaRanking = new batalla.vista.PantallaRanking();
        batalla.controlador.ControladorRanking controladorRanking = new batalla.controlador.ControladorRanking(pantallaRanking);
        controladorRanking.iniciar();
    }

    private void abrirConfiguracion() {
        PantallaCreacion pantallaCreacion = new PantallaCreacion();
        ControladorCreacion controladorCreacion = new ControladorCreacion(pantallaCreacion);
        controladorCreacion.iniciar();
        vista.dispose();
    }

    public void iniciar() {
        vista.setVisible(true);
    }
}

