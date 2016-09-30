
package modelo;

/**
 *
 * @author Hemerson e Jefferson
 */
public class Endereco {
    private String logradouro;
    private String nomeLogradouro;
    private int num;
    private String complemento;
    private String cidade;
    private String estado;
    private String bairro;
    private String cep;
    private boolean propria;
    private String tempoReside;
    private String valorAluguel;
    
    public Endereco(){
        logradouro = "";
        this.nomeLogradouro = "";
        this.num = 0;
        this.complemento = "";
        this.cidade = "";
        this.estado = "";
        this.bairro = "";
        this.cep = "";
        this.propria = false;
        this.tempoReside = "";
        this.valorAluguel = "";
    }
    
    public Endereco(Endereco End){
        this.logradouro = End.getLogradouro();
        this.nomeLogradouro = End.getNomeLogradouro();
        this.num = End.getNum();
        this.complemento = End.getComplemento();
        this.cidade = End.getCidade();
        this.estado = End.getEstado();
        this.bairro = End.getBairro();
        this.cep = End.getCep();
        this.propria = End.isPropria();
        this.tempoReside = End.getTempoReside();
        this.valorAluguel = End.getValorAluguel();
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNomeLogradouro() {
        return nomeLogradouro;
    }

    public void setNomeLogradouro(String nomeLogradouro) {
        this.nomeLogradouro = nomeLogradouro;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isPropria() {
        return propria;
    }

    public void setPropria(boolean propria) {
        this.propria = propria;
    }

    public String getTempoReside() {
        return tempoReside;
    }

    public void setTempoReside(String tempoReside) {
        this.tempoReside = tempoReside;
    }

    public String getValorAluguel() {
        return valorAluguel;
    }

    public void setValorAluguel(String valorAluguel) {
        this.valorAluguel = valorAluguel;
    }

}