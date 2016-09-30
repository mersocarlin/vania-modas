package visaoGerente;

import controlador.Controlador;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import persistencia.DAO;
import utilitarios.DataSistema;
import utilitarios.Extenso;
import utilitarios.Impressao;
import utilitarios.Sessao;

/**
 *
 * @author Hemerson e Jefferson
 */
public class TelaVendaGer extends javax.swing.JFrame {

    //-----------PARAMETROS DA TELA DE CONSULTA-------------//
    String tipoConsulta;
    List l;
    Controlador controlador = new Controlador();
    DefaultTableModel modelo = new DefaultTableModel();
    DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
    //---------------------------------------------//
    DataSistema data = new DataSistema();
    DAO dao;
    private Sessao sessao;
    String tipoVenda = "";
    boolean recebimento = true;

    /** Creates new form Venda */
    public TelaVendaGer() {
        initComponents();
        sessao = null;
        timer1.start();
    }

    public TelaVendaGer(Sessao sessao, String tipo) {
        initComponents();
        tipoVenda = tipo;
        this.construtor(sessao);


    }

    private void alinhaColunas() {
        cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        jTableItensVenda.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
        jTableItensVenda.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
        jTableItensVenda.getColumnModel().getColumn(3).setCellRenderer(cellRenderer);
        jTableItensVenda.getColumnModel().getColumn(4).setCellRenderer(cellRenderer);
        jTableItensVenda.getColumnModel().getColumn(5).setCellRenderer(cellRenderer);
        jTableParcelas.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
        jTableParcelas.getColumnModel().getColumn(1).setCellRenderer(cellRenderer);
        cellRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
        jTableConsulta.getColumnModel().getColumn(2).setCellRenderer(cellRenderer);
    }

    private void calculaValorFinal() {
        double valorTotal = 0.0;
        try {
            double desc = Double.parseDouble(jTextFieldDesc.getText());
            if (desc == 1) {
                return;
            }
            desc = (desc / 100) * Double.parseDouble(jTextFieldSubTotal.getText().split(" ")[1]);
            valorTotal = Double.parseDouble(jTextFieldSubTotal.getText().split(" ")[1]) - desc;

        } catch (Exception e) {
            valorTotal = Double.parseDouble(jTextFieldSubTotal.getText().split(" ")[1]);
            jTextFieldDesc.setText("0");
        }
        valorTotal += 0.5;
        int vt = (int) valorTotal;
        valorTotal = vt;
        String total = "" + valorTotal;
        if (total.substring(total.lastIndexOf(".") + 1).length() < 2) {
            total += "0";
        }
        total = "R$ " + total;
        jTextFieldTotal.setText(total);
    }

    private String finalizaVenda() {
        HashMap<String, Object> dadosVenda = new HashMap(this.pegaDados(true));
        HashMap<String, Object> dados = new HashMap<String, Object>();
        dadosVenda.put("operacao", "inserir");
        dadosVenda.put("modelo", "venda");
        dados = controlador.recebeOperacao(dadosVenda);
        JOptionPane.showMessageDialog(null, (String) dados.get("retorno"));
        if (((String) dados.get("retorno")).equals("Venda inserida com sucesso.")) {
            return "sucesso";
        } else {
            return "Erro";
        }

    }

    private HashMap pegaDados(boolean atualizarEstoque) {
        HashMap<String, Object> dadosVenda = new HashMap<String, Object>();
        dadosVenda.put("cod", jTextFieldNoCupom.getText());
        dadosVenda.put("codCliente", jTextFieldCodigoCliente.getText());
        dadosVenda.put("codFuncionario", jTextFieldCodigoFuncionario.getText());

        if (jComboBoxPagamento.getSelectedItem().equals("A VISTA")) {
            dadosVenda.put("avista", "true");
        } else {
            dadosVenda.put("avista", "false");
        }

        String aux = jTextFieldDataVenda.getText();
        if (aux == null) {
            aux = "00/00/0000";
        }
        dadosVenda.put("dataVenda", aux);

        List lista = new ArrayList();
        modelo = (DefaultTableModel) jTableItensVenda.getModel();
        //exemplo: item#codigo#descricao#qtde#valorUnitario
        for (int i = 0; i < modelo.getRowCount(); i++) {
            String str = "";
            str += (String) modelo.getValueAt(i, 1) + "#";
            str += (String) modelo.getValueAt(i, 2) + "#";
            str += (String) modelo.getValueAt(i, 3) + "#";
            str += (String) modelo.getValueAt(i, 4);
            lista.add(str);

            //DAR BAIXA NO ESTOQUE
            if (atualizarEstoque) {
                this.atualizaEstoque(Integer.parseInt((String) modelo.getValueAt(i, 1)), Integer.parseInt((String) modelo.getValueAt(i, 3)));
            }

        }
        dadosVenda.put("itensVenda", lista);
        dadosVenda.put("subTotal", jTextFieldSubTotal.getText().split(" ")[1]);
        dadosVenda.put("desconto", jTextFieldDesc.getText().split(" ")[0]);
        dadosVenda.put("valorTotal", jTextFieldTotal.getText().split(" ")[1]);
        if (jTextFieldValorEntrada.getText().equals("")) {
            dadosVenda.put("valorEntrada", "0.0");
            dadosVenda.put("valorRestante", "0.0");
        } else {
            dadosVenda.put("valorEntrada", jTextFieldValorEntrada.getText());
            double vTotal = Double.parseDouble((String) dadosVenda.get("valorTotal"));
            double conta = Double.parseDouble((String) dadosVenda.get("valorEntrada"));
            conta = vTotal - conta;
            if (conta <= 0.0) {
                conta = 0.0;
            }
            dadosVenda.put("valorRestante", conta + "");
        }
        lista = new ArrayList();
        modelo = (DefaultTableModel) jTableParcelas.getModel();
        //exemplo: dataVencimento#valor#datapagamento#situacao#funcionarioRecebeu
        for (int i = 0; i < modelo.getRowCount(); i++) {
            String str = "";
            str += ((String) modelo.getValueAt(i, 0)).split("/")[0] + "/" + ((String) modelo.getValueAt(i, 0)).split("/")[1] + "/" + ((String) modelo.getValueAt(i, 0)).split("/")[2] + "#";
            str += (String) modelo.getValueAt(i, 1) + "#";//valor
            str += "00/00/0000" + "#";//data de pagamento
            str += "false" + "#";//situacao
            str += " " + "#";//funcionarioRecebeu
            lista.add(str);
        }
        dadosVenda.put("parcelas", lista);
        return dadosVenda;
    }

    private void geraParcelas() {
        double auxTotal = Double.parseDouble(jTextFieldTotal.getText().split(" ")[1]);
        if (jComboBoxVezes.getSelectedIndex() != 0 && auxTotal != 0.0) {
            int vezes = Integer.parseInt((String) jComboBoxVezes.getSelectedItem());
            int dia, mes, ano;
            dia = Integer.parseInt(data.diaMesAno().split(" ")[1].split("/")[0]);
            mes = Integer.parseInt(data.diaMesAno().split(" ")[1].split("/")[1]);
            ano = Integer.parseInt(data.diaMesAno().split(" ")[1].split("/")[2]);
            modelo = (DefaultTableModel) jTableParcelas.getModel();
            while (modelo.getRowCount() > 0) {
                modelo.removeRow(0);
            }
            double entrada;
            try {
                entrada = Double.parseDouble(jTextFieldValorEntrada.getText());
            } catch (Exception e) {
                jTextFieldValorEntrada.setText("0.00");
                entrada = 0.0;
            }

            if (jCheckBoxEntrada.isSelected()) {
                try {
                    auxTotal -= entrada;
                } catch (Exception e) {
                    auxTotal = Double.parseDouble(jTextFieldTotal.getText().split(" ")[1]);
                }
            }
            if (auxTotal < vezes) {
                int ant = vezes;
                vezes = (int) auxTotal;
                jComboBoxVezes.setSelectedIndex(vezes);
                JOptionPane.showMessageDialog(null, "Impossivel calcular parcelas. Parcela minima R$ 1.00.\n" +
                        "Alterando quantidade de parcelas de " + ant + " para " + vezes + " vezes.");
            }
            for (int i = 0; i < vezes; i++) {
                int parcela = 0;
                if (mes == 12) {
                    mes = 1;
                    ano++;
                } else {
                    mes++;
                }
                if ((dia > 28) && (mes == 2)) {
                    dia = 28;
                } else {
                    if ((dia == 31) && (mes != 2)) {
                        dia = 30;
                    }
                }
                String date;
                if (dia < 10) {
                    date = "0" + dia;
                } else {
                    date = "" + dia;
                }
                if (mes < 10) {
                    date += "/0" + mes;
                } else {
                    date += "/" + mes;
                }
                date += "/" + ano;
                //conta
                double d = auxTotal / (vezes - i);
                parcela = (int) d;
                String valorParcela = parcela + ".00";
                auxTotal -= parcela;
                Object linha[] = {date, valorParcela};
                modelo.addRow(linha);
            }
            jTableParcelas.setModel(modelo);
        }
    }

    private void habilitaPrazo(boolean b) {
        jCheckBoxEntrada.setEnabled(b);
        jTextFieldValorEntrada.setEnabled(false);
        jComboBoxVezes.setEnabled(b);
        jLabelVezes.setEnabled(b);
        jTableParcelas.setEnabled(b);
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
        jPanel7 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabelNameConsulta = new javax.swing.JLabel();
        jTextFieldNome = new javax.swing.JTextField();
        jFormattedTextFieldCod = new javax.swing.JFormattedTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTableConsulta = new javax.swing.JTable();
        timer1 = new org.netbeans.examples.lib.timerbean.Timer();
        jDialogRecebimento = new javax.swing.JDialog();
        jPanel9 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jTextFieldValorCaixa = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jTextFieldDinheiroCaixa = new javax.swing.JTextField();
        jTextFieldTrocoCaixa = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jButtonReceberCaixa = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldCodigoFuncionario = new javax.swing.JTextField();
        jTextFieldCodigoCliente = new javax.swing.JTextField();
        jComboBoxFuncionario = new javax.swing.JComboBox();
        jComboBoxCliente = new javax.swing.JComboBox();
        jButtonConsultaFuncionario = new javax.swing.JButton();
        jButtonConsultaCliente = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldDataVenda = new javax.swing.JTextField();
        jTextFieldHoraVenda = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jComboBoxPagamento = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTextFieldCodigoProduto = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextFieldQtdePro = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jTextFieldValorUni = new javax.swing.JTextField();
        jComboBoxProduto = new javax.swing.JComboBox();
        jButtonConsultaProduto = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableItensVenda = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jCheckBoxEntrada = new javax.swing.JCheckBox();
        jComboBoxVezes = new javax.swing.JComboBox();
        jLabelVezes = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableParcelas = new javax.swing.JTable();
        jTextFieldValorEntrada = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextFieldDesc = new javax.swing.JTextField();
        jTextFieldSubTotal = new javax.swing.JTextField();
        jTextFieldTotal = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jButtonFinalizarVenda = new javax.swing.JButton();
        jButtonCancelar = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        jTextFieldNoCupom = new javax.swing.JTextField();
        jButtonLimpar = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabelData = new javax.swing.JLabel();
        jLabelHora = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabelLogin = new javax.swing.JLabel();

        jDialogConsulta.setResizable(false);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel14.setText("Codigo: ");

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
        jFormattedTextFieldCod.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jFormattedTextFieldCodKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jFormattedTextFieldCod, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(jLabelNameConsulta, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldNome, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(jFormattedTextFieldCod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        jScrollPane3.setViewportView(jTableConsulta);

        javax.swing.GroupLayout jDialogConsultaLayout = new javax.swing.GroupLayout(jDialogConsulta.getContentPane());
        jDialogConsulta.getContentPane().setLayout(jDialogConsultaLayout);
        jDialogConsultaLayout.setHorizontalGroup(
            jDialogConsultaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 650, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialogConsultaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialogConsultaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 626, Short.MAX_VALUE))
                .addContainerGap())
        );
        jDialogConsultaLayout.setVerticalGroup(
            jDialogConsultaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogConsultaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        timer1.addTimerListener(new org.netbeans.examples.lib.timerbean.TimerListener() {
            public void onTime(java.awt.event.ActionEvent evt) {
                timer1OnTime(evt);
            }
        });

        jDialogRecebimento.setModal(true);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Caixa"));

        jLabel17.setFont(new java.awt.Font("Verdana", 1, 18));
        jLabel17.setText("Valor: ");

        jTextFieldValorCaixa.setEditable(false);
        jTextFieldValorCaixa.setFont(new java.awt.Font("Arial", 1, 18));
        jTextFieldValorCaixa.setForeground(new java.awt.Color(0, 0, 204));
        jTextFieldValorCaixa.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextFieldValorCaixa.setFocusable(false);

        jLabel18.setFont(new java.awt.Font("Verdana", 1, 18));
        jLabel18.setText("V. Recebido: ");

        jTextFieldDinheiroCaixa.setFont(new java.awt.Font("Arial", 1, 18));
        jTextFieldDinheiroCaixa.setForeground(new java.awt.Color(0, 0, 204));
        jTextFieldDinheiroCaixa.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextFieldDinheiroCaixa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldDinheiroCaixaActionPerformed(evt);
            }
        });
        jTextFieldDinheiroCaixa.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldDinheiroCaixaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldDinheiroCaixaFocusLost(evt);
            }
        });

        jTextFieldTrocoCaixa.setEditable(false);
        jTextFieldTrocoCaixa.setFont(new java.awt.Font("Arial", 1, 18));
        jTextFieldTrocoCaixa.setForeground(new java.awt.Color(0, 0, 204));
        jTextFieldTrocoCaixa.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextFieldTrocoCaixa.setFocusable(false);

        jLabel19.setFont(new java.awt.Font("Verdana", 1, 18));
        jLabel19.setText("Troco:");

        jButtonReceberCaixa.setText("Receber");
        jButtonReceberCaixa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonReceberCaixaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldTrocoCaixa, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldValorCaixa, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldDinheiroCaixa, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap(217, Short.MAX_VALUE)
                .addComponent(jButtonReceberCaixa)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jTextFieldValorCaixa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldDinheiroCaixa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18))
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jTextFieldTrocoCaixa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jButtonReceberCaixa)
                .addContainerGap())
        );

        javax.swing.GroupLayout jDialogRecebimentoLayout = new javax.swing.GroupLayout(jDialogRecebimento.getContentPane());
        jDialogRecebimento.getContentPane().setLayout(jDialogRecebimentoLayout);
        jDialogRecebimentoLayout.setHorizontalGroup(
            jDialogRecebimentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogRecebimentoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jDialogRecebimentoLayout.setVerticalGroup(
            jDialogRecebimentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialogRecebimentoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel1.setText("Funcionario: ");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel2.setText("Cliente:");

        jTextFieldCodigoFuncionario.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTextFieldCodigoFuncionario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCodigoFuncionarioActionPerformed(evt);
            }
        });
        jTextFieldCodigoFuncionario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldCodigoFuncionarioFocusGained(evt);
            }
        });

        jTextFieldCodigoCliente.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTextFieldCodigoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCodigoClienteActionPerformed(evt);
            }
        });
        jTextFieldCodigoCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldCodigoClienteFocusGained(evt);
            }
        });

        jComboBoxFuncionario.setEditable(true);
        jComboBoxFuncionario.setFont(new java.awt.Font("Tahoma", 0, 14));
        jComboBoxFuncionario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxFuncionarioActionPerformed(evt);
            }
        });

        jComboBoxCliente.setEditable(true);
        jComboBoxCliente.setFont(new java.awt.Font("Tahoma", 0, 14));
        jComboBoxCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxClienteActionPerformed(evt);
            }
        });

        jButtonConsultaFuncionario.setText(">");
        jButtonConsultaFuncionario.setToolTipText("Procurar funcionario");
        jButtonConsultaFuncionario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConsultaFuncionarioActionPerformed(evt);
            }
        });

        jButtonConsultaCliente.setText(">");
        jButtonConsultaCliente.setToolTipText("Procurar cliente");
        jButtonConsultaCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConsultaClienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextFieldCodigoFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextFieldCodigoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBoxFuncionario, 0, 613, Short.MAX_VALUE)
                            .addComponent(jComboBoxCliente, 0, 613, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButtonConsultaFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonConsultaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldCodigoFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxFuncionario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonConsultaFuncionario))
                .addGap(13, 13, 13)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldCodigoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonConsultaCliente))
                .addGap(13, 13, 13))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel3.setText("Data:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel4.setText("Hora: ");

        jTextFieldDataVenda.setEditable(false);
        jTextFieldDataVenda.setFont(new java.awt.Font("Tahoma", 0, 14));
        jTextFieldDataVenda.setForeground(new java.awt.Color(255, 0, 0));
        jTextFieldDataVenda.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldDataVenda.setFocusable(false);

        jTextFieldHoraVenda.setEditable(false);
        jTextFieldHoraVenda.setFont(new java.awt.Font("Tahoma", 0, 14));
        jTextFieldHoraVenda.setForeground(new java.awt.Color(255, 0, 0));
        jTextFieldHoraVenda.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextFieldHoraVenda.setFocusable(false);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel5.setText("Pagto: ");

        jComboBoxPagamento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "A VISTA", "A PRAZO" }));
        jComboBoxPagamento.setToolTipText("Tipo de venda");
        jComboBoxPagamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxPagamentoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextFieldHoraVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldDataVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBoxPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(7, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextFieldDataVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextFieldHoraVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBoxPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel6.setText("Produto: ");

        jTextFieldCodigoProduto.setFont(new java.awt.Font("Tahoma", 0, 14));
        jTextFieldCodigoProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldCodigoProdutoActionPerformed(evt);
            }
        });
        jTextFieldCodigoProduto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldCodigoProdutoFocusGained(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel7.setText("Qtde: ");

        jTextFieldQtdePro.setFont(new java.awt.Font("Tahoma", 0, 14));
        jTextFieldQtdePro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldQtdeProActionPerformed(evt);
            }
        });
        jTextFieldQtdePro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldQtdeProFocusGained(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel8.setText("Valor Unit: ");

        jTextFieldValorUni.setEditable(false);
        jTextFieldValorUni.setFont(new java.awt.Font("Tahoma", 0, 14));
        jTextFieldValorUni.setForeground(new java.awt.Color(0, 0, 204));
        jTextFieldValorUni.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldValorUniActionPerformed(evt);
            }
        });
        jTextFieldValorUni.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldValorUniFocusLost(evt);
            }
        });

        jComboBoxProduto.setEditable(true);
        jComboBoxProduto.setFont(new java.awt.Font("Tahoma", 0, 14));
        jComboBoxProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxProdutoActionPerformed(evt);
            }
        });

        jButtonConsultaProduto.setText(">");
        jButtonConsultaProduto.setToolTipText("Procurar produto");
        jButtonConsultaProduto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonConsultaProdutoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldCodigoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jComboBoxProduto, 0, 472, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonConsultaProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldQtdePro, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldValorUni, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTextFieldCodigoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldValorUni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7)
                    .addComponent(jTextFieldQtdePro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonConsultaProduto)
                    .addComponent(jComboBoxProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jTableItensVenda.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Item", "Codigo", "Descricao dos Produtos", "Qtde", "Unitario", "Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableItensVenda.setToolTipText("Relacao de produtos. Para excluir um item selecione e pressione DELETE");
        jTableItensVenda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTableItensVendaKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTableItensVenda);

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel9.setText("Parcelas: ");

        jCheckBoxEntrada.setText("Entrada");
        jCheckBoxEntrada.setEnabled(false);
        jCheckBoxEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxEntradaActionPerformed(evt);
            }
        });

        jComboBoxVezes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15" }));
        jComboBoxVezes.setToolTipText("Quantidade de parcelas");
        jComboBoxVezes.setEnabled(false);
        jComboBoxVezes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jComboBoxVezesMouseReleased(evt);
            }
        });
        jComboBoxVezes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBoxVezesActionPerformed(evt);
            }
        });

        jLabelVezes.setText("Vez(es)");
        jLabelVezes.setEnabled(false);

        jTableParcelas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Data", "Valor"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableParcelas.setToolTipText("Parcelas");
        jTableParcelas.setEnabled(false);
        jScrollPane2.setViewportView(jTableParcelas);

        jTextFieldValorEntrada.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jTextFieldValorEntrada.setForeground(new java.awt.Color(0, 0, 204));
        jTextFieldValorEntrada.setToolTipText("Valor de entrada");
        jTextFieldValorEntrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldValorEntradaActionPerformed(evt);
            }
        });
        jTextFieldValorEntrada.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldValorEntradaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldValorEntradaFocusLost(evt);
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
                        .addComponent(jCheckBoxEntrada)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextFieldValorEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBoxVezes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabelVezes)))
                .addGap(37, 37, 37)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(jComboBoxVezes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelVezes))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jCheckBoxEntrada)
                            .addComponent(jTextFieldValorEntrada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel10.setFont(new java.awt.Font("Verdana", 1, 18));
        jLabel10.setText("TOTAL: ");

        jLabel11.setFont(new java.awt.Font("Verdana", 1, 18));
        jLabel11.setText("SUB-TOTAL: ");

        jLabel12.setFont(new java.awt.Font("Verdana", 1, 18));
        jLabel12.setText("Desc %: ");

        jTextFieldDesc.setFont(new java.awt.Font("Tahoma", 1, 14));
        jTextFieldDesc.setText("0");
        jTextFieldDesc.setToolTipText("Desconto em porcentagem");
        jTextFieldDesc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldDescActionPerformed(evt);
            }
        });
        jTextFieldDesc.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextFieldDescFocusLost(evt);
            }
        });

        jTextFieldSubTotal.setEditable(false);
        jTextFieldSubTotal.setFont(new java.awt.Font("Arial", 1, 18));
        jTextFieldSubTotal.setForeground(new java.awt.Color(0, 0, 204));
        jTextFieldSubTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextFieldSubTotal.setText("R$ 0.00");
        jTextFieldSubTotal.setToolTipText("Valor total da compra sem desconto");
        jTextFieldSubTotal.setFocusable(false);

        jTextFieldTotal.setEditable(false);
        jTextFieldTotal.setFont(new java.awt.Font("Arial", 1, 18));
        jTextFieldTotal.setForeground(new java.awt.Color(0, 0, 204));
        jTextFieldTotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextFieldTotal.setText("R$ 0.00");
        jTextFieldTotal.setToolTipText("Total da compra");
        jTextFieldTotal.setFocusable(false);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 283, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jTextFieldSubTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextFieldTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(jLabel12)
                    .addComponent(jTextFieldDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jButtonFinalizarVenda.setText("Finalizar");
        jButtonFinalizarVenda.setToolTipText("Finaliza venda ");
        jButtonFinalizarVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFinalizarVendaActionPerformed(evt);
            }
        });

        jButtonCancelar.setText("Cancelar");
        jButtonCancelar.setToolTipText("cancelar venda");
        jButtonCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelarActionPerformed(evt);
            }
        });

        jLabel13.setText("Cupom: ");

        jTextFieldNoCupom.setEditable(false);
        jTextFieldNoCupom.setFont(new java.awt.Font("Cambria Math", 1, 14));
        jTextFieldNoCupom.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jTextFieldNoCupom.setText("00000000");
        jTextFieldNoCupom.setToolTipText("Numero do cupom");

        jButtonLimpar.setText("Limpar");
        jButtonLimpar.setToolTipText("Limpa todos os campos");
        jButtonLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLimparActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addGap(6, 6, 6)
                .addComponent(jTextFieldNoCupom, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonFinalizarVenda)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonLimpar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonCancelar)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jTextFieldNoCupom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonFinalizarVenda)
                    .addComponent(jButtonLimpar)
                    .addComponent(jButtonCancelar))
                .addContainerGap(62, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabelHora.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        jLabel15.setText("Usuario:");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelLogin, javax.swing.GroupLayout.DEFAULT_SIZE, 534, Short.MAX_VALUE)
                        .addGap(333, 333, 333))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabelData, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabelHora, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jLabelLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(30, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
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
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void insereItem() {
        try {
            modelo = (DefaultTableModel) jTableItensVenda.getModel();
            String s = jTextFieldValorUni.getText();
            double vtt = Double.parseDouble(s) * Integer.parseInt(jTextFieldQtdePro.getText());

            String total = "" + vtt;
            if (total.substring(total.lastIndexOf(".") + 1).length() < 2) {
                total += "0";
            }

            String unitario = jTextFieldValorUni.getText();
            if (unitario.substring(unitario.lastIndexOf(".") + 1).length() < 2) {
                unitario += "0";
            }
            Object linha[] = new Object[]{jTableItensVenda.getRowCount() + 1, jTextFieldCodigoProduto.getText(),
                jComboBoxProduto.getSelectedItem(), jTextFieldQtdePro.getText(), unitario, total
            };

            modelo.addRow(linha);

            double valorSubTotal = vtt + Double.parseDouble(jTextFieldSubTotal.getText().split(" ")[1]);
            this.setValorSubTotal(valorSubTotal);

            jTableItensVenda.setModel(modelo);

            this.atualizaSoma();
        } catch (Exception e) {
        }
    }

    private void jTextFieldCodigoFuncionarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldCodigoFuncionarioActionPerformed
        this.localizaFuncionario("id");
    }//GEN-LAST:event_jTextFieldCodigoFuncionarioActionPerformed

    private void jTextFieldCodigoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldCodigoClienteActionPerformed
        this.localizaCliente("id");
}//GEN-LAST:event_jTextFieldCodigoClienteActionPerformed

    private void jTextFieldCodigoProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldCodigoProdutoActionPerformed
        if (!jTextFieldCodigoProduto.getText().equals("0")) {
            try {
                Integer.parseInt(jTextFieldCodigoProduto.getText());
                this.localizaProduto("id");
            } catch (Exception e) {
            }
        } else {
            jButtonFinalizarVenda.requestFocus();
        }
    }//GEN-LAST:event_jTextFieldCodigoProdutoActionPerformed

    private void jTextFieldQtdeProActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldQtdeProActionPerformed
        try {
            int qtdeDigitada = Integer.parseInt(jTextFieldQtdePro.getText());
            int qtdeProduto = 0;
            HashMap<String, Object> dados = new HashMap<String, Object>();
            dados.put("modelo", "produto");
            dados.put("operacao", "busca");
            dados.put("posicao", "cod");
            dados.put("cod", "" + Integer.parseInt(jTextFieldCodigoProduto.getText()));
            dados = controlador.recebeOperacao(dados);
            int linha = -1;
            if (((String) dados.get("retorno")).equals("sucesso")) {
                modelo = (DefaultTableModel) jTableItensVenda.getModel();
                for (int i = 0; i < modelo.getRowCount(); i++) {
                    int codigo = Integer.parseInt((String) modelo.getValueAt(i, 1));
                    if (codigo == Integer.parseInt((String) dados.get("cod"))) { //ja esta listado na tabela
                        linha = i;
                        break;
                    }
                }
                qtdeProduto = Integer.parseInt((String) dados.get("qtdeEntrada")) - Integer.parseInt((String) dados.get("qtdeVendida"));
                if (linha != -1) { //ja esta listado na tabela
                    qtdeDigitada += Integer.parseInt((String) modelo.getValueAt(linha, 3));
                    if (qtdeDigitada > qtdeProduto) { //erro
                        if (qtdeProduto != 0 && (qtdeDigitada != qtdeProduto)) {
                            JOptionPane.showMessageDialog(null, "Quantidade indisponivel!\nDisponivel: " + qtdeProduto, "Aviso", JOptionPane.INFORMATION_MESSAGE);
                            jTextFieldQtdePro.setText("1");
                            jTextFieldQtdePro.requestFocus();
                        } else {
                            JOptionPane.showMessageDialog(null, "Produto indisponivel em estoque!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                            jTextFieldQtdePro.setText(" ");
                            jComboBoxProduto.removeAllItems();
                            jTextFieldCodigoProduto.setText(" ");
                            jTextFieldValorUni.setText(" ");
                            jTextFieldCodigoProduto.requestFocus();
                        }

                    } else { //atualiza tabela - colunas 3 e 5
                        modelo.setValueAt(qtdeDigitada + "", linha, 3);
                        modelo.setValueAt((qtdeDigitada * Double.parseDouble((String) modelo.getValueAt(linha, 4))) + "0", linha, 5);

                        //atualiza soma
                        this.atualizaSoma();

                        jTextFieldCodigoProduto.requestFocus();
                        if (this.tipoVenda.equals("A PRAZO")) {
                            this.geraParcelas();
                        }
                    }
                } else { //insere na tabela de acordo com a quantidade
                    if (qtdeDigitada > qtdeProduto) {
                        //verifica se ja foi inserido na tabela
                        if (qtdeProduto != 0 && (qtdeDigitada != qtdeProduto)) {
                            JOptionPane.showMessageDialog(null, "Quantidade indisponivel!\nDisponivel: " + qtdeProduto, "Aviso", JOptionPane.INFORMATION_MESSAGE);
                            jTextFieldQtdePro.setText("1");
                            jTextFieldQtdePro.requestFocus();
                        } else {
                            JOptionPane.showMessageDialog(null, "Produto indisponivel em estoque!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
                            jTextFieldQtdePro.setText(" ");
                            jComboBoxProduto.removeAllItems();
                            jTextFieldCodigoProduto.setText(" ");
                            jTextFieldValorUni.setText(" ");
                            jTextFieldCodigoProduto.requestFocus();
                        }
                    } else {
                        this.insereItem();
                        this.atualizaSoma();
                        jTextFieldCodigoProduto.requestFocus();
                        if (this.tipoVenda.equals("A PRAZO")) {
                            this.geraParcelas();
                        }
                        jTextFieldCodigoProduto.setText("");
                        jTextFieldQtdePro.setText("");
                        jTextFieldValorUni.setText("");
                        jComboBoxProduto.removeAllItems();
                        jTextFieldCodigoProduto.requestFocus();
                    }
                }
            }

        } catch (NumberFormatException e) {
        }

    }//GEN-LAST:event_jTextFieldQtdeProActionPerformed

    private void jTextFieldCodigoProdutoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldCodigoProdutoFocusGained
        jTextFieldValorUni.setText("");
        jTextFieldCodigoProduto.setSelectionStart(0);
        jTextFieldCodigoProduto.setSelectionEnd(jTextFieldCodigoProduto.getText().length());
    }//GEN-LAST:event_jTextFieldCodigoProdutoFocusGained

    private void jTextFieldDescFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDescFocusLost
        this.calculaValorFinal();
}//GEN-LAST:event_jTextFieldDescFocusLost

    private void jTableItensVendaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableItensVendaKeyReleased
        if (KeyEvent.VK_DELETE == evt.getKeyCode()) {
            this.removeLinha();
            this.atualizaSoma();
        }
    }//GEN-LAST:event_jTableItensVendaKeyReleased

    private void jCheckBoxEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxEntradaActionPerformed
        this.atualizaSoma();
        if (jCheckBoxEntrada.isSelected()) {
            jTextFieldValorEntrada.setEnabled(true);
        } else {
            jTextFieldValorEntrada.setText("");
            jTextFieldValorEntrada.setEnabled(false);
            this.geraParcelas();
        }
}//GEN-LAST:event_jCheckBoxEntradaActionPerformed

    private void jComboBoxVezesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxVezesActionPerformed
        this.atualizaSoma();
        double total = Double.parseDouble(jTextFieldTotal.getText().split(" ")[1]);
        if (jComboBoxVezes.getSelectedIndex() != 0 && (total != 0.0)) {
            this.geraParcelas();
        } else {
            modelo = (DefaultTableModel) jTableParcelas.getModel();
            while (modelo.getRowCount() > 0) {
                modelo.removeRow(0);
            }
        }
    }//GEN-LAST:event_jComboBoxVezesActionPerformed

    private void jTextFieldDescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDescActionPerformed
        this.atualizaSoma();
    }//GEN-LAST:event_jTextFieldDescActionPerformed

    private void jComboBoxPagamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxPagamentoActionPerformed
        this.mudarVenda();
        this.tipoVenda = (String) this.jComboBoxPagamento.getSelectedItem();
        if (this.jComboBoxPagamento.getSelectedIndex() == 0) {
            this.tipoVenda = "A VISTA";
        } else {
            this.tipoVenda = "A PRAZO";
        }
        this.setTitle(tipoVenda);
}//GEN-LAST:event_jComboBoxPagamentoActionPerformed

    private void jTextFieldValorUniActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldValorUniActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_jTextFieldValorUniActionPerformed

    private void jTextFieldValorUniFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldValorUniFocusLost
        // TODO add your handling code here:
}//GEN-LAST:event_jTextFieldValorUniFocusLost

    private void jTextFieldValorEntradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldValorEntradaActionPerformed
        jTextFieldValorEntrada.setText(this.trataPreco(jTextFieldValorEntrada.getText()).toUpperCase());
        if (!jTextFieldValorEntrada.getText().equals("")) {
            double entrada = Double.parseDouble(jTextFieldValorEntrada.getText());
            double compra = Double.parseDouble(jTextFieldTotal.getText().split(" ")[1]);
            if ((entrada >= compra) && (compra != 0.0)) {
                JOptionPane.showMessageDialog(null, "Impossivel vender a prazo.\n " +
                        "Valor de entrada e' igual ou superior ao valor de venda.\n" +
                        "Mude o tipo de venda para avista");
                jComboBoxVezes.setSelectedIndex(0);
            } else {
                jComboBoxVezes.setSelectedIndex(jComboBoxVezes.getSelectedIndex());
            }
        }
    }//GEN-LAST:event_jTextFieldValorEntradaActionPerformed

    private void jButtonLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLimparActionPerformed
        this.limpaTudo();
    }//GEN-LAST:event_jButtonLimparActionPerformed

private void jComboBoxFuncionarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxFuncionarioActionPerformed
    if (evt.getActionCommand().toString().equals("comboBoxEdited")) {
        this.localizaFuncionario("nome");
    }
}//GEN-LAST:event_jComboBoxFuncionarioActionPerformed

private void jComboBoxClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxClienteActionPerformed
    if (evt.getActionCommand().toString().equals("comboBoxEdited")) {
        this.localizaCliente("nome");
    }
}//GEN-LAST:event_jComboBoxClienteActionPerformed

private void jComboBoxProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxProdutoActionPerformed
    if (evt.getActionCommand().toString().equals("comboBoxEdited")) {
        this.localizaProduto("desc");
    }
}//GEN-LAST:event_jComboBoxProdutoActionPerformed

private void jButtonFinalizarVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonFinalizarVendaActionPerformed
    int finalizar = JOptionPane.showConfirmDialog(null, "Finalizar venda?", "", JOptionPane.YES_NO_OPTION);
    if (finalizar == 0) { //sim
        if (!(jTextFieldTotal.getText().equals("")) && !(jTextFieldTotal.getText() == null) && !(jTextFieldTotal.getText().split(" ")[1].equals("0.00")) &&
                !jTextFieldCodigoCliente.getText().equals("") && !jTextFieldCodigoFuncionario.getText().equals("")) {
            boolean verificador = true;
            String valorAReceber = "0.00";
            if(!jTextFieldCodigoCliente.getText().equals("ERRO") && !jTextFieldCodigoFuncionario.getText().equals("ERRO") && !jTextFieldValorEntrada.getText().equals("ERRO")){
                this.atualizaSoma();
                if (this.tipoVenda.equals("A VISTA")) {
                    verificador = true;
                    valorAReceber = jTextFieldTotal.getText().split(" ")[1];
                } else {
                    if (jComboBoxVezes.getSelectedIndex() == 0) {
                        verificador = false;
                        JOptionPane.showMessageDialog(null, "Voce deve corrigir a venda. \n Nao ha numero de parcelas .");
                        jComboBoxVezes.requestFocus();
                    }
                    if (jTextFieldValorEntrada.getText().equals("ERRO")) {
                        verificador = false;
                        JOptionPane.showMessageDialog(null, "Voce deve corrigir a venda. \n Valor de entrada contem erro(s).");
                        jCheckBoxEntrada.requestFocus();
                    } else {
                        if ((!jTextFieldValorEntrada.getText().equals("0.00")) && (jTextFieldValorEntrada.getText() != null) && (!jTextFieldValorEntrada.getText().equals(""))) {
                            valorAReceber = jTextFieldValorEntrada.getText();
                            verificador = true;
                        }
                    }
                }
                try {
                    Integer.parseInt(jTextFieldCodigoCliente.getText());
                } catch (NumberFormatException numberFormatException) {
                    verificador = false;
                    jTextFieldCodigoCliente.setText("ERRO");
                    JOptionPane.showMessageDialog(null, "Voce deve corrigir a venda. \n Codigo do cliente invalido.");
                }
                try {
                    Integer.parseInt(jTextFieldCodigoFuncionario.getText());
                } catch (NumberFormatException numberFormatException) {
                    verificador = false;
                    jTextFieldCodigoFuncionario.setText("ERRO");
                    JOptionPane.showMessageDialog(null, "Voce deve corrigir a venda. \n Codigo do funcionario invalido.");
                }
            }else{
                verificador = false;
            }
            if (verificador) {
                if (this.finalizaVenda().equals("sucesso")) {
                    this.atualizaClienteFuncionario();
                    if (!valorAReceber.equals("0.00")) {
                        while (recebimento) {
                            this.jDialogRecebimento.setSize(330, 260);
                            this.jDialogRecebimento.setLocationRelativeTo(this);
                            this.jDialogRecebimento.setTitle("RECEBIMENTO");
                            jTextFieldValorCaixa.setText(valorAReceber);
                            jTextFieldDinheiroCaixa.requestFocus();
                            this.jDialogRecebimento.setVisible(true);
                        }
                        this.imprimirRecibo();
                    }
                    //carregar uma tela para receber a venda.
                    this.imprimirVenda();

                    finalizar = JOptionPane.showConfirmDialog(null, "Realizar outra venda?", "", JOptionPane.YES_NO_OPTION);
                    if (finalizar == 0) {
                        this.limpaTudo();
                        jTextFieldCodigoFuncionario.requestFocus();
                    } else {
                        new TelaPrincipalGer(sessao).show();
                        this.dispose();
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null, "Venda contem erros!");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Venda invalida!");
        }
    }
}//GEN-LAST:event_jButtonFinalizarVendaActionPerformed

private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
    new TelaPrincipalGer(sessao).show();
    this.dispose();
}//GEN-LAST:event_formWindowClosing

private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
// TODO add your handling code here:
}//GEN-LAST:event_formWindowClosed

private void jButtonConsultaFuncionarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConsultaFuncionarioActionPerformed
    this.jDialogConsulta.setSize(800, 405);
    this.jDialogConsulta.setLocationRelativeTo(this);
    this.jDialogConsulta.setTitle("CONSULTA FUNCIONARIO");
    l = new ArrayList();
    jTableConsulta.getColumnModel().getColumn(0).setPreferredWidth(3);
    jTableConsulta.getColumnModel().getColumn(1).setPreferredWidth(350);
    jTableConsulta.getColumnModel().getColumn(2).setPreferredWidth(70);
    jTableConsulta.getTableHeader().setReorderingAllowed(false);
    jTableConsulta.getColumnModel().getColumn(0).setPreferredWidth(3);
    jTableConsulta.getTableHeader().setReorderingAllowed(false);
    this.limpaTabelaConsulta();
    this.alinhaColunas();
    this.tipoConsulta = "FUNCIONARIO";
    this.preencheTabelaConsulta();
    jTableConsulta.getColumnModel().getColumn(2).setHeaderValue("CPF");
    this.jDialogConsulta.setVisible(true);
}//GEN-LAST:event_jButtonConsultaFuncionarioActionPerformed

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

private void jTableConsultaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableConsultaMouseClicked
    if (evt.getClickCount() == 2) {
        if (jTableConsulta.getSelectedRow() >= 0) {
            modelo = (DefaultTableModel) jTableConsulta.getModel();
            if (this.tipoConsulta.equals("CLIENTE")) {
                jTextFieldCodigoCliente.setText((String) modelo.getValueAt(jTableConsulta.getSelectedRow(), 0));
                jComboBoxCliente.removeAllItems();
                jComboBoxCliente.addItem(modelo.getValueAt(jTableConsulta.getSelectedRow(), 1));
                jComboBoxCliente.setSelectedIndex(0);
                jTextFieldCodigoProduto.requestFocus();
            } else {
                if (this.tipoConsulta.equals("FUNCIONARIO")) {
                    jTextFieldCodigoFuncionario.setText((String) modelo.getValueAt(jTableConsulta.getSelectedRow(), 0));
                    jComboBoxFuncionario.removeAllItems();
                    jComboBoxFuncionario.addItem(modelo.getValueAt(jTableConsulta.getSelectedRow(), 1));
                    jComboBoxFuncionario.setSelectedIndex(0);
                    jTextFieldCodigoCliente.requestFocus();
                } else {
                    if (this.tipoConsulta.equals("PRODUTO")) {
                        jTextFieldCodigoProduto.setText((String) modelo.getValueAt(jTableConsulta.getSelectedRow(), 0));
                        jComboBoxProduto.removeAllItems();
                        jComboBoxProduto.addItem(modelo.getValueAt(jTableConsulta.getSelectedRow(), 1));
                        jComboBoxProduto.setSelectedIndex(0);
                        jTextFieldValorUni.setText((String) modelo.getValueAt(jTableConsulta.getSelectedRow(), 2));
                        jTextFieldQtdePro.requestFocus();
                    }
                }
            }
            jDialogConsulta.dispose();
        }
    }
}//GEN-LAST:event_jTableConsultaMouseClicked

private void jButtonConsultaClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConsultaClienteActionPerformed
    this.jDialogConsulta.setSize(800, 405);
    this.jDialogConsulta.setLocationRelativeTo(this);
    this.jDialogConsulta.setTitle("CONSULTA CLIENTE");
    l = new ArrayList();
    jTableConsulta.getColumnModel().getColumn(0).setPreferredWidth(3);
    jTableConsulta.getColumnModel().getColumn(1).setPreferredWidth(350);
    jTableConsulta.getColumnModel().getColumn(2).setPreferredWidth(70);
    jTableConsulta.getTableHeader().setReorderingAllowed(false);
    jTableConsulta.getColumnModel().getColumn(0).setPreferredWidth(3);
    jTableConsulta.getTableHeader().setReorderingAllowed(false);
    this.limpaTabelaConsulta();
    this.alinhaColunas();
    this.tipoConsulta = "CLIENTE";
    this.preencheTabelaConsulta();
    jTableConsulta.getColumnModel().getColumn(2).setHeaderValue("CPF");
    this.jDialogConsulta.setVisible(true);
}//GEN-LAST:event_jButtonConsultaClienteActionPerformed

private void jFormattedTextFieldCodKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jFormattedTextFieldCodKeyReleased
    if (jFormattedTextFieldCod.getText().length() == 0) {
        this.preencheTabelaConsulta();
        jTextFieldNome.setText("");
    }
}//GEN-LAST:event_jFormattedTextFieldCodKeyReleased

private void jButtonConsultaProdutoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonConsultaProdutoActionPerformed
    this.jDialogConsulta.setSize(800, 405);
    this.jDialogConsulta.setLocationRelativeTo(this);
    this.jDialogConsulta.setTitle("CONSULTA PRODUTO");
    l = new ArrayList();
    jTableConsulta.getColumnModel().getColumn(0).setPreferredWidth(3);
    jTableConsulta.getColumnModel().getColumn(1).setPreferredWidth(370);
    jTableConsulta.getColumnModel().getColumn(2).setPreferredWidth(50);
    jTableConsulta.getTableHeader().setReorderingAllowed(false);
    jTableConsulta.getColumnModel().getColumn(0).setPreferredWidth(3);
    jTableConsulta.getTableHeader().setReorderingAllowed(false);
    this.limpaTabelaConsulta();
    this.alinhaColunas();
    this.tipoConsulta = "PRODUTO";
    this.preencheTabelaConsulta();
    jTableConsulta.getColumnModel().getColumn(2).setHeaderValue("VALOR");
    this.jDialogConsulta.setVisible(true);
}//GEN-LAST:event_jButtonConsultaProdutoActionPerformed

private void jTextFieldCodigoFuncionarioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldCodigoFuncionarioFocusGained
    jTextFieldCodigoFuncionario.setSelectionStart(0);
    jTextFieldCodigoFuncionario.setSelectionEnd(jTextFieldCodigoFuncionario.getText().length());
}//GEN-LAST:event_jTextFieldCodigoFuncionarioFocusGained

private void jTextFieldCodigoClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldCodigoClienteFocusGained
    jTextFieldCodigoCliente.setSelectionStart(0);
    jTextFieldCodigoCliente.setSelectionEnd(jTextFieldCodigoCliente.getText().length());
}//GEN-LAST:event_jTextFieldCodigoClienteFocusGained

private void jTextFieldQtdeProFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldQtdeProFocusGained
    jTextFieldQtdePro.setSelectionStart(0);
    jTextFieldQtdePro.setSelectionEnd(jTextFieldQtdePro.getText().length());
}//GEN-LAST:event_jTextFieldQtdeProFocusGained

private void jButtonCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonCancelarActionPerformed
    new TelaPrincipalGer(sessao).show();
    this.dispose();
}//GEN-LAST:event_jButtonCancelarActionPerformed

private void timer1OnTime(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timer1OnTime
    DataSistema d = DataSistema.getInstance();
    String g = d.diaMesAno();
    jLabelData.setText(g);
    String h = d.horaMinSeg();
    jLabelHora.setText(h);
}//GEN-LAST:event_timer1OnTime

private void jButtonReceberCaixaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReceberCaixaActionPerformed
    try {
        double dinheiro = Double.parseDouble(this.trataPreco(jTextFieldDinheiroCaixa.getText()));
        double valorParcela = Double.parseDouble(this.trataPreco(jTextFieldValorCaixa.getText()));
        if (dinheiro < valorParcela) {
            JOptionPane.showMessageDialog(null, "Valor insuficiente!", "Erro", JOptionPane.INFORMATION_MESSAGE);
            jTextFieldDinheiroCaixa.setSelectionStart(0);
            jTextFieldDinheiroCaixa.setSelectionEnd(jTextFieldDinheiroCaixa.getText().length());
            jTextFieldDinheiroCaixa.requestFocus();
        } else {
            this.atualizaCaixa();
            recebimento = false;
        }
    } catch (Exception e) {
        System.out.println("" + e.toString());
        JOptionPane.showMessageDialog(null, "Valor invalido!", "Erro", JOptionPane.INFORMATION_MESSAGE);
        jTextFieldDinheiroCaixa.setSelectionStart(0);
        jTextFieldDinheiroCaixa.setSelectionEnd(jTextFieldDinheiroCaixa.getText().length());
        jTextFieldDinheiroCaixa.requestFocus();

    }


}//GEN-LAST:event_jButtonReceberCaixaActionPerformed

private void jTextFieldDinheiroCaixaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDinheiroCaixaFocusGained
    jTextFieldDinheiroCaixa.setSelectionStart(0);
    jTextFieldDinheiroCaixa.setSelectionEnd(jTextFieldDinheiroCaixa.getText().length());
}//GEN-LAST:event_jTextFieldDinheiroCaixaFocusGained

private void jTextFieldDinheiroCaixaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldDinheiroCaixaActionPerformed
    try {
        double dinheiro = Double.parseDouble(this.trataPreco(jTextFieldDinheiroCaixa.getText()));
        double valorParcela = Double.parseDouble(this.trataPreco(jTextFieldValorCaixa.getText()));
        if (dinheiro < valorParcela) {
            JOptionPane.showMessageDialog(null, "Valor insuficiente!", "Erro", JOptionPane.INFORMATION_MESSAGE);
            jTextFieldDinheiroCaixa.setSelectionStart(0);
            jTextFieldDinheiroCaixa.setSelectionEnd(jTextFieldDinheiroCaixa.getText().length());
        } else {
            jTextFieldTrocoCaixa.setText(this.trataPreco("" + (dinheiro - valorParcela)));
            jButtonReceberCaixa.requestFocus();
        }
    } catch (Exception e) {
        jTextFieldDinheiroCaixa.setSelectionStart(0);
        jTextFieldDinheiroCaixa.setSelectionEnd(jTextFieldDinheiroCaixa.getText().length());
    }
}//GEN-LAST:event_jTextFieldDinheiroCaixaActionPerformed

private void jTextFieldDinheiroCaixaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldDinheiroCaixaFocusLost
    jTextFieldDinheiroCaixa.setText(this.trataPreco(jTextFieldDinheiroCaixa.getText()));
}//GEN-LAST:event_jTextFieldDinheiroCaixaFocusLost

private void jTextFieldValorEntradaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldValorEntradaFocusGained
    jTextFieldValorEntrada.setSelectionStart(0);
    jTextFieldValorEntrada.setSelectionEnd(jTextFieldValorEntrada.getText().length());
}//GEN-LAST:event_jTextFieldValorEntradaFocusGained

private void jComboBoxVezesMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jComboBoxVezesMouseReleased
    // TODO add your handling code here:
}//GEN-LAST:event_jComboBoxVezesMouseReleased

private void jTextFieldValorEntradaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldValorEntradaFocusLost
    jTextFieldValorEntrada.setText(this.trataPreco(jTextFieldValorEntrada.getText()).toUpperCase());
}//GEN-LAST:event_jTextFieldValorEntradaFocusLost

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new TelaVendaGer().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonCancelar;
    private javax.swing.JButton jButtonConsultaCliente;
    private javax.swing.JButton jButtonConsultaFuncionario;
    private javax.swing.JButton jButtonConsultaProduto;
    private javax.swing.JButton jButtonFinalizarVenda;
    private javax.swing.JButton jButtonLimpar;
    private javax.swing.JButton jButtonReceberCaixa;
    private javax.swing.JCheckBox jCheckBoxEntrada;
    private javax.swing.JComboBox jComboBoxCliente;
    private javax.swing.JComboBox jComboBoxFuncionario;
    private javax.swing.JComboBox jComboBoxPagamento;
    private javax.swing.JComboBox jComboBoxProduto;
    private javax.swing.JComboBox jComboBoxVezes;
    private javax.swing.JDialog jDialogConsulta;
    private javax.swing.JDialog jDialogRecebimento;
    private javax.swing.JFormattedTextField jFormattedTextFieldCod;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelData;
    private javax.swing.JLabel jLabelHora;
    private javax.swing.JLabel jLabelLogin;
    private javax.swing.JLabel jLabelNameConsulta;
    private javax.swing.JLabel jLabelVezes;
    private javax.swing.JPanel jPanel1;
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
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTableConsulta;
    private javax.swing.JTable jTableItensVenda;
    private javax.swing.JTable jTableParcelas;
    private javax.swing.JTextField jTextFieldCodigoCliente;
    private javax.swing.JTextField jTextFieldCodigoFuncionario;
    private javax.swing.JTextField jTextFieldCodigoProduto;
    private javax.swing.JTextField jTextFieldDataVenda;
    private javax.swing.JTextField jTextFieldDesc;
    private javax.swing.JTextField jTextFieldDinheiroCaixa;
    private javax.swing.JTextField jTextFieldHoraVenda;
    private javax.swing.JTextField jTextFieldNoCupom;
    private javax.swing.JTextField jTextFieldNome;
    private javax.swing.JTextField jTextFieldQtdePro;
    private javax.swing.JTextField jTextFieldSubTotal;
    private javax.swing.JTextField jTextFieldTotal;
    private javax.swing.JTextField jTextFieldTrocoCaixa;
    private javax.swing.JTextField jTextFieldValorCaixa;
    private javax.swing.JTextField jTextFieldValorEntrada;
    private javax.swing.JTextField jTextFieldValorUni;
    private org.netbeans.examples.lib.timerbean.Timer timer1;
    // End of variables declaration//GEN-END:variables

    private void limpaTabelaItens() {
        modelo = (DefaultTableModel) jTableItensVenda.getModel();
        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }
    }
    // End of variables declaration

    private void limpaTabelaParcela() {
        modelo = (DefaultTableModel) jTableParcelas.getModel();
        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }
    }
    // End of variables declaration

    private void limpaTudo() {
        jComboBoxFuncionario.removeAllItems();
        jComboBoxCliente.removeAllItems();
        jComboBoxProduto.removeAllItems();
        jTextFieldCodigoCliente.setText("");
        jTextFieldCodigoFuncionario.setText("");
        this.preConstrutor();
        jTextFieldCodigoProduto.setText("");
        if(this.tipoVenda.equals("A VISTA")){
            jTextFieldDesc.setText("10");
        }else{
            jTextFieldDesc.setText("");
        }
        jTextFieldQtdePro.setText("");
        jTextFieldSubTotal.setText("");
        jTextFieldTotal.setText("");
        jTextFieldValorEntrada.setText("");
        jTextFieldValorUni.setText("");
        jComboBoxVezes.setSelectedIndex(0);
        jCheckBoxEntrada.setSelected(false);
        recebimento = true;
        this.limpaTabelaParcela();
        this.limpaTabelaItens();
        this.limpaCaixa();
        this.atualizaSoma();
    }
    // End of variables declaration

    private void localizaCliente(String pesquisa) {
        HashMap<String, Object> dados = new HashMap<String, Object>();
        String digitado = "";
        if (pesquisa.equals("id")) {
            digitado = jTextFieldCodigoCliente.getText();
            if ((digitado != null) && (digitado.compareTo("") != 0)) {
                dados.put("modelo", "cliente");
                dados.put("operacao", "busca");
                dados.put("posicao", "cod");
                try {
                    dados.put("cod", "" + Integer.parseInt(digitado));
                    dados = controlador.recebeOperacao(dados);
                    if (((String) dados.get("retorno")).equals("sucesso")) {
                        if (((String) dados.get("ativo")).equals("true")) {
                            if (((String) dados.get("cadastroAprovado")).equals("true")) {
                                jComboBoxCliente.removeAllItems();
                                jComboBoxCliente.addItem(dados.get("nome"));
                                jComboBoxCliente.setSelectedIndex(0);
                                jTextFieldCodigoProduto.requestFocus();
                            } else {
                                JOptionPane.showMessageDialog(null, "Cadastro do cliente: " +
                                        digitado + " nao esta' aprovado");
                                jComboBoxCliente.removeAllItems();
                                jTextFieldCodigoCliente.setText("");
                                jTextFieldCodigoCliente.requestFocus();
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "Cadastro do cliente: " +
                                    digitado + " nao encontrado");
                            jComboBoxCliente.removeAllItems();
                            jTextFieldCodigoCliente.setText("");
                            jTextFieldCodigoCliente.requestFocus();
                        }
                    } else {
                        jComboBoxCliente.removeAllItems();
                        jTextFieldCodigoCliente.setText("");
                        jTextFieldCodigoCliente.requestFocus();
                    }
                } catch (NumberFormatException numberFormatException) {
                    jComboBoxCliente.removeAllItems();
                    jTextFieldCodigoCliente.setText("ERRO");
                    jTextFieldCodigoCliente.requestFocus();
                }
            }
        } else {
            try {
                l = new ArrayList();
                dados.put("modelo", "cliente");
                dados.put("operacao", "ordenaLista");
                dados.put("classificacao", "ordem");
                dados = controlador.recebeOperacao(dados);
                l = (List) dados.get("lista");
                List listaNomes = new ArrayList();
                List listaCodigos = new ArrayList();
                if (l.size() > 0) {
                    digitado = (String) jComboBoxCliente.getSelectedItem();
                    CharSequence charSeq = digitado.toUpperCase();
                    jComboBoxCliente.removeAllItems();
                    for (int i = 0; i < l.size(); i++) {
                        dados = (HashMap<String, Object>) l.get(i);
                        if ((((String) dados.get("nome")).contains(charSeq)) &&
                                (((String) dados.get("cadastroAprovado")).equals("true")) &&
                                ((String) dados.get("ativo")).equals("true")) {
                            jComboBoxCliente.addItem(dados.get(pesquisa));
                            listaNomes.add(dados.get(pesquisa));
                            listaCodigos.add(dados.get("cod"));
                        }
                    }
                    jComboBoxCliente.setPopupVisible(true);
                    if (listaCodigos.size() > 1) {
                        jComboBoxCliente.setPopupVisible(true);
                    } else {
                        if (listaCodigos.size() == 1) {
                            jComboBoxCliente.setSelectedItem(listaNomes.get(0));
                            jTextFieldCodigoProduto.requestFocus();
                        }
                    }
                    jTextFieldCodigoCliente.setText("" + listaCodigos.get(jComboBoxCliente.getSelectedIndex()));
                } else {
                    jTextFieldCodigoCliente.setText("");
                    jComboBoxCliente.removeAllItems();
                    jTextFieldCodigoCliente.requestFocus();
                }
            } catch (Exception ex) {
                jTextFieldCodigoCliente.setText("");
                jComboBoxCliente.removeAllItems();
                jTextFieldCodigoCliente.requestFocus();
            }
        }
    }

    private void localizaFuncionario(String pesquisa) {
        HashMap<String, Object> dados = new HashMap<String, Object>();
        String digitado = "";
        if (pesquisa.equals("id")) {
            digitado = jTextFieldCodigoFuncionario.getText();
            if ((digitado != null) && (digitado.compareTo("") != 0)) {
                dados.put("modelo", "funcionario");
                dados.put("operacao", "busca");
                dados.put("posicao", "cod");
                try {
                    dados.put("cod", "" + Integer.parseInt(digitado));
                    dados = controlador.recebeOperacao(dados);
                    if (((String) dados.get("retorno")).equals("sucesso")) {
                        jComboBoxFuncionario.removeAllItems();
                        jComboBoxFuncionario.addItem(dados.get("nome"));
                        jComboBoxFuncionario.setSelectedIndex(0);
                        jTextFieldCodigoCliente.requestFocus();
                    } else {
                        jComboBoxFuncionario.removeAllItems();
                        jTextFieldCodigoFuncionario.setText("");
                        jTextFieldCodigoFuncionario.requestFocus();
                    }
                } catch (NumberFormatException numberFormatException) {
                    jComboBoxFuncionario.removeAllItems();
                    jTextFieldCodigoFuncionario.setText("ERRO");
                    jTextFieldCodigoFuncionario.requestFocus();
                }
            }
        } else {
            try {
                l = new ArrayList();
                dados.put("modelo", "funcionario");
                dados.put("operacao", "ordenaLista");
                dados.put("classificacao", "ordem");
                dados = controlador.recebeOperacao(dados);
                l = (List) dados.get("lista");
                List listaNomes = new ArrayList();
                List listaCodigos = new ArrayList();
                if (l.size() > 0) {
                    digitado = (String) jComboBoxFuncionario.getSelectedItem();
                    CharSequence charSeq = digitado.toUpperCase();
                    jComboBoxFuncionario.removeAllItems();
                    for (int i = 0; i < l.size(); i++) {
                        dados = (HashMap<String, Object>) l.get(i);
                        if (((String) dados.get("nome")).contains(charSeq)) {
                            jComboBoxFuncionario.addItem(dados.get(pesquisa));
                            listaNomes.add(dados.get(pesquisa));
                            listaCodigos.add(dados.get("cod"));
                        }
                    }
                    jComboBoxFuncionario.setPopupVisible(true);
                    if (listaCodigos.size() > 1) {
                        jComboBoxFuncionario.setPopupVisible(true);
                    } else {
                        if (listaCodigos.size() == 1) {
                            jComboBoxFuncionario.setSelectedItem(listaNomes.get(0));
                            jTextFieldCodigoCliente.requestFocus();
                        }
                    }
                    jTextFieldCodigoFuncionario.setText("" + listaCodigos.get(jComboBoxFuncionario.getSelectedIndex()));
                } else {
                    jTextFieldCodigoFuncionario.setText("");
                    jComboBoxFuncionario.removeAllItems();
                    jTextFieldCodigoFuncionario.requestFocus();
                }
            } catch (Exception ex) {
                jTextFieldCodigoFuncionario.setText("");
                jComboBoxFuncionario.removeAllItems();
                jTextFieldCodigoFuncionario.requestFocus();
            }
        }
    }

    private void localizaProduto(String pesquisa) {
        HashMap<String, Object> dados = new HashMap<String, Object>();
        String digitado = "";
        if (pesquisa.equals("id")) {
            digitado = jTextFieldCodigoProduto.getText();
            if ((digitado != null) && (digitado.compareTo("") != 0)) {
                dados.put("modelo", "produto");
                dados.put("operacao", "busca");
                dados.put("posicao", "cod");
                dados.put("cod", "" + Integer.parseInt(digitado));
                dados = controlador.recebeOperacao(dados);
                if (((String) dados.get("retorno")).equals("sucesso") && ((String) dados.get("ativo")).equals("true")) {
                    jComboBoxProduto.removeAllItems();
                    jComboBoxProduto.addItem(dados.get("desc"));
                    jComboBoxProduto.setSelectedIndex(0);
                    jTextFieldValorUni.setText((String) dados.get("valorVenda"));
                    jTextFieldQtdePro.setText("1");
                    jTextFieldQtdePro.requestFocus();
                } else {
                    jComboBoxProduto.removeAllItems();
                    jTextFieldCodigoProduto.setText("");
                    jTextFieldCodigoProduto.requestFocus();
                }
            }
        } else {
            try {
                l = new ArrayList();
                dados.put("modelo", "produto");
                dados.put("operacao", "ordenaLista");
                dados.put("classificacao", "ordem");
                dados = controlador.recebeOperacao(dados);
                l = (List) dados.get("lista");
                List listaNomes = new ArrayList();
                List listaCodigos = new ArrayList();
                List listaPrecos = new ArrayList();
                if (l.size() > 0) {
                    digitado = (String) jComboBoxProduto.getSelectedItem();
                    CharSequence charSeq = digitado.toUpperCase();
                    jComboBoxProduto.removeAllItems();
                    for (int i = 0; i < l.size(); i++) {
                        dados = (HashMap<String, Object>) l.get(i);
                        if (((String) dados.get("desc")).contains(charSeq) && ((String) dados.get("ativo")).equals("true")) {
                            jComboBoxProduto.addItem(dados.get(pesquisa));
                            listaNomes.add(dados.get(pesquisa));
                            listaCodigos.add(dados.get("cod"));
                            listaPrecos.add(dados.get("valorVenda"));
                        }
                    }
                    jComboBoxProduto.setPopupVisible(true);
                    if (listaCodigos.size() > 1) {
                        jComboBoxProduto.setPopupVisible(true);
                    } else {
                        if (listaCodigos.size() == 1) {
                            jComboBoxProduto.setSelectedItem(listaNomes.get(0));
                            jTextFieldCodigoProduto.setText("" + listaCodigos.get(0));
                            jTextFieldValorUni.setText((String) listaPrecos.get(0));
                            jTextFieldQtdePro.setText("1");
                            jTextFieldQtdePro.requestFocus();
                        }
                    }
                    jTextFieldCodigoProduto.setText("" + listaCodigos.get(jComboBoxProduto.getSelectedIndex()));
                    jTextFieldValorUni.setText((String) listaPrecos.get(jComboBoxProduto.getSelectedIndex()));
                } else {
                    jTextFieldCodigoProduto.setText("");
                    jComboBoxProduto.removeAllItems();
                    jTextFieldCodigoProduto.requestFocus();
                }
            } catch (Exception ex) {
                System.out.println(ex);
                jTextFieldCodigoProduto.setText("");
                jComboBoxProduto.removeAllItems();
                jTextFieldCodigoProduto.requestFocus();
            }
        }
    }

    private void removeLinha() {
        int linhaSelecionada = jTableItensVenda.getSelectedRow();
        modelo = (DefaultTableModel) jTableItensVenda.getModel();
        if (linhaSelecionada >= 0) {
            double d = Double.parseDouble((String) modelo.getValueAt(linhaSelecionada, 5));
            double valorSubTotal = Double.parseDouble(jTextFieldSubTotal.getText().split(" ")[1]) - d;
            this.setValorSubTotal(valorSubTotal);
            this.calculaValorFinal();
            modelo.removeRow(linhaSelecionada);
            for (int i = 0; i < modelo.getRowCount(); i++) {
                modelo.setValueAt(i + 1, i, 0);
            }
        }

        //jTableItensVenda.setModel(modelo);
    }

    private void setValorSubTotal(double valorSubTotal) {
        String total = "" + valorSubTotal;
        if (total.substring(total.lastIndexOf(".") + 1).length() < 2) {
            total += "0";
        }
        total = "R$ " + total;
        jTextFieldSubTotal.setText(total);
    }

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
                            CharSequence charSeq = digitado.toUpperCase();
                            if (((String) dados.get("nome")).contains(charSeq)) {
                                Object[] linha = new Object[]{((String) dados.get("cod")), ((String) dados.get("nome")), ((String) dados.get("cpf"))};
                                modelo.addRow(linha);
                            }
                        }
                        //jTable1.setModel(modelo);
                    } else {
                        JOptionPane.showMessageDialog(null, "Nenhum registro encontrado!");
                    }
                } else {
                    this.limpaTabelaConsulta();
                }
            } else {
                if (tipoConsulta.compareTo("PRODUTO") == 0) {
                    //dao = new DAOProdutoDB4O();
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
                            CharSequence charSeq = digitado.toUpperCase();
                            if (((String) dados.get("desc")).contains(charSeq)) {
                                Object[] linha = new Object[]{((String) dados.get("cod")), ((String) dados.get("desc")), ((String) dados.get("valorVenda"))};
                                modelo.addRow(linha);
                            }
                        }
                        //jTable1.setModel(modelo);
                    } else {
                        this.limpaTabelaConsulta();
                    }
                }
            }
        } else {
            this.preencheTabelaConsulta();
        }
    }

    private void limpaTabelaConsulta() {
        modelo = (DefaultTableModel) jTableConsulta.getModel();
        while (modelo.getRowCount() > 0) {
            modelo.removeRow(0);
        }
        jTableConsulta.setModel(modelo);
    }

    private void preencheTabelaConsulta() {
        HashMap<String, Object> dados = new HashMap<String, Object>();
        String coluna2 = "";
        String coluna3 = "";
        if (this.tipoConsulta.equals("CLIENTE") || this.tipoConsulta.equals("FUNCIONARIO")) {
            coluna2 = "nome";
            coluna3 = "cpf";
        } else {
            coluna2 = "desc";
            coluna3 = "valorVenda";
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
                    if (this.tipoConsulta.equals("CLIENTE")) {
                        if (((String) dados.get("cadastroAprovado")).equals("true") && (((String) dados.get("ativo")).equals("true"))) {
                            Object[] linha = new Object[]{((String) dados.get("cod")), ((String) dados.get(coluna2)), ((String) dados.get(coluna3))};
                            modelo.addRow(linha);
                        }
                    } else {
                        if (((String) dados.get("ativo")).equals("true")) {
                            Object[] linha = new Object[]{((String) dados.get("cod")), ((String) dados.get(coluna2)), ((String) dados.get(coluna3))};
                            modelo.addRow(linha);
                        }

                    }

                }
            }
        } catch (Exception ex) {
            this.limpaTabelaConsulta();
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
            coluna2 = "desc";
            coluna3 = "valorVenda";
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
                    if (!((String) dados.get("retorno")).equals("sucesso")) {
                        JOptionPane.showMessageDialog(this, "Nenhum " + tipoConsulta + " foi encontrado com este codigo.");
                        this.preencheTabelaConsulta();
                    } else {
                        Object[] linha = new Object[]{((String) dados.get("cod")), ((String) dados.get(coluna2)), ((String) dados.get(coluna3))};
                        modelo.addRow(linha);
                        jTextFieldNome.setText((String) dados.get("nome"));
                    }

                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao consultar " + tipoConsulta + " .");
            this.limpaTabelaConsulta();
        }
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
                    for (int i = 0; i <
                            l.size(); i++) {
                        dados = (HashMap<String, Object>) l.get(i);
                        Object[] linha = new Object[]{((String) dados.get("cod")), ((String) dados.get("nome")), ((String) dados.get("cpf"))};
                        modelo.addRow(linha);
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
                        for (int i = 0; i <
                                l.size(); i++) {
                            dados = (HashMap<String, Object>) l.get(i);
                            Object[] linha = new Object[]{((String) dados.get("cod")), ((String) dados.get("desc")), ((String) dados.get("valorVenda"))};
                            modelo.addRow(linha);
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
            }
        }
    }

    private String setMaiorId() {
        String ret = "";
        HashMap<String, Object> dados = new HashMap<String, Object>();
        dados.put("modelo", "venda");
        dados.put("operacao", "maiorId");
        dados = controlador.recebeOperacao(dados);
        if (dados != null) {
            ret = (String) dados.get("retorno");
            if (ret.length() < 8) {
                int tam = 8 - ret.length();
                String s = "";
                for (int j = 0; j < tam; j++) {
                    s += "0";
                }
                ret = s + ret;
            }
        }
        return ret;
    }

    private void atualizaEstoque(int codigo, int quantidade) {
        HashMap<String, Object> dados = new HashMap<String, Object>();
        dados.put("modelo", "produto");
        dados.put("operacao", "busca");
        dados.put("posicao", "cod");
        dados.put("cod", codigo + "");
        dados = controlador.recebeOperacao(dados);
        l = (List) dados.get("lista");
        dados = (HashMap<String, Object>) l.get(0);
        int qtdeVendida = Integer.parseInt((String) dados.get("qtdeVendida"));

        dados.remove("qtdeVendida");
        qtdeVendida = (qtdeVendida + quantidade);
        dados.put("qtdeVendida", "" + qtdeVendida);
        if (Integer.parseInt((String) dados.get("qtdeEntrada")) == qtdeVendida) {
            dados.put("dataTermino", jLabelData.getText().split(" ")[1]);
        } else {
            dados.put("dataTermino", "");
        }
        dados.put("modelo", "produto");
        dados.put("operacao", "alterar");
        dados = controlador.recebeOperacao(dados);
    }

    private void atualizaClienteFuncionario() {
        HashMap<String, Object> dados = new HashMap<String, Object>();
        String model = "";
        String parametro = "";
        String codigo = "";
        for (int j = 0; j < 2; j++) {
            if (j == 0) {
                model = "cliente";
                parametro = "compras";
                codigo = jTextFieldCodigoCliente.getText();
            } else {
                model = "funcionario";
                parametro = "vendas";
                codigo = jTextFieldCodigoFuncionario.getText();
            }
            dados.put("modelo", model);
            dados.put("operacao", "busca");
            dados.put("posicao", "cod");
            dados.put("cod", codigo);
            dados = controlador.recebeOperacao(dados);
            l = (List) dados.get("lista");
            dados = (HashMap<String, Object>) l.get(0);
            String compras = (String) dados.get(parametro);
            if (compras.equals("")) {
                compras = jTextFieldNoCupom.getText();
            } else {
                compras += jTextFieldNoCupom.getText();
            }
            dados.put(parametro, compras);
            dados.put("modelo", model);
            dados.put("operacao", "alterar");
            dados = controlador.recebeOperacao(dados);
        }
    }

    private void imprimirVenda() {
        Impressao impressao = new Impressao();
        impressao.imprimir(this.getModeloImprimir());

    }

    private String getModeloImprimir() {
        HashMap<String, Object> dados = this.pegaDados(false);
        String impri = "";
        impri += "|============================================================|\n";
        if (((String) dados.get("avista")).equals("true")) {
            impri += "        VENDA A VISTA\n";
        } else {
            impri += "        VENDA A PRAZO\n";
        }
        impri += " Venda No: ";
        String aux = dados.get("cod") + "";
        while (aux.length() < 15) {
            aux += " ";
        }
        impri += aux + "Horario: " + jLabelHora.getText() + "  Data: " + dados.get("dataVenda") + "\n";
        impri += "|------------------------------------------------------------|\n";
        impri += " Funcionario: " + dados.get("codFuncionario") + "--";
        aux = jComboBoxFuncionario.getSelectedItem() + "";
        if (aux.length() > 40) {
            String[] split = aux.split("");
            for (int i = 0; i < split.length; i++) {
                impri += split[i];
            }
        } else {
            impri += aux;
        }
        impri += "\n Cliente: " + dados.get("codCliente") + "--";
        aux = jComboBoxCliente.getSelectedItem() + "";
        if (aux.length() > 45) {
            String[] split = aux.split("");
            for (int i = 0; i < split.length; i++) {
                impri += split[i];
            }
            impri += "\n";
        } else {
            impri += aux + "\n";
        }
        impri += "|------------------------------------------------------------|\n";
        impri += " ITEM|  CODIGO |       DESCRICAO     | QTDE |  UNIT |  TOTAL\n";
        //calcular o tamanho dos campos...
        //exemplo: codigo#descricao#qtde#valorUnitario
        List lista = (List) dados.get("itensVenda");
        String str = "";
        for (int i = 0; i < lista.size(); i++) {
            aux = " " + (i + 1);
            while (aux.length() < 5) {
                aux += " ";
            }
            aux += "|";
            str += aux;
            aux = ((String) lista.get(i)).split("#")[0];
            String preencher = "";
            for (int k = 0; k < (9 - aux.length()); k++) {
                preencher += " ";
            }
            aux += "|";
            str += preencher + aux;
            aux = ((String) lista.get(i)).split("#")[1];
            while (aux.length() < 21) {
                aux += " ";
            }
            if (aux.length() > 21) {
                String[] desc = aux.split("");
                aux = "";
                for (int j = 0; j < 22; j++) {
                    aux += desc[j];
                }
            }
            aux += "|";
            str += aux;
            aux = ((String) lista.get(i)).split("#")[2];
            preencher = "";
            for (int k = 0; k < (6 - aux.length()); k++) {
                preencher += " ";
            }
            aux += "|";
            str += preencher + aux;
            aux = ((String) lista.get(i)).split("#")[3];
            preencher = "";
            for (int k = 0; k < (7 - aux.length()); k++) {
                preencher += " ";
            }
            aux += "|";
            str += preencher + aux;
            aux = (Double.parseDouble(((String) lista.get(i)).split("#")[3])) * ((Double.parseDouble(((String) lista.get(i)).split("#")[2]))) + "";
            aux += "0";
            preencher = "";
            for (int k = 0; k < (8 - aux.length()); k++) {
                preencher += " ";
            }
            str += preencher + aux + "\n";
        }
        impri += str;
        impri += "|------------------------------------------------------------|\n";
        impri += " Sub-Total: R$ " + dados.get("subTotal") + "\n";
        str = dados.get("desconto") + "";
        if (str.equals("")) {
            str = "0";
        }
        impri += " Desconto: " + str + " %\n";
        impri += " Total: R$ " + dados.get("valorTotal") + "\n";
        //exemplo: dataVencimento#valor#datapagamento#situacao#funcionarioRecebeu
        str = "";
        if (!((String) dados.get("avista")).equals("true")) {
            impri += " Entrada: R$ " + dados.get("valorEntrada") + "\n";
            impri += "|------------------------------------------------------------|\n";
            lista = (List) dados.get("parcelas");
            if ((lista.size()) < 10) {
                str = "0" + (lista.size());
            } else {
                str = lista.size() + "";
            }
            impri += " Quantidade de Parcelas: " + str + "\n";
            str = "";
            for (int i = 0; i < lista.size(); i++) {
                str += " Parcela No: ";
                if ((i + 1) < 10) {
                    str += "0" + (i + 1);
                } else {
                    str += (i + 1);
                }
                str += "  Valor: R$";
                aux = ((String) lista.get(i)).split("#")[1];
                if (aux.length() < 7) {
                    aux += " ";
                }
                str += aux;
                str += "  Venc. em: " + ((String) lista.get(i)).split("#")[0] + "\n";
            }
            impri += str;
            impri += "|------------------------------------------------------------|\n";
            impri += "\n\n Assinatura do Cliente:__________________________\n\n";
        }

        impri += "|============================================================|\n";
        return impri;
    }

    private void atualizaSoma() {
        double total = 0.0;
        modelo = (DefaultTableModel) jTableItensVenda.getModel();
        for (int i = 0; i < modelo.getRowCount(); i++) {
            total += Double.parseDouble(((String) modelo.getValueAt(i, 5)));
        }
        jTextFieldSubTotal.setText("R$ " + total + "0");
        jTextFieldTotal.setText("R$ " + total + "0");
        jTextFieldCodigoProduto.setText("");
        jTextFieldQtdePro.setText("");
        jTextFieldValorUni.setText("");
        jComboBoxProduto.removeAllItems();
        if (this.tipoVenda.equals("A PRAZO") && jComboBoxVezes.getSelectedIndex() != 0) {
            this.geraParcelas();
        }
        this.calculaValorFinal();
    }

    private void construtor(Sessao sessao) {
        jLabelLogin.setText(sessao.getLogin());
        this.sessao = new Sessao();
        this.sessao.setLogin(sessao.getLogin());
        this.sessao.setSenha(sessao.getSenha());
        timer1.start();
        this.preConstrutor();
        this.mudarVenda();
    }

    private void mudarVenda() {
        if (jComboBoxPagamento.getSelectedIndex() == 0) {
            this.habilitaPrazo(false);
            this.limpaTabelaParcela();
            jTextFieldDesc.setText("10");
        } else {
            this.habilitaPrazo(true);
            this.limpaTabelaParcela();
            jTextFieldDesc.setText("0");
            this.calculaValorFinal();
        }
        jTextFieldValorEntrada.setText("");
        jComboBoxVezes.setSelectedIndex(0);
        jCheckBoxEntrada.setSelected(false);
    }

    private void preConstrutor() {
        this.setLocationRelativeTo(null);
        this.setTitle(this.tipoVenda);
        //pesquisar o usuario e setar o seu nome e cod nos campos
        HashMap<String, Object> dados = new HashMap<String, Object>();
        dados.put("modelo", "funcionario");
        dados.put("operacao", "login");
        dados.put("login", jLabelLogin.getText());
        dados = controlador.recebeOperacao(dados);
        if (dados != null) {
            jTextFieldCodigoFuncionario.setText((String) dados.get("cod"));
            jComboBoxFuncionario.removeAllItems();
            jComboBoxFuncionario.addItem(dados.get("nome"));
            jComboBoxFuncionario.setSelectedIndex(0);
            jTextFieldCodigoCliente.requestFocus();
        }

        if (this.tipoVenda.equals("A VISTA")) {
            jComboBoxPagamento.setSelectedIndex(0);
            this.habilitaPrazo(false);
        } else {
            jComboBoxPagamento.setSelectedIndex(1);
            this.habilitaPrazo(true);
        }
        controlador = new Controlador();
        jTableItensVenda.getColumnModel().getColumn(0).setPreferredWidth(3);
        jTableItensVenda.getColumnModel().getColumn(1).setPreferredWidth(30);
        jTableItensVenda.getColumnModel().getColumn(2).setPreferredWidth(350);
        jTableItensVenda.getColumnModel().getColumn(3).setPreferredWidth(3);
        jTableItensVenda.getColumnModel().getColumn(4).setPreferredWidth(15);
        jTableItensVenda.getColumnModel().getColumn(5).setPreferredWidth(15);
        jTextFieldDataVenda.setText(data.diaMesAno().split(" ")[1]);
        jTextFieldHoraVenda.setText(data.horaMinSeg());
        jTableItensVenda.getTableHeader().setReorderingAllowed(false);
        jTableParcelas.getTableHeader().setReorderingAllowed(false);
        jTableItensVenda.getColumnModel().getColumn(0).setPreferredWidth(3);
        this.alinhaColunas();
        jTextFieldNoCupom.setText(this.setMaiorId());
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

    private void atualizaCaixa() {
        //incluir um caixa do dia se este ja' nao estiver...
        //considerando que um caixa comeca com 150,00 reais
        HashMap<String, Object> caixa = new HashMap<String, Object>();
        String date = jLabelData.getText().split(" ")[1];
        List entra = new ArrayList();
        caixa.put("data", date);
        caixa.put("modelo", "caixa");
        caixa.put("operacao", "busca");
        caixa.put("posicao", "data");
        caixa = controlador.recebeOperacao(caixa);
        if (caixa == null) {
            //inserir venda
            caixa = new HashMap<String, Object>();
            caixa.put("modelo", "caixa");
            caixa.put("operacao", "maiorId");
            caixa = controlador.recebeOperacao(caixa);
            if (caixa != null) {
                String cod = caixa.get("retorno") + "";
                caixa = new HashMap<String, Object>();
                caixa.put("data", date);
                caixa.put("modelo", "caixa");
                caixa.put("operacao", "inserir");
                caixa.put("saidas", "");
                caixa.put("cod", cod);
            }
        } else {
            List saidas;
            if (!caixa.get("saidas").equals("")) {
                saidas = (List) caixa.get("saidas");
            } else {
                saidas = null;
            }
            entra = (List) caixa.get("entradas");
            String dataAntigo = (String) caixa.get("data");
            String cod = (String) caixa.get("cod");
            caixa = new HashMap<String, Object>();
            caixa.put("modelo", "caixa");
            caixa.put("operacao", "alterar");
            caixa.put("cod", cod);
            caixa.put("data", dataAntigo);
            if (saidas == null) {
                caixa.put("saidas", "");
            } else {
                caixa.put("saidas", saidas);
            }
        }
        List aux = new ArrayList();
        for (int i = 0; i < entra.size(); i++) {
            aux.add(entra.get(i));
        }
        String add = jTextFieldValorCaixa.getText() + "#";
        if (this.tipoVenda.equals("A VISTA")) {
            add += "avista/" + jTextFieldNoCupom.getText() + "/" + jTextFieldCodigoCliente.getText() + "#";//recebimento avista
        } else {
            if (this.tipoVenda.equals("A PRAZO") && jCheckBoxEntrada.isSelected() && (!jTextFieldValorEntrada.getText().equals("ERRO"))) {
                //recebimento da entrada.
                add += "entrada/" + jTextFieldNoCupom.getText() + "/" + jTextFieldCodigoCliente.getText() + "#";
            } else {
                //nothing
            }
        }
        add += sessao.getLogin() + "#" + jLabelHora.getText();
        aux.add(add);//adiciona o recebimento

        caixa.put("entradas", aux);
        caixa = controlador.recebeOperacao(caixa);
        JOptionPane.showMessageDialog(null, caixa.get("retorno"));
        this.jDialogRecebimento.setVisible(false);
        //Exemplo entrada: valor#natureza#funcionario#horario
        //Exemplo saida: valor#natureza#funcionario#horario
        //Exemplo natureza: parcela/numeroParcela/codVenda/cliente
        //Exemplo natureza: avista/cliente (cliente ou nao)
        //Exemplo natureza: entrada/codVenda/cliente
    }

    private void limpaCaixa() {
        jTextFieldDinheiroCaixa.setText("");
        jTextFieldTrocoCaixa.setText("");
        jTextFieldValorCaixa.setText("");
        this.recebimento = false;
    }

    private void imprimirRecibo() {
        Impressao impressao = new Impressao();
        impressao.imprimir(this.getModeloImprimirRecibo());
    }

    private String getModeloImprimirRecibo() {
        String impri = "";
        impri += "|============================================================|\n";
        impri += " VANIA MODAS  RECIBO DE PAGAMENTO \n";
        impri += " Horario: " + jLabelHora.getText() + " Data: " + jLabelData.getText() + "\n";
        impri += "|------------------------------------------------------------|\n";
        impri += " Funcionario: " + jTextFieldCodigoFuncionario.getText() + "--";
        String nome = jComboBoxFuncionario.getSelectedItem() + "";
        if (nome.length() > 40) {
            impri += " ";
            for (int i = 0; i < 40; i++) {
                impri += nome.charAt(i);
            }
            impri += "\n ";
        } else {
            impri += " " + nome + "\n";
        }
        impri += " Cliente: " + jTextFieldCodigoCliente.getText() + "--";
        nome = jComboBoxCliente.getSelectedItem() + "";
        if (nome.length() > 45) {
            impri += " ";
            for (int i = 0; i < 45; i++) {
                impri += nome.charAt(i);
            }
            impri += "\n ";
        } else {
            impri += " " + nome + "\n";
        }
        impri += "|------------------------------------------------------------|\n";
        impri += " Cupom No: " + jTextFieldNoCupom.getText() + "  ";
        String valor = "";
        if (this.tipoVenda.equals("A VISTA")) {
            impri += " Pagamento a vista\n";
            impri += " Valor: " + jTextFieldSubTotal.getText().split(" ")[1] + " ";
            if (!jTextFieldDesc.getText().equals("0")) {
                impri += " Desconto: " + jTextFieldDesc.getText() + " %\n";
            }

            valor = jTextFieldTotal.getText().split(" ")[1];
            impri += " ValorRecebido: R$" + valor + "\n";
        } else {
            impri += " Pagamento da entrada\n";
            valor = jTextFieldValorEntrada.getText();
            impri += " Valor: " + valor + "\n";
        }
        Extenso number = new Extenso(Double.parseDouble(valor));
        nome = "(" + number.toString() + ")";
        if (nome.length() > 60) {
            int i = 0;
            while (i < nome.length()) {
                impri += " ";
                for (; (i < (i + 60) && (nome.length() < 60)); i++) {
                    impri += nome.charAt(i);
                }
                impri += "\n";
            }
        } else {
            impri += " " + nome + "\n";
        }
        impri += " Troco: R$" + jTextFieldTrocoCaixa.getText() + "\n";
        impri += "|------------------------------------------------------------|\n";
        impri += " " + new Random().nextInt(65000) + " VANIA MODAS ONDE VOCE ENCONTRA O SEU ESTILO\n";
        return impri + "|=====================================================1a VIA=|\n" +
                "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n" +
                impri + "|=====================================================2a VIA=|\n" +
                "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n";

    }
}
