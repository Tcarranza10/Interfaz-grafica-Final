package batalla.controlador;

import batalla.Conexion.BatallaDAO;
import batalla.Conexion.PersonajeDAO;
import batalla.modelo.*;
import batalla.vista.PantallaBatalla;
import batalla.vista.PantallaResultado;

import javax.swing.Timer; // ← IMPORTANTE: javax.swing.Timer, NO java.util.Timer
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

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
    private Timer timer; // ✅ NUEVO: Timer para controlar turnos
    
    private List<String> combatLog = new ArrayList<>();
    private int mayorDanio = 0;
    private String personajeMayorDanio = "";
    private int batallaMasLarga = 0;
    private String ganadorBatallaMasLarga = "";

    public ControladorBatalla(PantallaBatalla vista, ConfiguracionPartida config) {
        this.vista = vista;
        this.config = config;
        this.heroe = (Heroe) config.getHeroe();
        this.villano = (Villano) config.getVillano();
        this.turnoHeroe = random.nextBoolean();
        configurarEventos();
    }
    
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
        int cantidadBatallas = (int) vista.getSpnNumBatallas().getValue();
        boolean ataquesSupremos = vista.getChkAtkSupremos().isSelected();
        
        config.setCantidadBatallas(cantidadBatallas);
        config.setAtaquesSupremosActivados(ataquesSupremos);
        
        vista.getBtnIniciar().setEnabled(false);
        
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
        
        iniciarTimerTurnos();
    }
    
    private void iniciarTimerTurnos() {
        // Detener timer anterior si existe
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        
        // Crear nuevo timer que ejecuta cada 2 segundos
        timer = new Timer(1000, e -> {
            if (!pausado) {
                ejecutarTurno();
            }
        });
        
        timer.start();
    }
    
    private void ejecutarTurno() {
        // Verificar si la batalla terminó
        if (!heroe.estaVivo() || !villano.estaVivo()) {
            timer.stop(); 
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
        
        if (danioCausado > mayorDanio) {
            mayorDanio = danioCausado;
            personajeMayorDanio = atacante;
        }
        
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
        vista.agregarLog("─────────────────────────────"); // Separador visual
        vista.agregarLog("");
        combatLog.add(logHeroe);
        combatLog.add(logVillano);
        
        turnoHeroe = !turnoHeroe;
        turno++;
        actualizarInformacionPartida();
        actualizarEstadoPersonajes();
    }
    
    private void actualizarInformacionPartida() {
        vista.setInfoPartida("Batalla: " + batallaActual + "/" + config.getCantidadBatallas());
        vista.setTurno("Turno: " + turno);
    }
    
    private void actualizarEstadoPersonajes() {
        vista.actualizarEstadoPersonaje(heroe);
        vista.actualizarEstadoPersonaje(villano);
    }

    private void finalizarBatalla() {
        String logFinal = "=== BATALLA FINALIZADA ===";
        vista.agregarLog(logFinal);
        combatLog.add(logFinal);
        
        Personaje ganador;
        String logGanador;
        if (heroe.estaVivo()) {
            ganador = heroe;
            heroe.incrementarVictoria();
            villano.incrementarDerrota();
            logGanador = heroe.getNombre() + " ha triunfado!";
            vista.agregarLog(logGanador);
            combatLog.add(logGanador);
        } else {
            ganador = villano;
            villano.incrementarVictoria();
            heroe.incrementarDerrota();
            logGanador = villano.getNombre() + " ha vencido!";
            vista.agregarLog(logGanador);
            combatLog.add(logGanador);
        }
        
        if (turno > batallaMasLarga) {
            batallaMasLarga = turno;
            ganadorBatallaMasLarga = ganador.getNombre();
        }
        
        guardarBatallaEnBD(ganador, turno);
        
        batallaActual++;
        if (batallaActual > config.getCantidadBatallas()) {
            finalizarTodasLasBatallas();
        } else {
            reiniciarParaSiguienteBatalla();
        }
    }
    
    private void guardarBatallaEnBD(Personaje ganador, int turnos) {
        try {
            PersonajeDAO pdao = new PersonajeDAO();
            
            int heroeId = pdao.asegurarPersonajeEnBD(heroe);
            int villanoId = pdao.asegurarPersonajeEnBD(villano);
            int ganadorId = pdao.asegurarPersonajeEnBD(ganador);
            
            if (heroeId > 0 && villanoId > 0 && ganadorId > 0) {
                pdao.actualizarEstadisticas(heroe);
                pdao.actualizarEstadisticas(villano);
                
                BatallaDAO batallaDAO = new BatallaDAO();
                batallaDAO.insertarBatalla(heroe, villano, ganador, turnos);
                
                System.out.println("✓ Batalla guardada en BD correctamente");
            } else {
                System.err.println("✗ Error: IDs inválidos");
            }
        } catch (Exception e) {
            System.err.println("✗ Error al guardar batalla en BD: " + e.getMessage());
        }
    }
    
    private void reiniciarParaSiguienteBatalla() {
        String logPreparacion = "=== PREPARANDO SIGUIENTE BATALLA ===";
        vista.agregarLog("");
        vista.agregarLog(logPreparacion);
        combatLog.add("");
        combatLog.add(logPreparacion);
        
        // Usar Timer para el delay entre batallas
        Timer delayTimer = new Timer(3000, e -> {
            iniciarBatalla();
        });
        delayTimer.setRepeats(false); // Solo una vez
        delayTimer.start();
    }
    
    private void finalizarTodasLasBatallas() {
        // Detener timer si está corriendo
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        
        GestorPersistencia.guardarPersonajes(config.getPersonajes());
        
        PartidaGuardada partida = new PartidaGuardada();
        partida.setHeroeNombre(heroe.getNombre());
        partida.setHeroeApodo(heroe.getApodo());
        partida.setVillanoNombre(villano.getNombre());
        partida.setVillanoApodo(villano.getApodo());
        partida.setCantidadBatallas(config.getCantidadBatallas());
        partida.setAtaquesSupremosActivados(config.isAtaquesSupremosActivados());
        partida.setCombatLog(combatLog);
        
        PartidaGuardada.EstadisticaBatalla estadistica = new PartidaGuardada.EstadisticaBatalla();
        estadistica.setMayorDanio(mayorDanio);
        estadistica.setPersonajeMayorDanio(personajeMayorDanio);
        estadistica.setTurnos(batallaMasLarga);
        estadistica.setGanador(ganadorBatallaMasLarga);
        estadistica.setArmasInvocadasHeroe(heroe.getArmasInvocadas());
        estadistica.setArmasInvocadasVillano(villano.getArmasInvocadas());
        estadistica.setAtaquesSupremosHeroe(heroe.getAtaquesSupremosUsados());
        estadistica.setAtaquesSupremosVillano(villano.getAtaquesSupremosUsados());
        partida.getEstadisticas().add(estadistica);
        
        javax.swing.SwingUtilities.invokeLater(() -> {
            PantallaResultado pantallaResultado = new PantallaResultado();
            
            List<Personaje> personajesFinales = new ArrayList<>();
            personajesFinales.add(heroe);
            personajesFinales.add(villano);
            
            ControladorResultado controladorResultado = new ControladorResultado(
                pantallaResultado, 
                personajesFinales,
                config.getCantidadBatallas(), 
                partida,
                mayorDanio, 
                personajeMayorDanio, 
                batallaMasLarga, 
                ganadorBatallaMasLarga
            );
            controladorResultado.iniciar();
            vista.dispose();
        });
    }
}