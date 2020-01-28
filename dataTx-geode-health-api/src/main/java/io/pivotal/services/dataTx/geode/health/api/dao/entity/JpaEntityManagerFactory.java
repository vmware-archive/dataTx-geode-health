package io.pivotal.services.dataTx.geode.health.api.dao.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.dbcp2.*;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.PostgreSQL9Dialect;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.jpa.boot.internal.PersistenceUnitInfoDescriptor;

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

    @NonNull
    private Integer batchSize;

    private JpaEntityManagerFactory()
    {

    }

    private JpaEntityManagerFactory(@NonNull String jdbcUrl,
                                    String jdbcDriverClassName,
                                    @NonNull String jdbcUsername,
                                    @NonNull String jdbcPassword,
                                    @NonNull Class[] entityClasses,
                                    Class<? extends Dialect> dialectClass,
                                    @NonNull StatDbType statDbType,
                                    @NonNull Integer batchSize)
    {
        this.jdbcUrl = jdbcUrl;
        this.jdbcDriverClassName = jdbcDriverClassName;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
        this.entityClasses = entityClasses;
        this.dialectClass = dialectClass;
        this.statDbType = statDbType;
        this.batchSize = batchSize;
    }

    public EntityManager getEntityManager() {

        return getEntityManagerFactory().createEntityManager();
    }

    protected EntityManagerFactory getEntityManagerFactory()  {

        try
        {
            Class.forName(this.jdbcDriverClassName);
        }
        catch(ClassNotFoundException e)
        {
            throw new IllegalArgumentException("ClassNotFoundException:"+this.jdbcDriverClassName);
        }

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
        properties.put("hibernate.temp.use_jdbc_metadata_defaults","false");
        properties.put("hbm2ddl.auto","update");
        properties.put("hibernate.show_sql","true");
        properties.put("hibernate.format_sql","true");
        properties.put("hibernate.jdbc.batch_size", batchSize);

        properties.put("hibernate.connection.datasource", getDataSource());

        return properties;
    }

    protected Class[] getEntities() {
        return entityClasses;
    }

    protected DataSource getDataSource() {

        //GenericObjectPool connectionPool = new GenericObjectPool(null);
        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(jdbcUrl,
                jdbcUsername,
                jdbcPassword);
        PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory
                (connectionFactory,
                null);

        ObjectPool<PoolableConnection> connectionPool =
                                 new GenericObjectPool<>(poolableConnectionFactory);

        // Set the factory's pool property to the owning pool
         poolableConnectionFactory.setPool(connectionPool);
         PoolingDataSource<PoolableConnection> dataSource =
               new PoolingDataSource<>(connectionPool);

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
                    this.dialectClass = PostgreSQL9Dialect.class;
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
                    this.statDbType,
                    this.batchSize);
        }
    }//-------------------------------------------
}
