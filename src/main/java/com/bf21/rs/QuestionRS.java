package com.bf21.rs;

import com.bf21.entity.Coach;
import com.bf21.entity.Question;
import com.bf21.entity.dto.CollectionDTO;
import com.bf21.entity.dto.QuestionDTO;
import com.bf21.repository.CoachDAO;
import com.bf21.repository.QuestionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/faq")
public class QuestionRS {

    @Autowired
    private CoachDAO coachDAO;

    @Autowired
    private QuestionDAO questionDAO;

    @RequestMapping(value = "/question", method = RequestMethod.GET)
    public QuestionDTO getById(@RequestParam("idQuestion") Integer idQuestion) {
        Question question = questionDAO.find(idQuestion);

        QuestionDTO result = new QuestionDTO(question);
        if(question == null) {
            result.httpStatus = HttpStatus.NOT_FOUND.value();
            result.apiMessage = "The question does not exist.";
            return result;
        }

        result.httpStatus = HttpStatus.OK.value();
        result.apiMessage = "The question has been found successfully.";

        return result;
    }

    @RequestMapping(value = "/question/list", method = RequestMethod.GET)
    public CollectionDTO<QuestionDTO> getAll(@RequestParam(name = "query", required = false) String query) {
        CollectionDTO<QuestionDTO> resultCollection = new CollectionDTO<>(new ArrayList<>());

        List<Question> questions;
        if(query != null) {
            questions = questionDAO.findAllByQuery(query);
        } else {
            questions = questionDAO.findAll();
        }

        if (!questions.isEmpty()) {
            questions.stream().forEach(question -> {
                resultCollection.add(new QuestionDTO(question));
            });

            resultCollection.httpStatus = HttpStatus.OK.value();
            resultCollection.apiMessage = "Question list found successfully.";
        } else {
            resultCollection.httpStatus = HttpStatus.NO_CONTENT.value();
        }

        return resultCollection;
    }

    @RequestMapping(value = "/question", method = RequestMethod.POST)
    public QuestionDTO createQuestion(@RequestBody Question question) throws Exception {
        QuestionDTO result;

        questionDAO.persist(question);

        result = new QuestionDTO(question);
        result.httpStatus = HttpStatus.CREATED.value();
        result.apiMessage = "The question was created successfully";

        return result;
    }

    @RequestMapping(value = "/question/{idQuestion}/answer", method = RequestMethod.POST)
    public QuestionDTO answerQuestion(@PathVariable("idQuestion") Integer idQuestion,
                                      @RequestParam("idCoach") Integer idCoach,
                                      @RequestBody Question question) throws Exception {

        QuestionDTO result;

        Question existentQuestion = questionDAO.find(idQuestion);
        if(existentQuestion == null) {
            result = new QuestionDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The question does not exist";
            return result;
        }

        Coach existingCoach = coachDAO.find(idCoach);
        if(existingCoach == null) {
            result = new QuestionDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The coach id does not exist";
            return result;
        } else {
            existentQuestion.setCoach(existingCoach);
        }

        if(question.getAnswer() == null || question.getAnswer().isEmpty()) {
            result = new QuestionDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The answer is mandatory";
            return result;
        }

        existentQuestion.setAnswer(question.getAnswer());
        questionDAO.merge(existentQuestion);

        result = new QuestionDTO(existentQuestion);
        result.httpStatus = HttpStatus.OK.value();
        result.apiMessage = "The answer was created successfully";

        return result;
    }

    @RequestMapping(value = "/question", method = RequestMethod.PUT)
    public QuestionDTO updateQuestion(@RequestBody Question question) throws Exception {
        QuestionDTO result;

        Integer idQuestion = question.getIdQuestion();
        if(idQuestion == null) {
            result = new QuestionDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The idQuestion is mandatory";
            return result;
        }

        Question existentQuestion = questionDAO.find(question.getIdQuestion());
        if(existentQuestion == null) {
            result = new QuestionDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The question does not exist";
            return result;
        }


        Coach coach = question.getCoach();
        if(coach != null) {
            Integer idCoach = coach.getIdCoach();
            if(idCoach == null) {
                result = new QuestionDTO();
                result.httpStatus = HttpStatus.BAD_REQUEST.value();
                result.apiMessage = "The idCoach is mandatory";
                return result;
            } else {
                Coach existingCoach = coachDAO.find(idCoach);
                if(existingCoach == null) {
                    result = new QuestionDTO();
                    result.httpStatus = HttpStatus.BAD_REQUEST.value();
                    result.apiMessage = "The coach id does not exist";
                    return result;
                } else {
                    question.setCoach(existingCoach);
                }
            }
        }

        question.setCreationDate(existentQuestion.getCreationDate());
        questionDAO.merge(question);

        result = new QuestionDTO(question);
        result.httpStatus = HttpStatus.OK.value();
        result.apiMessage = "The question was updated successfully";

        return result;
    }

    @RequestMapping(value = "/question/{idQuestion}", method = RequestMethod.PUT)
    public QuestionDTO setIsFAQ(@PathVariable("idQuestion") Integer idQuestion,
                                @RequestParam("idCoach") Integer idCoach,
                                      @RequestParam String isFAQ) throws Exception {

        QuestionDTO result;

        Question existentQuestion = questionDAO.find(idQuestion);
        if(existentQuestion == null) {
            result = new QuestionDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The question does not exist";
            return result;
        }

        Coach existingCoach = coachDAO.find(idCoach);
        if(existingCoach == null) {
            result = new QuestionDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The coach id does not exist";
            return result;
        }

        if(!isFAQ.equals("Y") && !isFAQ.equals("N")) {
            result = new QuestionDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The flag should be Y or N";
            return result;
        }

        existentQuestion.setIsFAQ(isFAQ);
        questionDAO.merge(existentQuestion);

        result = new QuestionDTO(existentQuestion);
        result.httpStatus = HttpStatus.OK.value();
        result.apiMessage = "The answer was created successfully";

        return result;
    }

    @RequestMapping(value = "/question", method = RequestMethod.DELETE)
    public QuestionDTO deleteQuestion(@RequestParam("idQuestion") Integer idQuestion) throws Exception {
        Question question = questionDAO.find(idQuestion);

        QuestionDTO result;

        if(question == null) {
            result = new QuestionDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The question does not exist";
            return result;
        }

        questionDAO.remove(question);

        result = new QuestionDTO(question);
        result.httpStatus = HttpStatus.ACCEPTED.value();
        result.apiMessage = "The question was removed";

        return result;
    }

}
