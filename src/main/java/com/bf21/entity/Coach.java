package com.bf21.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "COACH")
public class Coach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_COACH")
    private Integer idCoach;

    @Column(name = "NM_NAME")
    private String name;

    @Column(name = "DS_LOGIN")
    private String login;

    @Column(name = "DS_PASSWORD")
    private String password;

    @Column(name = "DS_EMAIL")
    private String email;

    @Column(name = "DS_PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "DT_CREATION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(name = "DT_MODIFICATION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Coach [");
        sb.append("idCoach = ").append(idCoach);
        sb.append(", name = ").append(name);
        sb.append(", login = ").append(login);
        sb.append(", password = ").append(password);
        sb.append(", email = ").append(email);
        sb.append(", phoneNumber = ").append(phoneNumber);
        sb.append(", creationDate = ").append(creationDate);
        sb.append(", modificationDate = ").append(modificationDate);
        sb.append(']');
        return sb.toString();
    }
}
