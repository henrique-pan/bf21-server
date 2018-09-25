package com.bf21.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "CLIENT_GOAL")
public class ClientGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CLIENT_GOAL")
    private Integer idClientGoal;

    @Column(name = "DS_GOAL")
    private String goal;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ClientGoal [");
        sb.append("idClientGoal = ").append(idClientGoal);
        sb.append(", goal = ").append(goal);
        sb.append(']');
        return sb.toString();
    }
}
