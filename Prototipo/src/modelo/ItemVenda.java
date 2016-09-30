package modelo;

/**
 *
 * @author Hemerson
 */
public class ItemVenda {
    private int cod;
    private String descricao;
    private String preco;
    private int quantidade;

    public ItemVenda() {
        this.cod = 0;
        this.descricao = null;
        this.preco = null;
        this.quantidade = 0;
    }

    public ItemVenda(ItemVenda itemVenda) {
        this.cod = itemVenda.getCod();
        this.descricao = itemVenda.getDescricao();
        this.preco = itemVenda.getPreco();
        this.quantidade = itemVenda.getQuantidade();
    }

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        //exemplo: item#codigo#descricao#qtde#valorUnitario
        String str = ""+this.cod+"#"+this.descricao+"#"+this.quantidade+"#"+this.preco;
        return str;
    }
}
