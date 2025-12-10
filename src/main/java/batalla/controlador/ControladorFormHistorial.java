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

        // Obtener estadísticas desde la BD
        BatallaDAO batallaDAO = new BatallaDAO();
        batallaDAO.obtenerEstadisticasBatalla(info);

        // -------- COMBAT LOG --------
        // Intentar obtener el combat log guardado desde la BD
        java.util.List<String> eventos = batallaDAO.obtenerCombatLog(info.getId());
        
        String combatLog;
        if (eventos != null && !eventos.isEmpty()) {
            // Mostrar el combat log guardado
            combatLog = "=== COMBAT LOG ===\n\n";
            for (String evento : eventos) {
                combatLog += evento + "\n";
            }
        } else {
            // Si no hay combat log guardado, mostrar resumen básico
            combatLog = "=== COMBAT LOG (RESUMEN) ===\n\n";
            combatLog += info.getHeroeNombre() + " vs " + info.getVillanoNombre() + "\n";
            combatLog += "Ganador: " + info.getGanadorNombre() + "\n";
            combatLog += "Duración: " + info.getTurnos() + " turnos\n\n";
            combatLog += "Vida final del " + info.getHeroeNombre() + ": " + info.getHeroeVidaFinal() + "\n";
            combatLog += "Vida final del " + info.getVillanoNombre() + ": " + info.getVillanoVidaFinal() + "\n\n";
            combatLog += "(El log detallado no está disponible para esta batalla)";
        }
        
        vista.setCombatLog(combatLog);

        // -------- ESTADÍSTICAS BÁSICAS --------
        String mayorDanioStr = info.getMayorDanio() > 0 
            ? info.getMayorDanio() + " por " + info.getPersonajeMayorDanio()
            : "No registrado";
        vista.setMayorDanio(mayorDanioStr);
        
        vista.setBatallaMasLarga(info.getTurnos() + " turnos");
        
        String armasHeroeStr = info.getHeroeArmasInvocadas() > 0
            ? String.valueOf(info.getHeroeArmasInvocadas())
            : "No registrado";
        vista.setArmasHeroe(armasHeroeStr);
        
        String armasVillanoStr = info.getVillanoArmasInvocadas() > 0
            ? String.valueOf(info.getVillanoArmasInvocadas())
            : "No registrado";
        vista.setArmasVillano(armasVillanoStr);
        
        String supremosHeroeStr = info.getHeroeSupremosUsados() > 0
            ? String.valueOf(info.getHeroeSupremosUsados())
            : "No registrado";
        vista.setSupremosHeroe(supremosHeroeStr);
        
        String supremosVillanoStr = info.getVillanoSupremosUsados() > 0
            ? String.valueOf(info.getVillanoSupremosUsados())
            : "No registrado";
        vista.setSupremosVillano(supremosVillanoStr);
    }

    public void iniciar() {
        vista.setVisible(true);
    }
}
