package com.tmobile.ct.tep.qapi.web.rest;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.tmobile.ct.tep.qapi.datasource.RoutingDataSourceConfiguration;
import com.tmobile.ct.tep.qapi.domain.ReturnObject;
import com.tmobile.ct.tep.qapi.service.DbService;
import com.tmobile.ct.tep.qapi.service.RepoService;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/api/datareader")
public class Controller {

    @Autowired
    DbService dbService;

    @Autowired
    RepoService repoService;

    @Autowired
    RoutingDataSourceConfiguration routingDataSourceConfiguration;

    static Logger logger = LoggerFactory.getLogger(Controller.class);


    @PostMapping(value= "/{L1identifier}/{L2identifier}/v1", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity post(@RequestBody JSONObject body, @PathVariable String L1identifier, @PathVariable String L2identifier){
        ReturnObject result;
        logger.info("Post Request method called");
        result = dbService.get(body,L1identifier, L2identifier, null);
        ResponseEntity r = new ResponseEntity(result,result.getStatus());
        logger.info("Post Response sent");
        return r;
    }

    @PostMapping(value= "/{L1identifier}/{L2identifier}/v1/**", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity post(@RequestBody JSONObject body, @PathVariable String L1identifier, @PathVariable String L2identifier, HttpServletRequest request){
        ReturnObject result;
        String env = getResultWithEnv(request);
        result = dbService.get(body,L1identifier, L2identifier, env);
        ResponseEntity r = new ResponseEntity(result,result.getStatus());
        logger.info("Post Response sent");
        return r;
    }

    @PostMapping(value= "/{L1identifier}/{L2identifier}/v2", produces= MediaType.APPLICATION_JSON_VALUE)
    public List post2(@RequestBody JSONObject body, @PathVariable String L1identifier, @PathVariable String L2identifier){
        ReturnObject result;
        logger.info("Post Request method called");
        result = dbService.get(body,L1identifier, L2identifier, null);
        logger.info("Post Response sent");
        return result.getResult();
    }

    @PostMapping(value= "/{L1identifier}/{L2identifier}/v2/**", produces= MediaType.APPLICATION_JSON_VALUE)
    public List post2(@RequestBody JSONObject body, @PathVariable String L1identifier, @PathVariable String L2identifier, HttpServletRequest request){
        ReturnObject result;
        String env = getResultWithEnv(request);
        result = dbService.get(body,L1identifier, L2identifier, env);
        logger.info("Post Response sent");
        return result.getResult();
    }

    @PostMapping(value= "/{L1identifier}/{L2identifier}/refresh/**", produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity refresh(@RequestBody JSONObject body, @PathVariable String L1identifier, @PathVariable String L2identifier, HttpServletRequest request){
        ReturnObject result = new ReturnObject();
        String env = getResultWithEnv(request);
        String l1_env = L1identifier +"/" +env;
        logger.info("Refreshing connection: " +l1_env);
        routingDataSourceConfiguration.remove(l1_env);
        result.setMessage("Connection succesfully refreshed.");
        ResponseEntity r = new ResponseEntity("Connection succesfully refreshed.", HttpStatus.OK);
        logger.info("Post Response sent");
        return r;
    }

    @PostMapping(value="/scan")
    public ResponseEntity scan(){
        repoService.scan();
        ResponseEntity responseEntity = new ResponseEntity("Latest repo scanned.", HttpStatus.OK);
        return responseEntity;
    }


    private String getResultWithEnv(HttpServletRequest request)
    {
        logger.info("Post Request method called");
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String ) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        AntPathMatcher apm = new AntPathMatcher();
        return apm.extractPathWithinPattern(bestMatchPattern, path);

    }

    /**
     * Allows for some fields to be null in the response
     * @return
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MappingJackson2HttpMessageConverter converter =
                new MappingJackson2HttpMessageConverter(mapper);
        return converter;
    }

}
