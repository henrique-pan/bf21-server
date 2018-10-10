package com.bf21.repository;

import com.bf21.entity.Client;
import com.bf21.entity.Coach;
import com.bf21.entity.FoodPlan;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Repository
public class FoodPlanDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public FoodPlan find(Integer idFoodPlan) {
        FoodPlan foodPlan = entityManager.find(FoodPlan.class, idFoodPlan);
        return foodPlan;
    }

    public List<FoodPlan> findAll() {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT fp FROM FoodPlan fp ");
        jpql.append(" ORDER BY fp.creationDate DESC ");

        TypedQuery<FoodPlan> qry = entityManager.createQuery(jpql.toString(), FoodPlan.class);

        try {
            List<FoodPlan> foodPlanList = qry.getResultList();
            return foodPlanList;
        } catch (Exception e) {
            return null;
        }
    }

    public List<FoodPlan> findAllByCoach(Coach coach) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT fp FROM FoodPlan fp ");
        jpql.append(" WHERE fp.coach.idCoach = :idCoach ");
        jpql.append(" ORDER BY fp.creationDate DESC ");

        TypedQuery<FoodPlan> qry = entityManager.createQuery(jpql.toString(), FoodPlan.class);
        qry.setParameter("idCoach", coach.getIdCoach());

        try {
            List<FoodPlan> foodPlanList = qry.getResultList();
            return foodPlanList;
        } catch (Exception e) {
            return null;
        }
    }

    public List<FoodPlan> findAllByClient(Client client) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT fp FROM FoodPlan fp ");
        jpql.append(" WHERE fp.client.idClient = :idClient ");
        jpql.append(" ORDER BY fp.creationDate DESC ");

        TypedQuery<FoodPlan> qry = entityManager.createQuery(jpql.toString(), FoodPlan.class);
        qry.setParameter("idClient", client.getIdClient());

        try {
            List<FoodPlan> foodPlanList = qry.getResultList();
            return foodPlanList;
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void persist(FoodPlan foodPlan) throws Exception {
        try {
            foodPlan.setCreationDate(new Date());
            entityManager.persist(foodPlan);
        } catch (Exception e) {
            throw new RuntimeException("Error to persist foodPlan: " + e.getCause().getCause().getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void merge(FoodPlan foodPlan) throws Exception {
        try {
            foodPlan.setModificationDate(new Date());
            entityManager.merge(foodPlan);
        } catch (Exception e) {
            throw new RuntimeException("Error to persist foodPlan: " + e.getCause().getCause().getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void remove(FoodPlan foodPlan) throws Exception {
        try {
            entityManager.remove(entityManager.merge(foodPlan));
        } catch (Exception e) {
            throw new RuntimeException("Error to persist foodPlan: " + e.getCause());
        }
    }

}