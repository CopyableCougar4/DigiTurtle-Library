package com.digiturtle.parsing;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLReader {
   
   /** Document to parse */
   private Document document;
   /** Tag name wrapping the elements */
   private String tagName;
   
   /**
    * Construct the reader, using the filepath (a file)
    * @throws ParserConfigurationException 
    * @throws IOException 
    * @throws SAXException 
    */
   public XMLReader(File file, String tagName) throws ParserConfigurationException, SAXException, IOException{
      this.tagName = tagName;
      // Parse the XML file
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      document = dBuilder.parse(file);
      document.getDocumentElement().normalize();
   }
   
   /**
    * Construct the reader, using the filepath (a file)
    * @throws ParserConfigurationException 
    * @throws IOException 
    * @throws SAXException 
    */
   public XMLReader(InputStream inputStream, String tagName) throws ParserConfigurationException, SAXException, IOException{
      this.tagName = tagName;
      // Parse the XML file
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      document = dBuilder.parse(inputStream);
      document.getDocumentElement().normalize();
   }
   
   /**
    * Get a NodeList for a tagname
    */
   public NodeList getNodes(String node){
      return document.getElementsByTagName(node);
   }
   
   /**
    * Get a node for an id from a nodelist, with the givne 'tagname'
    */
   public Element getNodeByID(String id, String tagname){
      NodeList nodeList = getNodes(tagname);
      for(int i = 0; i < nodeList.getLength(); i++){
         Node node = nodeList.item(i);
         Element element = (Element) node;
         if(element.getAttribute("id").equalsIgnoreCase(id)){
            return element;
         }
      }
      return null;
   }
   
   /**
    * Get a value for a node
    */
   public String getNodeValue(Node node, String tagName){
      Element element = (Element) node;
      return element.getElementsByTagName(tagName).item(0).getTextContent();
   }
   
   /**
    * Find a node with a given ID
    */
   public Node findByID(String id, String tag){
      NodeList nodes = ((Element) document.getElementsByTagName(tagName).item(0)).getElementsByTagName(tag);
      for(int index = 0; index < nodes.getLength(); index++){
         Node node = nodes.item(index);
         Element element = (Element) node;
         if(element.getAttribute("id").equalsIgnoreCase(id)){
            return node;
         }
      }
      return null;
   }
   
   /**
    * Find a tag for a node
    */
   public String findTag(Node node, String title){
      if(node == null){
         return null; // send the string null
      }
      Element element = (Element) node;
      return element.getAttribute(title) == null ? "NULL" : element.getAttribute(title);
   }

}
