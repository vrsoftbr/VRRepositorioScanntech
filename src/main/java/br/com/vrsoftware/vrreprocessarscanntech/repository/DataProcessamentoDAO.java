package br.com.vrsoftware.vrreprocessarscanntech.repository;

import br.com.vrsoftware.vrreprocessarscanntech.config.DatabaseConfig;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
public class DataProcessamentoDAO {

    private final JdbcClient jdbc;

    public DataProcessamentoDAO(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public String getDataProcessamento(int idLoja) {
        return jdbc.sql("SELECT data FROM dataprocessamento WHERE id_loja = :idLoja")
                .param("idLoja", idLoja)
                .query(String.class)
                .optional()
                .orElse(null);
    }

    public String getDataMaisAntiga() {
        return jdbc.sql("""
                SELECT dp.data 
                FROM dataprocessamento dp
                INNER JOIN loja l ON l.id = dp.id_loja
                WHERE l.id_situacaocadastro = 1
                ORDER BY dp.id
                LIMIT 1
                """)
                .query(String.class)
                .optional()
                .orElse(null);
    }
}
