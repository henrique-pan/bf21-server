package com.bf21.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "FOOD_PLAN_DAY")
public class PlanDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_FOOD_PLAN_DAY")
    private Integer idPlanDay;

    @ManyToOne
    @JoinColumn(name = "ID_FOOD_PLAN")
    private FoodPlan foodPlan;

    @Column(name = "DT_DAY")
    @Temporal(TemporalType.DATE)
    private Date day;

    @OneToMany(mappedBy = "planDay", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlanMeal> planMeals;

    @Column(name = "DT_CREATION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(name = "DT_MODIFICATION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;

    public void addPlanMeal(PlanMeal planMeal) {
        if (planMeals == null) {
            planMeals = new LinkedList<>();
        }
        planMeals.add(planMeal);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("[");
        sb.append("idPlanDay = ").append(idPlanDay);
        sb.append(", foodPlan = ").append(foodPlan.getPlanName());
        sb.append(", day = ").append(day);
        sb.append(", planMeals = ").append(planMeals);
        sb.append(", creationDate = ").append(creationDate);
        if (modificationDate != null) sb.append(", modificationDate = ").append(modificationDate);
        sb.append(']');
        return sb.toString();
    }
}
