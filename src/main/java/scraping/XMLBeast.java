package scraping;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Attribute;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class XMLBeast {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("Comienza la BESTIA a leer!!!!!");
		
		SAXBuilder builder = new SAXBuilder();
		  File xmlFile = new File("XBRL_Files/2018027459.xbrl");
          
		  try {

			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			System.out.println(rootNode.getName()+"!!!!!");
			System.out.println(showMeAttributes(rootNode));
			System.out.println(rootNode.getAttributes()+"!!!!!");
			List list = rootNode.getChildren("xbrli:schemaRef");
            System.out.println(list.size()+"!!!!!");
			for (int i = 0; i < list.size(); i++) {
                
			   Element node = (Element) list.get(i);
			   System.out.println("First Name : " + node.getValue());
			   /*System.out.println("First Name : " +             node.getChildText("firstname"));
			   System.out.println("Last Name : " + node.getChildText("lastname"));
			   System.out.println("Nick Name : " + node.getChildText("nickname"));
			   System.out.println("Salary : " + node.getChildText("salary"));*/

			}

		  } catch (IOException io) {
			System.out.println(io.getMessage());
		  } catch (JDOMException jdomex) {
			System.out.println(jdomex.getMessage());
		  }
		}
		
	public static String showMeAttributes(Element nodo){
	    String a ="ddddddddd";
	    List<Attribute> atributos = nodo.getAttributes();
	    for (int i=0;i<atributos.size();i++) {
	        
	         System.out.println(atributos.item(i).getName());
	        
	    }
	    
	    
	    return a;
	}	

}
