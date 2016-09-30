
package modelo;

/**
 *
 * @author Hemerson e Jefferson
 */
public class Parcela {
    private String valor;
    private Data dataVencimento;
    private boolean pago;
    private Data dataPagamento;
    private String loginFuncionario;

    public Parcela(){
        this.valor = "";
        this.dataPagamento = new Data();
        this.dataVencimento = new Data();
        this.pago = false;
        loginFuncionario = "";
    }

    public Parcela(Parcela parcela){
        this.valor = parcela.getValor();
        this.dataPagamento = new Data(parcela.getDataPagamento());
        this.dataVencimento = new Data(parcela.getDataVencimento());
        this.pago = false;
        this.loginFuncionario = parcela.getLoginFuncionario();
    }

    public String getLoginFuncionario() {
        return loginFuncionario;
    }

    public void setLoginFuncionario(String funcionario) {
        this.loginFuncionario = funcionario;
    }

    public Data getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Data dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public Data getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Data dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public boolean isPago() {
        return pago;
    }

    public void setPago(boolean pago) {
        this.pago = pago;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        //exemplo: dataVencimento#valor#datapagamento#situacao#funcionarioRecebeu
        if(this.pago){
            return this.dataVencimento.toString()+"#"+this.valor+"#"+this.dataPagamento.toString()+"#"+"true"+"#"+this.getLoginFuncionario();
        }else{
            return this.dataVencimento.toString()+"#"+this.valor+"#"+this.dataPagamento.toString()+"#"+"false"+"#"+this.getLoginFuncionario();
        }
    }
}
