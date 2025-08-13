package br.com.vrsoftware.vrreprocessarscanntech.service;

import br.com.vrsoftware.vrreprocessarscanntech.repository.DataProcessamentoDAO;
import org.springframework.stereotype.Service;

@Service
public class DataProcessamentoService {

    private final DataProcessamentoDAO dataProcessamentoDAO;

    public DataProcessamentoService(DataProcessamentoDAO dataProcessamentoDAO) {
        this.dataProcessamentoDAO = dataProcessamentoDAO;
    }

    /**
     * Método responsável por obter a data de processamento configurada para
     * a loja informada.
     *
     * @param idLoja identificador da loja
     * @return data de processamento no formato ISO (yyyy-MM-dd)
     *
     * @throws IllegalArgumentException caso a data de processamento não esteja configurada
     */
    public String getDataProcessamento(Integer idLoja) {
        String data = dataProcessamentoDAO.getDataProcessamento(idLoja);
        if (data == null) {
            throw new IllegalArgumentException("Data de processamento da loja " + idLoja + " não configurada!");
        }
        return data;
    }

    /**
     * Método responsável por obter a data de processamento mais antiga
     * registrada no sistema.
     *
     * @return data mais antiga no formato ISO (yyyy-MM-dd)
     */
    public String getDataMaisAntiga() {
        return dataProcessamentoDAO.getDataMaisAntiga();
    }
}