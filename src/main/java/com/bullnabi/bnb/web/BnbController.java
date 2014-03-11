package com.bullnabi.bnb.web;


import java.util.List;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.w3c.dom.Document;

import com.bullnabi.bnb.domain.Bnb;
import com.bullnabi.bnb.global.component.BnbProp;
import com.bullnabi.bnb.global.utils.DataMap;
import com.bullnabi.bnb.global.utils.XmlUtil;
import com.bullnabi.bnb.service.BnbService;

@Controller
@RequestMapping(value="/bnb/*.do")
public class BnbController {
	
	private static final Logger logger = LoggerFactory.getLogger(BnbController.class);
	
	@Autowired
	private ServletContext servletContext;
	
	@Autowired
    private BnbService bnbService;
	
	@Autowired
    private BnbProp bnbProp;
	
	
	@RequestMapping
	public @ResponseBody List<DataMap> list(DataMap parameters) throws Exception {

		Bnb bnb = new Bnb(servletContext, bnbProp, parameters);
		
		List<DataMap> list = bnbService.list(bnb);
		return list;
	}
	
	@RequestMapping
	public @ResponseBody int create(DataMap parameters) throws Exception {
		
		Bnb bnb = new Bnb(servletContext, bnbProp, parameters);
		bnb.setBnbProp(bnbProp);
		bnb.setDataMap(parameters);
		
		int result = 0;
		result = bnbService.create(bnb);
		return result;
	}
	
	@RequestMapping
	public @ResponseBody int update(DataMap parameters) throws Exception {
		
		Bnb bnb = new Bnb(servletContext, bnbProp, parameters);
		bnb.setBnbProp(bnbProp);
		bnb.setDataMap(parameters);
		
		int result = 0;
		result = bnbService.update(bnb);
		return result;
	}
	
	@RequestMapping
	public @ResponseBody int delete(DataMap parameters) throws Exception {
		
		Bnb bnb = new Bnb(servletContext, bnbProp, parameters);
		bnb.setBnbProp(bnbProp);
		bnb.setDataMap(parameters);
		
		int result = 0;
		result = bnbService.delete(bnb);
		return result;
	}
	
	@RequestMapping
	public @ResponseBody String metadata(DataMap parameters) throws Exception {
		
		Bnb bnb = new Bnb(servletContext, bnbProp, parameters);
		
		Document metaXML = bnb.getMetadataDocument();
		
		// Server 단에서 xml override
		XmlUtil.overrideXmlList(
				metaXML.getElementsByTagName("column"), "fieldId", 
				metaXML.getElementsByTagName("field"), "id");
		XmlUtil.overrideXmlList(
				metaXML.getElementsByTagName("item"), "fieldId", 
				metaXML.getElementsByTagName("field"), "id");

		String result = XmlUtil.getDocumentAsXml(metaXML);
		
		return result;
	}
	
	
}
