package com.bullnabi.bnb.service;

import java.util.concurrent.ConcurrentHashMap;


import org.springframework.stereotype.Service;

import com.bullnabi.bnb.domain.RestDTO;

@Service("restService")
public class RestService {

    private final ConcurrentHashMap<Long, RestDTO> dtoMap = new ConcurrentHashMap<Long, RestDTO>();
    
    
    public RestDTO findRest(long id) {
          
           return dtoMap.get(id);
    }
   
   
    public RestDTO createRest(long id, String name, String job) {
          
           return dtoMap.put(id, new RestDTO(id, name, job));
    }
   
   
    public RestDTO updateRest(long id, String name, String job) {
          
           return dtoMap.replace(id, new RestDTO(id, name, job));
    }
   
   
    public RestDTO deleteRest(long id) {
          
           return dtoMap.remove(id);
    }
}
