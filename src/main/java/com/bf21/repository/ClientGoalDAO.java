package com.bf21.repository;

import com.bf21.entity.Client;
import com.bf21.entity.ClientGoal;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ClientGoalDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public ClientGoal find(Integer idClientGoal) {
        ClientGoal clientGoal = entityManager.find(ClientGoal.class, idClientGoal);
        return clientGoal;
    }

    public List<ClientGoal> findAll() {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT cg FROM ClientGoal cg ");
        jpql.append(" ORDER BY cg.idClientGoal DESC ");

        TypedQuery<ClientGoal> qry = entityManager.createQuery(jpql.toString(), ClientGoal.class);

        try {
            List<ClientGoal> clientGoalList = qry.getResultList();
            return clientGoalList;
        } catch (Exception e) {
            return null;
        }
    }

}
