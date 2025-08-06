package br.com.vrsoftware.vrreprocessarscanntech.service;

import br.com.vrsoftware.vrreprocessarscanntech.dto.LojaResumoDTO;
import br.com.vrsoftware.vrreprocessarscanntech.model.DataEnvio;
import br.com.vrsoftware.vrreprocessarscanntech.repository.DataEnvioDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DataEnvioService {

    private final DataEnvioDAO dataEnvioDAO;
    private final LojaService lojaService;
    private final DataProcessamentoService dataProcessamentoService;

    public DataEnvioService(DataEnvioDAO dataEnvioDAO, LojaService lojaService, DataProcessamentoService dataProcessamentoService) {
        this.dataEnvioDAO = dataEnvioDAO;
        this.lojaService = lojaService;
        this.dataProcessamentoService = dataProcessamentoService;
    }

    private Set<String> gerarDatas(String dataInicio, String dataFim) {
        Set<String> datas = new HashSet<>();
        LocalDate inicio = LocalDate.parse(dataInicio);
        LocalDate fim = LocalDate.parse(dataFim);

        while (!inicio.isAfter(fim)) {
            datas.add(inicio.toString());
            inicio = inicio.plusDays(1);
        }

        return datas;
    }

    @Transactional
    public void salvar(List<Integer> lojas, String dataInicio, String dataFim) {
        if (lojas == null || lojas.isEmpty()) {
            throw new IllegalArgumentException("A lista de lojas não pode ser vazia");
        }

        Set<String> datas;
        try{
            datas = gerarDatas(dataInicio, dataFim);
        } catch (Exception e){
            throw new IllegalArgumentException("Formato de data inválido. Use dd-MM-YYYY.");
        }

        DateTimeFormatter brFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Integer idLoja : lojas) {
            String dataProcessamento = dataProcessamentoService.getDataProcessamento(idLoja);
            LocalDate dataProc = LocalDate.parse(dataProcessamento);
            LocalDate fim = LocalDate.parse(dataFim);
            if (!fim.isBefore(dataProc)) {
                throw new IllegalArgumentException(
                        "A Data Fim para a loja " + idLoja + " não pode ser superior ou igual à data de processamento! " + dataProc.format(brFormatter)
                );
            }
        }

        List<Integer> idsParaReprocessar = new ArrayList<>();
        for (Integer idLoja : lojas) {
            for (String data : datas) {
                Integer idExistente = dataEnvioDAO.getId(idLoja, data);

                if (idExistente == null) {

                    DataEnvio novo = new DataEnvio();
                    novo.setIdLoja(idLoja);
                    novo.setData(data);
                    int novoId = dataEnvioDAO.incluirRetornaId(novo);
                    idsParaReprocessar.add(novoId);
                } else {
                    idsParaReprocessar.add(idExistente);
                }
            }
        }

        if (!idsParaReprocessar.isEmpty()) {
            dataEnvioDAO.reprocessarDataEnvio(idsParaReprocessar);
        }
    }

    public List<DataEnvio> listarParaReprocessar(Integer idLoja) {
        return dataEnvioDAO.listarParaReprocessar(idLoja);
    }

    public List<LojaResumoDTO> listarResumoPorLoja() {
        List<DataEnvio> registros = dataEnvioDAO.listarParaReprocessar(null);
        if (registros == null) {
            registros = List.of();
        }

        Map<Integer, String> descricaoLojas = lojaService.mapearIdParaNome();

        DateTimeFormatter entrada = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter saida = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        return registros.stream()
                .collect(Collectors.groupingBy(DataEnvio::getIdLoja))
                .entrySet().stream()
                .map(entry -> {
                    int idLoja = entry.getKey();
                    String descricaoLoja = descricaoLojas.getOrDefault(idLoja, "Loja " + idLoja);
                    List<DataEnvio> lista = entry.getValue();

                    long total = lista.size();

                    String menorData = lista.stream().map(DataEnvio::getData).min(String::compareTo).orElse(null);
                    String maiorData = lista.stream().map(DataEnvio::getData).max(String::compareTo).orElse(null);

                    String menorDataFormatada = menorData != null ? LocalDate.parse(menorData, entrada).format(saida) : "-";
                    String maiorDataFormatada = maiorData != null ? LocalDate.parse(maiorData, entrada).format(saida) : "-";

                    return new LojaResumoDTO(idLoja, total, descricaoLoja, menorDataFormatada, maiorDataFormatada);
                })
                .sorted(Comparator.comparing(LojaResumoDTO::getDescricao))
                .toList();
    }
}
