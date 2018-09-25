package com.bf21.rs;

import com.bf21.entity.Coach;
import com.bf21.entity.dto.CoachDTO;
import com.bf21.entity.dto.CollectionDTO;
import com.bf21.repository.CoachDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/coach")
public class CoachRS {

    @Autowired
    private CoachDAO coachDAO;

    @RequestMapping(method = RequestMethod.GET)
    public CoachDTO getById(@RequestParam("idCoach") Integer idCoach) {
        Coach coach = coachDAO.find(idCoach);

        CoachDTO result = new CoachDTO(coach);
        if(coach == null) {
            result.httpStatus = HttpStatus.NOT_FOUND.value();
            result.apiMessage = "The coach does not exist.";
            return result;
        }

        result.httpStatus = HttpStatus.OK.value();
        result.apiMessage = "The coach has been found successfully.";

        return result;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CollectionDTO<CoachDTO> getAll(@RequestParam(name = "query", required = false) String query) {
        CollectionDTO<CoachDTO> resultCollection = new CollectionDTO<>(new ArrayList<>());

        List<Coach> coaches;
        if(query != null) {
            coaches = coachDAO.findAllByQuery(query);
        } else {
            coaches = coachDAO.findAll();
        }

        if (!coaches.isEmpty()) {
            coaches.stream().forEach(coach -> {
                resultCollection.add(new CoachDTO(coach));
            });

            resultCollection.httpStatus = HttpStatus.OK.value();
            resultCollection.apiMessage = "Coach list found successfully.";
        } else {
            resultCollection.httpStatus = HttpStatus.NO_CONTENT.value();
        }

        return resultCollection;
    }

    @RequestMapping(method = RequestMethod.POST)
    public CoachDTO createCoach(@RequestBody Coach coach) throws Exception {
        CoachDTO result;

        coachDAO.persist(coach);

        result = new CoachDTO(coach);
        result.httpStatus = HttpStatus.CREATED.value();
        result.apiMessage = "The coach was created successfully";

        return result;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public CoachDTO updateCoach(@RequestBody Coach coach) throws Exception {
        CoachDTO result;

        Integer idCoach = coach.getIdCoach();
        if(idCoach == null) {
            result = new CoachDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The idCoach is mandatory";
            return result;
        }

        Coach existentCoach = coachDAO.find(coach.getIdCoach());
        if(existentCoach == null) {
            result = new CoachDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The coach does not exist";
            return result;
        }

        coach.setCreationDate(existentCoach.getCreationDate());
        coachDAO.merge(coach);

        result = new CoachDTO(coach);
        result.httpStatus = HttpStatus.OK.value();
        result.apiMessage = "The coach was updated successfully";

        return result;
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public CoachDTO deleteCoach(@RequestParam("idCoach") Integer idCoach) throws Exception {
        Coach coach = coachDAO.find(idCoach);

        CoachDTO result;

        if(coach == null) {
            result = new CoachDTO();
            result.httpStatus = HttpStatus.BAD_REQUEST.value();
            result.apiMessage = "The coach does not exist";
            return result;
        }

        coachDAO.remove(coach);

        result = new CoachDTO(coach);
        result.httpStatus = HttpStatus.ACCEPTED.value();
        result.apiMessage = "The coach was removed";

        return result;
    }

}
