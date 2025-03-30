# PARTE B

## Respuestas 

### 5.6 ¿Qué sucede al ejecutar el método `drop()` sobre una colección y sobre una base de datos?
- **En una colección:** El método `drop()` elimina la colección completa, incluyendo todos los documentos e índices. La colección deja de existir y no se puede recuperar sin un respaldo previo.
- **En una base de datos:** El método `dropDatabase()` elimina toda la base de datos, incluidas todas sus colecciones, documentos e índices. Esta operación es irreversible.

### 5.9 ¿Qué sucede al ejecutar el método `skip()` sobre una colección?
El método `skip()` omite un número específico de documentos en el resultado de una consulta, útil para la paginación junto con `limit()`.

**Ejemplo:**
```javascript
db.paises.find().skip(10).limit(5)
```
- Omitirá los primeros 10 documentos y mostrará los siguientes 5.

### 5.10 ¿Cómo el uso de expresiones regulares en Mongo puede reemplazar el uso de la cláusula LIKE de SQL?
En MongoDB, las expresiones regulares permiten buscar patrones de texto, reemplazando la cláusula `LIKE` de SQL.

**Ejemplo en SQL:**
```sql
SELECT * FROM paises WHERE nombre LIKE 'Arg%';
```
**Ejemplo en MongoDB:**
```javascript
db.paises.find({ name: /^Arg/ })
```
El símbolo `^` indica el inicio de la cadena.

### 5.11 ¿Cómo se crea un índice para la colección países asignando el campo código como índice?
Usa `createIndex()` para optimizar las consultas mediante índices.
```javascript
db.paises.createIndex({ codigo: 1 })
```
- `1` es ascendente, `-1` sería descendente.

### 5.12 ¿Cómo se realiza un backup de la base de datos `paises_db`?
Usa el comando `mongodump` desde la terminal para generar un respaldo completo.
```bash
mongodump --db paises_db --out /ruta/al/respaldo
```
Para restaurar el respaldo:
```bash
mongorestore --db paises_db /ruta/al/respaldo/paises_db
```

