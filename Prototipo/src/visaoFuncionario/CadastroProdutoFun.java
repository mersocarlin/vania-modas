package visaoFuncionario;

import controlador.Controlador;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import utilitarios.DataSistema;
import utilitarios.Sessao;

/**
 *
 * @author Hemerson e Jefferson
 */
public class CadastroProdutoFun extends javax.swing.JFrame {

    private UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
    private boolean cadastrar = true;
    boolean ordem = true;
    Controlador controlador;
    DefaultTableModel modelo;
    DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
    private Sessao sessao;
    String valorCompra;

    /** Creates new form CadastroProduto */
    public CadastroProdutoFun() {
        initComponents();
        timer1.start();
        cadastrar = true;
        controlador = new Controlador();
        jTextFieldCod.setText(this.getMaiorId());
        valorCompra = "0.00";
    }

    public CadastroProdutoFun(Sessao sessao) {
        initComponents();
        this.construtor(sessao);
        cadastrar = true;
        controlador = new Controlador();
        jTextFieldCod.setText(this.getMaiorId());
        valorCompra = "0.00";
    }

    public CadastroProdutoFun(HashMap<String, Object> c, Sessao sessao) {
        initComponents();
        controlador = new Controlador();
        this.construtor(sessao);
        this.mostraDados(c);
        this.modificaConsultar();
    }

    private String cadastrarProduto() {
        if (!jTextFieldDesc.getText().equals("")) {
            if (!jTextFieldQtdeEntrada.getText().equals("")) {
                if (!jTextFieldValorVenda.getText().equals("")) {
                    HashMap<String, Object> dados = new HashMap<String, Object>();
                    if (cadastrar) {
                        dados = this.pegaDados();
                        if (dados == null) {
                            JOptionPane.showMessageDialog(null, "Por favor, corrija os dados!",
                                    "Erro", JOptionPane.WARNING_MESSAGE);
                            return "";
                        } else {
                            HashMap<String, Object> retorno = new HashMap<String, Object>();
                            retorno.put("nome", jTextFieldDesc.getText().toUpperCase());
                            retorno.put("operacao", "consultar");
                            retorno.put("modelo", "produto");
                            retorno = controlador.recebeOperacao(retorno);
                            if (retorno != null) {
                                JOptionPane.showMessageDialog(null, "Este produto ja esta cadastrado no base de dados do sistema!");
                            } else {
                                retorno = null;
                                dados.put("modelo", "produto");
                                dados.put("operacao", "inserir");
                                dados = controlador.recebeOperacao(dados);
                                JOptionPane.showMessageDialog(null, (String) dados.get("retorno"));
                                return "sucesso";
                            }
                        }
                    } else {
                        dados = this.pegaDados();
                        dados.put("operacao", "alterar");
                        dados.put("modelo", "produto");
                        dados = controlador.recebeOperacao(dados);
                        JOptionPane.showMessageDialog(null, (String) dados.get("retorno"));
                        return "sucesso";
                    }
                    this.limpa();
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, digite um valor de venda!",
                            "Erro", JOptionPane.WARNING_MESSAGE);
                    jTextFieldValorVenda.grabFocus();
                    return "";
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, digite a quantidade de mercadoria!",
                        "Erro", JOptionPane.WARNING_MESSAGE);
                jTextFieldQtdeEntrada.grabFocus();
                return "";
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, digite uma descricao para o novo Produto!",
                    "Erro", JOptionPane.WARNING_MESSAGE);
            jTextFieldDesc.grabFocus();
            return "";
        }
        return "";
    }

    private void mascaraTel() {
        MaskFormatter format = null;
        jFormattedTextFieldContatoFornecedor.setFormatterFactory(null);
        try {
            format = new MaskFormatter("(##)####-####");
            format.setPlaceholderCharacter('_');
        } catch (Exception ex) {
            System.out.println("Erro carregar mascara telefone");
        }
        jFormattedTextFieldContatoFornecedor.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldContatoFornecedor.setValue(null);
    }

    private HashMap<String, Object> pegaDados() {
        HashMap<String, Object> retorno = new HashMap<String, Object>();
        String h;
        try {
            int cod = Integer.parseInt(jTextFieldCod.getText());
            retorno.put("cod", jTextFieldCod.getText());
        } catch (Exception e) {
            return null;
        }
        try {
            retorno.put("dataTermino", jFormattedTextFieldDataTermino.getText());
        } catch (Exception e) {
            retorno.put("dataTermino", "01/01/1991");
        }
        retorno.put("desc", jTextFieldDesc.getText().toUpperCase());
        retorno.put("nomeFornecedor", jTextFieldNomeFornecedor.getText().toUpperCase());
        h = (String) jFormattedTextFieldContatoFornecedor.getValue();
        if (h == null) {
            h = "(00)0000-0000";
        }
        retorno.put("contatoFornecedor", h);
        h = (String) jFormattedTextFieldDataCadastro.getValue();
        if (h == null) {
            h = "00/00/0000";
        }
        retorno.put("dataCadastro", h);
        h = (String) jFormattedTextFieldDataEntrada.getValue();
        if (h == null) {
            h = (String) jFormattedTextFieldDataCadastro.getValue();
        }
        retorno.put("dataEntrada", h);
        try {
            int cod = Integer.parseInt(jTextFieldQtdeEntrada.getText());
            retorno.put("qtdeEntrada", jTextFieldQtdeEntrada.getText());
        } catch (Exception e) {
            return null;
        }

        retorno.put("valorCompra", valorCompra);
        try {
            retorno.put("qtdeVendida", jTextFieldQtdeVendida.getText());
        } catch (Exception e) {
            retorno.put("qtdeVendida", "0");
        }
        h = this.trataPreco(jTextFieldValorVenda.getText());
        if (!h.equals("erro")) {
            retorno.put("valorVenda", h);
        } else {
            return null;
        }
        retorno.put("ativo", "true");
        return retorno;
    }

    private String trataPreco(String valor) {
        try {
            valor = valor.replace(',', '.');
        } catch (Exception e) {
            return "0.00";
        }
        try {
            Double.parseDouble(valor);
            if (valor.equals("")) {
                return "0.00";
            } else {
                String depois = valor.substring(valor.lastIndexOf(".") + 1);
                String antes = valor.substring(0, valor.lastIndexOf(".") + 1);
                if (antes.compareTo("") == 0) {
                    antes = depois + ".";
                    depois = "";
                } else {
                    if (antes.compareTo(".") == 0) {
                        antes = "0" + antes;
                    }
                }
                if (depois.length() < 2) {
                    while (depois.length() < 2) {
                        depois += "0";
                    }
                }
                return antes + depois;
            }
        } catch (NumberFormatException numberFormatException) {
            return "erro";
        }
    }

    public void mostraDados(HashMap<String, Object> produto) {
        if (!cadastrar) {
            jTextFieldQtdeVendida.setEnabled(true);
            jFormattedTextFieldDataTermino.setEnabled(true);
        }

        if (produto != null) {
            jTextFieldCod.setText((String) produto.get("cod"));
            jTextFieldDesc.setText((String) produto.get("desc"));
            jTextFieldNomeFornecedor.setText((String) produto.get("nomeFornecedor"));
            jTextFieldQtdeEntrada.setText((String) produto.get("qtdeEntrada"));
            jTextFieldQtdeVendida.setText((String) produto.get("qtdeVendida"));
            this.valorCompra = (String) produto.get("valorCompra");
            jTextFieldValorVenda.setText((String) produto.get("valorVenda"));
            jFormattedTextFieldContatoFornecedor.setValue(produto.get("contatoFornecedor"));
            jFormattedTextFieldDataCadastro.setValue(produto.get("dataCadastro"));
            jFormattedTextFieldDataEntrada.setValue(produto.get("dataEntrada"));
            jFormattedTextFieldDataTermino.setValue(produto.get("dataTermino"));

            // ---------------- PREENCHE TABELA -----------------
            this.limpaTabela();
            modelo = (DefaultTableModel) jTable1.getModel();

            HashMap<String, Object> dadosVenda = new HashMap<String, Object>();
            dadosVenda.put("modelo", "venda");
            dadosVenda.put("operacao", "ordenaLista");
            dadosVenda.put("classificacao", "ordem");
            dadosVenda = controlador.recebeOperacao(dadosVenda);
            List lista = (List) dadosVenda.get("lista");
            Object linha[] = new Object[]{};
            for (int j = 0; j < lista.size(); j++) {
                List itens = (List) ((HashMap<String, Object>) lista.get(j)).get("itensVenda");
                for (int i = 0; i < itens.size(); i++) {
                    //exemplo: codigo#descricao#qtde#valorUnitario (cod,desc,quantidade,preco)
                    String item = (String) itens.get(i);
                    if (Integer.parseInt((String) produto.get("cod")) == Integer.parseInt(item.split("#")[0])) {
                        linha = new Object[]{((HashMap<String, Object>) lista.get(j)).get("cod"), ((HashMap<String, Object>) lista.get(j)).get("dataVenda"), item.split("#")[2]};
                        modelo.addRow(linha);
                        break;
                    }
                }
            }
        }
    }

    private void construtor(Sessao sessao) {
        DataSistema d = DataSistema.getInstance();
        jFormattedTextFieldDataCadastro.setValue(d.diaMesAno().split(" ")[1]);
        this.setLocationRelativeTo(null);
        this.mascaraData();
        try {
            UIManager.setLookAndFeel(looks[3].getClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.cadastrar = true;
        jTextFieldDesc.requestFocus();
        this.mascaraTel();
        jTable1.getTableHeader().setReorderingAllowed(false);
        this.alinhaColunas();
        jLabelLogin.setText(sessao.getLogin());
        this.sessao = new Sessao();
        this.sessao.setLogin(sessao.getLogin());
        this.sessao.setSenha(sessao.getSenha());
        timer1.start();
        if(!cadastrar){
            jButtonCadastrar.setToolTipText("Alterar produto");
            jButtonLimpar.setToolTipText("Excluir produto");
            jButtonVoltar.setToolTipText("Retorna para a tela principal e nao altera o produto");
        }else{
            jButtonCadastrar.setToolTipText("Cadastrar produto");
            jButtonLimpar.setToolTipText("Limpa todos os campos");
            jButtonVoltar.setToolTipText("Cancela o cadastro do produto");
            jButtonVoltar.setToolTipText("Retorna para a tela principal e nao cadastra o produto");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        timer1 = new org.netbeans.examples.lib.timerbean.Timer();
        jLabelCod = new javax.swing.JLabel();
        jTextFieldCod = new javax.swing.JTextField();
        jLabelNasc1 = new javax.swing.JLabel();
        jFormattedTextFieldDataCadastro = new javax.swing.JFormattedTextField();
        jPanel3 = new javax.swing.JPanel();
        jButtonCadastrar = new javax.swing.JButton();
        jButtonLimpar = new javax.swing.JButton();
        jButtonVoltar = new javax.swing.JButton();
        jButtonPrimeiro = new javax.swing.JButton();
        jButtonAnterior = new javax.swing.JButton();
        jButtonProximo = new javax.swing.JButton();
        jButtonUltimo = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jTextFieldDesc = new javax.swing.JTextField();
        jLabelDesc = new javax.swing.JLabel();
        jLabelQtde = new javax.swing.JLabel();
        jTextFieldQtdeEntrada = new javax.swing.JTextField();
        jLabelQtde1 = new javax.swing.JLabel();
        jTextFieldQtdeVendida = new javax.swing.JTextField();
        jLabelPreco1 = new javax.swing.JLabel();
        jTextFieldValorVenda = new javax.swing.JTextField();
        jLabelEntrada = new javax.swing.JLabel();
        jFormattedTextFieldDataEntrada = new javax.swing.JFormattedTextField();
        jLabelTermino = new javax.swing.JLabel();
        jFormattedTextFieldDataTermino = new javax.swing.JFormattedTextField();
        jButtonDataAtual = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jTextFieldNomeFornecedor = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jFormattedTextFieldContatoFornecedor = new javax.swing.JFormattedTextField();
        jLabelFornecedor = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldValorCompra = new javax.swing.JTextField();
        jLabelPreco = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabelLogin = new javax.swing.JLabel();
        jLabelData = new javax.swing.JLabel();
        jLabelHora = new javax.swing.JLabel();

        timer1.addTimerListener(new org.netbeans.examples.lib.timerbean.TimerListener() {
            public void onTime(java.awt.event.ActionEvent evt) {
                timer1OnTime(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("CADASTRO DE PRODUTO");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabelCod.setText("Codigo: ");

        jTextFieldCod.setEditable(false);
        jTextFieldCod.setFont(new java.awt.Font("Tahoma", 1, 11));
        jTextFieldCod.setForeground(new java.awt.Color(255, 0, 0));
        jTextFieldCod.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldCod.setDisabledTextColor(new java.awt.Color(255, 0, 0));
        jTextFieldCod.setFocusable(false);

        jLabelNasc1.setText("Data Cadastro:");

        jFormattedTextFieldDataCadastro.setEditable(false);
        jFormattedTextFieldDataCadastro.setForeground(new java.awt.Color(255, 0, 0));
        jFormattedTextFieldDataCadastro.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jFormattedTextFieldDataCadastro.setText("00/00/0000");
        jFormattedTextFieldDataCadastro.setFocusable(false);
        jFormattedTextFieldDataCadastro.setFont(new java.awt.Font("Tahoma", 1, 11));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Obcoes/Navegacao"));

        jButtonCadastrar.setText("Cadastrar");
        jButtonCadastrar.setToolTipText("Cadastrar produto");
        jButtonCadastrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCadastrarActionPerformed(evt);
            }
        });

        jButtonLimpar.setText("Limpar");
        jButtonLimpar.setToolTipText("Limpa todos os campos");
        jButtonLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLimparActionPerformed(evt);
            }
        });

        jButtonVoltar.setText("Cancelar");
        jButtonVoltar.setToolTipText("Cancela o cadastro do produto");
        jButtonVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVoltarActionPerformed(evt);
            }
        });

        jButtonPrimeiro.setText("<");
        jButtonPrimeiro.setToolTipText("Primeiro produto");
        jButtonPrimeiro.setEnabled(false);
        jButtonPrimeiro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrimeiroActionPerformed(evt);
            }
        });

        jButtonAnterior.setText("<<");
        jButtonAnterior.setToolTipText("Produto anterior");
        jButtonAnterior.setEnabled(false);
        jButtonAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAnteriorActionPerformed(evt);
            }
        });

        jButtonProximo.setText(">>");
        jButtonProximo.setToolTipText("Proximo produto");
        jButtonProximo.setEnabled(false);
        jButtonProximo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonProximoActionPerformed(evt);
            }
        });

        jButtonUltimo.setText(">");
        jButtonUltimo.setToolTipText("Ultimo produto");
        jButtonUltimo.setEnabled(false);
        jButtonUltimo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUltimoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonCadastrar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonLimpar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonVoltar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 146, Short.MAX_VALUE)
                .addComponent(jButtonPrimeiro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonAnterior)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonProximo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonUltimo)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCadastrar)
                    .addComponent(jButtonLimpar)
                    .addComponent(jButtonVoltar)
                    .addComponent(jButtonPrimeiro)
                    .addComponent(jButtonAnterior)
                    .addComponent(jButtonProximo)
                    .addComponent(jButtonUltimo))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        jLabelDesc.setText("Descricao:");

        jLabelQtde.setText("Qtd. Entrada:");

        jTextFieldQtdeEntrada.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldQtdeEntradaFocusLost(evt);
            }
        });

        jLabelQtde1.setText("Qtd. Vendida:");

        jTextFieldQtdeVendida.setEditable(false);
        jTextFieldQtdeVendida.setFocusable(false);

        jLabelPreco1.setText("Valor Venda:");

        jTextFieldValorVenda.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldValorVendaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldValorVendaFocusLost(evt);
            }
        });

        jLabelEntrada.setText("Data Entrada:");

        jFormattedTextFieldDataEntrada.setEditable(false);
        jFormattedTextFieldDataEntrada.setFocusable(false);

        jLabelTermino.setText("Data Termino:");

        jFormattedTextFieldDataTermino.setEditable(false);
        jFormattedTextFieldDataTermino.setFocusable(false);

        jButtonDataAtual.setText(">");
        jButtonDataAtual.setToolTipText("Pega a data atual e insere na data de entrada");
        jButtonDataAtual.setEnabled(false);
        jButtonDataAtual.setFocusable(false);
        jButtonDataAtual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDataAtualActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Fornecedor"));

        jTextFieldNomeFornecedor.setEditable(false);
        jTextFieldNomeFornecedor.setFocusable(false);

        jLabel1.setText("Contato:");

        jFormattedTextFieldContatoFornecedor.setEditable(false);
        jFormattedTextFieldContatoFornecedor.setFocusable(false);
        jFormattedTextFieldContatoFornecedor.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jFormattedTextFieldContatoFornecedorFocusLost(evt);
            }
        });

        jLabelFornecedor.setText("Nome:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelFornecedor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jFormattedTextFieldContatoFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldNomeFornecedor, javax.swing.GroupLayout.DEFAULT_SIZE, 455, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelFornecedor)
                    .addComponent(jTextFieldNomeFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jFormattedTextFieldContatoFornecedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setText("(*)");

        jLabel4.setForeground(new java.awt.Color(255, 0, 0));
        jLabel4.setText("(*)");

        jTextFieldValorCompra.setEditable(false);
        jTextFieldValorCompra.setFocusable(false);
        jTextFieldValorCompra.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldValorCompraFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldValorCompraFocusLost(evt);
            }
        });

        jLabelPreco.setText("Valor Compra:");

        jLabel6.setForeground(new java.awt.Color(255, 0, 0));
        jLabel6.setText("(*)");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelDesc, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                            .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jLabelEntrada, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabelQtde, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 77, Short.MAX_VALUE))
                            .addComponent(jLabelPreco1, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jTextFieldDesc, javax.swing.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel4))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextFieldValorVenda)
                                    .addComponent(jFormattedTextFieldDataEntrada)
                                    .addComponent(jTextFieldQtdeEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButtonDataAtual, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel6))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 90, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jLabelPreco, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabelTermino, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabelQtde1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jTextFieldValorCompra, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jTextFieldQtdeVendida, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jFormattedTextFieldDataTermino, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelDesc)
                    .addComponent(jTextFieldDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(11, 11, 11)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelQtde)
                            .addComponent(jTextFieldQtdeEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelPreco1)
                            .addComponent(jTextFieldValorVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(7, 7, 7))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldQtdeVendida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelQtde1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldValorCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelPreco))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelEntrada)
                    .addComponent(jFormattedTextFieldDataEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonDataAtual)
                    .addComponent(jFormattedTextFieldDataTermino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTermino))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        jTabbedPane1.addTab("Dados", jPanel4);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cupom", "Data Venda", "Quantidade"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setToolTipText("Relacao de vendas onde constam o produto");
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Saida", jPanel5);

        jLabel2.setForeground(new java.awt.Color(255, 0, 0));
        jLabel2.setText("(*)");

        jLabel5.setText("Campos obrigatorios");

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel12.setText("Usuario:");

        jLabelHora.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelLogin, javax.swing.GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabelData, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelHora, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabelLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelData, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelHora, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 638, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelCod, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldCod, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 306, Short.MAX_VALUE)
                        .addComponent(jLabelNasc1)
                        .addGap(18, 18, 18)
                        .addComponent(jFormattedTextFieldDataCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCod)
                    .addComponent(jTextFieldCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNasc1)
                    .addComponent(jFormattedTextFieldDataCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCadastrarActionPerformed
        if (this.cadastrarProduto().equals("sucesso")) {
            new TelaPrincipalFun(sessao).show();
            this.dispose();
        }

}//GEN-LAST:event_jButtonCadastrarActionPerformed

    private void jButtonVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVoltarActionPerformed
        new TelaPrincipalFun(sessao).show();
        this.dispose();
}//GEN-LAST:event_jButtonVoltarActionPerformed

private void jButtonLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimparActionPerformed
    if (cadastrar) {
        this.limpa();
    } else {
        int resposta = JOptionPane.showConfirmDialog(null, "Desejar excluir este Produto?", "Exclusao", JOptionPane.YES_NO_OPTION);
        if (resposta == 0) {
            int cod = Integer.parseInt(this.jTextFieldCod.getText());
            HashMap<String, Object> dados = this.pegaDados();
            dados.put("operacao", "alterar");
            dados.put("modelo", "produto");
            dados.remove("ativo");
            dados.put("ativo", "false");
            dados = controlador.recebeOperacao(dados);
            String str = (String) dados.get("retorno");
            if (str.equals("Produto alterado!")) {
                JOptionPane.showMessageDialog(null, "Produto excluido com sucesso!");
                new TelaPrincipalFun(sessao).show();
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Impossivel excluir produto. Contacte o administrador.");
            }
        }
    }
}//GEN-LAST:event_jButtonLimparActionPerformed

private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    new TelaPrincipalFun(sessao).show();
    this.dispose();
}//GEN-LAST:event_formWindowClosing

private void jButtonPrimeiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrimeiroActionPerformed
    HashMap<String, Object> dados = new HashMap<String, Object>();
    dados.put("modelo", "produto");
    dados.put("operacao", "busca");
    dados.put("posicao", "primeiro");
    dados.put("classificacao", "ordem");
    dados = controlador.recebeOperacao(dados);
    if (dados.get("ativo").equals("true")) {
        this.mostraDados(dados);
    } else {
        String codigo = (String) dados.get("cod");
        while (true) {
            dados.put("modelo", "produto");
            dados.put("operacao", "busca");
            dados.put("posicao", "proximo");
            dados.put("classificacao", "ordem");
            dados.put("cod", codigo);
            dados = controlador.recebeOperacao(dados);
            if (dados.get("ativo").equals("true")) {
                this.mostraDados(dados);
                break;
            } else {
                codigo = (String) dados.get("cod");
            }
        }
    }
}//GEN-LAST:event_jButtonPrimeiroActionPerformed

private void jButtonAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAnteriorActionPerformed
    HashMap<String, Object> dados = new HashMap<String, Object>();
    dados.put("modelo", "produto");
    dados.put("operacao", "busca");
    dados.put("posicao", "anterior");
    dados.put("classificacao", "ordem");
    dados.put("cod", jTextFieldCod.getText());
    dados = controlador.recebeOperacao(dados);
    if (dados.get("ativo").equals("true")) {
        this.mostraDados(dados);
    } else {
        String codigo = (String) dados.get("cod");
        while (true) {
            dados.put("modelo", "produto");
            dados.put("operacao", "busca");
            dados.put("posicao", "anterior");
            dados.put("classificacao", "ordem");
            dados.put("cod", codigo);
            dados = controlador.recebeOperacao(dados);
            if (dados.get("ativo").equals("true")) {
                this.mostraDados(dados);
                break;
            } else {
                codigo = (String) dados.get("cod");
            }
        }
    }
}//GEN-LAST:event_jButtonAnteriorActionPerformed

private void jButtonProximoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonProximoActionPerformed
    HashMap<String, Object> dados = new HashMap<String, Object>();
    dados.put("modelo", "produto");
    dados.put("operacao", "busca");
    dados.put("posicao", "proximo");
    dados.put("classificacao", "ordem");
    dados.put("cod", jTextFieldCod.getText());
    dados = controlador.recebeOperacao(dados);
    if (dados.get("ativo").equals("true")) {
        this.mostraDados(dados);
    } else {
        String codigo = (String) dados.get("cod");
        while (true) {
            dados.put("modelo", "produto");
            dados.put("operacao", "busca");
            dados.put("posicao", "proximo");
            dados.put("classificacao", "ordem");
            dados.put("cod", codigo);
            dados = controlador.recebeOperacao(dados);
            if (dados.get("ativo").equals("true")) {
                this.mostraDados(dados);
                break;
            } else {
                codigo = (String) dados.get("cod");
            }
        }
    }
}//GEN-LAST:event_jButtonProximoActionPerformed

private void jButtonUltimoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonUltimoActionPerformed
    HashMap<String, Object> dados = new HashMap<String, Object>();
    dados.put("modelo", "produto");
    dados.put("operacao", "busca");
    dados.put("posicao", "ultimo");
    dados.put("classificacao", "ordem");
    dados.put("cod", jTextFieldCod.getText());
    dados = controlador.recebeOperacao(dados);
    if (dados.get("ativo").equals("true")) {
        this.mostraDados(dados);
    } else {
        String codigo = (String) dados.get("cod");
        while (true) {
            dados.put("modelo", "produto");
            dados.put("operacao", "busca");
            dados.put("posicao", "anterior");
            dados.put("classificacao", "ordem");
            dados.put("cod", codigo);
            dados = controlador.recebeOperacao(dados);
            if (dados.get("ativo").equals("true")) {
                this.mostraDados(dados);
                break;
            } else {
                codigo = (String) dados.get("cod");
            }
        }
    }
}//GEN-LAST:event_jButtonUltimoActionPerformed

private void jTextFieldValorCompraFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldValorCompraFocusLost
    jTextFieldValorCompra.setText(this.trataPreco(jTextFieldValorCompra.getText()).toUpperCase());
    jFormattedTextFieldDataEntrada.requestFocus();
}//GEN-LAST:event_jTextFieldValorCompraFocusLost

private void jTextFieldValorVendaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldValorVendaFocusLost
    jTextFieldValorVenda.setText(this.trataPreco(jTextFieldValorVenda.getText()).toUpperCase());
    jTextFieldValorCompra.requestFocus();
}//GEN-LAST:event_jTextFieldValorVendaFocusLost

private void jTextFieldValorCompraFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldValorCompraFocusGained
    jTextFieldValorCompra.setSelectionStart(0);
    jTextFieldValorCompra.setSelectionEnd(jTextFieldValorCompra.getText().length());
}//GEN-LAST:event_jTextFieldValorCompraFocusGained

private void jTextFieldValorVendaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldValorVendaFocusGained
    jTextFieldValorVenda.setSelectionStart(0);
    jTextFieldValorVenda.setSelectionEnd(jTextFieldValorVenda.getText().length());
}//GEN-LAST:event_jTextFieldValorVendaFocusGained

private void jButtonDataAtualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDataAtualActionPerformed
    String str = DataSistema.getInstance().diaMesAno();
    jFormattedTextFieldDataEntrada.setValue(str.split(" ")[1]);

}//GEN-LAST:event_jButtonDataAtualActionPerformed

private void jFormattedTextFieldContatoFornecedorFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextFieldContatoFornecedorFocusLost
    if (!cadastrar) {
        jTabbedPane1.setSelectedIndex(1);
        jTable1.requestFocus();
    } else {
        jTabbedPane1.setSelectedIndex(0);
        jButtonCadastrar.requestFocus();
    }
}//GEN-LAST:event_jFormattedTextFieldContatoFornecedorFocusLost

private void jTextFieldQtdeEntradaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldQtdeEntradaFocusLost
    jTextFieldValorVenda.requestFocus();
}//GEN-LAST:event_jTextFieldQtdeEntradaFocusLost

private void timer1OnTime(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timer1OnTime
    DataSistema d = DataSistema.getInstance();
    String g = d.diaMesAno();
    jLabelData.setText(g);
    String h = d.horaMinSeg();
    jLabelHora.setText(h);
}//GEN-LAST:event_timer1OnTime

private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
    if (evt.getClickCount() == 2) {
        if (jTable1.getSelectedRow() >= 0) {
            RecebimentoFun recebimento = new RecebimentoFun("produto#"+jTextFieldCod.getText(), (DefaultTableModel) jTable1.getModel(), jTable1.getSelectedRow(), sessao);
            recebimento.show();
            this.dispose();
        }
    }
}//GEN-LAST:event_jTable1MouseClicked

    /**
     * @param args the command line arguments
     */
    public void modificaConsultar() {
        jTextFieldDesc.setEnabled(false);
        jTextFieldDesc.setBackground(Color.WHITE);
        jTextFieldDesc.setDisabledTextColor(Color.BLACK);
        cadastrar = false;
        jButtonCadastrar.setText("Alterar");
        jButtonLimpar.setText("Excluir");
        jButtonLimpar.setEnabled(false);
        this.jButtonAnterior.setEnabled(true);
        this.jButtonPrimeiro.setEnabled(true);
        this.jButtonProximo.setEnabled(true);
        this.jButtonUltimo.setEnabled(true);
    }

    public void limpa() {
        jFormattedTextFieldDataEntrada.setValue(null);
        jTextFieldValorCompra.setText("");
        jFormattedTextFieldDataTermino.setValue(null);
        jTextFieldCod.setText("");
        jTextFieldDesc.setText("");
        jTextFieldNomeFornecedor.setText("");
        jTextFieldQtdeEntrada.setText("");
//        barCode.setValue("");
//        ((CodigoBarra) jPanelCodigo).setString("");
//        ((CodigoBarra) jPanelCodigo).setBarCode2D(barCode);
    }

    public void mascaraData() {
        MaskFormatter format = null;
        jFormattedTextFieldDataEntrada.setFormatterFactory(null);
        jFormattedTextFieldDataTermino.setFormatterFactory(null);
        try {
            format = new MaskFormatter("##/##/####");
            format.setPlaceholderCharacter('0');
        } catch (Exception ex) {
            System.out.println();
        }
        jFormattedTextFieldDataEntrada.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldDataEntrada.setValue(null);
        jFormattedTextFieldDataTermino.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldDataTermino.setValue(null);
    }

    private String getMaiorId() {
        HashMap<String, Object> dados = new HashMap<String, Object>();
        dados.put("modelo", "produto");
        dados.put("operacao", "maiorId");
        dados = controlador.recebeOperacao(dados);
        if (dados != null) {
            return (String) dados.get("retorno");
        }
        return "";
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new CadastroProdutoFun().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAnterior;
    private javax.swing.JButton jButtonCadastrar;
    private javax.swing.JButton jButtonDataAtual;
    private javax.swing.JButton jButtonLimpar;
    private javax.swing.JButton jButtonPrimeiro;
    private javax.swing.JButton jButtonProximo;
    private javax.swing.JButton jButtonUltimo;
    private javax.swing.JButton jButtonVoltar;
    private javax.swing.JFormattedTextField jFormattedTextFieldContatoFornecedor;
    private javax.swing.JFormattedTextField jFormattedTextFieldDataCadastro;
    private javax.swing.JFormattedTextField jFormattedTextFieldDataEntrada;
    private javax.swing.JFormattedTextField jFormattedTextFieldDataTermino;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelCod;
    private javax.swing.JLabel jLabelData;
    private javax.swing.JLabel jLabelDesc;
    private javax.swing.JLabel jLabelEntrada;
    private javax.swing.JLabel jLabelFornecedor;
    private javax.swing.JLabel jLabelHora;
    private javax.swing.JLabel jLabelLogin;
    private javax.swing.JLabel jLabelNasc1;
    private javax.swing.JLabel jLabelPreco;
    private javax.swing.JLabel jLabelPreco1;
    private javax.swing.JLabel jLabelQtde;
    private javax.swing.JLabel jLabelQtde1;
    private javax.swing.JLabel jLabelTermino;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextFieldCod;
    private javax.swing.JTextField jTextFieldDesc;
    private javax.swing.JTextField jTextFieldNomeFornecedor;
    private javax.swing.JTextField jTextFieldQtdeEntrada;
    private javax.swing.JTextField jTextFieldQtdeVendida;
    private javax.swing.JTextField jTextFieldValorCompra;
    private javax.swing.JTextField jTextFieldValorVenda;
    private org.netbeans.examples.lib.timerbean.Timer timer1;
    // End of variables declaration//GEN-END:variables

    private void alinhaColunas() {
        cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        jTable1.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
        jTable1.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
        jTable1.getColumnModel().getColumn(2).setCellRenderer(cellRenderer);
    }

    private void limpaTabela() {
        modelo = (DefaultTableModel) jTable1.getModel();
        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }
    }
}
