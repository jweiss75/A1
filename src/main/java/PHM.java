import scraping.*;
import Bd.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.io.FilenameFilter;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.FileReader;


import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
// test de capacidad.

public class PHM {


    public static void main(String[] args) {

        // TODO Auto-generated method stub
        Navigate navigate = new Navigate();
        navigate.setRoute("XBRL_Files/");
        System.out.println("------------------------------->" + navigate.getRoute());
        try {
            // navigate.submittingForm("VIDRALA");
            // navigate.printCompanies();
            // System.out.println("------------------------------->");
            UnzipFile unzip = new UnzipFile();
            unzip.setRoute("XBRL_Files/");
            
            List<String> listacotizadas= readData(); 
            
            for (String cotizada:listacotizadas){
               System.out.println(cotizada);   
               navigate.submittingForm(cotizada, navigate.getRoute());
               unzip.setFileZip(cotizada+".zip");
               serialBD();  
            }
            
            
            
           /* navigate.submittingForm("FAES", navigate.getRoute());
            unzip.setFileZip("FAES.zip");
            unzip.unzip();
            navigate.submittingForm("PHARMA MAR", navigate.getRoute());
            unzip.setFileZip("PHARMA MAR.zip");
            unzip.unzip();     
            navigate.submittingForm("ALMIRALL", navigate.getRoute());
            unzip.setFileZip("ALMIRALL.zip");
            unzip.unzip();

            // go acreoos de directory of files a use xml for database 
            unzip.serialUnzip();
           
            serialBD();*/
            
            
            


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void serialBD() throws IOException {
try

                    {
        System.out.print("Iniciando generación de la base de datos");

        File dir = new File("XBRL_Files/");
        String[] ficheros = dir.list();
        DBManage inserciondb = new DBManage();
        Connection con = inserciondb.conection();
        XMLHorror monster = new XMLHorror();
        for (String fichero : ficheros) {
            
            
            int j = fichero.lastIndexOf(".");
            System.out.println("encontrado..."+fichero+" con extensión " + fichero.substring(j + 1));
            
            
            if (j > 0) {
                String extension = fichero.substring(j + 1);
                if (extension.equals("xbrl") == true) {
                    
                    System.out.println("Se procede a incluir datos en la base de datos");
                    System.out.print("Procesando " + fichero);
                    
                    
                    
                    monster.setSourceFile("XBRL_Files/" + fichero);
                    monster.setTagName("LegalNameValue");
                    monster.setConnection();
                    NodeList documento = monster.nodosDocumeto();
                    monster.refyear = monster.anualidadList(documento);
                    monster.entidad= monster.BuscaEntidad(documento, "Entity" );
                    monster.searchTags(documento, "BalanceConsolidado" );
                    monster.searchTags(documento, "CuentaPerdidasGananciasConsolidada" );
                    monster.closeCon();
                    
                    
                    

                }


            }
            
           con.close();
        }
} catch (Exception e) {
                        
                        e.printStackTrace();
                    }

    }

    class EvaluaExtension implements FilenameFilter {

        public boolean accept(File dir, String extension) {
            return dir.getName().endsWith(extension);
        }
    }


/////////////////////////// LISTA DE COTIZADAS /////////////////////////////

public static List<String> readData() throws IOException { 
    int count = 0;
    String file = "ListedCompanies/IBEX.csv";
    List<String> content = new ArrayList<>();
    try(BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line = "";
        while ((line = br.readLine()) != null) {
            content.add(line);
        }
    } catch (FileNotFoundException e) {
      //Some error logging
    }
    return content;
}



}
