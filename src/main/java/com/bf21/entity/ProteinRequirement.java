package com.bf21.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "PROTEIN_REQUIREMENT")
public class ProteinRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PROTEIN_REQUIREMENT")
    private Integer idProteinRequirement;

    @Column(name = "DS_DESCRIPTION")
    private String description;

    @Column(name = "VL_TAX")
    private BigDecimal tax;

    public ProteinRequirement() {}

    public ProteinRequirement(Integer idProteinRequirement, String description, BigDecimal tax) {
        this.idProteinRequirement = idProteinRequirement;
        this.description = description;
        this.tax = tax;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ProteinRequirement [");
        sb.append("idProteinRequirement = ").append(idProteinRequirement);
        sb.append(", description = ").append(description);
        sb.append(", tax = ").append(tax);
        sb.append(']');
        return sb.toString();
    }
}
