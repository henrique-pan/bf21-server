package com.bf21.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "FOOD_PLAN_MEAL")
public class PlanMeal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_FOOD_PLAN_MEAL")
    private Integer idPlanMeal;

    @ManyToOne
    @JoinColumn(name = "ID_FOOD_PLAN_DAY")
    private PlanDay planDay;

    @Column(name = "NM_NAME")
    private String name;

    @Column(name = "NR_ORDER")
    private Integer order;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "FOOD_PLAN_MEAL_FOOD", joinColumns = {@JoinColumn(name = "ID_FOOD_PLAN_MEAL")}, inverseJoinColumns = {@JoinColumn(name = "ID_FOOD")})
    private List<Food> foods;

    @Column(name = "DT_CREATION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(name = "DT_MODIFICATION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;

    public void addFood(Food food) {
        if (foods == null) {
            foods = new LinkedList<>();
        }
        foods.add(food);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("[");
        sb.append("idPlanMeal = ").append(idPlanMeal);
        sb.append(", planDay = ").append(planDay.getDay());
        sb.append(", name = ").append(name);
        sb.append(", order = ").append(order);
        if (foods != null) sb.append(", foods = ").append(foods);
        sb.append(", creationDate = ").append(creationDate);
        if (modificationDate != null) sb.append(", modificationDate = ").append(modificationDate);
        sb.append(']');
        return sb.toString();
    }
}
