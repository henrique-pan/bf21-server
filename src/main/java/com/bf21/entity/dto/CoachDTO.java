package com.bf21.entity.dto;

import com.bf21.entity.Coach;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CoachDTO extends HttpDTO {

    private Integer idCoach;

    private String name;

    private String login;

    private String password;

    private String email;

    private String phoneNumber;

    @JsonFormat(pattern="dd/MM/yyyy hh:mm:ss")
    private Date creationDate;

    @JsonFormat(pattern="dd/MM/yyyy hh:mm:ss")
    private Date modificationDate;

    public CoachDTO() {}

    public CoachDTO(Coach coach) {
        if(coach != null) {
            this.idCoach = coach.getIdCoach();
            this.name = coach.getName();
            this.login = coach.getLogin();
            this.password = coach.getPassword();
            this.email = coach.getEmail();
            this.phoneNumber = coach.getPhoneNumber();
            this.creationDate = coach.getCreationDate();
            this.modificationDate = coach.getModificationDate();
        }
    }

}
