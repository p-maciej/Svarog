package svarog.language;

//InterfaceTranslations
//
//InterfaceTranslations(string filename) - reading xml format file like:
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
//	SetNewInterfaceTranslations(String file_name) //erasing existing data and making new key/value array list, returns void
//
//	getValue(String k) //return value to given key if any exist
//
//	getKey(String v) //return key to given value if any exist
//
//	GetInterfaceTranslations() //return this, we could remove it in future
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

public class InterfaceTranslations {
	  public static enum languages {
	      PL_pl, EN_en
	  }
  
	  private ArrayList<InterfaceTranslation> data = new ArrayList<InterfaceTranslation>();
	
	  private static final String path = "resources/languages/";
	
	  InterfaceTranslations(languages option) {
	      CreateData(Options(option));
	  }
	
	  public String getValue(String k) {
	      for(int i = 0; i< data.size();i++){
	          if(data.get(i).getKey().equals(k)) {
	              return data.get(i).getValue();
	          }
	      }
	      return "There is no " + k +"!";
	  }
	
	  public String getKey(String v) {
	      for(int i = 0; i< data.size(); i++){
	          if(data.get(i).getValue().equals(v)) {
	              return data.get(i).getKey();
	          }
	      }
	      return "There is no " + v +"!";
	  }
	
	  private String Options(languages option) {
	      String file_name;
	      if(option == languages.EN_en) {
	          file_name = "en_EN.lang";
	      } else if(option == languages.PL_pl) {
	          file_name = "pl_PL.lang";
	      } else {
	          file_name = "en_EN.lang";
	      }
	      return file_name;
	  }
	
	  void CreateData(String file_name){
	      try{
	          //creating a constructor of file class and parsing an XML file
	          File file = new File(path + file_name);
	          //an instance of factory that gives a document builder
	          DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	          //an instance of builder to parse the specified xml file
	          DocumentBuilder db = dbf.newDocumentBuilder();
	          Document doc = db.parse(file);
	          doc.getDocumentElement().normalize();
	          NodeList nodeList = doc.getElementsByTagName("variable");
	          // nodeList is not iterable, so we are using for loop
	          for (int itr = 0; itr < nodeList.getLength(); itr++) {
	              Node node = nodeList.item(itr);
	              if (node.getNodeType() == Node.ELEMENT_NODE) {
	                  Element eElement = (Element) node;
	                  data.add(0, new InterfaceTranslation(eElement.getElementsByTagName("key").item(0).getTextContent().trim(), eElement.getElementsByTagName("translation").item(0).getTextContent().trim()));
	              }
	          }
	      }
	      catch (Exception e) {
	          e.printStackTrace();
	      }
	  }
}
