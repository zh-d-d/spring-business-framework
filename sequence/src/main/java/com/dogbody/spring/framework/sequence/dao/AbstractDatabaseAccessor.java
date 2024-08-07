package com.dogbody.spring.framework.sequence.dao;

import com.dogbody.spring.framework.sequence.SequenceDefinition;
import com.dogbody.spring.framework.sequence.exception.SequenceException;

import java.sql.*;
import java.util.Optional;

/**
 * @author zhangdd on 2024/8/6
 */
public abstract class AbstractDatabaseAccessor implements DataAccessor {

    private final static String CREATE_TABLE_DDL = "CREATE TABLE IF NOT EXISTS $TABLE_NAME(\n" +
            "  id BIGINT NOT NULL AUTO_INCREMENT,\n" +
            "  `name` VARCHAR(100) NOT NULL COMMENT '业务编码名称',\n" +
            "  `key` VARCHAR(50) NOT NULL COMMENT '业务编码',\n" +
            "  initial_value BIGINT NOT NULL COMMENT '初始值' DEFAULT 0,\n" +
            "  step INT NOT NULL COMMENT '步长' DEFAULT 1,\n" +
            "  cache_size INT NOT NULL COMMENT '缓存大小' DEFAULT 50,\n" +
            "  cache_mode ENUM('MEMORY',\n" +
            "'REDIS') NOT NULL COMMENT '缓存模式' DEFAULT 'MEMORY',\n" +
            "  `number` BIGINT NOT NULL COMMENT '序列号当前最大值' DEFAULT 0,\n" +
            "  create_time DATETIME NOT NULL DEFAULT current_timestamp,\n" +
            "  modify_time DATETIME NOT NULL DEFAULT current_timestamp,\n" +
            "  PRIMARY KEY(id)\n" +
            ") COMMENT '序列号生成器记录表';";

    //CREATE UNIQUE INDEX uk_key ON $TABLE_NAME(`key`);

    private final static String FIND_DEFINITION_USE_KEY = "select name, `key`, initial_value, step, cache_size, cache_mode from $TABLE_NAME where `key` = ?";

    private final static String INSERT_DEFINITION = "insert into $TABLE_NAME(name, `key`, initial_value, step, cache_size, cache_mode, number) values(?, ?, ?, ?, ?, ?,?)";

    private final static String UPDATE_DEFINITION = "update $TABLE_NAME set initial_value=?,step=?,cache_size=?,cache_mode=? where `key`=?";

    private final static String SELECT_NUM_FOR_UPDATE = "select number from $TABLE_NAME where `key`=? for update";

    private final static String UPDATE_NUMBER = "update $TABLE_NAME set number=? where number=? and `key`=?";

    protected abstract Connection getConnection() throws SQLException;

    private final String tableName;

    public AbstractDatabaseAccessor(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public void init() {
        //创建数据库表
        try (Connection connection = getConnection()){
            String createTableSql = CREATE_TABLE_DDL.replace("$TABLE_NAME", tableName);
            PreparedStatement preparedStatement = connection.prepareStatement(createTableSql);
            preparedStatement.execute();
        } catch (Exception e) {
            final String msg = String.format("fail to execute create table %s ", tableName);
            throw new SequenceException(msg, e);
        }
    }

    @Override
    public Optional<SequenceDefinition> find(String key) {
        String sql = FIND_DEFINITION_USE_KEY.replace("$TABLE_NAME", tableName);
        try (Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, key);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString(1);
                long initial = resultSet.getLong(3);
                int step = resultSet.getInt(4);
                int cacheSize = resultSet.getInt(5);
                String cacheMode = resultSet.getString(6);
                return Optional.of(new SequenceDefinition(name, key, initial, step, cacheSize, cacheMode));
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            final String msg = String.format("fail to find definition of sequence %s", key);
            throw new SequenceException(msg, e);
        }
    }

    @Override
    public void insert(SequenceDefinition definition) {
        String sql = INSERT_DEFINITION.replace("$TABLE_NAME", tableName);
        try (Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, definition.getName());
            statement.setString(2, definition.getKey());
            statement.setLong(3, definition.getInitialValue());
            statement.setLong(4, definition.getStep());
            statement.setLong(5, definition.getCacheSize());
            statement.setString(6, definition.getCacheMode());
            statement.setLong(7, definition.getInitialValue());
            statement.execute();
        } catch (SQLIntegrityConstraintViolationException e) {
            //do nothing because sequence already exists
        } catch (Exception e) {
            final String msg = String.format("fail to insert sequence %s", definition.toString());
            throw new SequenceException(msg, e);
        }
    }

    @Override
    public void update(SequenceDefinition definition) {
        String sql = UPDATE_DEFINITION.replace("$TABLE_NAME", tableName);
        try (Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, definition.getInitialValue());
            statement.setInt(2, definition.getStep());
            statement.setInt(3, definition.getCacheSize());
            statement.setString(4, definition.getCacheMode());
            statement.setString(5, definition.getKey());
            statement.execute();
        } catch (Exception e) {
            final String msg = String.format("fail to update sequence %s", definition.toString());
            throw new SequenceException(msg, e);
        }
    }

    @Override
    public long grow(SequenceDefinition definition) {
        try (Connection connection = getConnection()){
            connection.setAutoCommit(false);
            long oldNumber = getNumberForUpdate(definition.getKey());
            long newNumber = oldNumber + (long) definition.getStep() * definition.getCacheSize();
            updateNumber(definition.getKey(), oldNumber, newNumber);
            connection.commit();
            return newNumber;
        } catch (Exception e) {
            final String msg = String.format("fail to load number of sequence %s", definition.toString());
            throw new SequenceException(msg, e);
        }
    }


    private long getNumberForUpdate(String key) {
        String sql = SELECT_NUM_FOR_UPDATE.replace("$TABLE_NAME", tableName);
        try (Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, key);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            } else {
                final String msg = String.format("fail to get current number of sequence %s", key);
                throw new SequenceException(msg);
            }
        } catch (Exception e) {
            final String msg = String.format("fail to get current number of sequence %s", key);
            throw new SequenceException(msg, e);
        }
    }

    private void updateNumber(String key, Long oldNumber, Long newNumber) {
        String sql = UPDATE_NUMBER.replace("$TABLE_NAME", tableName);
        try (Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, newNumber);
            statement.setLong(2, oldNumber);
            statement.setString(3, key);
            statement.executeUpdate();
        } catch (Exception e) {
            final String msg = String.format("fail to update sequence %s", key);
            throw new SequenceException(msg, e);
        }
    }

}
