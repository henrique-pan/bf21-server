package com.bf21.repository;

import com.bf21.entity.FoodPlan;
import com.bf21.entity.PlanDay;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Repository
public class PlanDayDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public PlanDay find(Integer idPlanDay) {
        PlanDay planDay = entityManager.find(PlanDay.class, idPlanDay);
        return planDay;
    }

    public List<PlanDay> findAllByPlan(FoodPlan foodPlan) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT pd FROM PlanDay pd ");
        jpql.append(" WHERE pd.foodPlan.idFoodPlan = :idFoodPlan ");
        jpql.append(" ORDER BY pd.creationDate DESC ");

        TypedQuery<PlanDay> qry = entityManager.createQuery(jpql.toString(), PlanDay.class);
        qry.setParameter("idFoodPlan", foodPlan.getIdFoodPlan());

        try {
            List<PlanDay> planDayList = qry.getResultList();
            return planDayList;
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void merge(PlanDay planDay) throws Exception {
        try {
            planDay.setModificationDate(new Date());
            entityManager.merge(planDay);
        } catch (Exception e) {
            throw new RuntimeException("Error to persist planDay: " + e.getCause().getCause().getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void remove(PlanDay planDay) throws Exception {
        try {
            entityManager.remove(entityManager.merge(planDay));
        } catch (Exception e) {
            throw new RuntimeException("Error to persist planDay: " + e.getCause());
        }
    }

}