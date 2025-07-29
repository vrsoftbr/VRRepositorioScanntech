package br.com.vrsoftware.vrreprocessarscanntech.model;

public class DataEnvio {

    private int id;
    private int idLoja;
    private String data;
    private boolean enviado;
    private boolean erro;
    private int loteVendaEnviada;
    private int loteVendaCanceladaEnviada;
    private int totalLoteVenda;
    private int totalLoteVendaCancelada;
    private boolean reprocessar;

    public DataEnvio() {
    }

    public DataEnvio(int idLoja, String data) {
        this.idLoja = idLoja;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdLoja() {
        return idLoja;
    }

    public void setIdLoja(int idLoja) {
        this.idLoja = idLoja;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isEnviado() {
        return enviado;
    }

    public void setEnviado(boolean enviado) {
        this.enviado = enviado;
    }

    public boolean isErro() {
        return erro;
    }

    public void setErro(boolean erro) {
        this.erro = erro;
    }

    public int getLoteVendaEnviada() {
        return loteVendaEnviada;
    }

    public void setLoteVendaEnviada(int loteVendaEnviada) {
        this.loteVendaEnviada = loteVendaEnviada;
    }

    public int getLoteVendaCanceladaEnviada() {
        return loteVendaCanceladaEnviada;
    }

    public void setLoteVendaCanceladaEnviada(int loteVendaCanceladaEnviada) {
        this.loteVendaCanceladaEnviada = loteVendaCanceladaEnviada;
    }

    public int getTotalLoteVenda() {
        return totalLoteVenda;
    }

    public void setTotalLoteVenda(int totalLoteVenda) {
        this.totalLoteVenda = totalLoteVenda;
    }

    public int getTotalLoteVendaCancelada() {
        return totalLoteVendaCancelada;
    }

    public void setTotalLoteVendaCancelada(int totalLoteVendaCancelada) {
        this.totalLoteVendaCancelada = totalLoteVendaCancelada;
    }

    public boolean isReprocessar() {
        return reprocessar;
    }

    public void setReprocessar(boolean reprocessar) {
        this.reprocessar = reprocessar;
    }
}
