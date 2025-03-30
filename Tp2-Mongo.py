import requests
import mysql.connector
from mysql.connector import Error

# Configuración de la conexión a la base de datos
DB_CONFIG = {
    "host": "localhost",
    "user": "root",
    "password": "",
    "database": "tp2magni"
}

try:
    # Conectar a la base de datos
    conn = mysql.connector.connect(**DB_CONFIG)
    cursor = conn.cursor()

    # Crear la tabla Pais si no existe
    create_table_query = """
    CREATE TABLE IF NOT EXISTS Pais (
        codigoPais INT PRIMARY KEY,
        nombrePais VARCHAR(50) NOT NULL,
        capitalPais VARCHAR(50) NOT NULL,
        region VARCHAR(50) NOT NULL,
        subregion VARCHAR(50) NOT NULL,
        poblacion BIGINT NOT NULL,
        latitud DECIMAL(10, 6) NOT NULL,
        longitud DECIMAL(10, 6) NOT NULL
    );
    """
    
    cursor.execute(create_table_query)
    print("Tabla 'Pais' creada correctamente.")

except mysql.connector.Error as e:
    print(f"Error al conectar a MySQL: {e}")

finally:
    if 'cursor' in locals():
        cursor.close()
    if 'conn' in locals():
        conn.close()

# Función para obtener datos desde la API
def obtener_datos_pais(codigo):
    url = f"https://restcountries.com/v3.1/alpha/{codigo:03d}"
    response = requests.get(url)
    
    if response.status_code == 200:
        data = response.json()
        if isinstance(data, list) and len(data) > 0:
            return data[0]  # La API devuelve una lista con un solo objeto
    return None

# Función para insertar o actualizar los datos en MySQL
def guardar_pais_en_bd(codigo, datos, conexion):
    try:
        cursor = conexion.cursor()
        
        nombre = datos["name"]["common"]
        capital = datos["capital"][0] if "capital" in datos else "Desconocida"
        region = datos.get("region", "Desconocida")
        subregion = datos.get("subregion", "Desconocida")
        poblacion = datos.get("population", 0)
        latitud, longitud = datos["latlng"] if "latlng" in datos else (0.0, 0.0)

        # Verificar si el país ya existe en la base de datos
        cursor.execute("SELECT * FROM Pais WHERE codigoPais = %s", (codigo,))
        existe = cursor.fetchone()

        if existe:
            sql = """
                UPDATE Pais SET nombrePais=%s, capitalPais=%s, region=%s, subregion=%s, 
                poblacion=%s, latitud=%s, longitud=%s WHERE codigoPais=%s
            """
            valores = (nombre, capital, region, subregion, poblacion, latitud, longitud, codigo)
            cursor.execute(sql, valores)
        else:
            sql = """
                INSERT INTO Pais (codigoPais, nombrePais, capitalPais, region, subregion, poblacion, latitud, longitud)
                VALUES (%s, %s, %s, %s, %s, %s, %s, %s)
            """
            valores = (codigo, nombre, capital, region, subregion, poblacion, latitud, longitud)
            cursor.execute(sql, valores)

        conexion.commit()
        print(f"Datos guardados para código {codigo}: {nombre}")

    except Error as e:
        print(f"Error en código {codigo}: {e}")

# Función principal
def main():
    try:
        conexion = mysql.connector.connect(**DB_CONFIG)
        if conexion.is_connected():
            print("Conectado a la base de datos")

            for codigo in range(1, 301):
                datos = obtener_datos_pais(codigo)
                if datos:
                    guardar_pais_en_bd(codigo, datos, conexion)
                else:
                    print(f"No hay datos para el código {codigo}, continuando...")

        conexion.close()
        print("Conexión cerrada")

    except Error as e:
        print(f"Error al conectar con la base de datos: {e}")

# Ejecutar el script
if __name__ == "__main__":
    main()