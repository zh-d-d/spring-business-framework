package com.dogbody.spring.framework.sequence.dao;

import com.dogbody.spring.framework.sequence.properties.SequenceProperties;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author zhangdd on 2024/8/6
 */
public class DataSourceDataAccessor extends AbstractDatabaseAccessor implements DataAccessor {

    private final DataSource dataSource;

    public DataSourceDataAccessor(DataSource dataSource, SequenceProperties sequenceProperties) {
        super(sequenceProperties.getTableName());
        this.dataSource = dataSource;
    }

    @Override
    protected Connection getConnection() throws SQLException {

        return dataSource.getConnection();
    }
}
