package io.pivotal.services.dataTx.gART.api.dao;

import com.zaxxer.hikari.HikariConfig;
import io.pivotal.services.dataTx.gART.api.dao.entity.StatEntity;
import lombok.Builder;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.transaction.Transaction;
import java.util.Optional;

/**
 * @author Gregory Green
 */
@Builder
@Setter
public class StatDao
{
    private final  EntityManager entityManager;

    public StatDao(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    public StatEntity save(StatEntity stat)
    {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        StatEntity managed= entityManager.merge(stat);
        entityManager.flush();
        transaction.commit();
        return  managed;
    }


    public Optional<StatEntity> findStatById(long id)
    {
        StatEntity stat = this.entityManager.find(StatEntity.class,id);
        if(stat ==null)
            return Optional.empty();

        this.entityManager.detach(stat);

        return Optional.of(stat);

    }//-------------------------------------------

    public void deleteStat(StatEntity statEntity)
    {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        StatEntity stat =  this.entityManager.find(StatEntity.class,statEntity.getId());
        this.entityManager.remove(stat);
        this.entityManager.flush();
        transaction.commit();
    }
}
