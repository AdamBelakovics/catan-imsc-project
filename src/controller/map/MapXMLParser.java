package controller.map;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import controller.player.Resource;



/**
 * @author matesebi
 * class for parsing playmaps from XML files an example given in catan_base_map.xml
 */
public class MapXMLParser {
	
	public static void readCatanMap(File f, Table board){
		
		ArrayList<Hex> fields = board.getFields();
		ArrayList<Hex> toberemoved = new ArrayList<>();
		for(int i = 0; i < fields.size(); i++){
			if(fields.get(i).getNeighbouringVertices().size() < 6){
				toberemoved.add(fields.get(i));
			}
		}
		fields.removeAll(toberemoved);
		for(Hex h : toberemoved){
			h.setResource(null);
			h.setProsperity(0);
		}
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(f);
			NodeList docelements = doc.getElementsByTagName("hex");
			for(int i = 0; i < docelements.getLength(); i++){
				fields.get(i).setResource(Resource.valueOf(docelements.item(i).getAttributes().getNamedItem("res").getNodeValue()));
				fields.get(i).setProsperity(Integer.parseInt(docelements.item(i).getAttributes().getNamedItem("prosp").getNodeValue()));
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
