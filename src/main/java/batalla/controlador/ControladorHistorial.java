package batalla.controlador;

import batalla.Conexion.BatallaDAO;
import batalla.vista.PantallaHistorial;
import batalla.vista.PantallaPrincipal;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ControladorHistorial {

    private final PantallaHistorial vista;
    private final BatallaDAO batallaDAO;

    public ControladorHistorial(PantallaHistorial vista) {
        this.vista = vista;
        this.batallaDAO = new BatallaDAO();
        inicializar();
    }

    private void inicializar() {
        cargarTabla();

        vista.getBtnVolver().addActionListener(e -> volver());
        vista.getBtnBorrarPartida().addActionListener(e -> borrarPartida());
        vista.getBtnCargarPartida().addActionListener(e -> cargarPartida());
    }

    // ============================================================
    // 1) Cargar el historial desde la base de datos
    // ============================================================
    private void cargarTabla() {
        var lista = batallaDAO.listarTodasRows();

        String[] columnas = {
            "N° Batalla",
            "Fecha",
            "Héroe",
            "Villano",
            "Ganador",
            "N° Turnos"
        };

        Object[][] datos = new Object[lista.size()][columnas.length];

        for (int i = 0; i < lista.size(); i++) {
            String[] fila = lista.get(i);

            datos[i][0] = fila[0];  // ID
            datos[i][1] = fila[1];  // Fecha
            datos[i][2] = fila[2];  // Héroe
            datos[i][3] = fila[3];  // Villano
            datos[i][4] = fila[4];  // Ganador
            datos[i][5] = fila[5];  // Turnos
        }

        vista.actualizarTabla(datos, columnas);
    }

    // ============================================================
    // 2) Cargar partida seleccionada
    // ============================================================
    private void cargarPartida() {
        int fila = vista.getFilaSeleccionada();

        if (fila == -1) {
            JOptionPane.showMessageDialog(vista,
                    "Debes seleccionar una batalla.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idBatalla = Integer.parseInt(
                vista.getTable().getValueAt(fila, 0).toString()
        );

        BatallaDAO.BatallaInfo info = batallaDAO.obtenerBatallaPorId(idBatalla);

        if (info == null) {
            JOptionPane.showMessageDialog(vista,
                    "No se pudo cargar la batalla.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Mostramos los datos de forma sencilla (después podemos hacer otra pantalla)
        JOptionPane.showMessageDialog(vista,
                "ID: " + info.getId() + "\n" +
                "Fecha: " + info.getFecha() + "\n" +
                "Turnos: " + info.getTurnos() + "\n\n" +
                "HÉROE:\n" +
                "ID: " + info.getHeroeId() + "\n" +
                "Nombre: " + info.getHeroeNombre() + "\n" +
                "Apodo: " + info.getHeroeApodo() + "\n" +
                "Vida final: " + info.getHeroeVidaFinal() + "\n\n" +
                "VILLANO:\n" +
                "ID: " + info.getVillanoId() + "\n" +
                "Nombre: " + info.getVillanoNombre() + "\n" +
                "Apodo: " + info.getVillanoApodo() + "\n" +
                "Vida final: " + info.getVillanoVidaFinal() + "\n\n" +
                "GANADOR:\n" +
                "ID: " + info.getGanadorId() + "\n" +
                "Nombre: " + info.getGanadorNombre(),
                "Detalle de Batalla",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ============================================================
    // 3) Borrar partida
    // ============================================================
    private void borrarPartida() {
        int fila = vista.getFilaSeleccionada();

        if (fila == -1) {
            JOptionPane.showMessageDialog(vista,
                    "Debe seleccionar una batalla para borrar.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idBatalla = Integer.parseInt(
                vista.getTable().getValueAt(fila, 0).toString()
        );

        int confirm = JOptionPane.showConfirmDialog(
                vista,
                "¿Seguro que deseas borrar la batalla " + idBatalla + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try (Connection conn = batalla.Conexion.ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM batallas WHERE id = ?")) {

            ps.setInt(1, idBatalla);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(vista, "Batalla borrada.");
            cargarTabla();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista,
                    "Error al borrar la batalla.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ============================================================
    // 4) Volver al menú principal
    // ============================================================
    private void volver() {
        PantallaPrincipal p = new PantallaPrincipal();
        ControladorPrincipal ctrl = new ControladorPrincipal(p);
        ctrl.iniciar();
        vista.dispose();
    }

    public void iniciar() {
        vista.setVisible(true);
    }

}
