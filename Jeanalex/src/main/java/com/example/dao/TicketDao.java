package com.example.dao;

import com.example.config.ConexionPostgresDatabase;
import com.example.model.Ticket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketDao implements CrudDao<Ticket> {
    @Override
    public Ticket crear(Ticket ticket) throws SQLException {
        String sql = """
                INSERT INTO ticket
                (asunto, descripcion, tipo_solicitud, estado, prioridad, id_agente, id_coordinador, id_tecnico, id_ans,
                 evidencia, fecha_creacion, fecha_asignacion, fecha_cierre)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            completarStatement(statement, ticket);
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    ticket.setId(keys.getInt(1));
                }
            }
        }

        return ticket;
    }

    @Override
    public Optional<Ticket> buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM ticket WHERE id = ?";

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
    public List<Ticket> listar() throws SQLException {
        String sql = "SELECT * FROM ticket ORDER BY fecha_creacion DESC";
        List<Ticket> tickets = new ArrayList<>();

        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                tickets.add(mapear(resultSet));
            }
        }

        return tickets;
    }

    public List<Ticket> listarPendientes() throws SQLException {
        String sql = "SELECT * FROM ticket WHERE estado IN ('Abierta', 'Pendiente') ORDER BY fecha_creacion";
        List<Ticket> tickets = new ArrayList<>();

        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                tickets.add(mapear(resultSet));
            }
        }

        return tickets;
    }

    @Override
    public boolean actualizar(Ticket ticket) throws SQLException {
        String sql = """
                UPDATE ticket
                SET asunto = ?, descripcion = ?, tipo_solicitud = ?, estado = ?, prioridad = ?, id_agente = ?,
                    id_coordinador = ?, id_tecnico = ?, id_ans = ?, evidencia = ?, fecha_creacion = ?,
                    fecha_asignacion = ?, fecha_cierre = ?
                WHERE id = ?
                """;

        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            completarStatement(statement, ticket);
            statement.setInt(14, ticket.getId());
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "DELETE FROM ticket WHERE id = ?";

        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    private void completarStatement(PreparedStatement statement, Ticket ticket) throws SQLException {
        statement.setString(1, ticket.getAsunto());
        statement.setString(2, ticket.getDescripcion());
        statement.setString(3, ticket.getTipoSolicitud());
        statement.setString(4, ticket.getEstado());
        statement.setString(5, ticket.getPrioridad());
        statement.setInt(6, ticket.getIdAgente());
        statement.setInt(7, ticket.getIdCoordinador());
        statement.setInt(8, ticket.getIdTecnico());
        statement.setInt(9, ticket.getIdAns());
        statement.setString(10, ticket.getEvidencia());
        statement.setTimestamp(11, ticket.getFechaCreacion() == null ? null : Timestamp.valueOf(ticket.getFechaCreacion()));
        statement.setTimestamp(12, ticket.getFechaAsignacion() == null ? null : Timestamp.valueOf(ticket.getFechaAsignacion()));
        statement.setTimestamp(13, ticket.getFechaCierre() == null ? null : Timestamp.valueOf(ticket.getFechaCierre()));
    }

    private Ticket mapear(ResultSet resultSet) throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setId(resultSet.getInt("id"));
        ticket.setAsunto(resultSet.getString("asunto"));
        ticket.setDescripcion(resultSet.getString("descripcion"));
        ticket.setTipoSolicitud(resultSet.getString("tipo_solicitud"));
        ticket.setEstado(resultSet.getString("estado"));
        ticket.setPrioridad(resultSet.getString("prioridad"));
        ticket.setIdAgente(resultSet.getInt("id_agente"));
        ticket.setIdCoordinador(resultSet.getInt("id_coordinador"));
        ticket.setIdTecnico(resultSet.getInt("id_tecnico"));
        ticket.setIdAns(resultSet.getInt("id_ans"));
        ticket.setEvidencia(resultSet.getString("evidencia"));

        Timestamp fechaCreacion = resultSet.getTimestamp("fecha_creacion");
        Timestamp fechaAsignacion = resultSet.getTimestamp("fecha_asignacion");
        Timestamp fechaCierre = resultSet.getTimestamp("fecha_cierre");

        if (fechaCreacion != null) {
            ticket.setFechaCreacion(fechaCreacion.toLocalDateTime());
        }
        if (fechaAsignacion != null) {
            ticket.setFechaAsignacion(fechaAsignacion.toLocalDateTime());
        }
        if (fechaCierre != null) {
            ticket.setFechaCierre(fechaCierre.toLocalDateTime());
        }

        return ticket;
    }
}
