package com.bf21.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "MACRO")
public class Macro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_MACRO")
    private Integer idMacro;

    @Column(name = "NM_MACRO")
    private String macro;

    public Macro() {
    }

    public Macro(Integer idMacro, String macro) {
        this.idMacro = idMacro;
        this.macro = macro;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Macro [");
        sb.append("idMacro = ").append(idMacro);
        sb.append(", macro = ").append(macro);
        sb.append(']');
        return sb.toString();
    }
}
