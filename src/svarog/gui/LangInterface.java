package svarog.gui;

//LangInterface
//
// LangInterface(string filename) - reading xml format file like:
//<?xml version="1.0"?>
//<file>
//<variable>
//<key>a</key>
//<translation>1</translation>
//</variable>
//<variable>
//<key>a</key>
//<translation>1</translation>
//</variable>
//</file>
//
//Methods:
//	SetNewLangInterface(String file_name) //erasing existing data and making new key/value array list, returns void
//
//	getValue(String k) //return value to given key if any exist
//
//	getKey(String v) //return key to given value if any exist
//
//	GetLangInterface() //return this, we could remove it in future
//
//Fields:
//	List<String> keys
//	List<String> values
//	String path



import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LangInterface {
    List<String> keys = new ArrayList<String>();
    List<String> values = new ArrayList<String>();
    
    String path = "resources/lang/interf/";

    public LangInterface(String file_name) {
        try
        {
            //creating a constructor of file class and parsing an XML file
            File file = new File(path + file_name);
            //an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            //System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("variable");
            // nodeList is not iterable, so we are using for loop
            for (int itr = 0; itr < nodeList.getLength(); itr++)
            {
                Node node = nodeList.item(itr);
                //System.out.println("\nNode Name :" + node.getNodeName());
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) node;
                    //System.out.println("key: "+ eElement.getElementsByTagName("key").item(0).getTextContent());
                    keys.add(eElement.getElementsByTagName("key").item(0).getTextContent().trim());
                    //System.out.println("translation: "+ eElement.getElementsByTagName("translation").item(0).getTextContent());
                    values.add(eElement.getElementsByTagName("translation").item(0).getTextContent().trim());
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void SetNewLangInterface(String file_name){
        try
        {
            keys.removeAll(keys);
            values.removeAll(values);
            //creating a constructor of file class and parsing an XML file
            File file = new File(path + file_name);
            //an instance of factory that gives a document builder
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //an instance of builder to parse the specified xml file
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            //System.out.println("Root element: " + doc.getDocumentElement().getNodeName());
            NodeList nodeList = doc.getElementsByTagName("variable");
            // nodeList is not iterable, so we are using for loop
            for (int itr = 0; itr < nodeList.getLength(); itr++)
            {
                Node node = nodeList.item(itr);
                //System.out.println("\nNode Name :" + node.getNodeName());
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) node;
                    //System.out.println("key: "+ eElement.getElementsByTagName("key").item(0).getTextContent());
                    keys.add(eElement.getElementsByTagName("key").item(0).getTextContent().trim());
                    //System.out.println("translation: "+ eElement.getElementsByTagName("translation").item(0).getTextContent());
                    values.add(eElement.getElementsByTagName("translation").item(0).getTextContent().trim());
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getValue(String k) {
        int iterator =0;
        for(String i:keys){
            //System.out.println(i+""+k);
            if(i.equals(k)) {
                return values.get(iterator);
            }
            iterator++;
        }
        return "There is no " + k +"!";
    }

    public String getKey(String v) {
        int iterator =0;
        for(String i:keys){
            //System.out.println(i+""+v);
            if(i.equals(v)) {
                return values.get(iterator);
            }
            iterator++;
        }
        return "There is no " + v +"!";
    }

    LangInterface GetLangInterface(){
        return this;
    }
}
