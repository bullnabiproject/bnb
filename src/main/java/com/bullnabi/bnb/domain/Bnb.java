package com.bullnabi.bnb.domain;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.servlet.ServletContext;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.bullnabi.bnb.global.component.BnbProp;
import com.bullnabi.bnb.global.utils.DataMap;
import com.bullnabi.bnb.global.utils.XmlUtil;

public class Bnb implements Serializable {
	
	private static final long serialVersionUID = 8348427598366340226L;
	
	/** javax.servlet.ServletContext */
	private ServletContext servletContext;
	
	/** 환경설정 Properties */
	private BnbProp bnbProp;
	
	/** 업무 코드 */
	private String businessCode;
	
	/** 파라미터 데이터 저장 (단건) */
	private DataMap dataMap;
	
	/** 파라미터 데이터 저장 (멀티) */
	private List<DataMap> dataMapList;
	
	
	/** 생성자 
	 * @param parameters 
	 * @param bnbProp 
	 * @param servletContext */
	public Bnb(ServletContext servletContext, BnbProp bnbProp, DataMap parameters){
		
		this.servletContext = servletContext;
		this.setBnbProp(bnbProp);
		this.setDataMap(parameters);
	}
	
	/**
	 * 설정 정보를 바탕으로 XML Document를 만든다.
	 * @return Document
	 */
	public Document getMetadataDocument(){
		
		//TODO 한번로드한 document는 계속 들고 있되, bnbProp나 businessCode가 변경되면 리셋되도록 처리
		
		if( this.businessCode == null 
				|| this.bnbProp == null 
				|| this.bnbProp.metadataPath == null
				|| this.bnbProp.metadataPath == "" ){
			
			return null;
		}
			
		String metadataFilePath = servletContext.getRealPath( this.bnbProp.metadataPath ) + "/" + this.businessCode + ".xml";
		
		File metadataFile = new File(metadataFilePath);
		
		Document document = null;
		try {
			document = XmlUtil.getDocument(metadataFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return document;
	}
	
	/**
	 * Metadata의 field 노드
	 * @return Document
	 */
	public NodeList getMetadataFieldList(){
		
		Document document = getMetadataDocument();
		
		NodeList fields = null;
		if( document != null ){
			fields = document.getElementsByTagName("field");
		}
		return fields;
	}
	
	/**
	 * 	<dataMaster/> 노드를 돌려주는 유틸성 함수
	 */
	public Element getMetadataMaster(){
		return (Element)(getMetadataDocument().getElementsByTagName("dataMaster").item(0));
	}
	
	
	public BnbProp getBnbProp() {
		return bnbProp;
	}

	public void setBnbProp(BnbProp bnbProp) {
		this.bnbProp = bnbProp;
	}
	
	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}
	
	public DataMap getDataMap() {
		return dataMap;
	}

	public void setDataMap(DataMap dataMap) {
		if(dataMap.get("businesscode") != null){
			this.businessCode = (String) dataMap.get("businesscode");
		}
		this.dataMap = dataMap;
	}

	public List<DataMap> getDataMapList() {
		return dataMapList;
	}

	public void setDataMapList(List<DataMap> dataMapList) {
		this.dataMapList = dataMapList;
	}

	
	/** ExtendService */
	private String extendService;
	public String getExtendService(){
		
		return getMetadataMaster().getAttribute("extendService");
	}
	
	/** Table Name */
	private String tableName;
	public String getTableName(){
		
		return getMetadataMaster().getAttribute("tableName");
	}
	
	/**
	 * list 호출 시 검색조건문으로
	 * sql 문을 String으로 바로 받는다.
	 * 
	 *******이 부분 좀 바꿔야 될 듯
	 */
	private String whereSearch;
	public String getWhereSearch() {
		if(whereSearch == null){
			whereSearch = "";
		}
		return whereSearch;
	}
	public void setWhereSearch(String whereSearch) {
		this.whereSearch = whereSearch;
	}
	
	/**
	 * list 호출 시 정렬조건문으로
	 * sql 문을 String으로 바로 받는다.
	 * 
	 *******이 부분 좀 바꿔야 될 듯
	 */
	private String orderQry;
	public String getOrderQry() {
		if(orderQry == null){
			orderQry = "";
		}
		return orderQry;
	}
	public void setOrderQry(String orderQry) {
		this.orderQry = orderQry;
	}
	
	/**
	 * select 쿼리에서 필요한 JOIN sql 생성
	 * 	ex) left join t_opportunity t_opportunity on t_opportunity.oppt_cd = targetTable.oppt_cd
	 */
	private String joinSql = "";
	public void setJoinSql(String joinSql){
		this.joinSql = joinSql;
	}
	public String getJoinSql(){
		return this.joinSql;
	}
	
	/**
	 * update, delete 등의 쿼리에서 필요한 key 값에 대한 sql 생성
	 * 	ex) and keyName_1 = 1 and keyName_2 = "00xxab"
	 */
	private String wherePrimaryKey;
	public void setWherePrimaryKey(String wherePrimaryKey){
		this.wherePrimaryKey = wherePrimaryKey;
	}
	public String getWherePrimaryKey(){
		return this.wherePrimaryKey;
	}
	
	/**
	 * insert 의 쿼리에서 필요한  sql 생성 [ insert into table (XXX) values(..) ]
	 * 	key값 insert 문은 keyType 별로 정의해서 만들어야 됨
	 * 	파라미터(map)으로 넘어온 것들만 sql문 만듬 - 파라미터가 넘어오지 않은 필드값은 저장되지 않는다.
	 * 	ex) filed_1, filed_2
	 */
	private String createFieldQuiry;
	public void setCreateFieldQuiry(String createFieldQuiry){
		this.createFieldQuiry = createFieldQuiry;
	}
	public String getCreateFieldQuiry(){
		return this.createFieldQuiry;
	}
	
	/**
	 * insert 의 쿼리에서 필요한  sql 생성 [ insert into table (..) values(XXX) ]
	 * 	key값 insert 문은 keyType 별로 정의해서 만들어야 됨
	 * 	파라미터(map)으로 넘어온 것들만 sql문 만듬 - 파라미터가 넘어오지 않은 필드값은 저장되지 않는다.
	 * 	ex) 1, '김경하'
	 */
	private String createValueQuiry;
	public void setCreateValueQuiry(String createValueQuiry){
		this.createValueQuiry = createValueQuiry;
	}
	public String getCreateValueQuiry(){
		return this.createValueQuiry;
	}
	
	/**
	 * update 의 쿼리에서 필요한 sql 생성 [ update table set XXX where .. ]
	 * 	key값 업데이트 문은 keyType 별로 정의해서 만들어야 됨
	 * 	파라미터(map)으로 넘어온 것들만 sql문 만듬 - 파라미터가 넘어오지 않은 필드값은 변경되지 않는다.
	 * 	ex) filed_1 = 1 , filed_2 = '김경하'
	 */
	private String updateQuiry;
	public void setUpdateQuiry(String updateQuiry){
		this.updateQuiry = updateQuiry;
	}
	public String getUpdateQuiry(){
		return this.updateQuiry;
	}
}
