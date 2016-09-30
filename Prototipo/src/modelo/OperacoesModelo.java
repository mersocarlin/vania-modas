package modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import persistencia.DAO;
import persistencia.DAOCaixaDB4O;
import persistencia.DAOClienteDB4O;
import persistencia.DAOFuncionarioDB4O;
import persistencia.DAOProdutoDB4O;
import persistencia.DAOVendaDB4O;

/**
 *
 * @author Hemerson e Jefferson
 */
public class OperacoesModelo {

    DAO dao;

    public HashMap<String, Object> executar(HashMap<String, Object> dados) {
        HashMap<String, Object> retorno = new HashMap<String, Object>();
        if (dados != null) {
            try {
                if (dados.get("modelo").equals("cliente")) {
                    if (dados.get("operacao").equals("inserir")) {
                        retorno.put("retorno", "" + this.incluirCliente(dados));
                        return retorno;
                    } else {
                        if (dados.get("operacao").equals("consultar")) {
                            return this.buscarClienteNome(dados);
                        } else {
                            if (dados.get("operacao").equals("maiorId")) {
                                dao = new DAOClienteDB4O();
                                retorno.put("retorno", "" + this.maiorID());
                                return retorno;
                            } else {
                                if (dados.get("operacao").equals("excluir")) {
                                    dao = new DAOClienteDB4O();
                                    retorno.put("retorno", "" + this.excluir((String) dados.get("cod")));
                                    return retorno;
                                } else {
                                    if (dados.get("operacao").equals("busca")) {
                                        if (dados.get("posicao").equals("primeiro")) {
                                            retorno = this.primeiroCliente(dados);
                                            retorno.put("retorno", "sucesso");
                                            return retorno;
                                        } else {
                                            if (dados.get("posicao").equals("proximo")) {
                                                retorno = this.proximoCliente(dados);
                                                retorno.put("retorno", "sucesso");
                                                return retorno;
                                            } else {
                                                if (dados.get("posicao").equals("anterior")) {
                                                    retorno = this.anteriorCliente(dados);
                                                    retorno.put("retorno", "sucesso");
                                                    return retorno;
                                                } else {
                                                    if (dados.get("posicao").equals("ultimo")) {
                                                        retorno = this.ultimoCliente(dados);
                                                        retorno.put("retorno", "sucesso");
                                                        return retorno;
                                                    } else {
                                                        if (dados.get("posicao").equals("cod")) {
                                                            retorno = this.consultarClienteCod(dados);
                                                            return retorno;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    } else {
                                        if (dados.get("operacao").equals("ordenaLista")) {
                                            retorno = this.ordenaListaCliente(dados);
                                            retorno.put("retorno", "sucesso");
                                            return retorno;
                                        } else {
                                            if (dados.get("operacao").equals("alterar")) {
                                                retorno.put("retorno", this.alterarCliente(dados));
                                                return retorno;

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (((String) dados.get("modelo")).equals("funcionario")) {
                        if (dados.get("operacao").equals("inserir")) {
                            retorno.put("retorno", "" + this.incluirFuncionario(dados));
                            return retorno;
                        } else {

                            if (dados.get("operacao").equals("consultar")) {
                                return this.buscarFuncionarioNome(dados);
                            } else {
                                if (dados.get("operacao").equals("maiorId")) {
                                    dao = new DAOFuncionarioDB4O();
                                    retorno.put("retorno", "" + this.maiorID());
                                    return retorno;
                                } else {
                                    if (dados.get("operacao").equals("excluir")) {
                                        dao = new DAOFuncionarioDB4O();
                                        retorno.put("retorno", "" + this.excluir((String) dados.get("cod")));
                                        return retorno;
                                    } else {
                                        if (dados.get("operacao").equals("busca")) {
                                            if (dados.get("posicao").equals("primeiro")) {
                                                retorno = this.primeiroFuncionario(dados);
                                                retorno.put("retorno", "sucesso");
                                                return retorno;
                                            } else {
                                                if (dados.get("posicao").equals("proximo")) {
                                                    retorno = this.proximoFuncionario(dados);
                                                    retorno.put("retorno", "sucesso");
                                                    return retorno;
                                                } else {
                                                    if (dados.get("posicao").equals("anterior")) {
                                                        retorno = this.anteriorFuncionario(dados);
                                                        retorno.put("retorno", "sucesso");
                                                        return retorno;
                                                    } else {
                                                        if (dados.get("posicao").equals("ultimo")) {
                                                            retorno = this.ultimoFuncionario(dados);
                                                            retorno.put("retorno", "sucesso");
                                                            return retorno;
                                                        } else {
                                                            if (dados.get("posicao").equals("cod")) {
                                                                retorno = this.consultarFuncionarioCod(dados);
                                                                return retorno;

                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            if (dados.get("operacao").equals("ordenaLista")) {
                                                retorno = this.ordenaListaFuncionario(dados);
                                                retorno.put("retorno", "sucesso");
                                                return retorno;

                                            } else {
                                                if (dados.get("operacao").equals("alterar")) {
                                                    retorno.put("retorno", this.alterarFuncionario(dados));
                                                    return retorno;

                                                } else {
                                                    if (dados.get("operacao").equals("login")) {
                                                        return this.loginFuncionario(dados);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    } else {
                        if (((String) dados.get("modelo")).equals("produto")) {
                            if (dados.get("operacao").equals("inserir")) {
                                retorno.put("retorno", "" + this.incluirProduto(dados));
                                return retorno;
                            } else {
                                if (dados.get("operacao").equals("consultar")) {
                                    return this.buscarProdutoNome(dados);
                                } else {
                                    if (dados.get("operacao").equals("maiorId")) {
                                        dao = new DAOProdutoDB4O();
                                        retorno.put("retorno", "" + this.maiorID());
                                        return retorno;
                                    } else {
                                        if (dados.get("operacao").equals("excluir")) {
                                            dao = new DAOProdutoDB4O();
                                            retorno.put("retorno", "" + this.excluir((String) dados.get("cod")));
                                            return retorno;
                                        } else {
                                            if (dados.get("operacao").equals("busca")) {
                                                if (dados.get("posicao").equals("primeiro")) {
                                                    retorno = this.primeiroProduto(dados);
                                                    retorno.put("retorno", "sucesso");
                                                    return retorno;
                                                } else {
                                                    if (dados.get("posicao").equals("proximo")) {
                                                        retorno = this.proximoProduto(dados);
                                                        retorno.put("retorno", "sucesso");
                                                        return retorno;
                                                    } else {
                                                        if (dados.get("posicao").equals("anterior")) {
                                                            retorno = this.anteriorProduto(dados);
                                                            retorno.put("retorno", "sucesso");
                                                            return retorno;
                                                        } else {
                                                            if (dados.get("posicao").equals("ultimo")) {
                                                                retorno = this.ultimoProduto(dados);
                                                                retorno.put("retorno", "sucesso");
                                                                return retorno;
                                                            } else {
                                                                if (dados.get("posicao").equals("cod")) {
                                                                    retorno = this.consultarProdutoCod(dados);
                                                                    return retorno;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            } else {
                                                if (dados.get("operacao").equals("ordenaLista")) {
                                                    retorno = this.ordenaListaProduto(dados);
                                                    retorno.put("retorno", "sucesso");
                                                    return retorno;
                                                } else {
                                                    if (dados.get("operacao").equals("alterar")) {
                                                        retorno.put("retorno", this.alterarProduto(dados));
                                                        return retorno;

                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            if (((String) dados.get("modelo")).equals("venda")) {
                                if (dados.get("operacao").equals("inserir")) {
                                    retorno.put("retorno", "" + this.incluirVenda(dados));
                                    return retorno;
                                } else {
                                    if (dados.get("operacao").equals("consultar")) {
                                    } else {
                                        if (dados.get("operacao").equals("consultarID")) {
                                            dao = new DAOVendaDB4O();
                                            retorno.put("retorno", "" + this.buscarVendaID(dados));
                                            return retorno;
                                        } else {
                                            if (dados.get("operacao").equals("maiorId")) {
                                                dao = new DAOVendaDB4O();
                                                retorno.put("retorno", "" + this.maiorID());
                                                return retorno;
                                            } else {
                                                if (dados.get("operacao").equals("excluir")) {
                                                    dao = new DAOVendaDB4O();
                                                    retorno.put("retorno", "" + this.excluir((String) dados.get("cod")));
                                                    return retorno;
                                                } else {
                                                    if (dados.get("operacao").equals("busca")) {
                                                        if (dados.get("posicao").equals("primeiro")) {
                                                            retorno = this.primeiraVenda(dados);
                                                            retorno.put("retorno", "sucesso");
                                                            return retorno;
                                                        } else {
                                                            if (dados.get("posicao").equals("proximo")) {
                                                                retorno = this.proximaVenda(dados);
                                                                retorno.put("retorno", "sucesso");
                                                                return retorno;
                                                            } else {
                                                                if (dados.get("posicao").equals("anterior")) {
                                                                    retorno = this.anteriorVenda(dados);
                                                                    retorno.put("retorno", "sucesso");
                                                                    return retorno;
                                                                } else {
                                                                    if (dados.get("posicao").equals("ultimo")) {
                                                                        retorno = this.ultimaVenda(dados);
                                                                        retorno.put("retorno", "sucesso");
                                                                        return retorno;
                                                                    } else {
                                                                        if (dados.get("posicao").equals("cod")) {
                                                                            retorno = this.consultarVendaCod(dados);
                                                                            return retorno;
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    } else {
                                                        if (dados.get("operacao").equals("ordenaLista")) {
                                                            retorno = this.ordenaListaVenda(dados);
                                                            retorno.put("retorno", "sucesso");
                                                            return retorno;
                                                        } else {
                                                            if (dados.get("operacao").equals("alterar")) {
                                                                retorno.put("retorno", this.alterarVenda(dados));
                                                                return retorno;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                if (((String) dados.get("modelo")).equals("caixa")) {
                                    if (dados.get("operacao").equals("inserir")) {
                                        retorno.put("retorno", "" + this.incluirCaixa(dados));
                                        return retorno;
                                    } else {
                                        if (dados.get("operacao").equals("consultar")) {
                                            //
                                        } else {
                                            if (dados.get("operacao").equals("consultarID")) {
                                            } else {
                                                if (dados.get("operacao").equals("maiorId")) {
                                                    dao = new DAOCaixaDB4O();
                                                    retorno.put("retorno", "" + this.maiorID());
                                                    return retorno;
                                                } else {
                                                    if (dados.get("operacao").equals("excluir")) {
                                                        /*retorno.put("retorno", "" + this.excluir((String) dados.get("cod")));
                                                        return retorno;*/
                                                    } else {
                                                        if (dados.get("operacao").equals("busca")) {
                                                            if (dados.get("posicao").equals("primeiro")) {
                                                                retorno = this.primeiroCaixa(dados);
                                                                retorno.put("retorno", "sucesso");
                                                                return retorno;
                                                            } else {
                                                                if (dados.get("posicao").equals("proximo")) {
                                                                    retorno = this.proximoCaixa(dados);
                                                                    retorno.put("retorno", "sucesso");
                                                                    return retorno;
                                                                } else {
                                                                    if (dados.get("posicao").equals("anterior")) {
                                                                        retorno = this.anteriorCaixa(dados);
                                                                        retorno.put("retorno", "sucesso");
                                                                        return retorno;
                                                                    } else {
                                                                        if (dados.get("posicao").equals("ultimo")) {
                                                                            retorno = this.ultimoCaixa(dados);
                                                                            retorno.put("retorno", "sucesso");
                                                                            return retorno;
                                                                        } else {
                                                                            if (dados.get("posicao").equals("cod")) {
                                                                                retorno = this.consultarCaixaCod(dados);
                                                                                return retorno;
                                                                            } else {
                                                                                if (dados.get("posicao").equals("data")) {
                                                                                    retorno = this.consultarCaixaData(dados);
                                                                                    return retorno;
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            if (dados.get("operacao").equals("ordenaLista")) {
                                                                retorno = this.ordenaListaCaixa(dados);
                                                                retorno.put("retorno", "sucesso");
                                                                return retorno;
                                                            } else {
                                                                if (dados.get("operacao").equals("alterar")) {
                                                                    retorno.put("retorno", this.alterarCaixa(dados));
                                                                    return retorno;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    private String alterarCliente(HashMap<String, Object> dados) {
        dao = new DAOClienteDB4O();
        dao.conectar();
        int id = 0;
        Cliente cliente = new Cliente(dados);
        try {
            id = Integer.parseInt((String) dados.get("cod"));
        } catch (Exception ex) {
            id = 0;
        }
        String str = dao.alterar(id, cliente);
        dao.desconectar();
        return str;
    }

    private String alterarFuncionario(HashMap<String, Object> dados) {
        dao = new DAOFuncionarioDB4O();
        dao.conectar();
        int id = 0;
        Funcionario funcionario = new Funcionario(dados);
        try {
            id = Integer.parseInt((String) dados.get("cod"));
        } catch (Exception ex) {
            id = 0;
        }
        String str = dao.alterar(id, funcionario);
        dao.desconectar();
        return str;

    }

    private String alterarProduto(HashMap<String, Object> dados) {
        dao = new DAOProdutoDB4O();
        dao.conectar();
        int id = 0;
        Produto produto = new Produto(dados);
        try {
            id = Integer.parseInt((String) dados.get("cod"));
        } catch (Exception ex) {
            id = 0;
        }
        String str = dao.alterar(id, produto);
        dao.desconectar();
        return str;

    }

    private String alterarVenda(HashMap<String, Object> dados) {
        dao = new DAOVendaDB4O();
        dao.conectar();
        int id = 0;
        String str;
        Venda venda = new Venda(dados);
        try {
            id = Integer.parseInt((String) dados.get("cod"));
            str = dao.alterar(id, venda);
            dao.desconectar();

        } catch (Exception ex) {
            id = 0;
            str = "Erro: impossivel alterar venda";
        }
        return str;

    }

    private HashMap<String, Object> buscarVendaID(HashMap<String, Object> dados) {
        HashMap<String, Object> retorno = new HashMap<String, Object>();
        try {
            Venda venda;
            dao = new DAOVendaDB4O();
            dao.conectar();
            venda = (Venda) dao.localizar(Integer.parseInt((String) dados.get("cod")));
            dao.desconectar();
            if (venda != null) {
                retorno = venda.toHashMap();
                retorno.put("retorno", "sucesso");
                return retorno;
            } else {
                return null;
            }
        } catch (Exception es) {
            retorno.put("retorno", "Erro ao consultar venda. Contate o admnistrador.");
            return retorno;
        }
    }

    private HashMap<String, Object> anteriorCliente(HashMap<String, Object> dados) {
        dao = new DAOClienteDB4O();
        dao.conectar();
        Cliente cliente;
        int codigo = 0;
        try {
            codigo = Integer.parseInt((String) dados.get("cod"));
        } catch (Exception ex) {
            codigo = 0;
        }
        if (dados.get("classificacao").equals("ordem")) {
            cliente = (Cliente) dao.anterior(codigo, true);
        } else {
            cliente = (Cliente) dao.anterior(codigo, false);
        }

        dao.desconectar();
        HashMap<String, Object> retorno;
        if (cliente != null) {
            retorno = cliente.toHashMap();
        } else {
            retorno = null;
        }
        return retorno;
    }

    private HashMap<String, Object> anteriorFuncionario(HashMap<String, Object> dados) {
        dao = new DAOFuncionarioDB4O();
        dao.conectar();
        Funcionario funcionario;
        int codigo = 0;
        try {
            codigo = Integer.parseInt((String) dados.get("cod"));
        } catch (Exception ex) {
            codigo = 0;
        }
        if (dados.get("classificacao").equals("ordem")) {
            funcionario = (Funcionario) dao.anterior(codigo, true);
        } else {
            funcionario = (Funcionario) dao.anterior(codigo, false);
        }

        dao.desconectar();
        HashMap<String, Object> retorno;
        if (funcionario != null) {
            retorno = funcionario.toHashMap();
        } else {
            retorno = null;
        }
        return retorno;
    }

    private HashMap<String, Object> anteriorProduto(HashMap<String, Object> dados) {
        dao = new DAOProdutoDB4O();
        dao.conectar();
        Produto produto;
        int codigo = 0;
        try {
            codigo = Integer.parseInt((String) dados.get("cod"));
        } catch (Exception ex) {
            codigo = 0;
        }
        if (dados.get("classificacao").equals("ordem")) {
            produto = (Produto) dao.anterior(codigo, true);
        } else {
            produto = (Produto) dao.anterior(codigo, false);
        }

        dao.desconectar();
        HashMap<String, Object> retorno;
        if (produto != null) {
            retorno = produto.toHashMap();
        } else {
            retorno = null;
        }
        return retorno;
    }

    private HashMap<String, Object> anteriorVenda(HashMap<String, Object> dados) {
        dao = new DAOVendaDB4O();
        dao.conectar();
        Venda venda;
        int codigo = 0;
        try {
            codigo = Integer.parseInt((String) dados.get("cod"));
        } catch (Exception ex) {
            codigo = 0;
        }
        if (dados.get("classificacao").equals("ordem")) {
            venda = (Venda) dao.anterior(codigo, true);
        } else {
            venda = (Venda) dao.anterior(codigo, false);
        }

        dao.desconectar();
        HashMap<String, Object> retorno;
        if (venda != null) {
            retorno = venda.toHashMap();
        } else {
            retorno = null;
        }
        return retorno;
    }

    private HashMap<String, Object> buscarFuncionarioNome(HashMap<String, Object> dados) {
        HashMap<String, Object> retorno = new HashMap<String, Object>();
        try {
            Funcionario funcionario;
            dao = new DAOFuncionarioDB4O();
            dao.conectar();
            funcionario = (Funcionario) dao.localizar((String) dados.get("nome"), "nome");
            dao.desconectar();
            if (funcionario != null) {
                retorno = funcionario.toHashMap();
                retorno.put("retorno", "sucesso");
                return retorno;
            } else {
                return null;
            }

        } catch (Exception es) {
            retorno.put("retorno", "Erro ao consultar funcionario. Contate o admnistrador.");
            return retorno;
        }
    }

    private HashMap<String, Object> buscarProdutoNome(HashMap<String, Object> dados) {
        HashMap<String, Object> retorno = new HashMap<String, Object>();
        try {
            Produto produto;
            dao = new DAOProdutoDB4O();
            dao.conectar();
            produto = (Produto) dao.localizar((String) dados.get("desc"), "desc");
            dao.desconectar();
            if (produto != null) {
                retorno = produto.toHashMap();
                retorno.put("retorno", "sucesso");
                return retorno;
            } else {
                return null;
            }

        } catch (Exception es) {
            retorno.put("retorno", "Erro ao consultar produto. Contate o admnistrador.");
            return retorno;
        }
    }

    private HashMap<String, Object> consultarClienteCod(HashMap<String, Object> dados) {
        Cliente cliente;
        List l = new ArrayList();
        HashMap<String, Object> retorno = new HashMap<String, Object>();
        try {
            int e = Integer.parseInt((String) dados.get("cod"));
            dao = new DAOClienteDB4O();
            dao.conectar();
            cliente = (Cliente) dao.localizar(e);
            dao.desconectar();
            retorno = cliente.toHashMap();
            l.add(retorno);
            retorno.put("lista", l);
            retorno.put("retorno", "sucesso");
            return retorno;
        } catch (Exception ex) {
            retorno.put("lista", null);
            retorno.put("retorno", "Erro impossivel consultar cliente.");
            return retorno;
        }
    }

    private HashMap<String, Object> consultarFuncionarioCod(HashMap<String, Object> dados) {
        Funcionario funcionario;
        List l = new ArrayList();
        HashMap<String, Object> retorno = new HashMap<String, Object>();
        try {
            int e = Integer.parseInt((String) dados.get("cod"));
            dao = new DAOFuncionarioDB4O();
            dao.conectar();
            funcionario = (Funcionario) dao.localizar(e);
            dao.desconectar();
            retorno = funcionario.toHashMap();
            l.add(retorno);
            retorno.put("lista", l);
            retorno.put("retorno", "sucesso");
            return retorno;
        } catch (Exception ex) {
            retorno.put("lista", null);
            retorno.put("retorno", "Erro impossivel consultar funcionario.");
            return retorno;
        }
    }

    private HashMap<String, Object> consultarProdutoCod(HashMap<String, Object> dados) {
        Produto produto;
        List l = new ArrayList();
        HashMap<String, Object> retorno = new HashMap<String, Object>();
        try {
            int e = Integer.parseInt((String) dados.get("cod"));
            dao = new DAOProdutoDB4O();
            dao.conectar();
            produto = (Produto) dao.localizar(e);
            dao.desconectar();
            retorno = produto.toHashMap();
            l.add(retorno);
            retorno.put("lista", l);
            retorno.put("retorno", "sucesso");
            return retorno;
        } catch (Exception ex) {
            retorno.put("lista", null);
            retorno.put("retorno", "Erro impossivel consultar produto.");
            return retorno;
        }
    }

    private HashMap<String, Object> consultarVendaCod(HashMap<String, Object> dados) {
        Venda venda;
        List l = new ArrayList();
        HashMap<String, Object> retorno = new HashMap<String, Object>();
        try {
            int e = Integer.parseInt((String) dados.get("cod"));
            dao = new DAOVendaDB4O();
            dao.conectar();
            venda = (Venda) dao.localizar(e);
            dao.desconectar();
            retorno = venda.toHashMap();
            l.add(retorno);
            retorno.put("lista", l);
            retorno.put("retorno", "sucesso");
            return retorno;
        } catch (Exception ex) {
            retorno.put("lista", null);
            retorno.put("retorno", "Erro impossivel consultar venda.");
            return retorno;
        }
    }

    private HashMap<String, Object> ordenaListaCliente(HashMap<String, Object> dados) {
        List l = new ArrayList();
        List m = new ArrayList();
        HashMap<String, Object> retorno = new HashMap<String, Object>();
        dao = new DAOClienteDB4O();
        dao.conectar();
        if (((String) dados.get("classificacao")).equals("ordem")) {
            l = dao.ordenaLista(false);
        } else {
            l = dao.ordenaLista(true);
        }
        dao.desconectar();
        for (int i = 0; i < l.size(); i++) {
            retorno = ((Cliente) l.get(i)).toHashMap();
            m.add(retorno);
        }
        retorno.put("lista", m);
        return retorno;
    }

    private HashMap<String, Object> ordenaListaFuncionario(HashMap<String, Object> dados) {
        List l = new ArrayList();
        List m = new ArrayList();
        HashMap<String, Object> retorno = new HashMap<String, Object>();
        dao = new DAOFuncionarioDB4O();
        dao.conectar();
        if (((String) dados.get("classificacao")).equals("ordem")) {
            l = dao.ordenaLista(false);
        } else {
            l = dao.ordenaLista(true);
        }
        dao.desconectar();
        for (int i = 0; i < l.size(); i++) {
            retorno = ((Funcionario) l.get(i)).toHashMap();
            m.add(retorno);
        }
        retorno.put("lista", m);
        return retorno;
    }

    private HashMap<String, Object> ordenaListaProduto(HashMap<String, Object> dados) {
        List l = new ArrayList();
        List m = new ArrayList();
        HashMap<String, Object> retorno = new HashMap<String, Object>();
        dao = new DAOProdutoDB4O();
        dao.conectar();
        if (((String) dados.get("classificacao")).equals("ordem")) {
            l = dao.ordenaLista(false);
        } else {
            l = dao.ordenaLista(true);
        }
        dao.desconectar();
        for (int i = 0; i < l.size(); i++) {
            retorno = ((Produto) l.get(i)).toHashMap();
            m.add(retorno);
        }
        retorno.put("lista", m);
        return retorno;
    }

    private HashMap<String, Object> ordenaListaVenda(HashMap<String, Object> dados) {
        List l = new ArrayList();
        List m = new ArrayList();
        HashMap<String, Object> retorno = new HashMap<String, Object>();
        dao = new DAOVendaDB4O();
        dao.conectar();
        if (((String) dados.get("classificacao")).equals("ordem")) {
            l = dao.ordenaLista(false);
        } else {
            l = dao.ordenaLista(true);
        }
        dao.desconectar();
        for (int i = 0; i < l.size(); i++) {
            retorno = ((Venda) l.get(i)).toHashMap();
            m.add(retorno);
        }
        retorno.put("lista", m);
        return retorno;
    }

    private String excluir(String cod) {
        String str = "";
        try {
            int e = Integer.parseInt(cod);
            dao.conectar();
            str = dao.excluir(e);
            dao.desconectar();
            return str;
        } catch (Exception ex) {
            return "Erro impossivel excluir.";
        }

    }

    private String incluirCliente(HashMap<String, Object> dados) {
        String str;
        try {
            Cliente cliente = new Cliente(dados);
            dao = new DAOClienteDB4O();
            dao.conectar();
            dao.incluir(cliente);
            System.out.println("num clientes inseridos:" + dao.nroObjetos());
            dao.desconectar();
            str = "Cliente inserido com sucesso.";
        } catch (Exception ex) {
            str = "Falha ao inserir cliente. Contate o administrador.";
        }

        return str;
    }

    public String incluirFuncionario(HashMap<String, Object> dados) {
        String str;
        try {
            Funcionario funcionario = new Funcionario(dados);
            dao = new DAOFuncionarioDB4O();
            dao.conectar();
            dao.incluir(funcionario);
            System.out.println("num funcionarios inseridos:" + dao.nroObjetos());
            dao.desconectar();
            str = "Funcionario inserido com sucesso.";
        } catch (Exception ex) {
            str = "Falha ao inserir Funcionario. Contate o administrador.";
        }
        return str;
    }

    public String incluirProduto(HashMap<String, Object> dados) {
        String str;
        try {
            Produto produto = new Produto(dados);
            dao = new DAOProdutoDB4O();
            dao.conectar();
            dao.incluir(produto);
            dao.desconectar();
            str = "Produto inserido com sucesso.";
        } catch (Exception ex) {
            str = "Falha ao inserir produto. Contate o administrador.";
        }

        return str;
    }

    public String incluirVenda(HashMap<String, Object> dados) {
        String str;
        try {
            Venda venda = new Venda(dados);
            dao = new DAOVendaDB4O();
            dao.conectar();
            dao.incluir(venda);
            dao.desconectar();
            str = "Venda inserida com sucesso.";
        } catch (Exception ex) {
            str = "Falha ao inserir venda. Contate o administrador.";
        }
        return str;
    }

    /**
     * Consulta cliente pelo nome
     * @param dados
     * @return HashMap com os dados do cliente consultado
     */
    private HashMap<String, Object> buscarClienteNome(HashMap<String, Object> dados) {
        HashMap<String, Object> retorno = new HashMap<String, Object>();
        try {
            Cliente cliente;
            dao = new DAOClienteDB4O();
            dao.conectar();
            cliente = (Cliente) dao.localizar((String) dados.get("nome"), "nome");
            dao.desconectar();
            if (cliente != null) {
                retorno = cliente.toHashMap();
                retorno.put("retorno", "sucesso");
                return retorno;
            } else {
                return null;
            }

        } catch (Exception es) {
            retorno.put("retorno", "Erro ao consultar cliente. Contate o admnistrador.");
            return retorno;
        }

    }

    private String maiorID() {
        String str = "";
        dao.conectar();
        str = dao.maiorId() + "";
        dao.desconectar();
        return str;
    }

    private HashMap<String, Object> primeiroCliente(HashMap<String, Object> dados) {
        dao = new DAOClienteDB4O();
        dao.conectar();
        Cliente cliente;
        if (dados.get("classificacao").equals("ordem")) {
            cliente = (Cliente) dao.primeiro(true);
        } else {
            cliente = (Cliente) dao.primeiro(false);
        }

        dao.desconectar();
        HashMap<String, Object> retorno;
        if (cliente != null) {
            retorno = cliente.toHashMap();
        } else {
            retorno = null;
        }

        return retorno;
    }

    private HashMap<String, Object> primeiroFuncionario(HashMap<String, Object> dados) {
        dao = new DAOFuncionarioDB4O();
        dao.conectar();
        Funcionario funcionario;
        if (dados.get("classificacao").equals("ordem")) {
            funcionario = (Funcionario) dao.primeiro(true);
        } else {
            funcionario = (Funcionario) dao.primeiro(false);
        }

        dao.desconectar();
        HashMap<String, Object> retorno;
        if (funcionario != null) {
            retorno = funcionario.toHashMap();
        } else {
            retorno = null;
        }

        return retorno;
    }

    private HashMap<String, Object> primeiroProduto(HashMap<String, Object> dados) {
        dao = new DAOProdutoDB4O();
        dao.conectar();
        Produto produto;
        if (dados.get("classificacao").equals("ordem")) {
            produto = (Produto) dao.primeiro(true);
        } else {
            produto = (Produto) dao.primeiro(false);
        }

        dao.desconectar();
        HashMap<String, Object> retorno;
        if (produto != null) {
            retorno = produto.toHashMap();
        } else {
            retorno = null;
        }

        return retorno;
    }

    private HashMap<String, Object> primeiraVenda(HashMap<String, Object> dados) {
        dao = new DAOVendaDB4O();
        dao.conectar();
        Venda venda;
        if (dados.get("classificacao").equals("ordem")) {
            venda = (Venda) dao.primeiro(true);
        } else {
            venda = (Venda) dao.primeiro(false);
        }

        dao.desconectar();
        HashMap<String, Object> retorno;
        if (venda != null) {
            retorno = venda.toHashMap();
        } else {
            retorno = null;
        }
        return retorno;
    }

    private HashMap<String, Object> proximoCliente(HashMap<String, Object> dados) {
        dao = new DAOClienteDB4O();
        dao.conectar();
        Cliente cliente;
        int codigo = 0;
        try {
            codigo = Integer.parseInt((String) dados.get("cod"));
        } catch (Exception ex) {
            codigo = 0;
        }
        if (dados.get("classificacao").equals("ordem")) {
            cliente = (Cliente) dao.proximo(codigo, true);
        } else {
            cliente = (Cliente) dao.proximo(codigo, false);
        }

        dao.desconectar();
        HashMap<String, Object> retorno;
        if (cliente != null) {
            retorno = cliente.toHashMap();
        } else {
            retorno = null;
        }
        return retorno;
    }

    private HashMap<String, Object> proximoFuncionario(HashMap<String, Object> dados) {
        dao = new DAOFuncionarioDB4O();
        dao.conectar();
        Funcionario funcionario;
        int codigo = 0;
        try {
            codigo = Integer.parseInt((String) dados.get("cod"));
        } catch (Exception ex) {
            codigo = 0;
        }
        if (dados.get("classificacao").equals("ordem")) {
            funcionario = (Funcionario) dao.proximo(codigo, true);
        } else {
            funcionario = (Funcionario) dao.proximo(codigo, false);
        }

        dao.desconectar();
        HashMap<String, Object> retorno;
        if (funcionario != null) {
            retorno = funcionario.toHashMap();
        } else {
            retorno = null;
        }
        return retorno;
    }

    private HashMap<String, Object> proximoProduto(HashMap<String, Object> dados) {
        dao = new DAOProdutoDB4O();
        dao.conectar();
        Produto produto;
        int codigo = 0;
        try {
            codigo = Integer.parseInt((String) dados.get("cod"));
        } catch (Exception ex) {
            codigo = 0;
        }
        if (dados.get("classificacao").equals("ordem")) {
            produto = (Produto) dao.proximo(codigo, true);
        } else {
            produto = (Produto) dao.proximo(codigo, false);
        }

        dao.desconectar();
        HashMap<String, Object> retorno;
        if (produto != null) {
            retorno = produto.toHashMap();
        } else {
            retorno = null;
        }
        return retorno;
    }

    private HashMap<String, Object> proximaVenda(HashMap<String, Object> dados) {
        dao = new DAOVendaDB4O();
        dao.conectar();
        Venda venda;
        int codigo = 0;
        try {
            codigo = Integer.parseInt((String) dados.get("cod"));
        } catch (Exception ex) {
            codigo = 0;
        }
        if (dados.get("classificacao").equals("ordem")) {
            venda = (Venda) dao.proximo(codigo, true);
        } else {
            venda = (Venda) dao.proximo(codigo, false);
        }

        dao.desconectar();
        HashMap<String, Object> retorno;
        if (venda != null) {
            retorno = venda.toHashMap();
        } else {
            retorno = null;
        }
        return retorno;
    }

    private HashMap<String, Object> ultimoCliente(HashMap<String, Object> dados) {
        dao = new DAOClienteDB4O();
        dao.conectar();
        Cliente cliente;
        if (dados.get("classificacao").equals("ordem")) {
            cliente = (Cliente) dao.ultimo(true);
        } else {
            cliente = (Cliente) dao.ultimo(false);
        }
        dao.desconectar();
        HashMap<String, Object> retorno;
        if (cliente != null) {
            retorno = cliente.toHashMap();
        } else {
            retorno = null;
        }
        return retorno;
    }

    private HashMap<String, Object> ultimoFuncionario(HashMap<String, Object> dados) {
        dao = new DAOFuncionarioDB4O();
        dao.conectar();
        Funcionario funcionario;
        if (dados.get("classificacao").equals("ordem")) {
            funcionario = (Funcionario) dao.ultimo(true);
        } else {
            funcionario = (Funcionario) dao.ultimo(false);
        }
        dao.desconectar();
        HashMap<String, Object> retorno;
        if (funcionario != null) {
            retorno = funcionario.toHashMap();
        } else {
            retorno = null;
        }
        return retorno;
    }

    private HashMap<String, Object> ultimoProduto(HashMap<String, Object> dados) {
        dao = new DAOProdutoDB4O();
        dao.conectar();
        Produto produto;
        if (dados.get("classificacao").equals("ordem")) {
            produto = (Produto) dao.ultimo(true);
        } else {
            produto = (Produto) dao.ultimo(false);
        }
        dao.desconectar();
        HashMap<String, Object> retorno;
        if (produto != null) {
            retorno = produto.toHashMap();
        } else {
            retorno = null;
        }
        return retorno;
    }

    private HashMap<String, Object> ultimaVenda(HashMap<String, Object> dados) {
        dao = new DAOVendaDB4O();
        dao.conectar();
        Venda venda;
        if (dados.get("classificacao").equals("ordem")) {
            venda = (Venda) dao.ultimo(true);
        } else {
            venda = (Venda) dao.ultimo(false);
        }
        dao.desconectar();
        HashMap<String, Object> retorno;
        if (venda != null) {
            retorno = venda.toHashMap();
        } else {
            retorno = null;
        }
        return retorno;
    }

    private HashMap<String, Object> loginFuncionario(HashMap<String, Object> dados) {
        HashMap<String, Object> retorno = new HashMap<String, Object>();
        try {
            Funcionario funcionario;
            dao = new DAOFuncionarioDB4O();
            dao.conectar();
            funcionario = (Funcionario) dao.localizar((String) dados.get("login"), "login");
            dao.desconectar();
            if (funcionario != null) {
                retorno = funcionario.toHashMap();
                retorno.put("retorno", "sucesso");
                return retorno;
            } else {
                return null;
            }

        } catch (Exception es) {
            retorno.put("retorno", "Erro ao consultar funcionario. Contate o admnistrador.");
            return retorno;
        }
    }

    private String incluirCaixa(HashMap<String, Object> dados) {
        Caixa caixa = new Caixa(dados);
        String str;
        try {
            dao = new DAOCaixaDB4O();
            dao.conectar();
            dao.incluir(caixa);
            dao.desconectar();
            str = "Historico do caixa inserido com sucesso.";
        } catch (Exception ex) {
            str = "Falha ao inserir historico do caixa. Contate o administrador.";
        }
        return str;
    }

    private HashMap<String, Object> primeiroCaixa(HashMap<String, Object> dados) {
        dao = new DAOCaixaDB4O();
        dao.conectar();
        Caixa caixa;
        if (dados.get("classificacao").equals("ordem")) {
            caixa = (Caixa) dao.primeiro(true);
        } else {
            caixa = (Caixa) dao.primeiro(false);
        }
        dao.desconectar();
        HashMap<String, Object> retorno;
        if (caixa != null) {
            retorno = caixa.toHashMap();
        } else {
            retorno = null;
        }
        return retorno;
    }

    private HashMap<String, Object> proximoCaixa(HashMap<String, Object> dados) {
        dao = new DAOCaixaDB4O();
        dao.conectar();
        Caixa caixa;
        int codigo = 0;
        try {
            codigo = Integer.parseInt((String) dados.get("cod"));
        } catch (Exception ex) {
            codigo = 0;
        }
        if (dados.get("classificacao").equals("ordem")) {
            caixa = (Caixa) dao.proximo(codigo, true);
        } else {
            caixa = (Caixa) dao.proximo(codigo, false);
        }
        dao.desconectar();
        HashMap<String, Object> retorno;
        if (caixa != null) {
            retorno = caixa.toHashMap();
        } else {
            retorno = null;
        }
        return retorno;
    }

    private HashMap<String, Object> anteriorCaixa(HashMap<String, Object> dados) {
        dao = new DAOCaixaDB4O();
        dao.conectar();
        Caixa caixa;
        int codigo = 0;
        try {
            codigo = Integer.parseInt((String) dados.get("cod"));
        } catch (Exception ex) {
            codigo = 0;
        }
        if (dados.get("classificacao").equals("ordem")) {
            caixa = (Caixa) dao.anterior(codigo, true);
        } else {
            caixa = (Caixa) dao.anterior(codigo, false);
        }
        dao.desconectar();
        HashMap<String, Object> retorno;
        if (caixa != null) {
            retorno = caixa.toHashMap();
        } else {
            retorno = null;
        }
        return retorno;
    }

    private HashMap<String, Object> ultimoCaixa(HashMap<String, Object> dados) {
        dao = new DAOCaixaDB4O();
        dao.conectar();
        Caixa caixa;
        if (dados.get("classificacao").equals("ordem")) {
            caixa = (Caixa) dao.ultimo(true);
        } else {
            caixa = (Caixa) dao.ultimo(false);
        }
        dao.desconectar();
        HashMap<String, Object> retorno;
        if (caixa != null) {
            retorno = caixa.toHashMap();
        } else {
            retorno = null;
        }
        return retorno;
    }

    private HashMap<String, Object> consultarCaixaCod(HashMap<String, Object> dados) {
        HashMap<String, Object> retorno = new HashMap<String, Object>();
        try {
            Caixa caixa;
            dao = new DAOCaixaDB4O();
            dao.conectar();
            caixa = (Caixa) dao.localizar(Integer.parseInt((String) dados.get("cod")));
            dao.desconectar();
            if (caixa != null) {
                retorno = caixa.toHashMap();
                retorno.put("retorno", "sucesso");
                return retorno;
            } else {
                return null;
            }
        } catch (Exception es) {
            retorno.put("retorno", "Erro ao consultar historico caixa. Contate o admnistrador.");
            return retorno;
        }
    }

    private HashMap<String, Object> consultarCaixaData(HashMap<String, Object> dados) {
        HashMap<String, Object> retorno = new HashMap<String, Object>();
        try {
            Caixa caixa;
            dao = new DAOCaixaDB4O();
            dao.conectar();
            caixa = (Caixa) dao.localizar((String) dados.get("data"), "data");
            dao.desconectar();
            if (caixa != null) {
                retorno = caixa.toHashMap();
                retorno.put("retorno", "sucesso");
                return retorno;
            } else {
                return null;
            }
        } catch (Exception es) {
            retorno.put("retorno", "Erro ao consultar historico caixa. Contate o admnistrador.");
            return retorno;
        }
    }

    private HashMap<String, Object> ordenaListaCaixa(HashMap<String, Object> dados) {
        List l = new ArrayList();
        List m = new ArrayList();
        HashMap<String, Object> retorno = new HashMap<String, Object>();
        dao = new DAOCaixaDB4O();
        dao.conectar();
        if (((String) dados.get("classificacao")).equals("ordem")) {
            l = dao.ordenaLista(false);
        } else {
            l = dao.ordenaLista(true);
        }
        dao.desconectar();
        for (int i = 0; i < l.size(); i++) {
            retorno = ((Caixa) l.get(i)).toHashMap();
            m.add(retorno);
        }
        retorno.put("lista", m);
        return retorno;
    }

    private String alterarCaixa(HashMap<String, Object> dados) {
        dao = new DAOCaixaDB4O();
        dao.conectar();
        int id = 0;
        String str;
        Caixa caixa = new Caixa(dados);
        try {
            id = Integer.parseInt((String) dados.get("cod"));
            str = dao.alterar(id, caixa);
            dao.desconectar();
        } catch (Exception ex) {
            id = 0;
            str = "Erro: impossivel alterar historico de caixa";
        }
        return str;
    }
}
