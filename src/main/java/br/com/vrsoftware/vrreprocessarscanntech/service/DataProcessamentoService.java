package br.com.vrsoftware.vrreprocessarscanntech.service;

import br.com.vrsoftware.vrreprocessarscanntech.repository.DataProcessamentoDAO;
import org.springframework.stereotype.Service;

@Service
public class DataProcessamentoService {

    private final DataProcessamentoDAO dataProcessamentoDAO;

    public DataProcessamentoService(DataProcessamentoDAO dataProcessamentoDAO) {
        this.dataProcessamentoDAO = dataProcessamentoDAO;
    }

    public String getDataProcessamento(Integer idLoja) {
        String data = dataProcessamentoDAO.getDataProcessamento(idLoja);
        if (data == null) {
            throw new IllegalArgumentException("Data de processamento da loja " + idLoja + " não configurada!");
        }
        return data;
    }

    public String getDataMaisAntiga() {
        return dataProcessamentoDAO.getDataMaisAntiga();
    }
}