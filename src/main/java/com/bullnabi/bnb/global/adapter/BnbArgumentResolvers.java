package com.bullnabi.bnb.global.adapter;

import java.io.EOFException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

import com.bullnabi.bnb.global.utils.DataMap;


public class BnbArgumentResolvers implements WebArgumentResolver {
	
	private static final Logger logger = LoggerFactory.getLogger(BnbArgumentResolvers.class);
	
	public Object resolveArgument(MethodParameter param, NativeWebRequest request) throws Exception {
		
		Class parameterType = param.getParameterType();
        String parameterName = param.getParameterName();
        
        String contentType = request.getHeader("content-type");
        Boolean isJsonParameter = false;
        if( contentType == null || contentType.indexOf("json") != -1 ){
        	isJsonParameter = true;
        }
        
        
        /*
			Iterator<String> geaderNames = request.getHeaderNames();
	        while( geaderNames.hasNext() ){
	        	String name = geaderNames.next();
	        	String value = request.getHeader(name);
	        	System.out.println("name:"+name+" value:"+value);
	        }
	        
			Websquare
				name:accept value:application/json
				name:submissionid value:ad1000List1DataSubmission
				name:content-type value:application/json; charset="UTF-8"
				
			
			jqGrid
				name:accept value:* /*
				name:x-requested-with value:XMLHttpRequest
				name:content-type value:application/x-www-form-urlencoded; charset=UTF-8
         */
        
        logger.debug("request parameterType:"+parameterType);
        logger.debug("request parameterName:"+parameterName);
        logger.debug("request contentType:"+contentType);
        logger.debug("request isJsonParameter:"+isJsonParameter);
        
		/*
        
        BufferedReader br = new BufferedReader(new InputStreamReader(httpServletRequest.getInputStream()));
        String line = null;
        while ( (line = br.readLine()) != null ) {
        	System.out.println("line:"+line);
        }
        
		Websquare
			request parameterType:interface java.util.Map
			request parameterName:parameters
			line:{"fromDate":"201301","toDate":"201302"}
		jqGrid
			request parameterType:interface java.util.Map
			request parameterName:parameters
			line:CORP_NM=(%EC%A3%BC)%EB%8F%99%EC%96%91%EC%A0%95%EB%B3%B4%EC%84%9C%EB%B9%84%EC%8A%A4&ESTB_DT=20030101&EMP_NM=%EC%9C%A4%EB%8C%80%EB%A1%9C&EMP_EMAIL=daero81%40naver.com&oper=edit&id=1
		*/
        
		Map parameter = new DataMap();
		
		HttpServletRequest httpServletRequest = (HttpServletRequest)request.getNativeRequest();
        
		if( isJsonParameter ){
			try{
				parameter = new ObjectMapper().readValue(httpServletRequest.getInputStream(), DataMap.class);
			}catch(EOFException e){
				logger.error(e.toString());
			}
		}else{
	        Enumeration<String> parameterNames = httpServletRequest.getParameterNames();
	        while(parameterNames.hasMoreElements()) {
	        	String name =  parameterNames.nextElement();
	        	String value =  httpServletRequest.getParameter(name);
	        	//System.out.println("parameterName:"+name+" parameterValue:"+value);
	        	parameter.put(name.toLowerCase(), value);
	        }
		}
        
        logger.debug("request parameterMap:"+parameter.toString());
 
        return parameter;
	}
	
	
	public static void main(String[] args) throws Exception
	  {
	/*
	{
	    "name": "foo",
	    "age": "45",
	    "children": [
	        {
	            "name": "bar",
	            "age": "15"
	        },
	        {
	            "name": "baz",
	            "age": "10"
	        }
	    ]
	}
	 */
	    String jsonInput = "{\"name\":\"foo\",\"age\":\"45\",\"children\":[{\"name\":\"bar\",\"age\":\"15\"},{\"name\":\"baz\",\"age\":\"10\"}]}";

	    ObjectMapper mapper = new ObjectMapper();
	    HashMap parent = mapper.readValue(jsonInput, HashMap.class);
	    System.out.println(mapper.writeValueAsString(parent));
	    // output:
	    // {"name":"foo","age":45,"children":[{"name":"bar","age":15},{"name":"baz","age":10}]}
	  }
	
}
