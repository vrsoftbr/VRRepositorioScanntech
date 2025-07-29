package br.com.vrsoftware.vrreprocessarscanntech.service;

import br.com.vrsoftware.vrreprocessarscanntech.model.DataEnvio;
import br.com.vrsoftware.vrreprocessarscanntech.repository.DataEnvioDAO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DataEnvioService {

    private final DataEnvioDAO dataEnvioDAO;

    public DataEnvioService(DataEnvioDAO dataEnvioDAO) {
        this.dataEnvioDAO = dataEnvioDAO;
    }

    /**
     * Gera um Set de datas entre inicio e fim (formato yyyy-MM-dd)
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
     * Marca registros para reprocessamento (cria se não existir).
     */

    @Transactional
    public void reprocessar(List<Integer> lojas, String dataInicio, String dataFim) {
        Set<String> datas = gerarDatas(dataInicio, dataFim);
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
     * Lista registros com reprocessar = true para uma loja.
     */
    public List<DataEnvio> listarParaReprocessar(Integer idLoja) {
        return dataEnvioDAO.listarParaReprocessar(idLoja);
    }
}
