package br.com.vrsoftware.vrreprocessarscanntech.controller;

import br.com.vrsoftware.vrreprocessarscanntech.dto.LojaResumoDTO;
import br.com.vrsoftware.vrreprocessarscanntech.model.DataEnvio;
import br.com.vrsoftware.vrreprocessarscanntech.service.DataEnvioService;
import br.com.vrsoftware.vrreprocessarscanntech.service.LojaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ui/data-envio")
public class DataEnvioUIController {

    private final DataEnvioService dataEnvioService;
    private final LojaService lojaService;

    public DataEnvioUIController(DataEnvioService dataEnvioService, LojaService lojaService) {
        this.dataEnvioService = dataEnvioService;
        this.lojaService = lojaService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("lojas", lojaService.listar());
        return "dataenvio-form";
    }

    @PostMapping("/reprocessar")
    public String reprocessar(@RequestParam("lojas") List<Integer> lojas,
                              @RequestParam("dataInicio") String dataInicio,
                              @RequestParam("dataFim") String dataFim,
                              Model model) {
        try {
            dataEnvioService.salvar(lojas, dataInicio, dataFim);
            model.addAttribute("mensagem", "Registros marcados para reprocessamento!");
        } catch (Exception e) {
            model.addAttribute("erro", "Erro: " + e.getMessage());
        }

        model.addAttribute("lojas", lojaService.listar());
        return "dataenvio-form";
    }

    @GetMapping("/listar")
    public String listar(Model model) {
        List<LojaResumoDTO> resumos = dataEnvioService.listarResumoPorLoja();
        if (resumos == null) {
            resumos = List.of();
        }

        model.addAttribute("resumos", resumos);
        return "dataenvio-list";
    }
}