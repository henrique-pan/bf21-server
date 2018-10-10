package com.bf21.repository;

import com.bf21.entity.PlanDay;
import com.bf21.entity.PlanMeal;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Repository
public class PlanMealDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public PlanMeal find(Integer idPlanMeal) {
        PlanMeal planMeal = entityManager.find(PlanMeal.class, idPlanMeal);
        return planMeal;
    }

    public List<PlanMeal> findAllByPlanDay(PlanDay planDay) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT pm FROM PlanMeal pm ");
        jpql.append(" WHERE pm.planDay.idPlanDay = :idPlanDay ");
        jpql.append(" ORDER BY pm.creationDate DESC ");

        TypedQuery<PlanMeal> qry = entityManager.createQuery(jpql.toString(), PlanMeal.class);
        qry.setParameter("idPlanDay", planDay.getIdPlanDay());

        try {
            List<PlanMeal> planMealList = qry.getResultList();
            return planMealList;
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void merge(PlanMeal planMeal) throws Exception {
        try {
            planMeal.setModificationDate(new Date());
            entityManager.merge(planMeal);
        } catch (Exception e) {
            throw new RuntimeException("Error to persist planMeal: " + e.getCause().getCause().getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void remove(PlanMeal planMeal) throws Exception {
        try {
            StringBuilder jpql = new StringBuilder();
            jpql.append(" DELETE FROM PlanMeal pm ");
            jpql.append(" WHERE pm.idPlanMeal = :idPlanMeal ");

            Query query = entityManager.createQuery(jpql.toString());
            query.setParameter("idPlanMeal", planMeal.getIdPlanMeal());

            query.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("Error to remove planMeal: " + e.getCause());
        }
    }

}