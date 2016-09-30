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
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import persistencia.DAO;
import persistencia.DAOFuncionarioDB4O;
import utilitarios.*;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Hemerson e Jefferson
 */
public class CadastroFuncionarioGer extends javax.swing.JFrame {

    private UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
    boolean cadastrar = true;
    DAO dao = new DAOFuncionarioDB4O();
    boolean ordem = true;
    Controlador controlador;
    boolean cpfValido = false;
    DefaultTableModel modelo;
    DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
    private Sessao sessao;

    public CadastroFuncionarioGer() {
        initComponents();
        jTextFieldCodigoFuncionario.setText(this.getMaiorId());
        timer1.start();
    }

    public CadastroFuncionarioGer(Sessao sessao) {
        initComponents();
        this.construtor(sessao);
        jTextFieldCodigoFuncionario.setText(this.getMaiorId());
    }

    public CadastroFuncionarioGer(HashMap<String, Object> c, Sessao sessao) {
        initComponents();
        this.construtor(sessao);
        this.mostraDados(c);
        this.modificaConsultar();
    }

    private void alinhaColunas() {
        cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        jTable1.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
        jTable1.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
        jTable1.getColumnModel().getColumn(2).setCellRenderer(cellRenderer);
        jTable1.getColumnModel().getColumn(3).setCellRenderer(cellRenderer);
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
        DataSistema d = DataSistema.getInstance();
        jFormattedTextFieldDataCadastro.setValue(d.diaMesAno().split(" ")[1]);
        this.jCheckBoxCasaPropria.setSelected(true);
        this.cadastrar = true;
        this.cpfValido = false;
        this.carregaCidades("PR", 2);
        this.carregaCidades("PR", 1);
        jTextFieldNomeFuncionario.requestFocus();
        jTable1.getTableHeader().setReorderingAllowed(false);
        this.alinhaColunas();
        jLabelLogin.setText(sessao.getLogin());
        this.sessao = new Sessao();
        this.sessao.setLogin(sessao.getLogin());
        this.sessao.setSenha(sessao.getSenha());
        if(!cadastrar){
            jButtonCadastrar.setToolTipText("Alterar funcionario");
            jButtonLimpar.setToolTipText("Excluir funcionario");
            jButtonVoltar.setToolTipText("Retorna para a tela principal e nao altear o funcionario");
        }else{
            jButtonCadastrar.setToolTipText("Cadastrar funcionario");
            jButtonLimpar.setToolTipText("Limpa todos os campos");
            jButtonVoltar.setToolTipText("Retorna para a tela principal e nao cadastra o funcionario");
        }
    }

    /** Creates new form CadastroFuncionario */
    private String cadastraFuncionario() {
        if (jTextFieldNomeFuncionario.getText().compareTo("") != 0) {
            HashMap<String, Object> dadosFuncionario = new HashMap(this.pegaDados());
            if (cadastrar) {
                if (cpfValido) {
                    HashMap<String, Object> dados = new HashMap<String, Object>();
                    dados.put("nome", jTextFieldNomeFuncionario.getText().toUpperCase());
                    dados.put("cpf", (String) jFormattedTextFieldCPFFuncionario.getValue());
                    //verificar se ja existe um funcionario com este nome e cpf;
                    dados.put("operacao", "consultar");
                    dados.put("modelo", "funcionario");
                    HashMap<String, Object> retorno = controlador.recebeOperacao(dados);

                    if (retorno != null) {
                        JOptionPane.showMessageDialog(null, "Este funcionario ja esta cadastrado no base de dados do sistema!");
                        dadosFuncionario = null;
                    } else {
                        dadosFuncionario = new HashMap<String, Object>(this.pegaDados());
                        dadosFuncionario.put("operacao", "inserir");
                        dadosFuncionario.put("modelo", "funcionario");
                        dados = controlador.recebeOperacao(dadosFuncionario);
                        JOptionPane.showMessageDialog(null, (String) dados.get("retorno"));
                        return "sucesso";
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Digite um cpf valido para o novo funcionario!");
                    return "";
                }
            } else {
                dadosFuncionario = this.pegaDados();
                dadosFuncionario.put("operacao", "alterar");
                dadosFuncionario.put("modelo", "funcionario");
                dadosFuncionario = controlador.recebeOperacao(dadosFuncionario);
                JOptionPane.showMessageDialog(null, (String) dadosFuncionario.get("retorno"));
                return "sucesso";
            }

        } else {
            JOptionPane.showMessageDialog(null, "Digite um nome para o novo funcionario!");
            return "";
        }
        return "";
    }

    private String getMaiorId() {
        HashMap<String, Object> dados = new HashMap<String, Object>();
        dados.put("modelo", "funcionario");
        dados.put("operacao", "maiorId");
        dados = controlador.recebeOperacao(dados);
        if (dados != null) {
            return (String) dados.get("retorno");
        }
        return "";
    }

    private HashMap pegaDados() {
        HashMap<String, Object> dadosFuncionario = new HashMap<String, Object>();
        dadosFuncionario.put("nome", jTextFieldNomeFuncionario.getText().toUpperCase());
        dadosFuncionario.put("cpf", (String) jFormattedTextFieldCPFFuncionario.getValue());
        String aux = "";
        try {
            aux = jTextFieldCodigoFuncionario.getText();
        } catch (Exception exception) {
            aux = "1";
        }
        dadosFuncionario.put("cod", aux);
        aux = (String) jFormattedTextFieldDataCadastro.getValue();
        if (aux == null) {
            aux = "00/00/0000";
        }
        dadosFuncionario.put("dataCadastro", aux);

        // ------------- DADOS PESSOAIS -----------------
        aux = (String) jFormattedTextFieldNascFuncionario.getValue();
        if (aux == null) {
            aux = "00/00/0000";
        }
        dadosFuncionario.put("dataNasc", aux);
        dadosFuncionario.put("rg", jTextFieldRGFuncionario.getText());
        dadosFuncionario.put("sexo", (String) jComboBoxSexoFuncionario.getSelectedItem());
        dadosFuncionario.put("telefone", (String) jFormattedTextFieldFoneFuncionario.getValue());
        dadosFuncionario.put("celular", (String) jFormattedTextFieldCelFuncionario.getValue());
        dadosFuncionario.put("estadoCivil", jTextFieldEstadoCivilFuncionario.getText().toUpperCase());
        dadosFuncionario.put("email", jTextFieldEmailFuncionario.getText().toLowerCase());

        // ------------- ENDERECO FUNCIONARIO -----------------
        dadosFuncionario.put("tipoLogradouro", (String) jComboBoxLogradouro.getSelectedItem());
        dadosFuncionario.put("nomeLogradouro", jTextFieldNomeLogradouro.getText().toUpperCase());
        int auxInt = 0;
        try {
            auxInt = Integer.parseInt(jTextFieldNum.getText());
        } catch (Exception exception) {
            auxInt = 0;
        }
        dadosFuncionario.put("numLogradouro", "" + auxInt);
        dadosFuncionario.put("complemento", jTextFieldComplemento.getText().toUpperCase());
        dadosFuncionario.put("bairro", jTextFieldBairro.getText().toUpperCase());
        dadosFuncionario.put("estado", (String) jComboBoxUF.getSelectedItem());
        dadosFuncionario.put("cidade", (String) jComboBoxCidade.getSelectedItem());
        dadosFuncionario.put("cep", (String) jFormattedTextFieldCEP.getValue());
        if (!jCheckBoxCasaPropria.isSelected()) {
            dadosFuncionario.put("propria", "false");
            dadosFuncionario.put("valorAluguel", this.trataPreco(jTextFieldAluguel.getText()));
        } else {
            dadosFuncionario.put("propria", "true");
        }
        dadosFuncionario.put("tempoResidencia", jTextFieldTempoResidencia.getText().toUpperCase());

        // ------------- CONJUGE -----------------
        dadosFuncionario.put("nomeConjuge", jTextFieldNomeConjuge.getText().toUpperCase());
        aux = jFormattedTextFieldNascConjuge.getText();
        if (aux.compareTo("") == 0) {
            aux = "00/00/0000";
        }
        dadosFuncionario.put("nascConjuge", aux);
        dadosFuncionario.put("rgConjuge", jTextFieldRGConjuge.getText());
        dadosFuncionario.put("cpfConjuge", (String) jFormattedTextFieldCPFConjuge.getValue());
        dadosFuncionario.put("sexoConjuge", (String) jComboBoxSexoConjuge.getSelectedItem());
        dadosFuncionario.put("celularConjuge", (String) jFormattedTextFieldCelConjuge.getValue());

        // ------------- TRABALHO -----------------
        dadosFuncionario.put("funcao", jTextFieldFuncao.getText());
        dadosFuncionario.put("salario", this.trataPreco(jTextFieldSalario.getText()));
        aux = (String) jFormattedTextFieldDataAdmissao.getValue();
        if (aux == null) {
            aux = "00/00/0000";
        }
        dadosFuncionario.put("dataAdmissao", aux);
        if (jCheckGerente.isSelected()) {
            dadosFuncionario.put("gerente", "true");
        } else {
            dadosFuncionario.put("gerente", "false");
        }

        // ------------- REFERENCIAS COMERCIAIS -----------------
        dadosFuncionario.put("refCom1", jTextFieldNomeRefCom1.getText().toUpperCase() + "#" + jFormattedTextFieldFoneRefCom1.getValue());
        dadosFuncionario.put("refCom2", jTextFieldNomeRefCom2.getText().toUpperCase() + "#" + jFormattedTextFieldFoneRefCom2.getValue());
        dadosFuncionario.put("refCom3", jTextFieldNomeRefCom3.getText().toUpperCase() + "#" + jFormattedTextFieldFoneRefCom3.getValue());

        // ------------- LOGIN E SENHA -----------------
        dadosFuncionario.put("login", jTextFieldLogin.getText().toLowerCase());
        Sessao s = new Sessao();
        s.setSenha(jPasswordFieldSenha1.getText());//criptografia
        dadosFuncionario.put("senha", s.getSenhaCriptografada());
        if (cadastrar) {
            dadosFuncionario.put("vendas", "");
        } else {
            dadosFuncionario.put("vendas", this.pegaTabelaVendas());
        }
        dadosFuncionario.put("ativo", "true");
        return dadosFuncionario;
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

    private void mostraDados(HashMap<String, Object> funcionario) {
        if (funcionario != null) {
            String aux;
            jFormattedTextFieldDataCadastro.setValue((String) funcionario.get("dataCadastro"));
            // ------------- DADOS PESSOAIS -----------------
            jTextFieldCodigoFuncionario.setText((String) funcionario.get("cod"));
            jTextFieldNomeFuncionario.setText((String) funcionario.get("nome"));
            jFormattedTextFieldNascFuncionario.setValue((String) funcionario.get("dataNasc"));
            jTextFieldRGFuncionario.setText((String) funcionario.get("rg"));
            jFormattedTextFieldCPFFuncionario.setValue((String) funcionario.get("cpf"));
            jComboBoxSexoFuncionario.setSelectedItem((String) funcionario.get("sexo"));
            jFormattedTextFieldFoneFuncionario.setValue((String) funcionario.get("telefone"));
            jFormattedTextFieldCelFuncionario.setValue((String) funcionario.get("celular"));
            jTextFieldEstadoCivilFuncionario.setText((String) funcionario.get("estadoCivil"));
            jTextFieldEmailFuncionario.setText((String) funcionario.get("email"));

            // ------------- ENDERECO FUNCIONARIO -----------------
            jComboBoxLogradouro.setSelectedItem((String) funcionario.get("tipoLogradouro"));
            jTextFieldNomeLogradouro.setText((String) funcionario.get("nomeLogradouro"));
            jTextFieldNum.setText((String) funcionario.get("numLogradouro"));
            jTextFieldComplemento.setText((String) funcionario.get("complemento"));
            jTextFieldBairro.setText((String) funcionario.get("bairro"));
            jComboBoxUF.setSelectedItem((String) funcionario.get("estado"));
            jComboBoxCidade.setSelectedItem((String) funcionario.get("cidade"));
            jFormattedTextFieldCEP.setValue((String) funcionario.get("cep"));
            if (((String) funcionario.get("propria")).equals("true")) {
                jCheckBoxCasaPropria.setSelected(true);
            } else {
                jCheckBoxCasaPropria.setSelected(false);
                jTextFieldAluguel.setText((String) funcionario.get("valorAluguel"));
            }
            jTextFieldTempoResidencia.setText((String) funcionario.get("tempoResidencia"));

            // ------------- CONJUGE -----------------
            jTextFieldNomeConjuge.setText((String) funcionario.get("nomeConjuge"));
            jFormattedTextFieldNascConjuge.setValue((String) funcionario.get("nascConjuge"));
            jTextFieldRGConjuge.setText((String) funcionario.get("rgConjuge"));
            jFormattedTextFieldCPFConjuge.setValue((String) funcionario.get("cpfConjuge"));
            jComboBoxSexoConjuge.setSelectedItem((String) funcionario.get("sexoConjuge"));
            jFormattedTextFieldCelConjuge.setValue((String) funcionario.get("celularConjuge"));

            // ------------- TRABALHO -----------------
            jTextFieldFuncao.setText((String) funcionario.get("funcao"));
            jTextFieldSalario.setText((String) funcionario.get("salario"));
            jFormattedTextFieldDataAdmissao.setValue(funcionario.get("dataAdmissao"));
            if(((String)funcionario.get("gerente")).equals("true")){
                jCheckGerente.setSelected(true);
            }else{
                jCheckGerente.setSelected(false);
            }

            // ------------- REFERENCIAS COMERCIAIS -----------------
            aux = (String) funcionario.get("refCom1");
            jTextFieldNomeRefCom1.setText(aux.split("#")[0]);
            jFormattedTextFieldFoneRefCom1.setValue(aux.split("#")[1]);
            aux = (String) funcionario.get("refCom2");
            jTextFieldNomeRefCom2.setText(aux.split("#")[0]);
            jFormattedTextFieldFoneRefCom2.setValue(aux.split("#")[1]);
            aux = (String) funcionario.get("refCom3");
            jTextFieldNomeRefCom3.setText(aux.split("#")[0]);
            jFormattedTextFieldFoneRefCom3.setValue(aux.split("#")[1]);

            // ------------- LOGIN E SENHA -----------------
            jTextFieldLogin.setText((String) funcionario.get("login"));
            Sessao s = new Sessao();
            s.setSenha((String) funcionario.get("senha"));
            jPasswordFieldSenha1.setText(s.getSenha());
            jPasswordFieldSenha2.setText(s.getSenha());

            // ---------------- TABELA DE VENDAS -----------------
            this.limpaTabela();
            modelo = (DefaultTableModel) jTable1.getModel();
            String str = (String) funcionario.get("vendas");
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
                            linha = new Object[]{codVenda, dadosVenda.get("dataVenda"), "A VISTA", dadosVenda.get("valorTotal")};
                        } else {
                            linha = new Object[]{codVenda, dadosVenda.get("dataVenda"), "A PRAZO", dadosVenda.get("valorTotal")};
                        }
                        modelo.addRow(linha);
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    private void limpaTabela() {
        modelo = (DefaultTableModel) jTable1.getModel();
        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        timer1 = new org.netbeans.examples.lib.timerbean.Timer();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jLabelNomeCli2 = new javax.swing.JLabel();
        jTextFieldNomeFuncionario = new javax.swing.JTextField();
        jLabelRG2 = new javax.swing.JLabel();
        jTextFieldRGFuncionario = new javax.swing.JTextField();
        jComboBoxSexoFuncionario = new javax.swing.JComboBox();
        jLabelSexo2 = new javax.swing.JLabel();
        jFormattedTextFieldNascFuncionario = new javax.swing.JFormattedTextField();
        jLabelNasc3 = new javax.swing.JLabel();
        jLabelTelefone1 = new javax.swing.JLabel();
        jFormattedTextFieldFoneFuncionario = new javax.swing.JFormattedTextField();
        jLabelCelular1 = new javax.swing.JLabel();
        jFormattedTextFieldCelFuncionario = new javax.swing.JFormattedTextField();
        jTextFieldEstadoCivilFuncionario = new javax.swing.JTextField();
        jLabelEstadoCivil1 = new javax.swing.JLabel();
        jLabelEmail1 = new javax.swing.JLabel();
        jTextFieldEmailFuncionario = new javax.swing.JTextField();
        jLabelCPF2 = new javax.swing.JLabel();
        jFormattedTextFieldCPFFuncionario = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
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
        jTextFieldComplemento = new javax.swing.JTextField();
        jLabelComplemento1 = new javax.swing.JLabel();
        jTextFieldNum = new javax.swing.JTextField();
        jLabelNumTrabalho2 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabelNomeCli3 = new javax.swing.JLabel();
        jTextFieldNomeConjuge = new javax.swing.JTextField();
        jTextFieldRGConjuge = new javax.swing.JTextField();
        jLabelRG3 = new javax.swing.JLabel();
        jLabelCPF3 = new javax.swing.JLabel();
        jFormattedTextFieldCPFConjuge = new javax.swing.JFormattedTextField();
        jComboBoxSexoConjuge = new javax.swing.JComboBox();
        jLabelSexo3 = new javax.swing.JLabel();
        jFormattedTextFieldNascConjuge = new javax.swing.JFormattedTextField();
        jLabelNasc4 = new javax.swing.JLabel();
        jFormattedTextFieldCelConjuge = new javax.swing.JFormattedTextField();
        jLabelCelConjuge1 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jTextFieldNomeRefCom1 = new javax.swing.JTextField();
        jLabelNomeRefCom1 = new javax.swing.JLabel();
        jFormattedTextFieldFoneRefCom1 = new javax.swing.JFormattedTextField();
        jLabelFoneRefCom1 = new javax.swing.JLabel();
        jTextFieldNomeRefCom2 = new javax.swing.JTextField();
        jLabelNomeRefCom4 = new javax.swing.JLabel();
        jFormattedTextFieldFoneRefCom2 = new javax.swing.JFormattedTextField();
        jLabelFoneRefCom4 = new javax.swing.JLabel();
        jTextFieldNomeRefCom3 = new javax.swing.JTextField();
        jLabelNomeRefCom5 = new javax.swing.JLabel();
        jFormattedTextFieldFoneRefCom3 = new javax.swing.JFormattedTextField();
        jLabelFoneRefCom5 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jTextFieldFuncao = new javax.swing.JTextField();
        jLabelNomeConjuge1 = new javax.swing.JLabel();
        jLabelNascConjuge1 = new javax.swing.JLabel();
        jFormattedTextFieldDataAdmissao = new javax.swing.JFormattedTextField();
        jLabelRGConjuge1 = new javax.swing.JLabel();
        jTextFieldSalario = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldLogin = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPasswordFieldSenha1 = new javax.swing.JPasswordField();
        jPasswordFieldSenha2 = new javax.swing.JPasswordField();
        jCheckGerente = new javax.swing.JCheckBox();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jButtonDataAtual = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jButtonCadastrar = new javax.swing.JButton();
        jButtonLimpar = new javax.swing.JButton();
        jButtonVoltar = new javax.swing.JButton();
        jButtonPrimeiro = new javax.swing.JButton();
        jButtonAnterior = new javax.swing.JButton();
        jButtonProximo = new javax.swing.JButton();
        jButtonUltimo = new javax.swing.JButton();
        jLabelCodigoCliente = new javax.swing.JLabel();
        jTextFieldCodigoFuncionario = new javax.swing.JTextField();
        jLabelNasc1 = new javax.swing.JLabel();
        jFormattedTextFieldDataCadastro = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
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
        setTitle("CADASTRO DE FUNCIONARIOS");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jTabbedPane2.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        jLabelNomeCli2.setText("Nome: ");

        jTextFieldNomeFuncionario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldNomeFuncionarioFocusLost(evt);
            }
        });

        jLabelRG2.setText("RG: ");

        jTextFieldRGFuncionario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldRGFuncionarioActionPerformed(evt);
            }
        });

        jComboBoxSexoFuncionario.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Feminino", "Masculino" }));
        jComboBoxSexoFuncionario.setToolTipText("Escolha o sexo");

        jLabelSexo2.setText("Sexo:");

        jFormattedTextFieldNascFuncionario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextFieldNascFuncionarioKeyReleased(evt);
            }
        });

        jLabelNasc3.setText("Nasc.:");

        jLabelTelefone1.setText("Fone: ");

        jLabelCelular1.setText("Celular: ");

        jFormattedTextFieldCelFuncionario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jFormattedTextFieldCelFuncionarioActionPerformed(evt);
            }
        });

        jLabelEstadoCivil1.setText("Est. Civil:");

        jLabelEmail1.setText("E-mail: ");

        jTextFieldEmailFuncionario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldEmailFuncionarioFocusLost(evt);
            }
        });

        jLabelCPF2.setText("CPF: ");

        jFormattedTextFieldCPFFuncionario.setToolTipText("Somente numeros");
        jFormattedTextFieldCPFFuncionario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextFieldCPFFuncionarioKeyReleased(evt);
            }
        });

        jLabel4.setForeground(new java.awt.Color(255, 0, 0));
        jLabel4.setText("(*)");

        jLabel5.setForeground(new java.awt.Color(255, 0, 0));
        jLabel5.setText("(*)");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelCPF2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelNomeCli2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelNasc3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelTelefone1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelEmail1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelEstadoCivil1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldEstadoCivilFuncionario, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
                    .addComponent(jTextFieldEmailFuncionario, javax.swing.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jFormattedTextFieldNascFuncionario, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                            .addComponent(jFormattedTextFieldCPFFuncionario, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE)
                            .addComponent(jFormattedTextFieldFoneFuncionario, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 122, Short.MAX_VALUE))
                        .addGap(4, 4, 4)
                        .addComponent(jLabel4)
                        .addGap(36, 36, 36)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelCelular1, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                            .addComponent(jLabelSexo2, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE)
                            .addComponent(jLabelRG2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextFieldRGFuncionario)
                            .addComponent(jComboBoxSexoFuncionario, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jFormattedTextFieldCelFuncionario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                        .addComponent(jTextFieldNomeFuncionario, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNomeCli2)
                    .addComponent(jTextFieldNomeFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextFieldRGFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelCPF2)
                        .addComponent(jFormattedTextFieldCPFFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel4))
                    .addComponent(jLabelRG2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jFormattedTextFieldNascFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBoxSexoFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelSexo2))
                    .addComponent(jLabelNasc3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelTelefone1)
                    .addComponent(jFormattedTextFieldFoneFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFormattedTextFieldCelFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelCelular1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelEstadoCivil1)
                    .addComponent(jTextFieldEstadoCivilFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelEmail1)
                    .addComponent(jTextFieldEmailFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Dados Pessoais", jPanel6);

        jLabelCEP1.setText("CEP: ");

        jLabelBairro1.setText("Bairro: ");

        jLabelUF1.setText("UF: ");

        jComboBoxUF.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PR", "AC", "AL", "AM", "AP", "BA", "CE", "DF", "ES", "GO", "MA", "MG", "MS", "MT", "PA", "PB", "PE", "PI", "RJ", "RN", "RO", "RR", "RS", "SC", "SE", "SP", "TO" }));
        jComboBoxUF.setSelectedItem("PR");
        jComboBoxUF.setToolTipText("Escolha um estado e depois uma cidade");
        jComboBoxUF.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBoxUFItemStateChanged(evt);
            }
        });

        jLabelCidade1.setText("Cidade: ");

        jComboBoxCidade.setToolTipText("Escolha uma cidade de acordo com o estado escolhido");

        jTextFieldAluguel.setToolTipText("Valor do aluguel");
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
        jCheckBoxCasaPropria.setToolTipText("Casa propria ou alugada");
        jCheckBoxCasaPropria.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxCasaPropriaItemStateChanged(evt);
            }
        });

        jTextFieldTempoResidencia.setToolTipText("tempo de residencia em anos");
        jTextFieldTempoResidencia.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldTempoResidenciaFocusLost(evt);
            }
        });

        jLabelTempoResidencia1.setText("Tempo R.:");

        jComboBoxLogradouro.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Rua", "Avenida", "Travessa", "Estrada", "Rodovia" }));
        jComboBoxLogradouro.setToolTipText("Logradouro");

        jLabelComplemento1.setText("Comp.: ");

        jLabelNumTrabalho2.setText("No: ");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jCheckBoxCasaPropria)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelValorAluguel1, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldAluguel, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelTempoResidencia1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldTempoResidencia, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabelBairro1, javax.swing.GroupLayout.DEFAULT_SIZE, 73, Short.MAX_VALUE)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabelCidade1, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                                            .addComponent(jLabelCEP1, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(jFormattedTextFieldCEP, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 149, Short.MAX_VALUE)
                                        .addComponent(jLabelUF1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComboBoxUF, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jComboBoxCidade, javax.swing.GroupLayout.Alignment.LEADING, 0, 371, Short.MAX_VALUE)
                                    .addComponent(jTextFieldBairro, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(jLabelNumTrabalho2, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextFieldNum, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jComboBoxLogradouro, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel7Layout.createSequentialGroup()
                                        .addComponent(jLabelComplemento1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jTextFieldComplemento, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE))
                                    .addComponent(jTextFieldNomeLogradouro, javax.swing.GroupLayout.DEFAULT_SIZE, 316, Short.MAX_VALUE))))))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldNomeLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldComplemento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNumTrabalho2)
                    .addComponent(jTextFieldNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelComplemento1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelBairro1))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelCEP1)
                            .addComponent(jFormattedTextFieldCEP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxUF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelUF1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBoxCidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelCidade1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBoxCasaPropria)
                    .addComponent(jLabelTempoResidencia1)
                    .addComponent(jTextFieldTempoResidencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldAluguel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelValorAluguel1))
                .addContainerGap())
        );

        jTabbedPane2.addTab("Endereco", jPanel7);

        jLabelNomeCli3.setText("Nome: ");

        jTextFieldRGConjuge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldRGConjugeActionPerformed(evt);
            }
        });

        jLabelRG3.setText("RG: ");

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

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabelNomeCli3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelRG3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelCelConjuge1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelNasc4, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jFormattedTextFieldCelConjuge, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                            .addComponent(jFormattedTextFieldNascConjuge, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldRGConjuge, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabelSexo3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelCPF3, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jComboBoxSexoConjuge, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jFormattedTextFieldCPFConjuge, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)))
                    .addComponent(jTextFieldNomeConjuge, javax.swing.GroupLayout.DEFAULT_SIZE, 372, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNomeCli3)
                    .addComponent(jTextFieldNomeConjuge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jFormattedTextFieldCPFConjuge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelCPF3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBoxSexoConjuge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelSexo3)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelRG3)
                            .addComponent(jTextFieldRGConjuge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelNasc4)
                            .addComponent(jFormattedTextFieldNascConjuge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCelConjuge1)
                    .addComponent(jFormattedTextFieldCelConjuge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(43, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Conjuge", jPanel8);

        jLabelNomeRefCom1.setText("Nome: ");

        jLabelFoneRefCom1.setText("Fone: ");

        jLabelNomeRefCom4.setText("Nome: ");

        jLabelFoneRefCom4.setText("Fone: ");

        jLabelNomeRefCom5.setText("Nome: ");

        jFormattedTextFieldFoneRefCom3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jFormattedTextFieldFoneRefCom3FocusLost(evt);
            }
        });

        jLabelFoneRefCom5.setText("Fone: ");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelFoneRefCom1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelNomeRefCom1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFormattedTextFieldFoneRefCom1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldNomeRefCom1, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelFoneRefCom4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelNomeRefCom4, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFormattedTextFieldFoneRefCom2, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldNomeRefCom2, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelFoneRefCom5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelNomeRefCom5, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFormattedTextFieldFoneRefCom3, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldNomeRefCom3, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNomeRefCom1)
                    .addComponent(jTextFieldNomeRefCom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelFoneRefCom1)
                    .addComponent(jFormattedTextFieldFoneRefCom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNomeRefCom4)
                    .addComponent(jTextFieldNomeRefCom2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelFoneRefCom4)
                    .addComponent(jFormattedTextFieldFoneRefCom2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNomeRefCom5)
                    .addComponent(jTextFieldNomeRefCom3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelFoneRefCom5)
                    .addComponent(jFormattedTextFieldFoneRefCom3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane2.addTab("Inf. Comerciais", jPanel9);

        jLabelNomeConjuge1.setText("Funcao:");

        jLabelNascConjuge1.setText("Data Adm:");

        jFormattedTextFieldDataAdmissao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextFieldDataAdmissaoKeyReleased(evt);
            }
        });

        jLabelRGConjuge1.setText("Salario: R$");

        jTextFieldSalario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldSalarioFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldSalarioFocusLost(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Acesso", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jLabel1.setText("Login:");

        jTextFieldLogin.setEditable(false);
        jTextFieldLogin.setFocusable(false);

        jLabel2.setText("Senha:");

        jPasswordFieldSenha1.setToolTipText("A senha deve conter apenas letras e numeros");
        jPasswordFieldSenha1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPasswordFieldSenha1FocusGained(evt);
            }
        });

        jPasswordFieldSenha2.setToolTipText("Repita a senha para confirmacao");
        jPasswordFieldSenha2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordFieldSenha2ActionPerformed(evt);
            }
        });
        jPasswordFieldSenha2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPasswordFieldSenha2FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jPasswordFieldSenha2FocusLost(evt);
            }
        });

        jCheckGerente.setText("Gerente");
        jCheckGerente.setToolTipText("Privilegio do funcionario");

        jLabel7.setForeground(new java.awt.Color(255, 0, 0));
        jLabel7.setText("(*)");

        jLabel8.setForeground(new java.awt.Color(255, 0, 0));
        jLabel8.setText("(*)");

        jLabel9.setForeground(new java.awt.Color(255, 0, 0));
        jLabel9.setText("(*)");

        jLabel10.setText("Conf.:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextFieldLogin)
                    .addComponent(jPasswordFieldSenha1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPasswordFieldSenha2, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel9))
                    .addComponent(jCheckGerente))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextFieldLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jPasswordFieldSenha1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10)
                            .addComponent(jPasswordFieldSenha2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel7)
                        .addComponent(jCheckGerente)))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jButtonDataAtual.setText(">");
        jButtonDataAtual.setToolTipText("Insere a data atual");
        jButtonDataAtual.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDataAtualActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabelRGConjuge1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelNomeConjuge1, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel10Layout.createSequentialGroup()
                                .addComponent(jTextFieldSalario, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabelNascConjuge1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jFormattedTextFieldDataAdmissao, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonDataAtual, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextFieldFuncao, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelNomeConjuge1)
                    .addComponent(jTextFieldFuncao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelRGConjuge1)
                    .addComponent(jButtonDataAtual)
                    .addComponent(jLabelNascConjuge1)
                    .addComponent(jFormattedTextFieldDataAdmissao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldSalario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(13, 13, 13)
                .addComponent(jPanel4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Trabalho", jPanel10);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cupom", "Data Venda", "Situacao", "Total"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setToolTipText("Relacao de vendas que o funcionario realizou");
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane2.addTab("Vendas", jPanel2);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Opcoes/Navegacao", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION));

        jButtonCadastrar.setText("Cadastrar");
        jButtonCadastrar.setToolTipText("Cadastrar funcionario");
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
        jButtonVoltar.setToolTipText("Cancela o cadastro");
        jButtonVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonVoltarActionPerformed(evt);
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

        jButtonAnterior.setText("<<");
        jButtonAnterior.setToolTipText("Cadastro anterior");
        jButtonAnterior.setEnabled(false);
        jButtonAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAnteriorActionPerformed(evt);
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

        jButtonUltimo.setText(">");
        jButtonUltimo.setToolTipText("Ultimo cadastro");
        jButtonUltimo.setEnabled(false);
        jButtonUltimo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonUltimoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonCadastrar, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonLimpar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonVoltar, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonPrimeiro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonAnterior)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonProximo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonUltimo)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonCadastrar)
                    .addComponent(jButtonLimpar)
                    .addComponent(jButtonPrimeiro)
                    .addComponent(jButtonAnterior)
                    .addComponent(jButtonProximo)
                    .addComponent(jButtonUltimo)
                    .addComponent(jButtonVoltar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabelCodigoCliente.setText("Codigo:");

        jTextFieldCodigoFuncionario.setEditable(false);
        jTextFieldCodigoFuncionario.setFont(new java.awt.Font("Tahoma", 1, 11));
        jTextFieldCodigoFuncionario.setForeground(new java.awt.Color(255, 0, 0));
        jTextFieldCodigoFuncionario.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldCodigoFuncionario.setText("0000");
        jTextFieldCodigoFuncionario.setDisabledTextColor(new java.awt.Color(255, 0, 0));
        jTextFieldCodigoFuncionario.setFocusable(false);

        jLabelNasc1.setText("Data Cadastro:");

        jFormattedTextFieldDataCadastro.setEditable(false);
        jFormattedTextFieldDataCadastro.setForeground(new java.awt.Color(255, 0, 0));
        jFormattedTextFieldDataCadastro.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jFormattedTextFieldDataCadastro.setText("00/00/0000");
        jFormattedTextFieldDataCadastro.setFocusable(false);
        jFormattedTextFieldDataCadastro.setFont(new java.awt.Font("Tahoma", 1, 11));

        jLabel6.setForeground(new java.awt.Color(255, 0, 0));
        jLabel6.setText("(*)");

        jLabel3.setText("Campos obrigatorios");

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel12.setText("Usuario:");

        jLabelHora.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelLogin, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabelData, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelHora, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabelLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
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
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addGap(35, 35, 35))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jTabbedPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 590, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabelCodigoCliente)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextFieldCodigoFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 278, Short.MAX_VALUE)
                                .addComponent(jLabelNasc1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jFormattedTextFieldDataCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelCodigoCliente)
                    .addComponent(jTextFieldCodigoFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jFormattedTextFieldDataCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelNasc1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jButtonVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonVoltarActionPerformed
    new TelaPrincipalGer(sessao).show();
    this.dispose();
}//GEN-LAST:event_jButtonVoltarActionPerformed

private void jButtonLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimparActionPerformed
    if (cadastrar) {
        this.limpa();
    } else {
        int resposta = JOptionPane.showConfirmDialog(null, "Desejar excluir este funcionario?", "Exclusao", JOptionPane.YES_NO_OPTION);
        if (resposta == 0) {
            int cod = Integer.parseInt(this.jTextFieldCodigoFuncionario.getText());
            HashMap<String, Object> dados = this.pegaDados();
            dados.put("operacao", "alterar");
            dados.put("modelo", "funcionario");
            dados.remove("ativo");
            dados.put("ativo", "false");
            dados = controlador.recebeOperacao(dados);
            String str = (String) dados.get("retorno");
            if (str.equals("Funcionario alterado!")) {
                JOptionPane.showMessageDialog(null, "Funcionario excluido com sucesso!");
                new TelaPrincipalGer(sessao).show();
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Impossivel excluir funcionario. Contacte o administrador.");
            }
        }
    }
}//GEN-LAST:event_jButtonLimparActionPerformed

private void jButtonCadastrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCadastrarActionPerformed
    String senha1 = jPasswordFieldSenha1.getText();
    String senha2 = jPasswordFieldSenha2.getText();
    try {
        new Sessao().setSenha(jPasswordFieldSenha1.getText());//criptografia
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Senha contem caracteres especiais",
                "Erro", JOptionPane.INFORMATION_MESSAGE);
        jTabbedPane2.setSelectedIndex(4);
        jPasswordFieldSenha1.requestFocus();
        return;
    }
    if (!jTextFieldNomeFuncionario.getText().equals("")) {
        this.validaCPF(jFormattedTextFieldCPFFuncionario.getText(), "funcionario");
        if (cpfValido) {
            if ((!senha1.equals("")) && (!senha2.equals(""))) {
                if (senha1.equals(senha2)) {
                    if ((jTextFieldAluguel.getText().equals("") || jTextFieldAluguel.getText() == null) && !jCheckBoxCasaPropria.isSelected()) {
                        JOptionPane.showMessageDialog(null, "O valor do aluguel esta incorreto!", "Erro", JOptionPane.INFORMATION_MESSAGE);
                        jTabbedPane2.setSelectedIndex(1);
                        jTextFieldAluguel.requestFocus();
                    } else {
                        if (jTextFieldAluguel.getText().toUpperCase().equals("ERRO") && !jCheckBoxCasaPropria.isSelected()) {
                            JOptionPane.showMessageDialog(null, "O valor do aluguel esta incorreto!", "Erro", JOptionPane.INFORMATION_MESSAGE);
                            jTabbedPane2.setSelectedIndex(1);
                            jTextFieldAluguel.requestFocus();
                        } else {
                            if (jTextFieldSalario.getText().toUpperCase().equals("ERRO")) {
                                JOptionPane.showMessageDialog(null, "O valor do salario esta incorreto!", "Erro", JOptionPane.INFORMATION_MESSAGE);
                                jTabbedPane2.setSelectedIndex(4);
                                jTextFieldSalario.requestFocus();
                            } else {
                                String str = this.cadastraFuncionario();
                                if (str.equals("sucesso")) {
                                    new TelaPrincipalGer(sessao).show();
                                    this.dispose();
                                }
                            }

                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor, corrija a senha!",
                            "A senhas nao conferem", JOptionPane.WARNING_MESSAGE);
                    jTabbedPane2.setSelectedIndex(4);
                    jPasswordFieldSenha1.grabFocus();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Por favor, digite uma senha!",
                        "Erro", JOptionPane.WARNING_MESSAGE);
                jTabbedPane2.setSelectedIndex(4);
                jPasswordFieldSenha1.grabFocus();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Por favor, digite um CPF valido!",
                    "Erro", JOptionPane.WARNING_MESSAGE);
            jTabbedPane2.setSelectedIndex(0);
            jFormattedTextFieldCPFFuncionario.grabFocus();
        }
    } else {
        JOptionPane.showMessageDialog(null, "Por favor, digite um nome!",
                "Erro", JOptionPane.WARNING_MESSAGE);
        jTabbedPane2.setSelectedIndex(0);
        jTextFieldNomeFuncionario.grabFocus();
    }
}//GEN-LAST:event_jButtonCadastrarActionPerformed

private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    dao.desconectar();
    new TelaPrincipalGer(sessao).show();
    this.dispose();
}//GEN-LAST:event_formWindowClosing

private void jButtonPrimeiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrimeiroActionPerformed
    HashMap<String, Object> dados = new HashMap<String, Object>();
    dados.put("modelo", "funcionario");
    dados.put("operacao", "busca");
    dados.put("posicao", "primeiro");
    dados.put("classificacao", "ordem");
    dados = controlador.recebeOperacao(dados);
    if (dados.get("ativo").equals("true")) {
        this.mostraDados(dados);
    } else {
        String codigo = (String) dados.get("cod");
        while (true) {
            dados.put("modelo", "funcionario");
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
    dados.put("modelo", "funcionario");
    dados.put("operacao", "busca");
    dados.put("posicao", "anterior");
    dados.put("classificacao", "ordem");
    dados.put("cod", jTextFieldCodigoFuncionario.getText());
    dados = controlador.recebeOperacao(dados);
    if (dados.get("ativo").equals("true")) {
        this.mostraDados(dados);
    } else {
        String codigo = (String) dados.get("cod");
        while (true) {
            dados.put("modelo", "funcionario");
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
    dados.put("modelo", "funcionario");
    dados.put("operacao", "busca");
    dados.put("posicao", "proximo");
    dados.put("classificacao", "ordem");
    dados.put("cod", jTextFieldCodigoFuncionario.getText());
    dados = controlador.recebeOperacao(dados);
    if (dados.get("ativo").equals("true")) {
        this.mostraDados(dados);
    } else {
        String codigo = (String) dados.get("cod");
        while (true) {
            dados.put("modelo", "funcionario");
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
    dados.put("modelo", "funcionario");
    dados.put("operacao", "busca");
    dados.put("posicao", "ultimo");
    dados.put("classificacao", "ordem");
    dados = controlador.recebeOperacao(dados);
    if (dados.get("ativo").equals("true")) {
        this.mostraDados(dados);
    } else {
        String codigo = (String) dados.get("cod");
        while (true) {
            dados.put("modelo", "funcionario");
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

private void jFormattedTextFieldDataAdmissaoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextFieldDataAdmissaoKeyReleased
    // TODO add your handling code here:
}//GEN-LAST:event_jFormattedTextFieldDataAdmissaoKeyReleased

private void jPasswordFieldSenha1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPasswordFieldSenha1FocusGained
    jPasswordFieldSenha1.setSelectionStart(0);
    jPasswordFieldSenha1.setSelectionEnd(jPasswordFieldSenha1.getText().length());
}//GEN-LAST:event_jPasswordFieldSenha1FocusGained

private void jPasswordFieldSenha2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPasswordFieldSenha2FocusGained
    jPasswordFieldSenha2.setSelectionStart(0);
    jPasswordFieldSenha2.setSelectionEnd(jPasswordFieldSenha2.getText().length());
}//GEN-LAST:event_jPasswordFieldSenha2FocusGained

private void jTextFieldSalarioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldSalarioFocusLost
    jTextFieldSalario.setText(this.trataPreco(jTextFieldSalario.getText()).toUpperCase());
}//GEN-LAST:event_jTextFieldSalarioFocusLost

private void jTextFieldSalarioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldSalarioFocusGained
    jTextFieldSalario.setSelectionStart(0);
    jTextFieldSalario.setSelectionEnd(jTextFieldSalario.getText().length());
}//GEN-LAST:event_jTextFieldSalarioFocusGained

private void jTextFieldRGFuncionarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldRGFuncionarioActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_jTextFieldRGFuncionarioActionPerformed

private void jFormattedTextFieldCPFFuncionarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextFieldCPFFuncionarioKeyReleased
    this.validaCPF(jFormattedTextFieldCPFFuncionario.getText(), "funcionario");
}//GEN-LAST:event_jFormattedTextFieldCPFFuncionarioKeyReleased

private void jFormattedTextFieldNascFuncionarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextFieldNascFuncionarioKeyReleased
    String x = jFormattedTextFieldNascFuncionario.getText();
    this.validaNasc(x, "funcionario");
}//GEN-LAST:event_jFormattedTextFieldNascFuncionarioKeyReleased

private void jFormattedTextFieldCelFuncionarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jFormattedTextFieldCelFuncionarioActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_jFormattedTextFieldCelFuncionarioActionPerformed

private void jTextFieldEmailFuncionarioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldEmailFuncionarioFocusLost
    jTabbedPane2.setSelectedIndex(1);
    jTextFieldNomeLogradouro.requestFocus();
}//GEN-LAST:event_jTextFieldEmailFuncionarioFocusLost

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

private void jTextFieldTempoResidenciaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldTempoResidenciaFocusLost
    jTabbedPane2.setSelectedIndex(2);
    jTextFieldNomeConjuge.requestFocus();
}//GEN-LAST:event_jTextFieldTempoResidenciaFocusLost

private void jTextFieldRGConjugeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldRGConjugeActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_jTextFieldRGConjugeActionPerformed

private void jFormattedTextFieldCPFConjugeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextFieldCPFConjugeKeyReleased
    this.validaCPF(jFormattedTextFieldCPFFuncionario.getText(), "conjuge");
}//GEN-LAST:event_jFormattedTextFieldCPFConjugeKeyReleased

private void jFormattedTextFieldNascConjugeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextFieldNascConjugeKeyReleased
    String x = jFormattedTextFieldNascFuncionario.getText();
    this.validaNasc(x, "conjuge");
}//GEN-LAST:event_jFormattedTextFieldNascConjugeKeyReleased

private void jFormattedTextFieldCelConjugeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextFieldCelConjugeFocusLost
    jTabbedPane2.setSelectedIndex(3);
    jTextFieldNomeRefCom1.requestFocus();
}//GEN-LAST:event_jFormattedTextFieldCelConjugeFocusLost

private void jFormattedTextFieldFoneRefCom3FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jFormattedTextFieldFoneRefCom3FocusLost
    jTabbedPane2.setSelectedIndex(4);
    jTextFieldFuncao.requestFocus();
}//GEN-LAST:event_jFormattedTextFieldFoneRefCom3FocusLost

private void jPasswordFieldSenha2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPasswordFieldSenha2FocusLost
    if (!cadastrar) {
        jTabbedPane2.setSelectedIndex(5);
        jTable1.requestFocus();
    } else {
        jTabbedPane2.setSelectedIndex(0);
        jButtonCadastrar.requestFocus();
    }

}//GEN-LAST:event_jPasswordFieldSenha2FocusLost

private void jButtonDataAtualActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDataAtualActionPerformed
    String str = DataSistema.getInstance().diaMesAno();
    jFormattedTextFieldDataAdmissao.setValue(str.split(" ")[1]);
}//GEN-LAST:event_jButtonDataAtualActionPerformed

private void jTextFieldNomeFuncionarioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldNomeFuncionarioFocusLost
    String nome = jTextFieldNomeFuncionario.getText();
    String login = "";
    int tamanho = nome.split(" ").length;
    for (int i = 0; i < tamanho - 1; i++) {
        String aux = nome.split(" ")[i];
        if (aux.length() > 2) {
            login += aux.charAt(0);
        }
    }
    login += nome.split(" ")[tamanho - 1];
    jTextFieldLogin.setText(login.toLowerCase());
}//GEN-LAST:event_jTextFieldNomeFuncionarioFocusLost

private void jPasswordFieldSenha2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordFieldSenha2ActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_jPasswordFieldSenha2ActionPerformed

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
            RecebimentoGer recebimento = new RecebimentoGer("funcionario", (DefaultTableModel) jTable1.getModel(), jTable1.getSelectedRow(), sessao);
            recebimento.show();
            this.dispose();
        }
    }
}//GEN-LAST:event_jTable1MouseClicked
    private void limpa() {
        // ------------- DADOS PESSOAIS -----------------
        jTextFieldCodigoFuncionario.setText("" + this.getMaiorId());
        jTextFieldNomeFuncionario.setText("");
        jFormattedTextFieldNascFuncionario.setValue(null);
        jTextFieldRGFuncionario.setText("");
        jFormattedTextFieldCPFFuncionario.setValue(null);
        jComboBoxSexoFuncionario.setSelectedIndex(0);
        jFormattedTextFieldFoneFuncionario.setValue(null);
        jFormattedTextFieldCelFuncionario.setValue(null);
        jTextFieldEstadoCivilFuncionario.setText("");
        jTextFieldEmailFuncionario.setText("");

        // ------------- ENDERECO FUNCIONARIO -----------------
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

        // ------------- CONJUGE -----------------
        jTextFieldNomeConjuge.setText("");
        jFormattedTextFieldNascConjuge.setValue(null);
        jTextFieldRGConjuge.setText("");
        jFormattedTextFieldCPFConjuge.setValue(null);
        jComboBoxSexoConjuge.setSelectedIndex(0);
        jFormattedTextFieldCelConjuge.setValue(null);

        // ------------- TRABALHO -----------------
        jTextFieldFuncao.setText("");
        jTextFieldSalario.setText("");
        jFormattedTextFieldDataAdmissao.setValue(null);

        // ------------- REFERENCIAS COMERCIAIS -----------------
        jTextFieldNomeRefCom1.setText("");
        jFormattedTextFieldFoneRefCom1.setValue(null);
        jTextFieldNomeRefCom2.setText("");
        jFormattedTextFieldFoneRefCom2.setValue(null);
        jTextFieldNomeRefCom3.setText("");
        jFormattedTextFieldFoneRefCom3.setValue(null);

        // ------------- LOGIN E SENHA -----------------
        jTextFieldLogin.setText("");
        jPasswordFieldSenha1.setText("");
        jPasswordFieldSenha2.setText("");
    }

    public void modificaConsultar() {
        jButtonCadastrar.setText("Alterar");
        jButtonLimpar.setText("Excluir");
        this.cadastrar = false;
        this.jButtonAnterior.setEnabled(true);
        this.jButtonPrimeiro.setEnabled(true);
        this.jButtonProximo.setEnabled(true);
        this.jButtonUltimo.setEnabled(true);
    }

    private void carregaCidades(String estado, int i) {
        /*
         * i = 1 = combobox UF Funcionario
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

    public void validaNasc(String nascimento, String pessoa) {
        if (nascimento.length() > 0) {
            String[] y = nascimento.split("/");
            int i = Integer.parseInt(y[2]);
            DataSistema d = DataSistema.getInstance();
            if ((i > (Integer.parseInt((d.diaMesAno().split(" ")[1]).split("/")[2]) - 16)) || (i == 0) || (i < 1900)) {
                if (pessoa.equals("funcionario")) {
                    jFormattedTextFieldNascFuncionario.setBackground(Color.RED);
                } else {
                    jFormattedTextFieldNascConjuge.setBackground(Color.RED);
                }
            } else {
                if (pessoa.equals("funcionario")) {
                    jFormattedTextFieldNascFuncionario.setBackground(Color.CYAN);
                } else {
                    jFormattedTextFieldNascConjuge.setBackground(Color.CYAN);
                }
            }
        }
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
                if (pessoa.equals("funcionario")) {
                    jFormattedTextFieldCPFFuncionario.setBackground(Color.CYAN);
                    cpfValido = true;
                } else {
                    jFormattedTextFieldCPFConjuge.setBackground(Color.CYAN);
                }

            } else {
                if (pessoa.equals("funcionario")) {
                    jFormattedTextFieldCPFFuncionario.setBackground(Color.RED);
                } else {
                    jFormattedTextFieldCPFConjuge.setBackground(Color.RED);
                }
            }
        }
    }

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
        jFormattedTextFieldCPFFuncionario.setFormatterFactory(null);
        jFormattedTextFieldCPFConjuge.setFormatterFactory(null);
        try {
            format = new MaskFormatter("###.###.###-##");
            format.setPlaceholderCharacter('0');
        } catch (Exception ex) {
        }
        jFormattedTextFieldCPFFuncionario.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldCPFFuncionario.setValue(null);
        jFormattedTextFieldCPFConjuge.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldCPFConjuge.setValue(null);
    }

    public void mascaraData() {
        MaskFormatter format = null;
        jFormattedTextFieldDataCadastro.setFormatterFactory(null);
        jFormattedTextFieldNascFuncionario.setFormatterFactory(null);
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
        jFormattedTextFieldNascFuncionario.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldNascFuncionario.setValue(null);
        jFormattedTextFieldNascConjuge.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldNascConjuge.setValue(null);
        jFormattedTextFieldDataAdmissao.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldDataAdmissao.setValue(null);
    }

    public void mascaraTEL() {
        MaskFormatter format = null;
        jFormattedTextFieldFoneFuncionario.setFormatterFactory(null);
        jFormattedTextFieldCelFuncionario.setFormatterFactory(null);
        jFormattedTextFieldCelConjuge.setFormatterFactory(null);
        jFormattedTextFieldFoneRefCom1.setFormatterFactory(null);
        jFormattedTextFieldFoneRefCom2.setFormatterFactory(null);
        jFormattedTextFieldFoneRefCom3.setFormatterFactory(null);
        try {
            format = new MaskFormatter("(##)####-####");
            format.setPlaceholderCharacter('_');
        } catch (Exception ex) {
            System.out.println("Erro carregar mascara telefone");
        }
        jFormattedTextFieldFoneFuncionario.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldFoneFuncionario.setValue(null);
        jFormattedTextFieldCelFuncionario.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldCelFuncionario.setValue(null);
        jFormattedTextFieldCelConjuge.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldCelConjuge.setValue(null);
        jFormattedTextFieldFoneRefCom1.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldFoneRefCom1.setValue(null);
        jFormattedTextFieldFoneRefCom2.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldFoneRefCom2.setValue(null);
        jFormattedTextFieldFoneRefCom3.setFormatterFactory(new DefaultFormatterFactory(format));
        jFormattedTextFieldFoneRefCom3.setValue(null);
    }

    private String pegaTabelaVendas() {
        HashMap<String, Object> dados = new HashMap<String, Object>();
        dados.put("modelo", "funcionario");
        dados.put("operacao", "busca");
        dados.put("cod", jTextFieldCodigoFuncionario.getText());
        dados.put("posicao", "cod");
        dados = controlador.recebeOperacao(dados);
        return (String) dados.get("vendas");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new CadastroFuncionarioGer().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButtonAnterior;
    private javax.swing.JButton jButtonCadastrar;
    private javax.swing.JButton jButtonDataAtual;
    private javax.swing.JButton jButtonLimpar;
    private javax.swing.JButton jButtonPrimeiro;
    private javax.swing.JButton jButtonProximo;
    private javax.swing.JButton jButtonUltimo;
    private javax.swing.JButton jButtonVoltar;
    private javax.swing.JCheckBox jCheckBoxCasaPropria;
    private javax.swing.JCheckBox jCheckGerente;
    private javax.swing.JComboBox jComboBoxCidade;
    private javax.swing.JComboBox jComboBoxLogradouro;
    private javax.swing.JComboBox jComboBoxSexoConjuge;
    private javax.swing.JComboBox jComboBoxSexoFuncionario;
    private javax.swing.JComboBox jComboBoxUF;
    private javax.swing.JFormattedTextField jFormattedTextFieldCEP;
    private javax.swing.JFormattedTextField jFormattedTextFieldCPFConjuge;
    private javax.swing.JFormattedTextField jFormattedTextFieldCPFFuncionario;
    private javax.swing.JFormattedTextField jFormattedTextFieldCelConjuge;
    private javax.swing.JFormattedTextField jFormattedTextFieldCelFuncionario;
    private javax.swing.JFormattedTextField jFormattedTextFieldDataAdmissao;
    private javax.swing.JFormattedTextField jFormattedTextFieldDataCadastro;
    private javax.swing.JFormattedTextField jFormattedTextFieldFoneFuncionario;
    private javax.swing.JFormattedTextField jFormattedTextFieldFoneRefCom1;
    private javax.swing.JFormattedTextField jFormattedTextFieldFoneRefCom2;
    private javax.swing.JFormattedTextField jFormattedTextFieldFoneRefCom3;
    private javax.swing.JFormattedTextField jFormattedTextFieldNascConjuge;
    private javax.swing.JFormattedTextField jFormattedTextFieldNascFuncionario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelBairro1;
    private javax.swing.JLabel jLabelCEP1;
    private javax.swing.JLabel jLabelCPF2;
    private javax.swing.JLabel jLabelCPF3;
    private javax.swing.JLabel jLabelCelConjuge1;
    private javax.swing.JLabel jLabelCelular1;
    private javax.swing.JLabel jLabelCidade1;
    private javax.swing.JLabel jLabelCodigoCliente;
    private javax.swing.JLabel jLabelComplemento1;
    private javax.swing.JLabel jLabelData;
    private javax.swing.JLabel jLabelEmail1;
    private javax.swing.JLabel jLabelEstadoCivil1;
    private javax.swing.JLabel jLabelFoneRefCom1;
    private javax.swing.JLabel jLabelFoneRefCom4;
    private javax.swing.JLabel jLabelFoneRefCom5;
    private javax.swing.JLabel jLabelHora;
    private javax.swing.JLabel jLabelLogin;
    private javax.swing.JLabel jLabelNasc1;
    private javax.swing.JLabel jLabelNasc3;
    private javax.swing.JLabel jLabelNasc4;
    private javax.swing.JLabel jLabelNascConjuge1;
    private javax.swing.JLabel jLabelNomeCli2;
    private javax.swing.JLabel jLabelNomeCli3;
    private javax.swing.JLabel jLabelNomeConjuge1;
    private javax.swing.JLabel jLabelNomeRefCom1;
    private javax.swing.JLabel jLabelNomeRefCom4;
    private javax.swing.JLabel jLabelNomeRefCom5;
    private javax.swing.JLabel jLabelNumTrabalho2;
    private javax.swing.JLabel jLabelRG2;
    private javax.swing.JLabel jLabelRG3;
    private javax.swing.JLabel jLabelRGConjuge1;
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
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPasswordField jPasswordFieldSenha1;
    private javax.swing.JPasswordField jPasswordFieldSenha2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextFieldAluguel;
    private javax.swing.JTextField jTextFieldBairro;
    private javax.swing.JTextField jTextFieldCodigoFuncionario;
    private javax.swing.JTextField jTextFieldComplemento;
    private javax.swing.JTextField jTextFieldEmailFuncionario;
    private javax.swing.JTextField jTextFieldEstadoCivilFuncionario;
    private javax.swing.JTextField jTextFieldFuncao;
    private javax.swing.JTextField jTextFieldLogin;
    private javax.swing.JTextField jTextFieldNomeConjuge;
    private javax.swing.JTextField jTextFieldNomeFuncionario;
    private javax.swing.JTextField jTextFieldNomeLogradouro;
    private javax.swing.JTextField jTextFieldNomeRefCom1;
    private javax.swing.JTextField jTextFieldNomeRefCom2;
    private javax.swing.JTextField jTextFieldNomeRefCom3;
    private javax.swing.JTextField jTextFieldNum;
    private javax.swing.JTextField jTextFieldRGConjuge;
    private javax.swing.JTextField jTextFieldRGFuncionario;
    private javax.swing.JTextField jTextFieldSalario;
    private javax.swing.JTextField jTextFieldTempoResidencia;
    private org.netbeans.examples.lib.timerbean.Timer timer1;
    // End of variables declaration//GEN-END:variables
}
