package com.bullnabi.bnb.persistence;

import java.util.List;

import com.bullnabi.bnb.domain.Bnb;
import com.bullnabi.bnb.global.utils.DataMap;

public interface BnbMapper {
	
	List<DataMap> list(Bnb bnb);
	
	int create(Bnb bnb);
	
	int update(Bnb bnb);
	
	int delete(Bnb bnb);
}
