package br.com.vrsoftware.vrreprocessarscanntech.dto;

import lombok.Data;
import java.util.List;

@Data
public class ReprocessarRequestDTO {
    private List<Integer> lojas;
    private String dataInicio;
    private String dataFim;

    public List<Integer> getLojas() {
        return lojas;
    }

    public void setLojas(List<Integer> lojas) {
        this.lojas = lojas;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getDataFim() {
        return dataFim;
    }

    public void setDataFim(String dataFim) {
        this.dataFim = dataFim;
    }
}
