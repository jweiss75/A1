import scraping.*;
import Bd.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.io.FilenameFilter;

import java.sql.Connection;

// test de capacidad.

public class PHM {


    public static void main(String[] args) {

        // TODO Auto-generated method stub
        Navigate navigate = new Navigate();
        navigate.setRoute("XBRL_Files/");
        System.out.println("------------------------------->" + navigate.getRoute());
        try {
            // navigate.submittingForm("VIDRALA");
            navigate.printCompanies();
            System.out.println("------------------------------->");
            navigate.submittingForm("BANCO SANTANDER", navigate.getRoute());

            // unzipfile

            UnzipFile unzip = new UnzipFile();
            unzip.setRoute("XBRL_Files/");
            unzip.setFileZip("BANCO SANTANDER.zip");
            unzip.unzip();

            // go acreoos de directory of files a use xml for database 
            unzip.serialUnzip();

            serialBD();


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void serialBD() throws IOException {

        System.out.print("Iniciando generación de la base de datos");

        File dir = new File("XBRL_Files/");
        String[] ficheros = dir.list();
        DBManage inserciondb = new DBManage();
        for (String fichero : ficheros) {
            
            
            int j = fichero.lastIndexOf(".");
            System.out.println("encontrado..."+fichero+" con extensión " + fichero.substring(j + 1));
            
            
            if (j > 0) {
                String extension = fichero.substring(j + 1);
                if (extension.equals("xbrl") == true) {
                    
                    System.out.println("Se procede a incluir datos en la base de datos");
                    System.out.print("Procesando " + fichero);
                    XMLMonster monster = new XMLMonster();
                    
                    monster.setCon(inserciondb.conection());
                    monster.setSourceFile("XBRL_Files/" + fichero);
                    monster.setTagName("LegalNameValue");

                    try

                    {
                        monster.searchTags();
                        monster.setTagName("context");
                        XMLMummy mummy = new XMLMummy();
                        mummy.setListaElemento(monster.searchTags());
                        monster.setMatrizRefYear(mummy.anualidadList());
                        System.out.println(mummy.anualidadList());
                        monster.setMatrizRefYear(mummy.getRefYear());
                        monster.setTagName("BalanceConsolidado");
                        monster.searchTags();
                        monster.closeCon();

                    } catch (Exception e) {
                        
                        e.printStackTrace();
                    }

                }


            }
        }


    }

    class EvaluaExtension implements FilenameFilter {

        public boolean accept(File dir, String extension) {
            return dir.getName().endsWith(extension);
        }
    }
}
