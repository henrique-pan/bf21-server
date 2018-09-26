package com.bf21.repository;

import com.bf21.entity.Coach;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Repository
public class CoachDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Coach find(Integer idCoach) {
        Coach coach = entityManager.find(Coach.class, idCoach);
        return coach;
    }

    public Coach findByLogin(String login) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT c FROM Coach c ");
        jpql.append(" WHERE c.login = :login ");

        TypedQuery<Coach> qry = entityManager.createQuery(jpql.toString(), Coach.class);
        qry.setParameter("login", login);

        try {
            Coach coach = qry.getSingleResult();
            return coach;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Coach> findAll() {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT c FROM Coach c ");
        jpql.append(" ORDER BY c.creationDate DESC ");

        TypedQuery<Coach> qry = entityManager.createQuery(jpql.toString(), Coach.class);

        try {
            List<Coach> coachList = qry.getResultList();
            return coachList;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Coach> findAllByQuery(String query) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT c FROM Coach c ");
        jpql.append(" WHERE UPPER(c.name) LIKE :name OR UPPER(c.phoneNumber) LIKE :phoneNumber OR UPPER(c.email) LIKE :email ");
        jpql.append(" ORDER BY c.creationDate DESC ");

        TypedQuery<Coach> qry = entityManager.createQuery(jpql.toString(), Coach.class);
        qry.setParameter("name", '%' + query.toUpperCase() + '%');
        qry.setParameter("phoneNumber", '%' + query.toUpperCase() + '%');
        qry.setParameter("email", '%' + query.toUpperCase() + '%');

        try {
            List<Coach> coachList = qry.getResultList();
            return coachList;
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void persist(Coach coach) throws Exception {
        try {
            coach.setCreationDate(new Date());
            entityManager.persist(coach);
        } catch (Exception e) {
            throw new RuntimeException("Error to persist coach: " + e.getCause().getCause().getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void merge(Coach coach) throws Exception {
        try {
            coach.setModificationDate(new Date());
            entityManager.merge(coach);
        } catch (Exception e) {
            throw new RuntimeException("Error to persist coach: " + e.getCause().getCause().getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void remove(Coach coach) throws Exception {
        try {
            entityManager.remove(entityManager.merge(coach));
        } catch (Exception e) {
            throw new RuntimeException("Error to persist coach: " + e.getCause());
        }
    }

}
