package utilitarios;

/**
 *
 * @author Hemerson e Jefferson
 */
import java.util.Comparator;
import modelo.Cliente;
import modelo.Funcionario;
import modelo.Produto;
import modelo.Venda;

public class ComparaID implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        if (o1 instanceof Cliente) {
            Cliente c1 = (Cliente) o1;
            Cliente c2 = (Cliente) o2;
            return (c1.getCodigo() - c2.getCodigo());
        } else {
            if (o1 instanceof Funcionario) {
                Funcionario f1 = (Funcionario) o1;
                Funcionario f2 = (Funcionario) o2;
                return (f1.getCodigo() - f2.getCodigo());
            } else {
                if (o1 instanceof Produto) {
                    Produto p1 = (Produto) o1;
                    Produto p2 = (Produto) o2;
                    return (p1.getCodigo() - p2.getCodigo());
                } else {
                    if (o1 instanceof Venda) {
                        Venda v1 = (Venda) o1;
                        Venda v2 = (Venda) o2;
                        return (v1.getCodigo() - v2.getCodigo());
                    }
                }
            }
        }
        return 0;
    }
}
