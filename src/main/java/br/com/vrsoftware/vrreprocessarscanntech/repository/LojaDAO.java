package br.com.vrsoftware.vrreprocessarscanntech.repository;

import br.com.vrsoftware.vrreprocessarscanntech.config.DatabaseConfig;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class LojaDAO {

    private final JdbcClient jdbc;

    public LojaDAO(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public List<Map<String, Object>> listarLojas() {
        return jdbc.sql("SELECT id, descricao FROM loja ORDER BY descricao")
                .query()
                .listOfRows();
    }
}