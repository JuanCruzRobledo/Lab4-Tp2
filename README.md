
# Proyecto de Base de Datos con Python y MySQL

Este proyecto utiliza **Python** para crear y configurar automáticamente la tabla `Pais` en MySQL.

## Requisitos

- **Python 3.x**
- **MySQL** instalado.
- Paquete `mysql-connector-python`:  
  ```bash
  pip install mysql-connector-python
  ```

## Configuración

Modifica las credenciales de la base de datos en el archivo Python:
```python
DB_CONFIG = {
    "host": "localhost",
    "user": "root",
    "password": "",
    "database": "mi_base_de_datos",
    "port": 3306
}
```

## Funcionamiento

1. Se conecta a la base de datos configurada.
2. Crea automáticamente la tabla `Pais` si no existe.
3. La tabla tiene los siguientes campos: `codigoPais`, `nombrePais`, `capitalPais`, `region`, `subregion`, `poblacion`, `latitud`, `longitud`.

## Ejecución

1. Asegúrate de que MySQL esté corriendo.
2. Ejecuta el script Python:

   ```bash
   python script.py
   ```

La tabla se creará automáticamente si no existe.
