## Flight On Time – Predicción de Puntualidad de Vuelos

---

## Descripción del Dominio

Este proyecto implementa una aplicación orientada a servicios que permite **predecir si un vuelo llegará a tiempo o sufrirá retrasos**, utilizando técnicas de **Machine Learning** y una **arquitectura basada en microservicios**.

El sistema está dividido en dos componentes principales:

- **API Java (A)**: expone endpoints REST y actúa como intermediaria entre el cliente y el modelo predictivo.
- **API Python (B)**: contiene el modelo de Machine Learning entrenado y se encarga de realizar la predicción.

La relación entre ambos componentes es **unidireccional**:  
la **API Java consume la API Python**, enviándole los datos del vuelo y recibiendo como respuesta la predicción.

---

## Componentes del Sistema

### Java API (A)
- Implementada con **Spring Boot**
- Gestiona las solicitudes HTTP del cliente
- Valida los datos de entrada
- Consume la API Python para obtener la predicción
- Devuelve la respuesta final al cliente

### Python API (B)
- Implementada con **Flask**
- Contiene un modelo de Machine Learning previamente entrenado
- Recibe datos del vuelo
- Retorna la predicción (vuelo a tiempo / retrasado)

La comunicación se realiza mediante **HTTP + JSON**.

---

## Requisitos

### Backend Java
- Java: **JDK 17 o superior** (recomendado JDK 21)
- Maven: **3.6+**
- IDE recomendado: **IntelliJ IDEA**

### Backend Python
- Python: **3.9 o superior**
- pip
- IDE recomendado: **Visual Studio Code**

---

## Estructura del Proyecto
flight-on-time/
│
├── java-api/
│ ├── src/
│ │ ├── main/
│ │ │ ├── java/
│ │ │ │ └── com.flightontime.api/
│ │ │ │ ├── controller/ # Controladores REST
│ │ │ │ ├── service/ # Lógica de negocio
│ │ │ │ └── JavaApiApplication.java
│ │ │ └── resources/
│ │ │ └── application.properties
│ └── pom.xml
│
├── python-api/
│ ├── app.py # API Flask
│ ├── modelo_vuelos.joblib # Modelo entrenado
│ └──requirements.txt # Dependencias Python
└── README.md


---

## Configuración y Ejecución

### 1️⃣ Ejecutar la API Python (Modelo de Machine Learning)

⚠️ **IMPORTANTE:** La API Python debe ejecutarse **antes** que la API Java.

```bash
cd python-api
pip install -r requirements.txt
python app.py

## La API Python quedará disponible en:

http://localhost:5000

## 2️⃣ Ejecutar la API Java

### Opción A: Desde IntelliJ IDEA (Recomendado)

1. Abrir **IntelliJ IDEA**
2. Seleccionar **Open Project**
3. Abrir la carpeta `java-api`
4. Esperar a que Maven descargue las dependencias
5. Ejecutar la clase `JavaApiApplication`

La API Java quedará disponible en:

---

### Opción B: Desde consola con Maven

```bash
cd java-api
mvn spring-boot:run

## Comunicación entre Servicios

1. El cliente envía una solicitud HTTP a la API Java
2. La API Java valida los datos
3. La API Java envía una petición HTTP a la API Python
4. La API Python procesa los datos con el modelo entrenado
5. La API Python devuelve la predicción
6. La API Java retorna el resultado final al cliente

## Flujo de Uso

1. El usuario envía los datos del vuelo
2. La API Java recibe la solicitud
3. Se consulta el modelo de Machine Learning
4. Se obtiene una predicción:
5. Vuelo a tiempo
6. Vuelo con retraso
7. Se devuelve la respuesta en formato JSON

## Características Implementadas

✅ Arquitectura de microservicios
✅ Separación de responsabilidades (Java / Python)
✅ Comunicación vía HTTP REST
✅ Modelo de Machine Learning entrenado
✅ Predicción automática de puntualidad
✅ API desacoplada y escalable
✅ Uso de formato JSON

### Modelo de Machine Learning
- Modelo entrenado previamente y serializado con Joblib
- Cargado automáticamente al iniciar la API Python
- No requiere reentrenamiento
- Devuelve una predicción binaria (a tiempo / retrasado)

### Manejo de Errores
- El sistema maneja correctamente:
- Errores de conexión entre servicios
- Datos inválidos o incompletos
- Errores internos del modelo
- Respuestas HTTP claras y descriptivas

### Arquitectura
## Java API

- controller/: Endpoints REST
- service/: Lógica de negocio
- JavaApiApplication: Punto de entrada

## Python API

- app.py: API Flask y carga del modelo
- modelo_vuelos.joblib: Modelo entrenado
- requirements.txt: Dependencias del proyecto

## Notas Importantes
- Ambas APIs deben estar ejecutándose simultáneamente.
- La API Python debe iniciarse primero.
- Puertos por defecto:
   - Python: 5000
   - Java: 8080
- El modelo ya está entrenado y listo para usar.