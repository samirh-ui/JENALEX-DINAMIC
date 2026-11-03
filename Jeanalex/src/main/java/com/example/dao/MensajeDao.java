package com.example.dao;

import com.example.config.ConexionPostgresDatabase;
import com.example.model.Mensaje;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MensajeDao {
    public Mensaje crear(Mensaje mensaje) throws SQLException {
        String sql = """
                INSERT INTO mensaje (id_remitente, id_destinatario, asunto, contenido, fecha_envio, leido)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, mensaje.getIdRemitente());
            statement.setInt(2, mensaje.getIdDestinatario());
            statement.setString(3, mensaje.getAsunto());
            statement.setString(4, mensaje.getContenido());
            statement.setTimestamp(5, Timestamp.valueOf(mensaje.getFechaEnvio()));
            statement.setBoolean(6, mensaje.isLeido());
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    mensaje.setId(keys.getInt(1));
                }
            }
        }
        return mensaje;
    }

    public List<Mensaje> listar() throws SQLException {
        String sql = """
                SELECT id, id_remitente, id_destinatario, asunto, contenido, fecha_envio, leido
                FROM mensaje
                ORDER BY fecha_envio DESC
                """;
        List<Mensaje> mensajes = new ArrayList<>();
        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                mensajes.add(mapear(resultSet));
            }
        }
        return mensajes;
    }

    private Mensaje mapear(ResultSet resultSet) throws SQLException {
        Mensaje mensaje = new Mensaje();
        mensaje.setId(resultSet.getInt("id"));
        mensaje.setIdRemitente(resultSet.getInt("id_remitente"));
        mensaje.setIdDestinatario(resultSet.getInt("id_destinatario"));
        mensaje.setAsunto(resultSet.getString("asunto"));
        mensaje.setContenido(resultSet.getString("contenido"));
        mensaje.setFechaEnvio(resultSet.getTimestamp("fecha_envio").toLocalDateTime());
        mensaje.setLeido(resultSet.getBoolean("leido"));
        return mensaje;
    }
}
