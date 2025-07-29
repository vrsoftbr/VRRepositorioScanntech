package br.com.vrsoftware.vrreprocessarscanntech.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/status")
public class StatusController {

    private String status = "LIBERADO";
    private String mensagem = "Sistema disponível para reprocessamento";

    @GetMapping
    public Map<String, Object> statusServidor() {
        return Map.of(
                "status", "ONLINE",
                "timestamp", java.time.LocalDateTime.now().toString()
        );
    }

    @PostMapping("/aguarde")
    public Map<String, String> setAguarde(@RequestBody(required = false) Map<String, String> body) {
        this.status = "AGUARDE";
        this.mensagem = body != null ? body.getOrDefault("mensagem", "Processamento em andamento") : "Processamento em andamento";
        return Map.of("status", status, "mensagem", mensagem);
    }

    @GetMapping("/espera")
    public Map<String, String> getStatus() {
        return Map.of("status", status, "mensagem", mensagem);
    }
}
