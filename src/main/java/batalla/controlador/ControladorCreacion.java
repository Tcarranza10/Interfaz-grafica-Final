package batalla.controlador;

import batalla.modelo.*;
import batalla.vista.PantallaCreacion;
import batalla.vista.PantallaBatalla;
import javax.swing.JOptionPane;


import batalla.Conexion.PersonajeDAO;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador para la pantalla de configuración inicial
 * Gestiona la creación de personajes y configuración de partidas
 */
public class ControladorCreacion {
    private PantallaCreacion vista;
    private ConfiguracionPartida config;
    private Random random = new Random();
    private Heroe heroeSeleccionado;
    private Villano villanoSeleccionado;

    public ControladorCreacion(PantallaCreacion vista) {
        this.vista = vista;
        this.config = new ConfiguracionPartida();
        configurarEventos();
        generarValoresAleatorios();
        cargarPersonajesEnCombos();
    }

    private void configurarEventos() {
        // Tab Crear
        vista.getBtnCrearH().addActionListener(e -> crearHeroe());
        vista.getBtnCrearV().addActionListener(e -> crearVillano());
        
        // Tab Cargar
        vista.getBtnBorrarH().addActionListener(e -> borrarHeroe());
        vista.getBtnBorrarV().addActionListener(e -> borrarVillano());
        vista.getCboxNombreHeroe().addActionListener(e -> seleccionarHeroe());
        vista.getCboxNombreVillano().addActionListener(e -> seleccionarVillano());
        
        // Botones principales
        vista.getBtnSiguiente().addActionListener(e -> siguiente());
        vista.getBtnVolver().addActionListener(e -> volver());
    }
    
    private void cargarPersonajesEnCombos() {
        try {
            PersonajeDAO dao = new PersonajeDAO();
            List<Personaje> todosPersonajes = dao.listarTodos();
            
            List<Heroe> heroes = new ArrayList<>();
            List<Villano> villanos = new ArrayList<>();
            
            for (Personaje p : todosPersonajes) {
                if (p instanceof Heroe) {
                    heroes.add((Heroe) p);
                } else if (p instanceof Villano) {
                    villanos.add((Villano) p);
                }
            }
            
            vista.getCboxNombreHeroe().removeAllItems();
            vista.getCboxNombreVillano().removeAllItems();
            
            for (Heroe h : heroes) {
                vista.getCboxNombreHeroe().addItem(h.getNombre() + " (" + h.getApodo() + ")");
            }
            
            for (Villano v : villanos) {
                vista.getCboxNombreVillano().addItem(v.getNombre() + " (" + v.getApodo() + ")");
            }
            
        } catch (Exception e) {
            System.err.println("Error al cargar personajes desde BD: " + e.getMessage());
            JOptionPane.showMessageDialog(vista, 
                "Error al cargar personajes desde la base de datos", 
                "Error", 
                JOptionPane.WARNING_MESSAGE);
        }
        
        actualizarEstadoBtnSiguiente();
    }
    
    private void crearHeroe() {
        String nombre = vista.getTxtNombreH().getText().trim();
        int vida = (int) vista.getSpnVidaH1().getValue();
        int fuerza = (int) vista.getSpnFuerzaH1().getValue();
        int defensa = (int) vista.getSpnDefensaH1().getValue();
        
        // Validaciones
        if (nombre.length() < 3 || nombre.length() > 10) {
            JOptionPane.showMessageDialog(vista, 
                "El nombre debe tener entre 3 y 10 caracteres", 
                "Error de validación", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!nombre.matches("[a-zA-Z\\s]+")) {
            JOptionPane.showMessageDialog(vista, 
                "El nombre solo puede contener letras y espacios", 
                "Error de validación", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (vida <= 0 || fuerza <= 0 || defensa <= 0) {
            JOptionPane.showMessageDialog(vista, 
                "Los valores deben ser positivos", 
                "Error de validación", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Generar apodo y bendición aleatoria (30-100)
        String apodo = generarApodo(nombre);    
        int bendicion = random.nextInt(71) + 30;
        
        Heroe heroe = new Heroe(nombre, apodo, vida, fuerza, defensa, bendicion);
        
        try {
            PersonajeDAO dao = new PersonajeDAO();
            dao.insertar(heroe);
            
            heroeSeleccionado = heroe;
            actualizarCamposHeroe(heroe);
            
            JOptionPane.showMessageDialog(vista, 
                "Héroe creado y guardado en la base de datos correctamente\n" +
                "Ya puedes hacer clic en 'Siguiente' para comenzar la batalla.", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, 
                "Error al guardar héroe en la base de datos: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        vista.getTxtNombreH().setText("");
        generarValoresAleatorios();
        cargarPersonajesEnCombos();
        
        actualizarEstadoBtnSiguiente();
        
    }

    private void crearVillano() {
        String nombre = vista.getTxtNombreV().getText().trim();
        int vida = (int) vista.getSpnVidaV1().getValue();
        int fuerza = (int) vista.getSpnFuerzaV1().getValue();
        int defensa = (int) vista.getSpnDefensaV1().getValue();
        
        // Validaciones
        if (nombre.length() < 3 || nombre.length() > 10) {
            JOptionPane.showMessageDialog(vista, 
                "El nombre debe tener entre 3 y 10 caracteres", 
                "Error de validación", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!nombre.matches("[a-zA-Z\\s]+")) {
            JOptionPane.showMessageDialog(vista, 
                "El nombre solo puede contener letras y espacios", 
                "Error de validación", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (vida <= 0 || fuerza <= 0 || defensa <= 0) {
            JOptionPane.showMessageDialog(vista, 
                "Los valores deben ser positivos", 
                "Error de validación", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Generar apodo y bendición aleatoria (30-100)
        String apodo = generarApodo(nombre);
        int bendicion = random.nextInt(71) + 30;
        
        Villano villano = new Villano(nombre, apodo, vida, fuerza, defensa, bendicion);
        // Guardar en BASE DE DATOS
        try {
            PersonajeDAO dao = new PersonajeDAO();
            dao.insertar(villano);
            
            villanoSeleccionado = villano;
            actualizarCamposVillano(villano);
            
            JOptionPane.showMessageDialog(vista, 
                "Villano creado y guardado en la base de datos correctamente\n" +
                "Ya puedes hacer clic en 'Siguiente' para comenzar la batalla.", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, 
                "Error al guardar villano en la base de datos: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        vista.getTxtNombreV().setText("");
        generarValoresAleatorios();
        cargarPersonajesEnCombos();
        
        actualizarEstadoBtnSiguiente();
        
    }
    
    private String generarApodo(String nombrePersonaje) {
        String[] apodos = {"El Valiente", "El Feroz", "El Sabio", "El Poderoso", 
            "El Oscuro", "El Destructor", "El Guardián", "El Temible"};
    
        return nombrePersonaje + " " + apodos[random.nextInt(apodos.length)];
    }
    
    private void generarValoresAleatorios() {
        // Vida: 100-160
        int vidaH1 = 100 + random.nextInt(61); // 100 a 160
        int vidaV1 = 100 + random.nextInt(61);
        vista.getSpnVidaH1().setValue(vidaH1);
        vista.getSpnVidaV1().setValue(vidaV1);
        
        // Fuerza: 15-25
        int fuerzaH1 = 15 + random.nextInt(11); // 15 a 25
        int fuerzaV1 = 15 + random.nextInt(11);
        vista.getSpnFuerzaH1().setValue(fuerzaH1);
        vista.getSpnFuerzaV1().setValue(fuerzaV1);
        
        // Defensa: 8-13
        int defensaH1 = 8 + random.nextInt(6); // 8 a 13
        int defensaV1 = 8 + random.nextInt(6);
        vista.getSpnDefensaH1().setValue(defensaH1);
        vista.getSpnDefensaV1().setValue(defensaV1);
    }
    
    private void seleccionarHeroe() {
        String seleccion = (String) vista.getCboxNombreHeroe().getSelectedItem();
        if (seleccion != null) {
            String nombre = seleccion.split(" \\(")[0];
            
            try {
                PersonajeDAO dao = new PersonajeDAO();
                List<Personaje> todosPersonajes = dao.listarTodos();
                
                for (Personaje p : todosPersonajes) {
                    if (p instanceof Heroe && p.getNombre().equals(nombre)) {
                        heroeSeleccionado = (Heroe) p;
                        actualizarCamposHeroe(heroeSeleccionado);
                        break;
                    }
                }
            } catch (Exception e) {
                System.err.println("Error al seleccionar héroe: " + e.getMessage());
            }
        }
        actualizarEstadoBtnSiguiente();
    }
    
    private void seleccionarVillano() {
        String seleccion = (String) vista.getCboxNombreVillano().getSelectedItem();
        if (seleccion != null) {
            String nombre = seleccion.split(" \\(")[0];
            
            // ============================================
            // CAMBIO: Cargar desde BASE DE DATOS
            // ============================================
            try {
                PersonajeDAO dao = new PersonajeDAO();
                List<Personaje> todosPersonajes = dao.listarTodos();
                
                for (Personaje p : todosPersonajes) {
                    if (p instanceof Villano && p.getNombre().equals(nombre)) {
                        villanoSeleccionado = (Villano) p;
                        actualizarCamposVillano(villanoSeleccionado);
                        break;
                    }
                }
            } catch (Exception e) {
                System.err.println("Error al seleccionar villano: " + e.getMessage());
            }
        }
        actualizarEstadoBtnSiguiente();
    }
    
    private void actualizarCamposHeroe(Heroe heroe) {
        vista.getSpnVidaH2().setValue(heroe.getVida());
        vista.getSpnFuerzaH2().setValue(heroe.getFuerza());
        vista.getSpnDefensaH2().setValue(heroe.getDefensa());
    }
    
    private void actualizarCamposVillano(Villano villano) {
        vista.getSpnVidaV2().setValue(villano.getVida());
        vista.getSpnFuerzaV2().setValue(villano.getFuerza());
        vista.getSpnDefensaV2().setValue(villano.getDefensa());
    }
    
    private void borrarHeroe() {
        if (heroeSeleccionado == null) {
            JOptionPane.showMessageDialog(vista, 
                "Debe seleccionar un héroe para borrar", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(vista, 
            "¿Está seguro de borrar este héroe de la base de datos?", 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            // ============================================
            // CAMBIO: Borrar de BASE DE DATOS
            // ============================================
            try {
                PersonajeDAO dao = new PersonajeDAO();
                // Nota: Necesitarías agregar un método delete() en PersonajeDAO
                // Por ahora solo limpiamos la selección
                // dao.eliminar(heroeSeleccionado.getId());
                
                JOptionPane.showMessageDialog(vista, 
                    "Héroe borrado correctamente\n(Implementar método DELETE en PersonajeDAO)", 
                    "Información", 
                    JOptionPane.INFORMATION_MESSAGE);
                    
                heroeSeleccionado = null;
                cargarPersonajesEnCombos();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(vista, 
                    "Error al borrar héroe: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void borrarVillano() {
        if (villanoSeleccionado == null) {
            JOptionPane.showMessageDialog(vista, 
                "Debe seleccionar un villano para borrar", 
                "Error", 
            JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirmacion = JOptionPane.showConfirmDialog(vista, 
            "¿Está seguro de borrar este villano de la base de datos?", 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            // ============================================
            // CAMBIO: Borrar de BASE DE DATOS
            // ============================================
            try {
                PersonajeDAO dao = new PersonajeDAO();
                // Nota: Necesitarías agregar un método delete() en PersonajeDAO
                // Por ahora solo limpiamos la selección
                // dao.eliminar(villanoSeleccionado.getId());
                
                JOptionPane.showMessageDialog(vista, 
                    "Villano borrado correctamente\n(Implementar método DELETE en PersonajeDAO)", 
                    "Información", 
                    JOptionPane.INFORMATION_MESSAGE);
                    
                villanoSeleccionado = null;
                cargarPersonajesEnCombos();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(vista, 
                    "Error al borrar villano: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void actualizarEstadoBtnSiguiente() {
        // Debug para ver qué está pasando
        System.out.println("=== Actualizando botón Siguiente ===");
        System.out.println("Héroe seleccionado: " + (heroeSeleccionado != null ? heroeSeleccionado.getNombre() : "NULL"));
        System.out.println("Villano seleccionado: " + (villanoSeleccionado != null ? villanoSeleccionado.getNombre() : "NULL"));
        
        boolean debeHabilitar = heroeSeleccionado != null && villanoSeleccionado != null;
        System.out.println("¿Habilitar botón?: " + debeHabilitar);
        System.out.println("====================================");
        
        vista.getBtnSiguiente().setEnabled(debeHabilitar);
    }

    private void siguiente() {
        System.out.println("=== Método siguiente() ===");
        System.out.println("Tab actual: " + vista.getjTabbedPane1().getSelectedIndex()); // 0=Crear, 1=Cargar
        
        // ============================================
        // Si estamos en tab "Crear" y hay personajes creados,
        // intentar recuperarlos de la base de datos
        // ============================================
        if (vista.getjTabbedPane1().getSelectedIndex() == 0) {
            System.out.println("→ Usuario en tab CREAR, verificando personajes...");
            
            // Si no hay personajes seleccionados, intentar cargar los últimos creados
            if (heroeSeleccionado == null || villanoSeleccionado == null) {
                System.out.println("→ Intentando cargar últimos personajes de BD...");
                cargarUltimosPersonajesCreados();
            }
        }
        
        // ============================================
        // Validación final
        // ============================================
        if (heroeSeleccionado == null && villanoSeleccionado == null) {
            JOptionPane.showMessageDialog(vista, 
                "Debe crear al menos un héroe y un villano antes de continuar.\n\n" +
                "Opciones:\n" +
                "1. Tab CREAR: Crea un héroe y un villano, luego haz clic en 'Siguiente'\n" +
                "2. Tab CARGAR: Selecciona un héroe y un villano de las listas", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (heroeSeleccionado == null) {
            JOptionPane.showMessageDialog(vista, 
                "Falta un HÉROE.\n\n" +
                "• En tab CREAR: Escribe un nombre y haz clic en 'Crear Heroe'\n" +
                "• En tab CARGAR: Selecciona un héroe de la lista", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (villanoSeleccionado == null) {
            JOptionPane.showMessageDialog(vista, 
                "Falta un VILLANO.\n\n" +
                "• En tab CREAR: Escribe un nombre y haz clic en 'Crear Villano'\n" +
                "• En tab CARGAR: Selecciona un villano de la lista", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // ============================================
        // DEBUG
        // ============================================
        System.out.println("=== Iniciando batalla ===");
        System.out.println("Héroe: " + heroeSeleccionado.getNombre() + " (" + heroeSeleccionado.getApodo() + ")");
        System.out.println("Villano: " + villanoSeleccionado.getNombre() + " (" + villanoSeleccionado.getApodo() + ")");
        
        // ============================================
        // Crear copias para la batalla
        // ============================================
        Heroe heroeCopia = new Heroe(
            heroeSeleccionado.getNombre(),
            heroeSeleccionado.getApodo(),
            heroeSeleccionado.getVidaMaxima(),
            heroeSeleccionado.getFuerza(),
            heroeSeleccionado.getDefensa(),
            heroeSeleccionado.getBendicionesIniciales()
        );
        
        Villano villanoCopia = new Villano(
            villanoSeleccionado.getNombre(),
            villanoSeleccionado.getApodo(),
            villanoSeleccionado.getVidaMaxima(),
            villanoSeleccionado.getFuerza(),
            villanoSeleccionado.getDefensa(),
            villanoSeleccionado.getBendicionesIniciales()
        );
        
        config.agregarPersonaje(heroeCopia);
        config.agregarPersonaje(villanoCopia);
        
        System.out.println("✓ Personajes agregados a la configuración");
        System.out.println("========================\n");
        
        // Abrir pantalla de batalla
        PantallaBatalla pantallaBatalla = new PantallaBatalla();
        ControladorBatalla controladorBatalla = new ControladorBatalla(pantallaBatalla, config);
        controladorBatalla.iniciar();
        
        vista.dispose();
    }

    // ============================================
    // NUEVO MÉTODO: Cargar últimos personajes creados
    // ============================================
    private void cargarUltimosPersonajesCreados() {
        try {
            PersonajeDAO dao = new PersonajeDAO();
            List<Personaje> todos = dao.listarTodos();
            
            if (todos.isEmpty()) {
                System.out.println("✗ No hay personajes en la BD");
                return;
            }
            
            // Buscar el último héroe y el último villano
            Heroe ultimoHeroe = null;
            Villano ultimoVillano = null;
            
            for (int i = todos.size() - 1; i >= 0; i--) {
                Personaje p = todos.get(i);
                
                if (p instanceof Heroe && ultimoHeroe == null) {
                    ultimoHeroe = (Heroe) p;
                } else if (p instanceof Villano && ultimoVillano == null) {
                    ultimoVillano = (Villano) p;
                }
                
                // Si ya encontramos ambos, salir del bucle
                if (ultimoHeroe != null && ultimoVillano != null) {
                    break;
                }
            }
            
            // Asignar los personajes encontrados
            if (heroeSeleccionado == null && ultimoHeroe != null) {
                heroeSeleccionado = ultimoHeroe;
                System.out.println("✓ Héroe cargado de BD: " + ultimoHeroe.getNombre());
            }
            
            if (villanoSeleccionado == null && ultimoVillano != null) {
                villanoSeleccionado = ultimoVillano;
                System.out.println("✓ Villano cargado de BD: " + ultimoVillano.getNombre());
            }
            
            // Actualizar estado del botón
            actualizarEstadoBtnSiguiente();
            
        } catch (Exception e) {
            System.err.println("✗ Error al cargar últimos personajes: " + e.getMessage());
        }
    }

    private void volver() {
        batalla.vista.PantallaPrincipal pantallaPrincipal = new batalla.vista.PantallaPrincipal();
        ControladorPrincipal controladorPrincipal = new ControladorPrincipal(pantallaPrincipal);
        controladorPrincipal.iniciar();
        vista.dispose();
    }

    public void iniciar() {
        vista.setVisible(true);
    }
}
