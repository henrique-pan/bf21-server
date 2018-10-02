package com.bf21.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "FOOD_NUTRIENT")
public class FoodNutrient implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "ID_FOOD")
    private Food food;

    @Id
    @ManyToOne
    @JoinColumn(name = "ID_NUTRIENT")
    private Nutrient nutrient;

    @Column(name = "VL_TOTAL")
    private BigDecimal total;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("[");
        sb.append("nutrient = ").append(nutrient.getNutrient());
        sb.append(", total = ").append(total);
        sb.append(']');
        return sb.toString();
    }
}
