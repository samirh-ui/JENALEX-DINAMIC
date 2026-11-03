package com.example.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CrudDao<T> {
    T crear(T entidad) throws SQLException;

    Optional<T> buscarPorId(int id) throws SQLException;

    List<T> listar() throws SQLException;

    boolean actualizar(T entidad) throws SQLException;

    boolean eliminar(int id) throws SQLException;
}
