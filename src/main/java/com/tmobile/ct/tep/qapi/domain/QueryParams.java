package com.tmobile.ct.tep.qapi.domain;

import java.util.ArrayList;

public class QueryParams {

    private ArrayList<String> paramsNameList;
    private ArrayList<String> paramsValueList;
    private ArrayList<String> paramsDataTypeList;
    private String sqlPreparedStatement;

    public ArrayList<String> getParamsNameList() {
        return paramsNameList;
    }

    public void setParamsNameList(ArrayList<String> paramsNameList) {
        this.paramsNameList = paramsNameList;
    }

    public ArrayList<String> getParamsValueList() {
        return paramsValueList;
    }

    public void setParamsValueList(ArrayList<String> paramsValueList) {
        this.paramsValueList = paramsValueList;
    }

    public ArrayList<String> getParamsDataTypeList() {
        return paramsDataTypeList;
    }

    public void setParamsDataTypeList(ArrayList<String> paramsDataTypeList) {
        this.paramsDataTypeList = paramsDataTypeList;
    }

    public String getSqlPreparedStatement() {
        return sqlPreparedStatement;
    }

    public void setSqlPreparedStatement(String sqlPreparedStatement) {
        this.sqlPreparedStatement = sqlPreparedStatement;
    }
}
