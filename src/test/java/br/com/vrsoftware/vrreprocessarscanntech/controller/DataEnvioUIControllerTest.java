package br.com.vrsoftware.vrreprocessarscanntech.controller;

import br.com.vrsoftware.vrreprocessarscanntech.dto.LojaResumoDTO;
import br.com.vrsoftware.vrreprocessarscanntech.service.DataEnvioService;
import br.com.vrsoftware.vrreprocessarscanntech.service.LojaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DataEnvioUIControllerTest {

    private DataEnvioService dataEnvioService;
    private LojaService lojaService;
    private DataEnvioUIController controller;
    private Model model;

    @BeforeEach
    void setUp() {
        dataEnvioService = mock(DataEnvioService.class);
        lojaService = mock(LojaService.class);
        model = mock(Model.class);
        controller = new DataEnvioUIController(dataEnvioService, lojaService);
    }

    @Test
    @DisplayName("Teste do método index - Deve retornar a view dataenvio-form com lista de lojas")
    void testIndex() {
        when(lojaService.listar()).thenReturn(List.of(
                Map.of("id", 1, "nome", "Loja 1"),
                Map.of("id", 2, "nome", "Loja 2")
        ));

        String viewName = controller.index(model);

        verify(model, times(1)).addAttribute(eq("lojas"), any());
        assertEquals("dataenvio-form", viewName);
    }

    @Test
    @DisplayName("Teste do método reprocessar - Sucesso ao salvar dados")
    void testReprocessarSucesso() {
        when(lojaService.listar()).thenReturn(List.of(
                Map.of("id", 1, "nome", "Loja 1")
        ));

        String view = controller.reprocessar(
                List.of(1, 2), "2025-01-01", "2025-01-31", model);

        verify(dataEnvioService, times(1)).salvar(List.of(1, 2), "2025-01-01", "2025-01-31");
        verify(model, times(1)).addAttribute("mensagem", "Registros marcados para reprocessamento!");
        verify(model, times(1)).addAttribute(eq("lojas"), any());
        assertEquals("dataenvio-form", view);
    }

    @Test
    @DisplayName("Teste do método reprocessar - Deve lidar com exceção do service")
    void testReprocessarErro() {
        doThrow(new RuntimeException("Falha ao salvar")).when(dataEnvioService)
                .salvar(anyList(), anyString(), anyString());

        when(lojaService.listar()).thenReturn(List.of(
                Map.of("id", 1, "nome", "Loja 1")
        ));

        String view = controller.reprocessar(
                List.of(1), "2025-01-01", "2025-01-31", model);

        verify(model, times(1)).addAttribute(eq("erro"), contains("Erro:"));
        verify(model, times(1)).addAttribute(eq("lojas"), any());
        assertEquals("dataenvio-form", view);
    }

    @Test
    @DisplayName("Teste do método listar - Deve retornar lista de resumos")
    void testListar() {
        List<LojaResumoDTO> resumoList = Arrays.asList(
                new LojaResumoDTO(1, 80, "Loja 1","25-07-2025", "26-07-2025"),
                new LojaResumoDTO(2, 45,"Loja 2","25-07-2025", "26-07-2025" )
        );

        when(dataEnvioService.listarResumoPorLoja()).thenReturn(resumoList);

        String view = controller.listar(model);

        verify(model, times(1)).addAttribute("resumos", resumoList);
        assertEquals("dataenvio-list", view);
    }

    @Test
    @DisplayName("Teste do método listar - Lista nula deve retornar lista vazia")
    void testListarComListaNula() {
        when(dataEnvioService.listarResumoPorLoja()).thenReturn(null);

        String view = controller.listar(model);

        verify(model, times(1)).addAttribute("resumos", List.of());
        assertEquals("dataenvio-list", view);
    }
}