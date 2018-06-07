package scraping;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XMLMonster {
	
	public static void main(String[] args) throws Exception {
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		DocumentBuilder db = dbf.newDocumentBuilder();
		File file = new File("XBRL_Files/2016025878.xbrl");
		FileInputStream fis = new FileInputStream(file);
		Document doc = db.parse(fis);
		 
		NodeList entries = doc.getElementsByTagName("*");
		
		for (int i=0; i<entries.getLength(); i++) {
		    Element element = (Element) entries.item(i);
		    System.out.println("Found element: " + element.getNodeName());
		}
		
	}

}