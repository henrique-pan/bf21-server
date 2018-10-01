package com.bf21.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "QUESTION")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_QUESTION")
    private Integer idQuestion;

    @Column(name = "DS_QUESTION", columnDefinition = "TEXT")
    private String question;

    @Column(name = "NM_SENDER_NAME")
    private String senderName;

    @Column(name = "DS_SENDER_EMAIL")
    private String senderEmail;

    @Column(name = "DS_ANSWER", columnDefinition = "TEXT")
    private String answer;

    @OneToOne
    @JoinColumn(name = "ID_COACH")
    private Coach coach;

    @Column(name = "FL_FAQ")
    private String isFAQ;

    @Column(name = "DT_CREATION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(name = "DT_MODIFICATION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Question [");
        sb.append("idQuestion = ").append(idQuestion);
        sb.append(", question = ").append(question);
        sb.append(", senderName = ").append(senderName);
        sb.append(", senderEmail = ").append(senderEmail);
        sb.append(", answer = ").append(answer);
        sb.append(", coach = ").append(coach);
        sb.append(", isFAQ = ").append(isFAQ);
        sb.append(", creationDate = ").append(creationDate);
        sb.append(", modificationDate = ").append(modificationDate);
        sb.append(']');
        return sb.toString();
    }
}
