package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Hemerson e Jefferson
 * Exemplo entrada: valor#natureza(parcela,avista)#funcionario
 * Exemplo saida: valor#natureza#funcionario
 */
public class Caixa {

    private List entradas;
    private List saidas;
    private double valorTotalSaida;
    private double valorTotalEntrada;
    private int codigo;
    private String data;

    public Caixa() {
        this.entradas = null;
        this.saidas = null;
        this.valorTotalSaida = 0.0;
        this.codigo = 0;
        this.data = null;
        this.valorTotalEntrada = 0.0;
    }

    public Caixa(int codigo) {
        this.entradas = null;
        this.saidas = null;
        this.valorTotalSaida = 0.0;
        this.codigo = codigo;
        this.data = null;
        this.valorTotalEntrada = 0.0;
    }

    public Caixa(String data) {
        this.entradas = null;
        this.saidas = null;
        this.valorTotalSaida = 0.0;
        this.valorTotalEntrada = 0.0;
        this.codigo = 0;
        this.data = data;
    }

    public Caixa(HashMap<String, Object> dados) {
        //Exemplo entrada: valor#natureza#funcionario#horario
        //Exemplo saida: valor#natureza#funcionario#horario
        //Exemplo natureza: parcela/numeroParcela/codVenda/cliente
        //Exemplo natureza: avista/cliente (cliente ou nao)
        //Exemplo natureza: entrada/codVenda/cliente
        List entra;
        if (!dados.get("entradas").equals("")) {
            entra = (List) dados.get("entradas");
            this.entradas = new ArrayList();
            for (int i = 0; i < entra.size(); i++) {
                this.entradas.add(entra.get(i));

            }
        } else {
            this.entradas = null;
        }
        if (!dados.get("saidas").equals("")) {
            entra = (List) dados.get("saidas");
            this.saidas = new ArrayList();
            for (int i = 0; i < entra.size(); i++) {
                this.saidas.add(entra.get(i));

            }
        } else {
            this.saidas = null;
        }
        this.data = (String) dados.get("data");
        this.setCodigo(Integer.parseInt((String) dados.get("cod")));
        double ttl = 0.00;
        if (entradas != null) {
            for (int i = 0; i < entradas.size(); i++) {
                String aux = ((String) entradas.get(i)).split("#")[0];
                ttl += Double.parseDouble(aux);
            }
        }
        this.valorTotalEntrada = ttl;
        ttl = 0.00;
        if (saidas != null) {
            for (int i = 0; i < saidas.size(); i++) {
                String aux = ((String) saidas.get(i)).split("#")[0];
                ttl += Double.parseDouble(aux);
            }
        }
        this.valorTotalSaida = ttl;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List getEntradas() {
        return entradas;
    }

    public void setEntradas(List entradas) {
        this.entradas = entradas;
    }

    public List getSaidas() {
        return saidas;
    }

    public void setSaidas(List saidas) {
        this.saidas = saidas;
    }

    public double getValorTotalEntrada() {
        return valorTotalEntrada;
    }

    public void setValorTotalEntrada(double valorTotalEntrada) {
        this.valorTotalEntrada = valorTotalEntrada;
    }

    public double getValorTotalSaida() {
        return valorTotalSaida;
    }

    public void setValorTotalSaida(double valorTotalSaida) {
        this.valorTotalSaida = valorTotalSaida;
    }

    public HashMap<String, Object> toHashMap() {
        //Exemplo entrada: valor#natureza#funcionario
        //Exemplo saida: valor#natureza#funcionario
        //Exemplo natureza: parcela#numero#cliente
        //Exemplo natureza: avista#cliente (cliente ou nao)
        HashMap<String, Object> retorno = new HashMap<String, Object>();
        retorno.put("cod", this.getCodigo() + "");
        if (this.getEntradas() == null) {
            retorno.put("entradas", "");
        } else {
            retorno.put("entradas", this.getEntradas());
        }
        if (this.getSaidas() == null) {
            retorno.put("saidas", "");
        } else {
            retorno.put("saidas", this.getSaidas());
        }
        retorno.put("data", this.getData() + "");
        retorno.put("valorTotalEntrada", this.getValorTotalEntrada() + "");
        retorno.put("valorTotalSaida", this.getValorTotalSaida() + "");
        return retorno;
    }
}
