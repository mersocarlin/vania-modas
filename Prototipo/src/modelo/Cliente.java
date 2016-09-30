package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Hemerson e Jefferson
 */
public class Cliente {

    private int codigo;
    private Pessoa pessoa;
    private Pessoa conjuge;
    private Pessoa infPessoal1;
    private Pessoa infPessoal2;
    private boolean cadastroAprovado;
    private boolean ativo;
    private Data dataCadastro;
    private List compras;
    private boolean amigo;

    public Cliente() {
        this.codigo = 0;
        this.pessoa = new Pessoa();
        this.conjuge = new Pessoa();
        this.infPessoal1 = new Pessoa();
        this.infPessoal2 = new Pessoa();
        this.cadastroAprovado = false;
        this.ativo = false;
        this.dataCadastro = new Data();
        this.compras = new ArrayList();
        this.amigo = false;
    }

    public Cliente(int codigo) {
        this.codigo = codigo;
        this.pessoa = null;
        this.conjuge = null;
        this.infPessoal1 = null;
        this.infPessoal2 = null;
        this.cadastroAprovado = false;
        this.ativo = false;
        this.dataCadastro = null;
        this.compras = null;
        this.amigo = false;
    }

    public Cliente(String nome) {
        this.codigo = 0;
        this.pessoa = new Pessoa(nome, "nome");
        this.conjuge = null;
        this.infPessoal1 = null;
        this.infPessoal2 = null;
        this.cadastroAprovado = false;
        this.ativo = false;
        this.dataCadastro = null;
        this.compras = null;
        this.amigo = false;
    }

    public Cliente(Cliente cliente) {
        this.codigo = cliente.getCodigo();
        this.pessoa = new Pessoa(cliente.getPessoa());
        this.conjuge = new Pessoa(cliente.getConjuge());
        this.infPessoal1 = new Pessoa(cliente.getInfPessoal1());
        this.infPessoal2 = new Pessoa(cliente.getInfPessoal2());
        this.cadastroAprovado = cliente.isCadastroAprovado();
        this.ativo = cliente.isAtivo();
        this.dataCadastro = new Data(cliente.getDataCadastro());
        this.amigo = cliente.isAmigo();
    }

    public Cliente(HashMap<String, Object> dados) {
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

        // ------------- ENDERECO CLIENTE -----------------
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

        // ------------- TRABALHO CLIENTE -----------------
        this.getPessoa().getTrabalho().setNomeEmpresa((String) dados.get("trabalho"));
        this.getPessoa().getTrabalho().setFuncao((String) dados.get("funcao"));
        this.getPessoa().getTrabalho().setSalario((String) dados.get("salario"));
        this.getPessoa().getTrabalho().getEndereco().setLogradouro((String) dados.get("tipoLogradouroTrabalho"));
        this.getPessoa().getTrabalho().getEndereco().setNomeLogradouro((String) dados.get("nomeLogradouroTrabalho"));
        try {
            this.getPessoa().getTrabalho().getEndereco().setNum(Integer.parseInt((String) dados.get("numLogradouroTrabalho")));
        } catch (Exception exception) {
            this.getPessoa().getTrabalho().getEndereco().setNum(0);
        }
        this.getPessoa().getTrabalho().getEndereco().setComplemento((String) dados.get("complementoTrabalho"));
        this.getPessoa().getTrabalho().getEndereco().setBairro((String) dados.get("bairroTrabalho"));
        this.getPessoa().getTrabalho().setTelefone((String) dados.get("telefoneTrabalho"));
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

        // ------------- INFORMACOES PESSOAIS -----------------
        this.infPessoal1 = new Pessoa();
        this.infPessoal2 = new Pessoa();
        this.getInfPessoal1().setNome((String) dados.get("nomeInfPessoal1"));
        this.getInfPessoal1().setTelefone((String) dados.get("foneInfPessoal1"));
        this.getInfPessoal2().setNome((String) dados.get("nomeInfPessoal2"));
        this.getInfPessoal2().setTelefone((String) dados.get("foneInfPessoal2"));

        // ------------- REFERENCIAS COMERCIAIS -----------------
        this.getPessoa().setRefCom1((String) dados.get("refCom1"));
        this.getPessoa().setRefCom2((String) dados.get("refCom2"));
        this.getPessoa().setRefCom3((String) dados.get("refCom3"));

        if (((String) dados.get("cadastroAprovado")).equals("true")) {
            this.setCadastroAprovado(true);
        } else {
            this.setCadastroAprovado(false);
        }
        this.getPessoa().setObservacao((String) dados.get("observacao"));

        this.compras = new ArrayList();
        String comp = (String) dados.get("compras");
        if (!comp.equals("")) {
            for (int i = 0; i < comp.split("#").length; i++) {
                this.compras.add(comp.split("#")[i]);
            }
        }
        if (((String) dados.get("ativo")).equals("false")) {
            this.setAtivo(false);
        } else {
            this.setAtivo(true);
        }
        this.amigo = false;

    }

    public boolean isAmigo() {
        return amigo;
    }

    public void setAmigo(boolean amigo) {
        this.amigo = amigo;
    }

    public boolean isCadastroAprovado() {
        return cadastroAprovado;
    }

    public void setCadastroAprovado(boolean cadastroAprovado) {
        this.cadastroAprovado = cadastroAprovado;
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

    public Pessoa getInfPessoal1() {
        return infPessoal1;
    }

    public void setInfPessoal1(Pessoa infPessoal1) {
        this.infPessoal1 = infPessoal1;
    }

    public Pessoa getInfPessoal2() {
        return infPessoal2;
    }

    public void setInfPessoal2(Pessoa infPessoal2) {
        this.infPessoal2 = infPessoal2;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Data getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Data dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public List getCompras() {
        return compras;
    }

    public void setCompras(List compras) {
        this.compras = new ArrayList(compras);
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    
    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> retorno = new HashMap<String, Object>();
        String aux = "";
        retorno.put("cod", "" + this.codigo);
        if (pessoa != null) {
            //faz operações da pessoa
            retorno.put("nome", this.getPessoa().getNome());
            retorno.put("cpf", this.getPessoa().getCPF());
            retorno.put("dataNasc", aux);
            retorno.put("rg", this.getPessoa().getRG());
            retorno.put("sexo", this.getPessoa().getSexo());
            retorno.put("telefone", this.getPessoa().getTelefone());
            retorno.put("celular", this.getPessoa().getCelular());
            retorno.put("estadoCivil", this.getPessoa().getEstadoCivil());
            retorno.put("email", this.getPessoa().getEmail());

            // ------------- ENDERECO CLIENTE -----------------
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

            // ------------- TRABALHO CLIENTE -----------------

            retorno.put("trabalho", this.getPessoa().getTrabalho().getNomeEmpresa());
            retorno.put("funcao", this.getPessoa().getTrabalho().getFuncao());
            retorno.put("salario", this.getPessoa().getTrabalho().getSalario());
            retorno.put("tipoLogradouroTrabalho", this.getPessoa().getTrabalho().getEndereco().getLogradouro());
            retorno.put("nomeLogradouroTrabalho", this.getPessoa().getTrabalho().getEndereco().getNomeLogradouro());
            retorno.put("numLogradouroTrabalho", "" + this.getPessoa().getTrabalho().getEndereco().getNum());
            retorno.put("complementoTrabalho", this.getPessoa().getTrabalho().getEndereco().getComplemento());
            retorno.put("bairroTrabalho", this.getPessoa().getTrabalho().getEndereco().getBairro());
            retorno.put("telefoneTrabalho", this.getPessoa().getTrabalho().getTelefone());

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
        if (infPessoal1 != null) {
            retorno.put("nomeInfPessoal1", this.getInfPessoal1().getNome());
            retorno.put("foneInfPessoal1", this.getInfPessoal1().getTelefone());
        }
        if (infPessoal2 != null) {
            retorno.put("nomeInfPessoal2", this.getInfPessoal2().getNome());
            retorno.put("foneInfPessoal2", this.getInfPessoal2().getTelefone());
        }
        if (dataCadastro != null) {
            aux = dataCadastro.getDia() + "/" + dataCadastro.getMes() + "/" +
                    dataCadastro.getAno();
            retorno.put("dataCadastro", aux);
        } else {
            aux = "00/00/0000";
            retorno.put("dataCadastro", aux);
        }

        if (cadastroAprovado) {
            retorno.put("cadastroAprovado", "true");
        } else {
            retorno.put("cadastroAprovado", "false");
        }
        retorno.put("observacao", this.getPessoa().getObservacao());

        String comp = "";
        if (this.getCompras().size() > 0) {
            for (int i = 0; i < this.getCompras().size(); i++) {
                comp += this.getCompras().get(i) + "#";
            }
        }
        retorno.put("compras", comp);
        if (this.isAtivo()) {
            retorno.put("ativo", "true");
        } else {
            retorno.put("ativo", "false");
        }
        return retorno;
    }
}
