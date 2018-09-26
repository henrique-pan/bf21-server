package com.bf21.repository;

import com.bf21.entity.Macro;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class MacroDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Macro find(Integer idMacro) {
        Macro macro = entityManager.find(Macro.class, idMacro);
        return macro;
    }

    public List<Macro> findAll() {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT m FROM Macro m ");
        jpql.append(" ORDER BY m.idMacro ");

        TypedQuery<Macro> qry = entityManager.createQuery(jpql.toString(), Macro.class);

        try {
            List<Macro> macroList = qry.getResultList();
            return macroList;
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void merge(Macro macro) throws Exception {
        try {
            entityManager.merge(macro);
        } catch (Exception e) {
            throw new Exception("Error to persist macro: " + e.getMessage());
        }
    }

}
