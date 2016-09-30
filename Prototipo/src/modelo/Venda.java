package modelo;

/**
 *
 * @author Hemerson e Jefferson
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Venda {

    private int codigo;
    private int codCliente;
    private int codFuncionario;
    private boolean avista;
    private Data dataVenda;
    private List itensVenda;
    private double valorTotal;
    private double valorRestante;
    private double valorEntrada;
    private List parcelas;

    public Venda() {
        this.codigo = 0;
        this.codCliente = 0;
        this.codFuncionario = 0;
        this.avista = false;
        this.dataVenda = new Data();
        this.itensVenda = new ArrayList();
        this.parcelas = new ArrayList();
        this.valorTotal = 0.0;
        this.valorRestante = 0.0;
        this.valorEntrada = 0.0;
    }

    public Venda(Venda venda) {
        this.codigo = venda.getCodigo();
        this.codCliente = venda.getCodCliente();
        this.codFuncionario = venda.getCodFuncionario();
        this.avista = venda.isAvista();
        this.dataVenda = new Data(venda.getDataVenda());
        this.itensVenda = new ArrayList(venda.getItensVenda());
        this.parcelas = new ArrayList(venda.getParcelas());
        this.valorTotal = venda.getValorTotal();
        this.valorRestante = venda.getValorRestante();
        this.valorEntrada = venda.getValorEntrada();
    }

    public Venda(int codigo) {
        this.codigo = codigo;
        this.codCliente = 0;
        this.codFuncionario = 0;
        this.avista = false;
        this.dataVenda = null;
        this.itensVenda = null;
        this.parcelas = null;
        this.valorTotal = 0.0;
        this.valorRestante = 0.0;
        this.valorEntrada = 0.0;
    }

    public Venda(HashMap<String, Object> dados) {
        this.setCodigo(Integer.parseInt((String) dados.get("cod")));
        this.setCodCliente(Integer.parseInt((String) dados.get("codCliente")));
        this.setCodFuncionario(Integer.parseInt((String) dados.get("codFuncionario")));
        if (((String) dados.get("avista")).equals("true")) {
            this.setAvista(true);
        } else {
            this.setAvista(false);
        }

        this.dataVenda = new Data();
        String data = (String) dados.get("dataVenda");
        if (data.compareTo("") != 0) {
            this.getDataVenda().setDia(data.split("/")[0]);
            this.getDataVenda().setMes(data.split("/")[1]);
            this.getDataVenda().setAno(data.split("/")[2]);
        } else {
            this.getDataVenda().setDia("00");
            this.getDataVenda().setMes("00");
            this.getDataVenda().setAno("0000");
        }

        List lista = (List) dados.get("itensVenda");
        List lista2 = new ArrayList();
        for (int i = 0; i < lista.size(); i++) {
            ItemVenda itemVenda = new ItemVenda();
            String str = (String) lista.get(i);
            itemVenda.setCod(Integer.parseInt(str.split("#")[0]));
            itemVenda.setDescricao(str.split("#")[1]);
            itemVenda.setQuantidade(Integer.parseInt(str.split("#")[2]));
            itemVenda.setPreco(str.split("#")[3]);
            lista2.add(itemVenda);
        }

        this.setItensVenda(new ArrayList(lista2));
        this.setValorTotal(Double.parseDouble((String) dados.get("valorTotal")));
        this.setValorRestante(Double.parseDouble((String) dados.get("valorRestante")));
        this.setValorEntrada(Double.parseDouble((String) dados.get("valorEntrada")));

        lista = (List) dados.get("parcelas");
        List lista3 = new ArrayList();
        //exemplo: dataVencimento#valor#datapagamento#situacao#funcionarioRecebeu
        for (int i = 0; i < lista.size(); i++) {
            Parcela parcela = new Parcela();
            String str = (String) lista.get(i);
            parcela.setDataVencimento(new Data(str.split("#")[0].split("/")[0], str.split("#")[0].split("/")[1], str.split("#")[0].split("/")[2]));
            parcela.setValor(str.split("#")[1]);
            parcela.setDataPagamento(new Data(str.split("#")[2].split("/")[0], str.split("#")[2].split("/")[1], str.split("#")[2].split("/")[2]));
            if (str.split("#")[3].equals("true")) {
                parcela.setPago(true);
            } else {
                parcela.setPago(false);
            }
            parcela.setLoginFuncionario(str.split("#")[4]);
            lista3.add(parcela);
        }
        this.setParcelas(new ArrayList(lista3));
    }

    public boolean isAvista() {
        return avista;
    }

    public void setAvista(boolean avista) {
        this.avista = avista;
    }

    public int getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(int codCliente) {
        this.codCliente = codCliente;
    }

    public int getCodFuncionario() {
        return codFuncionario;
    }

    public void setCodFuncionario(int codFuncionario) {
        this.codFuncionario = codFuncionario;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Data getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(Data dataVenda) {
        this.dataVenda = new Data(dataVenda);
    }

    public List getParcelas() {
        return parcelas;
    }

    public void setParcelas(List parcelas) {
        this.parcelas = new ArrayList(parcelas);
    }

    public List getItensVenda() {
        return itensVenda;
    }

    public void setItensVenda(List itensVenda) {
        this.itensVenda = new ArrayList(itensVenda);
    }

    public double getValorRestante() {
        return valorRestante;
    }

    public void setValorRestante(double valorRestante) {
        this.valorRestante = valorRestante;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public double getValorEntrada() {
        return valorEntrada;
    }

    public void setValorEntrada(double valorEntrada) {
        this.valorEntrada = valorEntrada;
    }

    public HashMap<String, Object> toHashMap() {
        HashMap<String, Object> retorno = new HashMap<String, Object>();
        retorno.put("cod", this.getCodigo() + "");
        retorno.put("codCliente", this.getCodCliente() + "");
        retorno.put("codFuncionario", this.getCodFuncionario() + "");
        
        if (this.isAvista()) {
            retorno.put("avista", "true");
        } else {
            retorno.put("avista", "false");
        }

        retorno.put("dataVenda", this.getDataVenda() + "");

        List lista = new ArrayList();
        for (int i = 0; i < this.getItensVenda().size(); i++) {
            lista.add(((ItemVenda) this.getItensVenda().get(i)).toString());
        }
        retorno.put("itensVenda", lista);
        retorno.put("valorTotal", this.getValorTotal() + "");
        retorno.put("valorRestante", this.getValorRestante() + "");
        retorno.put("valorEntrada", this.getValorEntrada() + "");
        lista = new ArrayList();
        for (int i = 0; i < this.getParcelas().size(); i++) {
            lista.add(((Parcela) this.getParcelas().get(i)).toString());
        }
        retorno.put("parcelas", lista);
        return retorno;
    }
}
