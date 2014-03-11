package com.bullnabi.bnb.global.component;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 프로퍼티
 **/
@Component
public class BnbProp{
	
	@Autowired 
	public Properties bnbProperties;
	
	public String getProperty(String key){
		return bnbProperties.getProperty(key);
	}
	
	@Value("#{bnbProperties['env.database.type']}")
	public String envDatabaseType;
	
	@Value("#{bnbProperties['env.metadata.path']}")
	public String metadataPath;
	
}	
