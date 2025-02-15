# Funcionalidades de la Aplicación "Sopa de Letras"

---

## Menú Principal

- **Nueva Partida:**  
  Permite gestionar el banco de palabras mediante un submenú, y posteriormente iniciar el juego.

- **Historial de Partidas**

- **Mostrar Puntuaciones Más Altas (Top 3)**

- **Mostrar Información de Estudiante**

- **Salir**

---

## Nueva Partida

- **Ingreso del nombre del jugador:**  
  Se solicita el nombre del jugador antes de iniciar la partida.

- **Selección de Sección:**  
  El jugador debe elegir una sección (A a G), lo que determina:
  - El **tamaño del tablero**.
  - La **longitud permitida de las palabras**.  
    *(Ver Tabla 1 del PDF de este repositorio)*

---

## Menú Palabras (Submenú)

Permite gestionar el banco de palabras con las siguientes opciones:

- **Insertar palabras:**  
  Se valida que cada palabra tenga la longitud permitida.

- **Modificar palabra:**  
  Permite reemplazar una palabra existente por otra, cumpliendo la validación de longitud.

- **Eliminar palabra:**  
  Se elimina la palabra indicada del banco.

- **Mostrar palabras:**  
  Visualiza todas las palabras ingresadas.

---

## Modo de Juego ("Jugar")

- Se **genera un tablero** (matriz de caracteres) en el que se colocan las palabras de forma aleatoria, ya sea **horizontal o verticalmente**.

- Las celdas que no contienen una palabra se **rellenan con letras aleatorias (A–Z)**.

---

## Mecánica del Juego

- Durante la partida, el jugador **ingresa palabras**:
  - **Si la palabra es encontrada** (y no ha sido identificada previamente):
    - Se **reemplaza** en el tablero por el símbolo **`#`**.
    - Se **suman puntos** equivalentes a la **cantidad de letras** de la palabra.
  - **Por cada error**, se **restan 5 puntos**.
  - El jugador dispone de **4 oportunidades** para cometer errores.

---

## Registro de Partidas

- Al finalizar cada partida, se **guarda el registro** en un **historial** para llevar un seguimiento de las jugadas.

---
