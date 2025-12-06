package batalla.controlador;

import batalla.Conexion.BatallaDAO;
import batalla.Conexion.PersonajeDAO;
import batalla.modelo.*;
import batalla.vista.PantallaResultado;
import batalla.vista.PantallaPrincipal;

import java.util.List;

/**
 * Controlador para la pantalla de resultados
 * Muestra el resultado de la batalla y el historial
 */
public class ControladorResultado {
    private PantallaResultado vista;
    private List<Personaje> personajes;
    private int totalBatallas;
    private Heroe heroe;
    private Villano villano;
    private String ganador;
    private int turnos;

    // Constructor para múltiples batallas con estadísticas completas
    public ControladorResultado(PantallaResultado vista, List<Personaje> personajes, int totalBatallas,
                                PartidaGuardada partida, int mayorDanio, String personajeMayorDanio,
                                int batallaMasLarga, String ganadorBatallaMasLarga) {
        this.vista = vista;
        this.personajes = personajes;
        this.totalBatallas = totalBatallas;
        configurarEventos();
        mostrarReporteCompleto(partida, mayorDanio, personajeMayorDanio, batallaMasLarga, ganadorBatallaMasLarga);
    }
    
    // Constructor para múltiples batallas (compatibilidad)
    public ControladorResultado(PantallaResultado vista, List<Personaje> personajes, int totalBatallas) {
        this.vista = vista;
        this.personajes = personajes;
        this.totalBatallas = totalBatallas;
        configurarEventos();
        mostrarReporteCompleto(null, 0, "", 0, "");
    }
    
    // Constructor de compatibilidad para una sola batalla
    public ControladorResultado(PantallaResultado vista, Heroe heroe, Villano villano, String ganador, int turnos) {
        this.vista = vista;
        this.heroe = heroe;
        this.villano = villano;
        this.ganador = ganador;
        this.turnos = turnos;
        configurarEventos();
        mostrarResultados();
    }

    private PartidaGuardada partidaGuardada;
    // Estadísticas y metadatos usados en el reporte y al guardar partidas
    private int mayorDanio;
    private String personajeMayorDanio;
    private int batallaMasLarga;
    private String ganadorBatallaMasLarga;
    
    private void configurarEventos() {
        vista.getBtnVolver().addActionListener(e -> volverPrincipal());
        vista.getBtnAgain().addActionListener(e -> nuevaBatalla());
        vista.getBtnGuardarPartida().addActionListener(e -> guardarPartida());
    }

    private void mostrarResultados() {
        vista.limpiar();
        
        // Mostrar resultado de la batalla
        vista.agregarResultado("=== RESULTADO DE LA BATALLA ===");
        vista.agregarResultado("");
        vista.agregarResultado("Ganador: " + ganador);
        vista.agregarResultado("Turnos totales: " + turnos);
        vista.agregarResultado("");
        vista.agregarResultado("Estadísticas finales:");
        vista.agregarResultado(heroe.getNombre() + ": " + heroe.getVida() + " vida restante");
        vista.agregarResultado(villano.getNombre() + ": " + villano.getVida() + " vida restante");
        vista.agregarResultado("");
        
        // Mostrar armas utilizadas
        vista.agregarResultado("Armas utilizadas:");
        if (heroe.getArma() != null) {
            vista.agregarResultado(heroe.getNombre() + ": " + heroe.getArma().getNombre());
        } else {
            vista.agregarResultado(heroe.getNombre() + ": Ninguna");
        }
        
        if (villano.getArma() != null) {
            vista.agregarResultado(villano.getNombre() + ": " + villano.getArma().getNombre());
        } else {
            vista.agregarResultado(villano.getNombre() + ": Ninguna");
        }
        
        // Mostrar historial (simplificado)
        vista.agregarHistorial("Batalla: " + heroe.getNombre() + " vs " + villano.getNombre());
        vista.agregarHistorial("Ganador: " + ganador);
        vista.agregarHistorial("Turnos: " + turnos);

        // Construir objeto PartidaGuardada básico para permitir guardar la partida individual
        partidaGuardada = new PartidaGuardada();
        partidaGuardada.setHeroeNombre(heroe.getNombre());
        partidaGuardada.setVillanoNombre(villano.getNombre());
        partidaGuardada.setCantidadBatallas(1);
        // Agregar un combat log mínimo con información esencial (si el juego tuviera un log real, usarlo)
        partidaGuardada.getCombatLog().add("Resultado: " + ganador + " en " + turnos + " turnos");
        partidaGuardada.getCombatLog().add(heroe.getNombre() + " vida final: " + heroe.getVida());
        partidaGuardada.getCombatLog().add(villano.getNombre() + " vida final: " + villano.getVida());
    }
    
    private void mostrarReporteCompleto(PartidaGuardada partida, int mayorDanio, String personajeMayorDanio,
                                       int batallaMasLarga, String ganadorBatallaMasLarga) {
        this.partidaGuardada = partida;
        this.mayorDanio = mayorDanio;
        this.personajeMayorDanio = personajeMayorDanio;
        this.batallaMasLarga = batallaMasLarga;
        this.ganadorBatallaMasLarga = ganadorBatallaMasLarga;
        vista.limpiar();
        
        // Mostrar datos en tabla - una fila por batalla
        Object[][] datos = new Object[totalBatallas][5];
        String[] columnas = {"N° Batalla", "Héroe", "Villano", "Ganador", "Turnos"};
        
        // Por ahora, mostrar datos básicos - se puede mejorar con datos reales de cada batalla
        for (int i = 0; i < totalBatallas && i < datos.length; i++) {
            Personaje h = personajes.stream().filter(p -> p instanceof Heroe).findFirst().orElse(null);
            Personaje v = personajes.stream().filter(p -> p instanceof Villano).findFirst().orElse(null);
            
            if (h != null && v != null) {
                datos[i][0] = i + 1;
                datos[i][1] = h.getNombre();
                datos[i][2] = v.getNombre();
                // El ganador se determinaría por las victorias
                datos[i][3] = h.getVictorias() > v.getVictorias() ? h.getNombre() : v.getNombre();
                datos[i][4] = batallaMasLarga; // Usar turnos reales si están disponibles
            }
        }
        
        vista.actualizarTabla(datos, columnas);
        
        // Mostrar estadísticas
        vista.agregarResultado("=== ESTADÍSTICAS GENERALES ===");
        vista.agregarResultado("Total de batallas: " + totalBatallas);
        if (mayorDanio > 0) {
            vista.agregarResultado("Mayor daño en un solo ataque: " + mayorDanio + " (" + personajeMayorDanio + ")");
        }
        if (batallaMasLarga > 0) {
            vista.agregarResultado("Batalla más larga: " + batallaMasLarga + " turnos (Ganador: " + ganadorBatallaMasLarga + ")");
        }
        
        // Calcular totales
        int totalArmasHeroe = 0;
        int totalArmasVillano = 0;
        int totalSupremosHeroe = 0;
        int totalSupremosVillano = 0;
        
        for (Personaje p : personajes) {
            if (p instanceof Heroe) {
                totalArmasHeroe += p.getArmasInvocadas();
                totalSupremosHeroe += p.getAtaquesSupremosUsados();
            } else {
                totalArmasVillano += p.getArmasInvocadas();
                totalSupremosVillano += p.getAtaquesSupremosUsados();
            }
        }
        
        vista.agregarResultado("Total armas invocadas héroe: " + totalArmasHeroe);
        vista.agregarResultado("Total armas invocadas villano: " + totalArmasVillano);
        vista.agregarResultado("Ataques supremos ejecutados héroe: " + totalSupremosHeroe);
        vista.agregarResultado("Ataques supremos ejecutados villano: " + totalSupremosVillano);
        
        // Cargar historial
        List<String> historial = GestorPersistencia.cargarHistorial();
        vista.agregarHistorial("=== HISTORIAL DE BATALLAS ===");
        for (String batalla : historial) {
            vista.agregarHistorial(batalla);
        }
    }
    
    
    private void guardarPartida() {

        if (heroe == null || villano == null) {
            javax.swing.JOptionPane.showMessageDialog(vista,
                    "No hay datos de batalla para guardar.",
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ================================
        // 1) Asegurar que los Personajes existen en la BD
        // ================================
        PersonajeDAO pdao = new PersonajeDAO();

        pdao.asegurarPersonajeEnBD(heroe);
        pdao.asegurarPersonajeEnBD(villano);

        // Determinar ganador como objeto Personaje
        Personaje ganadorObj =
                ganador.equals(heroe.getNombre()) ? heroe : villano;

        pdao.asegurarPersonajeEnBD(ganadorObj);

        // ================================
        // 2) Guardar en tabla batallas
        // ================================
        BatallaDAO batallaDAO = new BatallaDAO();
        batallaDAO.insertarBatalla(heroe, villano, ganadorObj, turnos);

        javax.swing.JOptionPane.showMessageDialog(vista,
                "Batalla guardada correctamente en la base de datos.",
                "Éxito",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }


    private void volverPrincipal() {
        PantallaPrincipal pantallaPrincipal = new PantallaPrincipal();
        ControladorPrincipal controladorPrincipal = new ControladorPrincipal(pantallaPrincipal);
        controladorPrincipal.iniciar();
        vista.dispose();
    }

    private void nuevaBatalla() {
        batalla.vista.PantallaCreacion pantallaCreacion = new batalla.vista.PantallaCreacion();
        ControladorCreacion controladorCreacion = new ControladorCreacion(pantallaCreacion);
        controladorCreacion.iniciar();
        vista.dispose();
    }

    public void iniciar() {
        vista.setVisible(true);
    }
}

