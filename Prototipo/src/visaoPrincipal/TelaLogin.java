package visaoPrincipal;

/**
 *
 * @author Hemerson e Jefferson
 */
import visaoGerente.TelaPrincipalGer;
import controlador.Controlador;
import java.util.HashMap;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import utilitarios.Sessao;
import visaoFuncionario.TelaPrincipalFun;

public class TelaLogin extends javax.swing.JFrame {

    Controlador controlador = new Controlador();
    private UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();

    /** Creates new form TelaLogin */
    public TelaLogin() {
        initComponents();
        try {
            UIManager.setLookAndFeel(looks[3].getClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setLocationRelativeTo(null);
    }

    private void entra() {
        String login = "root";
        //pesquisa um funcionario pelo nome
        //root e senha root eh o administrador
        HashMap<String, Object> dados = new HashMap<String, Object>();
        Sessao sessao = new Sessao();
        if (sessao.validaLoginSenha(jTextFieldLogin.getText(), jPasswordFieldSenha.getText())) {
            JOptionPane.showMessageDialog(null, "Bem vindo(a) Administrador !\nSessao iniciada com privilegios admi" +
                                "nistrativos", "Software Vania Modas", JOptionPane.INFORMATION_MESSAGE);
            new TelaPrincipalGer(sessao).show();
            this.dispose();
        } else {
            dados.put("modelo", "funcionario");
            dados.put("operacao", "login");
            dados.put("login", jTextFieldLogin.getText());
            dados = controlador.recebeOperacao(dados);
            if (dados != null) {
                sessao.setLogin((String)dados.get("login"));
                sessao.setSenha((String)dados.get("senha"));
                if (sessao.validaLoginSenha(jTextFieldLogin.getText(), jPasswordFieldSenha.getText())) {
                    if(dados.get("gerente").equals("true")){
                        JOptionPane.showMessageDialog(null, "Bem vindo(a) "+dados.get("nome")+ " !\nSessao iniciada com privilegios admi" +
                                "nistrativos", "Software Vania Modas", JOptionPane.INFORMATION_MESSAGE);
                        new TelaPrincipalGer(sessao).show();
                        this.dispose();
                    }else{
                        JOptionPane.showMessageDialog(null, "Bem vindo(a) "+dados.get("nome")+" !\nSessao iniciada sem privilegios admi" +
                                "nistrativo", "Software Vania Modas", JOptionPane.INFORMATION_MESSAGE);
                        new TelaPrincipalFun(sessao).show();
                        this.dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Usuario e/ou Senha invalidos!", "Erro!", JOptionPane.ERROR_MESSAGE);
                    jTextFieldLogin.requestFocus();
                }
            }else{
                JOptionPane.showMessageDialog(null, "Usuario inexistente!", "Erro!", JOptionPane.ERROR_MESSAGE);
                jTextFieldLogin.requestFocus();
            }

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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextFieldLogin = new javax.swing.JTextField();
        jPasswordFieldSenha = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Area de Acesso");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel1.setText("Login:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel2.setText("Senha:");

        jTextFieldLogin.setFont(new java.awt.Font("Tahoma", 0, 14));
        jTextFieldLogin.setText("root");
        jTextFieldLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextFieldLoginActionPerformed(evt);
            }
        });
        jTextFieldLogin.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextFieldLoginFocusGained(evt);
            }
        });

        jPasswordFieldSenha.setFont(new java.awt.Font("Tahoma", 0, 14));
        jPasswordFieldSenha.setText("root");
        jPasswordFieldSenha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordFieldSenhaActionPerformed(evt);
            }
        });
        jPasswordFieldSenha.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jPasswordFieldSenhaFocusGained(evt);
            }
        });

        jButton1.setText("Entrar");
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jTextFieldLogin, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPasswordFieldSenha))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextFieldLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jPasswordFieldSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 0, 0));
        jLabel3.setText(" VANIA MODAS");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    this.entra();
}//GEN-LAST:event_jButton1ActionPerformed

private void jTextFieldLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldLoginActionPerformed
    jPasswordFieldSenha.requestFocus();
}//GEN-LAST:event_jTextFieldLoginActionPerformed

private void jPasswordFieldSenhaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordFieldSenhaActionPerformed
    this.entra();
}//GEN-LAST:event_jPasswordFieldSenhaActionPerformed

private void jTextFieldLoginFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextFieldLoginFocusGained
    jTextFieldLogin.setSelectionStart(0);
    jTextFieldLogin.setSelectionEnd(jTextFieldLogin.getText().length());
}//GEN-LAST:event_jTextFieldLoginFocusGained

private void jPasswordFieldSenhaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPasswordFieldSenhaFocusGained
    jPasswordFieldSenha.setSelectionStart(0);
    jPasswordFieldSenha.setSelectionEnd(jPasswordFieldSenha.getText().length());
}//GEN-LAST:event_jPasswordFieldSenhaFocusGained

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new TelaLogin().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordFieldSenha;
    private javax.swing.JTextField jTextFieldLogin;
    // End of variables declaration//GEN-END:variables
}
