package br.com.vrsoftware.vrreprocessarscanntech.dto;

public class LojaResumoDTO {
    private int idLoja;
    private long total;
    private String descricao;
    private String dataInicio;
    private String dataFim;

    public LojaResumoDTO(int idLoja, long total, String descricao, String dataInicio, String dataFim) {
        this.idLoja = idLoja;
        this.total = total;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public int getIdLoja() {
        return idLoja;
    }

    public void setIdLoja(int idLoja) {
        this.idLoja = idLoja;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
