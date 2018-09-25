package com.bf21.repository;

import com.bf21.entity.Client;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Date;
import java.util.List;

@Repository
public class ClientDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Client find(Integer idClient) {
        Client client = entityManager.find(Client.class, idClient);
        return client;
    }

    public List<Client> findAll() {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT c FROM Client c ");
        jpql.append(" ORDER BY c.creationDate DESC ");

        TypedQuery<Client> qry = entityManager.createQuery(jpql.toString(), Client.class);

        try {
            List<Client> clientList = qry.getResultList();
            return clientList;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Client> findAllByQuery(String query) {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT c FROM Client c ");
        jpql.append(" WHERE UPPER(c.name) LIKE :name OR UPPER(c.phoneNumber) LIKE :phoneNumber OR UPPER(c.email) LIKE :email ");
        jpql.append(" ORDER BY c.creationDate DESC ");

        TypedQuery<Client> qry = entityManager.createQuery(jpql.toString(), Client.class);
        qry.setParameter("name", '%' + query.toUpperCase() + '%');
        qry.setParameter("phoneNumber", '%' + query.toUpperCase() + '%');
        qry.setParameter("email", '%' + query.toUpperCase() + '%');

        try {
            List<Client> clientList = qry.getResultList();
            return clientList;
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void persist(Client client) throws Exception {
        try {
            client.setCreationDate(new Date());
            entityManager.persist(client);
        } catch (Exception e) {
            throw new RuntimeException("Error to persist client: " + e.getCause().getCause().getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void merge(Client client) throws Exception {
        try {
            client.setModificationDate(new Date());
            entityManager.merge(client);
        } catch (Exception e) {
            throw new RuntimeException("Error to persist client: " + e.getCause().getCause().getMessage());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void remove(Client client) throws Exception {
        try {
            entityManager.remove(entityManager.merge(client));
        } catch (Exception e) {
            throw new RuntimeException("Error to persist client: " + e.getCause());
        }
    }

}
