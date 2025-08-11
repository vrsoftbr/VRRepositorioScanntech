package br.com.vrsoftware.vrreprocessarscanntech.repository;

import br.com.vrsoftware.vrreprocessarscanntech.model.DataEnvio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.sql.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataProcessamentoDAOTest {

    @Mock
    private JdbcClient jdbcClient;

    @Mock
    private JdbcClient.StatementSpec statementSpec;

    @Mock
    private JdbcClient.MappedQuerySpec<String> stringQuerySpec;

    private DataProcessamentoDAO dataProcessamentoDAO;

    @BeforeEach
    void setUp() {
        dataProcessamentoDAO = new DataProcessamentoDAO(jdbcClient);
    }

    @Test
    void testConstructor() {
        DataProcessamentoDAO dataProcessamentoDAO = new DataProcessamentoDAO(jdbcClient);
        assertNotNull(dataProcessamentoDAO);
    }

    @Test
    @DisplayName("Teste do Método getDataProcessamento - Deve retornar a data de correta ao validar no banco de dados")
    void testGetDataProcessamentoRetornaData() {
        Integer idLoja = 1;
        String dataEsperada = "20-07-2025";

        when(jdbcClient.sql(any(String.class))).thenReturn(statementSpec);
        when(statementSpec.param("idLoja", idLoja)).thenReturn(statementSpec);
        when(statementSpec.query(String.class)).thenReturn(stringQuerySpec);
        when(stringQuerySpec.optional()).thenReturn(Optional.of(dataEsperada));

        String resultado = dataProcessamentoDAO.getDataProcessamento(idLoja);

        assertEquals(dataEsperada, resultado);
        verify(jdbcClient).sql(contains("SELECT data FROM dataprocessamento"));
        verify(statementSpec).param("idLoja", idLoja);
    }

    @Test
    @DisplayName("Teste do Método getDataProcessamento - Deve retornar null quando data não for encontrada no banco")
    void testGetDataProcessamentoRetornaNull() {
        Integer idLoja = 1;

        when(jdbcClient.sql(any(String.class))).thenReturn(statementSpec);
        when(statementSpec.param("idLoja", idLoja)).thenReturn(statementSpec);
        when(statementSpec.query(String.class)).thenReturn(stringQuerySpec);
        when(stringQuerySpec.optional()).thenReturn(Optional.empty());

        String resultado = dataProcessamentoDAO.getDataProcessamento(idLoja);

        assertNull(resultado);
    }

    @Test
    @DisplayName("Teste do Método getDataMaisAntiga - Deve retornar a data mais antiga quando encontrada no banco")
    void testGetDataMaisAntigaRetornaData() {
        String dataEsperada = "2025-07-20";

        when(jdbcClient.sql(any(String.class))).thenReturn(statementSpec);
        when(statementSpec.query(String.class)).thenReturn(stringQuerySpec);
        when(stringQuerySpec.optional()).thenReturn(Optional.of(dataEsperada));

        String resultado = dataProcessamentoDAO.getDataMaisAntiga();

        assertEquals(dataEsperada, resultado);
        verify(jdbcClient).sql(contains("SELECT dp.data"));
        verify(statementSpec).query(String.class);
        verify(stringQuerySpec).optional();
    }

    @Test
    @DisplayName("Teste do Método getDataMaisAntiga - Deve retornar null quando nenhuma data for encontrada")
    void testGetDataMaisAntigaRetornaNull() {
        when(jdbcClient.sql(any(String.class))).thenReturn(statementSpec);
        when(statementSpec.query(String.class)).thenReturn(stringQuerySpec);
        when(stringQuerySpec.optional()).thenReturn(Optional.empty());

        String resultado = dataProcessamentoDAO.getDataMaisAntiga();

        assertNull(resultado);
        verify(jdbcClient).sql(contains("SELECT dp.data"));
        verify(statementSpec).query(String.class);
        verify(stringQuerySpec).optional();
    }
}