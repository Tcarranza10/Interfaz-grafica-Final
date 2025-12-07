package batalla.controlador;

import batalla.Conexion.PersonajeDAO;
import batalla.modelo.GestorPersistencia;
import batalla.vista.PantallaRanking;

import java.util.List;

/**
 * Controlador para la pantalla de ranking
 */
public class ControladorRanking {
    private PantallaRanking vista;

    public ControladorRanking(PantallaRanking vista) {
        this.vista = vista;
        configurarEventos();
        cargarRanking();
    }

    private void configurarEventos() {
        vista.getBtnVolver().addActionListener(e -> vista.dispose());
    }

    private void cargarRanking() {
        try {
            PersonajeDAO dao = new PersonajeDAO();
            List<batalla.modelo.Personaje> personajes = dao.obtenerRanking(); // Ya está ordenado por victorias DESC
            
            Object[][] datos = new Object[personajes.size()][6];
            String[] columnas = {"N°", "Nombre", "Tipo", "Victorias", "Derrotas", "Ataques Supremos Usados"};
            
            for (int i = 0; i < personajes.size(); i++) {
                batalla.modelo.Personaje p = personajes.get(i);
                datos[i][0] = i + 1;
                datos[i][1] = p.getNombre();
                datos[i][2] = p.getTipo();
                datos[i][3] = p.getVictorias();
                datos[i][4] = p.getDerrotas();
                datos[i][5] = p.getAtaquesSupremosUsados();
            }
            
            vista.actualizarTabla(datos, columnas);
            
        } catch (Exception e) {
            System.err.println("Error al cargar ranking desde BD: " + e.getMessage());
            javax.swing.JOptionPane.showMessageDialog(vista,
                "Error al cargar el ranking desde la base de datos",
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    public void iniciar() {
        vista.setVisible(true);
    }
}

