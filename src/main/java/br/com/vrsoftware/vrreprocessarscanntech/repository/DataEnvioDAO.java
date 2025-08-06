package br.com.vrsoftware.vrreprocessarscanntech.repository;

import br.com.vrsoftware.vrreprocessarscanntech.model.DataEnvio;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.util.*;

@Repository
public class DataEnvioDAO {

    private final JdbcClient jdbc;

    public DataEnvioDAO(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public Integer getId(Integer idLoja, String data) {
        return jdbc.sql("""
                SELECT id FROM scanntech.dataenvio
                WHERE id_loja = :idLoja AND data = :data
                LIMIT 1
                """)
                .param("idLoja", idLoja)
                .param("data", Date.valueOf(data))
                .query(Integer.class)
                .optional()
                .orElse(null);
    }

    public int incluirRetornaId(DataEnvio dataEnvio) {
        return jdbc.sql("""
                INSERT INTO scanntech.dataenvio
                (id_loja, data, enviado, erro, lotevendaenviada, lotevendacanceladaenviada, totallotevenda, totallotevendacancelada, reprocessar)
                VALUES (:idLoja, :data, false, false, 0, 0, 0, 0, true)
                RETURNING id
                """)
                .param("idLoja", dataEnvio.getIdLoja())
                .param("data", Date.valueOf(dataEnvio.getData()))
                .query(Integer.class)
                .single();
    }

    public void reprocessarDataEnvio(List<Integer> ids) {
        jdbc.sql("""
                UPDATE scanntech.dataenvio
                SET enviado = false, erro = false, reprocessar = true,
                lotevendaenviada = 0, lotevendacanceladaenviada = 0, totallotevenda = 0, totallotevendacancelada = 0
                WHERE id IN (:ids)
                """)
                .param("ids", ids)
                .update();
    }

    public List<DataEnvio> listarParaReprocessar(Integer idLoja) {
        String sql = """
        SELECT * FROM scanntech.dataenvio
        WHERE reprocessar = true
        """ + (idLoja != null ? " AND id_loja = :idLoja" : "") + """
        ORDER BY data ASC
    """;

        var query = jdbc.sql(sql);
        if (idLoja != null) query.param("idLoja", idLoja);

        return query.query(DataEnvio.class).list();
    }
}
