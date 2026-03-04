package io.github.xiechanglei.cell.starter.jpa.entity;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

/**
 * 运行时创建表的工具类
 *
 * @author xie
 * @date 2024/12/19
 */
@RequiredArgsConstructor
public class CellJpaTableCreator {
    /**
     * 数据源，用于连接数据库。
     */
    private final DataSource dataSource;
    /**
     * JPA 实体管理器工厂构建器，用于构建 {@link LocalContainerEntityManagerFactoryBean} 实例。
     */
    private final EntityManagerFactoryBuilder builder;

    /**
     * 创建rbac下的所有的表
     *
     * @param needCreateEntityPackages rbac下的实体包
     */
    public void createTables(String[] needCreateEntityPackages) {
        if (needCreateEntityPackages == null || needCreateEntityPackages.length == 0) {
            return;
        }
        // 这个地方的目的其实是在建表，但是这个地方是创建了一个新的数据库链接池的方式，有没有更好的方式呢？写到这里，主观认为，如何去建表不应该是这个类的职责，这个类应该是一个工具类，而不是一个业务类
        LocalContainerEntityManagerFactoryBean tempFactoryBean = buildFactoryBean(builder, needCreateEntityPackages, new HibernateJpaVendorAdapter() {{
            setGenerateDdl(true);
            setShowSql(true);
        }});
        tempFactoryBean.destroy();
    }


    /**
     * 构建一个 {@link LocalContainerEntityManagerFactoryBean} 实例。
     * <p>
     * 根据提供的 EntityManagerFactoryBuilder、实体扫描包和 JPA Vendor Adapter 配置创建一个新的 factoryBean。
     * </p>
     *
     * @param builder               构建 JPA 实体管理器工厂的构建器。
     * @param allEntityScanPackages 实体扫描包列表。
     * @param jpaVendorAdapter      JPA Vendor Adapter 配置。
     * @return 配置好的 {@link LocalContainerEntityManagerFactoryBean} 实例。
     */
    private LocalContainerEntityManagerFactoryBean buildFactoryBean(EntityManagerFactoryBuilder builder, String[] allEntityScanPackages, JpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean factoryBean = builder.dataSource(dataSource).persistenceUnit("default").build();
        factoryBean.setPackagesToScan(allEntityScanPackages);
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.afterPropertiesSet();
        return factoryBean;
    }
}
