package br.com.vrsoftware.vrreprocessarscanntech.service;

import br.com.vrsoftware.vrreprocessarscanntech.repository.LojaDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LojaServiceTest {

    @Mock
    private LojaDAO lojaDAO;

    @InjectMocks
    private LojaService lojaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Teste do Método Listar - Deve retornar a lista de lojas do DAO")
    void testListar() {
        List<Map<String, Object>> lojasMock = new ArrayList<>();
        lojasMock.add(Map.of("id", 1, "descricao", "Loja 1"));
        lojasMock.add(Map.of("id", 2, "descricao", "Loja 2"));
        when(lojaDAO.listarLojas()).thenReturn(lojasMock);
        
        List<Map<String, Object>> result = lojaService.listar();

        assertEquals(2, result.size());
        assertEquals("Loja 1", result.get(0).get("descricao"));
        verify(lojaDAO, times(1)).listarLojas();
    }

    @Test
    @DisplayName("Teste do Método mapearIdParaNome - Deve mapear ID para descrição corretamente")
    void testMapearIdParaNome() {
        List<Map<String, Object>> lojasMock = new ArrayList<>();
        lojasMock.add(Map.of("id", 1, "descricao", "Loja 1"));
        lojasMock.add(Map.of("id", 2, "descricao", "Loja 2"));
        when(lojaDAO.listarLojas()).thenReturn(lojasMock);

        Map<Integer, String> result = lojaService.mapearIdParaNome();

        assertEquals(2, result.size());
        assertEquals("Loja 1", result.get(1));
        assertEquals("Loja 2", result.get(2));
        verify(lojaDAO, times(1)).listarLojas();
    }

    @Test
    @DisplayName("Teste do método mapearIdParaNome - Deve retornar mapa vazio quando lista de lojas estiver vazia")
    void testMapearIdParaNomeListaVazia() {
        when(lojaDAO.listarLojas()).thenReturn(Collections.emptyList());

        Map<Integer, String> result = lojaService.mapearIdParaNome();

        assertTrue(result.isEmpty());
        verify(lojaDAO, times(1)).listarLojas();
    }
}