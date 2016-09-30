
package modelo;

/**
 *
 * @author Hemerson e Jefferson
 */
public class Pessoa {

    private String nome;
    private String sexo;
    private String estadoCivil;
    private String RG;
    private String CPF;
    private Data nascimento;
    private Endereco endereco;
    private String telefone;
    private String celular;
    private Trabalho trabalho;
    private String observacao;
    private String email;
    private String refCom1;
    private String refCom2;
    private String refCom3;

    public Pessoa() {
        this.nome = "";
        this.RG = "";
        this.CPF = "";
        this.sexo = "";
        this.estadoCivil = "";
        this.nascimento = new Data();
        this.endereco = new Endereco();
        this.telefone = "";
        this.trabalho = new Trabalho();
        this.celular = "";
        this.observacao = "";
        this.email = "";
        this.refCom1 = "";
        this.refCom2 = "";
        this.refCom3 = "";
    }

    /**
     * Construtor para cpf ou nome
     * @param dado valor a ser atribuido
     * @param campo sera cpf ou nome
     */
    public Pessoa(String dado, String campo) {
        if (campo.equals("cpf")) {
            this.nome = null;
            this.CPF = dado;
        } else {
            this.nome = dado;
            this.CPF = null;
        }
        this.RG = this.sexo = this.estadoCivil = this.telefone = null;
        this.nascimento = null;
        this.endereco = null;
        this.trabalho = null;
        this.celular = this.observacao = this.email = null;
        this.refCom1 = this.refCom2 = this.refCom3 = null;
    }

    public Pessoa(Pessoa pessoa) {
        this.nome = pessoa.getNome();
        this.RG = pessoa.getRG();
        this.CPF = pessoa.getCPF();
        this.sexo = pessoa.getSexo();
        this.estadoCivil = pessoa.getEstadoCivil();
        this.nascimento = new Data(pessoa.getNascimento());
        this.endereco = new Endereco(pessoa.getEndereco());
        this.telefone = pessoa.getTelefone();
        this.trabalho = new Trabalho(pessoa.getTrabalho());
        this.celular = pessoa.getCelular();
        this.observacao = pessoa.getObservacao();
        this.email = pessoa.getEmail();
        this.refCom1 = pessoa.getRefCom1();
        this.refCom2 = pessoa.getRefCom2();
        this.refCom3 = pessoa.getRefCom3();
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getRG() {
        return RG;
    }

    public void setRG(String RG) {
        this.RG = RG;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = new Endereco(endereco);
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public Data getNascimento() {
        return nascimento;
    }

    public void setNascimento(Data nascimento) {
        this.nascimento = new Data(nascimento);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Trabalho getTrabalho() {
        return trabalho;
    }

    public void setTrabalho(Trabalho trabalho) {
        this.trabalho = new Trabalho(trabalho);
    }

    public String getRefCom1() {
        return refCom1;
    }

    public void setRefCom1(String refCom1) {
        this.refCom1 = refCom1;
    }

    public String getRefCom2() {
        return refCom2;
    }

    public void setRefCom2(String refCom2) {
        this.refCom2 = refCom2;
    }

    public String getRefCom3() {
        return refCom3;
    }

    public void setRefCom3(String refCom3) {
        this.refCom3 = refCom3;
    }
}