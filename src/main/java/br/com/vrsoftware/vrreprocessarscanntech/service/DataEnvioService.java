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

    /**
     * Método responsável por gerar uma lista de datas no formato ISO
     * (yyyy-MM-dd) entre as datas inicial e final informadas, incluindo ambas.
     *
     * @param dataInicio data inicial no formato yyyy-MM-dd
     * @param dataFim data final no formato yyyy-MM-dd
     * @return Conjunto de datas entre dataInicio e dataFim
     */
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

    /**
     * Método responsável por salvar registros de {@link DataEnvio} para as
     * lojas informadas e intervalo de datas especificado. Caso já existam
     * registros para a data e loja, serão reutilizados; caso contrário, serão
     * criados novos. Ao final, é feito o reprocessamento dos registros.
     *
     * @param lojas lista de IDs das lojas para processamento
     * @param dataInicio data inicial no formato yyyy-MM-dd
     * @param dataFim data final no formato yyyy-MM-dd
     *
     * @throws IllegalArgumentException caso a lista de lojas seja vazia ou haja erro de formato de data
     * @see DataEnvioDAO#incluirRetornaId(DataEnvio)
     * @see DataEnvioDAO#reprocessarDataEnvio(List)
     */
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

    /**
     * Método responsável por listar os registros de {@link DataEnvio}
     * pendentes de reprocessamento para a loja informada.
     *
     * @param idLoja identificador da loja; caso seja {@code null}, lista de todas as lojas
     * @return lista de registros de {@link DataEnvio} pendentes
     *
     * @see DataEnvioDAO#listarParaReprocessar(Integer)
     */
    public List<DataEnvio> listarParaReprocessar(Integer idLoja) {
        return dataEnvioDAO.listarParaReprocessar(idLoja);
    }

    /**
     * Método responsável por listar um resumo dos registros de {@link DataEnvio}
     * agrupados por loja, contendo:
     * <ul>
     *   <li>ID da loja</li>
     *   <li>Descrição da loja</li>
     *   <li>Total de registros pendentes</li>
     *   <li>Data mais antiga</li>
     *   <li>Data mais recente</li>
     * </ul>
     *
     * @return lista de {@link LojaResumoDTO} ordenada pela descrição da loja
     *
     * @see LojaService#mapearIdParaNome()
     * @see DataEnvioDAO#listarParaReprocessar(Integer)
     */
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
