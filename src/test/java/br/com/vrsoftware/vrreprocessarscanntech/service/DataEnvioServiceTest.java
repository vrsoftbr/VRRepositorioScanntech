package br.com.vrsoftware.vrreprocessarscanntech.service;

import br.com.vrsoftware.vrreprocessarscanntech.dto.LojaResumoDTO;
import br.com.vrsoftware.vrreprocessarscanntech.model.DataEnvio;
import br.com.vrsoftware.vrreprocessarscanntech.repository.DataEnvioDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DataEnvioServiceTest {

    @Mock
    private DataEnvioDAO dataEnvioDAO;

    @Mock
    private LojaService lojaService;

    @Mock
    private DataProcessamentoService dataProcessamentoService;

    @InjectMocks
    private DataEnvioService dataEnvioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Teste do método salvar - Quando a lista de loja está vazia")
    void testSalvarListaLojasVazia() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> dataEnvioService.salvar(Collections.emptyList(), "2025-01-01", "2025-01-10"));
        assertEquals("A lista de lojas não pode ser vazia", ex.getMessage());
    }

    @Test
    @DisplayName("Teste do método salvar - Quando o formato data está incorreto")
    void testSalvarFormatoDataInvalido() {
        List<Integer> lojas = List.of(1, 2);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> dataEnvioService.salvar(lojas, "01-01-2025", "31-01-2025"));
        assertEquals("Formato de data inválido. Use dd-MM-YYYY.", ex.getMessage());
    }

    @Test
    @DisplayName("Teste do método salvar - Quando a Data fim é maior ou igual a data de processamento")
    void testSalvarDataFimMaiorOuIgualDataProcessamento() {
        List<Integer> lojas = List.of(1);
        when(dataProcessamentoService.getDataProcessamento(1)).thenReturn("2025-01-15");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> dataEnvioService.salvar(lojas, "2025-01-01", "2025-01-20"));
        assertTrue(ex.getMessage().contains("não pode ser superior ou igual à data de processamento"));
    }

    @Test
    @DisplayName("Teste do método salvar - Incluir novos registros e reprocessar")
    void testSalvarIncluirNovosRegistrosEReprocessar() {
        List<Integer> lojas = List.of(1);
        when(dataProcessamentoService.getDataProcessamento(1)).thenReturn("2025-02-01");
        when(dataEnvioDAO.getId(anyInt(), anyString())).thenReturn(null);
        when(dataEnvioDAO.incluirRetornaId(any(DataEnvio.class))).thenReturn(100);

        dataEnvioService.salvar(lojas, "2025-01-01", "2025-01-03");

        verify(dataEnvioDAO, times(3)).incluirRetornaId(any(DataEnvio.class));
        verify(dataEnvioDAO, times(1)).reprocessarDataEnvio(anyList());
    }

    @Test
    @DisplayName("Teste do método salvar - Quando ja existem registos no banco")
    void testSalvarReutilizarIdsExistentes() {
        List<Integer> lojas = List.of(1);
        when(dataProcessamentoService.getDataProcessamento(1)).thenReturn("2025-02-01");
        when(dataEnvioDAO.getId(anyInt(), anyString())).thenReturn(200);

        dataEnvioService.salvar(lojas, "2025-01-01", "2025-01-02");

        verify(dataEnvioDAO, never()).incluirRetornaId(any(DataEnvio.class));
        verify(dataEnvioDAO, times(1)).reprocessarDataEnvio(anyList());
    }

    @Test
    @DisplayName("Teste do método listarParaReprocessar - Validar se o método esta trazendo a lista de registros marcados com reprocessar=true")
    void testListarParaReprocessarRetornarRegistrosMarcados() {
        List<DataEnvio> listaMock = List.of(new DataEnvio());
        when(dataEnvioDAO.listarParaReprocessar(1)).thenReturn(listaMock);

        List<DataEnvio> result = dataEnvioService.listarParaReprocessar(1);
        assertEquals(1, result.size());
        verify(dataEnvioDAO, times(1)).listarParaReprocessar(1);
    }

    @Test
    @DisplayName("Teste do método ListarResumoLoja - Registros devem vir agrupados e ordenados por loja")
    void testListarResumoPorLojaAgruparOrdenarRegistros() {
        DataEnvio d1 = new DataEnvio();
        d1.setIdLoja(1);
        d1.setData("2025-01-01");

        DataEnvio d2 = new DataEnvio();
        d2.setIdLoja(1);
        d2.setData("2025-01-05");

        DataEnvio d3 = new DataEnvio();
        d3.setIdLoja(2);
        d3.setData("2025-01-03");

        when(dataEnvioDAO.listarParaReprocessar(null)).thenReturn(List.of(d1, d2, d3));
        when(lojaService.mapearIdParaNome()).thenReturn(Map.of(1, "Loja A", 2, "Loja B"));

        List<LojaResumoDTO> result = dataEnvioService.listarResumoPorLoja();

        assertEquals(2, result.size());
        assertEquals("Loja A", result.get(0).getDescricao());
        assertEquals("01-01-2025", result.get(0).getDataInicio());
        assertEquals("05-01-2025", result.get(0).getDataFim());
    }
}