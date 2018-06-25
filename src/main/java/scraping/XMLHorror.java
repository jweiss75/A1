package scraping;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.ArrayList;

import Bd.DBManage;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class XMLHorror {
    public String     entidad;
    public String     anualidad;
    String            tagname       = "*";
    String            sourcefile    = "XBRL_Files/2012030315.xbrl";
    List<Vector>      matrizrefyear = null;
    public Connection con           = null;
    public List<Vector> refyear     =null;

    public static void main(String[] args) throws Exception {
        
       // Instancia
        System.out.println("Instanciando Horror");
        XMLHorror monster = new XMLHorror();
        monster.setConnection();
        NodeList documento = monster.nodosDocumeto();
        monster.refyear = monster.anualidadList(documento);
        monster.entidad= monster.BuscaEntidad(documento, "Entity" );
        monster.searchTags(documento, "BalanceConsolidado" );
        monster.searchTags(documento, "CuentaPerdidasGananciasConsolidada" );
        monster.closeCon();
    }


    public String BuscaEntidad (NodeList entries, String tagname) throws Exception {
        
          String buscaentidad="";
          List<Element> listadetags = new ArrayList<Element>();
          System.out.println("longitud del nodo:" + entries.getLength());
        
         try {
          
            for (int i = 0; i < entries.getLength(); i++) {
                
                  Element element = (Element)entries.item(i);
                  String nombre = element.getNodeName();
                  if (element.getChildNodes().getLength()>1){  
                     // System.out.println(element.getNodeName()+":"+ element.getChildNodes().getLength());
                  }
                
                if (nombre.toLowerCase().indexOf(tagname.toLowerCase()) > -1 || tagname == "*") {
                    
                    
                    
                    for (int y=1; y<element.getChildNodes().getLength()-1;y++)   {
                         Node nodo = element.getChildNodes().item(y);
                         if(nodo.getNodeName().toLowerCase().indexOf("identifier")>-1){
                               buscaentidad= nodo.getTextContent();
                         
                        }
                     }       

                    
                }
                
                
                
                
                
            }
                  
         }
         catch (Exception e){ buscaentidad="error";}
         System.out.println("Empresa: "+buscaentidad);
         return buscaentidad; 
        
         
        
}

    public List<Element> searchTags(NodeList entries, String tagname) throws Exception {

        List<Element> listadetags = new ArrayList<Element>();
        System.out.println("longitud del nodo:" + entries.getLength());
        try {
            for (int i = 0; i < entries.getLength(); i++) {
                Element element = (Element)entries.item(i);
               
               if (element.getChildNodes().getLength()>1){  
                // System.out.println(element.getNodeName()+":"+ element.getChildNodes().getLength());
            }
                String nombre = element.getNodeName();
                NamedNodeMap schema = element.getAttributes();
                if (nombre.toLowerCase().indexOf(tagname.toLowerCase()) > -1 || tagname == "*") {
                    String nombrenodoprincipal=element.getTagName();
                    System.out.print("| ELEMENTO    : " + element.getTagName());
                    System.out.print("| TIPO ESQUEMA: " + element.getSchemaTypeInfo());
                    System.out.print("| ATTRIBUTES  : " + schema);
                    System.out.print("| URI         : " + element.getNodeName());
                   // System.out.print("| ElementRef  : " + getContextRef(element));
                    // System.out.println("| Valor  : " + element.getTextContent().trim());
                    // if (nombre.toLowerCase().indexOf("legalnamevalue") > -1) {
                    //    this.entidad = element.getTextContent().trim();
                    // }
                    // if (element.hasChildNodes() == true) {
                        // System.out.println("|_ Tiene nodos________________________________________________" + element);
                        //stadetags.add(element);
                        // System.out.println("_______________________________________________________________");
                        // }
                        
                     for (int y=1; y<element.getChildNodes().getLength()-1;y++)   {
                         Node nodo = element.getChildNodes().item(y);
                         if(nodo.getNodeName().trim().length()>5){
                         String node = nodo.getNodeName();
                         String value= nodo.getTextContent();
                         String date = matchYearRef(nodo.getAttributes().getNamedItem("contextRef").toString());
                         
                         
                         
                         
                    
                    System.out.println(" Intentando insertar dtos.... " + this.con + "|" + this.entidad + "|" + nombre + "|" +node + "|" +value +"|" + date);
                    this.insercion(this.entidad, nombre, node, value, date);
                    
                         
                         
                        }

                     }
                     
                        
                }

            }
        } catch (Exception e) {

            System.out.println("MONSTER  ERROR:" + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("LISTA De TAGS is" + listadetags.size());
        return listadetags;

    }



/////////////////////////////////////////////////////////// CUBO DE ANUALIDAD ///////////////////////////////

    public String matchYearRef(String ref) {
        
        System.out.println("* this is matchYearRef");
        String year = "2018";
        

       System.out.println("//" + this.refyear.size());
      
        for (Vector referencia:this.refyear) {
            System.out.println( referencia.get(0) + "&&&&&&&" + ref);
            if (ref.toString().equals(referencia.get(0).toString())) {
                year = referencia.get(2).toString().substring(1,4);
                // System.out.println("&&&&&&&&&&&&&&&&&&&&&&" + referencia.get(2) + "&&&&&&&" + year);
                System.out.println(ref+"-----"+referencia.get(0).toString());
            }
         
        }

        
        return year;

    }


    public List<Vector> anualidadList(NodeList entries) throws Exception {

        List<Element> listadetags = new ArrayList<Element>();
        List<Vector> lista1 = new ArrayList<Vector>(); // Aqui guardaremos el array.
        
    try {
          
            for (int i = 0; i < entries.getLength(); i++) {
                
                  Element element = (Element)entries.item(i);
                 
                  String nombre = element.getNodeName();
                  if (element.getChildNodes().getLength()>1){  
                     // System.out.println(element.getNodeName()+":"+ element.getChildNodes().getLength());
                  }
                   
                if (nombre.toLowerCase().indexOf("context") > -1 &&  element.getAttributes().getNamedItem("id").toString().length()>5) {
                    
                    
                    // System.out.println(element.getAttributes().getNamedItem("id"));
                    listadetags.add(element);
                }
              
            }
        
         }
         catch (Exception e){ System.out.println(e.getMessage());}
        
        

        // Search the contexts and find their references.
       
        List<Element> listaElementosContexto = listadetags;
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!" +  listaElementosContexto.size());
        
        for (Element contexto : listaElementosContexto) {
            // find the context regerence.
            System.out.println(contexto.getAttributes().getNamedItem("id"));
            String id = contexto.getAttributes().getNamedItem("id").toString();
            System.out.println("******************************");
            System.out.println("Contexto con referencia: " + id);
            System.out.println("******************************");
            
            NodeList contextincludedtags = contexto.getChildNodes();
        
            //for(int m=0; m<contexto.getChildNodes().getLength() ;m++){
    
            //     contextincludedtags.addChild(contexto.getChildNodes().item(m));
    
            //}
            
            
          

            // NodeList contextincludedtags = monstruito.getAllChildren(contexto);
            for (int i = 0; i < contextincludedtags.getLength(); i++) {

                // We look for the <xrbl:period tag> but the prefic coul be different
                String str2 = "period";
                System.out.println(contextincludedtags.item(i).getNodeName().toLowerCase().contains(str2.toLowerCase()));

                if (contextincludedtags.item(i).getNodeName().toLowerCase().contains(str2.toLowerCase()) == true) {
                    NodeList nodoPerido = contextincludedtags.item(i).getChildNodes();
                    System.out.println("Period found: " + contextincludedtags.item(i).getNodeName());
                    for (int j = 0; j < nodoPerido.getLength(); j++) {
                        String valorperiodo = nodoPerido.item(j).getNodeName() + ":" + nodoPerido.item(j).getTextContent();
                        // System.out.println(nodoPerido.item(j).getNodeName()+":"+nodoPerido.item(j).getTextContent());

                        if (valorperiodo.length()>20){
                        Vector vec = new Vector();
                        vec.add(id);
                        vec.add(contextincludedtags.item(i).getNodeName());
                        vec.add(valorperiodo);
                        lista1.add(vec);
                    }


                    }

                }


            }

        }

        System.out.println(lista1);
        // this.setRefYear(lista1);
        return lista1;

    }











    /////////////////////////////////// LECTURA DEL DOCUMENTO /////////////////////////////

    public NodeList nodosDocumeto() throws Exception {

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            DocumentBuilder db = dbf.newDocumentBuilder();
            File file = new File(this.sourcefile);
            FileInputStream fis = new FileInputStream(file);
            Document doc = db.parse(fis);
            NodeList entries = doc.getElementsByTagName("*");
            return entries;
        } catch (Exception e) {

            System.out.println("MONSTER  ERROR:" + e.getMessage());
            e.printStackTrace();
            return null;
        }


    }



    ////////////////////////////////// INSERCIONES DE LA BASE DE DATOS ///////////////////////	
    
    public void insercion(String empresa, String estado, String parametro, String valor, String fecha) {
        System.out.println("::::::::::::::::::::::::::::INSERTANDO DATOS::::::::::::::::::");
        try {

            /*con1.setAutoCommit(false);
            Statement st = con1.createStatement();
            String query = "Insert into monster(empresa, parametro, valor, fecha) values('u','3','1','2');"; 
            // note that i'm leaving "date_created" out of this insert statement
            st.executeUpdate(query);
            con1.commit();
              // con1.close();*/

            String query = "Insert into monster(empresa, estado, parametro, valor, fecha) values (?,?,?,?,?);";
            // System.out.println(empresa + estado + parametro + valor + fecha);
            // System.out.println(query);
            PreparedStatement preparedStmt = this.con.prepareStatement(query);
            preparedStmt.setString(1, empresa);
            preparedStmt.setString(2, estado);
            preparedStmt.setString(3, parametro);
            preparedStmt.setString(4, valor);
            preparedStmt.setString(5, fecha);
            preparedStmt.executeUpdate();
            // System.out.println(empresa + estado + parametro + valor + fecha);
            // System.out.println(preparedStmt);


        } catch (SQLException e) {
            System.err.println("Got an exception!");
            System.err.println(e.getMessage());
            e.printStackTrace();
            e.getLocalizedMessage();
        }


        // execute the preparedstatement


    }

    ////////////////////////////////// CONEXIONES A LA BASE DE DATOS ////////////////////////	


    public void closeCon() {
        try {
            this.con.close();
            System.out.println("Closing the connection of this Monster....");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            e.getMessage();
        }

    }

    public void setConnection() {
        Connection conection = null;
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            System.out.println("Experando la conexión con la base de datos...");

            //conection = DriverManager.getConnection ("jdbc:mysql://sql7.freemysqlhosting.net:3306/sql7243941?characterEncoding=utf-8","sql7243941", "npbF5u7qUJ");

            conection =
                        DriverManager.getConnection("jdbc:mysql://db4free.net:3306/monster?useSSL=false&characterEncoding=Latin1",
                                                    "monster", "electroforesis");

            System.out.println("Conexión Correcta");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.err.println("NO PUDE CONECTAR!");
            System.err.println(e.getMessage());

        }

        this.con = conection;
    }


    ////////////////////////// SETTERS Y GETTERS /////////////////////////////	


    public String getTagName() {
        return tagname;
    }

    public void setTagName(String tagname) {
        this.tagname = tagname;
    }

    public String getSourceFile() {
        return sourcefile;
    }

    public void setSourceFile(String sourcefile) {
        this.sourcefile = sourcefile;
    }

    public List<Vector> getMatrizRefYear() {
        return matrizrefyear;
    }

    public void setMatrizRefYear(List<Vector> matrizrefyear) {
        this.matrizrefyear = matrizrefyear;
    }

    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        System.out.println("Opening BD Connection por this Monster");
        this.con = con;

    }

}
