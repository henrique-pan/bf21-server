package com.bf21.rs;

import com.bf21.entity.Coach;
import com.bf21.entity.dto.CoachDTO;
import com.bf21.repository.CoachDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginRS {

    @Autowired
    private CoachDAO coachDAO;

    @RequestMapping(method = RequestMethod.POST)
    public CoachDTO doLogin(@RequestBody Coach coach) {
        Coach existentCoach = coachDAO.findByLogin(coach.getLogin());

        CoachDTO result = new CoachDTO(existentCoach);
        if(existentCoach == null) {
            result.httpStatus = HttpStatus.UNAUTHORIZED.value();
            result.apiMessage = "Invalid login";
            return result;
        } else {
            if(!existentCoach.getPassword().equals(coach.getPassword())) {
                result.httpStatus = HttpStatus.UNAUTHORIZED.value();
                result.apiMessage = "Invalid password";
                return result;
            }
        }

        result.httpStatus = HttpStatus.OK.value();
        result.apiMessage = "The coach has been found successfully.";

        return result;
    }

}
