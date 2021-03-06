/*
 * Copyright (C) 2016-2017 ActionTech.
 * License: http://www.gnu.org/licenses/gpl.html GPL version 2 or higher.
 */

package com.actiontech.dble.sqlengine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MultiRowSQLQueryResultHandler extends OneRawSQLQueryResultHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiRowSQLQueryResultHandler.class);
    // callback  after get ResultSet
    private final SQLQueryResultListener<SQLQueryResult<List<Map<String, String>>>> callback;

    private List<Map<String, String>> resultRows = new LinkedList<>();

    public MultiRowSQLQueryResultHandler(String[] fetchCols,
                                         SQLQueryResultListener<SQLQueryResult<List<Map<String, String>>>> callback) {
        super(fetchCols, null);
        this.callback = callback;
    }

    @Override
    public boolean onRowData(String dataNode, byte[] rowData) {
        super.onRowData(dataNode, rowData);
        resultRows.add(getResult());

        return false;
    }

    @Override
    public void finished(String dataNode, boolean failed) {
        SQLQueryResult<List<Map<String, String>>> queryResult =
                new SQLQueryResult<>(this.resultRows, !failed);
        if (callback != null)
            this.callback.onResult(queryResult);
        else
            LOGGER.info(" callback is null ");
    }
}
