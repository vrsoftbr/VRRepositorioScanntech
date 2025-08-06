package br.com.vrsoftware.vrreprocessarscanntech.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.util.List;

@Data
public class ReprocessarRequestDTO {

    @NotEmpty(message = "A lista de Lojas não pode ser vazia")
    private List<Integer> lojas;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Data de início inválida (use yyyy-MM-dd)")
    private String dataInicio;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Data de fim inválida (use yyyy-MM-dd)")
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
