"""
Servidor Flask API para gestión de AudioMessages
Proporciona CRUD completo y servicio de archivos de audio
"""
from flask import Flask, request, jsonify, send_from_directory
from werkzeug.utils import secure_filename
from models import db, AudioMessage
import os

app = Flask(__name__)

# Configuración
UPLOAD_FOLDER = 'uploads'
DATABASE_PATH = 'audio_messages.db'
ALLOWED_EXTENSIONS = {'mp3', 'wav', 'ogg', 'm4a', 'aac', '3gp'}

app.config['SQLALCHEMY_DATABASE_URI'] = f'sqlite:///{DATABASE_PATH}'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
app.config['MAX_CONTENT_LENGTH'] = 50 * 1024 * 1024  # 50MB max

# Inicializar base de datos
db.init_app(app)

# Crear directorios necesarios
os.makedirs(UPLOAD_FOLDER, exist_ok=True)


def allowed_file(filename):
    """Verifica si el archivo tiene una extensión permitida"""
    return '.' in filename and \
           filename.rsplit('.', 1)[1].lower() in ALLOWED_EXTENSIONS


# Crear las tablas de la base de datos
with app.app_context():
    db.create_all()


@app.route('/messages', methods=['GET'])
def get_messages():
    """
    GET /messages - Obtiene lista de mensajes de audio
    Query params opcionales:
    - sender: filtrar por remitente
    - recipient: filtrar por destinatario
    """
    sender = request.args.get('sender')
    recipient = request.args.get('recipient')
    
    query = AudioMessage.query
    
    if sender:
        query = query.filter_by(sender=sender)
    if recipient:
        query = query.filter_by(recipient=recipient)
    
    messages = query.all()
    return jsonify([msg.to_dict() for msg in messages]), 200


@app.route('/messages/<int:message_id>', methods=['GET'])
def get_message(message_id):
    """
    GET /messages/<id> - Obtiene un mensaje específico por ID
    """
    message = AudioMessage.query.get(message_id)
    
    if not message:
        return jsonify({'error': 'Mensaje no encontrado'}), 404
    
    return jsonify(message.to_dict()), 200


@app.route('/messages', methods=['POST'])
def create_message():
    """
    POST /messages - Crea un nuevo mensaje de audio
    Requiere multipart/form-data con:
    - sender (string)
    - recipient (string)
    - audio_file (file)
    - duration (float, opcional)
    """
    # Validar que se incluyó un archivo
    if 'audio_file' not in request.files:
        return jsonify({'error': 'No se incluyó archivo de audio'}), 400
    
    file = request.files['audio_file']
    
    if file.filename == '':
        return jsonify({'error': 'No se seleccionó archivo'}), 400
    
    if not allowed_file(file.filename):
        return jsonify({'error': 'Tipo de archivo no permitido'}), 400
    
    # Obtener campos del formulario
    sender = request.form.get('sender')
    recipient = request.form.get('recipient')
    duration = request.form.get('duration', type=float)
    
    if not sender or not recipient:
        return jsonify({'error': 'sender y recipient son requeridos'}), 400
    
    # Guardar archivo
    filename = secure_filename(file.filename)
    # Generar nombre único si ya existe
    base_name, extension = os.path.splitext(filename)
    counter = 1
    max_retries = 1000
    while os.path.exists(os.path.join(app.config['UPLOAD_FOLDER'], filename)):
        if counter > max_retries:
            return jsonify({'error': 'No se pudo generar nombre único para el archivo'}), 500
        filename = f"{base_name}_{counter}{extension}"
        counter += 1
    
    file_path = os.path.join(app.config['UPLOAD_FOLDER'], filename)
    file.save(file_path)
    
    # Obtener tamaño del archivo
    file_size = os.path.getsize(file_path)
    
    # Crear registro en base de datos
    new_message = AudioMessage(
        sender=sender,
        recipient=recipient,
        filename=filename,
        duration=duration,
        file_size=file_size
    )
    
    db.session.add(new_message)
    db.session.commit()
    
    return jsonify(new_message.to_dict()), 201


@app.route('/messages/<int:message_id>', methods=['PUT'])
def update_message(message_id):
    """
    PUT /messages/<id> - Actualiza un mensaje existente
    Acepta multipart/form-data con campos opcionales:
    - sender (string)
    - recipient (string)
    - audio_file (file)
    - duration (float)
    """
    message = AudioMessage.query.get(message_id)
    
    if not message:
        return jsonify({'error': 'Mensaje no encontrado'}), 404
    
    # Actualizar archivo si se proporciona uno nuevo
    if 'audio_file' in request.files:
        file = request.files['audio_file']
        
        if file.filename != '' and allowed_file(file.filename):
            # Eliminar archivo anterior
            old_file_path = os.path.join(app.config['UPLOAD_FOLDER'], message.filename)
            if os.path.exists(old_file_path):
                os.remove(old_file_path)
            
            # Guardar nuevo archivo
            filename = secure_filename(file.filename)
            base_name, extension = os.path.splitext(filename)
            counter = 1
            max_retries = 1000
            while os.path.exists(os.path.join(app.config['UPLOAD_FOLDER'], filename)):
                if counter > max_retries:
                    return jsonify({'error': 'No se pudo generar nombre único para el archivo'}), 500
                filename = f"{base_name}_{counter}{extension}"
                counter += 1
            
            file_path = os.path.join(app.config['UPLOAD_FOLDER'], filename)
            file.save(file_path)
            
            message.filename = filename
            message.file_size = os.path.getsize(file_path)
    
    # Actualizar campos de metadatos
    if 'sender' in request.form:
        message.sender = request.form['sender']
    if 'recipient' in request.form:
        message.recipient = request.form['recipient']
    if 'duration' in request.form:
        try:
            message.duration = float(request.form['duration'])
        except ValueError:
            return jsonify({'error': 'duration debe ser un número válido'}), 400
    
    db.session.commit()
    
    return jsonify(message.to_dict()), 200


@app.route('/messages/<int:message_id>', methods=['DELETE'])
def delete_message(message_id):
    """
    DELETE /messages/<id> - Elimina un mensaje de audio
    """
    message = AudioMessage.query.get(message_id)
    
    if not message:
        return jsonify({'error': 'Mensaje no encontrado'}), 404
    
    # Eliminar archivo físico
    file_path = os.path.join(app.config['UPLOAD_FOLDER'], message.filename)
    if os.path.exists(file_path):
        os.remove(file_path)
    
    # Eliminar registro de base de datos
    db.session.delete(message)
    db.session.commit()
    
    return jsonify({'message': 'Mensaje eliminado exitosamente'}), 200


@app.route('/media/<filename>', methods=['GET'])
def serve_media(filename):
    """
    GET /media/<filename> - Sirve archivos de audio
    """
    return send_from_directory(app.config['UPLOAD_FOLDER'], filename)


@app.route('/', methods=['GET'])
def index():
    """
    GET / - Endpoint de bienvenida
    """
    return jsonify({
        'message': 'API de AudioMessages',
        'version': '1.0',
        'endpoints': {
            'GET /messages': 'Lista todos los mensajes (filtros: ?sender=X&recipient=Y)',
            'GET /messages/<id>': 'Obtiene un mensaje específico',
            'POST /messages': 'Crea un nuevo mensaje (multipart/form-data)',
            'PUT /messages/<id>': 'Actualiza un mensaje (multipart/form-data)',
            'DELETE /messages/<id>': 'Elimina un mensaje',
            'GET /media/<filename>': 'Sirve archivo de audio'
        }
    }), 200


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)
