package batalla.controlador;

import batalla.modelo.*;
import batalla.vista.PantallaBatalla;
import batalla.vista.PantallaResultado;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para la pantalla de batalla
 * Gestiona la lógica de combate entre héroe y villano
 */
public class ControladorBatalla {
    private PantallaBatalla vista;
    private ConfiguracionPartida config;
    private Heroe heroe;
    private Villano villano;
    private int turno = 1;
    private int batallaActual = 1;
    private Random random = new Random();
    private boolean turnoHeroe;
    private boolean pausado = false;

    // Constructor con ConfiguracionPartida
    public ControladorBatalla(PantallaBatalla vista, ConfiguracionPartida config) {
        this.vista = vista;
        this.config = config;
        this.heroe = (Heroe) config.getHeroe();
        this.villano = (Villano) config.getVillano();
        this.turnoHeroe = random.nextBoolean();
        configurarEventos();
    }
    
    // Constructor de compatibilidad
    public ControladorBatalla(PantallaBatalla vista, Heroe heroe, Villano villano) {
        this.vista = vista;
        this.heroe = heroe;
        this.villano = villano;
        this.turnoHeroe = random.nextBoolean();
        this.config = new ConfiguracionPartida();
        config.agregarPersonaje(heroe);
        config.agregarPersonaje(villano);
        configurarEventos();
    }
    
    private void configurarEventos() {
        vista.getBtnIniciar().addActionListener(e -> iniciarBatallas());
    }
    
    private void iniciarBatallas() {
        // Obtener configuración de la vista
        int cantidadBatallas = (int) vista.getSpnNumBatallas().getValue();
        boolean ataquesSupremos = vista.getChkAtkSupremos().isSelected();
        
        config.setCantidadBatallas(cantidadBatallas);
        config.setAtaquesSupremosActivados(ataquesSupremos);
        
        // Desactivar botón
        vista.getBtnIniciar().setEnabled(false);
        
        // Iniciar primera batalla
        batallaActual = 1;
        turno = 1;
        iniciarBatalla();
    }

    public void iniciar() {
        vista.setVisible(true);
        vista.limpiarLog();
        vista.setInfoPartida("Batalla: 0/" + config.getCantidadBatallas());
        vista.setTurno("Turno: 0");
    }
    
    private void iniciarBatalla() {
        // Restaurar todas las estadísticas iniciales antes de cada batalla
        heroe.restaurarEstadisticasIniciales();
        villano.restaurarEstadisticasIniciales();
        
        turno = 1;
        turnoHeroe = random.nextBoolean();
        
        actualizarInformacionPartida();
        String logInicio = "=== COMIENZA LA BATALLA " + batallaActual + " ===";
        String logContendientes = heroe.getNombre() + " (" + heroe.getApodo() + ") vs " + villano.getNombre() + " (" + villano.getApodo() + ")";
        vista.agregarLog(logInicio);
        vista.agregarLog(logContendientes);
        vista.agregarLog("");
        combatLog.add(logInicio);
        combatLog.add(logContendientes);
        combatLog.add("");
        
        actualizarEstadoPersonajes();
        
        // Iniciar el ciclo de batalla
        siguienteTurno();
    }
    
    private void actualizarInformacionPartida() {
        vista.setInfoPartida("Batalla: " + batallaActual + "/" + config.getCantidadBatallas());
        vista.setTurno("Turno: " + turno);
    }
    
    private void actualizarEstadoPersonajes() {
        vista.actualizarEstadoPersonaje(heroe);
        vista.actualizarEstadoPersonaje(villano);
    }

    public void siguienteTurno() {
        if (!heroe.estaVivo() || !villano.estaVivo()) {
            finalizarBatalla();
            return;
        }

        String logTurno = "--- TURNO " + turno + " ---";
        vista.agregarLog(logTurno);
        combatLog.add(logTurno);
        
        int danioCausado = 0;
        String atacante = "";
        
        if (turnoHeroe) {
            String logTurnoHeroe = "Turno de " + heroe.getNombre();
            vista.agregarLog(logTurnoHeroe);
            combatLog.add(logTurnoHeroe);
            
            // Usar ataque supremo si tiene 100% bendiciones y está activado
            if (config.isAtaquesSupremosActivados() && heroe.getBendiciones() >= 100) {
                String logSupremo = heroe.getNombre() + " tiene 100% de bendiciones! Usando ataque supremo!";
                vista.agregarLog(logSupremo);
                combatLog.add(logSupremo);
                int vidaAntes = villano.getVida();
                heroe.usarAtaqueSupremo(villano);
                danioCausado = vidaAntes - villano.getVida();
                atacante = heroe.getNombre();
            } else {
                int vidaAntes = villano.getVida();
                heroe.decidirAccion(villano);
                danioCausado = vidaAntes - villano.getVida();
                atacante = heroe.getNombre();
            }
        } else {
            String logTurnoVillano = "Turno de " + villano.getNombre();
            vista.agregarLog(logTurnoVillano);
            combatLog.add(logTurnoVillano);
            
            // Usar ataque supremo si tiene 100% bendiciones y está activado
            if (config.isAtaquesSupremosActivados() && villano.getBendiciones() >= 100 && !villano.isLeviatanInvocado()) {
                String logSupremo = villano.getNombre() + " tiene 100% de maldiciones! Invocando Leviatán!";
                vista.agregarLog(logSupremo);
                combatLog.add(logSupremo);
                villano.invocarLeviatan();
            } else {
                int vidaAntes = heroe.getVida();
                villano.decidirAccion(heroe);
                danioCausado = vidaAntes - heroe.getVida();
                atacante = villano.getNombre();
            }
        }
        
        // Actualizar mayor daño
        if (danioCausado > mayorDanio) {
            mayorDanio = danioCausado;
            personajeMayorDanio = atacante;
        }
        
        // Mostrar estado actual
        vista.agregarLog("");
        String logEstado = "Estado actual:";
        vista.agregarLog(logEstado);
        combatLog.add("");
        combatLog.add(logEstado);
        
        String logHeroe = heroe.getNombre() + ": " + heroe.getVida() + " vida | " + heroe.getBendiciones() + "% bendiciones";
        String logVillano = villano.getNombre() + ": " + villano.getVida() + " vida | " + villano.getBendiciones() + "% maldiciones";
        vista.agregarLog(logHeroe);
        vista.agregarLog(logVillano);
        vista.agregarLog("");
        combatLog.add(logHeroe);
        combatLog.add(logVillano);
        
        turnoHeroe = !turnoHeroe;
        turno++;
        actualizarInformacionPartida();
        actualizarEstadoPersonajes();
        
        // Continuar automáticamente si ambos están vivos y no está pausado
        if (heroe.estaVivo() && villano.estaVivo() && !pausado) {
            javax.swing.SwingUtilities.invokeLater(() -> {
                try {
                    Thread.sleep(1000); // Pausa de 1 segundo entre turnos
                    if (!pausado) {
                        siguienteTurno();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        } else if (!pausado) {
            finalizarBatalla();
        }
    }
    
    private List<String> combatLog = new ArrayList<>();
    private int mayorDanio = 0;
    private String personajeMayorDanio = "";
    private int batallaMasLarga = 0;
    private String ganadorBatallaMasLarga = "";

    private void finalizarBatalla() {
        String logFinal = "=== BATALLA FINALIZADA ===";
        vista.agregarLog(logFinal);
        combatLog.add(logFinal);
        
        Personaje ganador;
        String logGanador;
        if (heroe.estaVivo()) {
            ganador = heroe;
            heroe.incrementarVictoria();
            logGanador = heroe.getNombre() + " ha triunfado!";
            vista.agregarLog(logGanador);
            combatLog.add(logGanador);
        } else {
            ganador = villano;
            villano.incrementarVictoria();
            logGanador = villano.getNombre() + " ha vencido!";
            vista.agregarLog(logGanador);
            combatLog.add(logGanador);
        }
        
        // Actualizar estadísticas
        if (turno > batallaMasLarga) {
            batallaMasLarga = turno;
            ganadorBatallaMasLarga = ganador.getNombre();
        }
        
        // Verificar si hay más batallas
        batallaActual++;
        if (batallaActual > config.getCantidadBatallas()) {
            // Todas las batallas completadas
            finalizarTodasLasBatallas();
        } else {
            // Reiniciar para siguiente batalla
            reiniciarParaSiguienteBatalla();
        }
    }
    
    private void reiniciarParaSiguienteBatalla() {
        String logPreparacion = "=== PREPARANDO SIGUIENTE BATALLA ===";
        vista.agregarLog("");
        vista.agregarLog(logPreparacion);
        combatLog.add("");
        combatLog.add(logPreparacion);
        
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                Thread.sleep(2000);
                iniciarBatalla();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
    
    private void finalizarTodasLasBatallas() {
        // Guardar estadísticas de personajes
        GestorPersistencia.guardarPersonajes(config.getPersonajes());
        
        // Crear partida guardada con todos los datos
        PartidaGuardada partida = new PartidaGuardada();
        partida.setHeroeNombre(heroe.getNombre());
        partida.setHeroeApodo(heroe.getApodo());
        partida.setVillanoNombre(villano.getNombre());
        partida.setVillanoApodo(villano.getApodo());
        partida.setCantidadBatallas(config.getCantidadBatallas());
        partida.setAtaquesSupremosActivados(config.isAtaquesSupremosActivados());
        partida.setCombatLog(combatLog);
        
        // Abrir pantalla de resultados
        javax.swing.SwingUtilities.invokeLater(() -> {
            PantallaResultado pantallaResultado = new PantallaResultado();
            ControladorResultado controladorResultado = new ControladorResultado(
                pantallaResultado, config.getPersonajes(), config.getCantidadBatallas(), partida,
                mayorDanio, personajeMayorDanio, batallaMasLarga, ganadorBatallaMasLarga);
            controladorResultado.iniciar();
            vista.dispose();
        });
    }
}

