package com.bullnabi.bnb.global.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.bullnabi.bnb.domain.Bnb;
import com.bullnabi.bnb.global.utils.DataMap;

@Component
public class BnbQueryCo{
	
	@Autowired
    private BnbProp bnbProp;
	
	/**
	 * update, delete 등의 쿼리에서 필요한 key 값에 대한 sql 생성
	 * 	ex) and keyName_1 = 1 and keyName_2 = "00xxab"
	 */
	public void setJoinSql(Bnb bnb){
		String tmp = "";
		
		//" left join t_opportunity t_opportunity on t_opportunity.oppt_cd = targetTable.oppt_cd ";
		NodeList nodeList = bnb.getMetadataFieldList();
		//DataMap map = disco.getDataMap();
		for(int i=0; i<nodeList.getLength(); i++){
			Element field = (Element)nodeList.item(i);
			String tagName = field.getAttribute("tagName").replace("@", "");
			if(field.hasAttribute("joinRefTableName")){
				String joinRefTableName = field.getAttribute("joinRefTableName");
				String joinRefKeyField = field.getAttribute("joinRefKeyField");
				tmp += " left join " + joinRefTableName +" "+ joinRefTableName + " on "+
						joinRefTableName+"."+joinRefKeyField+" = ";
				if( tagName.indexOf(".") == -1 ){	//조인된 table에 다시 조인할 때 :: ex) tableName.field :: '.' 이 있으면 그대로 조인걸고 없으면 tagetTable로 조인건다.
					tmp += "targetTable."+tagName;
				}else{
					tmp += tagName;
				}
			}
		}
		if(tmp != "")
			System.out.println("joinSql : "+tmp);
		bnb.setJoinSql(tmp);
	}
	
	/**
	 * update, delete 등의 쿼리에서 필요한 key 값에 대한 sql 생성
	 * 	ex) and keyName_1 = 1 and keyName_2 = "00xxab"
	 */
	public void setWherePrimaryKey(Bnb bnb){
		String tmp = "";
		
		NodeList nodeList = bnb.getMetadataFieldList();
		DataMap map = bnb.getDataMap();
		for(int i=0; i<nodeList.getLength(); i++){
			Element field = (Element)nodeList.item(i);
			String tagName = field.getAttribute("tagName").replace("@", "");
			String dataType = field.getAttribute("dataType");
			if(field.hasAttribute("keyType")){
				tmp += " and targetTable." + tagName + " = ";
				if(!dataType.equals("number")){	// 쿼리문 따옴표 처리
					tmp += "'";
				}
				tmp += map.get(tagName);		// 파라미터로 항상 넘어 와야 한다. 안넘어 올 때 로직 처리
				if(!dataType.equals("number")){
					tmp += "' ";
				}
			}
		}
		System.out.println("wherePrimaryKey : "+tmp);
		bnb.setWherePrimaryKey(tmp);
	}
	
	/**
	 * 자식에서 부모키를 세팅
	 * 	ex) and keyName_1 = 1 and keyName_2 = "00xxab"
	 */
	public void setWherePrimaryKeyFromChild(Bnb bnb){
		String tmp = "";
		
		NodeList nodeList = bnb.getMetadataFieldList();
		DataMap map = bnb.getDataMap();
		for(int i=0; i<nodeList.getLength(); i++){
			Element field = (Element)nodeList.item(i);
			String tagName = field.getAttribute("tagName").replace("@", "");
			String dataType = field.getAttribute("dataType");
			if(field.hasAttribute("keyType")){
				if(field.getAttribute("keyType").equals("pk_fk")){
					tmp += " and targetTable." + tagName + " = ";
					if(!dataType.equals("number")){	// 쿼리문 따옴표 처리
						tmp += "'";
					}
					tmp += map.get(tagName);		// 파라미터로 항상 넘어 와야 한다. 안넘어 올 때 로직 처리
					if(!dataType.equals("number")){
						tmp += "' ";
					}
				}
			}
		}
		System.out.println("setWherePrimaryKeyFromChild : "+tmp);
		bnb.setWherePrimaryKey(tmp);
	}
	
	
	
	/**
	 * insert 의 쿼리에서 필요한  sql 생성 [ insert into table (XXX) values(..) ]
	 * 	key값 insert 문은 keyType 별로 정의해서 만들어야 됨
	 * 	파라미터(map)으로 넘어온 것들만 sql문 만듬 - 파라미터가 넘어오지 않은 필드값은 저장되지 않는다.
	 * 	ex) filed_1, filed_2
	 */
	public void setCreateFieldQuiry(Bnb bnb){
		String tmp = "";
		String sessionId = "508";//disco.getLoginUser().getUserCode();
		NodeList nodeList = bnb.getMetadataFieldList();
		DataMap map = bnb.getDataMap();
		Boolean isFirst = true;
		
		for(int i=0; i<nodeList.getLength(); i++){
			Element field = (Element)nodeList.item(i);
			String tagName = field.getAttribute("id");
			String dataType = field.getAttribute("dataType");
			
			boolean isCreateField = true;	// insert into () 쿼리에 포함되는 필드인지
			
			if(field.hasAttribute("keyType")){
				String keyType = field.getAttribute("keyType");
				if(keyType.equals("pk_auto_increment")){		// 1씩 자동 증가 하는 키
					isCreateField = false;
				}else if(keyType.equals("pk_auto_generate")){	// 로직에 의해 생성되는 키
					//생성 후 map에 넣어둔다.
					String value = this.setPkAutoGenerate(bnb);
					map.put(tagName, value);
					isCreateField = true;
				}else if(keyType.equals("pk_fk")){				// 참조 키. 사용자가 선택
					isCreateField = true;
				}else if(keyType.equals("pk_user")){
					isCreateField = true;
				}else{
					isCreateField = true;
				}
			}else {	// 키타입이 없으면 포함
				isCreateField = true;
			}
			
			if(field.hasAttribute("hidden")){	// 로그성 필드
				String hidden = field.getAttribute("hidden");
				isCreateField = true;
				if(	hidden.equals("createDatetime") || hidden.equals("updateDatetime")){	// 등록일시, 수정일시
					map.put(tagName, getQueryForSysdate());
				}else if(hidden.equals("createUser") || hidden.equals("updateUser")){			// 등록자, 수정자
					map.put(tagName, sessionId);												// 세션으로 부터 사용자 가져옴.
				}
			}
			
			if(isCreateField){
				
				if(map.get(tagName) != null){	// 파라미터로 넘어 온 것만
					if(isFirst){
						isFirst = false;
					}else{
						tmp += " , ";
					}
					tmp += tagName;
				}
			}
		}
		//System.out.println("createFieldQuiry : "+tmp);
		bnb.setCreateFieldQuiry(tmp);
	}
	
	/**
	 * insert 의 쿼리에서 필요한  sql 생성 [ insert into table (..) values(XXX) ]
	 * 	key값 insert 문은 keyType 별로 정의해서 만들어야 됨
	 * 	파라미터(map)으로 넘어온 것들만 sql문 만듬 - 파라미터가 넘어오지 않은 필드값은 저장되지 않는다.
	 * 	ex) 1, '김경하'
	 */
	public void setCreateValueQuiry(Bnb bnb){
		String tmp = "";
		NodeList nodeList = bnb.getMetadataFieldList();
		DataMap map = bnb.getDataMap();
		Boolean isFirst = true;
		
		for(int i=0; i<nodeList.getLength(); i++){
			Element field = (Element)nodeList.item(i);
			String tagName = field.getAttribute("id");
			String dataType = field.getAttribute("dataType");
			
			boolean isCreateField = true;	// insert into () 쿼리에 포함되는 필드인지
			if(field.hasAttribute("keyType")){
				String keyType = field.getAttribute("keyType");
				if(keyType.equals("pk_auto_increment")){	// 1씩 자동 증가 하는 키
					isCreateField = false;
				}else if(keyType.equals("pk_auto_generate")){	// 로직에 의해 생성되는 키
					isCreateField = true;
				}else if(keyType.equals("pk_fk")){	// 참조 키. 사용자가 선택
					isCreateField = true;
				}else if(keyType.equals("pk_user")){
					isCreateField = true;
				}else{
					isCreateField = true;
				}
			}else{	// 키타입이 없으면 포함
				isCreateField = true;
			}
			
			
			if(isCreateField){
				String value = null;
				if(map.get(tagName) != null){
					value = (String)map.get(tagName);
				}
				
				if(value != null){	// 파라미터로 넘어 온 것만
					if(isFirst){
						isFirst = false;
					}else{
						tmp += " , ";
					}
					
					if(		!dataType.equals("number")
						&&	!value.equals(getQueryForSysdate())){	// 쿼리문 따옴표 처리
						tmp += "'";
					}
					
					tmp += value;
					
					if(		!dataType.equals("number")
						&&	!value.equals(getQueryForSysdate())){	// 쿼리문 따옴표 처리
						tmp += "' ";
					}
				}
			}
		}
		//System.out.println("createValueQuiry : "+tmp);
		bnb.setCreateValueQuiry(tmp);
	}
	
	/**
	 * update 의 쿼리에서 필요한 sql 생성 [ update table set XXX where .. ]
	 * 	key값 업데이트 문은 keyType 별로 정의해서 만들어야 됨
	 * 	파라미터(map)으로 넘어온 것들만 sql문 만듬 - 파라미터가 넘어오지 않은 필드값은 변경되지 않는다.
	 * 	ex) filed_1 = 1 , filed_2 = '김경하'
	 */
	public void setUpdateQuiry(Bnb bnb){
		String tmp = "";
		String sessionId = "508";//disco.getLoginUser().getUserCode();
		NodeList nodeList = bnb.getMetadataFieldList();
		DataMap map = bnb.getDataMap();
		Boolean isFirst = true;
		
		for(int i=0; i<nodeList.getLength(); i++){
			Element field = (Element)nodeList.item(i);
			String tagName = field.getAttribute("tagName").replace("@", "");
			String dataType = field.getAttribute("dataType");
			
			// 키들도 같이 없데이트 해도 되지만 (어차피 같기 때문에) 확실하게 하기 위함.
			boolean isUpdatingField = true;	// update set 쿼리에 포함되는 필드인지
			if(field.hasAttribute("keyType")){
				String keyType = field.getAttribute("keyType");
				if(keyType.equals("pk_auto_increment")){	// 1씩 자동 증가 하는 키
					isUpdatingField = false;
				}else if(keyType.equals("pk_auto_generate")){	// 로직에 의해 생성되는 키
					isUpdatingField = false;
				}else if(keyType.equals("pk_fk")){	// 참조 키. 사용자가 선택
					isUpdatingField = true;
				}else if(keyType.equals("pk_user")){
					isUpdatingField = true;
				}else{
					isUpdatingField = true;
				}
			}else{	// 키타입이 없으면 포함
				isUpdatingField = true;
			}
			
			if(field.hasAttribute("hidden")){	// 로그성 필드
				String hidden = field.getAttribute("hidden");
				isUpdatingField = true;
				if(hidden.equals("updateDatetime")){	// 등록일시, 수정일시
					map.put(tagName, getQueryForSysdate());
				}else if(hidden.equals("updateUser")){	// 등록자, 수정자
					map.put(tagName, sessionId);		// 세션으로 부터 사용자 가져옴.
				}else{
					isUpdatingField = false;
				}
			}
			
			if(isUpdatingField){
				String value = null;
				if(map.get(tagName) != null){
					value = (String)map.get(tagName);
				}
				
				if(value != null){	// 파라미터로 넘어 온 것만
					if(isFirst){
						isFirst = false;
					}else{
						tmp += " , ";
					}
					
					tmp += tagName + " = ";
					
					if(		!dataType.equals("number")
						&&	!value.equals(getQueryForSysdate())){	// 쿼리문 따옴표 처리
						tmp += "'";
					}
					
					tmp += value;
					
					if(		!dataType.equals("number")
						&&	!value.equals(getQueryForSysdate())){	// 쿼리문 따옴표 처리
						tmp += "' ";
					}
				}
			}
		}
		System.out.println("updateQuiry : "+tmp);
		bnb.setUpdateQuiry(tmp);
	}
	
	/**
	 * 확장해서 쓰도록
	 * 	PK 자동 생성 로직이 들어가면 됨.
	 * @param bullnabi
	 * @return
	 */
	public String setPkAutoGenerate(Bnb bnb) {
		System.out.println("setPkAutoGenerate 메서드가 구현이 안됐네요. 확장해서 사용하세요.");
		return "";
	}
	
	
	
	/**
	 * 현재날짜
	 * @return
	 */
	public String getQueryForSysdate(){
		String qry = "sysdate";
		if( bnbProp.envDatabaseType.equals("oracle") ){
			qry = "sysdate";
		}
		else if( bnbProp.envDatabaseType.equals("mssql") ){
			qry = "getdate()";
		}
		else if( bnbProp.envDatabaseType.equals("mysql") ){
			qry = "sysdate()";
		}
		return qry;
	}
}	
