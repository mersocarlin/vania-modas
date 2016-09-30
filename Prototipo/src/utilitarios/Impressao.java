package utilitarios;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JOptionPane;

/**
 *
 * @author Hemerson e Jefferson
 */
public class Impressao {

    private static PrintService impressora;

    public Impressao() {
        this.detectaImpressoras();
    }

    public void imprimir(String imprimir) {
        try {
            InputStream prin = new ByteArrayInputStream(imprimir.getBytes());
            DocFlavor docFlavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
            Doc documentoTexto = new SimpleDoc(prin, docFlavor, null);
            //PrintService impressora2 = PrintServiceLookup.lookupDefaultPrintService(); // pega a //impressora padrao
            PrintRequestAttributeSet printerAttributes = new HashPrintRequestAttributeSet();
            printerAttributes.add(new JobName("Impressao-VANIAMODAS", null));
            printerAttributes.add(OrientationRequested.PORTRAIT);
            printerAttributes.add(MediaSizeName.ISO_A4); // informa o tipo de folha
            DocPrintJob printJob = impressora.createPrintJob();
            printJob.print(documentoTexto, (PrintRequestAttributeSet) printerAttributes); //tenta imprimir
            prin.close();
        } catch (Exception e) {
            System.out.println("Sem impressora");
            System.out.println(imprimir);
        }
    }

    private void detectaImpressoras() {

        try {

            DocFlavor df = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
            PrintService[] ps = PrintServiceLookup.lookupPrintServices(df, null);
            impressora = ps[0];
            /*for (PrintService p : ps) {

                System.out.println("Impressora encontrada: " + p.getName());

                if ( p.getName().contains("series")) {
                    System.out.println("Impressora Selecionada: " + p.getName());
                    impressora = p;
                    return;

                }

            }
            if(impressora == null){
                JOptionPane.showMessageDialog(null, "Impressora nao encontrada.\nInstale uma impressora" +
                        "para conseguir usar este recurso", "Software Vania Modas", JOptionPane.INFORMATION_MESSAGE);
            }*/

        } catch (Exception e) {
            //e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Impressora nao encontrada.\nInstale uma impressora" +
                        "para conseguir usar este recurso", "Software Vania Modas", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
