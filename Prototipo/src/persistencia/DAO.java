
package persistencia;

/**
 *
 * @author Hemerson e Jefferson
 */
import java.util.List;

public interface DAO {
    public String incluir(Object o);
    public String excluir(int id);
    public String excluir(Object o);
    public String alterar(int id, Object o);
    public Object localizar(String str, String campo);
    public Object localizar(int id);
    public Object primeiro(boolean ordem);
    public Object ultimo(boolean ordem);
    public Object proximo(int id, boolean ordem);
    public Object anterior(int id, boolean ordem);
    public String nroObjetos();
    public DAO conectar();
    public String desconectar();
    public String maiorId();
    public String limpa();
    public List ordenaLista(boolean ordem);
}
