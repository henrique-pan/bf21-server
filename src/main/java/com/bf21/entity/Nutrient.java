package com.bf21.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "NUTRIENT")
public class Nutrient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_NUTRIENT")
    private Integer idNutrient;

    @Column(name = "NM_NUTRIENT")
    private String nutrient;

    @Column(name = "FL_MACRO")
    private String isMacro;

    public Nutrient() {
    }

    public Nutrient(Integer idNutrient, String nutrient, boolean isMacro) {
        this.idNutrient = idNutrient;
        this.nutrient = nutrient;
        this.isMacro = isMacro ? "Y" : "N";
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Nutrient [");
        sb.append("idNutrient = ").append(idNutrient);
        sb.append(", nutrient = ").append(nutrient);
        sb.append(", isMacro = ").append(isMacro);
        sb.append(']');
        return sb.toString();
    }
}
