package com.bf21.entity.dto;

import com.bf21.entity.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlanMealDTO extends HttpDTO {

    private Integer idPlanMeal;
    private String name;
    private Integer order;

    private List<FoodDTO> foods;

    @JsonFormat(pattern="dd/MM/yyyy hh:mm:ss")
    private Date creationDate;

    @JsonFormat(pattern="dd/MM/yyyy hh:mm:ss")
    private Date modificationDate;

    public PlanMealDTO() {}

    public PlanMealDTO(PlanMeal planMeal) {
        if(planMeal != null) {
            this.idPlanMeal = planMeal.getIdPlanMeal();
            this.name = planMeal.getName();
            this.order = planMeal.getOrder();

            List<Food> foods = planMeal.getFoods();
            if(foods != null && !foods.isEmpty()) {
                this.foods = new LinkedList<>();
                for(Food f : foods) {
                    this.foods.add(new FoodDTO(f));
                }
            }

            this.creationDate = planMeal.getCreationDate();
            this.modificationDate = planMeal.getModificationDate();
        }
    }

}
