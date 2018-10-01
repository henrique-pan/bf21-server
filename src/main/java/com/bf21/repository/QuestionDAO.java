package com.bf21.repository;

import com.bf21.entity.Client;
import com.bf21.entity.Question;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Repository
public class QuestionDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Question find(Integer idQuestion) {
        Question question = entityManager.find(Question.class, idQuestion);
        return question;
    }

    public List<Question> findAll() {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT q FROM Question q ");
        jpql.append(" ORDER BY q.creationDate DESC ");

        TypedQuery<Question> qry = entityManager.createQuery(jpql.toString(), Question.class);

        try {
            List<Question> questionList = qry.getResultList();
            return questionList;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Question> findAllByQuery(String query) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT q FROM Question q ");
        jpql.append(" WHERE UPPER(q.question) LIKE :question OR UPPER(q.senderName) LIKE :senderName OR UPPER(q.senderEmail) LIKE :senderEmail ");
        jpql.append(" ORDER BY q.creationDate DESC ");

        TypedQuery<Question> qry = entityManager.createQuery(jpql.toString(), Question.class);
        qry.setParameter("question", '%' + query.toUpperCase() + '%');
        qry.setParameter("senderName", '%' + query.toUpperCase() + '%');
        qry.setParameter("senderEmail", '%' + query.toUpperCase() + '%');

        try {
            List<Question> questionList = qry.getResultList();
            return questionList;
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void persist(Question question) throws Exception {
        try {
            question.setCreationDate(new Date());
            question.setIsFAQ("N");
            entityManager.persist(question);
        } catch (Exception e) {
            throw new RuntimeException("Error to persist question: " + e.getCause().getCause().getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void merge(Question question) throws Exception {
        try {
            question.setModificationDate(new Date());
            entityManager.merge(question);
        } catch (Exception e) {
            throw new RuntimeException("Error to persist question: " + e.getCause().getCause().getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void remove(Question question) throws Exception {
        try {
            entityManager.remove(entityManager.merge(question));
        } catch (Exception e) {
            throw new RuntimeException("Error to persist question: " + e.getCause());
        }
    }

}
