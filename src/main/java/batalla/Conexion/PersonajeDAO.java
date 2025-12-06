package batalla.Conexion;

import batalla.modelo.Personaje;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonajeDAO {

    // INSERT - Agregar un personaje nuevo
    public void insertar(Personaje p) {

        String sql = "INSERT INTO personajes "
                   + "(nombre, apodo, tipo, vida_final, victorias, derrotas, supremos_usados, armas_invocadas) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getApodo());
            ps.setString(3, p.getTipo());
            ps.setInt(4, p.getVida()); // vida_final
            ps.setInt(5, p.getVictorias());
            ps.setInt(6, p.getDerrotas());
            ps.setInt(7, p.getAtaquesSupremosUsados());
            ps.setInt(8, p.getArmasInvocadas());

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al insertar personaje: " + e.getMessage());
        }
    }

    // UPDATE - Actualizar estadísticas luego de una batalla
    public void actualizarEstadisticas(Personaje p) {

        String sql = "UPDATE personajes SET "
                   + "vida_final = ?, victorias = ?, derrotas = ?, supremos_usados = ?, armas_invocadas = ? "
                   + "WHERE apodo = ?";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, p.getVida());
            ps.setInt(2, p.getVictorias());
            ps.setInt(3, p.getDerrotas());
            ps.setInt(4, p.getAtaquesSupremosUsados());
            ps.setInt(5, p.getArmasInvocadas());
            ps.setString(6, p.getApodo());

            ps.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error al actualizar estadísticas: " + e.getMessage());
        }
    }

    // SELECT - Obtener un personaje por apodo (UNIQUE)
    public Personaje obtenerPorApodo(String apodo) {

        String sql = "SELECT * FROM personajes WHERE apodo = ?";
        Personaje p = null;

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, apodo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = mapearPersonaje(rs);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar personaje: " + e.getMessage());
        }

        return p;
    }

    // SELECT - Listar todos los personajes
    public List<Personaje> listarTodos() {

        List<Personaje> lista = new ArrayList<>();
        String sql = "SELECT * FROM personajes";

        try (Connection conn = ConexionSQLite.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearPersonaje(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar personajes: " + e.getMessage());
        }

        return lista;
    }

    // SELECT - Obtener ranking (ordenado por victorias)
    public List<Personaje> obtenerRanking() {

        List<Personaje> lista = new ArrayList<>();
        String sql = "SELECT * FROM personajes ORDER BY victorias DESC";

        try (Connection conn = ConexionSQLite.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                lista.add(mapearPersonaje(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener ranking: " + e.getMessage());
        }

        return lista;
    }

    // MÉTODO PRIVADO - Convertir fila SQL → Objeto Personaje
    private Personaje mapearPersonaje(ResultSet rs) throws SQLException {

        Personaje p = new Personaje(
            rs.getString("nombre"),
            rs.getString("apodo"),
            rs.getString("tipo"),
            rs.getInt("vida_final"),
            0, // fuerza NO se guarda en la BD
            0, // defensa NO se guarda en la BD
            0  // bendiciones NO se guarda en la BD
        ) {
            @Override
            public void invocarArma() {}
            @Override
            public void decidirAccion(Personaje enemigo) {}
        };

        p.setId(rs.getInt("id"));
        p.setVictorias(rs.getInt("victorias"));
        p.setDerrotas(rs.getInt("derrotas"));
        p.setAtaquesSupremosUsados(rs.getInt("supremos_usados"));
        p.setArmasInvocadas(rs.getInt("armas_invocadas"));

        return p;
    }

    // ================================================================
    // ASEGURAR PERSONAJE EN BD
    // Si existe → devuelve su ID
    // Si no existe → lo inserta y devuelve el ID nuevo
    // También actualiza el objeto Personaje con p.setId(id)
    // ================================================================
    public int asegurarPersonajeEnBD(Personaje p) {

        if (p == null) return -1;

        // 1) BUSCAR PERSONAJE POR APODO (que es UNIQUE)
        String sqlBuscar = "SELECT id FROM personajes WHERE apodo = ? LIMIT 1";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sqlBuscar)) {

            ps.setString(1, p.getApodo());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int idExistente = rs.getInt("id");
                    p.setId(idExistente);   // ← asignar ID al objeto
                    return idExistente;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar personaje: " + e.getMessage());
        }

        // 2) SI NO EXISTE → INSERTARLO
        String sqlInsert = "INSERT INTO personajes "
                + "(nombre, apodo, tipo, vida_final, victorias, derrotas, supremos_usados, armas_invocadas) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionSQLite.conectar();
             PreparedStatement ps = conn.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, p.getNombre());
            ps.setString(2, p.getApodo());
            ps.setString(3, p.getTipo());
            ps.setInt(4, p.getVida());
            ps.setInt(5, p.getVictorias());
            ps.setInt(6, p.getDerrotas());
            ps.setInt(7, p.getAtaquesSupremosUsados());
            ps.setInt(8, p.getArmasInvocadas());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int nuevoId = rs.getInt(1);
                p.setId(nuevoId);   // ← asignar ID al objeto
                return nuevoId;
            }

        } catch (SQLException e) {
            System.err.println("Error al insertar personaje: " + e.getMessage());
        }

        return -1;
    }

}
