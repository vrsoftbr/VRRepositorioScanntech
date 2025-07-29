package br.com.vrsoftware.vrreprocessarscanntech.controller;

import br.com.vrsoftware.vrreprocessarscanntech.dto.ReprocessarRequestDTO;
import br.com.vrsoftware.vrreprocessarscanntech.model.DataEnvio;
import br.com.vrsoftware.vrreprocessarscanntech.service.DataEnvioService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/data-envio")
public class DataEnvioController {

    private final DataEnvioService dataEnvioService;

    public DataEnvioController(DataEnvioService dataEnvioService) {
        this.dataEnvioService = dataEnvioService;
    }

    /**
     * Marca registros para reprocessar (baseado em lojas e período).
     */
    @PostMapping("/reprocessar")
    public String reprocessar(@RequestBody ReprocessarRequestDTO request) {
        try {
            dataEnvioService.reprocessar(request.getLojas(), request.getDataInicio(), request.getDataFim());
            return "Datas marcadas para reprocessamento!";
        } catch (Exception e) {
            throw new RuntimeException("Erro ao reprocessar datas", e);
        }
    }

    /**
     * Lista registros marcados como reprocessar para uma loja.
     */
    @GetMapping("/reprocessar/{idLoja}")
    public List<DataEnvio> listarParaReprocessar(@PathVariable Integer idLoja) {
        try {
            return dataEnvioService.listarParaReprocessar(idLoja);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao listar registros para reprocessamento", e);
        }
    }
}

