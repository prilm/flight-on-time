from flask import Flask, request, jsonify
import joblib
import pandas as pd
import numpy as np # Es buena práctica, incluirlo por si acaso

app = Flask(__name__)

# 1. Cargar el modelo que está en la misma carpeta
try:
    model = joblib.load('modelo_vuelos.joblib')
    print("Modelo cargado exitosamente.")
except Exception as e:
    print(f"Error al cargar el modelo: {e}")
    model = None

@app.route('/predict', methods=['POST'])
def predict():
    if model is None:
        return jsonify({'error': 'Modelo no pudo ser cargado.'}), 500

    # 2. Obtener los datos del JSON de la petición
    data = request.get_json()
    print(f"Datos recibidos: {data}")

    try:
        # 3. CONSTRUIR EL DATAFRAME PARA LA PREDICCIÓN
        # Primero, procesamos la fecha
        fecha_partida = pd.to_datetime(data['fecha_partida'])
        
        # Creamos el diccionario de features
        features = {
            'AIRLINE': [data['aerolinea']],
            'ORIGIN_AIRPORT': [data['origen']],
            'DESTINATION_AIRPORT': [data['destino']],
            'MONTH': [fecha_partida.month],
            'DAY_OF_WEEK': [fecha_partida.dayofweek + 1],
            'SCHEDULED_DEPARTURE': [fecha_partida.hour * 100 + fecha_partida.minute], # Recreamos SCHEDULED_DEPARTURE
            'DISTANCE': [data['distancia_km']],
            'HORA': [fecha_partida.hour]
        }
        
        # ¡No olvidar la FRANJA_HORARIA!
        def asignar_franja_horaria(hora):
            if 5 <= hora < 12: return 'Mañana'
            elif 12 <= hora < 18: return 'Tarde'
            elif 18 <= hora < 22: return 'Noche'
            else: return 'Madrugada'
        
        features['FRANJA_HORARIA'] = [asignar_franja_horaria(fecha_partida.hour)]

        # Crear el DataFrame de Pandas
        input_df = pd.DataFrame(features)
        print(f"DataFrame para predicción:\n{input_df}")

        # 4. Hacer la predicción usando el pipeline completo
        prediction = model.predict(input_df)[0]
        probability = model.predict_proba(input_df)[0][1] # Probabilidad de ser clase '1' (Retrasado)

        # 5. Formatear la respuesta JSON
        status = "Retrasado" if prediction == 1 else "Puntual"

        response = {
            'prevision': status,
            'probabilidad': float(probability)
        }
        print(f"Respuesta enviada: {response}")
        return jsonify(response)

    except Exception as e:
        print(f"Error durante la predicción: {e}")
        return jsonify({'error': f'Error en el procesamiento de datos: {e}'}), 400

if __name__ == '__main__':
    # El puerto 5000 es estándar para desarrollo
    app.run(host='0.0.0.0', port=5000, debug=True)