package ua.ukma.warehouse.datasource;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Profile("testRouting")
public class DataSourceContextHolder {
    private static ThreadLocal<DataSourceEnum> threadLocal;

    public DataSourceContextHolder() {
        threadLocal = new ThreadLocal<>();
    }

    public void setBranchContext(DataSourceEnum dataSourceEnum) {
        threadLocal.set(dataSourceEnum);
    }

    public DataSourceEnum getBranchContext() {
        return threadLocal.get();
    }

    public static void clearBranchContext() {
        threadLocal.remove();
    }
}
