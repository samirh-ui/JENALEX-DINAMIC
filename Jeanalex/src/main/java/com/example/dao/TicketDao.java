package com.example.dao;

import com.example.config.ConexionPostgresDatabase;
import com.example.model.Ticket;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TicketDao implements CrudDao<Ticket> {

    @Override
    public Ticket crear(Ticket ticket) throws SQLException {
        String sql = "CALL sp_crear_ticket(?,?,?,?)";

        try (Connection connection = ConexionPostgresDatabase.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setString(1, ticket.getAsunto());
            statement.setString(2, ticket.getDescripcion());
            statement.setString(3, ticket.getEstado());
            statement.setString(4, ticket.getPrioridad());

            statement.execute();
        }

        return ticket;
    }

    @Override
    public Optional<Ticket> buscarPorId(int id) throws SQLException {
        String sql = "CALL sp_buscar_ticket(?)";

        try (Connection connection = ConexionPostgresDatabase.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

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
        String sql = "CALL sp_listar_tickets()";

        List<Ticket> tickets = new ArrayList<>();

        try (Connection connection = ConexionPostgresDatabase.getConnection();
             CallableStatement statement = connection.prepareCall(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                tickets.add(mapear(resultSet));
            }
        }

        return tickets;
    }

    public List<Ticket> listarPendientes() throws SQLException {
        String sql = "CALL sp_listar_tickets_pendientes()";
        List<Ticket> tickets = new ArrayList<>();

        try (Connection connection = ConexionPostgresDatabase.getConnection();
             CallableStatement statement = connection.prepareCall(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                tickets.add(mapear(resultSet));
            }
        }

        return tickets;
    }

    @Override
    public boolean actualizar(Ticket ticket) throws SQLException {
        String sql = "CALL sp_actualizar_ticket(?,?,?,?)";

        try (Connection connection = ConexionPostgresDatabase.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

            statement.setInt(1, ticket.getId());
            statement.setString(2, ticket.getAsunto());
            statement.setString(3, ticket.getDescripcion());
            statement.setString(4, ticket.getEstado());

            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        String sql = "CALL sp_eliminar_ticket(?)";

        try (Connection connection = ConexionPostgresDatabase.getConnection();
             CallableStatement statement = connection.prepareCall(sql)) {

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
        ticket.setFechaCreacion(resultSet.getTimestamp("fecha_creacion") == null ? null : resultSet.getTimestamp("fecha_creacion").toLocalDateTime());
        ticket.setFechaAsignacion(resultSet.getTimestamp("fecha_asignacion") == null ? null : resultSet.getTimestamp("fecha_asignacion").toLocalDateTime());
        ticket.setFechaCierre(resultSet.getTimestamp("fecha_cierre") == null ? null : resultSet.getTimestamp("fecha_cierre").toLocalDateTime());
        return ticket;
    }
}
