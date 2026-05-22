package com.example.dao;

import com.example.config.ConexionPostgresDatabase;
import com.example.model.Ans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AnsDao implements CrudDao<Ans> {
    @Override
    public Ans crear(Ans ans) throws SQLException {
        String sql = """
                INSERT INTO ans (tipo_solicitud, prioridad, tiempo_respuesta_horas, tiempo_solucion_horas, descripcion, activo)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            completar(statement, ans);
            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    ans.setId(keys.getInt(1));
                }
            }
        }
        return ans;
    }

    @Override
    public Optional<Ans> buscarPorId(int id) throws SQLException {
        String sql = """
                SELECT id, tipo_solicitud, prioridad, tiempo_respuesta_horas, tiempo_solucion_horas, descripcion, activo
                FROM ans
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
    public List<Ans> listar() throws SQLException {
        String sql = """
                SELECT id, tipo_solicitud, prioridad, tiempo_respuesta_horas, tiempo_solucion_horas, descripcion, activo
                FROM ans
                ORDER BY tipo_solicitud, prioridad
                """;
        List<Ans> acuerdos = new ArrayList<>();
        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                acuerdos.add(mapear(resultSet));
            }
        }
        return acuerdos;
    }

    @Override
    public boolean actualizar(Ans ans) throws SQLException {
        String sql = """
                UPDATE ans
                SET tipo_solicitud = ?, prioridad = ?, tiempo_respuesta_horas = ?,
                    tiempo_solucion_horas = ?, descripcion = ?, activo = ?
                WHERE id = ?
                """;
        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            completar(statement, ans);
            statement.setInt(7, ans.getId());
            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminar(int id) throws SQLException {
        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM ans WHERE id = ?")) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    private void completar(PreparedStatement statement, Ans ans) throws SQLException {
        statement.setString(1, ans.getTipoSolicitud());
        statement.setString(2, ans.getPrioridad());
        statement.setInt(3, ans.getTiempoRespuestaHoras());
        statement.setInt(4, ans.getTiempoSolucionHoras());
        statement.setString(5, ans.getDescripcion());
        statement.setBoolean(6, ans.isActivo());
    }

    private Ans mapear(ResultSet resultSet) throws SQLException {
        Ans ans = new Ans();
        ans.setId(resultSet.getInt("id"));
        ans.setTipoSolicitud(resultSet.getString("tipo_solicitud"));
        ans.setPrioridad(resultSet.getString("prioridad"));
        ans.setTiempoRespuestaHoras(resultSet.getInt("tiempo_respuesta_horas"));
        ans.setTiempoSolucionHoras(resultSet.getInt("tiempo_solucion_horas"));
        ans.setDescripcion(resultSet.getString("descripcion"));
        ans.setActivo(resultSet.getBoolean("activo"));
        return ans;
    }
}
