package com.bullnabi.bnb.global.utils;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * 무한 뎁스의 XML 구조를 만들 수 있다.
 * POJO 와 LIST 간.
 * TO-DO : MAP 일 경우에 대한 로직 추가. 
 */
public class XmlUtil {
	
	public static final String XML_CONTENT_TYPE = "text/xml;charset=UTF-8";
	
	/**
	 * 새로운 Document를 만든다.
	 * @return
	 */
	public static Document getDocument(){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		Document doc = null;
		try {
			builder = factory.newDocumentBuilder();
			doc = builder.newDocument();
		} catch (ParserConfigurationException e) {
			System.out.println("Error : XmlUtil.getDocument");
			e.printStackTrace();
		} finally {
		}
		return doc;
	}
	
	/**
	 * xml 파일을 Document로 변환한다.
	 * @param fle
	 * @return
	 * @throws IOException 
	 */
	public static Document getDocument(File file) throws IOException {
		Document doc = null;
		
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));;
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			
			DocumentBuilder builder = null;
			
			builder = factory.newDocumentBuilder();
			
			InputSource inputSource = new InputSource(br);
			doc = builder.parse(inputSource);
		}catch(Exception e){
			System.out.println(e.toString());
		}finally{
			br.close();
		    br = null;
		}
		return doc;
	}
	
	/**
	 * 문자로 된 xml을 Document로 변환한다.
	 * @param str
	 * @return
	 */
	public static Document getDocument(String str){
		Document doc = null;
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			DocumentBuilder builder = null;

			builder = factory.newDocumentBuilder();

			StringReader userdataReader = new StringReader(str);

			InputSource inputSource = new InputSource(userdataReader);

			doc = builder.parse(inputSource);
		}catch(Exception e){
			System.out.println(e.toString());
		}
		return doc;
	}
	
	/**
	 * Document를 헤더 포함된 String XML로 변환한다.
	 * @param doc
	 * @return
	 */
	public static String getDocumentAsXml(Document doc) {
		StringWriter sw = new java.io.StringWriter();
		try {
			DOMSource domSource = new DOMSource(doc);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			StreamResult sr = new StreamResult(sw);
			transformer.transform(domSource, sr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sw.toString();
	}
	
	/**
	 * 리스트를 XML로 바꿔준다.
	 * <ITEMS>
	 * 		<ITEM 속성1="값1" 속성2="값2"/>
	 * 		<ITEM 속성1="값1" 속성2="값2"/>
	 * 		...
	 * </ITEMS>
	 * @return
	 */
	public static Document toXMLDocument(List list) {
		
		Document doc = getDocument();
		
		Element elem = doc.createElement("ITEMS");
		doc.appendChild(elem);
		
		objectToXML(list, doc, elem);
		
		return doc;
	}
	
	/**
	 * 리스트를 XML로 바꿔준다.
	 * <ITEMS>
	 * 		<ITEM 속성1="값1" 속성2="값2"/>
	 * 		<ITEM 속성1="값1" 속성2="값2"/>
	 * 		...
	 * </ITEMS>
	 * @return
	 */
	public static Document toXMLDocumentByDataMap(List<DataMap> list) {
		
		Document doc = getDocument();
		
		Element elem = doc.createElement("ITEMS");
		doc.appendChild(elem);
		
		dataMapToXML(list, doc, elem);
		
		return doc;
	}
	
	/**
	 * POJO를 XML로 바꿔준다.
	 * 	<ITEM 속성1="값1" 속성2="값2"/>
	 * @return
	 */
	public static Document toXMLDocument(Object pojo) {
		
		Document doc = getDocument();
		
		Element elem = doc.createElement("ITEM");
		doc.appendChild(elem);
		
		if(pojo != null)
			objectToXML(pojo, doc, elem);
		
		return doc;
	}
	
	/**
	 * DataMap를 XML로 바꿔준다.
	 * 	<ITEM 속성1="값1" 속성2="값2"/>
	 * @return
	 */
	public static Document toXMLDocumentByDataMap(DataMap dataMap) {
		
		Document doc = getDocument();
		
		Element elem = doc.createElement("ITEM");
		doc.appendChild(elem);
		
		if(dataMap != null)
			dataMap.toXMLDocument(elem);
		
		return doc;
	}
	
	/**
	 * elem에 list의 내용을 추가한다.
	 * <list태그 명 length="2">
	 * 	<ITEM 속성1="값1" 속성2="값2"/>
	 * 	<ITEM 속성1="값1" 속성2="값2"/>
	 * </list태그 명>
	 */
	public static void objectToXML(List list, Document doc, Element elem) {
		
		if(list == null){
			elem.setAttribute("length", "0");
			return;
		}
		
		int listSize = list.size();
		elem.setAttribute("length", listSize+"");
		
		for(int i = 0; i < listSize; i++){
			
			if(		list.get(i).getClass() == ArrayList.class 
				|| 	list.get(i).getClass() == List.class){	//List 형태면 재귀함수 호출
				
				List subList = (List)list.get(i);
				Element listElem = doc.createElement("ITEMS");
				elem.appendChild(listElem);
				objectToXML(subList, doc, listElem);	
			
			}else{											//POJO 형태
				Object pojo = list.get(i);
				Element row = doc.createElement("ITEM");
				elem.appendChild(row);
				objectToXML(pojo, doc, row);			
			}
		}
	}
	
	/**
	 * elem에 list의 내용을 추가한다.
	 * <list태그 명 length="2">
	 * 	<ITEM 속성1="값1" 속성2="값2"/>
	 * 	<ITEM 속성1="값1" 속성2="값2"/>
	 * </list태그 명>
	 */
	public static void dataMapToXML(List<DataMap> list, Document doc, Element elem) {
		
		if(list == null){
			elem.setAttribute("length", "0");
			return;
		}
		
		int listSize = list.size();
		elem.setAttribute("length", listSize+"");
		
		for(int i = 0; i < listSize; i++){
			
														//POJO 형태
			DataMap dataMap = list.get(i);
			Element row = doc.createElement("ITEM");
			elem.appendChild(row);
			dataMap.toXMLDocument(row);
		}
	}
	
	public static DataMap xmlToDataMap(Node node) {
		
		DataMap dataMap = new DataMap();
		
		NamedNodeMap nodeMap = node.getAttributes();
		for(int i = 0; i < nodeMap.getLength(); i++){
			Node item = nodeMap.item(i);
			String key = item.getNodeName();
			String value = item.getNodeValue();
			dataMap.put(key, value);
		}
		return dataMap;
	}
	
	
	/**
	 * 넘어온 elem에 pojo의 속성과 값을 Attribute로 세팅한다.
	 * <ITEM 속성1="값1" 속성2="값2"/>
	 * @return
	 */
	public static void objectToXML(Object pojo, Document doc, Element elem){
		if(pojo == null)
			return;
		
		try {
			PropertyDescriptor[] props = Introspector.getBeanInfo(pojo.getClass()).getPropertyDescriptors();
		
			for (int i=0; i < props.length; i++) {
				
				PropertyDescriptor prop = props[i];
				
				String name = prop.getName();
				
				if (name.equals("class")) continue;
				Method getter = prop.getReadMethod();
				if(getter != null){
				
					if(		prop.getPropertyType() == List.class
						|| 	prop.getPropertyType() == ArrayList.class){	//List 형태면 자식으로
						
						List subList = (List)getter.invoke(pojo, null);
						//이게 Null 이면 태그자체를 만들지 말까 어쩔까..
						//밑에 Attribute랑 같은 논리로 하자.
						Element listElem = doc.createElement( name );
						elem.appendChild(listElem);
						objectToXML(subList, doc, listElem);
					
					}else{												//POJO 형태면 어트리뷰트로
						
						Object objVal = getter.invoke(pojo, null);
						String val = "";
						if (objVal == null)	val = "";
						else				val = objVal.toString();
						
						elem.setAttribute(name, val);
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 	xmlList 오버라이딩
	 */
	public static void overrideXmlList(
										NodeList target, String targetTag, 
										NodeList parent, String parentTag){
		
		for( int i=0; i<target.getLength(); i++ ){
			Element targetColumn = (Element)target.item(i);
			Element parentColumn = null;
			
			String targetTagValue = targetColumn.getAttribute(targetTag);
			for( int j=0; j<parent.getLength(); j++ ){
				
				if( targetTagValue.equals(((Element)parent.item(j)).getAttribute(parentTag)) ){
					parentColumn = (Element)parent.item(j);
					break;
				}
			}
			
			if( parentColumn != null ){
				overrideXml(targetColumn, parentColumn);
			}
		}
	}
	
	/**
	 * 	xml 오버라이딩
	 */
	public static Element overrideXml(Element target, Element parent){
		
		if(parent != null){
			
			NamedNodeMap namedNodeMap = parent.getAttributes();
			for( int i=0; i<namedNodeMap.getLength(); i++){
				
				Node attributeNode = namedNodeMap.item(i);
				String parentAttributeName = attributeNode.getNodeName();
				String parentAttributeValue = attributeNode.getNodeValue();
				
				// attribute override
				if(!target.hasAttribute(parentAttributeName)){
					target.setAttribute(parentAttributeName, parentAttributeValue);
				}
				
				// children override
				if(parent.getChildNodes().getLength() > 0){
					if(target.getChildNodes().getLength() == 0){
						for (int j=0; j<target.getChildNodes().getLength(); j++){
							
							target.appendChild(target.getChildNodes().item(j));
						}
					}
				}
				
			}
		}
		
		return target;
	}
	
}
