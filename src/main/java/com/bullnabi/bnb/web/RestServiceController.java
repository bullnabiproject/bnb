package com.bullnabi.bnb.web;

import com.bullnabi.bnb.domain.RestDTO;
import com.bullnabi.bnb.service.RestService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

//http://odysseymoon.tistory.com/46

@Controller
@RequestMapping("/restTest")
public class RestServiceController {
       
        final static Logger logger = LoggerFactory.getLogger(RestServiceController.class);
       
        @Autowired
        private RestService restService;
       
        @RequestMapping(value="/{id}", method=RequestMethod.GET)
        @ResponseBody
        public RestDTO getRest(@PathVariable("id") long id) {
               logger.debug("###get!!");
               return restService.findRest(id);
        }
       
        @RequestMapping(value="/{id}/{name}/{job}", method=RequestMethod.POST)
        @ResponseBody
        public RestDTO putRest(@PathVariable("id") long id, @PathVariable("name") String name,
                       @PathVariable("job") String job) {
               return restService.createRest(id, name, job);
        }
       
        @RequestMapping(value="/{id}/{name}/{job}", method=RequestMethod.PUT)
        @ResponseBody
        public RestDTO updateRest(@PathVariable("id") long id, @PathVariable("name") String name,
                       @PathVariable("job") String job) {
               return restService.updateRest(id, name, job);
        }
       
        @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
        @ResponseBody
        public RestDTO deleteRest(@PathVariable("id") long id) {
               return restService.deleteRest(id);
        }
 
}
