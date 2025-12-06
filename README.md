# IEFI - Batallas (Proyecto Java Swing)

Este repositorio contiene una aplicación de batalla (Héroe vs Villano) hecha en Java con arquitectura MVC y una interfaz gráfica basada en Swing.

## Instrucciones de ejecución

Requisitos mínimos:
- JDK 11 o JDK 17 (recomendado: JDK 17 LTS). Asegúrate de que `java` y `javac` apunten a la versión instalada.
- Maven 3.x para construir el proyecto.

Dependencias:
- No se usan librerías externas aparte de las que proporciona Java SE (Swing). El proyecto utiliza Maven para gestión de compilación pero no requiere dependencias adicionales en `pom.xml` por defecto.

## Comportamiento de persistencia (Historial de partidas)

- Cuando se guarda una partida desde la pantalla de resultados (`btnGuardarPartida`) la aplicación crea (si no existe) la carpeta `HistorialPartidas/` en la raíz del proyecto.
- Cada partida se guarda en un archivo independiente llamado `BatallaN.txt` (incremental) con el siguiente formato:

```
HEROE: <nombre_heroe>
VILLANO: <nombre_villano>
GANADOR: <nombre_ganador>
TURNOS: <numero_turnos>
COMBATLOG_START
... lineas de combat log ...
COMBATLOG_END
```

- La pantalla de historial (`PantallaHistorial`) lista los archivos `BatallaN.txt` y muestra en la tabla las columnas: `N° Batalla`, `Héroe`, `Villano`, `Ganador`, `Turnos`.
- Al seleccionar una fila y pulsar `Cargar Partida`, se abre `formHistorial` mostrando el `CombatLog` en `txtCombatLog` y los metadatos (Héroe, Villano, Ganador, Turnos) en el encabezado.
- Existe la opción `Borrar Partida` para eliminar el archivo `BatallaN.txt` seleccionado.

## Estructura del proyecto (resumen)

```
IEFI-InterfazGrafica/
├─ pom.xml
├─ README.md
├─ HistorialPartidas/                # carpeta creada en tiempo de ejecución con BatallaN.txt
├─ src/
│  ├─ main/
│  │  ├─ java/
│  │  │  ├─ batalla/
│  │  │  │  ├─ controlador/         # controladores MVC
│  │  │  │  │  ├─ ControladorHistorial.java
│  │  │  │  │  ├─ ControladorResultado.java
│  │  │  │  │  └─ ...
│  │  │  │  ├─ modelo/              # clases del dominio y persistencia
│  │  │  │  │  ├─ GestorPersistencia.java
│  │  │  │  │  ├─ PartidaGuardada.java
│  │  │  │  │  ├─ Heroe.java
│  │  │  │  │  └─ Villano.java
│  │  │  │  └─ vista/               # vistas Swing (JFrame / formularios)
│  │  │  │     ├─ PantallaHistorial.java
│  │  │  │     ├─ PantallaResultado.java
│  │  │  │     └─ formHistorial.java
│  │  │  └─ batalla/MainLauncher.java
│  │  └─ resources/
│  └─ test/
└─ target/
```

## Integrantes y roles

- Tomás — Modelo y Main
- Lourdes — Controlador
- Ulises — Vistas

## Uso de IA / Herramientas externas

Durante el desarrollo se usaron las siguientes ayudas y recursos:

- GitHub Copilot
- Chat-GPT
- YouTube

Prompts Ulises: https://chatgpt.com/share/6915c3a6-3e64-8010-8ad7-d0dc58ad400e
Video Youtube Ulises: https://www.youtube.com/watch?v=L2xczUN9aI0 / https://www.youtube.com/watch?v=LP7_DlIe670

## Video explicando
- https://youtu.be/KJyrmycQ5Vc
