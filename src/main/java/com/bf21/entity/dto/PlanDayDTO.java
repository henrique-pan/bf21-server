package com.bf21.entity.dto;

import com.bf21.entity.*;
import com.bf21.utils.DateDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlanDayDTO extends HttpDTO {

    private Integer idPlanDay;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @JsonDeserialize(using = DateDeserializer.class)
    private Date day;

    private List<PlanMealDTO> planMeals;

    @JsonFormat(pattern="dd/MM/yyyy hh:mm:ss")
    private Date creationDate;

    @JsonFormat(pattern="dd/MM/yyyy hh:mm:ss")
    private Date modificationDate;

    public PlanDayDTO() {}

    public PlanDayDTO(PlanDay planDay) {
        if(planDay != null) {
            this.idPlanDay = planDay.getIdPlanDay();
            this.day = planDay.getDay();

            List<PlanMeal> planMeals = planDay.getPlanMeals();
            if(planMeals != null && !planMeals.isEmpty()) {
                this.planMeals = new LinkedList<>();
                for(PlanMeal pm : planMeals) {
                    this.planMeals.add(new PlanMealDTO(pm));
                }
            }

            this.creationDate = planDay.getCreationDate();
            this.modificationDate = planDay.getModificationDate();
        }
    }

}
