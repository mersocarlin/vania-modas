
package modelo;

/**
 *
 * @author Hemerson e Jefferson
 */
public class Trabalho {
    private String nomeEmpresa;
    private String funcao;
    private Endereco endereco;
    private String telefone;
    private String salario;
    private Data dataAdmissao;
    
    public Trabalho(){
        this.nomeEmpresa = "";
        this.funcao = "";
        this.endereco = new Endereco();
        this.telefone = "";
        this.salario = "";
        this.dataAdmissao = new Data();
    }
    
    public Trabalho(Trabalho trab){
        this.nomeEmpresa = trab.nomeEmpresa;
        this.funcao = trab.funcao;
        this.endereco = trab.endereco;
        this.telefone = trab.telefone;
        this.salario = trab.salario;
        this.dataAdmissao = trab.dataAdmissao;
    }

    public Data getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(Data dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getSalario() {
        return salario;
    }

    public void setSalario(String salario) {
        this.salario = salario;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
   
}
