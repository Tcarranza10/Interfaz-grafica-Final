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

        // Configurar listeners - CADA BOTÓN CON SU ACCIÓN CORRECTA
        vista.getBtnVolver().addActionListener(e -> volver());
        vista.getBtnBorrarPartida().addActionListener(e -> borrarPartida());
        vista.getBtnCargarPartida().addActionListener(e -> cargarPartida());
        
        // ✅ IMPORTANTE: Actualizar debe llamar a refrescarDatos()
        if (vista.getBtnRefrescar() != null) {
            vista.getBtnRefrescar().addActionListener(e -> refrescarDatos());
        } else {
            System.err.println("⚠️ ADVERTENCIA: Botón Actualizar no encontrado");
        }
    }

    // ✅ ESTE MÉTODO SOLO RECARGA, NO BORRA NADA
    private void refrescarDatos() {
        System.out.println("→ Refrescando datos desde la base de datos...");
        
        try {
            cargarTabla(); // ← SOLO recarga la tabla
            
            JOptionPane.showMessageDialog(
                vista,
                "✓ Datos actualizados correctamente",
                "Actualización Exitosa",
                JOptionPane.INFORMATION_MESSAGE
            );
            
        } catch (Exception e) {
            System.err.println("✗ Error: " + e.getMessage());
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(
                vista,
                "Error al actualizar: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cargarTabla() {
        var lista = batallaDAO.listarTodasRows();

        String[] columnas = {
            "N° Batalla", "Fecha", "Héroe", "Villano", "Ganador", "N° Turnos"
        };

        Object[][] datos = new Object[lista.size()][columnas.length];

        for (int i = 0; i < lista.size(); i++) {
            String[] fila = lista.get(i);
            datos[i][0] = fila[0];
            datos[i][1] = fila[1];
            datos[i][2] = fila[2];
            datos[i][3] = fila[3];
            datos[i][4] = fila[4];
            datos[i][5] = fila[5];
        }

        vista.actualizarTabla(datos, columnas);
    }

    private void cargarPartida() {
        int fila = vista.getFilaSeleccionada();
        if (fila == -1) {
            JOptionPane.showMessageDialog(vista,
                    "Debes seleccionar una batalla.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        // ... resto del código ...
    }

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
            cargarTabla(); // Refrescar después de borrar

        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista,
                    "Error al borrar la batalla.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

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