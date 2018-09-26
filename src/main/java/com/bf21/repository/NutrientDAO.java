package com.bf21.repository;

import com.bf21.entity.Nutrient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class NutrientDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Nutrient find(Integer idNutrient) {
        Nutrient nutrient = entityManager.find(Nutrient.class, idNutrient);
        return nutrient;
    }

    public List<Nutrient> findAll() {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT n FROM Nutrient n ");
        jpql.append(" ORDER BY c.idNutrient ");

        TypedQuery<Nutrient> qry = entityManager.createQuery(jpql.toString(), Nutrient.class);

        try {
            List<Nutrient> nutrientList = qry.getResultList();
            return nutrientList;
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void merge(Nutrient nutrient) throws Exception {
        try {
            entityManager.merge(nutrient);
        } catch (Exception e) {
            throw new RuntimeException("Error to persist nutrient: " + e.getMessage());
        }
    }

}
