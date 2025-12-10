===============================================
SCRIPTS PARA CREAR LA BASE DE DATOS SQLite
===============================================

-- Crear tabla de personajes
CREATE TABLE personajes (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL UNIQUE,
    tipo TEXT NOT NULL,
    vida INTEGER NOT NULL,
    bendiciones TEXT
);

-- Crear tabla de batallas
CREATE TABLE batallas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    heroe_id INTEGER NOT NULL,
    villano_id INTEGER NOT NULL,
    ganador TEXT NOT NULL,
    turnos INTEGER NOT NULL,
    fecha_batalla DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (heroe_id) REFERENCES personajes(id),
    FOREIGN KEY (villano_id) REFERENCES personajes(id)
);

-- Crear tabla de eventos de batalla
CREATE TABLE eventos_batalla (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    batalla_id INTEGER NOT NULL,
    turno INTEGER NOT NULL,
    evento TEXT NOT NULL,
    FOREIGN KEY (batalla_id) REFERENCES batallas(id)
);

-- Crear tabla de estadísticas de batalla
CREATE TABLE estadisticas_batalla (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    batalla_id INTEGER NOT NULL,
    heroe_armas_invocadas INTEGER,
    heroe_supremos_usados INTEGER,
    villano_armas_invocadas INTEGER,
    villano_supremos_usados INTEGER,
    mayor_danio INTEGER,
    personaje_mayor_danio TEXT,
    FOREIGN KEY (batalla_id) REFERENCES batallas(id)
);

===============================================
DATOS DE EJEMPLO PARA INSERTAR
===============================================

-- Insertar personajes de ejemplo

INSERT INTO personajes (nombre, tipo, vida, bendiciones) VALUES
('Juan', 'Heroe', 100, '{"bendicion1": "Bendición Celestial", "bendicion2": "Bendición del Vacío"}');

INSERT INTO personajes (nombre, tipo, vida, bendiciones) VALUES
('Pika', 'Villano', 100, '{"bendicion1": "Bendición del Vacío"}');

INSERT INTO personajes (nombre, tipo, vida, bendiciones) VALUES
('Saku', 'Heroe', 100, '{"bendicion1": "Bendición Celestial"}');

-- Los datos de batallas, eventos y estadísticas se generan automáticamente
-- durante el juego y se guardan en la base de datos.

===============================================
INSTRUCCIONES DE USO
===============================================

1. Descarga SQLite Browser (DB Browser) o usa la línea de comandos de SQLite

2. Para crear la base de datos desde línea de comandos:
   - Abre CMD o PowerShell en la carpeta del proyecto
   - Ejecuta: sqlite3 batalla.db (crea el archivo)
   - Copia y pega los scripts de creación de tablas
   - Presiona Ctrl+D o escribe .exit para salir

3. El archivo de base de datos (batalla.db) debe estar en:
   c:\Users\Usuario\OneDrive\Documentos\GitHub\Interfaz-grafica-Final\src\main\java\batalla\database\

4. La aplicación Java creará automáticamente la base de datos y tablas
   si no existen al primera ejecución.

===============================================
INFORMACIÓN DE LAS TABLAS
===============================================

PERSONAJES:
- id: Identificador único
- nombre: Nombre del personaje
- tipo: "Heroe" o "Villano"
- vida: Vida inicial del personaje
- bendiciones: JSON con las bendiciones del personaje

BATALLAS:
- id: Identificador único de la batalla
- heroe_id: ID del héroe que participó
- villano_id: ID del villano que participó
- ganador: Nombre del ganador
- turnos: Número de turnos que duró la batalla
- fecha_batalla: Fecha y hora de la batalla

EVENTOS_BATALLA:
- id: Identificador único del evento
- batalla_id: ID de la batalla a la que pertenece
- turno: Número del turno en que ocurrió
- evento: Descripción del evento

ESTADISTICAS_BATALLA:
- id: Identificador único
- batalla_id: ID de la batalla
- heroe_armas_invocadas: Cantidad de armas invocadas por el héroe
- heroe_supremos_usados: Cantidad de supremos usados por el héroe
- villano_armas_invocadas: Cantidad de armas invocadas por el villano
- villano_supremos_usados: Cantidad de supremos usados por el villano
- mayor_danio: Valor del mayor daño realizado
- personaje_mayor_danio: Nombre del personaje que realizó el mayor daño