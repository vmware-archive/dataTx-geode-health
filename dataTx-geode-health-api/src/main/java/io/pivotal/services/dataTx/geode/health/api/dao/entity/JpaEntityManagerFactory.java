package io.pivotal.services.dataTx.geode.health.api.dao.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.PostgresPlusDialect;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

@Builder
@Setter
public class JpaEntityManagerFactory
{
    private JpaEntityManagerFactory()
    {

    }

    private JpaEntityManagerFactory(@NonNull String jdbcUrl,
                                    String jdbcDriverClassName,
                                    @NonNull String jdbcUsername,
                                    @NonNull String jdbcPassword,
                                    @NonNull Class[] entityClasses,
                                    Class<? extends Dialect> dialectClass,
                                    @NonNull StatDbType statDbType)
    {
        this.jdbcUrl = jdbcUrl;
        this.jdbcDriverClassName = jdbcDriverClassName;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
        this.entityClasses = entityClasses;
        this.dialectClass = dialectClass;
        this.statDbType = statDbType;
    }

    //= "jdbc:mysql://databaseurl"
    @NonNull
    private  String jdbcUrl;

    private  String jdbcDriverClassName;

    @NonNull
    private  String jdbcUsername;

    @NonNull
    private  String jdbcPassword;

    @NonNull
    @Getter
    private  Class[] entityClasses;

    private Class<? extends Dialect> dialectClass;

    @NonNull
    private StatDbType statDbType;


    public EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    protected EntityManagerFactory getEntityManagerFactory() {
        PersistenceUnitInfo persistenceUnitInfo = getPersistenceUnitInfo(
                getClass().getSimpleName());
        Map<String, Object> configuration = new HashMap<>();
        return new EntityManagerFactoryBuilderImpl(
                new PersistenceUnitInfoDescriptor(persistenceUnitInfo), configuration)
                .build();
    }
    protected List<String> getEntityClassNames() {
        return Arrays.asList(getEntities())
        .stream()
          .map(Class::getName)
          .collect(Collectors.toList());
    }

    protected Properties getProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", dialectClass.getName());
        properties.put("hibernate.id.new_generator_mappings", false);
        properties.put("hibernate.hbm2ddl.auto","create");
        properties.put("hbm2ddl.auto","create");
        //properties.put("javax.persistence.schema-generation.database.action","create");

        properties.put("hibernate.connection.datasource", getDataSource());

        return properties;
    }

    protected Class[] getEntities() {
        return entityClasses;
    }

    protected DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(this.jdbcDriverClassName);
        dataSource.setUsername(this.jdbcUsername);
        dataSource.setPassword(this.jdbcPassword);
        dataSource.setUrl(this.jdbcUrl);
        return dataSource;

    }

    protected HibernatePersistenceUnitInfo getPersistenceUnitInfo(String name) {
        return new HibernatePersistenceUnitInfo(name, getEntityClassNames(), getProperties());
    }

    public static class JpaEntityManagerFactoryBuilder
    {
        public JpaEntityManagerFactory build(){

            switch(this.statDbType)
            {
                case PostgresDB:
                    this.dialectClass = PostgresPlusDialect.class;
                    this.jdbcDriverClassName = "org.postgresql.Driver";
                    break;
                case H2:
                    this.dialectClass = H2Dialect.class;
                    this.jdbcDriverClassName = "org.h2.Driver";
                    break;
                default:
                    throw new IllegalArgumentException("statDbType not support:"+statDbType);
            }


            this.entityClasses  =new Class[]{StatEntity.class};

            return new JpaEntityManagerFactory(this.jdbcUrl,
                    this.jdbcDriverClassName,
                    this.jdbcUsername,
                    this.jdbcPassword,
                    this.entityClasses,
                    this.dialectClass,
                    this.statDbType);
        }
    }//-------------------------------------------
}
