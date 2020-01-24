package io.pivotal.services.dataTx.gART.api.dao;

import io.pivotal.services.dataTx.gART.api.dao.entity.JpaEntityManagerFactory;
import io.pivotal.services.dataTx.gART.api.dao.entity.StatDbType;
import io.pivotal.services.dataTx.gART.api.dao.entity.StatEntity;
import org.hibernate.dialect.H2Dialect;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;


//@EnableJpaRepositories
//@EnableTransactionManagement
@RunWith(SpringRunner.class)
@ComponentScan(basePackages = "io.pivotal.services.dataTx.gART.api")
@ContextConfiguration(classes = StatDaoTest.class)
@Configuration
public class StatDaoTest
{


    @Test
    public void verify_dao_can_write_to_databage()
    {

        String jdbcUrl = "jdbc:h2:~/test";
        String jdbcDriverClassName = org.h2.Driver.class.getName();
        String jdbcUsername = "sa";
        String jdbcPassword = "";

        String statName ="Stat";
        LocalDateTime timestamp = LocalDateTime.now();

        StatEntity expected =  StatEntity
                .builder()
                .statName(statName)
                .statTimestamp(timestamp)
                .build();

        Assert.assertTrue(expected.getId() == 0);

        JpaEntityManagerFactory.JpaEntityManagerFactoryBuilder builder =  JpaEntityManagerFactory.builder()
                .statDbType(StatDbType.H2)
                .jdbcUsername(jdbcUsername)
                .jdbcPassword(jdbcPassword)
                .jdbcUrl(jdbcUrl)
                .entityClasses(
                new Class[]{StatEntity.class});

        JpaEntityManagerFactory factory = builder.build();

        Assert.assertNotNull(factory.getEntityClasses());
        EntityManager em = factory.getEntityManager();

        StatDao dao = StatDao.builder()
                .entityManager(em)
                .build();

        StatEntity actual = dao.save(expected);

        StatEntity readActual = dao
                                .findStatById(
                                        actual.getId()).get();

        Assert.assertEquals(expected,readActual);

        dao.deleteStat(expected);

        expected = dao.findStatById(expected.getId()).orElse(null);
        Assert.assertNull(expected);

    }
}