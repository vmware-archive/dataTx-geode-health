package io.pivotal.services.dataTx.geode.health.api.dao;

import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import io.pivotal.services.dataTx.geode.health.api.dao.entity.JpaEntityManagerFactory;
import io.pivotal.services.dataTx.geode.health.api.dao.entity.StatDbType;
import io.pivotal.services.dataTx.geode.health.api.dao.entity.StatEntity;
import nyla.solutions.core.util.Config;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Random;


//@EnableJpaRepositories
//@EnableTransactionManagement
//@RunWith(SpringRunner.class)
//@ComponentScan(basePackages = "io.pivotal.services.dataTx.geode.gART.api")
@ContextConfiguration(classes = StatDaoTest.class)
@Configuration
@SpringBootTest
public class StatDaoTest
{
    private FakeValuesService fakerService = new FakeValuesService(
            Locale.ENGLISH,new RandomService());

    @Test
    public void verify_dao_can_write_to_databage()
    {


        String statName ="Stat";
        LocalDateTime timestamp = LocalDateTime.now();

        StatEntity expected =  StatEntity
                .builder()
                .statName(statName)
                .statTimestamp(timestamp)
                .build();

        assertTrue(expected.getId() == 0);

        String jdbcUrl = "jdbc:h2:~/test";
        String jdbcUsername = "sa";
        String jdbcPassword = "";

        JpaEntityManagerFactory factory = getJpaEntityManagerFactory(
                jdbcUrl,
                jdbcUsername,
                jdbcPassword,
                StatDbType.H2);

        assertNotNull(factory.getEntityClasses());
        EntityManager em = factory.getEntityManager();

        StatDao dao = StatDao.builder()
                .entityManager(em)
                .build();

        StatEntity actual = dao.save(expected);

        StatEntity readActual = dao
                                .findStatById(
                                        actual.getId()).get();

        assertEquals(expected,readActual);

        dao.deleteStat(expected);

        expected = dao.findStatById(expected.getId()).orElse(null);
        assertNull(expected);

    }

    @Test
    @EnabledIfSystemProperty(named = "testMode", matches = "INT")
    public void integration_test()
    {
        Faker faker = new Faker(new Random());
        String jdbcUrl = Config.getProperty("jdbcUrl");
        String jdbcUsername = Config.getProperty("jdbcUsername");
        String jdbcPassword = Config.getProperty("jdbcPassword");

        JpaEntityManagerFactory factory = getJpaEntityManagerFactory(
                jdbcUrl,
                jdbcUsername,
                jdbcPassword,
                StatDbType.PostgresDB);

        assertNotNull(factory.getEntityClasses());
        EntityManager em = factory.getEntityManager();

        StatDao dao = StatDao.builder()
                .entityManager(em)
                .build();

        StatEntity expected = new StatEntity();
        expected.setFilterTypeName("expected");
        expected.setLabel(faker.bothify("????label"));
        expected.setMachine(faker.company().name());
        expected.setStatName(faker.bothify("???"));
        expected.setStatTimestamp(LocalDateTime.now());
        expected.setStatValue(faker.number().randomDouble(3,1,555));

        expected = dao.save(expected);

        assertTrue( expected.getId() > 0,"id > 0");

        StatEntity actual = dao.findStatById(expected.getId()).get();
        assertEquals(expected,actual);


        //insert another
        expected.setId(23);
        dao.save(expected);


    }

    private JpaEntityManagerFactory getJpaEntityManagerFactory(String jdbcUrl, String jdbcUsername,
                                                               String jdbcPassword,
                                                               StatDbType statDbType)
    {
        JpaEntityManagerFactory.JpaEntityManagerFactoryBuilder builder =  JpaEntityManagerFactory.builder()
                .statDbType(statDbType)
                .jdbcUsername(jdbcUsername)
                .jdbcPassword(jdbcPassword)
                .jdbcUrl(jdbcUrl)
                .entityClasses(
                new Class[]{StatEntity.class});
        return builder.build();
    }
}