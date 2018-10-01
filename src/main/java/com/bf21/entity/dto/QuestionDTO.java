package com.bf21.entity.dto;

import com.bf21.entity.Coach;
import com.bf21.entity.Question;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionDTO extends HttpDTO {

    private Integer idQuestion;
    private String question;
    private String senderName;
    private String senderEmail;
    private String answer;
    private CoachDTO coach;
    private String isFAQ;

    @JsonFormat(pattern="dd/MM/yyyy hh:mm:ss")
    private Date creationDate;

    @JsonFormat(pattern="dd/MM/yyyy hh:mm:ss")
    private Date modificationDate;

    public QuestionDTO() {}

    public QuestionDTO(Question question) {
        if(question != null) {
            this.idQuestion = question.getIdQuestion();
            this.question = question.getQuestion();
            this.senderName = question.getSenderName();
            this.senderEmail = question.getSenderEmail();
            this.answer = question.getAnswer();
            if(question.getCoach() != null) {
                this.coach = new CoachDTO(question.getCoach());
            }
            this.isFAQ = question.getIsFAQ();
            this.creationDate = question.getCreationDate();
            this.modificationDate = question.getModificationDate();
        }
    }

}
