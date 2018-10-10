package com.bf21.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "FOOD_PLAN")
public class FoodPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_FOOD_PLAN")
    private Integer idFoodPlan;

    @Column(name = "NM_PLAN")
    private String planName;

    @OneToOne
    @JoinColumn(name = "ID_CLIENT")
    private Client client;

    @OneToOne
    @JoinColumn(name = "ID_COACH")
    private Coach coach;

    @OneToMany(mappedBy = "foodPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlanDay> planDays;

    @Column(name = "DT_CREATION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(name = "DT_MODIFICATION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;

    public void addPlanDay(PlanDay planDay) {
        if (planDays == null) {
            planDays = new ArrayList<>();
        }
        planDays.add(planDay);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FoodPlan [");
        sb.append("idFoodPlan = ").append(idFoodPlan);
        sb.append(", planName = ").append(planName);
        sb.append(", client = ").append(client.getEmail());
        sb.append(", coach = ").append(coach.getLogin());
        sb.append(", planDays = ").append(planDays);
        sb.append(", creationDate = ").append(creationDate);
        if (modificationDate != null) sb.append(", modificationDate = ").append(modificationDate);
        sb.append(']');
        return sb.toString();
    }
}
