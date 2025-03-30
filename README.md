# Proyecto de Migración de Datos de Países

Este proyecto permite migrar datos

## Configuración de la Base de Datos

Configura las credenciales de la base de datos en el archivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/tu_base_de_datos
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

## Uso del Endpoint para Migración de Datos

La migración de los países se realiza mediante el siguiente endpoint:

```
GET /migracion/iniciar?desde={codigo_desde}&hasta={codigo_hasta}
```

### Parámetros:
- `desde`: Código de país desde el cual comenzar la migración (ejemplo: `1`).
- `hasta`: Código de país hasta el cual migrar los datos (ejemplo: `300`).

### Ejemplo de Uso:

Para iniciar la migración de los países con códigos desde `1` hasta `300`, realiza una petición GET a:

```
http://localhost:8080/migracion/iniciar?desde=1&hasta=300
```

Este endpoint hará una llamada al API RestCountries para obtener los datos y los migrará a la tabla `Pais` en tu base de datos.

## Estructura de la Tabla `Pais`

La tabla se creará con la siguiente estructura:

```sql
CREATE TABLE Pais (
    codigoPais INT PRIMARY KEY,
    nombrePais VARCHAR(50) NOT NULL,
    capitalPais VARCHAR(50) NOT NULL,
    region VARCHAR(50) NOT NULL,
    subregion VARCHAR(50) NOT NULL,
    poblacion BIGINT NOT NULL,
    latitud DECIMAL(10, 6) NOT NULL,
    longitud DECIMAL(10, 6) NOT NULL
);
```

## Ejecutando la Aplicación

1. Clona este repositorio.
2. Configura las credenciales de la base de datos en `application.properties`.
3. Ejecuta la aplicación con `mvn spring-boot:run` o desde tu IDE.
4. Accede al endpoint mencionado para iniciar la migración de los países.
