package com.bf21.repository;

import com.bf21.entity.ProteinRequirement;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ProteinRequirementDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public ProteinRequirement find(Integer idProteinRequirement) {
        ProteinRequirement proteinRequirement = entityManager.find(ProteinRequirement.class, idProteinRequirement);
        return proteinRequirement;
    }

    public List<ProteinRequirement> findAll() {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT pr FROM ProteinRequirement pr ");
        jpql.append(" ORDER BY pr.idProteinRequirement ");

        TypedQuery<ProteinRequirement> qry = entityManager.createQuery(jpql.toString(), ProteinRequirement.class);

        try {
            List<ProteinRequirement> proteinRequirementList = qry.getResultList();
            return proteinRequirementList;
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void merge(ProteinRequirement proteinRequirement) throws Exception {
        try {
            entityManager.merge(proteinRequirement);
        } catch (Exception e) {
            throw new Exception("Error to persist protein requirement: " + e.getMessage());
        }
    }

}
