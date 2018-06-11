package scraping;

import java.util.List;
import java.util.ArrayList;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;

// El sueño de la razón produce monstruos

public class XMLMonster {

    String tagname    = "*";
    String sourcefile = "XBRL_Files/2016025878.xbrl";

    public static void main(String[] args) throws Exception {
        System.out.println("-------------------El sueño de la razón produce monstruos-----------------------");
        XMLMonster monster = new XMLMonster();
        monster.setTagName("ipp-gen:BalanceIndividual");
        monster.searchTags();
        monster.setTagName("ipp-gen:CuentaPerdidasGananciasIndividual");
        monster.searchTags();
        monster.setTagName("ipp-gen:BalanceConsolidado");
        monster.searchTags();
        monster.setTagName("ipp-gen:CuentaPerdidasGananciasConsolidado");
        monster.searchTags();
        
        
    }

    
    public static NodeList  getAllChildren(Element e) {
	       
	      NodeList allElements = e.getChildNodes();
	      for (int i = 0; i < allElements.getLength(); i++) {
	             if  (allElements.item(i).getTextContent().trim().length()!=0) {
                 System.out.print(" --->Nodo: "+allElements.item(i).getNodeName());
                 System.out.print(" --->ref: "+allElements.item(i).getAttributes().getNamedItem("contextRef").getNodeValue());
                 System.out.println(" ------->Valor: "+allElements.item(i).getTextContent().trim());
                }
            }

	      
	      
	       
	       return allElements; 
      }
    
    
     public static  String getContextRef(Element e) {
	       
	       String href = e.getAttribute("contextRef");
	       
	       return href; 
      }


    
    public List<Element> searchTags() throws Exception {
        List<Element> listadetags = new ArrayList<Element>();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            DocumentBuilder db = dbf.newDocumentBuilder();
            File file = new File(this.sourcefile);
            FileInputStream fis = new FileInputStream(file);
            Document doc = db.parse(fis);

            NodeList entries = doc.getElementsByTagName("*");

            for (int i = 0; i < entries.getLength(); i++) {
                Element element = (Element)entries.item(i);
                
                String nombre = element.getNodeName();
                NamedNodeMap schema = element.getAttributes();
        
                
                if (nombre.toLowerCase().indexOf(this.tagname.toLowerCase()) > -1 || this.tagname == "*") {
                    System.out.println("ELEMENTO: " + element.getTagName());
                    System.out.println("TIPO ESQUEMA: " + element.getSchemaTypeInfo());
                    System.out.println("ATTRIBUTES: " + schema);
                    System.out.println("URI: " + element.getNodeName());
                    System.out.println("ElementRef: " + getContextRef(element));
                    // Nodes 
                    
                    NodeList nodos =  getAllChildren(element);
                    
                    
                    
                    
                    listadetags.add(element);
                    System.out.println("____________________________");
                }


            }
        } catch (Exception e) {

            System.out.println("MONSTER genero error:" + e.getMessage());
        }

        return listadetags;

    }

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


}
