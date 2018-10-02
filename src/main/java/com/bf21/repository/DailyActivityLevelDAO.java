package com.bf21.repository;

import com.bf21.entity.DailyActivityLevel;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class DailyActivityLevelDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public DailyActivityLevel find(Integer idDailyActivityLevel) {
        DailyActivityLevel dailyActivityLevel = entityManager.find(DailyActivityLevel.class, idDailyActivityLevel);
        return dailyActivityLevel;
    }

    public List<DailyActivityLevel> findAll() {
        StringBuilder jpql = new StringBuilder();
        jpql.append(" SELECT dal FROM DailyActivityLevel dal ");
        jpql.append(" ORDER BY dal.idDailyActivityLevel ");

        TypedQuery<DailyActivityLevel> qry = entityManager.createQuery(jpql.toString(), DailyActivityLevel.class);

        try {
            List<DailyActivityLevel> dailyActivityLevelList = qry.getResultList();
            return dailyActivityLevelList;
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void merge(DailyActivityLevel dailyActivityLevel) throws Exception {
        try {
            entityManager.merge(dailyActivityLevel);
        } catch (Exception e) {
            throw new Exception("Error to persist daily activity level: " + e.getMessage());
        }
    }

}
