package ua.ukma.warehouse.datasource;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
@Profile("testRouting")
public class DataSourceRouting extends AbstractRoutingDataSource {

    private final DataSourcePrimaryConfig dataSourcePrimaryConfig;
    private final DataSourceSecondaryConfig dataSourceSecondaryConfig;
    private final DataSourceContextHolder dataSourceContextHolder;

    public DataSourceRouting(DataSourceContextHolder contextHolder, DataSourcePrimaryConfig one, DataSourceSecondaryConfig two) {
        this.dataSourceContextHolder = contextHolder;
        this.dataSourcePrimaryConfig = one;
        this.dataSourceSecondaryConfig = two;

        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(DataSourceEnum.PRIMARY, dataSourceOneDataSource());
        dataSourceMap.put(DataSourceEnum.SECONDARY, dataSourceTwoDataSource());

        this.setTargetDataSources(dataSourceMap);
        this.setDefaultTargetDataSource(dataSourceOneDataSource());
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return dataSourceContextHolder.getBranchContext();
    }

    private DataSource dataSourceOneDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(dataSourcePrimaryConfig.getUrl());
        dataSource.setUsername(dataSourcePrimaryConfig.getUsername());
        dataSource.setPassword(dataSourcePrimaryConfig.getPassword());
        return dataSource;
    }

    private DataSource dataSourceTwoDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(dataSourceSecondaryConfig.getUrl());
        dataSource.setUsername(dataSourceSecondaryConfig.getUsername());
        dataSource.setPassword(dataSourceSecondaryConfig.getPassword());
        return dataSource;
    }
}
