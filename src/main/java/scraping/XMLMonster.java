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

public class XMLMonster {

    String tagname    = "*";
    String sourcefile = "XBRL_Files/2016025878.xbrl";

    public static void main(String[] args) throws Exception {

        XMLMonster monster = new XMLMonster();
        monster.setTagName("ipp");
        monster.searchTags();


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
                NamedNodeMap schema = element.getAttributes("contextRef");
        
                
                if (nombre.toLowerCase().indexOf(this.tagname.toLowerCase()) > -1 || this.tagname == "*") {

                    System.out.println("Found element: " + schema);
                    System.out.println("Found element: " + element.getNodeName());
                    listadetags.add(element);

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
