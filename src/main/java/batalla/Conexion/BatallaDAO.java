package batalla.Conexion;

import batalla.modelo.Personaje;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para la tabla 'batallas'
 * - insertarBatalla(...) guarda una fila en la tabla batallas
 * - obtenerHistorialRows() devuelve las últimas 10 filas (listas de String[]) para mostrar en un JTable
 * - listarTodasRows() devuelve todas las filas
 * - obtenerBatallaPorId(id) devuelve un objeto BatallaInfo con datos básicos de la batalla
 * - borrarHistorial() elimina todas las filas (opcional, util)
 */
public class BatallaDAO {

    /**
     * Inserta una batalla en la tabla 'batallas'.
     *
     * @param heroe   personaje héroe (debe tener id válido)
     * @param villano personaje villano (debe tener id válido)
     * @param ganador personaje ganador (debe tener id válido)
     * @param turnos  cantidad de turnos
     */
    public void insertarBatalla(Personaje heroe, Personaje villano, Personaje ganador, int turnos) {
        String sql = "INSERT INTO batallas (heroe_id, villano_id, ganador_id, turnos) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, heroe.getId());
            ps.setInt(2, villano.getId());
            ps.setInt(3, ganador.getId());
            ps.setInt(4, turnos);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al insertar batalla: " + e.getMessage());
        }
    }

    /**
     * Obtiene las últimas 10 batallas (más recientes) como filas listas para un JTable.
     * Cada String[] tiene: { id, fecha, heroe, villano, ganador, turnos }
     *
     * @return List<String[]> con máximo 10 filas ordenadas por fecha descendente
     */
    public List<String[]> obtenerHistorialRows() {
        List<String[]> lista = new ArrayList<>();

        String sql = "SELECT b.id, b.fecha, "
                   + "h.nombre AS heroe, v.nombre AS villano, g.nombre AS ganador, "
                   + "b.turnos "
                   + "FROM batallas b "
                   + "JOIN personajes h ON b.heroe_id = h.id "
                   + "JOIN personajes v ON b.villano_id = v.id "
                   + "JOIN personajes g ON b.ganador_id = g.id "
                   + "ORDER BY b.fecha DESC "
                   + "LIMIT 10";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String[] fila = new String[6];
                fila[0] = String.valueOf(rs.getInt("id"));
                fila[1] = rs.getString("fecha");
                fila[2] = rs.getString("heroe");
                fila[3] = rs.getString("villano");
                fila[4] = rs.getString("ganador");
                fila[5] = String.valueOf(rs.getInt("turnos"));

                lista.add(fila);
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener historial: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Listar todas las batallas (sin límite).
     * Útil para depuración o reportes.
     */
    public List<String[]> listarTodasRows() {
        List<String[]> lista = new ArrayList<>();

        String sql = "SELECT b.id, b.fecha, "
                   + "h.nombre AS heroe, v.nombre AS villano, g.nombre AS ganador, "
                   + "b.turnos "
                   + "FROM batallas b "
                   + "JOIN personajes h ON b.heroe_id = h.id "
                   + "JOIN personajes v ON b.villano_id = v.id "
                   + "JOIN personajes g ON b.ganador_id = g.id "
                   + "ORDER BY b.id DESC";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String[] fila = new String[6];
                fila[0] = String.valueOf(rs.getInt("id"));
                fila[1] = rs.getString("fecha");
                fila[2] = rs.getString("heroe");
                fila[3] = rs.getString("villano");
                fila[4] = rs.getString("ganador");
                fila[5] = String.valueOf(rs.getInt("turnos"));

                lista.add(fila);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar todas las batallas: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Devuelve información básica de una batalla por su id.
     * Si necesitás más detalle (por-turno), habría que crear tabla eventos_batalla y guardarla al insertar.
     */
    public BatallaInfo obtenerBatallaPorId(int idBatalla) {
        String sql = "SELECT b.id, b.fecha, b.turnos, "
                   + "h.id AS heroe_id, h.nombre AS heroe_nombre, h.apodo AS heroe_apodo, h.vida_final AS heroe_vida_final, "
                   + "v.id AS villano_id, v.nombre AS villano_nombre, v.apodo AS villano_apodo, v.vida_final AS villano_vida_final, "
                   + "g.id AS ganador_id, g.nombre AS ganador_nombre "
                   + "FROM batallas b "
                   + "JOIN personajes h ON b.heroe_id = h.id "
                   + "JOIN personajes v ON b.villano_id = v.id "
                   + "JOIN personajes g ON b.ganador_id = g.id "
                   + "WHERE b.id = ?";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idBatalla);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    BatallaInfo info = new BatallaInfo();
                    info.setId(rs.getInt("id"));
                    info.setFecha(rs.getString("fecha"));
                    info.setTurnos(rs.getInt("turnos"));

                    info.setHeroeId(rs.getInt("heroe_id"));
                    info.setHeroeNombre(rs.getString("heroe_nombre"));
                    info.setHeroeApodo(rs.getString("heroe_apodo"));
                    info.setHeroeVidaFinal(rs.getInt("heroe_vida_final"));

                    info.setVillanoId(rs.getInt("villano_id"));
                    info.setVillanoNombre(rs.getString("villano_nombre"));
                    info.setVillanoApodo(rs.getString("villano_apodo"));
                    info.setVillanoVidaFinal(rs.getInt("villano_vida_final"));

                    info.setGanadorId(rs.getInt("ganador_id"));
                    info.setGanadorNombre(rs.getString("ganador_nombre"));

                    // Obtener estadísticas
                    obtenerEstadisticasBatalla(info);
                    
                    return info;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener batalla por id: " + e.getMessage());
        }

        return null;
    }

    /**
     * Borra todo el historial de batallas. Úsalo con precaución (opcional).
     */
    public void borrarHistorial() {
        String sql = "DELETE FROM batallas";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int afectados = ps.executeUpdate();
            System.out.println("batallas eliminadas: " + afectados);

        } catch (SQLException e) {
            System.err.println("Error al borrar historial: " + e.getMessage());
        }
    }

    // ---------------- Clase auxiliar para devolver detalles de batalla ----------------
    /**
     * Obtiene el último ID de batalla insertado
     */
    public int obtenerUltimoIdBatalla() {
        String sql = "SELECT MAX(id) as id FROM batallas";
        
        try (Connection conn = ConexionSQLite.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener último ID de batalla: " + e.getMessage());
        }
        
        return -1;
    }

    /**
     * Guarda un evento de combate para una batalla específica
     */
    public void guardarEventoCombate(int idBatalla, String evento) {
        String sql = "INSERT INTO eventos_batalla (id_batalla, evento, timestamp) VALUES (?, ?, datetime('now'))";
        
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idBatalla);
            ps.setString(2, evento);
            ps.executeUpdate();
            
        } catch (SQLException e) {
            // Si la tabla no existe, intentar crearla
            if (e.getMessage().contains("no such table")) {
                crearTablaCombatLog();
                guardarEventoCombate(idBatalla, evento); // Reintentar
            } else {
                System.err.println("Error al guardar evento: " + e.getMessage());
            }
        }
    }
    
    /**
     * Obtiene todos los eventos de una batalla
     */
    public List<String> obtenerCombatLog(int idBatalla) {
        List<String> eventos = new ArrayList<>();
        String sql = "SELECT evento FROM eventos_batalla WHERE id_batalla = ? ORDER BY id ASC";
        
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idBatalla);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                eventos.add(rs.getString("evento"));
            }
            rs.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener combat log: " + e.getMessage());
        }
        
        return eventos;
    }
    
    /**
     * Guarda estadísticas de una batalla
     */
    public void guardarEstadisticasBatalla(int idBatalla, int mayorDanio, String personajeMayorDanio,
                                           int armasHero, int armasVillano,
                                           int supremosHero, int supremosVillano) {
        String sql = "INSERT INTO estadisticas_batalla (id_batalla, mayor_danio, personaje_mayor_danio, " +
                     "armas_heroe, armas_villano, supremos_heroe, supremos_villano) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, idBatalla);
            ps.setInt(2, mayorDanio);
            ps.setString(3, personajeMayorDanio);
            ps.setInt(4, armasHero);
            ps.setInt(5, armasVillano);
            ps.setInt(6, supremosHero);
            ps.setInt(7, supremosVillano);
            ps.executeUpdate();
            
        } catch (SQLException e) {
            // Si la tabla no existe, intentar crearla
            if (e.getMessage().contains("no such table")) {
                crearTablaEstadisticas();
                guardarEstadisticasBatalla(idBatalla, mayorDanio, personajeMayorDanio,
                                          armasHero, armasVillano, supremosHero, supremosVillano);
            } else {
                System.err.println("Error al guardar estadísticas: " + e.getMessage());
            }
        }
    }
    
    /**
     * Obtiene las estadísticas de una batalla
     */
    public void obtenerEstadisticasBatalla(BatallaInfo info) {
        String sql = "SELECT mayor_danio, personaje_mayor_danio, armas_heroe, armas_villano, " +
                     "supremos_heroe, supremos_villano FROM estadisticas_batalla WHERE id_batalla = ?";
        
        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, info.getId());
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                info.setMayorDanio(rs.getInt("mayor_danio"));
                info.setPersonajeMayorDanio(rs.getString("personaje_mayor_danio"));
                info.setHeroeArmasInvocadas(rs.getInt("armas_heroe"));
                info.setVillanoArmasInvocadas(rs.getInt("armas_villano"));
                info.setHeroeSupremosUsados(rs.getInt("supremos_heroe"));
                info.setVillanoSupremosUsados(rs.getInt("supremos_villano"));
            }
            rs.close();
            
        } catch (SQLException e) {
            System.err.println("Error al obtener estadísticas: " + e.getMessage());
        }
    }
    
    /**
     * Crea la tabla de estadísticas de batalla
     */
    private void crearTablaEstadisticas() {
        String sql = "CREATE TABLE IF NOT EXISTS estadisticas_batalla (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "id_batalla INTEGER NOT NULL UNIQUE, " +
                     "mayor_danio INTEGER DEFAULT 0, " +
                     "personaje_mayor_danio TEXT, " +
                     "armas_heroe INTEGER DEFAULT 0, " +
                     "armas_villano INTEGER DEFAULT 0, " +
                     "supremos_heroe INTEGER DEFAULT 0, " +
                     "supremos_villano INTEGER DEFAULT 0, " +
                     "FOREIGN KEY (id_batalla) REFERENCES batallas(id) ON DELETE CASCADE)";
        
        try (Connection conn = ConexionSQLite.conectar();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(sql);
            System.out.println("✓ Tabla estadisticas_batalla creada correctamente");
            
        } catch (SQLException e) {
            System.err.println("Error al crear tabla estadísticas: " + e.getMessage());
        }
    }
    
    /**
     * Crea la tabla de eventos de batalla si no existe
     */
    private void crearTablaCombatLog() {
        String sql = "CREATE TABLE IF NOT EXISTS eventos_batalla (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "id_batalla INTEGER NOT NULL, " +
                     "evento TEXT NOT NULL, " +
                     "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                     "FOREIGN KEY (id_batalla) REFERENCES batallas(id) ON DELETE CASCADE)";
        
        try (Connection conn = ConexionSQLite.conectar();
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(sql);
            System.out.println("✓ Tabla eventos_batalla creada correctamente");
            
        } catch (SQLException e) {
            System.err.println("Error al crear tabla eventos_batalla: " + e.getMessage());
        }
    }

    public static class BatallaInfo {
        private int id;
        private String fecha;
        private int turnos;

        private int heroeId;
        private String heroeNombre;
        private String heroeApodo;
        private int heroeVidaFinal;
        private int heroeArmasInvocadas;
        private int heroeSupremosUsados;

        private int villanoId;
        private String villanoNombre;
        private String villanoApodo;
        private int villanoVidaFinal;
        private int villanoArmasInvocadas;
        private int villanoSupremosUsados;

        private int ganadorId;
        private String ganadorNombre;
        
        private int mayorDanio;
        private String personajeMayorDanio;

        // getters / setters
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        public String getFecha() { return fecha; }
        public void setFecha(String fecha) { this.fecha = fecha; }
        public int getTurnos() { return turnos; }
        public void setTurnos(int turnos) { this.turnos = turnos; }

        public int getHeroeId() { return heroeId; }
        public void setHeroeId(int heroeId) { this.heroeId = heroeId; }
        public String getHeroeNombre() { return heroeNombre; }
        public void setHeroeNombre(String heroeNombre) { this.heroeNombre = heroeNombre; }
        public String getHeroeApodo() { return heroeApodo; }
        public void setHeroeApodo(String heroeApodo) { this.heroeApodo = heroeApodo; }
        public int getHeroeVidaFinal() { return heroeVidaFinal; }
        public void setHeroeVidaFinal(int heroeVidaFinal) { this.heroeVidaFinal = heroeVidaFinal; }
        public int getHeroeArmasInvocadas() { return heroeArmasInvocadas; }
        public void setHeroeArmasInvocadas(int heroeArmasInvocadas) { this.heroeArmasInvocadas = heroeArmasInvocadas; }
        public int getHeroeSupremosUsados() { return heroeSupremosUsados; }
        public void setHeroeSupremosUsados(int heroeSupremosUsados) { this.heroeSupremosUsados = heroeSupremosUsados; }

        public int getVillanoId() { return villanoId; }
        public void setVillanoId(int villanoId) { this.villanoId = villanoId; }
        public String getVillanoNombre() { return villanoNombre; }
        public void setVillanoNombre(String villanoNombre) { this.villanoNombre = villanoNombre; }
        public String getVillanoApodo() { return villanoApodo; }
        public void setVillanoApodo(String villanoApodo) { this.villanoApodo = villanoApodo; }
        public int getVillanoVidaFinal() { return villanoVidaFinal; }
        public void setVillanoVidaFinal(int villanoVidaFinal) { this.villanoVidaFinal = villanoVidaFinal; }
        public int getVillanoArmasInvocadas() { return villanoArmasInvocadas; }
        public void setVillanoArmasInvocadas(int villanoArmasInvocadas) { this.villanoArmasInvocadas = villanoArmasInvocadas; }
        public int getVillanoSupremosUsados() { return villanoSupremosUsados; }
        public void setVillanoSupremosUsados(int villanoSupremosUsados) { this.villanoSupremosUsados = villanoSupremosUsados; }

        public int getGanadorId() { return ganadorId; }
        public void setGanadorId(int ganadorId) { this.ganadorId = ganadorId; }
        public String getGanadorNombre() { return ganadorNombre; }
        public void setGanadorNombre(String ganadorNombre) { this.ganadorNombre = ganadorNombre; }
        
        public int getMayorDanio() { return mayorDanio; }
        public void setMayorDanio(int mayorDanio) { this.mayorDanio = mayorDanio; }
        public String getPersonajeMayorDanio() { return personajeMayorDanio; }
        public void setPersonajeMayorDanio(String personajeMayorDanio) { this.personajeMayorDanio = personajeMayorDanio; }
    }
}
