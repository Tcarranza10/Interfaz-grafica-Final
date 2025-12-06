package batalla.controlador;

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
        List<batalla.modelo.Personaje> personajes = GestorPersistencia.cargarPersonajes();
        
        // Ordenar por victorias descendente
        personajes.sort((p1, p2) -> Integer.compare(p2.getVictorias(), p1.getVictorias()));
        
        // Calcular total de batallas para winrate
        int totalBatallas = personajes.stream()
            .mapToInt(batalla.modelo.Personaje::getVictorias)
            .sum();
        
        Object[][] datos = new Object[personajes.size()][6];
        String[] columnas = {"N°", "Nombre", "Tipo", "Victorias", "Winrate (%)", "Ataques Supremos Usados"};
        
        for (int i = 0; i < personajes.size(); i++) {
            batalla.modelo.Personaje p = personajes.get(i);
            datos[i][0] = i + 1;
            datos[i][1] = p.getNombre();
            datos[i][2] = p.getTipo();
            datos[i][3] = p.getVictorias();
            
            // Calcular winrate
            double winrate = totalBatallas > 0 ? (p.getVictorias() * 100.0) / totalBatallas : 0.0;
            datos[i][4] = String.format("%.2f", winrate);
            datos[i][5] = p.getAtaquesSupremosUsados();
        }
        
        vista.actualizarTabla(datos, columnas);
    }

    public void iniciar() {
        vista.setVisible(true);
    }
}

