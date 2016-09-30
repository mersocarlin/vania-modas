package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Hemerson e Jefferson
 */
public class Funcionario {

    private int codigo;
    private Pessoa pessoa;
    private Pessoa conjuge;
    private String login;
    private String senha;
    private boolean gerente;
    private boolean ativo;
    private Data dataCadastro;
    private List vendas;

    public Funcionario() {
        this.codigo = 0;
        this.pessoa = new Pessoa();
        this.conjuge = new Pessoa();
        this.login = "";
        this.senha = "";
        this.gerente = false;
        this.ativo = false;
        this.dataCadastro = new Data();
        this.vendas = new ArrayList();
    }

    public Funcionario(String str, String campo) {
        if (campo.equals("nome")) {
            this.codigo = 0;
            this.pessoa = new Pessoa(str, "nome");
            this.conjuge = null;
            this.login = null;
            this.senha = null;
            this.gerente = false;
            this.ativo = false;
            this.dataCadastro = null;
            this.vendas = null;
        } else {
            if (campo.equals("login")) {
                this.codigo = 0;
                this.pessoa = null;
                this.conjuge = null;
                this.login = str;
                this.senha = null;
                this.gerente = false;
                this.ativo = false;
                this.dataCadastro = null;
                this.vendas = null;
            }
        }
    }

    public Funcionario(Funcionario funcionario) {
        this.codigo = funcionario.codigo;
        this.pessoa = new Pessoa(funcionario.getPessoa());
        this.conjuge = new Pessoa(funcionario.getConjuge());
        this.login = funcionario.getLogin();
        this.senha = funcionario.getSenha();
        this.gerente = funcionario.isGerente();
        this.ativo = funcionario.isAtivo();
    }

    public Funcionario(int id) {
        this.codigo = id;
        this.pessoa = null;
        this.conjuge = null;
        this.login = null;
        this.senha = null;
        this.gerente = false;
        this.ativo = false;
    }

    public Funcionario(HashMap<String, Object> dados) {
        this.setCodigo(Integer.parseInt((String) (String) dados.get("cod")));
        this.dataCadastro = new Data();
        String nasc = (String) dados.get("dataCadastro");
        if (nasc.compareTo("") != 0) {
            this.getDataCadastro().setDia(nasc.split("/")[0]);
            this.getDataCadastro().setMes(nasc.split("/")[1]);
            this.getDataCadastro().setAno(nasc.split("/")[2]);
        } else {
            this.getDataCadastro().setDia("00");
            this.getDataCadastro().setMes("00");
            this.getDataCadastro().setAno("0000");
        }

        // ------------- DADOS PESSOAIS -----------------
        this.pessoa = new Pessoa();
        this.getPessoa().setNome((String) dados.get("nome"));
        nasc = (String) (String) dados.get("dataNasc");
        if (nasc.compareTo("") != 0) {
            this.getPessoa().getNascimento().setDia(nasc.split("/")[0]);
            this.getPessoa().getNascimento().setMes(nasc.split("/")[1]);
            this.getPessoa().getNascimento().setAno(nasc.split("/")[2]);
        } else {
            this.getPessoa().getNascimento().setDia("00");
            this.getPessoa().getNascimento().setMes("00");
            this.getPessoa().getNascimento().setAno("0000");
        }
        this.getPessoa().setRG((String) dados.get("rg"));
        this.getPessoa().setCPF((String) dados.get("cpf"));
        this.getPessoa().setSexo((String) dados.get("sexo"));
        this.getPessoa().setTelefone((String) dados.get("telefone"));
        this.getPessoa().setCelular((String) dados.get("celular"));
        this.getPessoa().setEstadoCivil((String) dados.get("estadoCivil"));
        this.getPessoa().setEmail((String) dados.get("email"));

        // ------------- ENDERECO -----------------
        this.getPessoa().getEndereco().setLogradouro((String) dados.get("tipoLogradouro"));
        this.getPessoa().getEndereco().setNomeLogradouro((String) dados.get("nomeLogradouro"));
        try {
            this.getPessoa().getEndereco().setNum(Integer.parseInt((String) dados.get("numLogradouro")));
        } catch (Exception exception) {
            this.getPessoa().getEndereco().setNum(0);
        }
        this.getPessoa().getEndereco().setComplemento((String) dados.get("complemento"));
        this.getPessoa().getEndereco().setBairro((String) dados.get("bairro"));
        this.getPessoa().getEndereco().setEstado((String) dados.get("estado"));
        this.getPessoa().getEndereco().setCidade((String) dados.get("cidade"));
        this.getPessoa().getEndereco().setCep((String) dados.get("cep"));

        if (((String) dados.get("propria")).equals("false")) {
            this.getPessoa().getEndereco().setPropria(false);
            this.getPessoa().getEndereco().setValorAluguel((String) dados.get("valorAluguel"));
        } else {
            this.getPessoa().getEndereco().setPropria(true);
        }
        this.getPessoa().getEndereco().setTempoReside((String) dados.get("tempoResidencia"));
        this.getPessoa().getTrabalho().setFuncao((String) dados.get("funcao"));
        this.getPessoa().getTrabalho().setSalario((String) dados.get("salario"));
        nasc = (String) dados.get("dataAdmissao");
        if (nasc.compareTo("") != 0) {
            this.getPessoa().getTrabalho().getDataAdmissao().setDia(nasc.split("/")[0]);
            this.getPessoa().getTrabalho().getDataAdmissao().setMes(nasc.split("/")[1]);
            this.getPessoa().getTrabalho().getDataAdmissao().setAno(nasc.split("/")[2]);
        } else {
            this.getPessoa().getTrabalho().getDataAdmissao().setDia("00");
            this.getPessoa().getTrabalho().getDataAdmissao().setMes("00");
            this.getPessoa().getTrabalho().getDataAdmissao().setAno("0000");
        }

        // ------------- CONJUGE -----------------
        this.conjuge = new Pessoa();
        this.getConjuge().setNome((String) dados.get("nomeConjuge"));
        nasc = (String) dados.get("nascConjuge");
        if (nasc.compareTo("") != 0) {
            this.getConjuge().getNascimento().setDia(nasc.split("/")[0]);
            this.getConjuge().getNascimento().setMes(nasc.split("/")[1]);
            this.getConjuge().getNascimento().setAno(nasc.split("/")[2]);
        } else {
            this.getConjuge().getNascimento().setDia("00");
            this.getConjuge().getNascimento().setMes("00");
            this.getConjuge().getNascimento().setAno("0000");
        }
        this.getConjuge().setRG((String) dados.get("rgConjuge"));
        this.getConjuge().setCPF((String) dados.get("cpfConjuge"));
        this.getConjuge().setSexo((String) dados.get("sexoConjuge"));
        this.getConjuge().setCelular((String) dados.get("celularConjuge"));

        // ------------- REFERENCIAS COMERCIAIS -----------------
        this.getPessoa().setRefCom1((String) dados.get("refCom1"));
        this.getPessoa().setRefCom2((String) dados.get("refCom2"));
        this.getPessoa().setRefCom3((String) dados.get("refCom3"));

        // ------------- LOGIN E SENHA -----------------
        this.setLogin((String) dados.get("login"));
        this.setSenha((String) dados.get("senha"));
        if (((String) dados.get("gerente")).equals("false")) {
            this.setGerente(false);
        } else {
            this.setGerente(true);
        }
        this.vendas = new ArrayList();
        String vend = (String) dados.get("vendas");
        if (!vend.equals("")) {
            for (int i = 0; i < vend.split("#").length; i++) {
                this.vendas.add(vend.split("#")[i]);
            }
        }
        if (((String) dados.get("ativo")).equals("false")) {
            this.setAtivo(false);
        } else {
            this.setAtivo(true);
        }

    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Data getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Data dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Pessoa getConjuge() {
        return conjuge;
    }

    public void setConjuge(Pessoa conjuge) {
        this.conjuge = conjuge;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isGerente() {
        return gerente;
    }

    public void setGerente(boolean gerente) {
        this.gerente = gerente;
    }

    public List getVendas() {
        return vendas;
    }

    public void setVendas(List vendas) {
        this.vendas = new ArrayList(vendas);
    }

    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> retorno = new HashMap<String, Object>();
        String aux = "";
        retorno.put("cod", this.codigo + "");
        retorno.put("login", this.login + "");
        retorno.put("senha", this.senha + "");
        if (this.isGerente()) {
            retorno.put("gerente", "true");
        } else {
            retorno.put("gerente", "false");
        }
        if (this.isAtivo()) {
            retorno.put("ativo", "true");
        } else {
            retorno.put("ativo", "false");
        }
        if (pessoa != null) {
            //faz operacoes da pessoa
            retorno.put("nome", this.getPessoa().getNome());
            retorno.put("cpf", this.getPessoa().getCPF());
            retorno.put("dataNasc", aux);
            retorno.put("rg", this.getPessoa().getRG());
            retorno.put("sexo", this.getPessoa().getSexo());
            retorno.put("telefone", this.getPessoa().getTelefone());
            retorno.put("celular", this.getPessoa().getCelular());
            retorno.put("estadoCivil", this.getPessoa().getEstadoCivil());
            retorno.put("email", this.getPessoa().getEmail());

            // ------------- REFERENCIAS COMERCIAIS -----------------
            retorno.put("refCom1", this.getPessoa().getRefCom1());
            retorno.put("refCom2", this.getPessoa().getRefCom2());
            retorno.put("refCom3", this.getPessoa().getRefCom3());

            // ------------- ENDERECO -----------------
            retorno.put("tipoLogradouro", this.getPessoa().getEndereco().getLogradouro());
            retorno.put("nomeLogradouro", this.getPessoa().getEndereco().getNomeLogradouro());
            retorno.put("numLogradouro", "" + this.getPessoa().getEndereco().getNum());
            retorno.put("complemento", this.getPessoa().getEndereco().getComplemento());
            retorno.put("bairro", this.getPessoa().getEndereco().getBairro());
            retorno.put("estado", this.getPessoa().getEndereco().getEstado());
            retorno.put("cidade", this.getPessoa().getEndereco().getCidade());
            retorno.put("cep", this.getPessoa().getEndereco().getCep());

            if (!this.getPessoa().getEndereco().isPropria()) {
                retorno.put("propria", "false");
                retorno.put("valorAluguel", this.getPessoa().getEndereco().getValorAluguel());
            } else {
                retorno.put("propria", "true");
            }
            retorno.put("tempoResidencia", this.getPessoa().getEndereco().getTempoReside());

            // ------------- TRABALHO -----------------
            retorno.put("funcao", this.getPessoa().getTrabalho().getFuncao());
            retorno.put("salario", this.getPessoa().getTrabalho().getSalario());
            aux = this.getPessoa().getTrabalho().getDataAdmissao().getDia() + "/" +
                    this.getPessoa().getTrabalho().getDataAdmissao().getMes() + "/" +
                    this.getPessoa().getTrabalho().getDataAdmissao().getAno();
            retorno.put("dataAdmissao", aux);

            // ------------- REFERENCIAS COMERCIAIS -----------------
            retorno.put("refCom1", this.getPessoa().getRefCom1());
            retorno.put("refCom2", this.getPessoa().getRefCom2());
            retorno.put("refCom3", this.getPessoa().getRefCom3());
        }
        if (conjuge != null) {

            retorno.put("nomeConjuge", this.getConjuge().getNome());
            aux = this.getConjuge().getNascimento().getDia() + "/" + this.getConjuge().getNascimento().getMes() +
                    "/" + this.getConjuge().getNascimento().getAno();
            retorno.put("nascConjuge", aux);
            retorno.put("rgConjuge", this.getConjuge().getRG());
            retorno.put("cpfConjuge", this.getConjuge().getCPF());
            retorno.put("sexoConjuge", this.getConjuge().getSexo());
            retorno.put("celularConjuge", this.getConjuge().getCelular());
        }

        if (dataCadastro != null) {
            aux = dataCadastro.getDia() + "/" + dataCadastro.getMes() + "/" +
                    dataCadastro.getAno();
            retorno.put("dataCadastro", aux);
        } else {
            aux = "00/00/0000";
            retorno.put("dataCadastro", aux);
        }
        String vend = "";
        if (this.getVendas().size() > 0) {
            for (int i = 0; i < this.getVendas().size(); i++) {
                vend += this.getVendas().get(i) + "#";
            }
        }
        retorno.put("vendas", vend);
        return retorno;
    }
}
