package com.bf21.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "FOOD")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_FOOD")
    private Integer idFood;

    @Column(name = "NM_NAME")
    private String name;

    @Column(name = "NM_BRAND")
    private String brand;

    @Column(name = "NR_PORTION_SIZE")
    private Integer portionSize;

    @OneToMany(mappedBy = "food", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<FoodNutrient> nutrients;

    @Fetch(FetchMode.SUBSELECT)
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "FOOD_MACRO", joinColumns = { @JoinColumn(name = "ID_FOOD") }, inverseJoinColumns = { @JoinColumn(name = "ID_MACRO") })
    private Set<Macro> macros;

    @Column(name = "DT_CREATION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(name = "DT_MODIFICATION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;

    public void addNutrient(Nutrient nutrient, BigDecimal total) {
        if(nutrients == null) {
            nutrients = new HashSet<>();
        }

        FoodNutrient fn = new FoodNutrient();
        fn.setFood(this);
        fn.setNutrient(nutrient);
        fn.setTotal(total);

        nutrients.add(fn);
    }

    public void addMacro(Macro macro) {
        if(macros == null) {
            macros = new HashSet<>();
        }
        macros.add(macro);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Food [");
        sb.append("idFood = ").append(idFood);
        sb.append(", name = ").append(name);
        sb.append(", brand = ").append(brand);
        sb.append(", portionSize = ").append(portionSize);
        sb.append(", nutrients = ").append(nutrients);
        sb.append(", creationDate = ").append(creationDate);
        sb.append(", modificationDate = ").append(modificationDate);
        sb.append(']');
        return sb.toString();
    }
}
