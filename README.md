Sistema de Gestión de Empresas y Domicilios Fiscales

Trabajo Final Integrador - Programación 2 
Tecnicatura Universitaria en Programación a Distancia

---

Descripción del Proyecto

Sistema de gestión empresarial desarrollado en Java que implementa la relación 1→1 unidireccional entre **Empresa** y **DomicilioFiscal**. Cada empresa tiene exactamente un domicilio fiscal asociado, y el sistema permite realizar operaciones CRUD completas sobre ambas entidades con persistencia en base de datos MySQL mediante JDBC y el patrón DAO.

Dominio: Empresa → DomicilioFiscal

Cada empresa tiene un único domicilio fiscal registrado. La relación es unidireccional: la empresa conoce su domicilio fiscal, pero el domicilio no conoce a qué empresa pertenece.

---

Tecnologías Utilizadas

- Java 21 - Lenguaje de programación
- MySQL 8.0+ - Base de datos relacional
- JDBC - Java Database Connectivity
- Git/GitHub - Control de versiones

---

Estructura del Proyecto

```
src/
├── com.tfi.empresa/
│   ├── config/
│   │   └── DatabaseConnection.java
│   ├── entities/
│   │   ├── Empresa.java
│   │   └── DomicilioFiscal.java
│   ├── dao/
│   │   ├── GenericDao.java
│   │   ├── EmpresaDao.java
│   │   └── DomicilioFiscalDao.java
│   ├── service/
│   │   ├── EmpresaServicio.java
│   │   └── DomicilioFiscalServicio.java
│   ├── util/
│   │   └── InputValidator.java
│   └── main/
│       ├── Main.java
│       └── AppMenu.java
│
sql/
├── Creacion.sql        # Creación de base de datos y tablas
└── Datos.sql          # Datos de prueba
```

---

Instalación y Configuración

Paso 1: Requisitos Previos

- JDK 21 o superior instalado
- MySQL 8.0 o superior instalado y ejecutándose
- MySQL Connector
- IDE: NetBeans


Paso 2: Crear la Base de Datos

Ejecutar los scripts SQL en orden:

desde MySQL Workbench:
1. Abrir MySQL Workbench
2. Conectarse al servidor local
3. Abrir archivo `sql/Creacion.sql` → Ejecutar
4. Abrir archivo `sql/Datos.sql` → Ejecutar

Paso 3: Configurar Credenciales de Base de Datos

Editar el archivo `src/com/tfi/empresa/config/DatabaseConnection.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/empresas";
private static final String USER = "root";         // Cambiar por tu usuario
private static final String PASSWORD = "tu_contraseña";  // Cambiar por tu contraseña
```

Paso 4: Compilar y Ejecutar


Desde tu IDE:

1. Importar el proyecto
2. Agregar `mysql-connector-j.jar` a las librerías del proyecto
3. Ejecutar `Main.java`

---

Uso del Sistema

Menú Principal

Al ejecutar `Main.java`, aparece:

```
=== MENU PRINCIPAL ===
1. Gestionar Empresas
2. Gestionar Domicilios Fiscales
0. Salir
```

Gestión de Empresas

```
--- GESTION DE EMPRESAS ---
1. Crear empresa
2. Listar empresas
3. Buscar empresa por ID
4. Buscar empresa por CUIT
5. Buscar empresas por razon social
6. Actualizar empresa
7. Eliminar empresa
0. Volver al menu principal
```

Ejemplo de Uso

Crear una empresa:
```
--- Nueva Empresa ---
Razon social: Mi Empresa SA
CUIT: 30-12345678-9
Actividad principal: Servicios
Email: contacto@miempresa.com

--- Domicilio Fiscal ---
Calle: San Martin
Numero: 456
Ciudad: Rosario
Provincia: Santa Fe
Codigo postal: 2000
Pais: Argentina

✓ Empresa creada correctamente (ID: 2)
```

Buscar por CUIT:
```
Ingrese CUIT a buscar: 30-12345678-9

Empresa{id=1, razonSocial='Simpsons S.A.', cuit='30-12345678-9', ...}
  Domicilio -> DomicilioFiscal{calle='Av. Siempre Viva', numero=742, ...}
```

---

Arquitectura del Sistema

Patrón de Capas

- config/ - Gestión de conexiones a MySQL
- entities/ - Clases de dominio (Empresa, DomicilioFiscal)
- dao/ - Acceso a datos con PreparedStatement
- service/ - Lógica de negocio y transacciones
- util/ - Validaciones de entrada
- main/ - Punto de entrada y menú de consola

Características Técnica:

Transacciones ACID:
- Todas las operaciones de modificación usan transacciones
- `setAutoCommit(false)` → operaciones → `commit()` / `rollback()`
- Si algo falla, se revierte todo

PreparedStatement:
- Usado en todas las consultas SQL
- Previene SQL Injection

Baja Lógica:
- Los registros no se borran físicamente
- Se marcan con `eliminado = TRUE`
- Los listados filtran por `eliminado = FALSE`

Relación 1→1 Unidireccional:
- En código: `Empresa` tiene atributo `DomicilioFiscal`
- En BD: `empresa.domicilio_id` es UNIQUE (garantiza 1→1)

---

Datos de Prueba

El archivo `Datos.sql` incluye:

- 1 empresa de prueba: Simpsons S.A. (CUIT: 30-12345678-9)
- Domicilio: Av. Siempre Viva 742, Springfield

---

Video Demostrativo

Enlace: [https://youtube.com/watch?v=XXXXXXXXX](https://youtube.com/watch?v=XXXXXXXXX)

Contenido (10-15 minutos):
- Presentación del equipo
- Explicación de la arquitectura
- Demostración del código Java
- Ejecución en vivo del CRUD
- Conclusiones

---

Solución de Problemas

Error: "Access denied for user"
Solución: Verificar usuario y contraseña en `DatabaseConnection.java`

Error: "Communications link failure"
Solución: 
- Verificar que MySQL esté ejecutándose
- Verificar puerto 3306 disponible

Error: "Table 'empresas.empresa' doesn't exist"
Solución: Ejecutar `sql/Creacion.sql`

Error: "ClassNotFoundException: com.mysql.cj.jdbc.Driver"
Solución: Agregar `mysql-connector-j.jar` al classpath

---

Mejoras Futuras

- Pool de conexiones (HikariCP)
- Logging con Log4j
- Tests unitarios con JUnit
- Migrar a Spring Boot
- Interfaz gráfica con JavaFX
- API REST

---

Herramientas Utilizadas

- NetBeans
- MySQL Workbench
- Git/GitHub

---


Fecha de entrega: 20/11/2025  
Comisión:Marcos Rios (Comision 9); Elias Pfaeffli (Comision 8)  
Docente: Ariel Enferrel (Comision 8); Cinthia Rigoni (Comision 9)
