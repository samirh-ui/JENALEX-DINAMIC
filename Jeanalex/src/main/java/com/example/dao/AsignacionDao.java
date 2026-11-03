package com.example.dao;

import com.example.config.ConexionPostgresDatabase;
import com.example.model.Asignacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AsignacionDao implements CrudDao<Asignacion> {
    @Override
    public Asignacion crear(Asignacion asignacion) throws SQLException {
        String sql = """
                INSERT INTO asignacion (id_agente, id_dispositivo, id_responsable_entrega, fecha_asignacion,
                                        fecha_devolucion, observaciones, estado_prestamo)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = ConexionPostgresDatabase.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                completar(statement, asignacion);
                statement.executeUpdate();
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        asignacion.setId(keys.getInt(1));
                    }
                }
            }

            actualizarEstadoDispositivo(connection, asignacion.getIdDispositivo(), "Asignado");
            registrarHistorial(connection, asignacion.getIdDispositivo(), asignacion.getIdResponsableEntrega(),
                    "Asignacion", "Equipo asignado al agente seleccionado");
            connection.commit();
        }
        return asignacion;
    }

    public boolean registrarDevolucion(int idAsignacion, int idUsuario, String observaciones) throws SQLException {
        Optional<Asignacion> asignacion = buscarPorId(idAsignacion);
        if (asignacion.isEmpty()) {
            return false;
        }

        String sql = """
                UPDATE asignacion
                SET fecha_devolucion = CURRENT_TIMESTAMP, observaciones = ?, estado_prestamo = 'Devuelto'
                WHERE id = ?
                """;
        try (Connection connection = ConexionPostgresDatabase.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, observaciones);
                statement.setInt(2, idAsignacion);
                if (statement.executeUpdate() == 0) {
                    connection.rollback();
                    return false;
                }
            }

            actualizarEstadoDispositivo(connection, asignacion.get().getIdDispositivo(), "Disponible");
            registrarHistorial(connection, asignacion.get().getIdDispositivo(), idUsuario,
                    "Devolucion", "Devolucion registrada");
            connection.commit();
            return true;
        }
    }

    @Override
    public Optional<Asignacion> buscarPorId(int id) throws SQLException {
        String sql = """
                SELECT id, id_agente, id_dispositivo, id_responsable_entrega, fecha_asignacion,
                       fecha_devolucion, observaciones, estado_prestamo
                FROM asignacion
                WHERE id = ?
                """;
        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapear(resultSet));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Asignacion> listar() throws SQLException {
        String sql = """
                SELECT id, id_agente, id_dispositivo, id_responsable_entrega, fecha_asignacion,
                       fecha_devolucion, observaciones, estado_prestamo
                FROM asignacion
                ORDER BY fecha_asignacion DESC
                """;
        List<Asignacion> asignaciones = new ArrayList<>();
        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                asignaciones.add(mapear(resultSet));
            }
        }
        return asignaciones;
    }

    public List<Object[]> listarHistorialDispositivo(int idDispositivo) throws SQLException {
        String sql = """
                SELECT id, id_dispositivo, id_usuario, accion, descripcion, fecha
                FROM historial_dispositivo
                WHERE id_dispositivo = ?
                ORDER BY fecha DESC
                """;
        List<Object[]> historial = new ArrayList<>();
        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idDispositivo);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    historial.add(new Object[]{
                            resultSet.getInt("id"),
                            resultSet.getInt("id_dispositivo"),
                            resultSet.getInt("id_usuario"),
                            resultSet.getString("accion"),
                            resultSet.getString("descripcion"),
                            resultSet.getTimestamp("fecha")
                    });
                }
            }
        }
        return historial;
    }

    @Override
    public boolean actualizar(Asignacion asignacion) throws SQLException {
        String sql = """
                UPDATE asignacion
                SET id_agente = ?, id_dispositivo = ?, id_responsable_entrega = ?, fecha_asignacion = ?,
                    fecha_devolucion = ?, observaciones = ?, estado_prestamo = ?
                WHERE id = ?
                """;
        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            completar(statement, asignacion);
            statement.setInt(8, asignacion.getId());
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM asignacion WHERE id = ?")) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    private void completar(PreparedStatement statement, Asignacion asignacion) throws SQLException {
        statement.setInt(1, asignacion.getIdAgente());
        statement.setInt(2, asignacion.getIdDispositivo());
        statement.setInt(3, asignacion.getIdResponsableEntrega());
        statement.setTimestamp(4, Timestamp.valueOf(asignacion.getFechaAsignacion()));
        statement.setTimestamp(5, asignacion.getFechaDevolucion() == null ? null : Timestamp.valueOf(asignacion.getFechaDevolucion()));
        statement.setString(6, asignacion.getObservaciones());
        statement.setString(7, asignacion.getEstadoPrestamo());
    }

    private void actualizarEstadoDispositivo(Connection connection, int idDispositivo, String estado) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE dispositivo SET estado = ? WHERE id = ?")) {
            statement.setString(1, estado);
            statement.setInt(2, idDispositivo);
            statement.executeUpdate();
        }
    }

    private void registrarHistorial(Connection connection, int idDispositivo, int idUsuario, String accion, String descripcion) throws SQLException {
        String sql = """
                INSERT INTO historial_dispositivo (id_dispositivo, id_usuario, accion, descripcion, fecha)
                VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idDispositivo);
            statement.setInt(2, idUsuario);
            statement.setString(3, accion);
            statement.setString(4, descripcion);
            statement.executeUpdate();
        }
    }

    private Asignacion mapear(ResultSet resultSet) throws SQLException {
        Asignacion asignacion = new Asignacion();
        asignacion.setId(resultSet.getInt("id"));
        asignacion.setIdAgente(resultSet.getInt("id_agente"));
        asignacion.setIdDispositivo(resultSet.getInt("id_dispositivo"));
        asignacion.setIdResponsableEntrega(resultSet.getInt("id_responsable_entrega"));
        asignacion.setFechaAsignacion(resultSet.getTimestamp("fecha_asignacion").toLocalDateTime());
        Timestamp fechaDevolucion = resultSet.getTimestamp("fecha_devolucion");
        asignacion.setFechaDevolucion(fechaDevolucion == null ? null : fechaDevolucion.toLocalDateTime());
        asignacion.setObservaciones(resultSet.getString("observaciones"));
        asignacion.setEstadoPrestamo(resultSet.getString("estado_prestamo"));
        return asignacion;
    }
}
