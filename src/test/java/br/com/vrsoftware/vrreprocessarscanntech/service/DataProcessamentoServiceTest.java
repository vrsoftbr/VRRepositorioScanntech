package br.com.vrsoftware.vrreprocessarscanntech.service;

import br.com.vrsoftware.vrreprocessarscanntech.repository.DataProcessamentoDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DataProcessamentoServiceTest {

    @Mock
    private DataProcessamentoDAO dataProcessamentoDAO;

    @InjectMocks
    private DataProcessamentoService dataProcessamentoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Teste do método GetDataProcessamento - Deve retornar a data de processamento quando existir")
    void testGetDataProcessamentoExisteData() {
        Integer idLoja = 1;
        when(dataProcessamentoDAO.getDataProcessamento(idLoja)).thenReturn("2025-08-06");

        String result = dataProcessamentoService.getDataProcessamento(idLoja);

        assertEquals("2025-08-06", result);
        verify(dataProcessamentoDAO, times(1)).getDataProcessamento(idLoja);
    }

    @Test
    @DisplayName("Teste do método GetDataProcessamento - Deve lançar exceção quando a data de processamento for nula")
    void testGetDataProcessamentoNaoExisteData() {
        Integer idLoja = 2;
        when(dataProcessamentoDAO.getDataProcessamento(idLoja)).thenReturn(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> dataProcessamentoService.getDataProcessamento(idLoja)
        );
        assertEquals("Data de processamento da loja 2 não configurada!", exception.getMessage());
        verify(dataProcessamentoDAO, times(1)).getDataProcessamento(idLoja);
    }

    @Test
    @DisplayName("Teste do método  Deve retornar a data mais antiga corretamente")
    void testGetDataMaisAntiga() {
        when(dataProcessamentoDAO.getDataMaisAntiga()).thenReturn("2025-01-01");

        String result = dataProcessamentoService.getDataMaisAntiga();

        assertEquals("2025-01-01", result);
        verify(dataProcessamentoDAO, times(1)).getDataMaisAntiga();
    }
}