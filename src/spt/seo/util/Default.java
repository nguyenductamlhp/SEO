package spt.seo.util;

import java.io.File;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Default {

	public Default() {
		// TODO Auto-generated constructor stub
	}

	public void setInputDefault() {
		File f = new File("/src/spt.seo.data/input.xml");
		
		try {
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			Document document = builder.newDocument();
			Element input = document.createElement("input");
			document.appendChild(input);
			
			for (char c = ' '; c <= '~'; c++) {
				String nodeValue = Character.toString(c);
				Element character = document.createElement("character");
				character.setNodeValue(nodeValue);
				input.appendChild(character);
			}
			
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			DOMSource src = new DOMSource(document);
			StreamResult result = new StreamResult(f);
			
			transformer.transform(src, result);
			JOptionPane.showMessageDialog(null, "Set default!");
					
		} catch (ParserConfigurationException | TransformerException e) {
			JOptionPane.showMessageDialog(null, "Không tìm thấy file input.xml");
			e.printStackTrace();
		}
	}

	public void setOutputDefault() {
		File f = new File("/src/spt.seo.data/output.xml");
		
		try {
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			Document document = builder.newDocument();
			Element output = document.createElement("input");
			document.appendChild(output);
			
			for (char c = ' '; c <= '~'; c++) {
				String nodeValue = Character.toString(c);
				Element character = document.createElement("character");
				character.setNodeValue(nodeValue);
				output.appendChild(character);
			}
			
			TransformerFactory transFactory = TransformerFactory.newInstance();
			Transformer transformer = transFactory.newTransformer();
			DOMSource src = new DOMSource(document);
			StreamResult result = new StreamResult(f);
			
			transformer.transform(src, result);
			JOptionPane.showMessageDialog(null, "Set default!");
					
		} catch (ParserConfigurationException | TransformerException e) {
			JOptionPane.showMessageDialog(null, "Không tìm thấy file input.xml");
			e.printStackTrace();
		}
	}
	
	public String[] getInputDefault() {
		String[] returnStr = new String[255];
		try {
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			Document document = builder.newDocument();
			Element input = document.getDocumentElement();
			NodeList charList =  input.getChildNodes();
			for (int i = 0; i < charList.getLength(); i++) {
				returnStr[i] = charList.item(i).getTextContent();
			}
			
					
		} catch (ParserConfigurationException e) {
			JOptionPane.showMessageDialog(null, "Không tìm thấy file input.xml");
			e.printStackTrace();
		}
		return returnStr;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
