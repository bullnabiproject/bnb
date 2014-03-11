package com.bullnabi.bnb.global.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Element;

public class DataMap<K, V> implements Map<K, V>, Serializable {

	private static final long serialVersionUID = 6773954545833958213L;
	/** The data map. */
	private Map<K, V> dataMap;

	/**
	 * Instantiates a new data map.
	 */
	public DataMap(){
		this.dataMap = new HashMap<K, V>();
	}

	/**
	 * Instantiates a new data map.
	 *
	 * @param srcMapParam the src map param
	 */
	public DataMap (Map<K, V> srcMapParam){
		this();
		if( srcMapParam != null ){
			this.dataMap.clear();
			Iterator<K> it = srcMapParam.keySet().iterator();
			while( it.hasNext() ){
				K key = (K) it.next();
				V val = srcMapParam.get(key);
				this.dataMap.put(key, val);
//				this.dataMap.put( key, val==null?"":val ); //generic ; ����ϰ� �Ǹ� null �϶�, ""(String); ��� ���Ѵ�.
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.util.Map#clear()
	 */
	public void clear() {
		this.dataMap.clear();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#containsKey(java.lang.Object)
	 */
	public boolean containsKey(Object key) {
		return this.dataMap.containsKey(key);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#containsValue(java.lang.Object)
	 */
	public boolean containsValue(Object value) {
		return this.dataMap.containsKey(value);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#entrySet()
	 */
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return this.dataMap.entrySet();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#get(java.lang.Object)
	 */
	public V get(Object key) {
		return (V)this.dataMap.get(key);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#isEmpty()
	 */
	public boolean isEmpty() {
		return this.dataMap.isEmpty();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#keySet()
	 */
	public Set<K> keySet() {
		return this.dataMap.keySet();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
	 */
	public V put(K key, V value) {
		return (V)dataMap.put(key, value );
	}

	/* (non-Javadoc)
	 * @see java.util.Map#putAll(java.util.Map)
	 */
	public void putAll(Map<? extends K, ? extends V> m) {
		this.dataMap.putAll(m);

	}

	/* (non-Javadoc)
	 * @see java.util.Map#remove(java.lang.Object)
	 */
	public V remove(Object key) {
		return (V)this.dataMap.remove(key);
	}

	/* (non-Javadoc)
	 * @see java.util.Map#size()
	 */
	public int size() {
		return this.dataMap.size();
	}

	/* (non-Javadoc)
	 * @see java.util.Map#values()
	 */
	public Collection<V> values() {
		return this.dataMap.values();
	}

	/**
	 * Gets the string.
	 *
	 * @param key the key
	 *
	 * @return the string
	 */
	public String getString( String key ) {
		String returnVal = null;
		Object obj = get( key );
		if( obj instanceof String[] ){
			String[] strArr = (String[])obj;
			returnVal = strArr[0];
		}else if(obj instanceof String) {
			returnVal = (String)obj;
		}else{
			returnVal = (obj==null?"":obj.toString());
		}

		return returnVal == null?"":returnVal ;
	}

	/**
	 * Gets the int.
	 *
	 * @param key the key
	 *
	 * @return the int
	 */
	public int  getInt( String key ){
		Object obj = get(key);
		try{
			if( obj == null ) {
				return 0;
			}else if( obj instanceof String ){
				return Integer.parseInt( (String)obj );
			}else if( obj instanceof Integer ){
				return ((Integer) obj ).intValue();
			}else if( obj instanceof BigDecimal ){
				return ((BigDecimal) obj ).intValue();
			}else if( obj instanceof String[] ){
				String[] strArr = (String[])obj;
				return Integer.parseInt( strArr[0] );
			}else if( obj instanceof Long ){
				return ((Long)obj).intValue() ;
			}else{
				return 0;
			}
		}catch(Exception e){
			return 0;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		String key;
		String val;
		Iterator<K> it = dataMap.keySet().iterator();
		while( it.hasNext() ){
			key = (String) it.next();
			val = getString( key );
			sb.append("(").append(key).append(",").append( val  ).append(")");
		}
		sb.append("}");
		return sb.toString();
	}
	
	/**
	 * Map의 key, value를 XML String으로 돌려준다.
	 * <item key1='value1' key2='value2'/>
	 * @return
	 */
	public String toXMLString(){
		return toXMLString("ITEM");
	}
	
	/**
	 * Map의 key, value를 XML String으로 돌려준다.
	 * <nodeName key1='value1' key2='value2'/>
	 * @param nodeName
	 * @return
	 */
	public String toXMLString(String nodeName){
		StringBuilder sb = new StringBuilder();
		sb.append("<"+nodeName);
		String key;
		String val;
		Iterator<K> it = dataMap.keySet().iterator();
		while( it.hasNext() ){
			key = (String) it.next();
			val = getString( key );
			sb.append(" ").append(key.toLowerCase()).append("=\"").append( val  ).append("\"");
		}
		sb.append("/>");
		return sb.toString();
	}
	
	/**
	 * Map의 key, value를 파라미터 XML객체에 Attribute로 세팅해서 돌려준다.
	 * <item key1='value1' key2='value2'/>
	 * @return
	 */
	public void toXMLDocument(Element row){
		String key;
		String val;
		Iterator<K> it = dataMap.keySet().iterator();
		while( it.hasNext() ){
			key = (String) it.next();
			val = getString( key );
			row.setAttribute(key.toLowerCase(), val);
		}
	}
}
