package controlador;

import java.util.HashMap;
import modelo.OperacoesModelo;

/**
 *
 * @author Hemerson e Jefferson
 */
public class Controlador {

    public HashMap<String, Object> recebeOperacao(HashMap<String, Object> dados) {
        OperacoesModelo operacao = new OperacoesModelo();
        return operacao.executar(dados);
    }
}
