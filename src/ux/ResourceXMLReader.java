package ux;

import java.awt.Color;
import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.*;

import org.w3c.dom.*;

import controller.player.Building;
import controller.player.Resource;

public class ResourceXMLReader {
	/**
	 * Parses an XML file containing Resource - Integer pairs, where the Integer represents
	 * the color of the tile 
	 * @param path the path to the XML file
	 * @return HashMap of Resource - Integer pairs containing the colors
	 * @author Kiss Lorinc
	 */
	public static HashMap<Resource, Color> readTextureXML(String path) {
		HashMap<Resource,Color> textureMap=new HashMap();
		try {
			Document textureXML=DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(path));
			NodeList nodes=textureXML.getDocumentElement().getElementsByTagName("Texture");
			for (int i=0;i<nodes.getLength();i++) {
				textureMap.put(
						Resource.valueOf(nodes.item(i).getAttributes().getNamedItem("r").getNodeValue()), 
						new Color(Integer.decode(nodes.item(i).getAttributes().getNamedItem("c").getNodeValue())));
			}
		} catch (Exception e) {
			System.out.println("Problem parsing texture XML");
			e.printStackTrace();
		}
		return textureMap;
	}
	
	/**
	 * Reads given resource XML file for given building, and returns with a hashmap containing Resource - Integer pairs,
	 * where the integer represents the building cost of given building
	 * @param path path of the XML file
	 * @param building the required building
	 * @return hashmap of the resource costs
	 */
	public static HashMap<Resource, Integer> readResourceXML(String path, BuildingEnum building) {
		HashMap<Resource,Integer> resourceMap=new HashMap();
		try {
			Document resourceXML=DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(path));
			NodeList nodes=resourceXML.getDocumentElement().getElementsByTagName("Building");
			for (int i=0;i<nodes.getLength();i++) {
				if (BuildingEnum.valueOf(nodes.item(i).getAttributes().getNamedItem("b").getNodeValue()).equals(building)) {
					NodeList resourceNodes=nodes.item(i).getChildNodes();
					for (int j=0;j<resourceNodes.getLength();j++) {
						 if (resourceNodes.item(j) instanceof Element == false)
						       continue;
						resourceMap.put(
								Resource.valueOf(resourceNodes.item(j).getAttributes().getNamedItem("r").getNodeValue()), 
								Integer.decode(resourceNodes.item(j).getAttributes().getNamedItem("v").getNodeValue()));
					}
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Problem parsing texture XML");
			e.printStackTrace();
		}
		return resourceMap;
	}
}
