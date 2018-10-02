package com.bf21.repository;

import com.bf21.entity.Food;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Repository
public class FoodDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Food find(Integer idFood) {
        Food food = entityManager.find(Food.class, idFood);
        return food;
    }

    public List<Food> findByName(String name) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT f FROM Food f ");
        jpql.append(" WHERE f.name = :name ");

        TypedQuery<Food> qry = entityManager.createQuery(jpql.toString(), Food.class);

        try {
            List<Food> foodList = qry.getResultList();
            return foodList;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Food> findAll() {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT f FROM Food f ");
        jpql.append(" ORDER BY f.creationDate DESC ");

        TypedQuery<Food> qry = entityManager.createQuery(jpql.toString(), Food.class);

        try {
            List<Food> foodList = qry.getResultList();
            return foodList;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Food> findAllByQuery(String query) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT f FROM Food f ");
        jpql.append(" WHERE UPPER(f.name) LIKE :name OR UPPER(f.brand) LIKE :brand ");
        jpql.append(" ORDER BY f.creationDate DESC ");

        TypedQuery<Food> qry = entityManager.createQuery(jpql.toString(), Food.class);
        qry.setParameter("name", '%' + query.toUpperCase() + '%');
        qry.setParameter("brand", '%' + query.toUpperCase() + '%');

        try {
            List<Food> foodList = qry.getResultList();
            return foodList;
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void persist(Food food) throws Exception {
        try {
            food.setCreationDate(new Date());
            entityManager.persist(food);
        } catch (Exception e) {
            throw new RuntimeException("Error to persist food: " + e.getCause().getCause().getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void merge(Food food) throws Exception {
        try {
            food.setModificationDate(new Date());
            entityManager.merge(food);
        } catch (Exception e) {
            throw new RuntimeException("Error to persist food: " + e.getCause().getCause().getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void remove(Food food) throws Exception {
        try {
            entityManager.remove(entityManager.merge(food));
        } catch (Exception e) {
            throw new RuntimeException("Error to persist food: " + e.getCause());
        }
    }

}
