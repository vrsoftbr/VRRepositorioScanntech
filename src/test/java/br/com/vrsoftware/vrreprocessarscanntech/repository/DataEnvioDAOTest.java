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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataEnvioDAOTest {

    @Mock
    private JdbcClient jdbcClient;

    @Mock
    private JdbcClient.StatementSpec statementSpec;

    @Mock
    private JdbcClient.MappedQuerySpec<Integer> integerQuerySpec;

    @Mock
    private JdbcClient.MappedQuerySpec<DataEnvio> dataEnvioQuerySpec;

    private DataEnvioDAO dataEnvioDAO;

    @BeforeEach
    void setUp() {
        dataEnvioDAO = new DataEnvioDAO(jdbcClient);
    }

    @Test
    void testConstructor() {
        DataEnvioDAO dataEnvioDao = new DataEnvioDAO(jdbcClient);
        assertNotNull(dataEnvioDao);
    }

    @Test
    @DisplayName("Teste do Método getId - Deve retornar o ID correto quando encontrado no banco de dados")
    void testGetIdRetornarIdQuandoEncontrado() {
        Integer idLoja = 1;
        String data = "2023-01-15";
        Integer idEsperado = 123;

        when(jdbcClient.sql(any(String.class))).thenReturn(statementSpec);
        when(statementSpec.param("idLoja", idLoja)).thenReturn(statementSpec);
        when(statementSpec.param("data", Date.valueOf(data))).thenReturn(statementSpec);
        when(statementSpec.query(Integer.class)).thenReturn(integerQuerySpec);
        when(integerQuerySpec.optional()).thenReturn(Optional.of(idEsperado));

        Integer resultado = dataEnvioDAO.getId(idLoja, data);

        assertEquals(idEsperado, resultado);
        verify(jdbcClient).sql(contains("SELECT id FROM scanntech.dataenvio"));
        verify(statementSpec).param("idLoja", idLoja);
        verify(statementSpec).param("data", Date.valueOf(data));
    }

    @Test
    @DisplayName("Teste do Método getId - Deve retornar null quando ID não for encontrado no banco de dados")
    void testGetIdRetornarNullNaoEncontrado() {
        Integer idLoja = 1;
        String data = "2023-01-15";

        when(jdbcClient.sql(any(String.class))).thenReturn(statementSpec);
        when(statementSpec.param("idLoja", idLoja)).thenReturn(statementSpec);
        when(statementSpec.param("data", Date.valueOf(data))).thenReturn(statementSpec);
        when(statementSpec.query(Integer.class)).thenReturn(integerQuerySpec);
        when(integerQuerySpec.optional()).thenReturn(Optional.empty());

        Integer resultado = dataEnvioDAO.getId(idLoja, data);

        assertNull(resultado);
    }

    @Test
    @DisplayName("Teste do Método incluirRetornaId - Deve inserir DataEnvio e retornar o ID gerado")
    void testIncluirRetornaIdInserirERetornarId() {
        DataEnvio dataEnvio = new DataEnvio();
        dataEnvio.setIdLoja(1);
        dataEnvio.setData("2023-01-15");
        Integer idEsperado = 456;

        when(jdbcClient.sql(any(String.class))).thenReturn(statementSpec);
        when(statementSpec.param("idLoja", dataEnvio.getIdLoja())).thenReturn(statementSpec);
        when(statementSpec.param("data", Date.valueOf(dataEnvio.getData()))).thenReturn(statementSpec);
        when(statementSpec.query(Integer.class)).thenReturn(integerQuerySpec);
        when(integerQuerySpec.single()).thenReturn(idEsperado);

        int resultado = dataEnvioDAO.incluirRetornaId(dataEnvio);

        assertEquals(idEsperado, resultado);
        verify(jdbcClient).sql(contains("INSERT INTO scanntech.dataenvio"));
        verify(jdbcClient).sql(contains("RETURNING id"));
        verify(statementSpec).param("idLoja", dataEnvio.getIdLoja());
        verify(statementSpec).param("data", Date.valueOf(dataEnvio.getData()));
    }

    @Test
    @DisplayName("Teste do Método reprocessarDataEnvio - Deve atualizar registros para reprocessamento com sucesso")
    void testReprocessarDataEnvioAtualizarRegistros() {
        List<Integer> ids = Arrays.asList(1, 2, 3);

        when(jdbcClient.sql(any(String.class))).thenReturn(statementSpec);
        when(statementSpec.param("ids", ids)).thenReturn(statementSpec);
        when(statementSpec.update()).thenReturn(3);

        dataEnvioDAO.reprocessarDataEnvio(ids);

        verify(jdbcClient).sql(contains("UPDATE scanntech.dataenvio"));
        verify(jdbcClient).sql(contains("SET enviado = false, erro = false, reprocessar = true"));
        verify(jdbcClient).sql(contains("WHERE id IN (:ids)"));
        verify(statementSpec).param("ids", ids);
        verify(statementSpec).update();
    }

    @Test
    @DisplayName("Teste do Método listarParaReprocessar - Deve listar DataEnvio para reprocessar filtrando por idLoja")
    void testListarParaReprocessarComIdLojaRetornarLista() {
        Integer idLoja = 1;
        DataEnvio dataEnvio1 = new DataEnvio();
        DataEnvio dataEnvio2 = new DataEnvio();
        List<DataEnvio> listaEsperada = Arrays.asList(dataEnvio1, dataEnvio2);

        when(jdbcClient.sql(any(String.class))).thenReturn(statementSpec);
        when(statementSpec.param("idLoja", idLoja)).thenReturn(statementSpec);
        when(statementSpec.query(DataEnvio.class)).thenReturn(dataEnvioQuerySpec);
        when(dataEnvioQuerySpec.list()).thenReturn(listaEsperada);

        List<DataEnvio> result = dataEnvioDAO.listarParaReprocessar(idLoja);

        assertEquals(listaEsperada, result);
        verify(jdbcClient).sql(contains("SELECT * FROM scanntech.dataenvio"));
        verify(jdbcClient).sql(contains("WHERE reprocessar = true"));
        verify(jdbcClient).sql(contains("AND id_loja = :idLoja"));
        verify(jdbcClient).sql(contains("ORDER BY data ASC"));
        verify(statementSpec).param("idLoja", idLoja);
    }

    @Test
    @DisplayName("Teste do Método listarParaReprocessar - Deve listar DataEnvio para reprocessar sem filtro de idLoja")
    void testListarParaReprocessarSemIdLojaRetornarLista() {
        DataEnvio dataEnvio1 = new DataEnvio();
        DataEnvio dataEnvio2 = new DataEnvio();
        List<DataEnvio> listaEsperada = Arrays.asList(dataEnvio1, dataEnvio2);

        when(jdbcClient.sql(any(String.class))).thenReturn(statementSpec);
        when(statementSpec.query(DataEnvio.class)).thenReturn(dataEnvioQuerySpec);
        when(dataEnvioQuerySpec.list()).thenReturn(listaEsperada);

        List<DataEnvio> result = dataEnvioDAO.listarParaReprocessar(null);

        assertEquals(listaEsperada, result);
        verify(jdbcClient).sql(contains("SELECT * FROM scanntech.dataenvio"));
        verify(jdbcClient).sql(contains("WHERE reprocessar = true"));
        verify(jdbcClient).sql(argThat(sql -> !sql.contains("AND id_loja = :idLoja")));
        verify(jdbcClient).sql(contains("ORDER BY data ASC"));
        verify(statementSpec, never()).param(eq("idLoja"), any());
    }

    @Test
    @DisplayName("Teste do Método listarParaReprocessar - Deve retornar lista vazia ao não encontrar registros para reprocessar")
    void testListarParaReprocessarRetornarListaVazia() {
        Integer idLoja = 1;
        List<DataEnvio> emptyList = Arrays.asList();

        when(jdbcClient.sql(any(String.class))).thenReturn(statementSpec);
        when(statementSpec.param("idLoja", idLoja)).thenReturn(statementSpec);
        when(statementSpec.query(DataEnvio.class)).thenReturn(dataEnvioQuerySpec);
        when(dataEnvioQuerySpec.list()).thenReturn(emptyList);

        List<DataEnvio> result = dataEnvioDAO.listarParaReprocessar(idLoja);

        assertTrue(result.isEmpty());
    }
}