package io.pivotal.services.dataTx.geode.health.api.dao;

import io.pivotal.services.dataTx.geode.health.api.dao.entity.StatEntity;
import lombok.Builder;
import lombok.Setter;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * Save statistics to database
 * @author Gregory Green, Nyla Green
 */
@Builder
@Setter
public class StatDao implements Closeable
{
    private  EntityManager entityManager;

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



    public void saveAll(Collection<StatEntity> entities)
    {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        for (StatEntity entity: entities)
        {
           entityManager.persist(entity);
        }

        entityManager.flush();
        transaction.commit();
    }//-------------------------------------------

    @Override
    public void close()
    throws IOException
    {
        if(this.entityManager != null)
            this.entityManager.close();
    }
}
