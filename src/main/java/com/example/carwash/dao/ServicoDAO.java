package com.example.carwash.dao;

import com.example.carwash.model.Servico;
import com.example.carwash.model.TipoServico;
import com.example.carwash.model.Plano;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServicoDAO {
    private Connection connection;

    public ServicoDAO(Connection connection) {
        this.connection = connection;
    }

    public Servico buscarServicoPorId(int servicoId) throws SQLException {
        String sql = "SELECT * FROM servicos WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, servicoId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Servico servico = new Servico();
                    servico.setId(rs.getInt("id"));
                    servico.setDescricao(rs.getString("descricao"));
                    servico.setTipoServico(TipoServico.valueOf(rs.getString("tipo_servico")));
                    servico.setPlano(Plano.valueOf(rs.getString("plano")));
                    servico.setPreco(rs.getBigDecimal("preco"));
                    return servico;
                }
            }
        }
        return null;
    }
}
