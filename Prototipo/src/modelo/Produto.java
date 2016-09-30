
package modelo;

import java.util.HashMap;

/**
 *
 * @author Hemerson e Jefferson
 */
public class Produto {

    private int codigo;
    private String descricao;
    private String valorVenda;
    private String valorCompra;
    private Fornecedor fornecedor;
    private int qtdeEntrada;
    private int qtdeVendida;
    private Data dataEntrada;
    private Data dataTermino;
    private Data dataCadastro;
    private boolean ativo;
    
    public Produto() {
        this.codigo = 0;
        this.descricao = null;
        this.fornecedor = null;
        this.qtdeEntrada = 0;
        this.qtdeVendida = 0;
        this.valorVenda = null;
        this.valorCompra = null;
        this.dataEntrada = null;
        this.dataTermino = null;
        this.ativo = false;
    }
    public Produto(int cod) {
        this.codigo = cod;
        this.descricao = null;
        this.fornecedor = null;
        this.qtdeEntrada = 0;
        this.qtdeVendida = 0;
        this.valorVenda = null;
        this.dataEntrada = null;
        this.dataTermino = null;
        this.ativo = false;
    }

    public Produto(String descricao) {
        this.codigo = 0;
        this.descricao = descricao;
        this.fornecedor = null;
        this.qtdeEntrada = 0;
        this.qtdeVendida = 0;
        this.valorVenda = null;
        this.valorCompra = null;
        this.dataEntrada = null;
        this.dataTermino = null;
        this.ativo = false;
    }

    public Produto(Produto p){
        this.codigo = p.getCodigo();
        this.descricao = p.getDescricao();
        this.fornecedor = new Fornecedor(p.getFornecedor());
        this.qtdeEntrada = p.getQtdeEntrada();
        this.qtdeVendida = p.getQtdeVendida();
        this.valorVenda = p.getValorVenda();
        this.valorCompra = p.getValorCompra();
        this.dataEntrada = new Data(p.getDataCompra());
        this.dataTermino = new Data(p.getDataTermino());
        this.ativo = p.isAtivo();
    }

    public Produto(HashMap<String,Object> dados){
        this.codigo = Integer.parseInt((String) dados.get("cod"));
        this.descricao = (String) dados.get("desc");
        try {
            this.qtdeEntrada = Integer.parseInt((String) dados.get("qtdeEntrada"));
        } catch (Exception e) {
        }
        try {
            this.qtdeVendida = Integer.parseInt((String) dados.get("qtdeVendida"));
        } catch (Exception e) {
        }
        String aux;
        this.valorVenda = (String) dados.get("valorVenda");
        this.valorCompra = (String) dados.get("valorCompra");
        this.dataEntrada = new Data();
        aux = (String)dados.get("dataEntrada");
        if (aux.compareTo("") != 0) {
            this.getDataCompra().setDia(aux.split("/")[0]);
            this.getDataCompra().setMes(aux.split("/")[1]);
            this.getDataCompra().setAno(aux.split("/")[2]);
        } else {
            this.getDataCompra().setDia("00");
            this.getDataCompra().setMes("00");
            this.getDataCompra().setAno("0000");
        }
        this.dataTermino = new Data();
        aux = (String)dados.get("dataTermino");
        if (aux.compareTo("") != 0) {
            this.getDataTermino().setDia(aux.split("/")[0]);
            this.getDataTermino().setMes(aux.split("/")[1]);
            this.getDataTermino().setAno(aux.split("/")[2]);
        } else {
            this.getDataTermino().setDia("00");
            this.getDataTermino().setMes("00");
            this.getDataTermino().setAno("0000");
        }
        this.dataCadastro = new Data();
        aux = (String)dados.get("dataCadastro");
        if (aux.compareTo("") != 0) {
            this.getDataCadastro().setDia(aux.split("/")[0]);
            this.getDataCadastro().setMes(aux.split("/")[1]);
            this.getDataCadastro().setAno(aux.split("/")[2]);
        } else {
            this.getDataCadastro().setDia("00");
            this.getDataCadastro().setMes("00");
            this.getDataCadastro().setAno("0000");
        }
        this.fornecedor = new Fornecedor(dados);
        if (((String) dados.get("ativo")).equals("false")) {
            this.setAtivo(false);
        } else {
            this.setAtivo(true);
        }
    }
    public String getValorCompra() {
        return valorCompra;
    }

    public void setValorCompra(String valorCompra) {
        this.valorCompra = valorCompra;
    }

    public String getValorVenda() {
        return valorVenda;
    }

    public void setValorVenda(String valorVenda) {
        this.valorVenda = valorVenda;
    }
    

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Data getDataCompra() {
        return dataEntrada;
    }

    public void setDataCompra(Data dataCompra) {
        this.dataEntrada = new Data(dataCompra);
    }

    public Data getDataTermino() {
        return dataTermino;
    }

    public void setDataTermino(Data dataTermino) {
        this.dataTermino = new Data(dataTermino);
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = new Fornecedor(fornecedor);
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getQtdeEntrada() {
        return qtdeEntrada;
    }

    public void setQtdeEntrada(int qtdeEntrada) {
        this.qtdeEntrada = qtdeEntrada;
    }

    public int getQtdeVendida() {
        return qtdeVendida;
    }

    public void setQtdeVendida(int qtdeVendida) {
        this.qtdeVendida = qtdeVendida;
    }

    public Data getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Data dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Data getDataEntrada() {
        return dataEntrada;
    }

    public void setDataEntrada(Data dataEntrada) {
        this.dataEntrada = dataEntrada;
    }

    
    public HashMap<String,Object> toHashMap(){
        HashMap<String,Object> retorno = new HashMap<String,Object>();

        retorno.put("cod", this.codigo+"");
        retorno.put("desc", this.descricao);
        retorno.put("valorVenda", this.valorVenda);
        retorno.put("valorCompra", this.valorCompra);
        if(fornecedor != null){
            retorno.put("nomeFornecedor", this.fornecedor.getNome());
            retorno.put("contatoFornecedor", this.fornecedor.getTelefone());
        }else{
            retorno.put("nomeFornecedor", "");
            retorno.put("contatoFornecedor", "");
        }
        retorno.put("qtdeEntrada", this.qtdeEntrada+"");
        retorno.put("qtdeVendida",this.qtdeVendida+"");
        String aux = "00/00/0000";
        if(dataEntrada != null){
            aux = this.dataEntrada.getDia()+"/"+this.dataEntrada.getMes()+"/"+
                    this.dataEntrada.getAno();
        }
        retorno.put("dataEntrada", aux);
        aux = "00/00/0000";
        if(dataTermino != null){
            aux = this.dataTermino.getDia()+"/"+this.dataTermino.getMes()+"/"+
                    this.dataTermino.getAno();
        }
        retorno.put("dataTermino", aux);
        aux = "00/00/0000";
        if(dataCadastro != null){
            aux = this.dataCadastro.getDia()+"/"+this.dataCadastro.getMes()+"/"+
                    this.dataCadastro.getAno();
        }
        retorno.put("dataCadastro", aux);
        if (this.isAtivo()) {
            retorno.put("ativo", "true");
        } else {
            retorno.put("ativo", "false");
        }
        return retorno;

    }

}
