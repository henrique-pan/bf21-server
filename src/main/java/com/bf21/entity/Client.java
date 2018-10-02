package com.bf21.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "CLIENT")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CLIENT")
    private Integer idClient;

    @Column(name = "NM_NAME")
    private String name;

    @Column(name = "NR_AGE")
    private Integer age;

    @Column(name = "FL_GENDER")
    private String gender;

    @Column(name = "DS_EMAIL")
    private String email;

    @Column(name = "DS_PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "VL_HEIGHT")
    private BigDecimal height;

    @Column(name = "VL_WEIGHT")
    private BigDecimal weight;

    @Column(name = "NR_BODY_FAT_PERCENTAGE")
    private Integer bodyFatPercentage;

    @Column(name = "NR_BMR")
    private Integer bmr;

    @Column(name = "NR_TDCE")
    private Integer tdce;

    @OneToOne
    @JoinColumn(name = "ID_CLIENT_GOAL")
    private ClientGoal clientGoal;

    @OneToOne
    @JoinColumn(name = "ID_DAILY_ACTIVITY_LEVEL")
    private DailyActivityLevel activityLevel;

    @OneToOne
    @JoinColumn(name = "ID_PROTEIN_REQUIREMENT")
    private ProteinRequirement proteinRequirement;

    @Column(name = "DT_CREATION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(name = "DT_MODIFICATION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Client [");
        sb.append("idClient = ").append(idClient);
        sb.append(", name = ").append(name);
        sb.append(", age = ").append(age);
        sb.append(", gender = ").append(gender);
        sb.append(", email = ").append(email);
        sb.append(", phoneNumber = ").append(phoneNumber);
        sb.append(", height = ").append(height);
        sb.append(", weight = ").append(weight);
        sb.append(", bodyFatPercentage = ").append(bodyFatPercentage);
        sb.append(", bmr = ").append(bmr);
        sb.append(", tdce = ").append(tdce);
        sb.append(", clientGoal = ").append(clientGoal.getGoal());
        sb.append(", activityLevel = ").append(activityLevel.getDescription());
        sb.append(", proteinRequirement = ").append(proteinRequirement.getDescription());
        sb.append(", creationDate = ").append(creationDate);
        sb.append(", modificationDate = ").append(modificationDate);
        sb.append(']');
        return sb.toString();
    }
}
