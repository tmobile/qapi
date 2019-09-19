package com.tmobile.ct.tep.qapi.db;


import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.ColumnDefinitions;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tmobile.ct.tep.qapi.datasource.RoutingDataSourceConfiguration;
import com.tmobile.ct.tep.qapi.domain.QueryParams;
import com.tmobile.ct.tep.qapi.domain.ReturnObject;
import com.tmobile.ct.tep.qapi.exceptions.ApiFailureException;
import com.tmobile.ct.tep.qapi.exceptions.DbConnectionException;
import com.tmobile.ct.tep.qapi.exceptions.TvaultConfigurationException;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class DbData {

    static Logger logger = LoggerFactory.getLogger(DbData.class);


    @Autowired
    RoutingDataSourceConfiguration routingDataSourceConfiguration;

    public DbData() {
    }

    public ReturnObject get(QueryParams queryParams, String env, String l1) {
        List result = null;
        Object type;
        try {
            type = routingDataSourceConfiguration.getConnection(env, l1);
        } catch (ApiFailureException e) {
            return new ReturnObject(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (DbConnectionException e) {
            return new ReturnObject(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (TvaultConfigurationException e){
            return new ReturnObject(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        JdbcTemplate jdbcTemplate;
        Session session;
        if (type instanceof JdbcTemplate) {
            jdbcTemplate = (JdbcTemplate) type;
            try {
                result = extractResultJdbcQuery(jdbcTemplate, queryParams);
            } catch (Exception e) {
                try {
                    jdbcTemplate = (JdbcTemplate) routingDataSourceConfiguration.reconnect(env, l1);
                    if (jdbcTemplate.getDataSource().getConnection().isValid(10)) {
                        result = extractResultJdbcQuery(jdbcTemplate, queryParams);
                    } else {
                        return new ReturnObject("Failed to connect to database for: " + env, HttpStatus.BAD_REQUEST);
                    }
                } catch (SQLException ex) {
                    return new ReturnObject("Failed to connect to database for: " + env, HttpStatus.BAD_REQUEST);
                }
            }
        } else if (type instanceof Session) {
            session = (Session) type;
            com.datastax.driver.core.PreparedStatement preparedStatement = session.prepare(queryParams.getSqlPreparedStatement());
            BoundStatement boundStatement = preparedStatement.bind();
            for (int i = 0; i < queryParams.getParamsValueList().size(); i++) {                     //pass values from paramsValueList into the prepared statement
                //datastax needs to know data type to map to cql data types
                String dataType = queryParams.getParamsDataTypeList().get(i).toUpperCase();
                String value = queryParams.getParamsValueList().get(i);
                if (dataType.equals("BIGINT")) {
                    boundStatement = boundStatement.setLong(i, Long.parseLong(value));
                } else if (dataType.equals("INT") || dataType.equals("INTEGER")) {
                    boundStatement = boundStatement.setInt(i, Integer.parseInt(value));
                } else if (dataType.equals("SMALLINT")) {
                    boundStatement = boundStatement.setInt(i, Short.parseShort(value));
                } else if (dataType.equals("TINYINT")) {
                    boundStatement = boundStatement.setInt(i, Byte.parseByte(value));
                } else if (dataType.equals("DEC") || dataType.equals("DECIMAL") ||
                    dataType.equals("NUMERIC")) {
                    boundStatement = boundStatement.setDecimal(i, BigDecimal.valueOf(Long.parseLong(value)));
                } else if (dataType.equals("REAL")) {
                    boundStatement = boundStatement.setFloat(i, Float.parseFloat(value));
                } else if (dataType.equals("FLOAT") || dataType.equals("DOUBLE")) {
                    boundStatement = boundStatement.setDouble(i, Double.valueOf(value));
                } else if (dataType.equals("CHAR") || dataType.equals("VARCHAR")) {
                    boundStatement = boundStatement.setString(i, value);
                }
            }
            result = extraCassandraData(session.execute(boundStatement));
        }
        logger.info("SQL statement executed.");
        return new ReturnObject(result, HttpStatus.OK);
    }

    private List extractResultJdbcQuery(JdbcTemplate jdbcTemplate, QueryParams queryParams) {
        return jdbcTemplate.query(queryParams.getSqlPreparedStatement(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement) throws SQLException {
                for (int i = 1; i <= queryParams.getParamsNameList().size(); i++) {                     //pass values from paramsValueList into the prepared statement
                    preparedStatement.setObject(i, queryParams.getParamsValueList().get(i - 1));
                }
            }
        }, new ResultSetExtractor<List>() {
            @Override
            public List extractData(ResultSet resultSet) throws SQLException,       //extract data from result set onto a list
                DataAccessException {
                List result = new ArrayList();
                ResultSetMetaData md = resultSet.getMetaData();
                int columns = md.getColumnCount();
                while (resultSet.next()) {
                    HashMap row = new HashMap(columns);
                    for (int i = 1; i <= columns; i++) {
                        if (resultSet.getObject(i) instanceof oracle.sql.BLOB) {
                            String blobString = handleBlob((oracle.sql.BLOB) resultSet.getBlob(i));
                            try {
                                ObjectMapper mapper = new ObjectMapper();
                                row.put(md.getColumnName(i), mapper.readValue(blobString, JSONObject.class));
                            } catch (JsonParseException e) {
                                logger.error("Error : [{}]", e.getMessage());
                            } catch (JsonMappingException e) {
                                logger.error("Error in Json Mapping: [{}]", e.getMessage());
                            } catch (IOException e) {
                                logger.error("IOException thrown for: [{}]", e.getMessage());
                            }
                        } else {
                            row.put(md.getColumnName(i), resultSet.getObject(i));
                        }
                    }
                    result.add(row);
                }
                return result;
            }
        });
    }

    private List extraCassandraData(com.datastax.driver.core.ResultSet resultSet) {

        List result = new ArrayList();
        List<ColumnDefinitions.Definition> columns = resultSet.getColumnDefinitions().asList();
        List<Row> rows = resultSet.all();
        for (int i = 0; i < rows.size(); i++) {
            HashMap row = new HashMap();
            for (int j = 0; j < columns.size(); j++) {
                String column = columns.get(j).getName();
                row.put(column, rows.get(i).getObject(j));
            }
            result.add(row);
        }
        return result;
    }

    private String handleBlob(oracle.sql.BLOB blob) throws SQLException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int length;
        InputStream in = blob.getBinaryStream();
        try {
            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
        } catch (IOException e) {
            logger.error("Could not read blob.");
        }
        try {
            String blobString = out.toString(StandardCharsets.UTF_8.name());
            return blobString;
        } catch (UnsupportedEncodingException e) {
            logger.error("Could not encode blob: [{}] ", e.getMessage());
        } catch (IOException e) {
            logger.error("IOException thrown for reason: [{}]", e.getMessage());
        }
        return null;
    }

}
