
package modelo;

import java.util.HashMap;

/**
 *
 * @author Hemerson e Jefferson
 */
public class Fornecedor {

    private String nome;
    private String telefone;

    public Fornecedor() {
        this.nome = null;
        this.telefone = null;
    }

    public Fornecedor(Fornecedor fornecedor){
        this.nome = fornecedor.getNome();
        this.telefone = fornecedor.getTelefone();
    }
    public Fornecedor(HashMap<String, Object> fornecedor){
        this.nome = (String)fornecedor.get("nomeFornecedor");
        this.telefone = (String)fornecedor.get("contatoFornecedor");
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public HashMap<String, Object> toHashMap(){
        HashMap<String, Object> retorno = new HashMap<String, Object>();
        retorno.put("nome", this.getNome());
        retorno.put("contato",this.getTelefone());
        return retorno;
    }
}

