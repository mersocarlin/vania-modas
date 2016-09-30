package utilitarios;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Hemerson e Jefferson
 */
public class Sessao {

    String login;
    String senha;
    List letras = new ArrayList();

    public Sessao() {
        login = "root";
        senha = "root";
        letras.add("a");
        letras.add("b");
        letras.add("c");
        letras.add("d");
        letras.add("e");
        letras.add("f");
        letras.add("g");
        letras.add("h");
        letras.add("i");
        letras.add("j");
        letras.add("k");
        letras.add("l");
        letras.add("m");
        letras.add("n");
        letras.add("o");
        letras.add("p");
        letras.add("q");
        letras.add("r");
        letras.add("s");
        letras.add("t");
        letras.add("u");
        letras.add("v");
        letras.add("x");
        letras.add("y");
        letras.add("w");
        letras.add("z");
        letras.add("A");
        letras.add("B");
        letras.add("C");
        letras.add("D");
        letras.add("E");
        letras.add("F");
        letras.add("G");
        letras.add("H");
        letras.add("I");
        letras.add("J");
        letras.add("K");
        letras.add("L");
        letras.add("M");
        letras.add("N");
        letras.add("O");
        letras.add("P");
        letras.add("Q");
        letras.add("R");
        letras.add("S");
        letras.add("T");
        letras.add("U");
        letras.add("V");
        letras.add("X");
        letras.add("Y");
        letras.add("W");
        letras.add("Z");
        letras.add("0");
        letras.add("1");
        letras.add("2");
        letras.add("3");
        letras.add("4");
        letras.add("5");
        letras.add("6");
        letras.add("7");
        letras.add("8");
        letras.add("9");

    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenhaCriptografada() {
        return criptografar();

    }
    public String getSenha(){
        return senha;
    }

    public void setSenha(String senha) {
        if(senha.contains("#")){
            this.senha = desincriptografar(senha);
        }else{
            this.senha = senha;
        }
    }

    private String criptografar() {
        Random randomico = new Random();
        int deslocamento = randomico.nextInt(this.letras.size());
        List seguranca = new ArrayList();
        for (int i = 0; i < this.letras.size(); i++) {
            if (i + deslocamento < this.letras.size()) {
                seguranca.add(this.letras.get(i + deslocamento));
            } else {
                seguranca.add(this.letras.get((i + deslocamento) - this.letras.size()));
            }
        }
        List criptografia = new ArrayList();
        criptografia.add(deslocamento);
        String[] character = senha.split("");
        for (int k = 0; k < character.length; k++) {
            for (int i = 0; i < seguranca.size(); i++) {
                if (seguranca.get(i).equals(character[k])) {
                    criptografia.add(i);
                }
            }
        }
        String saida = "";
        for (int i = 0; i < criptografia.size(); i++) {
            saida += criptografia.get(i) + "#";

        }
        return saida;
    }

    private String desincriptografar(String senha) {

        String[] codigo = senha.split("#");
        int deslocamento = Integer.parseInt(codigo[0]);
        List seguranca = new ArrayList();
        for (int i = 0; i < this.letras.size(); i++) {
            if (i + deslocamento < this.letras.size()) {
                seguranca.add(this.letras.get(i + deslocamento));
            } else {
                seguranca.add(this.letras.get((i + deslocamento) - this.letras.size()));
            }
        }
        String saida = "";
        for (int i = 1; i < codigo.length; i++) {
            saida += "" + seguranca.get(Integer.parseInt(codigo[i]));

        }
        return saida;
    }

    public boolean validaLoginSenha(String login, String senha) {
        if (login.equals(this.login) && senha.equals(this.senha)) {
            return true;
        }
        return false;
    }
}
