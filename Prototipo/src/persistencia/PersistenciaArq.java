package persistencia;

/**
 *
 * @author Hemerson e Jefferson
 */
import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import java.io.File;

public class PersistenciaArq {

    ObjectContainer db;
    ObjectSet result;

    public PersistenciaArq(String banco) {
        db = Db4o.openFile("arquivos/bd/" + banco + ".db");
        result = null;
    }

    public void setObjectSet(modelo.Cliente cliente) {
        result = db.get(cliente);
    }

    public void setObjectSet(modelo.Produto produto) {
        result = db.get(produto);
    }

    public void setObjectSet(modelo.Funcionario funcionario) {
        result = db.get(funcionario);
    }

    public void setObjectSet(modelo.Venda venda) {
        result = db.get(venda);
    }

    public void setObjectSet(modelo.Caixa caixa) {
        result = db.get(caixa);
    }

    public void limpar(String banco) {
        db.close();
        File f = new File("arquivos/bd/" + banco + ".db");
        f.delete();
        db = Db4o.openFile("arquivos/bd/" + banco + ".db");
    }
}
