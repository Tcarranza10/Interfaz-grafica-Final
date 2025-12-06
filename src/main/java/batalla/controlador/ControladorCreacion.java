package batalla.controlador;

import batalla.modelo.*;
import batalla.vista.PantallaCreacion;
import batalla.vista.PantallaBatalla;
import javax.swing.JOptionPane;

import java.util.Random;
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
        List<Heroe> heroes = GestorPersistencia.cargarHeroes();
        List<Villano> villanos = GestorPersistencia.cargarVillanos();
        
        vista.getCboxNombreHeroe().removeAllItems();
        vista.getCboxNombreVillano().removeAllItems();
        
        for (Heroe h : heroes) {
            vista.getCboxNombreHeroe().addItem(h.getNombre() + " (" + h.getApodo() + ")");
        }
        
        for (Villano v : villanos) {
            vista.getCboxNombreVillano().addItem(v.getNombre() + " (" + v.getApodo() + ")");
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
        String apodo = generarApodo();
        int bendicion = random.nextInt(71) + 30; // 30-100
        
        Heroe heroe = new Heroe(nombre, apodo, vida, fuerza, defensa, bendicion);
        GestorPersistencia.guardarPersonaje(heroe);
        
        JOptionPane.showMessageDialog(vista, 
            "Héroe creado y guardado correctamente", 
            "Éxito", 
            JOptionPane.INFORMATION_MESSAGE);
        
        vista.getTxtNombreH().setText("");
        // Generar nuevos valores aleatorios para los spinners
        generarValoresAleatorios();
        cargarPersonajesEnCombos();
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
        String apodo = generarApodo();
        int bendicion = random.nextInt(71) + 30; // 30-100
        
        Villano villano = new Villano(nombre, apodo, vida, fuerza, defensa, bendicion);
        GestorPersistencia.guardarPersonaje(villano);
        
        JOptionPane.showMessageDialog(vista, 
            "Villano creado y guardado correctamente", 
            "Éxito", 
            JOptionPane.INFORMATION_MESSAGE);
        
        vista.getTxtNombreV().setText("");
        // Generar nuevos valores aleatorios para los spinners
        generarValoresAleatorios();
        cargarPersonajesEnCombos();
    }
    
    private String generarApodo() {
        String[] apodos = {"El Valiente", "El Feroz", "El Sabio", "El Poderoso", 
                          "El Oscuro", "El Destructor", "El Guardián", "El Temible"};
        return apodos[random.nextInt(apodos.length)];
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
            List<Heroe> heroes = GestorPersistencia.cargarHeroes();
            for (Heroe h : heroes) {
                if (h.getNombre().equals(nombre)) {
                    heroeSeleccionado = h;
                    actualizarCamposHeroe(h);
                    break;
                }
            }
        }
        actualizarEstadoBtnSiguiente();
    }
    
    private void seleccionarVillano() {
        String seleccion = (String) vista.getCboxNombreVillano().getSelectedItem();
        if (seleccion != null) {
            String nombre = seleccion.split(" \\(")[0];
            List<Villano> villanos = GestorPersistencia.cargarVillanos();
            for (Villano v : villanos) {
                if (v.getNombre().equals(nombre)) {
                    villanoSeleccionado = v;
                    actualizarCamposVillano(v);
                    break;
                }
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
            "¿Está seguro de borrar este héroe?", 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            List<Personaje> personajes = GestorPersistencia.cargarPersonajes();
            personajes.removeIf(p -> p.getNombre().equals(heroeSeleccionado.getNombre()) && p instanceof Heroe);
            GestorPersistencia.guardarPersonajes(personajes);
            heroeSeleccionado = null;
            cargarPersonajesEnCombos();
            JOptionPane.showMessageDialog(vista, "Héroe borrado correctamente");
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
            "¿Está seguro de borrar este villano?", 
            "Confirmar", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            List<Personaje> personajes = GestorPersistencia.cargarPersonajes();
            personajes.removeIf(p -> p.getNombre().equals(villanoSeleccionado.getNombre()) && p instanceof Villano);
            GestorPersistencia.guardarPersonajes(personajes);
            villanoSeleccionado = null;
            cargarPersonajesEnCombos();
            JOptionPane.showMessageDialog(vista, "Villano borrado correctamente");
        }
    }
    
    private void actualizarEstadoBtnSiguiente() {
        vista.getBtnSiguiente().setEnabled(heroeSeleccionado != null && villanoSeleccionado != null);
    }

    private void siguiente() {
        if (heroeSeleccionado == null || villanoSeleccionado == null) {
            JOptionPane.showMessageDialog(vista, 
                "Debe seleccionar un héroe y un villano en el tab Cargar", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Verificar que hay personajes guardados
        List<Heroe> heroes = GestorPersistencia.cargarHeroes();
        List<Villano> villanos = GestorPersistencia.cargarVillanos();
        
        if (heroes.isEmpty() || villanos.isEmpty()) {
            JOptionPane.showMessageDialog(vista, 
                "Debe crear al menos un héroe y un villano antes de continuar", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Crear copias para la batalla usando valores iniciales
        Heroe heroeCopia = new Heroe(
            heroeSeleccionado.getNombre(),
            heroeSeleccionado.getApodo(),
            heroeSeleccionado.getVidaMaxima(), // Usar vida inicial
            heroeSeleccionado.getFuerza(),
            heroeSeleccionado.getDefensa(),
            heroeSeleccionado.getBendicionesIniciales() // Usar bendiciones iniciales
        );
        
        Villano villanoCopia = new Villano(
            villanoSeleccionado.getNombre(),
            villanoSeleccionado.getApodo(),
            villanoSeleccionado.getVidaMaxima(), // Usar vida inicial
            villanoSeleccionado.getFuerza(),
            villanoSeleccionado.getDefensa(),
            villanoSeleccionado.getBendicionesIniciales() // Usar bendiciones iniciales
        );
        
        config.agregarPersonaje(heroeCopia);
        config.agregarPersonaje(villanoCopia);
        
        // Abrir pantalla de batalla
        PantallaBatalla pantallaBatalla = new PantallaBatalla();
        ControladorBatalla controladorBatalla = new ControladorBatalla(pantallaBatalla, config);
        controladorBatalla.iniciar();
        
        vista.dispose();
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
