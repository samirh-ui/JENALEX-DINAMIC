package com.example.dao;

import com.example.config.ConexionPostgresDatabase;
import com.example.model.ReporteIndicadores;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReporteDao {

    public ReporteIndicadores obtenerIndicadores() throws SQLException {
        ReporteIndicadores indicadores = new ReporteIndicadores();
        indicadores.setTotalEquipos(contar("SELECT COUNT(*) FROM dispositivo"));
        indicadores.setEquiposMantenimiento(contar("""
                SELECT COUNT(*)
                FROM dispositivo
                WHERE LOWER(COALESCE(estado, '')) LIKE '%mantenimiento%'
                """));
        indicadores.setTotalUsuarios(contar("SELECT COUNT(*) FROM usuario"));
        indicadores.setTotalSolicitudes(contar("SELECT COUNT(*) FROM ticket"));
        indicadores.setSolicitudesAbiertas(contar("""
                SELECT COUNT(*)
                FROM ticket
                WHERE LOWER(COALESCE(estado, '')) NOT IN ('cerrada', 'cerrado', 'resuelta', 'resuelto')
                """));
        indicadores.setSolicitudesCerradas(contar("""
                SELECT COUNT(*)
                FROM ticket
                WHERE LOWER(COALESCE(estado, '')) IN ('cerrada', 'cerrado', 'resuelta', 'resuelto')
                """));
        indicadores.setPorcentajeCumplimientoAns(calcularCumplimientoAns());
        return indicadores;
    }

    public List<Object[]> listarEquiposPorCategoria() throws SQLException {
        return consultarFilas("""
                SELECT COALESCE(categoria, 'Sin categoria') AS categoria,
                       COUNT(*) AS total,
                       SUM(CASE WHEN LOWER(COALESCE(estado, '')) LIKE '%mantenimiento%' THEN 1 ELSE 0 END) AS mantenimiento
                FROM dispositivo
                GROUP BY COALESCE(categoria, 'Sin categoria')
                ORDER BY total DESC
                """, 3);
    }

    public List<Object[]> listarSolicitudesPorEstado() throws SQLException {
        return consultarFilas("""
                SELECT COALESCE(estado, 'Sin estado') AS estado,
                       COUNT(*) AS total
                FROM ticket
                GROUP BY COALESCE(estado, 'Sin estado')
                ORDER BY total DESC
                """, 2);
    }

    public List<Object[]> listarCumplimientoPorTecnico() throws SQLException {
        return consultarFilas("""
                SELECT COALESCE(u.nombre, 'Sin asignar') AS tecnico,
                       COUNT(*) AS total,
                       SUM(
                           CASE
                               WHEN t.fecha_cierre IS NOT NULL
                                    AND a.tiempo_solucion_horas IS NOT NULL
                                    AND t.fecha_cierre <= t.fecha_creacion + (a.tiempo_solucion_horas * INTERVAL '1 hour')
                               THEN 1
                               ELSE 0
                           END
                       ) AS dentro_ans
                FROM ticket t
                LEFT JOIN ans a ON a.id = t.id_ans
                LEFT JOIN usuario u ON u.id = t.id_tecnico
                GROUP BY COALESCE(u.nombre, 'Sin asignar')
                ORDER BY total DESC
                """, 3);
    }

    private int contar(String sql) throws SQLException {
        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next() ? resultSet.getInt(1) : 0;
        }
    }

    private double calcularCumplimientoAns() throws SQLException {
        String sql = """
                SELECT COUNT(*) AS total,
                       SUM(
                           CASE
                               WHEN t.fecha_cierre IS NOT NULL
                                    AND a.tiempo_solucion_horas IS NOT NULL
                                    AND t.fecha_cierre <= t.fecha_creacion + (a.tiempo_solucion_horas * INTERVAL '1 hour')
                               THEN 1
                               ELSE 0
                           END
                       ) AS dentro_ans
                FROM ticket t
                LEFT JOIN ans a ON a.id = t.id_ans
                WHERE t.fecha_cierre IS NOT NULL
                """;

        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            if (!resultSet.next()) {
                return 0;
            }

            int total = resultSet.getInt("total");
            int dentroAns = resultSet.getInt("dentro_ans");
            if (total == 0) {
                return 0;
            }

            return (dentroAns * 100.0) / total;
        }
    }

    private List<Object[]> consultarFilas(String sql, int columnas) throws SQLException {
        List<Object[]> filas = new ArrayList<>();
        try (Connection connection = ConexionPostgresDatabase.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Object[] fila = new Object[columnas];
                for (int i = 0; i < columnas; i++) {
                    fila[i] = resultSet.getObject(i + 1);
                }
                filas.add(fila);
            }
        }
        return filas;
    }
}
