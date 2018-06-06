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
		  File xmlFile = new File("XBRL_Files/2016025878.xbrl");
          
		  try {

			Document document = (Document) builder.build(xmlFile);
			Element rootNode = document.getRootElement();
			// System.out.println(rootNode.getName()+"!!!!!");
			System.out.println(showMeAttributes(rootNode));
			// System.out.println(rootNode.getAttributes()+"!!!!!");
			List<Element> list = rootNode.getChildren();
            System.out.println("SE HAN ENCONTRADO:"+list.size()+" nodos...");
            
			
            System.out.println(list.size()+"!!!!!");
			for (int i = 0; i < list.size(); i++) {
                
			   Element node = (Element) list.get(i);
			   
			   // System.out.println("First Name : |" + node.getName()+"|");
			   System.out.println("Nodo : | " + node.getname());
			   listaAtributos(node)
			   // Sacamos los atteributos 
			   
			   
			   
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
	         
	        System.out.println("--"+atributos.get(i).toString());
	        
	    }
	    
	    
	    return a;
	}	
	
	static public String atributos(String nombre, Element node) {
	List<Attribute> atributos = node.getAttributes();
	    String valor=""; 	   
			     for (int j=0; j < atributos.size(); j++){
			         //System.out.println("--Atributo: " + atributos.get(j).getName());
			           if (atributos.get(j).getName()==nombre){
			          valor = atributos.get(j).getValue();
			              
			         System.out.println("--Atributo: " + atributos.get(j).getName()+ "=" + atributos.get(j).getValue());
			          }
			     }
            return valor;
        }
        
        
        public static List<Attribute> listaAtributos(Element node) {
	List<Attribute> atributos = node.getAttributes();
	                     System.out.println(".. NOTE: "+""+ node.getName()+ " has de next attributes");
			     for (int j=0; j < atributos.size(); j++){
			         System.out.println("......."+atributos.get(j).getName());
			     }
			     return atributos;
            
        }
}
