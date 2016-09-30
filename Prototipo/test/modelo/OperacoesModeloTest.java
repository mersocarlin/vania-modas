package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import junit.framework.TestCase;
import persistencia.DAO;
import persistencia.DAOFuncionarioDB4O;
import persistencia.DAOProdutoDB4O;
import persistencia.DAOVendaDB4O;
import utilitarios.Sessao;

/**
 *
 * @author Hemerson e Jefferson
 */
public class OperacoesModeloTest extends TestCase {

    DAO dao;

    public OperacoesModeloTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of executar method, of class OperacoesModelo.
     */
    public void testExecutar() {
        System.out.println("executar");
        HashMap<String, Object> dados = null;
        OperacoesModelo instance = new OperacoesModelo();
        HashMap expResult = null;
        HashMap result = instance.executar(dados);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

        //fail("The test case is a prototype.");
    }

    public void testExecutar2() {
        System.out.println("executar");
        HashMap<String, Object> dados = new HashMap<String, Object>();
        dados.put("modelo", "fornecedor");
        OperacoesModelo instance = new OperacoesModelo();
        HashMap expResult = null;
        HashMap result = instance.executar(dados);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

        //fail("The test case is a prototype.");
    }

    public void testExecutar3() {
        System.out.println("executar");
        HashMap<String, Object> dados = new HashMap<String, Object>();
        dados.put("modelo", "fornecedor2");
        OperacoesModelo instance = new OperacoesModelo();
        HashMap expResult = null;
        HashMap result = instance.executar(dados);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.

        //fail("The test case is a prototype.");
    }

    /**
     * Test of incluirFuncionario method, of class OperacoesModelo.
     */
    public void testIncluirFuncionario() {
        System.out.println("incluirFuncionario");
        HashMap<String, Object> dados = new HashMap<String, Object>();
        dao = new DAOFuncionarioDB4O();
        //dados.put("cod", this.maiorID() + "");
        String nome = "JOAO BATISTA DA SILVA";
        dados.put("nome", nome);
        dados.put("cpf", "051.887.609-83");
        dados.put("dataCadastro", "");
        dados.put("dataNasc", "20/03/1983");
        dados.put("rg", "345");
        dados.put("sexo", "Masculino");
        dados.put("telefone", "(45)3333-3333");
        dados.put("celular", "(45)3333-3333");
        dados.put("estadoCivil", "solteiro");
        dados.put("email", "email@email.com.br");
        dados.put("tipoLogradouro", "Rua");
        dados.put("nomeLogradouro", "NOME DA RUA");
        dados.put("numLogradouro", "0");
        dados.put("complemento", "");
        dados.put("bairro", "NOME DO BAIRRO");
        dados.put("estado", "PR");
        dados.put("cidade", "Cascavel");
        dados.put("cep", "88.888-888");
        dados.put("propria", "true");
        dados.put("tempoResidencia", "5 ANOS");
        dados.put("nomeConjuge", "NOME DO CONJUGE");
        dados.put("nascConjuge", "15/11/1980");
        dados.put("rgConjuge", "456");
        dados.put("cpfConjuge", "051.887.609-83");
        dados.put("sexoConjuge", "Feminino");
        dados.put("celularConjuge", "(45)3333-3333");
        dados.put("funcao", "FUNCAO");
        dados.put("salario", "1500.00");
        dados.put("dataAdmissao", "20/10/2009");
        dados.put("refCom1", "COMERCIAL 1" + "#" + "(45)3333-3333");
        dados.put("refCom2", "COMERCIAL 2" + "#" + "(45)3333-3333");
        dados.put("refCom3", "COMERCIAL 3" + "#" + "(45)3333-3333");
        String login = "";
        int tamanho = nome.split(" ").length;
        for (int j = 0; j < tamanho - 1; j++) {
            String aux = nome.split(" ")[j];
            if (aux.length() > 2) {
                login += aux.charAt(0);
            }
        }
        login += nome.split(" ")[tamanho - 1];
        dados.put("login", login.toLowerCase());
        Sessao s = new Sessao();
        s.setSenha("123");//criptografia
        dados.put("senha", s.getSenhaCriptografada());
        dados.put("vendas", "");
        dados.put("ativo", "true");
        dados.put("gerente", "true");
        OperacoesModelo instance = new OperacoesModelo();
        String expResult = "Falha ao inserir Funcionario. Contate o administrador.";
        String result = instance.incluirFuncionario(dados);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    public void testIncluirFuncionario2() {
        System.out.println("incluirFuncionario");
        HashMap<String, Object> dados = new HashMap<String, Object>();
        OperacoesModelo instance = new OperacoesModelo();
        dao = new DAOFuncionarioDB4O();
        dados.put("cod", this.maiorID() + "");
        String nome = "JOAO BATISTA DA SILVA";
        dados.put("nome", nome);
        dados.put("cpf", "051.887.609-83");
        dados.put("dataCadastro", "00/00/0000");
        dados.put("dataNasc", "20/03/1983");
        dados.put("rg", "345");
        dados.put("sexo", "Masculino");
        dados.put("telefone", "(45)3333-3333");
        dados.put("celular", "(45)3333-3333");
        dados.put("estadoCivil", "solteiro");
        dados.put("email", "email@email.com.br");
        dados.put("tipoLogradouro", "Rua");
        dados.put("nomeLogradouro", "NOME DA RUA");
        dados.put("numLogradouro", "0");
        dados.put("complemento", "");
        dados.put("bairro", "NOME DO BAIRRO");
        dados.put("estado", "PR");
        dados.put("cidade", "Cascavel");
        dados.put("cep", "88.888-888");
        dados.put("propria", "true");
        dados.put("tempoResidencia", "5 ANOS");
        dados.put("nomeConjuge", "NOME DO CONJUGE");
        dados.put("nascConjuge", "15/11/1980");
        dados.put("rgConjuge", "456");
        dados.put("cpfConjuge", "051.887.609-83");
        dados.put("sexoConjuge", "Feminino");
        dados.put("celularConjuge", "(45)3333-3333");
        dados.put("funcao", "FUNCAO");
        dados.put("salario", "1500.00");
        dados.put("dataAdmissao", "20/10/2009");
        dados.put("refCom1", "COMERCIAL 1" + "#" + "(45)3333-3333");
        dados.put("refCom2", "COMERCIAL 2" + "#" + "(45)3333-3333");
        dados.put("refCom3", "COMERCIAL 3" + "#" + "(45)3333-3333");
        String login = "";
        int tamanho = nome.split(" ").length;
        for (int j = 0; j < tamanho - 1; j++) {
            String aux = nome.split(" ")[j];
            if (aux.length() > 2) {
                login += aux.charAt(0);
            }
        }
        login += nome.split(" ")[tamanho - 1];
        dados.put("login", login.toLowerCase());
        Sessao s = new Sessao();
        s.setSenha("123");//criptografia
        dados.put("senha", s.getSenhaCriptografada());
        dados.put("vendas", "");
        dados.put("ativo", "true");
        dados.put("gerente", "true");

        String expResult = "Funcionario inserido com sucesso.";
        String result = instance.incluirFuncionario(dados);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of incluirProduto method, of class OperacoesModelo.
     */
    public void testIncluirProduto() {
        System.out.println("incluirProduto");
        HashMap<String, Object> dados = new HashMap<String, Object>();
        dados.put("cod", "");
        dados.put("dataTermino", "");
        dados.put("desc", "");
        dados.put("qtdeEntrada", "");
        dados.put("valorVenda", "");
        dados.put("valorCompra", "");
        dados.put("nomeFornecedor", "");
        dados.put("contatoFornecedor", "(00)0000-0000");
        dados.put("dataCadastro", "");
        dados.put("dataEntrada", "");
        dados.put("qtdeVendida", "0");
        dados.put("ativo", "true");
        OperacoesModelo instance = new OperacoesModelo();
        String expResult = "Falha ao inserir produto. Contate o administrador.";
        String result = instance.incluirProduto(dados);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    public void testIncluirProduto2() {
        System.out.println("incluirProduto");
        HashMap<String, Object> dados = new HashMap<String, Object>();
        OperacoesModelo instance = new OperacoesModelo();
        dao = new DAOProdutoDB4O();
        dados.put("cod", "" + this.maiorID());
        dados.put("dataTermino", "");
        dados.put("desc", "MEIA FINA");
        dados.put("qtdeEntrada", "10");
        dados.put("valorVenda", "10.00");
        dados.put("valorCompra", "");
        dados.put("nomeFornecedor", "");
        dados.put("contatoFornecedor", "(00)0000-0000");
        dados.put("dataCadastro", "00/00/0000");
        dados.put("dataEntrada", "");
        dados.put("qtdeVendida", "0");
        dados.put("ativo", "true");
        String expResult = "Produto inserido com sucesso.";
        String result = instance.incluirProduto(dados);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of incluirVenda method, of class OperacoesModelo.
     */
    public void testIncluirVenda() {
        System.out.println("incluirVenda");
        HashMap<String, Object> dados = null;
        OperacoesModelo instance = new OperacoesModelo();
        String expResult = "Falha ao inserir venda. Contate o administrador.";
        String result = instance.incluirVenda(dados);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    public void testIncluirVenda2() {
        System.out.println("incluirVendavista");
        HashMap<String, Object> dados = new HashMap<String, Object>();
        OperacoesModelo instance = new OperacoesModelo();
        dao = new DAOVendaDB4O();
        dados.put("cod", this.maiorID()+"");
        dados.put("codCliente", "1");
        dados.put("codFuncionario", "1");
        dados.put("avista", "true");
        //dados.put("avista", "false");
        dados.put("dataVenda", "00/00/0000");

        List lista = new ArrayList();
        //exemplo: item#codigo#descricao#qtde#valorUnitario
        String str = "1#MEIA#2#10.00";
        lista.add(str);
        dados.put("itensVenda", lista);
        dados.put("subTotal", "20.00");
        dados.put("desconto", "");
        dados.put("valorTotal", "20.00");
        dados.put("valorEntrada", "0.0");
        dados.put("valorRestante", "0.0");
        dados.put("valorEntrada", "0.00");
        double vTotal = Double.parseDouble((String) dados.get("valorTotal"));
        double conta = Double.parseDouble((String) dados.get("valorEntrada"));
        conta = vTotal - conta;
        if (conta <= 0.0) {
            conta = 0.0;
        }
        dados.put("valorRestante", conta + "");
        //lista = new ArrayList();
        //exemplo: dataVencimento#valor#datapagamento#situacao#funcionarioRecebeu
        //str = "00/00/0000#10.00#00/00/0000#false##";//funcionarioRecebeu
        //lista.add(str);
        dados.put("parcelas", new ArrayList());
        String expResult = "Venda inserida com sucesso.";
        String result = instance.incluirVenda(dados);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    public void testIncluirVenda3() {
        System.out.println("incluirVendaAprazo");
        HashMap<String, Object> dados = new HashMap<String, Object>();
        OperacoesModelo instance = new OperacoesModelo();
        dao = new DAOVendaDB4O();
        dados.put("cod", this.maiorID()+"");
        dados.put("codCliente", "1");
        dados.put("codFuncionario", "1");
        //dados.put("avista", "true");
        dados.put("avista", "false");
        dados.put("dataVenda", "00/00/0000");

        List lista = new ArrayList();
        //exemplo: item#codigo#descricao#qtde#valorUnitario
        String str = "1#MEIA#2#10.00";
        lista.add(str);
        dados.put("itensVenda", lista);
        dados.put("subTotal", "20.00");
        dados.put("desconto", "");
        dados.put("valorTotal", "20.00");
        dados.put("valorEntrada", "5.00");
        dados.put("valorRestante", "0.0");
        dados.put("valorEntrada", "0.00");
        double vTotal = Double.parseDouble((String) dados.get("valorTotal"));
        double conta = Double.parseDouble((String) dados.get("valorEntrada"));
        conta = vTotal - conta;
        if (conta <= 0.0) {
            conta = 0.0;
        }
        dados.put("valorRestante", conta + "");
        lista = new ArrayList();
        //exemplo: dataVencimento#valor#datapagamento#situacao#funcionarioRecebeu
        str = "00/00/0000#10.00#00/00/0000#false# #";//funcionarioRecebeu
        lista.add(str);
        dados.put("parcelas", lista);
        String expResult = "Venda inserida com sucesso.";
        String result = instance.incluirVenda(dados);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    private String maiorID() {
        String str = "";
        dao.conectar();
        str = dao.maiorId() + "";
        dao.desconectar();
        return str;
    }
}
