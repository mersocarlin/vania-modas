
package utilitarios;

/**
 *
 * @author Hemerson e Jefferson
 */
import java.util.Date;

public class DataSistema {

    private static DataSistema instancia = null;
    String mes, dia, ano, dia_semana, show;
    String horas, minutos, segundos;

    /** Creates a new instance of data */
    public DataSistema() {
        this.mes = this.dia = this.ano = "";
        this.dia_semana = this.show = "";
        this.horas = this.minutos = this.segundos = "";
    }

    /**
     * Implementacao do padrao de projeto Singleton
     * @return
     */
    public static DataSistema getInstance() {
        if (instancia == null) {
            instancia = new DataSistema();
        }
        return instancia;
    }

    public String diaMesAno(){
        Date data = new Date();
        dia = ""+data.getDate();         //retorna valores de 0 a 6
        mes = ""+(1 + data.getMonth());  //retorna valores de 0 a 11        
        ano = ""+(1900 + data.getYear());   
        horas = ""+data.getHours();
        minutos = ""+data.getMinutes();
        segundos = ""+data.getSeconds();
        
        switch(data.getDay())
        { //formata os dias da semana
            case 0: dia_semana = "DOM"; break;
            case 1: dia_semana = "SEG"; break;
            case 2: dia_semana = "TER"; break;
            case 3: dia_semana = "QUA"; break;
            case 4: dia_semana = "QUI"; break;
            case 5: dia_semana = "SEX"; break;
            case 6: dia_semana = "SAB"; break;
        }             
        
        //formata dia
        if(data.getDate() < 10){
            dia = "0"+data.getDate();
        }
        //formata mes
        int a = Integer.parseInt(mes);
        if(a < 10){
            mes = "0"+mes;
        }
        show = dia_semana + ": " + dia + "/" + mes + "/" + ano;                           
        return show;
    }
    
    public String horaMinSeg(){
        Date data = new Date();
        horas = ""+data.getHours();
        minutos = ""+data.getMinutes();
        segundos = ""+data.getSeconds();        
       
        //formata horas, minutos e segundos
        if(data.getHours() < 10){
            horas = "0"+data.getHours();
        }
        if(data.getMinutes() < 10){
            minutos = "0"+data.getMinutes();
        }
        if(data.getSeconds() < 10){
            segundos = "0"+data.getSeconds();
        }
        show = horas + ":" + minutos + ":" + segundos; 
        return show;
    }

    public boolean antes(String data){
        String dataSistema = this.diaMesAno().split(" ")[1];
        Date data1 = new Date(Integer.parseInt(dataSistema.split("/")[2]), Integer.parseInt(dataSistema.split("/")[1]), Integer.parseInt(dataSistema.split("/")[0]));
        Date data2 = new Date(Integer.parseInt(data.split("/")[2]), Integer.parseInt(data.split("/")[1]), Integer.parseInt(data.split("/")[0]));

        if(data1.before(data2)){
            return true;
        }else{
            return false;
        }
    }
}