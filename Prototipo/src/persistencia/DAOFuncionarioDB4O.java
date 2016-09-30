package persistencia;

/**
 *
 * @author Hemerson e Jefferson
 */
import com.db4o.ObjectSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import modelo.Funcionario;
import utilitarios.ComparaID;
import utilitarios.ComparaNome;

public class DAOFuncionarioDB4O implements DAO {

    PersistenciaArq persistencia_arq;
    String banco = "funcionario";

    @Override
    public String incluir(Object o) {
        if (this.localizar(((Funcionario) o).getCodigo()) == null) {
            this.persistencia_arq.db.set(o);
            return "Inserido com sucesso";
        }
        return "Elemento ja existe";
    }

    @Override
    public String excluir(int id) {
        persistencia_arq.setObjectSet(new Funcionario(id));
        this.persistencia_arq.db.delete(persistencia_arq.result.next());
        return "Removido com sucesso";
    }

    @Override
    public String excluir(Object o) {
        this.persistencia_arq.db.delete(o);
        return "Removido com sucesso";
    }

    @Override
    public String alterar(int id, Object o) {
        this.persistencia_arq.setObjectSet(new Funcionario(id));
        if (persistencia_arq.result.size() > 0) {
            this.excluir(persistencia_arq.result.next());
            this.incluir(o);
            persistencia_arq.db.set(o);
        }
        return "Funcionario alterado!";
    }

    @Override
    public Object localizar(String str, String campo) {
        if (campo.compareTo("nome") == 0) {
            this.persistencia_arq.setObjectSet(new Funcionario(str,"nome"));
            if (persistencia_arq.result.size() > 0) {
                return persistencia_arq.result.next();
            }
        } else {
            if (campo.compareTo("id") == 0) {
                return localizar(Integer.parseInt(str));
            } else {
                if (campo.compareTo("login") == 0) {
                    this.persistencia_arq.setObjectSet(new Funcionario(str,"login"));
                    if (persistencia_arq.result.size() > 0) {
                        return persistencia_arq.result.next();
                    }
                }
//                this.persistencia_arq.setObjectSet(new Funcionario(0, null, str));
//                    if(persistencia_arq.result != null){
//                        return persistencia_arq.result.next();
//                    }
//                qualqer campo

            }
        }
        return null;
    }

    @Override
    public Object localizar(int id) {
        try {
            this.persistencia_arq.setObjectSet(new Funcionario(id));
            if (persistencia_arq.result.size() > 0) {
                return persistencia_arq.result.next();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Object primeiro(boolean ordem) {
        List l = this.ordenaLista(ordem);
        if (l.size() > 0) {
            return l.get(0);
        }
        return null;
    }

    @Override
    public Object ultimo(boolean ordem) {
        List l = this.ordenaLista(ordem);
        if (l.size() > 0) {
            return l.get(l.size() - 1);
        }
        return null;
    }

    @Override
    public Object proximo(int id, boolean ordem) {
        List b = this.ordenaLista(ordem);
        if (b.size() > 0) {
            for (int i = 0; i < b.size(); i++) {
                if (((Funcionario) b.get(i)).getCodigo() == id) {
                    if (i == b.size() - 1) {
                        return b.get(0);
                    } else {
                        return b.get(i + 1);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public Object anterior(int id, boolean ordem) {
        List b = this.ordenaLista(ordem);
        if (b.size() > 0) {
            for (int i = 0; i < b.size(); i++) {
                if (((Funcionario) b.get(i)).getCodigo() == id) {
                    if (i == 0) {
                        return b.get(b.size() - 1);
                    } else {
                        return b.get(i - 1);
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String nroObjetos() {
        persistencia_arq.setObjectSet(new Funcionario(0));
        return "" + persistencia_arq.result.size();
    }

    @Override
    public DAO conectar() {
        try {
            this.persistencia_arq = new PersistenciaArq(banco);
        } finally {
            return this;
        }
    }

    @Override
    public String desconectar() {
        try {
            this.persistencia_arq.db.close();
        } catch (Exception e) {
            return "Impossivel salver objetos";
        }
        return null;
    }

    @Override
    public String maiorId() {
        persistencia_arq.setObjectSet(new Funcionario(0));
        if (persistencia_arq.result.size() > 0) {
            int j = ((Funcionario) persistencia_arq.result.next()).getCodigo();
            for (int i = 0; i < persistencia_arq.result.size(); i++) {
                if (j < ((Funcionario) persistencia_arq.result.get(i)).getCodigo()) {
                    j = ((Funcionario) persistencia_arq.result.get(i)).getCodigo();
                }
            }
            j++;
            return "" + j;
        }
        return "1";
    }

    @Override
    public String limpa() {
        persistencia_arq.limpar(banco);
        return "Limpo com sucesso";
    }

    @Override
    public List ordenaLista(boolean ordem) {
        List lista = new ArrayList();
        Funcionario proto = new Funcionario(0);
        ObjectSet result = persistencia_arq.db.get(proto);
        if (result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                Funcionario p = (Funcionario) result.next();
                lista.add(p);
            }
        }
        if (ordem) { //ordena por codigo
            Collections.sort(lista, new ComparaID());
        } else { //ordena por nome
            Collections.sort(lista, new ComparaNome());
        }
        return lista;
    }
}
