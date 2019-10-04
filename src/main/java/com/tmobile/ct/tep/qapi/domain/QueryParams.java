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
