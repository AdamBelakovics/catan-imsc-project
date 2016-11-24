package ux.graphics;

import java.awt.Color;
import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.*;

import org.w3c.dom.*;

import controller.player.Resource;

public class TextureXMLReader {
	/**
	 * Parses an XML file containing Resource - Integer pairs, where the Integer represents
	 * the color of the tile 
	 * @param path the path to the XML file
	 * @return HashMap of Resource - Integer pairs containing the colors
	 * @author Kiss Lorinc
	 */
	public static HashMap<Resource, Integer> readXML(String path) {
		HashMap<Resource,Integer> textureMap=new HashMap();
		try {
			Document textureXML=DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(path));
			NodeList nodes=textureXML.getDocumentElement().getElementsByTagName("Texture");
			for (int i=0;i<nodes.getLength();i++) {
				textureMap.put(
						Resource.valueOf(nodes.item(i).getAttributes().getNamedItem("r").getNodeValue()), 
						Integer.decode(nodes.item(i).getAttributes().getNamedItem("c").getNodeValue()));
			}
		} catch (Exception e) {
			System.out.println("Problem parsing texture XML");
			e.printStackTrace();
		}
		return textureMap;
	}
}
