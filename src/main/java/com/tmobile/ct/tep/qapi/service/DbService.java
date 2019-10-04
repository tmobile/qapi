/*
 * // =========================================================================
 * // Copyright © 2019 T-Mobile USA, Inc.
 * //
 * // Licensed under the Apache License, Version 2.0 (the "License");
 * // you may not use this file except in compliance with the License.
 * // You may obtain a copy of the License at
 * //
 * //    http://www.apache.org/licenses/LICENSE-2.0
 * //
 * // Unless required by applicable law or agreed to in writing, software
 * // distributed under the License is distributed on an "AS IS" BASIS,
 * // WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * // See the License for the specific language governing permissions and
 * // limitations under the License.
 * // =========================================================================
 *
 */

package com.tmobile.ct.tep.qapi.service;

import com.tmobile.ct.tep.qapi.config.ApplicationProperties;
import com.tmobile.ct.tep.qapi.db.DbData;
import com.tmobile.ct.tep.qapi.domain.QueryParams;
import com.tmobile.ct.tep.qapi.domain.ReturnObject;
import com.tmobile.ct.tep.qapi.exceptions.L2IdentifierNotFoundException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class DbService {

    static Logger logger = LoggerFactory.getLogger(DbService.class);
    @Autowired
    private DbData data;

    @Autowired
    private RepoService repoService;

    @Autowired
    private ApplicationProperties applicationProperties;

    public DbService(){
    }


    public ReturnObject get(JSONObject j, String l1, String l2, String env){
        HashMap<String,String > values;
        try {
            values = parseConfig(l1, l2, env);
        }catch (FileNotFoundException e){
            logger.error("Config file not found");
            return new ReturnObject("Config File not found", HttpStatus.BAD_REQUEST);
        }catch (IOException e){
            logger.error("IO Exception thrown");
            return new ReturnObject("IOException", HttpStatus.BAD_REQUEST);
        }catch (ParseException e){
            logger.error("Error parsing through JSON config");
            return new ReturnObject("Error parsing through the config file", HttpStatus.BAD_REQUEST);
        }catch (L2IdentifierNotFoundException e){
            logger.error("L2 identifier not found in the config file");
            return new ReturnObject("L2 identifier not found in the config file", HttpStatus.BAD_REQUEST);
        }

        String sqlconfig = values.get("sqlconfig");
        QueryParams params = getParams(sqlconfig, j);
        logger.info("Service calling DbData");
        return data.get(params, values.get("env"), l1);
    }


    /**
     * Parses through the correct config file, based  ont the identifiers
     * @param l1 the config file name
     * @param l2 the sql statement name
     * @return Hashmap with the values "sqlconfig" and "database"
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ParseException
     * @throws L2IdentifierNotFoundException
     */
    public HashMap<String, String> parseConfig(String l1, String l2, String env) throws FileNotFoundException, IOException, ParseException, L2IdentifierNotFoundException
    {
        String sqlconfig;
        JSONParser parser = new JSONParser();
        Object object;
        logger.info("Before accessing config file.");
        File file = null;
        if (applicationProperties.getQueryConfig().getLocation().toLowerCase().trim().equals("local")){
            file = new File(getClass().getClassLoader().getResource("queryconfigs/" + l1 + ".json").getFile());
        }
        else if (applicationProperties.getQueryConfig().getLocation().toLowerCase().trim().equals("repository")){
            file = new File(repoService.getRepoDir() +File.separator + applicationProperties.getQueryConfig().getRepoRoot() + File.separator + l1 + ".json");
        }

        if(file.exists()){
            FileReader fileReader = new FileReader(file);
            object = parser.parse(fileReader);
            fileReader.close();
        }
        else {
            throw new FileNotFoundException("The config file: " +l1 +" was not found. ");
        }
        JSONObject jsonObject = (JSONObject) object;
        JSONObject statements = (JSONObject) jsonObject.get("statements");
        String defaultenv = (String) jsonObject.get("default_env");
        sqlconfig = (String) statements.get(l2);
        if (sqlconfig == null)
        {
            throw new L2IdentifierNotFoundException("L2 identifier not found in the config file");
        }
        HashMap<String,String> values = new HashMap<>();
        values.put("sqlconfig",sqlconfig);
        if (env == null)
        {
            values.put("env", defaultenv);
        }
        else
        {
            values.put ("env", env);
        }
        logger.info("Finished parsing through the config file");
        return values;
    }

    /**
     * Gets parameters names from sqlconfig, and gets the values from the jsonboject based on the names
     * @param sqlconfig the sql statement
     * @param j the jsonbody
     * @return Hashmap with of arraylist with values "names", and "values"
     */
    public QueryParams getParams(String sqlconfig, JSONObject j)
    {
        ArrayList<String> paramsNameList = new ArrayList<>();
        ArrayList paramsValueList = new ArrayList<>();
        ArrayList<String> paramsDataTypeList = new ArrayList<>();

        Pattern p = Pattern.compile("\\{.*?\\}");
        Matcher m = p.matcher(sqlconfig);
        while(m.find())                             //add param names between {} into paramsNameList
        {
            String value = m.group();
            String parts[] = value.split("::");
            if (parts.length > 1){
                value = "{" + parts[1];
                paramsDataTypeList.add(parts[0].substring(1));
            }
            paramsNameList.add(value);
        }

        for (int i =0;i<paramsNameList.size();i++){         //get values from JSON body, add them to paramsValueList
            String temp = paramsNameList.get(i);
            temp = temp.substring(1,temp.length()-1);
            paramsValueList.add(j.get(temp));
        }
        if (paramsDataTypeList.size() > 0){
            for (int i =0;i<paramsNameList.size();i++){
                String replace = "{" +paramsDataTypeList.get(i) +"::" +paramsNameList.get(i).substring(1);
                sqlconfig = sqlconfig.replace(replace,"?");   //prepare sqlconfig for PreparedStatement
            }
        }
        for (int i =0;i<paramsNameList.size();i++){
            sqlconfig = sqlconfig.replace(paramsNameList.get(i),"?");   //prepare sqlconfig for PreparedStatement
        }
        QueryParams queryParams = new QueryParams();
        queryParams.setParamsNameList(paramsNameList);
        queryParams.setParamsValueList(paramsValueList);
        queryParams.setParamsDataTypeList(paramsDataTypeList);
        queryParams.setSqlPreparedStatement(sqlconfig);

        return queryParams;
    }

}
