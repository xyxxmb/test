package com.buptyouth.mybatis.util;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;


public class MultipleDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceHolder.getDataSources();
    }
}