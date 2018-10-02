package com.bf21.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "DAILY_ACTIVITY_LEVEL")
public class DailyActivityLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DAILY_ACTIVITY_LEVEL")
    private Integer idDailyActivityLevel;

    @Column(name = "DS_DESCRIPTION")
    private String description;

    @Column(name = "VL_TAX")
    private BigDecimal tax;

    public DailyActivityLevel() {}

    public DailyActivityLevel(Integer idDailyActivityLevel, String description, BigDecimal tax) {
        this.idDailyActivityLevel = idDailyActivityLevel;
        this.description = description;
        this.tax = tax;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DailyActivityLevel [");
        sb.append("idDailyActivityLevel = ").append(idDailyActivityLevel);
        sb.append(", description = ").append(description);
        sb.append(", tax = ").append(tax);
        sb.append(']');
        return sb.toString();
    }
}
