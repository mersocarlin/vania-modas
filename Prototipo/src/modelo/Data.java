
package modelo;

/**
 *
 * @author Hemerson e Jefferson
 */
public class Data {
    private String dia;
    private String mes;
    private String ano;
    private String hora;
    private String minuto;
    private String segundo;

    public Data(){
        this.dia = "00";
        this.mes = "00";
        this.ano = "0000";
        this.hora = "00";
        this.minuto = "00";
        this.segundo = "00";
    }
    
    public Data(String d, String m, String a){
        this.dia = d;
        this.mes = m;
        this.ano = a;
        this.hora = null;
        this.minuto = null;
        this.segundo = null;
    }

    public Data(String dia, String mes, String ano, String hora, String minuto, String segundo) {
        this.dia = dia;
        this.mes = mes;
        this.ano = ano;
        this.hora = hora;
        this.minuto = minuto;
        this.segundo = segundo;
    }

    public Data(Data data){
        this.dia = data.getDia();
        this.mes = data.getMes();
        this.ano = data.getAno();
        this.hora = data.getHora();
        this.minuto = data.getMinuto();
        this.segundo = data.getSegundo();
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getMinuto() {
        return minuto;
    }

    public void setMinuto(String minuto) {
        this.minuto = minuto;
    }

    public String getSegundo() {
        return segundo;
    }

    public void setSegundo(String segundo) {
        this.segundo = segundo;
    }

    @Override
    public String toString() {
        return this.dia+"/"+this.mes+"/"+this.ano;
    }
}
