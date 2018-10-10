package com.bf21.entity.dto;

import com.bf21.entity.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import java.util.*;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FoodPlanDTO extends HttpDTO {

    private Integer idFoodPlan;
    private String planName;
    private ClientDTO client;
    private CoachDTO coach;
    private List<PlanDayDTO> planDays;

    @JsonFormat(pattern="dd/MM/yyyy hh:mm:ss")
    private Date creationDate;

    @JsonFormat(pattern="dd/MM/yyyy hh:mm:ss")
    private Date modificationDate;

    public FoodPlanDTO() {}

    public FoodPlanDTO(FoodPlan foodPlan) {
        if(foodPlan != null) {
            this.idFoodPlan = foodPlan.getIdFoodPlan();
            this.planName = foodPlan.getPlanName();
            this.client = new ClientDTO(foodPlan.getClient());
            this.coach = new CoachDTO(foodPlan.getCoach());

            List<PlanDay> planDays = foodPlan.getPlanDays();
            if(planDays != null && !planDays.isEmpty()) {
                this.planDays = new LinkedList<>();
                for(PlanDay pd : planDays) {
                    this.planDays.add(new PlanDayDTO(pd));
                }
            }

            this.creationDate = foodPlan.getCreationDate();
            this.modificationDate = foodPlan.getModificationDate();
        }
    }

}
