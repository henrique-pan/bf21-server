package com.bf21.entity.dto;


import com.bf21.entity.FoodNutrient;
import com.bf21.entity.Nutrient;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NutrientDTO extends HttpDTO {

    private Integer idNutrient;
    private String name;
    private Boolean isMacro;
    private BigDecimal total;

    public NutrientDTO() {}

    public NutrientDTO(Nutrient nutrient) {
        if(nutrient != null) {
            this.idNutrient = nutrient.getIdNutrient();
            this.name = nutrient.getNutrient();
            this.isMacro = nutrient.getIsMacro().equalsIgnoreCase("Y");
        }
    }

    public NutrientDTO(FoodNutrient foodNutrient) {
        if(foodNutrient != null) {
            Nutrient nutrient = foodNutrient.getNutrient();
            this.idNutrient = nutrient.getIdNutrient();
            this.name = nutrient.getNutrient();
            this.isMacro = nutrient.getIsMacro().equalsIgnoreCase("Y");
            this.total = foodNutrient.getTotal();
        }
    }
}
