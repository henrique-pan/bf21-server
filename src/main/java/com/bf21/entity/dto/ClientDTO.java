package com.bf21.entity.dto;

import com.bf21.entity.Client;
import com.bf21.entity.ClientGoal;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientDTO extends HttpDTO {

    private Integer idClient;
    private String name;
    private Integer age;
    private String email;
    private String phoneNumber;
    private BigDecimal height;
    private BigDecimal weight;
    private Integer bodyFatPercentage;
    private ClientGoal clientGoal;
    private Integer tdee;

    @JsonFormat(pattern="dd/MM/yyyy hh:mm:ss")
    private Date creationDate;

    @JsonFormat(pattern="dd/MM/yyyy hh:mm:ss")
    private Date modificationDate;

    public ClientDTO() {}

    public ClientDTO(Client client) {
        if(client != null) {
            this.idClient = client.getIdClient();
            this.name = client.getName();
            this.age = client.getAge();
            this.email = client.getEmail();
            this.phoneNumber = client.getPhoneNumber();
            this.height = client.getHeight();
            this.weight = client.getWeight();
            this.bodyFatPercentage = client.getBodyFatPercentage();
            this.clientGoal = client.getClientGoal();
            this.tdee = client.getTdee();
            this.creationDate = client.getCreationDate();
            this.modificationDate = client.getModificationDate();
        }
    }

}
