package visaoGerente;

import controlador.Controlador;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import utilitarios.CPF;
import utilitarios.DataSistema;
import utilitarios.Sessao;

/**
 *
 * @author Hemerson e Jefferson
 */
public class CadastroClienteGer extends javax.swing.JFrame {

    boolean ordem = true;
    private UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
    boolean cadastrar = true;
    Controlador controlador;
    boolean cpfValido = false;
    boolean nascValido = false;
    DefaultTableModel modelo;
    Sessao sessao;
    DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();

    /** Creates new form CadastroCliente */
    public CadastroClienteGer() {
        initComponents();
        sessao = null;
        timer1.start();
    }

    public CadastroClienteGer(Sessao sessao) {
        initComponents();
        this.construtor(sessao);
        jTextFieldCodigoCliente.setText(this.getMaiorId());

    }

    public CadastroClienteGer(HashMap<String, Object> c, Sessao sessao) {
        initComponents();
        this.construtor(sessao);
        this.mostraDados(c);
        this.modificaConsultar();
    }

    private String cadastraCliente() {
        if (jTextFieldNomeCliente.getText().compareTo("") != 0) {
            HashMap<String, Object> dadosCliente = new HashMap(this.pegaDados());
            this.validaCPF(jFormattedTextFieldCPFCliente.getText(), "cliente");
            if (cadastrar) {
                if (nascValido) {
                    if (cpfValido) {
                        HashMap<String, Object> dados = new HashMap<String, Object>();
                        dados.put("nome", jTextFieldNomeCliente.getText().toUpperCase());
                        dados.put("cpf", (String) jFormattedTextFieldCPFCliente.getValue());
                        dados.put("operacao", "consultar");
                        dados.put("modelo", "cliente");
                        HashMap<String, Object> retorno = controlador.recebeOperacao(dados);
                        if (retorno != null) {
                            JOptionPane.showMessageDialog(null, "Este cliente ja esta cadastrado" +
                                    " na base de dados do sistema!");
                            dadosCliente = null;
                        } else {
                            dadosCliente = new HashMap<String, Object>(this.pegaDados());
                            dadosCliente.put("operacao", "inserir");
                            dadosCliente.put("modelo", "cliente");
                            dados = controlador.recebeOperacao(dadosCliente);
                            JOptionPane.showMessageDialog(null, (String) dados.get("retorno"));
                            return "sucesso";
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Digite um cpf valido para o novo cliente!");
                        jFormattedTextFieldCPFCliente.grabFocus();
                        jTabbedPane1.setSelectedIndex(0);
                        return "";
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Cliente com idade inferior a 16anos!");
                    jFormattedTextFieldNascCliente.grabFocus();
                    jTabbedPane1.setSelectedIndex(0);
                    return "";
                }
            } else {
                dadosCliente = this.pegaDados();
                dadosCliente.put("operacao", "alterar");
                dadosCliente.put("modelo", "cliente");
                dadosCliente = controlador.recebeOperacao(dadosCliente);
                JOptionPane.showMessageDialog(null, (String) dadosCliente.get("retorno"));
                return "sucesso";
            }

        } else {
            JOptionPane.showMessageDialog(null, "Digite um nome para o novo cliente!");
            jTextFieldNomeCliente.grabFocus();
            return "";
        }
        return "";
    }

    private String getMaiorId() {
        HashMap<String, Object> dados = new HashMap<String, Object>();
        dados.put("modelo", "cliente");
        dados.put("operacao", "maiorId");
        dados = controlador.recebeOperacao(dados);
        if (dados != null) {
            return (String) dados.get("retorno");
        }
        return "";
    }

    private HashMap pegaDados() {
        HashMap<String, Object> dadosCliente = new HashMap<String, Object>();
        dadosCliente.put("nome", jTextFieldNomeCliente.getText().toUpperCase());
        dadosCliente.put("cpf", (String) jFormattedTextFieldCPFCliente.getValue());
        String aux = "";
        try {
            aux = jTextFieldCodigoCliente.getText();
        } catch (Exception exception) {
            aux = "1";
        }
        dadosCliente.put("cod", aux);
        aux = (String) jFormattedTextFieldDataCadastro.getValue();
        if (aux == null) {
            aux = "00/00/0000";
        }
        dadosCliente.put("dataCadastro", aux);

        // ------------- DADOS PESSOAIS -----------------

        aux = (String) jFormattedTextFieldNascCliente.getValue();
        if (aux == null) {
            aux = "00/00/0000";
        }
        dadosCliente.put("dataNasc", aux);
        dadosCliente.put("rg", jTextFieldRGCliente.getText());
        dadosCliente.put("sexo", (String) jComboBoxSexoCliente.getSelectedItem());
        aux = (String) jFormattedTextFieldFoneCliente.getValue();
        if (aux == null) {
            aux = "(00)0000-0000";
        }
        dadosCliente.put("telefone", aux);
        aux = (String) jFormattedTextFieldCelCliente.getValue();
        if (aux == null) {
            aux = "(00)0000-0000";
        }
        dadosCliente.put("celular", aux);
        dadosCliente.put("estadoCivil", jTextFieldEstadoCivil.getText().toUpperCase());
        dadosCliente.put("email", jTextFieldEmail.getText().toLowerCase());

        // ------------- ENDERECO CLIENTE -----------------
        dadosCliente.put("tipoLogradouro", (String) jComboBoxLogradouro.getSelectedItem());
        dadosCliente.put("nomeLogradouro", jTextFieldNomeLogradouro.getText().toUpperCase());
        int auxInt = 0;
        try {
            auxInt = Integer.parseInt(jTextFieldNum.getText());
        } catch (Exception exception) {
            auxInt = 0;
        }
        dadosCliente.put("numLogradouro", "" + auxInt);
        dadosCliente.put("complemento", jTextFieldComplemento.getText().toUpperCase());
        dadosCliente.put("bairro", jTextFieldBairro.getText().toUpperCase());
        dadosCliente.put("estado", (String) jComboBoxUF.getSelectedItem());
        dadosCliente.put("cidade", (String) jComboBoxCidade.getSelectedItem());
        aux = (String) jFormattedTextFieldCEP.getValue();
        if (aux == null) {
            aux = "00000-000";
        }
        dadosCliente.put("cep", aux);
        if (!jCheckBoxCasaPropria.isSelected()) {
            dadosCliente.put("propria", "false");
            dadosCliente.put("valorAluguel", this.trataPreco(jTextFieldAluguel.getText()));
        } else {
            dadosCliente.put("propria", "true");
        }

        dadosCliente.put("tempoResidencia", jTextFieldTempoResidencia.getText().toUpperCase());

        // ------------- TRABALHO CLIENTE -----------------
        dadosCliente.put("trabalho", jTextFieldEmpresa.getText().toUpperCase());
        dadosCliente.put("funcao", jTextFieldFuncao.getText().toUpperCase());
        dadosCliente.put("salario", this.trataPreco(jTextFieldSalario.getText()));
        dadosCliente.put("tipoLogradouroTrabalho", (String) jComboBoxLogradouroTrabalho.getSelectedItem());
        dadosCliente.put("nomeLogradouroTrabalho", jTextFieldNomeLogradouroTrabalho.getText().toUpperCase());


        try {
            auxInt = Integer.parseInt(jTextFieldNumTrabalho.getText());
        } catch (Exception exception) {
            auxInt = 0;
        }
        dadosCliente.put("numLogradouroTrabalho", "" + auxInt);
        dadosCliente.put("complementoTrabalho", jTextFieldComplementoTrabalho.getText().toUpperCase());
        dadosCliente.put("bairroTrabalho", jTextFieldBairroTrabalho.getText().toUpperCase());
        aux = (String) jFormattedTextFieldFoneTrabalho.getValue();
        if (aux == null) {
            aux = "(00)0000-0000";
        }
        dadosCliente.put("telefoneTrabalho", aux);

        aux = jFormattedTextFieldDataAdmissao.getText();
        if (aux.compareTo("") == 0) {
            aux = "00/00/0000";
        }
        dadosCliente.put("dataAdmissao", aux);


        // ------------- CONJUGE -----------------
        dadosCliente.put("nomeConjuge", jTextFieldNomeConjuge.getText().toUpperCase());

        aux = jFormattedTextFieldNascConjuge.getText();
        if (aux.compareTo("") == 0) {
            aux = "00/00/0000";
        }

        dadosCliente.put("nascConjuge", aux);
        dadosCliente.put("rgConjuge", jTextFieldRGConjuge.getText());
        dadosCliente.put("cpfConjuge", (String) jFormattedTextFieldCPFConjuge.getValue());
        dadosCliente.put("sexoConjuge", (String) jComboBoxSexoConjuge.getSelectedItem());
        aux = (String) jFormattedTextFieldCelConjuge.getValue();
        if (aux == null) {
            aux = "(00)0000-0000";
        }
        dadosCliente.put("celularConjuge", aux);

        // ------------- INFORMACOES PESSOAIS -----------------
        dadosCliente.put("nomeInfPessoal1", jTextFieldInfPessoal1.getText().toUpperCase());
        aux = (String) jFormattedTextFieldFoneInfPessoal1.getValue();
        if (aux == null) {
            aux = "(00)0000-0000";
        }
        dadosCliente.put("foneInfPessoal1", aux);
        dadosCliente.put("nomeInfPessoal2", jTextFieldInfPessoal2.getText().toUpperCase());
        aux = (String) jFormattedTextFieldFoneInfPessoal2.getValue();
        if (aux == null) {
            aux = "(00)0000-0000";
        }
        dadosCliente.put("foneInfPessoal2", aux);


        // ------------- REFERENCIAS COMERCIAIS -----------------
        dadosCliente.put("refCom1", jTextFieldNomeRefCom1.getText().toUpperCase() + "#" + jFormattedTextFieldFoneRefCom1.getValue());
        dadosCliente.put("refCom2", jTextFieldNomeRefCom2.getText().toUpperCase() + "#" + jFormattedTextFieldFoneRefCom2.getValue());
        dadosCliente.put("refCom3", jTextFieldNomeRefCom3.getText().toUpperCase() + "#" + jFormattedTextFieldFoneRefCom3.getValue());
        if (jCheckBoxCadastroAprovado.isSelected()) {
            dadosCliente.put("cadastroAprovado", "true");
        } else {
            dadosCliente.put("cadastroAprovado", "false");
        }
        dadosCliente.put("observacao", jTextAreaObservacao.getText());
        if (cadastrar) {
            dadosCliente.put("compras", "");
        } else {
            dadosCliente.put("compras", this.pegaTabelaCompras());
        }
        dadosCliente.put("ativo", "true");
        return dadosCliente;
    }

    private void carregaCidades(String estado, int i) {
        /*
         * i = 1 = combobox UF Cliente
         * i = 2 = combobox UF trabalho do cliente
         */
        try {
            // cria objeto stream para entrada
            FileReader InFile = new FileReader("arquivos/cidades/" + estado + ".txt");
            BufferedReader in = new BufferedReader(InFile);
            String linha;
            Vector arquivo = new Vector();
            while ((linha = in.readLine()) != null) {
                arquivo.add(new String(linha));
            }
            in.close();
            if (i == 1) {
                this.jComboBoxCidade.removeAllItems();
                for (int j = 0; j < arquivo.size(); j++) {
                    this.jComboBoxCidade.addItem(arquivo.get(j));
                }
            }
            this.jComboBoxCidade.setSelectedItem("Cascavel");
        } catch (IOException erro) {
            System.out.println("Erro na abertura dos arquivos.");
        }
    }

    private void construtor(Sessao sessao) {
        //this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        controlador = new Controlador();
        this.setLocationRelativeTo(null);
        try {
            UIManager.setLookAndFeel(looks[3].getClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.mascaraCPF();
        this.mascaraTEL();
        this.mascaraData();
        this.mascaraCEP();
        DataSistema dataSistema = new DataSistema();
        String dataCadastro = dataSistema.diaMesAno();
        jFormattedTextFieldDataCadastro.setValue(dataCadastro.split(" ")[1]);
        this.jCheckBoxCasaPropria.setSelected(true);
        this.jCheckBoxCadastroAprovado.setSelected(false);
        this.cadastrar = true;
        this.cpfValido = false;
        this.nascValido = false;
        this.carregaCidades("PR", 2);
        this.carregaCidades("PR", 1);
        jTextFieldNomeCliente.requestFocus();
        jTabbedPane1.setSelectedIndex(0);
        jTableCompras.getTableHeader().setReorderingAllowed(false);
        this.alinhaColunas();
        jLabelLogin.setText(sessao.getLogin());
        this.sessao = new Sessao();
        this.sessao.setLogin(sessao.getLogin());
        this.sessao.setSenha(sessao.getSenha());
        jCheckBoxCadastroAprovado.setSelected(true);
        timer1.start();
    }

    private void alinhaColunas() {
        cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        jTableCompras.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
        jTableCompras.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
        jTableCompras.getColumnModel().getColumn(2).setCellRenderer(cellRenderer);
        jTableCompras.getColumnModel().getColumn(3).setCellRenderer(cellRenderer);
        jTableCompras.getColumnModel().getColumn(4).setCellRenderer(cellRenderer);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        timer1 = new org.netbeans.examples.lib.timerbean.Timer();
        jLabelCodigoCliente = new javax.swing.JLabel();
        jTextFieldCodigoCliente = new javax.swing.JTextField();
        jLabelNasc1 = new javax.swing.JLabel();
        jFormattedTextFieldDataCadastro = new javax.swing.JFormattedTextField();
        jCheckBoxCadastroAprovado = new javax.swing.JCheckBox();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jLabelNomeCli2 = new javax.swing.JLabel();
        jTextFieldNomeCliente = new javax.swing.JTextField();
        jLabelRG2 = new javax.swing.JLabel();
        jTextFieldRGCliente = new javax.swing.JTextField();
        jLabelCPF2 = new javax.swing.JLabel();
        jFormattedTextFieldCPFCliente = new javax.swing.JFormattedTextField();
        jComboBoxSexoCliente = new javax.swing.JComboBox();
        jLabelSexo2 = new javax.swing.JLabel();
        jFormattedTextFieldNascCliente = new javax.swing.JFormattedTextField();
        jLabelNasc3 = new javax.swing.JLabel();
        jLabelTelefone1 = new javax.swing.JLabel();
        jFormattedTextFieldFoneCliente = new javax.swing.JFormattedTextField();
        jLabelCelular1 = new javax.swing.JLabel();
        jFormattedTextFieldCelCliente = new javax.swing.JFormattedTextField();
        jTextFieldEstadoCivil = new javax.swing.JTextField();
        jLabelEstadoCivil1 = new javax.swing.JLabel();
        jLabelEmail1 = new javax.swing.JLabel();
        jTextFieldEmail = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabelComplemento1 = new javax.swing.JLabel();
        jTextFieldComplemento = new javax.swing.JTextField();
        jLabelCEP1 = new javax.swing.JLabel();
        jFormattedTextFieldCEP = new javax.swing.JFormattedTextField();
        jTextFieldBairro = new javax.swing.JTextField();
        jLabelBairro1 = new javax.swing.JLabel();
        jLabelUF1 = new javax.swing.JLabel();
        jComboBoxUF = new javax.swing.JComboBox();
        jLabelCidade1 = new javax.swing.JLabel();
        jComboBoxCidade = new javax.swing.JComboBox();
        jTextFieldAluguel = new javax.swing.JTextField();
        jLabelValorAluguel1 = new javax.swing.JLabel();
        jCheckBoxCasaPropria = new javax.swing.JCheckBox();
        jTextFieldTempoResidencia = new javax.swing.JTextField();
        jLabelTempoResidencia1 = new javax.swing.JLabel();
        jComboBoxLogradouro = new javax.swing.JComboBox();
        jTextFieldNomeLogradouro = new javax.swing.JTextField();
        jLabelNumTrabalho2 = new javax.swing.JLabel();
        jTextFieldNum = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabelEmpresa1 = new javax.swing.JLabel();
        jTextFieldEmpresa = new javax.swing.JTextField();
        jLabelFuncao1 = new javax.swing.JLabel();
        jTextFieldFuncao = new javax.swing.JTextField();
        jLabelSalario1 = new javax.swing.JLabel();
        jTextFieldSalario = new javax.swing.JTextField();
        jLabelDataAdmissao1 = new javax.swing.JLabel();
        jFormattedTextFieldDataAdmissao = new javax.swing.JFormattedTextField();
        jLabelBairroTrabalho1 = new javax.swing.JLabel();
        jTextFieldBairroTrabalho = new javax.swing.JTextField();
        jLabelFoneTrabalho1 = new javax.swing.JLabel();
        jFormattedTextFieldFoneTrabalho = new javax.swing.JFormattedTextField();
        jTextFieldComplementoTrabalho = new javax.swing.JTextField();
        jLabelComplemento2 = new javax.swing.JLabel();
        jTextFieldNumTrabalho = new javax.swing.JTextField();
        jLabelNumTrabalho3 = new javax.swing.JLabel();
        jComboBoxLogradouroTrabalho = new javax.swing.JComboBox();
        jTextFieldNomeLogradouroTrabalho = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabelNomeCli3 = new javax.swing.JLabel();
        jTextFieldNomeConjuge = new javax.swing.JTextField();
        jLabelRG3 = new javax.swing.JLabel();
        jTextFieldRGConjuge = new javax.swing.JTextField();
        jLabelCPF3 = new javax.swing.JLabel();
        jFormattedTextFieldCPFConjuge = new javax.swing.JFormattedTextField();
        jComboBoxSexoConjuge = new javax.swing.JComboBox();
        jLabelSexo3 = new javax.swing.JLabel();
        jFormattedTextFieldNascConjuge = new javax.swing.JFormattedTextField();
        jLabelNasc4 = new javax.swing.JLabel();
        jFormattedTextFieldCelConjuge = new javax.swing.JFormattedTextField();
        jLabelCelConjuge1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabelNomeRefCom1 = new javax.swing.JLabel();
        jTextFieldNomeRefCom1 = new javax.swing.JTextField();
        jFormattedTextFieldFoneRefCom1 = new javax.swing.JFormattedTextField();
        jLabelFoneRefCom1 = new javax.swing.JLabel();
        jLabelNomeRefCom4 = new javax.swing.JLabel();
        jTextFieldNomeRefCom2 = new javax.swing.JTextField();
        jLabelFoneRefCom4 = new javax.swing.JLabel();
        jFormattedTextFieldFoneRefCom2 = new javax.swing.JFormattedTextField();
        jLabelNomeRefCom5 = new javax.swing.JLabel();
        jTextFieldNomeRefCom3 = new javax.swing.JTextField();
        jLabelFoneRefCom5 = new javax.swing.JLabel();
        jFormattedTextFieldFoneRefCom3 = new javax.swing.JFormattedTextField();
        jPanel6 = new javax.swing.JPanel();
        jTextFieldInfPessoal1 = new javax.swing.JTextField();
        jLabelNomeRefCom2 = new javax.swing.JLabel();
        jFormattedTextFieldFoneInfPessoal1 = new javax.swing.JFormattedTextField();
        jLabelFoneRefCom2 = new javax.swing.JLabel();
        jTextFieldInfPessoal2 = new javax.swing.JTextField();
        jLabelNomeRefCom6 = new javax.swing.JLabel();
        jFormattedTextFieldFoneInfPessoal2 = new javax.swing.JFormattedTextField();
        jLabelFoneRefCom6 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaObservacao = new javax.swing.JTextArea();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableCompras = new javax.swing.JTable();
        jComboBoxCompras = new javax.swing.JComboBox();
        jPanel8 = new javax.swing.JPanel();
        jButtonUltimo = new javax.swing.JButton();
        jButtonProximo = new javax.swing.JButton();
        jButtonAnterior = new javax.swing.JButton();
        jButtonPrimeiro = new javax.swing.JButton();
        jButtonVoltar = new javax.swing.JButton();
        jButtonLimpar = new javax.swing.JButton();
        jButtonSalvarCliente = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabelLogin = new javax.swing.JLabel();
        jLabelData = new javax.swing.JLabel();
        jLabelHora = new javax.swing.JLabel();

        timer1.addTimerListener(new org.netbeans.examples.lib.timerbean.TimerListener() {
            public void onTime(java.awt.event.ActionEvent evt) {
                timer1OnTime(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("CADASTRO CLIENTE");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabelCodigoCliente.setText("Codigo:");

        jTextFieldCodigoCliente.setEditable(false);
        jTextFieldCodigoCliente.setFont(new java.awt.Font("Tahoma", 1, 11));
        jTextFieldCodigoCliente.setForeground(new java.awt.Color(255, 0, 0));
        jTextFieldCodigoCliente.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldCodigoCliente.setText("0000");
        jTextFieldCodigoCliente.setDisabledTextColor(new java.awt.Color(255, 0, 0));
        jTextFieldCodigoCliente.setFocusable(false);

        jLabelNasc1.setText("Data Cadastro:");

        jFormattedTextFieldDataCadastro.setEditable(false);
        jFormattedTextFieldDataCadastro.setForeground(new java.awt.Color(255, 0, 0));
        jFormattedTextFieldDataCadastro.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jFormattedTextFieldDataCadastro.setText("00/00/0000");
        jFormattedTextFieldDataCadastro.setFocusable(false);
        jFormattedTextFieldDataCadastro.setFont(new java.awt.Font("Tahoma", 1, 11));

        jCheckBoxCadastroAprovado.setSelected(true);
        jCheckBoxCadastroAprovado.setText("CadastroAprovado");
        jCheckBoxCadastroAprovado.setToolTipText("Cadastro apto para realizar compras");

        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        jLabelNomeCli2.setText("Nome: ");

        jLabelRG2.setText("RG: ");

        jTextFieldRGCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldRGClienteActionPerformed(evt);
            }
        });

        jLabelCPF2.setText("CPF: ");

        jFormattedTextFieldCPFCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextFieldCPFClienteKeyReleased(evt);
            }
        });

        jComboBoxSexoCliente.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Feminino", "Masculino" }));

        jLabelSexo2.setText("Sexo:");

        jFormattedTextFieldNascCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextFieldNascClienteKeyReleased(evt);
            }
        });

        jLabelNasc3.setText("Nasc.:");

        jLabelTelefone1.setText("Fone: ");

        jLabelCelular1.setText("Celular: ");

        jFormattedTextFieldCelCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextFieldCelClienteActionPerformed(evt);
            }
        });

        jLabelEstadoCivil1.setText("Est. Civil:");

        jLabelEmail1.setText("E-mail: ");

        jTextFieldEmail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldEmailFocusLost(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setText("(*)");

        jLabel4.setForeground(new java.awt.Color(255, 0, 0));
        jLabel4.setText("(*)");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabelEstadoCivil1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelEmail1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                    .addComponent(jLabelNomeCli2, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                    .addComponent(jLabelCPF2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                    .addComponent(jLabelNasc3, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                    .addComponent(jLabelTelefone1, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 493, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jFormattedTextFieldFoneCliente, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jFormattedTextFieldNascCliente, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jFormattedTextFieldCPFCliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                                    .addComponent(jTextFieldEstadoCivil, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                                .addComponent(jLabel4)
                                .addGap(105, 105, 105)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(jLabelCelular1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabelSexo2, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE))
                                    .addComponent(jLabelRG2, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jFormattedTextFieldCelCliente)
                                    .addComponent(jComboBoxSexoCliente, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextFieldRGCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)))
                            .addComponent(jTextFieldNomeCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 482, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNomeCli2)
                    .addComponent(jLabel3)
                    .addComponent(jTextFieldNomeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelCPF2)
                            .addComponent(jFormattedTextFieldCPFCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelNasc3)
                            .addComponent(jFormattedTextFieldNascCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelTelefone1)
                            .addComponent(jFormattedTextFieldFoneCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldRGCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelRG2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxSexoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelSexo2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jFormattedTextFieldCelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelCelular1))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelEstadoCivil1)
                    .addComponent(jTextFieldEstadoCivil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelEmail1)
                    .addComponent(jTextFieldEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(61, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Dados Pessoais", jPanel5);

        jLabelComplemento1.setText("Comp.: ");

        jLabelCEP1.setText("CEP: ");

        jLabelBairro1.setText("Bairro: ");

        jLabelUF1.setText("UF: ");

        jComboBoxUF.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PR", "AC", "AL", "AM", "AP", "BA", "CE", "DF", "ES", "GO", "MA", "MG", "MS", "MT", "PA", "PB", "PE", "PI", "RJ", "RN", "RO", "RR", "RS", "SC", "SE", "SP", "TO" }));
        jComboBoxUF.setSelectedItem("PR");
        jComboBoxUF.setToolTipText("Selecione um estado");
        jComboBoxUF.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxUFItemStateChanged(evt);
            }
        });

        jLabelCidade1.setText("Cidade: ");

        jComboBoxCidade.setToolTipText("Selecione uma cidade de acordo com a UF");

        jTextFieldAluguel.setEnabled(false);
        jTextFieldAluguel.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldAluguelFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldAluguelFocusLost(evt);
            }
        });

        jLabelValorAluguel1.setText("Aluguel: R$");

        jCheckBoxCasaPropria.setSelected(true);
        jCheckBoxCasaPropria.setText("Casa Propria");
        jCheckBoxCasaPropria.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxCasaPropriaItemStateChanged(evt);
            }
        });

        jTextFieldTempoResidencia.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldTempoResidenciaFocusLost(evt);
            }
        });

        jLabelTempoResidencia1.setText("Temp. Resid.:");

        jComboBoxLogradouro.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Rua", "Avenida", "Travessa", "Estrada", "Rodovia" }));
        jComboBoxLogradouro.setToolTipText("Selecione um tipo de logradouro");

        jLabelNumTrabalho2.setText("No: ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jCheckBoxCasaPropria)
                        .addGap(41, 41, 41)
                        .addComponent(jLabelValorAluguel1)
                        .addGap(18, 18, 18)
                        .addComponent(jTextFieldAluguel, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(jLabelTempoResidencia1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 15, Short.MAX_VALUE)
                        .addComponent(jTextFieldTempoResidencia, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jComboBoxLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextFieldNomeLogradouro, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabelBairro1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabelCidade1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabelNumTrabalho2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabelCEP1, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGap(4, 4, 4)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jComboBoxCidade, 0, 524, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jFormattedTextFieldCEP, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 287, Short.MAX_VALUE)
                                .addComponent(jLabelUF1)
                                .addGap(18, 18, 18)
                                .addComponent(jComboBoxUF, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jTextFieldNum, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 135, Short.MAX_VALUE)
                                .addComponent(jLabelComplemento1)
                                .addGap(18, 18, 18)
                                .addComponent(jTextFieldComplemento, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextFieldBairro, javax.swing.GroupLayout.DEFAULT_SIZE, 524, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldNomeLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldComplemento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNumTrabalho2)
                    .addComponent(jTextFieldNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelComplemento1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelBairro1)
                    .addComponent(jTextFieldBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxUF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelUF1)
                    .addComponent(jLabelCEP1)
                    .addComponent(jFormattedTextFieldCEP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelCidade1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxCasaPropria)
                    .addComponent(jTextFieldTempoResidencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelValorAluguel1)
                    .addComponent(jTextFieldAluguel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelTempoResidencia1))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Endereco", jPanel1);

        jLabelEmpresa1.setText("Empresa: ");

        jLabelFuncao1.setText("Funcao: ");

        jLabelSalario1.setText("Salario: ");

        jTextFieldSalario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldSalarioFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldSalarioFocusLost(evt);
            }
        });

        jLabelDataAdmissao1.setText("Data Admissao: ");

        jLabelBairroTrabalho1.setText("Bairro: ");

        jLabelFoneTrabalho1.setText("Fone: ");

        jFormattedTextFieldFoneTrabalho.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jFormattedTextFieldFoneTrabalhoFocusLost(evt);
            }
        });

        jLabelComplemento2.setText("Comp.: ");

        jLabelNumTrabalho3.setText("No: ");

        jComboBoxLogradouroTrabalho.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Rua", "Avenida", "Travessa", "Estrada", "Rodovia" }));
        jComboBoxLogradouroTrabalho.setToolTipText("Selecione um logradouro");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelNumTrabalho3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelEmpresa1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelFuncao1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelSalario1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelBairroTrabalho1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jTextFieldNumTrabalho, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 122, Short.MAX_VALUE)
                                .addComponent(jLabelComplemento2, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldComplementoTrabalho, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jTextFieldBairroTrabalho, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 134, Short.MAX_VALUE)
                                .addComponent(jLabelFoneTrabalho1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jFormattedTextFieldFoneTrabalho, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jTextFieldSalario, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 155, Short.MAX_VALUE)
                                .addComponent(jLabelDataAdmissao1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jFormattedTextFieldDataAdmissao, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextFieldFuncao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE)
                            .addComponent(jTextFieldEmpresa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 503, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jComboBoxLogradouroTrabalho, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                        .addComponent(jTextFieldNomeLogradouroTrabalho, javax.swing.GroupLayout.PREFERRED_SIZE, 457, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelEmpresa1)
                    .addComponent(jTextFieldEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelFuncao1)
                    .addComponent(jTextFieldFuncao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelSalario1)
                    .addComponent(jFormattedTextFieldDataAdmissao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldSalario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelDataAdmissao1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldNomeLogradouroTrabalho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxLogradouroTrabalho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldComplementoTrabalho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNumTrabalho3)
                    .addComponent(jTextFieldNumTrabalho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelComplemento2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelBairroTrabalho1)
                    .addComponent(jTextFieldBairroTrabalho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFormattedTextFieldFoneTrabalho, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelFoneTrabalho1))
                .addContainerGap(64, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Trabalho", jPanel2);

        jLabelNomeCli3.setText("Nome: ");

        jLabelRG3.setText("RG: ");

        jTextFieldRGConjuge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldRGConjugeActionPerformed(evt);
            }
        });

        jLabelCPF3.setText("CPF: ");

        jFormattedTextFieldCPFConjuge.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextFieldCPFConjugeKeyReleased(evt);
            }
        });

        jComboBoxSexoConjuge.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Feminino", "Masculino" }));

        jLabelSexo3.setText("Sexo:");

        jFormattedTextFieldNascConjuge.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextFieldNascConjugeKeyReleased(evt);
            }
        });

        jLabelNasc4.setText("Nasc.:");

        jFormattedTextFieldCelConjuge.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jFormattedTextFieldCelConjugeFocusLost(evt);
            }
        });

        jLabelCelConjuge1.setText("Celular: ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabelNomeCli3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelNasc4, javax.swing.GroupLayout.DEFAULT_SIZE, 65, Short.MAX_VALUE)
                        .addComponent(jLabelCelConjuge1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabelRG3, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTextFieldRGConjuge, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                            .addComponent(jFormattedTextFieldNascConjuge)
                            .addComponent(jFormattedTextFieldCelConjuge))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 163, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabelCPF3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelSexo3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(51, 51, 51)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jFormattedTextFieldCPFConjuge)
                            .addComponent(jComboBoxSexoConjuge, 0, 128, Short.MAX_VALUE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldNomeConjuge, javax.swing.GroupLayout.PREFERRED_SIZE, 507, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNomeCli3)
                    .addComponent(jTextFieldNomeConjuge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jFormattedTextFieldCPFConjuge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldRGConjuge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelRG3)
                            .addComponent(jLabelCPF3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxSexoConjuge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelSexo3)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelNasc4)
                            .addComponent(jFormattedTextFieldNascConjuge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCelConjuge1)
                    .addComponent(jFormattedTextFieldCelConjuge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(108, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Conjuge", jPanel3);

        jLabelNomeRefCom1.setText("Nome: ");

        jLabelFoneRefCom1.setText("Fone: ");

        jLabelNomeRefCom4.setText("Nome: ");

        jLabelFoneRefCom4.setText("Fone: ");

        jLabelNomeRefCom5.setText("Nome: ");

        jLabelFoneRefCom5.setText("Fone: ");

        jFormattedTextFieldFoneRefCom3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jFormattedTextFieldFoneRefCom3FocusLost(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelFoneRefCom1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelNomeRefCom1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFormattedTextFieldFoneRefCom1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldNomeRefCom1, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelFoneRefCom4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelNomeRefCom4, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFormattedTextFieldFoneRefCom2, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldNomeRefCom2, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelFoneRefCom5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelNomeRefCom5, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFormattedTextFieldFoneRefCom3, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldNomeRefCom3, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNomeRefCom1)
                    .addComponent(jTextFieldNomeRefCom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelFoneRefCom1)
                    .addComponent(jFormattedTextFieldFoneRefCom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNomeRefCom4)
                    .addComponent(jTextFieldNomeRefCom2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelFoneRefCom4)
                    .addComponent(jFormattedTextFieldFoneRefCom2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNomeRefCom5)
                    .addComponent(jTextFieldNomeRefCom3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelFoneRefCom5)
                    .addComponent(jFormattedTextFieldFoneRefCom3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(64, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Inf. Comerciais", jPanel4);

        jLabelNomeRefCom2.setText("Nome: ");

        jLabelFoneRefCom2.setText("Fone: ");

        jLabelNomeRefCom6.setText("Nome: ");

        jFormattedTextFieldFoneInfPessoal2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jFormattedTextFieldFoneInfPessoal2FocusLost(evt);
            }
        });

        jLabelFoneRefCom6.setText("Fone: ");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 604, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabelFoneRefCom2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabelNomeRefCom2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jFormattedTextFieldFoneInfPessoal1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextFieldInfPessoal1, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE)))
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabelFoneRefCom6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabelNomeRefCom6, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jFormattedTextFieldFoneInfPessoal2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextFieldInfPessoal2, javax.swing.GroupLayout.DEFAULT_SIZE, 521, Short.MAX_VALUE))))
                    .addContainerGap()))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 268, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel6Layout.createSequentialGroup()
                    .addGap(37, 37, 37)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelNomeRefCom2)
                        .addComponent(jTextFieldInfPessoal1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelFoneRefCom2)
                        .addComponent(jFormattedTextFieldFoneInfPessoal1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelNomeRefCom6)
                        .addComponent(jTextFieldInfPessoal2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelFoneRefCom6)
                        .addComponent(jFormattedTextFieldFoneInfPessoal2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(105, Short.MAX_VALUE)))
        );

        jTabbedPane1.addTab("Inf. Pessoais", jPanel6);

        jTextAreaObservacao.setColumns(20);
        jTextAreaObservacao.setRows(5);
        jTextAreaObservacao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextAreaObservacaoKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTextAreaObservacao);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Observacao", jPanel7);

        jTableCompras.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cupom", "Data Venda", "Situacao", "Total", "Tipo", "Vencimento"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableCompras.setToolTipText("Duplo clique para visualizar venda");
        jTableCompras.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableComprasMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTableCompras);

        jComboBoxCompras.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "TODAS", "PENDENTES", "RECEBIDAS" }));
        jComboBoxCompras.setToolTipText("Selecao de compras efetuadas pelo cliente");
        jComboBoxCompras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxComprasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBoxCompras, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jComboBoxCompras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 211, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Compras", jPanel9);

        jPanel8.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButtonUltimo.setText(">");
        jButtonUltimo.setToolTipText("Ultimo cadastro");
        jButtonUltimo.setEnabled(false);
        jButtonUltimo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUltimoActionPerformed(evt);
            }
        });

        jButtonProximo.setText(">>");
        jButtonProximo.setToolTipText("Proximo cadastro");
        jButtonProximo.setEnabled(false);
        jButtonProximo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonProximoActionPerformed(evt);
            }
        });

        jButtonAnterior.setText("<<");
        jButtonAnterior.setToolTipText("Cadastro anterior");
        jButtonAnterior.setEnabled(false);
        jButtonAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAnteriorActionPerformed(evt);
            }
        });

        jButtonPrimeiro.setText("<");
        jButtonPrimeiro.setToolTipText("Primeiro cadastro");
        jButtonPrimeiro.setEnabled(false);
        jButtonPrimeiro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrimeiroActionPerformed(evt);
            }
        });

        jButtonVoltar.setText("Cancelar");
        jButtonVoltar.setToolTipText("Retorna para a tela principal e nao cadastra o cliente");
        jButtonVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVoltarActionPerformed(evt);
            }
        });

        jButtonLimpar.setText("Limpar");
        jButtonLimpar.setToolTipText("Limpa todos os campos");
        jButtonLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLimparActionPerformed(evt);
            }
        });

        jButtonSalvarCliente.setText("Cadastrar");
        jButtonSalvarCliente.setToolTipText("Cadastrar Cliente");
        jButtonSalvarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSalvarClienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonSalvarCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonLimpar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonVoltar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 360, Short.MAX_VALUE)
                .addComponent(jButtonPrimeiro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonAnterior)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonProximo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonUltimo)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonSalvarCliente)
                    .addComponent(jButtonLimpar)
                    .addComponent(jButtonVoltar)
                    .addComponent(jButtonPrimeiro)
                    .addComponent(jButtonAnterior)
                    .addComponent(jButtonProximo)
                    .addComponent(jButtonUltimo))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel5.setText("Campos obrigatorios");

        jLabel6.setForeground(new java.awt.Color(255, 0, 0));
        jLabel6.setText("(*)");

        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel12.setText("Usuario:");

        jLabelHora.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelLogin, javax.swing.GroupLayout.DEFAULT_SIZE, 629, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabelData, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelHora, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabelLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelData, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelHora, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabelCodigoCliente)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldCodigoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39)
                        .addComponent(jCheckBoxCadastroAprovado)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 237, Short.MAX_VALUE)
                        .addComponent(jLabelNasc1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jFormattedTextFieldDataCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCodigoCliente)
                    .addComponent(jTextFieldCodigoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFormattedTextFieldDataCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNasc1)
                    .addComponent(jCheckBoxCadastroAprovado))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void limpa() {
        // ------------- DADOS PESSOAIS -----------------
        jTextFieldCodigoCliente.setText("" + this.getMaiorId());
        jTextFieldNomeCliente.setText("");
        jFormattedTextFieldNascCliente.setValue(null);
        jTextFieldRGCliente.setText("");
        jFormattedTextFieldCPFCliente.setValue(null);
        jComboBoxSexoCliente.setSelectedIndex(0);
        jFormattedTextFieldFoneCliente.setValue(null);
        jFormattedTextFieldCelCliente.setValue(null);
        jTextFieldEstadoCivil.setText("");
        jTextFieldEmail.setText("");

        // ------------- ENDERECO CLIENTE -----------------
        jComboBoxLogradouro.setSelectedIndex(0);
        jTextFieldNomeLogradouro.setText("");
        jTextFieldNum.setText("");
        jTextFieldComplemento.setText("");
        jTextFieldBairro.setText("");
        jComboBoxUF.setSelectedItem("PR");
        this.carregaCidades("PR", 1);
        jFormattedTextFieldCEP.setValue(null);
        jCheckBoxCasaPropria.setSelected(true);
        jTextFieldTempoResidencia.setText("");

        // ------------- TRABALHO CLIENTE -----------------
        jTextFieldEmpresa.setText("");
        jTextFieldFuncao.setText("");
        jTextFieldSalario.setText("");
        jComboBoxLogradouroTrabalho.setSelectedIndex(0);
        jTextFieldNomeLogradouroTrabalho.setText("");
        jTextFieldNumTrabalho.setText("");
        jTextFieldComplementoTrabalho.setText("");
        jTextFieldBairroTrabalho.setText("");
        this.carregaCidades("PR", 1);
        jFormattedTextFieldFoneTrabalho.setValue(null);
        jFormattedTextFieldDataAdmissao.setValue(null);

        // ------------- CONJUGE -----------------
        jTextFieldNomeConjuge.setText("");
        jFormattedTextFieldNascConjuge.setValue(null);
        jTextFieldRGConjuge.setText("");
        jFormattedTextFieldCPFConjuge.setValue(null);
        jComboBoxSexoConjuge.setSelectedIndex(0);
        jFormattedTextFieldCelConjuge.setValue(null);

        // ------------- INFORMACOES PESSOAIS -----------------
        jTextFieldInfPessoal1.setText("");
        jFormattedTextFieldFoneInfPessoal1.setValue(null);
        jTextFieldInfPessoal2.setText("");
        jFormattedTextFieldFoneInfPessoal2.setValue(null);

        // ------------- REFERENCIAS COMERCIAIS -----------------
        jTextFieldNomeRefCom1.setText("");
        jFormattedTextFieldFoneRefCom1.setValue(null);
        jTextFieldNomeRefCom2.setText("");
        jFormattedTextFieldFoneRefCom2.setValue(null);
        jTextFieldNomeRefCom3.setText("");
        jFormattedTextFieldFoneRefCom3.setValue(null);
    }

    private void mostraDados(HashMap<String, Object> cliente) {
        if (cliente != null) {
            String aux;
            jFormattedTextFieldDataCadastro.setValue((String) cliente.get("dataCadastro"));
            if (((String) cliente.get("cadastroAprovado")).equals("true")) {
                jCheckBoxCadastroAprovado.setSelected(true);
            } else {
                jCheckBoxCadastroAprovado.setSelected(false);
            }
            // ------------- DADOS PESSOAIS -----------------
            jTextFieldCodigoCliente.setText((String) cliente.get("cod"));
            jTextFieldNomeCliente.setText((String) cliente.get("nome"));
            jFormattedTextFieldNascCliente.setValue((String) cliente.get("dataNasc"));
            jTextFieldRGCliente.setText((String) cliente.get("rg"));
            jFormattedTextFieldCPFCliente.setValue((String) cliente.get("cpf"));
            jComboBoxSexoCliente.setSelectedItem((String) cliente.get("sexo"));
            jFormattedTextFieldFoneCliente.setValue((String) cliente.get("telefone"));
            jFormattedTextFieldCelCliente.setValue((String) cliente.get("celular"));
            jTextFieldEstadoCivil.setText((String) cliente.get("estadoCivil"));
            jTextFieldEmail.setText((String) cliente.get("email"));

            // ------------- ENDERECO CLIENTE -----------------
            jComboBoxLogradouro.setSelectedItem((String) cliente.get("tipoLogradouro"));
            jTextFieldNomeLogradouro.setText((String) cliente.get("nomeLogradouro"));
            jTextFieldNum.setText((String) cliente.get("numLogradouro"));
            jTextFieldComplemento.setText((String) cliente.get("complemento"));
            jTextFieldBairro.setText((String) cliente.get("bairro"));
            jComboBoxUF.setSelectedItem((String) cliente.get("estado"));
            jComboBoxCidade.setSelectedItem((String) cliente.get("cidade"));
            jFormattedTextFieldCEP.setValue((String) cliente.get("cep"));
            if (((String) cliente.get("propria")).equals("true")) {
                jCheckBoxCasaPropria.setSelected(true);
            } else {
                jCheckBoxCasaPropria.setSelected(false);
                jTextFieldAluguel.setText((String) cliente.get("valorAluguel"));
            }
            jTextFieldTempoResidencia.setText((String) cliente.get("tempoResidencia"));

            // ------------- TRABALHO CLIENTE -----------------
            jTextFieldEmpresa.setText((String) cliente.get("trabalho"));
            jTextFieldFuncao.setText((String) cliente.get("funcao"));
            jTextFieldSalario.setText((String) cliente.get("salario"));
            jComboBoxLogradouroTrabalho.setSelectedItem((String) cliente.get("tipoLogradouroTrabalho"));
            jTextFieldNomeLogradouroTrabalho.setText((String) cliente.get("nomeLogradouroTrabalho"));
            jTextFieldNumTrabalho.setText((String) cliente.get("numLogradouroTrabalho"));
            jTextFieldComplementoTrabalho.setText((String) cliente.get("complementoTrabalho"));
            jTextFieldBairroTrabalho.setText((String) cliente.get("bairroTrabalho"));
            jFormattedTextFieldFoneTrabalho.setValue((String) cliente.get("telefoneTrabalho"));
            jFormattedTextFieldDataAdmissao.setValue((String) cliente.get("dataAdmissao"));

            // ------------- CONJUGE -----------------
            jTextFieldNomeConjuge.setText((String) cliente.get("nomeConjuge"));
            jFormattedTextFieldNascConjuge.setValue((String) cliente.get("nascConjuge"));
            jTextFieldRGConjuge.setText((String) cliente.get("rgConjuge"));
            jFormattedTextFieldCPFConjuge.setValue((String) cliente.get("cpfConjuge"));
            jComboBoxSexoConjuge.setSelectedItem((String) cliente.get("sexoConjuge"));
            jFormattedTextFieldCelConjuge.setValue((String) cliente.get("celularConjuge"));

            // ------------- INFORMACOES PESSOAIS -----------------
            jTextFieldInfPessoal1.setText((String) cliente.get("nomeInfPessoal1"));
            jFormattedTextFieldFoneInfPessoal1.setValue((String) cliente.get("foneInfPessoal1"));
            jTextFieldInfPessoal2.setText((String) cliente.get("nomeInfPessoal2"));
            jFormattedTextFieldFoneInfPessoal2.setValue((String) cliente.get("foneInfPessoal2"));

            // ------------- REFERENCIAS COMERCIAIS -----------------
            aux = (String) cliente.get("refCom1");
            jTextFieldNomeRefCom1.setText(aux.split("#")[0]);
            jFormattedTextFieldFoneRefCom1.setValue(aux.split("#")[1]);
            aux = (String) cliente.get("refCom2");
            jTextFieldNomeRefCom2.setText(aux.split("#")[0]);
            jFormattedTextFieldFoneRefCom2.setValue(aux.split("#")[1]);
            aux = (String) cliente.get("refCom3");
            jTextFieldNomeRefCom3.setText(aux.split("#")[0]);
            jFormattedTextFieldFoneRefCom3.setValue(aux.split("#")[1]);
            jTextAreaObservacao.setText((String) cliente.get("observacao"));

            // ---------------- TABELA DE COMPRAS -----------------
            jComboBoxCompras.setSelectedIndex(0);
            this.limpaTabela();
            modelo = (DefaultTableModel) jTableCompras.getModel();
            String str = (String) cliente.get("compras");
            for (int i = 0; i < str.split("#").length; i++) {
                try {
                    int codVenda = Integer.parseInt(str.split("#")[i]);
                    HashMap<String, Object> dadosVenda = new HashMap<String, Object>();
                    dadosVenda.put("modelo", "venda");
                    dadosVenda.put("operacao", "busca");
                    dadosVenda.put("cod", codVenda + "");
                    dadosVenda.put("posicao", "cod");
                    dadosVenda = controlador.recebeOperacao(dadosVenda);
                    List lista = (List) dadosVenda.get("lista");
                    dadosVenda = (HashMap<String, Object>) lista.get(0);
                    Object linha[] = new Object[]{};
                    if (Integer.parseInt((String) dadosVenda.get("cod")) == codVenda) {
                        if (dadosVenda.get("avista").equals("true")) {
                            linha = new Object[]{codVenda, dadosVenda.get("dataVenda"), "RECEBIDO", dadosVenda.get("valorTotal"), "A VISTA", dadosVenda.get("dataVenda")};
                        } else {
                            List parcelas = (List) dadosVenda.get("parcelas");
                            //exemplo: dataVencimento#valor#datapagamento#situacao#funcionarioRecebeu
                            Boolean verificador = true;
                            for (int k = 0; k < parcelas.size(); k++) {
                                String parcela = (String) parcelas.get(k);
                                if (parcela.split("#")[3].equals("false")) { //parcela nao foi paga
                                    linha = new Object[]{codVenda, dadosVenda.get("dataVenda"), "PENDENTE", dadosVenda.get("valorTotal"), "A PRAZO", parcela.split("#")[0]};
                                    verificador = false;
                                    break;
                                }
                            }
                            if (verificador) {
                                linha = new Object[]{codVenda, dadosVenda.get("dataVenda"), "RECEBIDO", dadosVenda.get("valorTotal"), "A PRAZO", dadosVenda.get("dataVenda")};
                            }
                        }
                        modelo.addRow(linha);
                    }
                } catch (Exception e) {
                }
            }
        }
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

    private void jButtonSalvarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSalvarClienteActionPerformed
        this.validaCPF(jFormattedTextFieldCPFCliente.getText(), "cliente");
        if ((jTextFieldAluguel.getText().equals("") || jTextFieldAluguel.getText() == null) && !jCheckBoxCasaPropria.isSelected()) {
            JOptionPane.showMessageDialog(null, "O valor do aluguel esta incorreto!", "Erro", JOptionPane.INFORMATION_MESSAGE);
            jTabbedPane1.setSelectedIndex(1);
            jTextFieldAluguel.requestFocus();
        } else {
            if (jTextFieldAluguel.getText().toUpperCase().equals("ERRO") && !jCheckBoxCasaPropria.isSelected()) {
                JOptionPane.showMessageDialog(null, "O valor do aluguel esta incorreto!", "Erro", JOptionPane.INFORMATION_MESSAGE);
                jTabbedPane1.setSelectedIndex(1);
                jTextFieldAluguel.requestFocus();
            } else {
                if (jTextFieldSalario.getText().toUpperCase().equals("ERRO")) {
                    JOptionPane.showMessageDialog(null, "O valor do salario esta incorreto!", "Erro", JOptionPane.INFORMATION_MESSAGE);
                    jTabbedPane1.setSelectedIndex(2);
                    jTextFieldSalario.requestFocus();
                } else {
                    String str = this.cadastraCliente();
                    if (str.equals("sucesso")) {
                        new TelaPrincipalGer(sessao).show();
                        this.dispose();
                    }
                }
            }
        }
}//GEN-LAST:event_jButtonSalvarClienteActionPerformed

    private void jButtonLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimparActionPerformed
        if (cadastrar) {
            this.limpa();
        } else {
            int resposta = JOptionPane.showConfirmDialog(null, "Desejar excluir este cliente?", "Exclusao", JOptionPane.YES_NO_OPTION);
            if (resposta == 0) {
                int cod = Integer.parseInt(this.jTextFieldCodigoCliente.getText());
                HashMap<String, Object> dados = new HashMap<String, Object>();
                dados = this.pegaDados();
                dados.put("operacao", "alterar");
                dados.put("modelo", "cliente");
                dados.remove("ativo");
                dados.put("ativo", "false");
                dados = controlador.recebeOperacao(dados);
                String str = (String) dados.get("retorno");
                if (str.equals("Cliente alterado!")) {
                    JOptionPane.showMessageDialog(null, "Cliente excluido com sucesso!");
                    new TelaPrincipalGer(sessao).show();
                    this.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Impossivel excluir cliente. Contacte o administrador.");
                }

            }
        }
}//GEN-LAST:event_jButtonLimparActionPerformed

    private void jButtonVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVoltarActionPerformed
        new TelaPrincipalGer(sessao).show();
        this.dispose();
}//GEN-LAST:event_jButtonVoltarActionPerformed

    private void jButtonPrimeiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrimeiroActionPerformed
        HashMap<String, Object> dados = new HashMap<String, Object>();
        dados.put("modelo", "cliente");
        dados.put("operacao", "busca");
        dados.put("posicao", "primeiro");
        dados.put("classificacao", "ordem");
        dados = controlador.recebeOperacao(dados);
        if (dados.get("ativo").equals("true")) {
            this.mostraDados(dados);
        } else {
            String codigo = (String) dados.get("cod");
            while (true) {
                dados.put("modelo", "cliente");
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
        dados.put("modelo", "cliente");
        dados.put("operacao", "busca");
        dados.put("posicao", "anterior");
        dados.put("classificacao", "ordem");
        dados.put("cod", jTextFieldCodigoCliente.getText());
        dados = controlador.recebeOperacao(dados);
        if (dados.get("ativo").equals("true")) {
            this.mostraDados(dados);
        } else {
            String codigo = (String) dados.get("cod");
            while (true) {
                dados.put("modelo", "cliente");
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
        dados.put("modelo", "cliente");
        dados.put("operacao", "busca");
        dados.put("posicao", "proximo");
        dados.put("classificacao", "ordem");
        dados.put("cod", jTextFieldCodigoCliente.getText());
        dados = controlador.recebeOperacao(dados);
        if (dados.get("ativo").equals("true")) {
            this.mostraDados(dados);
        } else {
            String codigo = (String) dados.get("cod");
            while (true) {
                dados.put("modelo", "cliente");
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
        dados.put("modelo", "cliente");
        dados.put("operacao", "busca");
        dados.put("posicao", "ultimo");
        dados.put("classificacao", "ordem");
        dados = controlador.recebeOperacao(dados);
        if (dados.get("ativo").equals("true")) {
            this.mostraDados(dados);
        } else {
            String codigo = (String) dados.get("cod");
            while (true) {
                dados.put("modelo", "cliente");
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

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        new TelaPrincipalGer(sessao).show();
        this.dispose();
    }//GEN-LAST:event_formWindowClosing

    private void jTextFieldRGClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldRGClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldRGClienteActionPerformed

    private void jFormattedTextFieldCPFClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextFieldCPFClienteKeyReleased
        this.validaCPF(jFormattedTextFieldCPFCliente.getText(), "cliente");
    }//GEN-LAST:event_jFormattedTextFieldCPFClienteKeyReleased

    private void jFormattedTextFieldNascClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextFieldNascClienteKeyReleased
        String x = jFormattedTextFieldNascCliente.getText();
        this.validaNasc(x, "cliente");
    }//GEN-LAST:event_jFormattedTextFieldNascClienteKeyReleased

    private void jFormattedTextFieldCelClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextFieldCelClienteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jFormattedTextFieldCelClienteActionPerformed

    private void jComboBoxUFItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBoxUFItemStateChanged
        String s = (String) jComboBoxUF.getSelectedItem();
        if (s.compareTo("") != 0) {
            this.carregaCidades((String) jComboBoxUF.getSelectedItem(), 1);
        }
    }//GEN-LAST:event_jComboBoxUFItemStateChanged

    private void jTextFieldAluguelFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldAluguelFocusGained
        jTextFieldAluguel.setSelectionStart(0);
        jTextFieldAluguel.setSelectionEnd(jTextFieldAluguel.getText().length());
    }//GEN-LAST:event_jTextFieldAluguelFocusGained

    private void jTextFieldAluguelFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldAluguelFocusLost
        jTextFieldAluguel.setText(this.trataPreco(jTextFieldAluguel.getText()).toUpperCase());
    }//GEN-LAST:event_jTextFieldAluguelFocusLost

    private void jCheckBoxCasaPropriaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jCheckBoxCasaPropriaItemStateChanged
        if (!jCheckBoxCasaPropria.isSelected()) {
            jTextFieldAluguel.setEnabled(true);
        } else {
            jTextFieldAluguel.setEnabled(false);
        }
    }//GEN-LAST:event_jCheckBoxCasaPropriaItemStateChanged

    private void jTextFieldSalarioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldSalarioFocusGained
        jTextFieldSalario.setSelectionStart(0);
        jTextFieldSalario.setSelectionEnd(jTextFieldSalario.getText().length());
    }//GEN-LAST:event_jTextFieldSalarioFocusGained

    private void jTextFieldSalarioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldSalarioFocusLost
        jTextFieldSalario.setText(this.trataPreco(jTextFieldSalario.getText()).toUpperCase());
    }//GEN-LAST:event_jTextFieldSalarioFocusLost

    private void jTextFieldRGConjugeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldRGConjugeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldRGConjugeActionPerformed

    private void jFormattedTextFieldCPFConjugeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextFieldCPFConjugeKeyReleased
        this.validaCPF(jFormattedTextFieldCPFCliente.getText(), "conjuge");
    }//GEN-LAST:event_jFormattedTextFieldCPFConjugeKeyReleased

    private void jFormattedTextFieldNascConjugeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextFieldNascConjugeKeyReleased
        String x = jFormattedTextFieldNascCliente.getText();
        this.validaNasc(x, "conjuge");
    }//GEN-LAST:event_jFormattedTextFieldNascConjugeKeyReleased

    private void jTextFieldEmailFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldEmailFocusLost
        jTabbedPane1.setSelectedIndex(1);
        jTextFieldNomeLogradouro.requestFocus();
    }//GEN-LAST:event_jTextFieldEmailFocusLost

    private void jTextFieldTempoResidenciaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldTempoResidenciaFocusLost
        jTabbedPane1.setSelectedIndex(2);
        jTextFieldEmpresa.requestFocus();
    }//GEN-LAST:event_jTextFieldTempoResidenciaFocusLost

    private void jFormattedTextFieldFoneTrabalhoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextFieldFoneTrabalhoFocusLost
        jTabbedPane1.setSelectedIndex(3);
        jTextFieldNomeConjuge.requestFocus();
    }//GEN-LAST:event_jFormattedTextFieldFoneTrabalhoFocusLost

    private void jFormattedTextFieldCelConjugeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextFieldCelConjugeFocusLost
        jTabbedPane1.setSelectedIndex(4);
        jTextFieldNomeRefCom1.requestFocus();
    }//GEN-LAST:event_jFormattedTextFieldCelConjugeFocusLost

    private void jFormattedTextFieldFoneRefCom3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextFieldFoneRefCom3FocusLost
        jTabbedPane1.setSelectedIndex(5);
        jTextFieldInfPessoal1.requestFocus();
    }//GEN-LAST:event_jFormattedTextFieldFoneRefCom3FocusLost

    private void jFormattedTextFieldFoneInfPessoal2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextFieldFoneInfPessoal2FocusLost
        jTabbedPane1.setSelectedIndex(6);
        jTextAreaObservacao.requestFocus();
    }//GEN-LAST:event_jFormattedTextFieldFoneInfPessoal2FocusLost

    private void jTableComprasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableComprasMouseClicked
        if (evt.getClickCount() == 2) {
            if (jTableCompras.getSelectedRow() >= 0) {
                RecebimentoGer recebimento = new RecebimentoGer("cliente", (DefaultTableModel) jTableCompras.getModel(), jTableCompras.getSelectedRow(), sessao);
                recebimento.show();
                this.dispose();
            }
        }
    }//GEN-LAST:event_jTableComprasMouseClicked

    private void jTextAreaObservacaoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextAreaObservacaoKeyReleased
        if (evt.getKeyCode() == 9) {
            if (cadastrar) {
                jTabbedPane1.setSelectedIndex(0);
                jButtonSalvarCliente.requestFocus();
            } else {
                jTabbedPane1.setSelectedIndex(7);
                jTableCompras.requestFocus();
            }
        }
    }//GEN-LAST:event_jTextAreaObservacaoKeyReleased

    private void timer1OnTime(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timer1OnTime
        DataSistema d = DataSistema.getInstance();
        String g = d.diaMesAno();
        jLabelData.setText(g);
        String h = d.horaMinSeg();
        jLabelHora.setText(h);
    }//GEN-LAST:event_timer1OnTime

    private void jComboBoxComprasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxComprasActionPerformed
        String compra = (String) jComboBoxCompras.getSelectedItem();
        HashMap<String, Object> dados = new HashMap<String, Object>();
        dados.put("modelo", "cliente");
        dados.put("operacao", "busca");
        dados.put("posicao", "cod");
        dados.put("cod", jTextFieldCodigoCliente.getText());
        dados = controlador.recebeOperacao(dados);
        String compras = (String) dados.get("compras");
        this.limpaTabela();
        modelo = (DefaultTableModel) jTableCompras.getModel();
        if (compra.equals("TODAS")) {
            if (!compras.equals("")) {
                for (int i = 0; i < compras.split("#").length; i++) {
                    dados.put("modelo", "venda");
                    dados.put("operacao", "busca");
                    dados.put("posicao", "cod");
                    dados.put("cod", compras.split("#")[i]);
                    dados = controlador.recebeOperacao(dados);
                    Object linha[] = new Object[]{};
                    if (dados != null) {
                        if (dados.get("avista").equals("true")) {
                            linha = new Object[]{dados.get("cod"), dados.get("dataVenda"), "RECEBIDO", dados.get("valorTotal"), "A VISTA", dados.get("dataVenda")};
                        } else {
                            List parcelas = (List) dados.get("parcelas");
                            //exemplo: dataVencimento#valor#datapagamento#situacao#funcionarioRecebeu
                            boolean verificador = true;
                            for (int k = 0; k < parcelas.size(); k++) {
                                String parcela = (String) parcelas.get(k);
                                if (parcela.split("#")[3].equals("false")) { //parcela nao foi paga
                                    linha = new Object[]{dados.get("cod"), dados.get("dataVenda"), "PENDENTE", dados.get("valorTotal"), "A PRAZO", parcela.split("#")[0]};
                                    verificador = false;
                                    break;
                                }
                            }
                            if (verificador) {
                                linha = new Object[]{dados.get("cod"), dados.get("dataVenda"), "RECEBIDO", dados.get("valorTotal"), "A PRAZO", dados.get("dataVenda")};
                            }
                        }
                        modelo.addRow(linha);
                    }
                }
            }
        } else {
            for (int i = 0; i < compras.split("#").length; i++) {
                dados = new HashMap<String, Object>();
                dados.put("modelo", "venda");
                dados.put("operacao", "busca");
                dados.put("posicao", "cod");
                int cod = Integer.parseInt(compras.split("#")[i]);
                dados.put("cod", cod + "");
                dados = controlador.recebeOperacao(dados);
                Object linha[] = new Object[]{};
                if (compra.equals("PENDENTES")) {
                    if (dados.get("avista").equals("false")) {
                        List parcelas = (List) dados.get("parcelas");
                        //exemplo: dataVencimento#valor#datapagamento#situacao#funcionarioRecebeu
                        for (int k = 0; k < parcelas.size(); k++) {
                            String parcela = (String) parcelas.get(k);
                            if (parcela.split("#")[3].equals("false")) { //parcela nao foi paga
                                linha = new Object[]{dados.get("cod"), dados.get("dataVenda"), "PENDENTE", dados.get("valorTotal"), "A PRAZO", parcela.split("#")[0]};
                                modelo.addRow(linha);
                                break;
                            }
                        }
                    }
                } else {
                    if (compra.equals("RECEBIDAS")) {
                        if (dados.get("avista").equals("true")) {
                            linha = new Object[]{dados.get("cod"), dados.get("dataVenda"), "RECEBIDO", dados.get("valorTotal"), "A VISTA", dados.get("dataVenda")};
                            modelo.addRow(linha);
                        } else {
                            List parcelas = (List) dados.get("parcelas");
                            //exemplo: dataVencimento#valor#datapagamento#situacao#funcionarioRecebeu
                            boolean verificador = true;
                            for (int k = 0; k < parcelas.size(); k++) {
                                String parcela = (String) parcelas.get(k);
                                if (parcela.split("#")[3].equals("false")) { //parcela nao foi paga
                                    verificador = false;
                                    break;
                                }
                            }
                            if (verificador) { //venda recebida
                                linha = new Object[]{dados.get("cod"), dados.get("dataVenda"), "RECEBIDO", dados.get("valorRestante"), "A PRAZO", dados.get("dataVenda")};
                                modelo.addRow(linha);
                            }
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_jComboBoxComprasActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new CadastroClienteGer().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAnterior;
    private javax.swing.JButton jButtonLimpar;
    private javax.swing.JButton jButtonPrimeiro;
    private javax.swing.JButton jButtonProximo;
    private javax.swing.JButton jButtonSalvarCliente;
    private javax.swing.JButton jButtonUltimo;
    private javax.swing.JButton jButtonVoltar;
    private javax.swing.JCheckBox jCheckBoxCadastroAprovado;
    private javax.swing.JCheckBox jCheckBoxCasaPropria;
    private javax.swing.JComboBox jComboBoxCidade;
    private javax.swing.JComboBox jComboBoxCompras;
    private javax.swing.JComboBox jComboBoxLogradouro;
    private javax.swing.JComboBox jComboBoxLogradouroTrabalho;
    private javax.swing.JComboBox jComboBoxSexoCliente;
    private javax.swing.JComboBox jComboBoxSexoConjuge;
    private javax.swing.JComboBox jComboBoxUF;
    private javax.swing.JFormattedTextField jFormattedTextFieldCEP;
    private javax.swing.JFormattedTextField jFormattedTextFieldCPFCliente;
    private javax.swing.JFormattedTextField jFormattedTextFieldCPFConjuge;
    private javax.swing.JFormattedTextField jFormattedTextFieldCelCliente;
    private javax.swing.JFormattedTextField jFormattedTextFieldCelConjuge;
    private javax.swing.JFormattedTextField jFormattedTextFieldDataAdmissao;
    private javax.swing.JFormattedTextField jFormattedTextFieldDataCadastro;
    private javax.swing.JFormattedTextField jFormattedTextFieldFoneCliente;
    private javax.swing.JFormattedTextField jFormattedTextFieldFoneInfPessoal1;
    private javax.swing.JFormattedTextField jFormattedTextFieldFoneInfPessoal2;
    private javax.swing.JFormattedTextField jFormattedTextFieldFoneRefCom1;
    private javax.swing.JFormattedTextField jFormattedTextFieldFoneRefCom2;
    private javax.swing.JFormattedTextField jFormattedTextFieldFoneRefCom3;
    private javax.swing.JFormattedTextField jFormattedTextFieldFoneTrabalho;
    private javax.swing.JFormattedTextField jFormattedTextFieldNascCliente;
    private javax.swing.JFormattedTextField jFormattedTextFieldNascConjuge;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelBairro1;
    private javax.swing.JLabel jLabelBairroTrabalho1;
    private javax.swing.JLabel jLabelCEP1;
    private javax.swing.JLabel jLabelCPF2;
    private javax.swing.JLabel jLabelCPF3;
    private javax.swing.JLabel jLabelCelConjuge1;
    private javax.swing.JLabel jLabelCelular1;
    private javax.swing.JLabel jLabelCidade1;
    private javax.swing.JLabel jLabelCodigoCliente;
    private javax.swing.JLabel jLabelComplemento1;
    private javax.swing.JLabel jLabelComplemento2;
    private javax.swing.JLabel jLabelData;
    private javax.swing.JLabel jLabelDataAdmissao1;
    private javax.swing.JLabel jLabelEmail1;
    private javax.swing.JLabel jLabelEmpresa1;
    private javax.swing.JLabel jLabelEstadoCivil1;
    private javax.swing.JLabel jLabelFoneRefCom1;
    private javax.swing.JLabel jLabelFoneRefCom2;
    private javax.swing.JLabel jLabelFoneRefCom4;
    private javax.swing.JLabel jLabelFoneRefCom5;
    private javax.swing.JLabel jLabelFoneRefCom6;
    private javax.swing.JLabel jLabelFoneTrabalho1;
    private javax.swing.JLabel jLabelFuncao1;
    private javax.swing.JLabel jLabelHora;
    private javax.swing.JLabel jLabelLogin;
    private javax.swing.JLabel jLabelNasc1;
    private javax.swing.JLabel jLabelNasc3;
    private javax.swing.JLabel jLabelNasc4;
    private javax.swing.JLabel jLabelNomeCli2;
    private javax.swing.JLabel jLabelNomeCli3;
    private javax.swing.JLabel jLabelNomeRefCom1;
    private javax.swing.JLabel jLabelNomeRefCom2;
    private javax.swing.JLabel jLabelNomeRefCom4;
    private javax.swing.JLabel jLabelNomeRefCom5;
    private javax.swing.JLabel jLabelNomeRefCom6;
    private javax.swing.JLabel jLabelNumTrabalho2;
    private javax.swing.JLabel jLabelNumTrabalho3;
    private javax.swing.JLabel jLabelRG2;
    private javax.swing.JLabel jLabelRG3;
    private javax.swing.JLabel jLabelSalario1;
    private javax.swing.JLabel jLabelSexo2;
    private javax.swing.JLabel jLabelSexo3;
    private javax.swing.JLabel jLabelTelefone1;
    private javax.swing.JLabel jLabelTempoResidencia1;
    private javax.swing.JLabel jLabelUF1;
    private javax.swing.JLabel jLabelValorAluguel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableCompras;
    private javax.swing.JTextArea jTextAreaObservacao;
    private javax.swing.JTextField jTextFieldAluguel;
    private javax.swing.JTextField jTextFieldBairro;
    private javax.swing.JTextField jTextFieldBairroTrabalho;
    private javax.swing.JTextField jTextFieldCodigoCliente;
    private javax.swing.JTextField jTextFieldComplemento;
    private javax.swing.JTextField jTextFieldComplementoTrabalho;
    private javax.swing.JTextField jTextFieldEmail;
    private javax.swing.JTextField jTextFieldEmpresa;
    private javax.swing.JTextField jTextFieldEstadoCivil;
    private javax.swing.JTextField jTextFieldFuncao;
    private javax.swing.JTextField jTextFieldInfPessoal1;
    private javax.swing.JTextField jTextFieldInfPessoal2;
    private javax.swing.JTextField jTextFieldNomeCliente;
    private javax.swing.JTextField jTextFieldNomeConjuge;
    private javax.swing.JTextField jTextFieldNomeLogradouro;
    private javax.swing.JTextField jTextFieldNomeLogradouroTrabalho;
    private javax.swing.JTextField jTextFieldNomeRefCom1;
    private javax.swing.JTextField jTextFieldNomeRefCom2;
    private javax.swing.JTextField jTextFieldNomeRefCom3;
    private javax.swing.JTextField jTextFieldNum;
    private javax.swing.JTextField jTextFieldNumTrabalho;
    private javax.swing.JTextField jTextFieldRGCliente;
    private javax.swing.JTextField jTextFieldRGConjuge;
    private javax.swing.JTextField jTextFieldSalario;
    private javax.swing.JTextField jTextFieldTempoResidencia;
    private org.netbeans.examples.lib.timerbean.Timer timer1;
    // End of variables declaration//GEN-END:variables

    public void mascaraCEP() {
        MaskFormatter format = null;
        jFormattedTextFieldCEP.setFormatterFactory(null);
        try {
            format = new MaskFormatter("##.###-###");
            format.setPlaceholderCharacter('_');
        } catch (Exception ex) {
        }
        jFormattedTextFieldCEP.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldCEP.setValue(null);
    }

    public void mascaraCPF() {
        MaskFormatter format = null;
        jFormattedTextFieldCPFCliente.setFormatterFactory(null);
        jFormattedTextFieldCPFConjuge.setFormatterFactory(null);
        try {
            format = new MaskFormatter("###.###.###-##");
            format.setPlaceholderCharacter('0');
        } catch (Exception ex) {
        }
        jFormattedTextFieldCPFCliente.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldCPFCliente.setValue(null);
        jFormattedTextFieldCPFConjuge.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldCPFConjuge.setValue(null);
    }

    public void mascaraData() {
        MaskFormatter format = null;
        jFormattedTextFieldDataCadastro.setFormatterFactory(null);
        jFormattedTextFieldNascCliente.setFormatterFactory(null);
        jFormattedTextFieldNascConjuge.setFormatterFactory(null);
        jFormattedTextFieldDataAdmissao.setFormatterFactory(null);
        try {
            format = new MaskFormatter("##/##/####");
            format.setPlaceholderCharacter('0');
        } catch (Exception ex) {
            System.out.println("Erro ao carregar mascara data");
        }
        jFormattedTextFieldDataCadastro.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldDataCadastro.setValue(null);
        jFormattedTextFieldNascCliente.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldNascCliente.setValue(null);
        jFormattedTextFieldNascConjuge.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldNascConjuge.setValue(null);
        jFormattedTextFieldDataAdmissao.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldDataAdmissao.setValue(null);
    }

    public void mascaraTEL() {
        MaskFormatter format = null;
        jFormattedTextFieldFoneCliente.setFormatterFactory(null);
        jFormattedTextFieldCelCliente.setFormatterFactory(null);
        jFormattedTextFieldFoneTrabalho.setFormatterFactory(null);
        jFormattedTextFieldCelConjuge.setFormatterFactory(null);
        jFormattedTextFieldFoneInfPessoal1.setFormatterFactory(null);
        jFormattedTextFieldFoneInfPessoal2.setFormatterFactory(null);
        jFormattedTextFieldFoneRefCom1.setFormatterFactory(null);
        jFormattedTextFieldFoneRefCom2.setFormatterFactory(null);
        jFormattedTextFieldFoneRefCom3.setFormatterFactory(null);
        try {
            format = new MaskFormatter("(##)####-####");
            format.setPlaceholderCharacter('_');
        } catch (Exception ex) {
            System.out.println("Erro carregar mascara telefone");
        }
        jFormattedTextFieldFoneCliente.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldFoneCliente.setValue(null);
        jFormattedTextFieldCelCliente.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldCelCliente.setValue(null);
        jFormattedTextFieldFoneTrabalho.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldFoneTrabalho.setValue(null);
        jFormattedTextFieldCelConjuge.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldCelConjuge.setValue(null);
        jFormattedTextFieldFoneInfPessoal1.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldFoneInfPessoal1.setValue(null);
        jFormattedTextFieldFoneInfPessoal2.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldFoneInfPessoal2.setValue(null);
        jFormattedTextFieldFoneRefCom1.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldFoneRefCom1.setValue(null);
        jFormattedTextFieldFoneRefCom2.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldFoneRefCom2.setValue(null);
        jFormattedTextFieldFoneRefCom3.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldFoneRefCom3.setValue(null);
    }

    public void validaCPF(String cpf, String pessoa) {
        if (cpf.length() > 0) {
            String aux = "";
            for (int i = 0; i < cpf.length(); i++) {
                if ((cpf.charAt(i) != '.') && (cpf.charAt(i) != '-') && (cpf.charAt(i) != ' ')) {
                    aux += cpf.charAt(i);
                }
            }

            if (CPF.validaCPF(aux)) {
                if (pessoa.equals("cliente")) {
                    jFormattedTextFieldCPFCliente.setBackground(Color.CYAN);
                    cpfValido = true;
                } else {
                    jFormattedTextFieldCPFConjuge.setBackground(Color.CYAN);
                }

            } else {
                if (pessoa.equals("cliente")) {
                    jFormattedTextFieldCPFCliente.setBackground(Color.RED);
                    cpfValido = false;
                } else {
                    jFormattedTextFieldCPFConjuge.setBackground(Color.RED);
                }
            }
        }
    }

    public void modificaConsultar() {
        jButtonSalvarCliente.setText("Alterar");
        jButtonLimpar.setText("Excluir");
        this.cadastrar = false;
        this.jButtonAnterior.setEnabled(true);
        this.jButtonPrimeiro.setEnabled(true);
        this.jButtonProximo.setEnabled(true);
        this.jButtonUltimo.setEnabled(true);
        jTabbedPane1.setSelectedIndex(0);
        jTextFieldNomeCliente.requestFocus();
        jButtonSalvarCliente.setToolTipText("Alterar Cliente");
        jButtonLimpar.setToolTipText("Excluir Cliente");
        jButtonVoltar.setToolTipText("Retorna para a tela principal e nao altera o cliente");
    }

    public void validaNasc(String nascimento, String pessoa) {
        if (nascimento.length() > 0) {
            String[] y = nascimento.split("/");
            int i = Integer.parseInt(y[2]);
            DataSistema d = DataSistema.getInstance();
            if ((i > (Integer.parseInt((d.diaMesAno().split(" ")[1]).split("/")[2]) - 16)) || (i == 0) || (i < 1900)) {
                if (pessoa.equals("cliente")) {
                    jFormattedTextFieldNascCliente.setBackground(Color.RED);
                    nascValido = false;
                } else {
                    jFormattedTextFieldNascConjuge.setBackground(Color.RED);
                }
            } else {
                if (pessoa.equals("cliente")) {
                    jFormattedTextFieldNascCliente.setBackground(Color.CYAN);
                    nascValido = true;
                } else {
                    jFormattedTextFieldNascConjuge.setBackground(Color.CYAN);
                }
            }
        }
    }

    private void limpaTabela() {
        modelo = (DefaultTableModel) jTableCompras.getModel();
        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }
    }

    private String pegaTabelaCompras() {
        HashMap<String, Object> dados = new HashMap<String, Object>();
        dados.put("modelo", "cliente");
        dados.put("operacao", "busca");
        dados.put("cod", jTextFieldCodigoCliente.getText());
        dados.put("posicao", "cod");
        dados = controlador.recebeOperacao(dados);
        return (String) dados.get("compras");
    }
}
