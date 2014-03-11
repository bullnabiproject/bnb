package com.bullnabi.bnb.service;

import java.util.List;

import com.bullnabi.bnb.domain.Bnb;
import com.bullnabi.bnb.global.component.BnbQueryCo;
import com.bullnabi.bnb.global.utils.DataMap;
import com.bullnabi.bnb.persistence.BnbMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BnbService {

	@Autowired
	public BnbMapper bnbMapper;
	
	@Autowired 
    public BnbQueryCo bnbQueryCo;

	public List<DataMap> list(Bnb bnb) {
		
		bnbQueryCo.setJoinSql(bnb);
		
		return bnbMapper.list(bnb);
	}
	
	public int create(Bnb bnb) throws Exception {
		
		bnbQueryCo.setCreateFieldQuiry(bnb);
		bnbQueryCo.setCreateValueQuiry(bnb);
		
		int result = bnbMapper.create(bnb);
		return result;
	}
	
	public int update(Bnb bnb) throws Exception {
		
		bnbQueryCo.setWherePrimaryKey(bnb);
		bnbQueryCo.setUpdateQuiry(bnb);
		
		int result = bnbMapper.update(bnb);
		return result;
	}
	
	public int delete(Bnb bnb) throws Exception {
		
		bnbQueryCo.setWherePrimaryKey(bnb);
		
		int result = bnbMapper.delete(bnb);
		return result;
	}
	
}
