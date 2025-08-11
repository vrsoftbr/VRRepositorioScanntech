package br.com.vrsoftware.vrreprocessarscanntech.controller;

import br.com.vrsoftware.vrreprocessarscanntech.dto.ReprocessarRequestDTO;
import br.com.vrsoftware.vrreprocessarscanntech.model.DataEnvio;
import br.com.vrsoftware.vrreprocessarscanntech.service.DataEnvioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DataEnvioControllerTest {

    private DataEnvioService dataEnvioService;
    private DataEnvioController dataEnvioController;

    @BeforeEach
    void setUp() {
        dataEnvioService = mock(DataEnvioService.class);
        dataEnvioController = new DataEnvioController(dataEnvioService);
    }

    @Test
    @DisplayName("Teste do metodo Reprocessar - Deve salvar as datas corretamente e retornar mensagem de sucesso")
    void testReprocessarSucesso() {
        ReprocessarRequestDTO request = new ReprocessarRequestDTO();
        request.setLojas(List.of(1, 2, 3));
        request.setDataInicio("2025-01-01");
        request.setDataFim("2025-01-31");

        String result = dataEnvioController.reprocessar(request);

        verify(dataEnvioService, times(1))
                .salvar(request.getLojas(), request.getDataInicio(), request.getDataFim());

        assertEquals("Datas marcadas para reprocessamento!", result);
    }

    @Test
    @DisplayName("Teste do Método Reprocessar - Deve lançar exceção ao falhar na chamada ao service")
    void testReprocessarException() {
        ReprocessarRequestDTO request = new ReprocessarRequestDTO();
        request.setLojas(List.of(1));
        request.setDataInicio("2025-01-01");
        request.setDataFim("2025-01-31");

        doThrow(new RuntimeException("Erro")).when(dataEnvioService)
                .salvar(anyList(), any(), any());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            dataEnvioController.reprocessar(request);
        });

        assertEquals("Erro ao reprocessar datas", ex.getMessage());
        assertNotNull(ex.getCause());
    }

    @Test
    @DisplayName("Teste do Método listarParaReprocessar - Deve retornar lista de registros da loja com sucesso")
    void testListarParaReprocessarSucesso() {
        Integer lojaId = 10;
        List<DataEnvio> mockList = Arrays.asList(
                new DataEnvio(), new DataEnvio()
        );

        when(dataEnvioService.listarParaReprocessar(lojaId)).thenReturn(mockList);

        List<DataEnvio> result = dataEnvioController.listarParaReprocessar(lojaId);

        assertEquals(2, result.size());
        verify(dataEnvioService, times(1)).listarParaReprocessar(lojaId);
    }

    @Test
    @DisplayName("Teste do Método listarParaReprocessar - Deve lançar exceção ao falhar no service")
    void testListarParaReprocessar_Exception() {
        Integer lojaId = 99;
        when(dataEnvioService.listarParaReprocessar(lojaId)).thenThrow(new RuntimeException("Erro"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            dataEnvioController.listarParaReprocessar(lojaId);
        });

        assertEquals("Erro ao listar registros para reprocessamento", ex.getMessage());
    }
}
