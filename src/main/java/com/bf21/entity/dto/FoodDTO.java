package com.bf21.entity.dto;

import com.bf21.entity.Food;
import com.bf21.entity.FoodNutrient;
import com.bf21.entity.Macro;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FoodDTO extends HttpDTO {

    private Integer idFood;
    private String name;
    private String brand;
    private Integer portionSize;
    private Set<NutrientDTO> nutrients;
    private Set<Macro> macros;

    @JsonFormat(pattern="dd/MM/yyyy hh:mm:ss")
    private Date creationDate;

    @JsonFormat(pattern="dd/MM/yyyy hh:mm:ss")
    private Date modificationDate;

    public FoodDTO() {}

    public FoodDTO(Food food) {
        if(food != null) {
            this.idFood = food.getIdFood();
            this.name = food.getName();
            this.brand = food.getBrand();
            this.portionSize = food.getPortionSize();

            Set<FoodNutrient> nutrients = food.getNutrients();
            if(nutrients != null && !nutrients.isEmpty()) {
               this.nutrients = new HashSet<>();
               for(FoodNutrient fn : nutrients) {
                    this.nutrients.add(new NutrientDTO(fn));
               }
            }

            Set<Macro> macros = food.getMacros();
            if(macros != null && !macros.isEmpty()) {
                this.macros = macros;
            }

            this.creationDate = food.getCreationDate();
            this.modificationDate = food.getModificationDate();
        }
    }
}
