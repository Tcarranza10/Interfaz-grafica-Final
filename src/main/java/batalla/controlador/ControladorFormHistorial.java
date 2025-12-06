package batalla.controlador;

import batalla.Conexion.BatallaDAO;
import batalla.vista.formHistorial;

public class ControladorFormHistorial {

    private final formHistorial vista;
    private final BatallaDAO.BatallaInfo info;

    public ControladorFormHistorial(formHistorial vista, BatallaDAO.BatallaInfo info) {
        this.vista = vista;
        this.info = info;

        configurarEventos();
        cargarDatos();
    }

    private void configurarEventos() {
        vista.getBtnCerrar().addActionListener(e -> vista.dispose());
    }

    private void cargarDatos() {

        // -------- ENCABEZADOS --------
        vista.setHeroeNombre(info.getHeroeNombre());
        vista.setVillanoNombre(info.getVillanoNombre());
        vista.setGanador(info.getGanadorNombre());
        vista.setTurnos(String.valueOf(info.getTurnos()));

        // -------- COMBAT LOG --------
        // NOTA: el combat log no está guardado en la base de datos
        vista.setCombatLog(
            "=== COMBAT LOG ===\n" +
            "(El combat log no está guardado en la base de datos.)\n" +
            "Si querés guardarlo, puedo ayudarte a crear la tabla eventos_batalla."
        );

        // -------- ESTADÍSTICAS BÁSICAS --------
        vista.setMayorDanio("N/A");
        vista.setBatallaMasLarga(info.getTurnos() + " turnos");
        vista.setArmasHeroe("N/A");
        vista.setArmasVillano("N/A");
        vista.setSupremosHeroe("N/A");
        vista.setSupremosVillano("N/A");
        vista.setWinrateHeroe("N/A");
        vista.setWinrateVillano("N/A");
    }

    public void iniciar() {
        vista.setVisible(true);
    }
}
