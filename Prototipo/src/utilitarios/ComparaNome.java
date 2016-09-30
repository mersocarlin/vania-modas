package utilitarios;

/**
 *
 * @author Hemerson e Jefferson
 */
import java.util.Comparator;
import modelo.Caixa;
import modelo.Cliente;
import modelo.Funcionario;
import modelo.Produto;
import modelo.Venda;

public class ComparaNome implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        if (o1 instanceof Cliente) {
            Cliente c1 = (Cliente) o1;
            Cliente c2 = (Cliente) o2;
            if (c1.getPessoa().getNome().compareTo(c2.getPessoa().getNome()) == 0) {
                return c1.getCodigo() - (c2.getCodigo());
            } else {
                return c1.getPessoa().getNome().compareTo(c2.getPessoa().getNome());
            }
        } else {
            if (o1 instanceof Funcionario) {
                Funcionario f1 = (Funcionario) o1;
                Funcionario f2 = (Funcionario) o2;
                if (f1.getPessoa().getNome().compareTo(f2.getPessoa().getNome()) == 0) {
                    return f1.getCodigo() - (f2.getCodigo());
                } else {
                    return f1.getPessoa().getNome().compareTo(f2.getPessoa().getNome());
                }
            } else {
                if (o1 instanceof Produto) {
                    Produto p1 = (Produto) o1;
                    Produto p2 = (Produto) o2;
                    if (p1.getDescricao().compareTo(p2.getDescricao()) == 0) {
                        return p1.getCodigo() - (p2.getCodigo());
                    } else {
                        return p1.getDescricao().compareTo(p2.getDescricao());
                    }
                } else {
                    if (o1 instanceof Venda) {
                        Venda v1 = (Venda) o1;
                        Venda v2 = (Venda) o2;
                        if (v1.getDataVenda().toString().compareTo(v2.getDataVenda().toString()) == 0) {
                            return v1.getCodigo() - (v2.getCodigo());
                        } else {
                            return v1.getDataVenda().toString().compareTo(v2.getDataVenda().toString());
                        }
                    } else {
                        if (o1 instanceof Caixa) {
                            Caixa v1 = (Caixa) o1;
                            Caixa v2 = (Caixa) o2;
                            if (v1.getData().toString().compareTo(v2.getData().toString()) == 0) {
                                return v1.getCodigo() - (v2.getCodigo());
                            } else {
                                return v1.getData().toString().compareTo(v2.getData().toString());
                            }
                        }
                    }
                }
            }
        }
        return 0;
    }
}
