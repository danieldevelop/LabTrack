# LabTrack — Sistema de seguimiento de muestras de laboratorio

## 1. Descripción del proyecto

LabTrack es una aplicación web desarrollada con Java, JSP y Servlets, cuyo objetivo es resolver un problema real de gestión y trazabilidad de muestras en un laboratorio clínico.

El sistema permitirá registrar pacientes, crear solicitudes de exámenes, gestionar muestras y controlar su procesamiento hasta la validación del resultado.

El proyecto tiene como objetivo principal aprender y aplicar:

- Java Server Pages (JSP).
- Java Servlets.
- Arquitectura MVC.
- Java Beans.
- Maven.
- JDBC y PostgreSQL.
- Archivo `application.properties` para la configuración.
- Git y GitHub para el control de versiones.

## 2. Problema que resuelve

Un laboratorio clínico necesita controlar el recorrido de las muestras desde su recepción hasta la validación del resultado.

Cuando este proceso se gestiona mediante planillas o registros manuales, pueden ocurrir problemas como:

- Muestras que no se procesan dentro del plazo esperado.
- Dificultad para conocer el estado actual de una muestra.
- Falta de información sobre quién realizó cada etapa.
- Pérdida de información sobre muestras rechazadas.
- Dificultad para consultar el historial de procesamiento.

LabTrack permitirá centralizar esta información y mantener un registro de cada cambio realizado.

## 3. Tecnologías

| Tecnología | Uso |
|---|---|
| Java 17 o superior | Lenguaje de programación |
| JSP | Interfaz de usuario |
| Java Servlet | Controlador de solicitudes HTTP |
| Apache Tomcat 11 | Servidor de aplicaciones |
| Maven | Gestión de dependencias y construcción |
| PostgreSQL | Base de datos |
| JDBC | Conexión y consultas SQL |
| Git y GitHub | Control de versiones |

**Compatibilidad:** se utilizará la API `jakarta.servlet`, correspondiente a Tomcat 11. No se utilizará `javax.servlet` mientras el proyecto esté basado en Tomcat 11.

## 4. Funcionalidades del sistema

### Módulo de pacientes

- Registrar pacientes.
- Consultar pacientes.
- Buscar pacientes por identificación.

### Módulo de solicitudes

- Crear solicitudes de exámenes.
- Asociar solicitudes a pacientes.
- Consultar las solicitudes registradas.

### Módulo de muestras

- Registrar muestras asociadas a una solicitud.
- Generar un código único para cada muestra.
- Consultar el estado actual de una muestra.

### Módulo de procesamiento

- Cambiar el estado de una muestra.
- Registrar observaciones.
- Validar las reglas de negocio antes de permitir un cambio.

### Módulo de trazabilidad

- Registrar cada cambio de estado.
- Identificar al usuario responsable.
- Consultar el historial de una muestra.

## 5. Reglas de negocio

1. Una solicitud puede contener una o varias muestras.
2. Cada muestra debe tener un código único.
3. Una muestra puede estar en uno de los siguientes estados:
    - `RECIBIDA`
    - `EN_PROCESO`
    - `VALIDADA`
    - `RECHAZADA`
4. Una muestra validada no puede volver a procesarse.
5. Una muestra rechazada debe tener un motivo obligatorio.
6. Cada cambio de estado debe registrar el estado anterior, el nuevo estado, la fecha y el usuario responsable.
7. El cambio de estado y su registro de trazabilidad deben guardarse dentro de una misma transacción.

## 6. Arquitectura MVC

El proyecto utilizará una arquitectura MVC con separación de responsabilidades.

### Model

Contiene los Java Beans que representan las entidades del sistema.

Ejemplos:

- `Paciente.java`
- `Solicitud.java`
- `Muestra.java`
- `Trazabilidad.java`

### View

Contiene las páginas JSP encargadas de mostrar la información y recibir los datos ingresados por el usuario.

Las vistas no deben ejecutar consultas SQL ni contener reglas de negocio.

### Controller

Contiene los Servlets que reciben las solicitudes HTTP, validan los parámetros básicos y coordinan las operaciones necesarias.

### DAO

Contiene las clases encargadas de acceder a PostgreSQL mediante JDBC.

### Service

Contiene las reglas de negocio y coordina operaciones que requieren varios DAO o transacciones.

## 7. Estructura del proyecto

La siguiente es la estructura objetivo del proyecto:

```text
labtrack/
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/
        │   └── cl/labtrack/
        │       ├── config/
        │       │   └── DatabaseConfig.java
        │       ├── controller/
        │       │   ├── PacienteServlet.java
        │       │   ├── SolicitudServlet.java
        │       │   └── MuestraServlet.java
        │       ├── dao/
        │       │   ├── PacienteDAO.java
        │       │   ├── SolicitudDAO.java
        │       │   └── MuestraDAO.java
        │       ├── model/
        │       │   ├── Paciente.java
        │       │   ├── Solicitud.java
        │       │   ├── Muestra.java
        │       │   └── Trazabilidad.java
        │       ├── service/
        │       │   └── MuestraService.java
        │       └── util/
        │           └── PropertiesUtil.java
        ├── resources/
        │   └── application.properties
        └── webapp/
            ├── index.jsp
            ├── assets/
            └── WEB-INF/
                ├── web.xml
                └── views/
                    ├── pacientes/
                    ├── solicitudes/
                    └── muestras/
```

La estructura representa el objetivo del proyecto. No es necesario crear todas las clases y directorios desde el comienzo.

## 8. Configuración de application.properties

El archivo de configuración se encuentra en:

`src/main/resources/application.properties`

Configuración inicial:

```properties
# Base de datos
db.driver=org.postgresql.Driver
db.url=${DB_URL}
db.username=${DB_USERNAME}
db.password=${DB_PASSWORD}

# Aplicación
app.name=LabTrack
app.version=1.0.0
app.environment=development
```

Las variables `DB_URL`, `DB_USERNAME` y `DB_PASSWORD` representan variables de entorno del sistema operativo.

Java Properties no reemplaza automáticamente las expresiones `${...}`. Se deberá implementar la lectura y resolución de estas variables mediante `PropertiesUtil.java`.

La clase `DatabaseConfig.java` utilizará esta configuración para crear las conexiones a PostgreSQL.

Las credenciales reales no deben almacenarse en el repositorio de GitHub.

## 9. Diseño inicial de la base de datos

Se utilizará PostgreSQL.

### Tabla paciente

```sql
CREATE TABLE paciente (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    identificacion VARCHAR(20) UNIQUE NOT NULL
);
```

### Tabla solicitud

```sql
CREATE TABLE solicitud (
    id SERIAL PRIMARY KEY,
    paciente_id INTEGER NOT NULL
        REFERENCES paciente(id),
    fecha_creacion TIMESTAMP NOT NULL
        DEFAULT CURRENT_TIMESTAMP
);
```

### Tabla muestra

```sql
CREATE TABLE muestra (
    id SERIAL PRIMARY KEY,
    solicitud_id INTEGER NOT NULL
        REFERENCES solicitud(id),
    codigo VARCHAR(50) UNIQUE NOT NULL,
    tipo_examen VARCHAR(100) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'RECIBIDA'
);
```

### Tabla trazabilidad

```sql
CREATE TABLE trazabilidad (
    id SERIAL PRIMARY KEY,
    muestra_id INTEGER NOT NULL
        REFERENCES muestra(id),
    estado_anterior VARCHAR(20),
    estado_nuevo VARCHAR(20) NOT NULL,
    usuario VARCHAR(100) NOT NULL,
    fecha TIMESTAMP NOT NULL
        DEFAULT CURRENT_TIMESTAMP,
    observacion TEXT
);
```

La tabla `trazabilidad` permitirá consultar todos los cambios realizados sobre una muestra.

## 10. Directrices de desarrollo

El desarrollo se realizará de manera incremental.

### Etapa 1: Configuración inicial

- Crear el proyecto Maven.
- Configurar Tomcat 11.
- Verificar que la aplicación se despliegue correctamente.
- Implementar la lectura de `application.properties`.
- Configurar la conexión a PostgreSQL.

### Etapa 2: Módulo de pacientes

- Crear el Java Bean `Paciente`.
- Implementar `PacienteDAO`.
- Crear `PacienteServlet`.
- Crear el formulario JSP de registro.
- Crear el listado de pacientes.
- Validar los datos antes de guardarlos.

### Etapa 3: Solicitudes y muestras

- Implementar los Java Beans correspondientes.
- Crear los DAO y Servlets.
- Registrar solicitudes asociadas a pacientes.
- Registrar muestras asociadas a solicitudes.
- Consultar el estado de las muestras.

### Etapa 4: Procesamiento

- Implementar `MuestraService`.
- Aplicar las reglas de negocio.
- Controlar los cambios de estado.
- Registrar la trazabilidad.
- Utilizar transacciones JDBC.

### Etapa 5: Mejoras

- Agregar validaciones adicionales.
- Implementar manejo de errores.
- Mejorar la interfaz de usuario.
- Agregar filtros y búsquedas.
- Implementar autenticación y control de acceso.

## 11. Estado actual del proyecto

El proyecto ya se encuentra iniciado.

| Elemento | Estado |
|---|---|
| Proyecto Maven | En desarrollo |
| Repositorio GitHub | Creado |
| Archivo `application.properties` | Creado con variables iniciales |
| Paquete `util` | Creado |
| Configuración de PostgreSQL | Pendiente de implementar |
| Arquitectura MVC | Pendiente de implementar |
| Java Beans | Pendiente de implementar |
| DAO | Pendiente de implementar |
| Servlets | Pendiente de implementar |
| Vistas JSP | Pendiente de implementar |

El siguiente objetivo es implementar la lectura del archivo `application.properties` y establecer la conexión con PostgreSQL.

## 12. Instrucciones para continuar el proyecto con Claude AI

Este proyecto debe desarrollarse de forma incremental, respetando la arquitectura definida en este documento.

Al continuar el desarrollo:

1. Revisar primero el estado actual del repositorio y los archivos existentes.
2. No volver a crear archivos que ya estén implementados.
3. Mantener la estructura MVC y la separación de responsabilidades.
4. Utilizar JDBC para el acceso a PostgreSQL.
5. Mantener las credenciales fuera del código fuente.
6. Explicar brevemente qué archivos se deben crear o modificar.
7. Proporcionar el código completo de los archivos nuevos o modificados.
8. No avanzar a otro módulo hasta verificar que el módulo actual funciona correctamente.
9. Priorizar soluciones sencillas y comprensibles, evitando incorporar frameworks adicionales innecesarios.
10. Mantener compatibilidad con Java 17 y superior y Apache Tomcat 11.

### Primer requerimiento para continuar

Revisar el proyecto existente y ayudar a implementar la configuración de conexión a PostgreSQL utilizando el archivo `application.properties` y el paquete `util` que ya se encuentra creado.

El objetivo es obtener una conexión funcional mediante JDBC antes de comenzar con los módulos de pacientes, solicitudes y muestras.

## 13. Compatibilidad y entorno de desarrollo

### Versiones utilizadas

- Java 25.
- Apache Tomcat 11.0.27.
- Maven.
- PostgreSQL.
- Git y GitHub.

### Consideraciones importantes

Este proyecto utiliza Apache Tomcat 11, por lo que debe utilizar la API Jakarta Servlet.

**Importante:** utilizar `jakarta.servlet` en lugar de `javax.servlet`.

Todas las dependencias de Maven deben ser compatibles con Java 25 y Tomcat 11.

No utilizar ejemplos, dependencias ni configuraciones destinadas exclusivamente a Tomcat 9.

Al desarrollar o modificar código, respetar estas versiones y mantener la compatibilidad con el entorno existente.

No cambiar las versiones de Java, Tomcat ni las dependencias principales sin consultar previamente.