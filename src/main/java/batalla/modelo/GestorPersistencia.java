package batalla.modelo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que gestiona la persistencia de datos en archivos
 */
public class GestorPersistencia {
    private static final String ARCHIVO_PERSONAJES = "Personajes.txt";
    private static final String ARCHIVO_HISTORIAL = "HistorialPartidas.txt";
    private static final String ARCHIVO_BATALLA_GUARDADA = "batalla_guardada.txt";
    
    /**
     * Guarda un personaje individual en Personajes.txt
     * Guarda la vida inicial (vidaMaxima) en lugar de la vida actual
     */
    public static void guardarPersonaje(Personaje personaje) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_PERSONAJES, true))) {
            writer.write(String.format("%s|%s|%s|%d|%d|%d|%d|%d|%d|%d",
                personaje.getNombre(),
                personaje.getApodo(),
                personaje.getTipo(),
                personaje.getVidaMaxima(), // Guardar vida inicial, no la actual
                personaje.getFuerza(),
                personaje.getDefensa(),
                personaje.getBendiciones(),
                personaje.getVictorias(),
                personaje.getAtaquesSupremosUsados(),
                personaje.getArmasInvocadas()
            ));
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error al guardar personaje: " + e.getMessage());
        }
    }
    
    /**
     * Guarda las estadísticas de personajes (sobrescribe)
     * Guarda la vida inicial (vidaMaxima) en lugar de la vida actual
     */
    public static void guardarPersonajes(List<Personaje> personajes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_PERSONAJES))) {
            for (Personaje p : personajes) {
                writer.write(String.format("%s|%s|%s|%d|%d|%d|%d|%d|%d|%d",
                    p.getNombre(),
                    p.getApodo(),
                    p.getTipo(),
                    p.getVidaMaxima(), // Guardar vida inicial, no la actual
                    p.getFuerza(),
                    p.getDefensa(),
                    p.getBendiciones(),
                    p.getVictorias(),
                    p.getAtaquesSupremosUsados(),
                    p.getArmasInvocadas()
                ));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar personajes: " + e.getMessage());
        }
    }
    
    /**
     * Carga las estadísticas de personajes desde Personajes.txt
     */
    public static List<Personaje> cargarPersonajes() {
        List<Personaje> personajes = new ArrayList<>();
        File archivo = new File(ARCHIVO_PERSONAJES);
        
        if (!archivo.exists()) {
            return personajes;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_PERSONAJES))) {
            String linea;
            while ((linea = reader.readLine()) != null && !linea.trim().isEmpty()) {
                String[] datos = linea.split("\\|");
                if (datos.length >= 10) {
                    String nombre = datos[0];
                    String apodo = datos[1];
                    String tipo = datos[2];
                    int vida = Integer.parseInt(datos[3]);
                    int fuerza = Integer.parseInt(datos[4]);
                    int defensa = Integer.parseInt(datos[5]);
                    int bendiciones = Integer.parseInt(datos[6]);
                    int victorias = Integer.parseInt(datos[7]);
                    int supremos = Integer.parseInt(datos[8]);
                    int armas = Integer.parseInt(datos[9]);
                    
                    Personaje p;
                    if ("Heroe".equals(tipo)) {
                        p = new Heroe(nombre, apodo, vida, fuerza, defensa, bendiciones);
                    } else {
                        p = new Villano(nombre, apodo, vida, fuerza, defensa, bendiciones);
                    }
                    
                    // Restaurar estadísticas acumuladas (victorias, ataques supremos, armas invocadas)
                    for (int i = 0; i < victorias; i++) p.incrementarVictoria();
                    for (int i = 0; i < supremos; i++) p.incrementarAtaqueSupremo();
                    for (int i = 0; i < armas; i++) p.incrementarArmaInvocada();
                    
                    // Restaurar vida y bendiciones a valores iniciales
                    p.restaurarEstadisticasIniciales();
                    
                    personajes.add(p);
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar personajes: " + e.getMessage());
        }
        
        return personajes;
    }
    
    /**
     * Carga solo héroes
     */
    public static List<Heroe> cargarHeroes() {
        List<Heroe> heroes = new ArrayList<>();
        for (Personaje p : cargarPersonajes()) {
            if (p instanceof Heroe) {
                heroes.add((Heroe) p);
            }
        }
        return heroes;
    }
    
    /**
     * Carga solo villanos
     */
    public static List<Villano> cargarVillanos() {
        List<Villano> villanos = new ArrayList<>();
        for (Personaje p : cargarPersonajes()) {
            if (p instanceof Villano) {
                villanos.add((Villano) p);
            }
        }
        return villanos;
    }
    
    /**
     * Guarda una partida completa en HistorialPartidas.txt
     */
    public static void guardarPartidaEnHistorial(String datosPartida) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_HISTORIAL, true))) {
            writer.write(datosPartida);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error al guardar partida en historial: " + e.getMessage());
        }
    }
    
    /**
     * Guarda una partida completa con CombatLog en HistorialPartidas.txt
     */
    public static void guardarPartidaCompleta(PartidaGuardada partida) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_HISTORIAL, true))) {
            // Guardar información básica
            writer.write(String.format("PARTIDA - Heroe: %s (%s) | Villano: %s (%s) | Batallas: %d | Ataques Supremos: %s",
                partida.getHeroeNombre(), partida.getHeroeApodo(),
                partida.getVillanoNombre(), partida.getVillanoApodo(),
                partida.getCantidadBatallas(),
                partida.isAtaquesSupremosActivados() ? "Sí" : "No"));
            writer.newLine();
            
            // Guardar CombatLog
            writer.write("COMBATLOG_START");
            writer.newLine();
            for (String log : partida.getCombatLog()) {
                writer.write(log);
                writer.newLine();
            }
            writer.write("COMBATLOG_END");
            writer.newLine();
            
            // Guardar estadísticas
            writer.write("STATS_START");
            writer.newLine();
            writer.write(String.format("MAYOR_DANIO|%d|%s",
                partida.getEstadisticas().isEmpty() ? 0 : partida.getEstadisticas().get(0).getMayorDanio(),
                partida.getEstadisticas().isEmpty() ? "N/A" : partida.getEstadisticas().get(0).getPersonajeMayorDanio()));
            writer.newLine();
            writer.write(String.format("BATALLA_LARGA|%d|%s",
                partida.getEstadisticas().isEmpty() ? 0 : partida.getEstadisticas().get(0).getTurnos(),
                partida.getEstadisticas().isEmpty() ? "N/A" : partida.getEstadisticas().get(0).getGanador()));
            writer.newLine();
            writer.write(String.format("ARMAS_HEROE|%d",
                partida.getEstadisticas().isEmpty() ? 0 : partida.getEstadisticas().get(0).getArmasInvocadasHeroe()));
            writer.newLine();
            writer.write(String.format("ARMAS_VILLANO|%d",
                partida.getEstadisticas().isEmpty() ? 0 : partida.getEstadisticas().get(0).getArmasInvocadasVillano()));
            writer.newLine();
            writer.write(String.format("SUPREMOS_HEROE|%d",
                partida.getEstadisticas().isEmpty() ? 0 : partida.getEstadisticas().get(0).getAtaquesSupremosHeroe()));
            writer.newLine();
            writer.write(String.format("SUPREMOS_VILLANO|%d",
                partida.getEstadisticas().isEmpty() ? 0 : partida.getEstadisticas().get(0).getAtaquesSupremosVillano()));
            writer.newLine();
            writer.write("STATS_END");
            writer.newLine();
            writer.write("PARTIDA_END");
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error al guardar partida completa: " + e.getMessage());
        }
    }
    
    public static void guardarPartidaComoArchivos(PartidaGuardada partida) {
        File dir = new File("HistorialPartidas");
        if (!dir.exists()) dir.mkdirs();

        // Determinar siguiente indice
        int max = 0;
        File[] files = dir.listFiles((d, name) -> name.matches("Batalla\\d+\\.txt"));
        if (files != null) {
            for (File f : files) {
                String n = f.getName().replaceAll("\\D+", "");
                try {
                    int val = Integer.parseInt(n);
                    if (val > max) max = val;
                } catch (NumberFormatException ignored) {}
            }
        }
        int siguiente = max + 1;
        File out = new File(dir, "Batalla" + siguiente + ".txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(out))) {
            writer.write("HEROE: " + (partida.getHeroeNombre() == null ? "N/A" : partida.getHeroeNombre()));
            writer.newLine();
            writer.write("VILLANO: " + (partida.getVillanoNombre() == null ? "N/A" : partida.getVillanoNombre()));
            writer.newLine();

            // Tomar estadísticas si existen
            if (partida.getEstadisticas() != null && !partida.getEstadisticas().isEmpty()) {
                PartidaGuardada.EstadisticaBatalla s = partida.getEstadisticas().get(0);
                writer.write("GANADOR: " + (s.getGanador() == null ? "N/A" : s.getGanador()));
                writer.newLine();
                writer.write("TURNOS: " + s.getTurnos());
                writer.newLine();
            } else {
                writer.write("GANADOR: N/A"); writer.newLine();
                writer.write("TURNOS: 0"); writer.newLine();
            }

            writer.write("COMBATLOG_START"); writer.newLine();
            if (partida.getCombatLog() != null) {
                for (String linea : partida.getCombatLog()) {
                    writer.write(linea == null ? "" : linea);
                    writer.newLine();
                }
            }
            writer.write("COMBATLOG_END"); writer.newLine();

        } catch (IOException e) {
            System.err.println("Error al escribir partida individual: " + e.getMessage());
        }
    }
    
    /**
     * Guarda una batalla en el historial (compatibilidad)
     */
    public static void guardarBatallaEnHistorial(String heroe, String villano, String ganador, int turnos) {
        guardarPartidaEnHistorial(String.format("BATALLA - Heroe: %s | Villano: %s | Ganador: %s | Turnos: %d",
            heroe, villano, ganador, turnos));
    }
    
    /**
     * Carga el historial completo de partidas desde HistorialPartidas.txt
     */
    public static List<String> cargarHistorialPartidas() {
        List<String> historial = new ArrayList<>();
        File archivo = new File(ARCHIVO_HISTORIAL);
        
        if (!archivo.exists()) {
            return historial;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_HISTORIAL))) {
            String linea;
            while ((linea = reader.readLine()) != null && !linea.trim().isEmpty()) {
                historial.add(linea);
            }
        } catch (IOException e) {
            System.err.println("Error al cargar historial de partidas: " + e.getMessage());
        }
        
        return historial;
    }

    /**
     * Sobrescribe el archivo de historial con la lista provista.
     */
    public static void guardarHistorialPartidas(List<String> partidas) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_HISTORIAL))) {
            for (String linea : partidas) {
                writer.write(linea);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error al guardar historial de partidas: " + e.getMessage());
        }
    }
    
    /**
     * Carga el historial de batallas (últimas 5) - compatibilidad
     */
    public static List<String> cargarHistorial() {
        List<String> historial = cargarHistorialPartidas();
        if (historial.size() > 5) {
            return historial.subList(Math.max(0, historial.size() - 5), historial.size());
        }
        return historial;
    }
    
    /**
     * Guarda el estado completo de la batalla
     */
    public static void guardarBatalla(ConfiguracionPartida config, int batallaActual, int turnoActual) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ARCHIVO_BATALLA_GUARDADA))) {
            oos.writeObject(config);
            oos.writeInt(batallaActual);
            oos.writeInt(turnoActual);
        } catch (IOException e) {
            System.err.println("Error al guardar batalla: " + e.getMessage());
        }
    }
    
    /**
     * Carga el estado completo de la batalla
     */
    public static EstadoBatalla cargarBatalla() {
        File archivo = new File(ARCHIVO_BATALLA_GUARDADA);
        if (!archivo.exists()) {
            return null;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(ARCHIVO_BATALLA_GUARDADA))) {
            ConfiguracionPartida config = (ConfiguracionPartida) ois.readObject();
            int batallaActual = ois.readInt();
            int turnoActual = ois.readInt();
            return new EstadoBatalla(config, batallaActual, turnoActual);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error al cargar batalla: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Clase auxiliar para almacenar el estado de la batalla
     */
    public static class EstadoBatalla implements Serializable {
        private static final long serialVersionUID = 1L;
        private ConfiguracionPartida config;
        private int batallaActual;
        private int turnoActual;
        
        public EstadoBatalla(ConfiguracionPartida config, int batallaActual, int turnoActual) {
            this.config = config;
            this.batallaActual = batallaActual;
            this.turnoActual = turnoActual;
        }
        
        public ConfiguracionPartida getConfig() { return config; }
        public int getBatallaActual() { return batallaActual; }
        public int getTurnoActual() { return turnoActual; }
    }
}


