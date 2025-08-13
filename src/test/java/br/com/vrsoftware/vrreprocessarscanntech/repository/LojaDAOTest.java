package br.com.vrsoftware.vrreprocessarscanntech.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LojaDAOTest {

    @Mock
    private JdbcClient jdbc;

    @Mock
    private JdbcClient.StatementSpec statementSpec;

    @Mock
    private JdbcClient.ResultQuerySpec resultQuerySpec;

    private LojaDAO lojaDAO;

    @BeforeEach
    void setUp() {
        lojaDAO = new LojaDAO(jdbc);
    }

    @Test
    void deveListarLojas_quandoHaResultados() {
        String sqlEsperado = "SELECT id, descricao FROM loja ORDER BY descricao";
        List<Map<String, Object>> resultadoEsperado = List.of(
                Map.of("id", 1L, "descricao", "Loja A"),
                Map.of("id", 2L, "descricao", "Loja B")
        );

        when(jdbc.sql(sqlEsperado)).thenReturn(statementSpec);
        when(statementSpec.query()).thenReturn(resultQuerySpec);
        when(resultQuerySpec.listOfRows()).thenReturn(resultadoEsperado);

        List<Map<String, Object>> resultado = lojaDAO.listarLojas();

        assertThat(resultado).isEqualTo(resultadoEsperado);

        InOrder inOrder = inOrder(jdbc, statementSpec, resultQuerySpec);
        inOrder.verify(jdbc).sql(sqlEsperado);
        inOrder.verify(statementSpec).query();
        inOrder.verify(resultQuerySpec).listOfRows();
        verifyNoMoreInteractions(jdbc, statementSpec, resultQuerySpec);
    }

    @Test
    void deveRetornarListaVazia_quandoNaoHaResultados() {
        String sqlEsperado = "SELECT id, descricao FROM loja ORDER BY descricao";
        when(jdbc.sql(sqlEsperado)).thenReturn(statementSpec);
        when(statementSpec.query()).thenReturn(resultQuerySpec);
        when(resultQuerySpec.listOfRows()).thenReturn(List.of());

        List<Map<String, Object>> resultado = lojaDAO.listarLojas();

        assertThat(resultado).isEmpty();

        verify(jdbc).sql(sqlEsperado);
        verify(statementSpec).query();
        verify(resultQuerySpec).listOfRows();
        verifyNoMoreInteractions(jdbc, statementSpec, resultQuerySpec);
    }
}
