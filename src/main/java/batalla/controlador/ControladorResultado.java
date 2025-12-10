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

    // Estadísticas y metadatos usados en el reporte y al guardar partidas
    private PartidaGuardada partidaGuardada;
    private int mayorDanio;
    private String personajeMayorDanio;
    private int batallaMasLarga;
    private String ganadorBatallaMasLarga;
    
    // Constructor para múltiples batallas con estadísticas completas
    public ControladorResultado(PantallaResultado vista, List<Personaje> personajes, int totalBatallas,
                                PartidaGuardada partida, int mayorDanio, String personajeMayorDanio,
                                int batallaMasLarga, String ganadorBatallaMasLarga) {
        this.vista = vista;
        this.personajes = personajes;
        this.totalBatallas = totalBatallas;
        this.partidaGuardada = partida;
        this.mayorDanio = mayorDanio;
        this.personajeMayorDanio = personajeMayorDanio;
        this.batallaMasLarga = batallaMasLarga;
        this.ganadorBatallaMasLarga = ganadorBatallaMasLarga;
        
        // Extraer héroe y villano de la lista de personajes
        extraerPersonajesDePartida();
        
        // DEBUG: Imprimir para verificar
        System.out.println("=== DEBUG ControladorResultado ===");
        System.out.println("Constructor múltiples batallas llamado");
        System.out.println("Personajes en lista: " + (personajes != null ? personajes.size() : "null"));
        System.out.println("Héroe extraído: " + (heroe != null ? heroe.getNombre() : "null"));
        System.out.println("Villano extraído: " + (villano != null ? villano.getNombre() : "null"));
        System.out.println("===================================");
        
        configurarEventos();
        mostrarReporteCompleto(partida, mayorDanio, personajeMayorDanio, batallaMasLarga, ganadorBatallaMasLarga);
    }
    
    // Constructor para múltiples batallas (compatibilidad)
    public ControladorResultado(PantallaResultado vista, List<Personaje> personajes, int totalBatallas) {
        this.vista = vista;
        this.personajes = personajes;
        this.totalBatallas = totalBatallas;
        
        // Extraer héroe y villano de la lista de personajes
        extraerPersonajesDePartida();
        
        // DEBUG
        System.out.println("=== DEBUG ControladorResultado ===");
        System.out.println("Constructor simple llamado");
        System.out.println("Héroe extraído: " + (heroe != null ? heroe.getNombre() : "null"));
        System.out.println("Villano extraído: " + (villano != null ? villano.getNombre() : "null"));
        System.out.println("===================================");
        
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
        
        // DEBUG
        System.out.println("=== DEBUG ControladorResultado ===");
        System.out.println("Constructor batalla individual llamado");
        System.out.println("Héroe: " + (heroe != null ? heroe.getNombre() : "null"));
        System.out.println("Villano: " + (villano != null ? villano.getNombre() : "null"));
        System.out.println("===================================");
        
        configurarEventos();
        mostrarResultados();
    }
    
    // Constructor para cargar batalla por ID desde historial
    public ControladorResultado(PantallaResultado vista, int idBatalla) {
        this.vista = vista;
        cargarBatallaDelHistorial(idBatalla);
        configurarEventos();
    }
    
    /**
     * Carga los detalles de una batalla desde la base de datos
     */
    private void cargarBatallaDelHistorial(int idBatalla) {
        try {
            System.out.println("→ Cargando batalla #" + idBatalla + " desde la BD...");
            
            BatallaDAO batallaDAO = new BatallaDAO();
            batalla.Conexion.ConexionSQLite conexion = new batalla.Conexion.ConexionSQLite();
            java.sql.Connection conn = conexion.conectar();
            
            String query = "SELECT heroe, villano, ganador, turnos FROM batallas WHERE id = ?";
            java.sql.PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, idBatalla);
            java.sql.ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                String nombreHeroe = rs.getString("heroe");
                String nombreVillano = rs.getString("villano");
                String ganador = rs.getString("ganador");
                int turnos = rs.getInt("turnos");
                
                System.out.println("  ✓ Batalla encontrada: " + nombreHeroe + " vs " + nombreVillano);
                System.out.println("  ✓ Ganador: " + ganador + " en " + turnos + " turnos");
                
                // Cargar personajes desde la BD usando apodo
                PersonajeDAO personajeDAO = new PersonajeDAO();
                this.heroe = (Heroe) personajeDAO.obtenerPorApodo(nombreHeroe);
                this.villano = (Villano) personajeDAO.obtenerPorApodo(nombreVillano);
                this.ganador = ganador;
                this.turnos = turnos;
                
                System.out.println("  ✓ Personajes cargados. Mostrando resultados...");
                mostrarResultados();
                System.out.println("  ✓ Resultados mostrados correctamente");
            } else {
                System.err.println("✗ Batalla #" + idBatalla + " no encontrada en la BD");
            }
            
            rs.close();
            ps.close();
            conn.close();
            
        } catch (Exception e) {
            System.err.println("✗ Error al cargar batalla del historial: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Extrae héroe y villano de la lista de personajes
     */
    private void extraerPersonajesDePartida() {
        System.out.println("→ Iniciando extracción de personajes...");
        
        if (personajes == null) {
            System.err.println("✗ ERROR: La lista de personajes es NULL");
            return;
        }
        
        if (personajes.isEmpty()) {
            System.err.println("✗ ERROR: La lista de personajes está VACÍA");
            return;
        }
        
        System.out.println("→ Lista contiene " + personajes.size() + " personajes");
        
        for (int i = 0; i < personajes.size(); i++) {
            Personaje p = personajes.get(i);
            System.out.println("  [" + i + "] " + p.getClass().getSimpleName() + ": " + p.getNombre());
            
            if (p instanceof Heroe && heroe == null) {
                heroe = (Heroe) p;
                System.out.println("  ✓ Héroe asignado: " + heroe.getNombre());
            } else if (p instanceof Villano && villano == null) {
                villano = (Villano) p;
                System.out.println("  ✓ Villano asignado: " + villano.getNombre());
            }
        }
        
        if (heroe == null) {
            System.err.println("✗ ERROR: No se encontró ningún Héroe en la lista");
        }
        if (villano == null) {
            System.err.println("✗ ERROR: No se encontró ningún Villano en la lista");
        }
    }
    
    private void configurarEventos() {
        vista.getBtnVolver().addActionListener(e -> volverPrincipal());
        vista.getBtnAgain().addActionListener(e -> nuevaBatalla());
        vista.getBtnGuardarPartida().addActionListener(e -> guardarPartida());
    }

    private void mostrarResultados() {
        if (heroe == null || villano == null || ganador == null) {
            System.err.println("Error: Datos incompletos para mostrar resultados");
            return;
        }
        
        // Crear datos para la tabla
        Object[][] datos = {
            {"-", heroe.getNombre(), villano.getNombre(), ganador, turnos}
        };
        
        String[] columnas = {"N° Batalla", "Héroe", "Villano", "Ganador", "Turnos"};
        
        vista.actualizarTabla(datos, columnas);
        
        // Construir objeto PartidaGuardada básico para permitir guardar la partida individual
        partidaGuardada = new PartidaGuardada();
        partidaGuardada.setHeroeNombre(heroe.getNombre());
        partidaGuardada.setHeroeApodo(heroe.getApodo());
        partidaGuardada.setVillanoNombre(villano.getNombre());
        partidaGuardada.setVillanoApodo(villano.getApodo());
        partidaGuardada.setCantidadBatallas(1);
        partidaGuardada.getCombatLog().add("Resultado: " + ganador + " en " + turnos + " turnos");
        partidaGuardada.getCombatLog().add(heroe.getNombre() + " vida final: " + heroe.getVida());
        partidaGuardada.getCombatLog().add(villano.getNombre() + " vida final: " + villano.getVida());
    }
    
    // Reemplazar el método mostrarReporteCompleto() con esta versión:

    private void mostrarReporteCompleto(PartidaGuardada partida, int mayorDanio, String personajeMayorDanio,
                                    int batallaMasLarga, String ganadorBatallaMasLarga) {
        vista.limpiar();
        
        // Mostrar datos en tabla - una fila por batalla
        Object[][] datos = new Object[totalBatallas][5];
        String[] columnas = {"N° Batalla", "Héroe", "Villano", "Ganador", "Turnos"};
        
        for (int i = 0; i < totalBatallas && i < datos.length; i++) {
            Personaje h = personajes.stream().filter(p -> p instanceof Heroe).findFirst().orElse(null);
            Personaje v = personajes.stream().filter(p -> p instanceof Villano).findFirst().orElse(null);
            
            if (h != null && v != null) {
                datos[i][0] = i + 1;
                datos[i][1] = h.getNombre();
                datos[i][2] = v.getNombre();
                datos[i][3] = h.getVictorias() > v.getVictorias() ? h.getNombre() : v.getNombre();
                datos[i][4] = batallaMasLarga;
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
        
        // ============================================
        // CAMBIO: Cargar historial desde BASE DE DATOS
        // ============================================
        vista.agregarHistorial("=== HISTORIAL DE BATALLAS (desde BD) ===");
        
        try {
            BatallaDAO batallaDAO = new BatallaDAO();
            List<String[]> historialBD = batallaDAO.obtenerHistorialRows();
            
            if (historialBD.isEmpty()) {
                vista.agregarHistorial("No hay batallas registradas en la base de datos.");
            } else {
                for (String[] fila : historialBD) {
                    // Formato: [id, fecha, heroe, villano, ganador, turnos]
                    String lineaHistorial = String.format(
                        "Batalla #%s - %s | %s vs %s | Ganador: %s | Turnos: %s",
                        fila[0], fila[1], fila[2], fila[3], fila[4], fila[5]
                    );
                    vista.agregarHistorial(lineaHistorial);
                }
            }
        } catch (Exception e) {
            vista.agregarHistorial("Error al cargar historial: " + e.getMessage());
            System.err.println("Error al cargar historial desde BD: " + e.getMessage());
        }
    }
    
    /**
     * Guarda la partida en la base de datos
     */
    private void guardarPartida() {
        System.out.println("\n=== INICIANDO GUARDADO DE PARTIDA ===");
        
        try {
            // DEBUG: Estado de variables
            System.out.println("Estado de variables:");
            System.out.println("  heroe: " + (heroe != null ? heroe.getNombre() : "NULL"));
            System.out.println("  villano: " + (villano != null ? villano.getNombre() : "NULL"));
            System.out.println("  personajes: " + (personajes != null ? personajes.size() + " elementos" : "NULL"));
            
            // Validación crítica
            if (heroe == null || villano == null) {
                System.err.println("✗ ERROR: Héroe o Villano son NULL");
                
                // Intentar recuperar de la lista de personajes
                if (personajes != null && !personajes.isEmpty()) {
                    System.out.println("→ Intentando recuperar de la lista de personajes...");
                    extraerPersonajesDePartida();
                    
                    if (heroe == null || villano == null) {
                        javax.swing.JOptionPane.showMessageDialog(vista,
                                "Error: No se pudieron obtener los datos de los personajes.\n\n" +
                                "DEBUG INFO:\n" +
                                "Lista de personajes: " + (personajes != null ? personajes.size() : "null") + "\n" +
                                "Héroe: " + (heroe != null ? "OK" : "NULL") + "\n" +
                                "Villano: " + (villano != null ? "OK" : "NULL"),
                                "Error",
                                javax.swing.JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    System.out.println("✓ Personajes recuperados exitosamente");
                } else {
                    javax.swing.JOptionPane.showMessageDialog(vista,
                            "Error: No hay datos de batalla para guardar.",
                            "Error",
                            javax.swing.JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            System.out.println("✓ Validación de personajes OK");

            // Asegurar que los Personajes existen en la BD
            System.out.println("\n→ Verificando personajes en BD...");
            PersonajeDAO pdao = new PersonajeDAO();

            int heroeId = pdao.asegurarPersonajeEnBD(heroe);
            System.out.println("  Héroe ID: " + heroeId);
            
            int villanoId = pdao.asegurarPersonajeEnBD(villano);
            System.out.println("  Villano ID: " + villanoId);

            // Validar IDs
            if (heroeId <= 0 || villanoId <= 0) {
                javax.swing.JOptionPane.showMessageDialog(vista,
                        "Error: No se pudieron registrar los personajes en la base de datos.\n\n" +
                        "IDs obtenidos:\n" +
                        "Héroe: " + heroeId + "\n" +
                        "Villano: " + villanoId,
                        "Error de Base de Datos",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                return;
            }

            System.out.println("✓ IDs válidos obtenidos");

            // ✅ SOLO actualizar estadísticas (la batalla ya fue guardada en ControladorBatalla)
            System.out.println("\n→ Actualizando estadísticas finales...");
            pdao.actualizarEstadisticas(heroe);
            pdao.actualizarEstadisticas(villano);
            System.out.println("✓ Estadísticas actualizadas");

            // ❌ NO GUARDAR LA BATALLA AQUÍ - Ya fue guardada en ControladorBatalla
            // BatallaDAO batallaDAO = new BatallaDAO();
            // batallaDAO.insertarBatalla(heroe, villano, ganadorObj, turnosGuardar);

            // Mostrar confirmación
            String mensaje = String.format(
                "✓ Estadísticas actualizadas correctamente en la base de datos.\n\n" +
                "Detalles:\n" +
                "• Héroe: %s (ID: %d)\n" +
                "  - Victorias: %d | Derrotas: %d\n" +
                "• Villano: %s (ID: %d)\n" +
                "  - Victorias: %d | Derrotas: %d\n\n" +
                "Nota: Las batallas ya fueron guardadas durante el combate.",
                heroe.getNombre(), heroeId,
                heroe.getVictorias(), heroe.getDerrotas(),
                villano.getNombre(), villanoId,
                villano.getVictorias(), villano.getDerrotas()
            );

            System.out.println("\n" + mensaje);
            
            javax.swing.JOptionPane.showMessageDialog(vista,
                    mensaje,
                    "Éxito",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            System.err.println("\n✗ ERROR CRÍTICO:");
            e.printStackTrace();
            
            javax.swing.JOptionPane.showMessageDialog(vista,
                    "Error inesperado al guardar:\n" + e.getMessage() +
                    "\n\nDetalles técnicos:\n" + e.getClass().getName() +
                    "\n\nRevisa la consola para más información.",
                    "Error Crítico",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        
        System.out.println("=== FIN GUARDADO DE PARTIDA ===\n");
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