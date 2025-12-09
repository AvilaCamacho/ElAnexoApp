"""
Modelo de base de datos para AudioMessage usando SQLAlchemy
"""
from flask_sqlalchemy import SQLAlchemy
from datetime import datetime

db = SQLAlchemy()


class AudioMessage(db.Model):
    """
    Modelo de AudioMessage - almacena metadatos de mensajes de audio
    """
    __tablename__ = 'audio_messages'
    
    id = db.Column(db.Integer, primary_key=True)
    sender = db.Column(db.String(100), nullable=False)
    recipient = db.Column(db.String(100), nullable=False)
    filename = db.Column(db.String(255), nullable=False, unique=True)
    duration = db.Column(db.Float)  # duración en segundos
    file_size = db.Column(db.Integer)  # tamaño en bytes
    created_at = db.Column(db.DateTime, default=datetime.utcnow)
    
    def to_dict(self):
        """
        Convierte el modelo a diccionario para respuestas JSON
        """
        return {
            'id': self.id,
            'sender': self.sender,
            'recipient': self.recipient,
            'filename': self.filename,
            'duration': self.duration,
            'file_size': self.file_size,
            'created_at': self.created_at.isoformat() if self.created_at else None
        }
    
    def __repr__(self):
        return f'<AudioMessage {self.id}: {self.sender} -> {self.recipient}>'
