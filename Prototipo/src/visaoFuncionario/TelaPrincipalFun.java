package visaoFuncionario;

import visaoPrincipal.*;
import controlador.Controlador;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import persistencia.DAO;
import persistencia.DAOClienteDB4O;
import persistencia.DAOFuncionarioDB4O;
import persistencia.DAOProdutoDB4O;
import persistencia.DAOVendaDB4O;
import utilitarios.*;

/**
 *
 * @author Hemerson e Jefferson
 */
public class TelaPrincipalFun extends javax.swing.JFrame {

    //-----------PARAMETROS DA TELA DE CONSULTA-------------//
    String tipoConsulta;
    private String retorno;
    List l;
    Sessao sessao;
    Controlador controlador = new Controlador();
    DefaultTableModel modelo = new DefaultTableModel();
    DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
    //---------------------------------------------//
    private UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();

    /** Creates new form TelaPrincipal */
    public TelaPrincipalFun() {
        initComponents();
        this.contrutor();
    }

    public TelaPrincipalFun(Sessao sessao) {
        initComponents();
        this.sessao = new Sessao();
        this.sessao.setLogin(sessao.getLogin());
        this.sessao.setSenha(sessao.getSenha());
        jLabelLogin.setText(sessao.getLogin());
        this.contrutor();

    }

    private void mostrarTela() {
        modelo = (DefaultTableModel) jTableConsulta.getModel();
        if (this.jDialogConsulta.getTitle().split(" ")[0].equals("CONSULTA")) {
            HashMap<String, Object> dados = new HashMap<String, Object>();
            if (jTableConsulta.getSelectedRow() >= 0) {
                if (tipoConsulta.compareTo("CLIENTE") == 0) {
                    for (int i = 0; i < l.size(); i++) {
                        String nome = (String) modelo.getValueAt(jTableConsulta.getSelectedRow(), 1);
                        int cod = Integer.parseInt((String) modelo.getValueAt(jTableConsulta.getSelectedRow(), 0));
                        dados = (HashMap<String, Object>) l.get(i);
                        int codCli = Integer.parseInt((String) dados.get("cod"));
                        if ((((String) dados.get("nome")).compareTo(nome) == 0) && codCli == cod) {
                            jDialogConsulta.setVisible(false);
                            CadastroClienteFun a = new CadastroClienteFun(dados, sessao);
                            a.setTitle("CONSULTA DE CLIENTE");
                            a.show();
                            jDialogConsulta.dispose();
                            this.dispose();
                        }
                    }
                } else {
                    if (tipoConsulta.compareTo("PRODUTO") == 0) {
                        for (int i = 0; i < l.size(); i++) {
                            String nome = (String) modelo.getValueAt(jTableConsulta.getSelectedRow(), 1);
                            int cod = Integer.parseInt((String) modelo.getValueAt(jTableConsulta.getSelectedRow(), 0));
                            dados = (HashMap<String, Object>) l.get(i);
                            int codProduto = Integer.parseInt((String) dados.get("cod"));
                            if ((((String) dados.get("desc")).compareTo(nome) == 0) && codProduto == codProduto) {
                                jDialogConsulta.setVisible(false);
                                CadastroProdutoFun a = new CadastroProdutoFun(dados, sessao);
                                a.setTitle("CONSULTA DE PRODUTO");
                                a.show();
                                jDialogConsulta.dispose();
                                this.dispose();
                            }
                        }
                    } else {
                        if (tipoConsulta.compareTo("CAIXA") == 0) {
                            for (int i = 0; i < l.size(); i++) {
                                String data = (String) modelo.getValueAt(jTableConsulta.getSelectedRow(), 1);
                                int cod = Integer.parseInt((String) modelo.getValueAt(jTableConsulta.getSelectedRow(), 0));
                                dados = (HashMap<String, Object>) l.get(i);
                                int codProduto = Integer.parseInt((String) dados.get("cod"));
                                if ((((String) dados.get("data")).compareTo(data) == 0) && codProduto == codProduto) {
                                    jDialogConsulta.setVisible(false);
                                    TelaCaixaFun a = new TelaCaixaFun(sessao, dados);
                                    a.setTitle("CONSULTA DE CAIXA");
                                    a.show();
                                    jDialogConsulta.dispose();
                                    this.dispose();
                                }
                            }
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione o " + tipoConsulta + " desejado.");
            }
        } else {
            if (jTableConsulta.getSelectedRow() >= 0) {
                modelo = (DefaultTableModel) jTableConsulta.getModel();
                int ret = Integer.parseInt((String) modelo.getValueAt(jTableConsulta.getSelectedRow(), 0));
                this.setRetorno(ret + "");
                this.dispose();
            }
        }
    }

    private void consultaCodigo() {
        HashMap<String, Object> dados = new HashMap<String, Object>();
        String coluna2 = "";
        String coluna3 = "";
        if (this.tipoConsulta.equals("CLIENTE") || this.tipoConsulta.equals("FUNCIONARIO")) {
            coluna2 = "nome";
            coluna3 = "cpf";
        } else {
            if (this.tipoConsulta.equals("PRODUTO")) {
                coluna2 = "desc";
                coluna3 = "valorVenda";
            } else {
                if (this.tipoConsulta.equals("CAIXA")) {
                    coluna2 = "data";
                }
            }

        }
        String cod = (String) jFormattedTextFieldCod.getText();
        try {
            if (cod != null) {
                if (!cod.equals("") && (Integer.parseInt(cod) != 0)) {
                    int i = Integer.parseInt(cod);
                    dados.put("modelo", tipoConsulta.toLowerCase());
                    dados.put("operacao", "busca");
                    dados.put("posicao", "cod");
                    dados.put("cod", "" + i);
                    dados = controlador.recebeOperacao(dados);
                    l = (List) dados.get("lista");
                    this.limpaTabelaConsulta();
                    if (!((String) dados.get("retorno")).equals("sucesso") || (dados.get("ativo").equals("false"))) {
                        JOptionPane.showMessageDialog(this, "Nenhum " + tipoConsulta + " foi encontrado com este codigo.");
                        this.preencheTabelaConsulta();
                    } else {
                        if (!this.tipoConsulta.equals("CAIXA")) {

                            Object[] linha = new Object[]{((String) dados.get("cod")), ((String) dados.get(coluna2)), ((String) dados.get(coluna3))};
                            modelo.addRow(linha);
                            jTextFieldNome.setText((String) dados.get("nome"));
                        } else {
                        }
                    }
                } else {
                    this.preencheTabelaConsulta();
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao consultar " + tipoConsulta + " .");
            this.preencheTabelaConsulta();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialogConsulta = new javax.swing.JDialog();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabelNameConsulta = new javax.swing.JLabel();
        jTextFieldNome = new javax.swing.JTextField();
        jFormattedTextFieldCod = new javax.swing.JFormattedTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableConsulta = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        buttonGroup1 = new javax.swing.ButtonGroup();
        timer1 = new org.netbeans.examples.lib.timerbean.Timer();
        jDialogAjuda = new javax.swing.JDialog();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaAjuda = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabelData = new javax.swing.JLabel();
        jLabelHora = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabelLogin = new javax.swing.JLabel();
        jLabelFoto = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenuCadastro = new javax.swing.JMenu();
        jMenuItemCadastrarCli = new javax.swing.JMenuItem();
        jMenuItemCadastrarProduto = new javax.swing.JMenuItem();
        jMenuConsulta = new javax.swing.JMenu();
        jMenuItemConsultarCliente = new javax.swing.JMenuItem();
        jMenuItemConsultarProduto = new javax.swing.JMenuItem();
        jMenuVariavel = new javax.swing.JMenu();
        jMenuItemRelatorioCaixa = new javax.swing.JMenuItem();
        jMenuVendas = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuLoginLogout = new javax.swing.JMenu();
        jMenuAjuda = new javax.swing.JMenu();
        jMenuItemAjuda = new javax.swing.JMenuItem();
        jMenuItemSobre = new javax.swing.JMenuItem();

        jDialogConsulta.setModal(true);
        jDialogConsulta.setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel1.setText("Codigo: ");

        jLabelNameConsulta.setText("Nome:");

        jTextFieldNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldNomeActionPerformed(evt);
            }
        });
        jTextFieldNome.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldNomeKeyReleased(evt);
            }
        });

        jFormattedTextFieldCod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextFieldCodActionPerformed(evt);
            }
        });

        jButton1.setText(">");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jFormattedTextFieldCod, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelNameConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jTextFieldNome, javax.swing.GroupLayout.DEFAULT_SIZE, 323, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jFormattedTextFieldCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1)
                    .addComponent(jTextFieldNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNameConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTableConsulta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codigo", "Nome", "CPF"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableConsulta.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableConsultaMouseClicked(evt);
            }
        });
        jTableConsulta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTableConsultaKeyReleased(evt);
            }
        });
        jScrollPane2.setViewportView(jTableConsulta);

        jButton2.setText("Todos");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Nome");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("Codigo");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jDialogConsultaLayout = new javax.swing.GroupLayout(jDialogConsulta.getContentPane());
        jDialogConsulta.getContentPane().setLayout(jDialogConsultaLayout);
        jDialogConsultaLayout.setHorizontalGroup(
            jDialogConsultaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogConsultaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialogConsultaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 610, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialogConsultaLayout.createSequentialGroup()
                        .addComponent(jRadioButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jRadioButton2)
                        .addGap(96, 96, 96)
                        .addComponent(jButton2)))
                .addContainerGap())
        );
        jDialogConsultaLayout.setVerticalGroup(
            jDialogConsultaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogConsultaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jDialogConsultaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        timer1.addTimerListener(new org.netbeans.examples.lib.timerbean.TimerListener() {
            public void onTime(java.awt.event.ActionEvent evt) {
                timer1OnTime(evt);
            }
        });

        jDialogAjuda.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jDialogAjuda.setTitle("Sistema Vania Modas - Ajuda");
        jDialogAjuda.setModal(true);
        jDialogAjuda.setResizable(false);

        jTextAreaAjuda.setColumns(20);
        jTextAreaAjuda.setEditable(false);
        jTextAreaAjuda.setRows(5);
        jTextAreaAjuda.setFocusable(false);
        jScrollPane1.setViewportView(jTextAreaAjuda);

        javax.swing.GroupLayout jDialogAjudaLayout = new javax.swing.GroupLayout(jDialogAjuda.getContentPane());
        jDialogAjuda.getContentPane().setLayout(jDialogAjudaLayout);
        jDialogAjudaLayout.setHorizontalGroup(
            jDialogAjudaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogAjudaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 528, Short.MAX_VALUE)
                .addContainerGap())
        );
        jDialogAjudaLayout.setVerticalGroup(
            jDialogAjudaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogAjudaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 354, Short.MAX_VALUE)
                .addContainerGap())
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("VANIA MODAS CASCAVEL");
        setResizable(false);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jPanel3.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabelHora.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jLabel12.setText("Usuario:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabelLogin, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelData, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabelHora, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel12)
                        .addComponent(jLabelLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabelData, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 17, Short.MAX_VALUE)
                    .addComponent(jLabelHora, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 17, Short.MAX_VALUE))
                .addContainerGap())
        );

        jLabelFoto.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabelFoto, javax.swing.GroupLayout.DEFAULT_SIZE, 925, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(jLabelFoto, javax.swing.GroupLayout.DEFAULT_SIZE, 612, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jMenuCadastro.setMnemonic('a');
        jMenuCadastro.setText("Cadastro");

        jMenuItemCadastrarCli.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        jMenuItemCadastrarCli.setText("Cliente");
        jMenuItemCadastrarCli.setRolloverEnabled(true);
        jMenuItemCadastrarCli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCadastrarCliActionPerformed(evt);
            }
        });
        jMenuCadastro.add(jMenuItemCadastrarCli);

        jMenuItemCadastrarProduto.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        jMenuItemCadastrarProduto.setText("Produto");
        jMenuItemCadastrarProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemCadastrarProdutoActionPerformed(evt);
            }
        });
        jMenuCadastro.add(jMenuItemCadastrarProduto);

        jMenuBar1.add(jMenuCadastro);

        jMenuConsulta.setMnemonic('o');
        jMenuConsulta.setText("Consulta");

        jMenuItemConsultarCliente.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, 0));
        jMenuItemConsultarCliente.setText("Cliente");
        jMenuItemConsultarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemConsultarClienteActionPerformed(evt);
            }
        });
        jMenuConsulta.add(jMenuItemConsultarCliente);

        jMenuItemConsultarProduto.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F7, 0));
        jMenuItemConsultarProduto.setText("Produto");
        jMenuItemConsultarProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemConsultarProdutoActionPerformed(evt);
            }
        });
        jMenuConsulta.add(jMenuItemConsultarProduto);

        jMenuBar1.add(jMenuConsulta);

        jMenuVariavel.setMnemonic('r');
        jMenuVariavel.setText("Relatorios");

        jMenuItemRelatorioCaixa.setText("Caixa do dia");
        jMenuItemRelatorioCaixa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemRelatorioCaixaActionPerformed(evt);
            }
        });
        jMenuVariavel.add(jMenuItemRelatorioCaixa);

        jMenuBar1.add(jMenuVariavel);

        jMenuVendas.setMnemonic('v');
        jMenuVendas.setText("Vendas");
        jMenuVendas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuVendasMousePressed(evt);
            }
        });

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F11, 0));
        jMenuItem1.setText("Venda a vista");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenuVendas.add(jMenuItem1);
        jMenuVendas.add(jSeparator2);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F12, 0));
        jMenuItem2.setText("Venda a prazo");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenuVendas.add(jMenuItem2);

        jMenuBar1.add(jMenuVendas);

        jMenuLoginLogout.setText("Login/Logout");
        jMenuLoginLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuLoginLogoutMousePressed(evt);
            }
        });
        jMenuLoginLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuLoginLogoutActionPerformed(evt);
            }
        });
        jMenuBar1.add(jMenuLoginLogout);

        jMenuAjuda.setText("Ajuda");

        jMenuItemAjuda.setText("Conteudos da Ajuda");
        jMenuItemAjuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemAjudaActionPerformed(evt);
            }
        });
        jMenuAjuda.add(jMenuItemAjuda);

        jMenuItemSobre.setText("Sobre");
        jMenuItemSobre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItemSobreActionPerformed(evt);
            }
        });
        jMenuAjuda.add(jMenuItemSobre);

        jMenuBar1.add(jMenuAjuda);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void alinhaColunas() {
        cellRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        jTableConsulta.getColumnModel().getColumn(2).setCellRenderer(cellRenderer);
    }

    private void limpaTabelaConsulta() {
        modelo = (DefaultTableModel) jTableConsulta.getModel();
        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }
        jTableConsulta.setModel(modelo);
    }

    public void setRetorno(String retorno) {
        this.retorno = retorno;
    }

    private void listarTodos(String ordem) {
        HashMap<String, Object> dados = new HashMap<String, Object>();
        if (tipoConsulta.equals("CLIENTE") || tipoConsulta.equals("FUNCIONARIO")) {
            try {
                dados.put("modelo", tipoConsulta.toLowerCase());
                dados.put("operacao", "ordenaLista");
                if (ordem.equals("nome")) {
                    dados.put("classificacao", "ordem");
                } else {
                    dados.put("classificacao", "desordem");
                }
                dados = controlador.recebeOperacao(dados);
                l = (List) dados.get("lista");
                if (l.size() > 0) {
                    this.limpaTabelaConsulta();
                    for (int i = 0; i < l.size(); i++) {
                        dados = (HashMap<String, Object>) l.get(i);
                        if (dados.get("ativo").equals("true")) {
                            Object[] linha = new Object[]{((String) dados.get("cod")), ((String) dados.get("nome")), ((String) dados.get("cpf"))};
                            modelo.addRow(linha);
                        }
                    }
                    //jTableConsulta.setModel(modelo);
                    if (!((String) dados.get("retorno")).equals("sucesso")) {
                        JOptionPane.showMessageDialog(this, "Nenhum " + tipoConsulta.toLowerCase() + " foi encontrado com este codigo.");
                        this.preencheTabelaConsulta();
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao consultar " + tipoConsulta.toLowerCase() + ".");
                this.limpaTabelaConsulta();
            }
        } else {
            if (tipoConsulta.compareTo("PRODUTO") == 0) {
                try {
                    dados.put("modelo", tipoConsulta.toLowerCase());
                    dados.put("operacao", "ordenaLista");
                    if (ordem.equals("nome")) {
                        dados.put("classificacao", "ordem");
                    } else {
                        dados.put("classificacao", "desordem");
                    }
                    dados = controlador.recebeOperacao(dados);
                    l = (List) dados.get("lista");
                    if (l.size() > 0) {
                        this.limpaTabelaConsulta();
                        for (int i = 0; i < l.size(); i++) {
                            dados = (HashMap<String, Object>) l.get(i);
                            if (dados.get("ativo").equals("true")) {
                                Object[] linha = new Object[]{((String) dados.get("cod")), ((String) dados.get("desc")), ((String) dados.get("valorVenda"))};
                                modelo.addRow(linha);
                            }
                        }
                        if (!((String) dados.get("retorno")).equals("sucesso")) {
                            JOptionPane.showMessageDialog(this, "Nenhum " + tipoConsulta.toLowerCase() + " foi encontrado com este codigo.");
                            this.preencheTabelaConsulta();
                        }
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao consultar " + tipoConsulta.toLowerCase() + ".");
                    this.limpaTabelaConsulta();
                }
            } else {
                if (tipoConsulta.compareTo("CAIXA") == 0) {
                    try {
                        dados.put("modelo", this.tipoConsulta.toLowerCase());
                        dados.put("operacao", "ordenaLista");
                        if (ordem.equals("nome")) {
                            dados.put("classificacao", "ordem");
                        } else {
                            dados.put("classificacao", "desordem");
                        }
                        dados = controlador.recebeOperacao(dados);
                        l = (List) dados.get("lista");
                        if (l.size() > 0) {
                            this.limpaTabelaConsulta();
                            for (int i = 0; i < l.size(); i++) {
                                dados = (HashMap<String, Object>) l.get(i);
                                String c2, c3;
                                if (this.tipoConsulta.equals("CAIXA")) {
                                    double s = Double.parseDouble(dados.get("valorTotalSaida") + ""), e = Double.parseDouble(dados.get("valorTotalEntrada") + "");
                                    s = e - s;
                                    c2 = ((String) dados.get("data"));
                                    c3 = this.trataPreco(s + "");
                                    Object[] linha = new Object[]{((String) dados.get("cod")), c2, c3};
                                    modelo.addRow(linha);
                                }
                            }
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Erro ao consultar " + tipoConsulta.toLowerCase() + ".");
                        this.limpaTabelaConsulta();
                    }
                }
            }
        }
    }

    private void preencheTabelaConsulta() {
        HashMap<String, Object> dados = new HashMap<String, Object>();
        String coluna2 = "";
        String coluna3 = "";
        if (this.tipoConsulta.equals("CLIENTE") || this.tipoConsulta.equals("FUNCIONARIO")) {
            coluna2 = "nome";
            coluna3 = "cpf";
            jLabelNameConsulta.setText("Nome:");
        } else {
            if (this.tipoConsulta.equals("PRODUTO")) {
                coluna2 = "desc";
                coluna3 = "valorVenda";
                jLabelNameConsulta.setText("Nome:");
            } else {
                if (this.tipoConsulta.equals("CAIXA")) {
                    coluna2 = "data";
                    jLabelNameConsulta.setText("Data:");
                }
            }
        }
        try {
            dados.put("modelo", this.tipoConsulta.toLowerCase());
            dados.put("operacao", "ordenaLista");
            dados.put("classificacao", "ordem");
            dados = controlador.recebeOperacao(dados);
            l = (List) dados.get("lista");
            if (l.size() > 0) {
                this.limpaTabelaConsulta();
                for (int i = 0; i < l.size(); i++) {
                    dados = (HashMap<String, Object>) l.get(i);
                    String c2, c3;
                    if (this.tipoConsulta.equals("CAIXA")) {
                        double s = Double.parseDouble(dados.get("valorTotalSaida") + ""), e = Double.parseDouble(dados.get("valorTotalEntrada") + "");
                        s = e - s;
                        c2 = ((String) dados.get("data"));
                        c3 = this.trataPreco(s + "");
                        Object[] linha = new Object[]{((String) dados.get("cod")), c2, c3};
                        modelo.addRow(linha);
                    } else {
                        if (dados.get("ativo").equals("true")) {
                            c2 = ((String) dados.get(coluna2));
                            c3 = ((String) dados.get(coluna3));
                            Object[] linha = new Object[]{((String) dados.get("cod")), c2, c3};
                            modelo.addRow(linha);
                        }
                    }

                }
            }
        } catch (Exception ex) {
            this.limpaTabelaConsulta();
        }
    }

private void timer1OnTime(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timer1OnTime
    DataSistema d = DataSistema.getInstance();
    String g = d.diaMesAno();
    jLabelData.setText(g);
    String h = d.horaMinSeg();
    jLabelHora.setText(h);
}//GEN-LAST:event_timer1OnTime

private void jMenuItemCadastrarCliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCadastrarCliActionPerformed
    new CadastroClienteFun(sessao).show();
    this.dispose();
}//GEN-LAST:event_jMenuItemCadastrarCliActionPerformed

private void jMenuItemCadastrarProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemCadastrarProdutoActionPerformed
    new CadastroProdutoFun(sessao).show();
    this.dispose();
}//GEN-LAST:event_jMenuItemCadastrarProdutoActionPerformed

private void jMenuItemConsultarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemConsultarClienteActionPerformed
    this.jDialogConsulta.setSize(800, 405);
    this.jDialogConsulta.setLocationRelativeTo(this);
    this.jDialogConsulta.setTitle("CONSULTA CLIENTE");
    l = new ArrayList();
    jTableConsulta.getColumnModel().getColumn(0).setPreferredWidth(3);
    jTableConsulta.getColumnModel().getColumn(1).setPreferredWidth(400);
    jTableConsulta.getColumnModel().getColumn(2).setPreferredWidth(60);
    jTableConsulta.getTableHeader().setReorderingAllowed(false);


    this.limpaTabelaConsulta();
    this.alinhaColunas();
    this.tipoConsulta = "CLIENTE";
    this.preencheTabelaConsulta();
    jTableConsulta.getColumnModel().getColumn(2).setHeaderValue("CPF");
    this.jDialogConsulta.setVisible(true);
}//GEN-LAST:event_jMenuItemConsultarClienteActionPerformed

private void jMenuItemConsultarProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemConsultarProdutoActionPerformed
    this.jDialogConsulta.setSize(800, 405);
    this.jDialogConsulta.setLocationRelativeTo(this);
    this.jDialogConsulta.setTitle("CONSULTA PRODUTO");
    l = new ArrayList();
    jTableConsulta.getColumnModel().getColumn(0).setPreferredWidth(3);
    jTableConsulta.getColumnModel().getColumn(1).setPreferredWidth(370);
    jTableConsulta.getColumnModel().getColumn(2).setPreferredWidth(50);
    jTableConsulta.getTableHeader().setReorderingAllowed(false);

    this.limpaTabelaConsulta();
    this.alinhaColunas();
    this.tipoConsulta = "PRODUTO";
    this.preencheTabelaConsulta();
    jTableConsulta.getColumnModel().getColumn(2).setHeaderValue("VALOR");
    this.jDialogConsulta.setVisible(true);
}//GEN-LAST:event_jMenuItemConsultarProdutoActionPerformed

private void jMenuVendasMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuVendasMousePressed
}//GEN-LAST:event_jMenuVendasMousePressed

private void jTextFieldNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldNomeActionPerformed
    this.consultaNome();
}//GEN-LAST:event_jTextFieldNomeActionPerformed

private void jTextFieldNomeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldNomeKeyReleased
    this.consultaNome();
}//GEN-LAST:event_jTextFieldNomeKeyReleased

private void jFormattedTextFieldCodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextFieldCodActionPerformed
    this.jTextFieldNome.setText("");
    this.consultaCodigo();
}//GEN-LAST:event_jFormattedTextFieldCodActionPerformed

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    this.consultaCodigo();
}//GEN-LAST:event_jButton1ActionPerformed

private void jTableConsultaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableConsultaMouseClicked
    if (evt.getClickCount() == 2) {
        this.mostrarTela();
    }
}//GEN-LAST:event_jTableConsultaMouseClicked

private void jTableConsultaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableConsultaKeyReleased
    if (evt.getKeyCode() == KeyEvent.VK_C) {
        modelo = (DefaultTableModel) jTableConsulta.getModel();
        if (modelo.getRowCount() > 0) {
            this.mostrarTela();
        }
    }
}//GEN-LAST:event_jTableConsultaKeyReleased

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    int resposta = JOptionPane.showConfirmDialog(null, "Ordenar por nome?", "Ordenar", JOptionPane.YES_NO_OPTION);
    if (resposta == 0) {
        this.listarTodos("nome");
    } else {
        this.listarTodos("cod");
    }

}//GEN-LAST:event_jButton2ActionPerformed

private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
    this.limpaTabelaConsulta();
    if (jRadioButton1.isSelected()) {
        this.listarTodos("nome");
    }
}//GEN-LAST:event_jRadioButton1ActionPerformed

private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
    this.limpaTabelaConsulta();
    if (jRadioButton2.isSelected()) {
        this.listarTodos("cod");
    }
}//GEN-LAST:event_jRadioButton2ActionPerformed

private void jMenuLoginLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuLoginLogoutActionPerformed
}//GEN-LAST:event_jMenuLoginLogoutActionPerformed

private void jMenuLoginLogoutMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuLoginLogoutMousePressed
    new TelaLogin().show();
    this.dispose();
}//GEN-LAST:event_jMenuLoginLogoutMousePressed

private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
    TelaVendaFun a = new TelaVendaFun(sessao, "A VISTA");
    a.show();
    this.dispose();
}//GEN-LAST:event_jMenuItem1ActionPerformed

private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
    TelaVendaFun a = new TelaVendaFun(sessao, "A PRAZO");
    a.show();
    this.dispose();
}//GEN-LAST:event_jMenuItem2ActionPerformed

private void jMenuItemRelatorioCaixaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemRelatorioCaixaActionPerformed
    TelaCaixaFun a = new TelaCaixaFun(sessao, null);
    a.show();
    this.dispose();
}//GEN-LAST:event_jMenuItemRelatorioCaixaActionPerformed

private void jMenuItemAjudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemAjudaActionPerformed
    this.jDialogAjuda.setSize(600, 600);
    this.jDialogAjuda.setLocationRelativeTo(this);
    this.jDialogAjuda.setTitle("Sistema Vania Modas - Ajuda");
    try {
        BufferedReader leitor = new BufferedReader(new FileReader("arquivos/ajuda/ajuda.txt"));
        String linha = null;
        while ((linha = leitor.readLine()) != null) {
            jTextAreaAjuda.append(linha+"\n");
        }
        leitor.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
    this.jDialogAjuda.setVisible(true);
}//GEN-LAST:event_jMenuItemAjudaActionPerformed

private void jMenuItemSobreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSobreActionPerformed
    String string = "USB Informatica\n";
    string += "\n";
    string += "HEMERSON LEANDRO SIQUEIRA CARLIN\n";
    string += "JEFFERSON ADRIANO DO ROSARIO\n";
    JOptionPane.showMessageDialog(null, string, "Sobre", JOptionPane.INFORMATION_MESSAGE);
}//GEN-LAST:event_jMenuItemSobreActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new TelaPrincipalFun().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JDialog jDialogAjuda;
    private javax.swing.JDialog jDialogConsulta;
    private javax.swing.JFormattedTextField jFormattedTextFieldCod;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabelData;
    private javax.swing.JLabel jLabelFoto;
    private javax.swing.JLabel jLabelHora;
    private javax.swing.JLabel jLabelLogin;
    private javax.swing.JLabel jLabelNameConsulta;
    private javax.swing.JMenu jMenuAjuda;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuCadastro;
    private javax.swing.JMenu jMenuConsulta;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItemAjuda;
    private javax.swing.JMenuItem jMenuItemCadastrarCli;
    private javax.swing.JMenuItem jMenuItemCadastrarProduto;
    private javax.swing.JMenuItem jMenuItemConsultarCliente;
    private javax.swing.JMenuItem jMenuItemConsultarProduto;
    private javax.swing.JMenuItem jMenuItemRelatorioCaixa;
    private javax.swing.JMenuItem jMenuItemSobre;
    private javax.swing.JMenu jMenuLoginLogout;
    private javax.swing.JMenu jMenuVariavel;
    private javax.swing.JMenu jMenuVendas;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable jTableConsulta;
    private javax.swing.JTextArea jTextAreaAjuda;
    private javax.swing.JTextField jTextFieldNome;
    private org.netbeans.examples.lib.timerbean.Timer timer1;
    // End of variables declaration//GEN-END:variables

    private void consultaNome() {
        if (jTextFieldNome.getText().length() > 0) {
            HashMap<String, Object> dados = new HashMap<String, Object>();
            String digitado = jTextFieldNome.getText();
            if (tipoConsulta.equals("CLIENTE") || tipoConsulta.equals("FUNCIONARIO")) {
                l = new ArrayList();
                if (digitado.compareTo("") != 0) {
                    dados.put("modelo", tipoConsulta.toLowerCase());
                    dados.put("operacao", "ordenaLista");
                    dados.put("classificacao", "ordem");
                    dados = controlador.recebeOperacao(dados);
                    l = (List) dados.get("lista");
                    if (l.size() > 0) {
                        this.limpaTabelaConsulta();
                        for (int i = 0; i < l.size(); i++) {
                            dados = (HashMap<String, Object>) l.get(i);
                            if (dados.get("ativo").equals("true")) {
                                CharSequence charSeq = digitado.toUpperCase();
                                if (((String) dados.get("nome")).contains(charSeq)) {
                                    Object[] linha = new Object[]{((String) dados.get("cod")), ((String) dados.get("nome")), ((String) dados.get("cpf"))};
                                    modelo.addRow(linha);
                                }
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Nenhum registro encontrado!");
                    }
                } else {
                    this.limpaTabelaConsulta();
                }
            } else {
                if (tipoConsulta.compareTo("PRODUTO") == 0) {
                    l = new ArrayList();
                    dados.put("modelo", tipoConsulta.toLowerCase());
                    dados.put("operacao", "ordenaLista");
                    dados.put("classificacao", "ordem");
                    dados = controlador.recebeOperacao(dados);
                    l = (List) dados.get("lista");
                    if (l.size() > 0) {
                        this.limpaTabelaConsulta();
                        for (int i = 0; i < l.size(); i++) {
                            dados = (HashMap<String, Object>) l.get(i);
                            if (dados.get("ativo").equals("true")) {
                                CharSequence charSeq = digitado.toUpperCase();
                                if (((String) dados.get("desc")).contains(charSeq)) {
                                    Object[] linha = new Object[]{((String) dados.get("cod")), ((String) dados.get("desc")), ((String) dados.get("valorVenda"))};
                                    modelo.addRow(linha);
                                }
                            }
                        }
                    } else {
                        this.limpaTabelaConsulta();
                    }
                } else {
                    if (tipoConsulta.compareTo("CAIXA") == 0) {
                        l = new ArrayList();
                        dados.put("modelo", tipoConsulta.toLowerCase());
                        dados.put("operacao", "ordenaLista");
                        dados.put("classificacao", "ordem");
                        dados = controlador.recebeOperacao(dados);
                        l = (List) dados.get("lista");
                        if (l.size() > 0) {
                            this.limpaTabelaConsulta();
                            for (int i = 0; i < l.size(); i++) {
                                dados = (HashMap<String, Object>) l.get(i);
                                if (dados.get("ativo").equals("true")) {
                                    CharSequence charSeq = digitado.toUpperCase();
                                    if (((String) dados.get("data")).contains(charSeq)) {
                                        double s = Double.parseDouble(dados.get("valorTotalSaida") + ""), e = Double.parseDouble(dados.get("valorTotalEntrada") + "");
                                        s = e - s;
                                        Object[] linha = new Object[]{((String) dados.get("cod")), ((String) dados.get("data")), this.trataPreco(s + "")};
                                        modelo.addRow(linha);
                                    }
                                }
                            }
                        } else {
                            this.limpaTabelaConsulta();
                        }

                    }
                }
            }
        } else {
            this.preencheTabelaConsulta();
        }
    }

    private void contrutor() {
        timer1.start();
        try {
            UIManager.setLookAndFeel(looks[3].getClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        jLabelFoto.setIcon(new ImageIcon("arquivos/logo.jpg"));

        jMenuCadastro.requestFocus();
    }

    private void povoaBD() {
        DAO dao;
        dao = new DAOClienteDB4O();
        dao.conectar();
        dao.limpa();
        dao.desconectar();
        dao = new DAOFuncionarioDB4O();
        dao.conectar();
        dao.limpa();
        dao.desconectar();
        dao = new DAOProdutoDB4O();
        dao.conectar();
        dao.limpa();
        dao.desconectar();
        dao = new DAOVendaDB4O();
        dao.conectar();
        dao.limpa();
        dao.desconectar();
        String diretorio = "";
        HashMap<String, Object> dados = new HashMap<String, Object>();
        int cod = 0;
        for (int i = 0; i < 3; i++) {
            if (i == 0) {
                diretorio = "arquivos/povoamento/produto.txt";
                cod = 0;
            } else {
                if (i == 1) {
                    diretorio = "arquivos/povoamento/cliente.txt";
                    cod = 0;
                } else {
                    diretorio = "arquivos/povoamento/funcionario.txt";
                    cod = 0;
                }
            }

            File arquivo = new File(diretorio); //cria novo arquivo
            if (arquivo.exists()) {
                try {
                    FileReader reader = new FileReader(diretorio); //leitura no arquivo
                    BufferedReader leitor = new BufferedReader(reader);
                    String linha = null;
                    try {
                        while (!(linha = leitor.readLine()).equals("FIM")) {
                            if (i == 0) { //produto
                                dados = new HashMap<String, Object>();
                                dados.put("modelo", "produto");
                                dados.put("operacao", "inserir");
                                cod++;
                                dados.put("cod", "" + cod);
                                dados.put("dataTermino", "");
                                dados.put("desc", linha.toUpperCase());
                                linha = leitor.readLine();
                                dados.put("qtdeEntrada", linha);
                                linha = leitor.readLine();
                                dados.put("valorVenda", this.trataPreco(linha));
                                linha = leitor.readLine();
                                dados.put("valorCompra", this.trataPreco(linha));
                                linha = leitor.readLine();
                                dados.put("nomeFornecedor", linha.toUpperCase());
                                dados.put("contatoFornecedor", "(00)0000-0000");
                                dados.put("dataCadastro", DataSistema.getInstance().diaMesAno().split(" ")[1]);
                                dados.put("dataEntrada", DataSistema.getInstance().diaMesAno().split(" ")[1]);
                                dados.put("qtdeVendida", "0");
                                dados.put("ativo", "true");
                                dados = controlador.recebeOperacao(dados);
                            } else {
                                if (i == 1) { //cliente
                                    dados = new HashMap<String, Object>();
                                    dados.put("modelo", "cliente");
                                    dados.put("operacao", "inserir");
                                    cod++;
                                    dados.put("cod", cod + "");
                                    dados.put("nome", linha.toUpperCase());
                                    linha = leitor.readLine();
                                    dados.put("cpf", linha.toUpperCase());
                                    dados.put("dataCadastro", DataSistema.getInstance().diaMesAno().split(" ")[1]);
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
                                    dados.put("trabalho", "NOME DA EMPRESA");
                                    dados.put("funcao", "FUNCAO");
                                    dados.put("salario", "1500.00");
                                    dados.put("tipoLogradouroTrabalho", "Rua");
                                    dados.put("nomeLogradouroTrabalho", "NOME DA RUA");
                                    dados.put("numLogradouroTrabalho", "0");
                                    dados.put("complementoTrabalho", "");
                                    dados.put("bairroTrabalho", "BAIRRO");
                                    dados.put("telefoneTrabalho", "(45)3333-3333");
                                    dados.put("dataAdmissao", "00/00/0000");
                                    dados.put("nomeConjuge", "NOME DO CONJUGE");
                                    dados.put("nascConjuge", "15/11/1980");
                                    dados.put("rgConjuge", "456");
                                    dados.put("cpfConjuge", "051.887.609-83");
                                    dados.put("sexoConjuge", "Feminino");
                                    dados.put("celularConjuge", "(45)3333-3333");
                                    dados.put("nomeInfPessoal1", "PESSOAL 1");
                                    dados.put("foneInfPessoal1", "(45)3333-3333");
                                    dados.put("nomeInfPessoal2", "PESSOAL 2");
                                    dados.put("foneInfPessoal2", "(45)3333-3333");
                                    dados.put("refCom1", "COMERCIAL 1" + "#" + "(45)3333-3333");
                                    dados.put("refCom2", "COMERCIAL 2" + "#" + "(45)3333-3333");
                                    dados.put("refCom3", "COMERCIAL 3" + "#" + "(45)3333-3333");
                                    dados.put("cadastroAprovado", "true");
                                    dados.put("observacao", "");
                                    dados.put("compras", "");
                                    dados.put("ativo", "true");
                                    dados = controlador.recebeOperacao(dados);
                                } else { //funcionario
                                    dados = new HashMap<String, Object>();
                                    dados.put("modelo", "funcionario");
                                    dados.put("operacao", "inserir");
                                    cod++;
                                    dados.put("cod", cod + "");
                                    String nome = linha.toUpperCase();
                                    dados.put("nome", nome);
                                    linha = leitor.readLine();
                                    dados.put("cpf", linha.toUpperCase());
                                    dados.put("dataCadastro", DataSistema.getInstance().diaMesAno().split(" ")[1]);
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
                                    dados = controlador.recebeOperacao(dados);
                                }
                            }
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(TelaPrincipalFun.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        leitor.close();
                        try {
                            reader.close();
                        } catch (IOException ex) {
                            Logger.getLogger(TelaPrincipalFun.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(TelaPrincipalFun.class.getName()).log(Level.SEVERE, null, ex);
                    }

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(TelaPrincipalFun.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        System.out.println("Banco povoado!");
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
}
